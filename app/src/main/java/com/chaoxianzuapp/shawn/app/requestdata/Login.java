package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.db.SqlLite;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by shawn on 2015-08-22.
 */
public class Login extends Thread {

    private Handler handler = new Handler();

    private boolean flag = true;

    private boolean success = true;

    private int result = 1;

    private String mb_id;

    private String mb_password;

    private Context context;

    private Activity activity;

    private boolean auto_login_checked = false;

    private boolean auto_login = true;//로그인 창에서(false) , 앱 부팅시(true)

    /*public Login(Context context, String mb_id, String mb_password, boolean auto_login_checked) {//로그인 창에서 사용

        this(context, mb_id, mb_password);

        DialogBase.setProgress(context, "로그인 중..");

        this.auto_login_checked = auto_login_checked;

        this.auto_login = false;
    }*/
    public Login(Context context, Activity activity, String mb_id, String mb_password, boolean auto_login_checked) {//로그인 창에서 사용

        this(context, mb_id, mb_password);

        DialogBase.setProgress(0, context, "로그인 중..");

        this.auto_login_checked = auto_login_checked;

        this.auto_login = false;

        this.activity = activity;
    }
    public Login(Context context, String mb_id, String mb_password) {//부팅 자동 로그인시

        this.context = context;
        this.mb_id = mb_id;
        this.mb_password = mb_password;
    }
    @Override
    public void run() {
        Log.d(BasicInfo.SQLLIST_TAG, "RequestMonetaLogin:-------2-------: :");
        this.get();
    }
    public synchronized void get() {

        if (this.flag == false) {
            try {
                super.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.flag = false;

        try {

            StringBuilder out = new StringBuilder("mb_id=");

            out.append(mb_id);
            out.append("&mb_password=");
            out.append(mb_password);
            out.append("&DEVICE_TYPE=");
            out.append(BasicInfo.DEVICE_TYPE);
            out.append("&APP_VERSION=");
            out.append(BasicInfo.APP_VERSION);
            out.append("&DEVICE_ID=");
            out.append(BasicInfo.DEVICE_ID);

            String res = RemoteDB.getHttpJsonData(BasicInfo.URI_LOGIN, out.toString());

            if (!"".equalsIgnoreCase(res)) {//loginText

                jsonParser(res);
            } else {

                success = false;
            }
        } catch (Exception e) {

            success = false;

            e.printStackTrace();
        }

        if (success == false) {

            handlerPlayer((auto_login == false ? "로그인을 실패하였습니다." : "자동 로그인을 실패하였습니다."));
        }
    }
    public void jsonParser(final String res) {

        handler.post(new Runnable() {//핸들러가 순서대로 내부적으로 처리(메인스레드에서 )
            @Override
            public void run() {
                set(res);
            }
        });
    }
    public void handlerPlayer(final String msg) {

         handler.post(new Runnable() {//핸들러가 순서대로 내부적으로 처리(메인스레드에서 )
             @Override
             public void run() {

                 if (auto_login == false) {//자동 로그인 아니면 팝업 권장

                     DialogBase.getProgress().dismiss();

                     DialogBase.setDialog(context ,null ,"ALERT" , new String[]{"알람","로그인을 실패하였습니다.","확인"});
                 }else{

                     BasicInfo.WAITDATA_FLAG = false;//데이터 리십 끝 (스프래시 통과

                     Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }
    public synchronized void set(String res) {

        try {
        /*
            result:
            0:성공
            1:실패
            2:비번,아이디 오류
            21:불량 유저 차단
	    */
            JSONObject jsonObj = new JSONObject(res);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            result = Integer.parseInt(result_);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "Login:: 데이터 불러오기 result:" + result);

            switch (result){
                //case 1:
                    //Toast.makeText(context, "로그인을 실패하였습니다. result:111111111", Toast.LENGTH_SHORT).show();
                    //break;
                case 0:

                    BasicInfo.userInfo.clear();

                    JSONObject item = jsonObj.getJSONObject("info");

                    Iterator<String> keys = item.keys();
                    while (keys.hasNext()) {

                        String key = keys.next();

                        if (!"".equals(key) && !"null".equals(key)) {

                            BasicInfo.userInfo.put(key, new String(item.getString(key).getBytes(), "UTF-8"));
                        }
                        //Log.d(BasicInfo.NETWORK_LOG_TAG, "Login:: 데이터 불러오기 key::"+key+"::" + BasicInfo.userInfo.get(key));
                    }

                    BasicInfo.userInfo.put("mb_id", mb_id);

                    String token = new String(jsonObj.getString("token").getBytes(), "UTF-8");

                    BasicInfo.userInfo.put("token", token);

                    if (auto_login == false) {//로그인 창에서 로그인시만 디비업데이트

                        if (BasicInfo.sqlLite != null) {//디비 컨넥션 된 상태만

                            String set_auto_login = auto_login_checked ? "1" : "0";
                            String set_mb_id = auto_login_checked ? mb_id : "";
                            String set_mb_password = auto_login_checked ? mb_password : "";

                            StringBuilder SQL = new StringBuilder("UPDATE ");
                            SQL.append(SqlLite.DB_USER_LOGIN_INFO);
                            SQL.append(" set auto_login = '");
                            SQL.append(set_auto_login);
                            SQL.append("', mb_id = '");
                            SQL.append(set_mb_id);
                            SQL.append("', mb_password = '");
                            SQL.append(set_mb_password);
                            SQL.append("' ");

                            Log.d(BasicInfo.SQLLIST_TAG, "Login SQL:: 로그인 디비 처리::" + SQL.toString());

                            BasicInfo.sqlLite.execSQL(SQL.toString());

                            //최신 user 상태 저장
                            BasicInfo.userSet.put("auto_login", set_auto_login);
                            BasicInfo.userSet.put("mb_id", set_mb_id);
                            BasicInfo.userSet.put("mb_password", set_mb_password);
                        }

                        Toast.makeText(context, "로그인을 하였습니다.", Toast.LENGTH_SHORT).show();

                        if(activity !=null) {
                            activity.invalidateOptionsMenu();
                        }
                    }
                    break;
                default:

                    success = false;
            }
        } catch (Exception e) {

            success = false;

            e.printStackTrace();
        }

        flag = true;
        super.notify();

        Log.d(BasicInfo.SQLLIST_TAG, "success." + success + ":auto_login:" + auto_login);

        if (this.auto_login == true) {

            BasicInfo.WAITDATA_FLAG = false;//데이터 리십 끝 (스프래시 통과

            if(success == false) {

                Toast.makeText(context, "자동 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show();//자동 로그인 실패 멘트
            }
        } else {

            DialogBase.getProgress().dismiss();

            if(success == false) {

                String msg;
                switch(result){
                    default://case 1:
                            msg = "로그인을 실패하였습니다.";
                        break;
                }

                DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", msg, "확인"});
            }
        }
    }
}