package com.chaoxianzuapp.shawn.app;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.dialog.DialogBase;
import com.chaoxianzuapp.shawn.app.gcm.QuickstartPreferences;
import com.chaoxianzuapp.shawn.app.requestdata.BaseData;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;


/**
 * Created by shawn on 2015-07-12.
 */
public class BroadcastControl {

    private Context context;
    private Activity activity;

    MyBroadcastReceiver receiver;

    public BroadcastControl(Context context , Activity activity){
        this.context = context;
        this.activity = activity;
    }
    public void registBroad(String[] actions){

        receiver = new MyBroadcastReceiver();

        IntentFilter filter = new IntentFilter();

        for(int i = 0 ; i < actions.length ;i++){
            filter.addAction(actions[i]);
        }

        context.registerReceiver(receiver, filter);
    }
    public void unregistBroad(){    Log.d(BasicInfo.SERVICE_TAG, "unregistBroad ::: .");

        //BasicInfo.STATE_NETWORK = true;

        context.unregisterReceiver(receiver);

        //SocketControl.socket = null;
    }
    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            Log.d(BasicInfo.SERVICE_TAG, "onReceive:" + action);

            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                Log.d(BasicInfo.SERVICE_TAG, "네트워크 책킹.");

                try {

                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {

                        unregistBroad();//브로드캐스트 종료.

                        BasicInfo.STATE_NETWORK = false;//접속 끊김

                        BasicInfo.CLOSE_ALL_PROCESS = true;

                        Log.d(BasicInfo.SERVICE_TAG, "네트워크 끊김.1");

                        DialogBase.setDialog(context, activity, "A", new String[]{"인터넷 접속이 끊겻습니다", "확인"});
                    } else {
                        Log.d("NectworkState", "네트워크 연결중");
                    }
                } catch (Exception e) {

                    Log.d(BasicInfo.SERVICE_TAG, "네트워크 끊김.3" + e.getMessage());
                }
            }else if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                // 액션이 READY일 경우
                Log.d(BasicInfo.GCM_TAG, "REGISTRATION_READY:");
            } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                // 액션이 GENERATING일 경우
                Log.d(BasicInfo.GCM_TAG, "REGISTRATION_GENERATING:");
            } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                // 액션이 COMPLETE일 경우

                String token = intent.getStringExtra("token");

                Log.d(BasicInfo.GCM_TAG, "REGISTRATION_GENERATING:" + token);

                new BaseData(context , activity , token).start();
            }else if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //다운로드 완료하는 동시에 db처리
                DialogBase.getProgress().dismiss();

                Toast.makeText(context, "다운로드를 완료하였습니다.", Toast.LENGTH_SHORT).show();

                BaseUtil.setLaunchApk(context, activity);
            }


            /*else{//소켓 브로드 캐스트 시작

                String mode = intent.getStringExtra("MODE");

                if (mode.equals("IN")) {

                    Class<?> cls = null;

                    Log.d(BasicInfo.SERVICE_TAG, "************BroadcastSocket->  PACKAGE_MAIN  ************" + action + "++++++"+BasicInfo.ACTIVITY_ENABLE);

                    try {

                        cls = Class.forName(BasicInfo.ACTIVITY_ENABLE);

                        context.startActivity(new Intent(context, cls));//방생성,방입장
                    //Intent inte =new Intent(context ,cls);
                    //inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//FLAG_ACTIVITY_NO_HISTORY,FLAG_ACTIVITY_NEW_TASK,FLAG_ACTIVITY_SINGLE_TOP
                   // context.startActivity(inte);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (mode.equals("M")) {



                    Log.d(BasicInfo.SERVICE_TAG, "************BroadcastSocket->  PACKAGE_MAIN  *****M*******" + action + "+++M+++"+BasicInfo.ACTIVITY_ENABLE);


                    Intent intentData = null;
                    switch(BasicInfo.ACTIVITY_ENABLE){
                        case BasicInfo.PACKAGE_CREAT_ROOM:

                            intentData = new Intent(context,CastCreateRoom.class);
                            break;
                        case BasicInfo.PACKAGE_IN_ROOM:

                            intentData = new Intent(context,CastInRoom.class);
                            break;
                    }

                    if(intentData != null){

                        String mb_id = intent.getStringExtra("mb_id");
                        String mb_nick = intent.getStringExtra("mb_nick");
                        String msg = intent.getStringExtra("msg");

                        intentData.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intentData.putExtra("mb_id", mb_id);
                        intentData.putExtra("mb_nick", mb_nick);
                        intentData.putExtra("msg", msg);
                        context.startActivity(intentData);//방생성,방입장

                        //Toast.makeText(context , "mb_id::" + mb_id + " ,, mb_nick::"+mb_nick+" ,, msg::" + msg ,Toast.LENGTH_SHORT).show();
                    }
                }else if (action.equals(BasicInfo.PACKAGE_MAIN)) {

                    Log.d(BasicInfo.SERVICE_TAG, "************BroadcastSocket->  PACKAGE_MAIN  ************" + action + ":::mode:::" + mode);
                } else if (action.equals(BasicInfo.PACKAGE_CREAT_ROOM)) {

                    Log.d(BasicInfo.SERVICE_TAG, "************BroadcastSocket->  PACKAGE_CREAT_ROOM  ************" + action + ":::mode:::" + mode);

                } else if (action.equals(BasicInfo.PACKAGE_IN_ROOM)) {

                    Log.d(BasicInfo.SERVICE_TAG, "************BroadcastSocket->  PACKAGE_IN_ROOM  ************" + action + ":::mode:::" + mode);

                }
            }*/
        }
    }
}
