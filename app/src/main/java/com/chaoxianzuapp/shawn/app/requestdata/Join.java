package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.BoardView;
import com.chaoxianzuapp.shawn.app.db.ImageUpload;
import com.chaoxianzuapp.shawn.app.db.SqlLite;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by shawn on 2015-10-16.
 */
public class Join extends Thread {

    private Handler handler = new Handler();

    private String w;
    private String mb_id;
    private String mb_password;
    private String mb_nick;
    private String mb_hp;
    private String gender;
    private String imageFilePath;

    private Context context;
    private Activity activity;

    public Join(Context context, Activity activity, String mb_id_, String mb_password_, String mb_nick_,String mb_hp_, String gender,String imageFilePath) {

        this.imageFilePath = imageFilePath;
        this.mb_id = mb_id_;
        this.mb_password = mb_password_;
        this.mb_nick = mb_nick_;
        this.mb_hp = mb_hp_;
        this.gender = gender;
        this.context = context;
        this.activity = activity;

        DialogBase.setProgress(0, context, "처리 중...");
    }
    public void makeListview(final String jsonData){

        handler.post(new Runnable() {//핸들러가 순서대로 내부적으로 처리(메인스레드에서 )
            @Override
            public void run() {
                set(jsonData);
            }
        });
    }
    @Override
    public void run() { this.get(); }
    public  void get(){

        Map< String, String > param = new HashMap<String ,String>();

        param.put("mb_password", mb_password);
        param.put("mb_nick", mb_nick);
        param.put("mb_hp", mb_hp);
        param.put("mb_sex", gender);

        if(BasicInfo.userInfo.size() > 0) {
            w = "u";
            param.put("w", w);
            param.put("mb_id", BasicInfo.userInfo.get("mb_id"));
            param.put("token" , BasicInfo.userInfo.get("token"));

        }else{
            w = "";
            param.put("w", w);
            param.put("mb_id", mb_id);
        }

        param.put("DEVICE_TYPE" ,BasicInfo.DEVICE_TYPE);
        param.put("DEVICE_ID" ,BasicInfo.DEVICE_ID);
        //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardWrite:: 데이터 불러오기 요청 값:" + out.toString());

        File file = null;
        try {

            if(imageFilePath != null) {
                file = new File(imageFilePath);
            }
            String jsonData = ImageUpload.getInstance().toUploadFile(file, "mb_photo", BasicInfo.URI_JOIN, param);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "Join:: 데이터 불러오기 요청 값:" + jsonData);
            if (!"".equalsIgnoreCase(jsonData)) {

                makeListview(jsonData);
            }
        }catch (Exception e) {

            DialogBase.getProgress().dismiss();
            e.printStackTrace();
        }
    }
    public  void set(String jsonData){

        try {

            JSONObject jsonObj = new JSONObject(jsonData);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            int result = Integer.parseInt(result_);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "Join:: 데이터 불러오기 result:" + result);
/*
	result:
	0	:성공 :회원 정보 수정을 성공하였습니다./회원가입을 성공하였습니다.
	1	:실패 :회원 정보 수정을 실패하였습니다./회원가입을 실패하였습니다.
	2	:아이디를 입력하세요.
	3	:닉네임을 입력하세요.
	4	:이미	사용중인	아이디입니다.
	5	:이미	사용중인	닉네임입니다.
	6	:\'{$filename}\' 파일의 용량이 서버에 설정($upload_max_filesize)된 값보다 크므로 업로드 할 수 없습니다.\\n
	7	:\'{$filename}\' 파일이 정상적으로 업로드 되지 않았습니다.\\n
	20	:로그인 후 수정하세요.
*/
            switch(result){
                case 20:
                    alert("로그인 후 수정하세요.");
                    break;
                case 7:
                    alert("사진이 정상적으로 업로드 되지 않았습니다.");
                    break;
                case 6:
                    alert("사진이 정상적으로 업로드 되지 않았습니다.");
                    break;
                case 5:
                    alert("이미 사용중인 닉네임입니다.");
                    break;
                case 4:
                    alert("이미 사용중인 아이디입니다.");
                    break;
                case 3:
                    alert("닉네임을 입력하세요.");
                    break;
                case 2:
                    alert("아이디를 입력하세요.");
                    break;
                case 1:
                    alert(BasicInfo.userInfo.size() > 0 ? "회원 정보 수정을 실패하였습니다." : "회원가입을 실패하였습니다.");
                    break;
                case 0:

                    //BasicInfo.userInfo.clear();

                    JSONObject item = jsonObj.getJSONObject("info");

                    Iterator<String> keys = item.keys();
                    while (keys.hasNext()) {

                        String key = keys.next();

                        if (!"".equals(key) && !"null".equals(key)) {

                            BasicInfo.userInfo.put(key, new String(item.getString(key).getBytes(), "UTF-8"));
                        }
                        //Log.d(BasicInfo.NETWORK_LOG_TAG, "Login:: 데이터 불러오기 key::"+key+"::" + BasicInfo.userInfo.get(key));
                    }

                    if("u".equals(w)) {

                        Toast.makeText(context, "회원 정보 수정을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }else{

                        BasicInfo.userInfo.put("mb_id", mb_id);

                        String token = new String(jsonObj.getString("token").getBytes(), "UTF-8");

                        BasicInfo.userInfo.put("token", token);

                        if (BasicInfo.sqlLite != null) {//디비 컨넥션 된 상태만

                            String set_auto_login = "1";
                            String set_mb_id = mb_id;
                            String set_mb_password = mb_password;

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

                        Toast.makeText(context, "회원 가입을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    activity.finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            DialogBase.getProgress().dismiss();
        }
    }
    public void alert(String msg){
        DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", msg, "확인"});
    }
}
