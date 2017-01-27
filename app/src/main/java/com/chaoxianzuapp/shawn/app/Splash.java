package com.chaoxianzuapp.shawn.app;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.db.SqlLite;
import com.chaoxianzuapp.shawn.app.gcm.QuickstartPreferences;
import com.chaoxianzuapp.shawn.app.gcm.RegistrationIntentService;
import com.chaoxianzuapp.shawn.app.requestdata.BaseData;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;
import java.util.Locale;

/**
 * Created by shawn on 2015-08-08.
 */
public class Splash extends Activity {

    public BroadcastControl receiver = null;

    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        adView = (AdView)findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        BasicInfo.HP = BaseUtil.getPhoneNumber(this);

        BasicInfo.DEVICE_ID = BaseUtil.getDeviceId(this);

        BasicInfo.APP_VERSION = BaseUtil.getVersion(this);

        Locale curLocale = getResources().getConfiguration().locale;

        BasicInfo.language = curLocale.getLanguage();

        BasicInfo.STATE_NETWORK = true;//부팅 초기화

        BasicInfo.CLOSE_ALL_PROCESS = false;//부팅 초기화

        BasicInfo.WAITDATA_FLAG = true;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        BasicInfo.density = dm.density;

        receiver = new BroadcastControl(this,this);

        receiver.registBroad(new String[]{ConnectivityManager.CONNECTIVITY_ACTION, QuickstartPreferences.REGISTRATION_READY,QuickstartPreferences.REGISTRATION_GENERATING,
                QuickstartPreferences.REGISTRATION_COMPLETE, DownloadManager.ACTION_DOWNLOAD_COMPLETE});//인터넷 상태

        BasicInfo.userSet.clear();

        // SD Card checking
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//SD카드 없으면 통과 (아이디 저장,자동로그인 취소처리

            Toast.makeText(this, "No SD card. Please insert SD card first.", Toast.LENGTH_LONG).show();
        } else {

            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!BasicInfo.ExternalChecked && externalPath != null) {

                BasicInfo.ExternalPath = externalPath + File.separator;

                Log.d(BasicInfo.SQLLIST_TAG, "ExternalPath : " + BasicInfo.ExternalPath);

                SqlLite.DB_SQLLITE = BasicInfo.ExternalPath + SqlLite.DB_SQLLITE;//디비 저장경로 셋팅

                BasicInfo.ExternalChecked = true;
            }

            openDB();//디비 컨넥션
        }

        getUserSet();//사용자 셋팅 불러오기

        getInstanceIdToken();//GCM REG_ID 불러오기

        //new BaseData(this,this).start();
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    //private final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {

            Log.d(BasicInfo.GCM_TAG, "checkPlayServices:" + resultCode);
            Log.d(BasicInfo.GCM_TAG, "checkPlayServices:" + GooglePlayServicesUtil.isUserRecoverableError(resultCode));
            /*if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(BasicInfo.GCM_TAG, "This device is not supported.");
                finish();
            }*/
            new BaseData(this , this , "").start();//구글 GCM토근 가져오기 실패시
            return false;
        }
        return true;
    }
    public void getUserSet() {

        String auto_login = "0";
        String mb_id = "";
        String mb_password = "";

        if (BasicInfo.sqlLite != null) {

            String SQL = "SELECT auto_login, mb_id, mb_password FROM " + SqlLite.DB_USER_LOGIN_INFO;

            int recordCount = -1;

            Cursor outCursor = BasicInfo.sqlLite.rawQuery(SQL);

            recordCount = outCursor.getCount();

            if(recordCount == 0) {//없으면 빈값 INSERT

                BasicInfo.sqlLite.execSQL("INSERT INTO " + SqlLite.DB_USER_LOGIN_INFO + "(auto_login) values(0)");
            }else{

                outCursor.moveToNext();

                auto_login = outCursor.getString(0);
                mb_id = outCursor.getString(1);
                mb_password = outCursor.getString(2);
            }
        }

        BasicInfo.userSet.put("auto_login", auto_login);
        BasicInfo.userSet.put("mb_id", mb_id);
        BasicInfo.userSet.put("mb_password", mb_password);

        Log.d(BasicInfo.SQLLIST_TAG, "BasicInfo.userSet auto_login: " + BasicInfo.userSet.get("auto_login"));
        Log.d(BasicInfo.SQLLIST_TAG, "BasicInfo.userSet mb_id: " + BasicInfo.userSet.get("mb_id"));
        Log.d(BasicInfo.SQLLIST_TAG, "BasicInfo.userSet mb_password: " + BasicInfo.userSet.get("mb_password"));
    }
    public void openDB() {
        if (BasicInfo.sqlLite != null) {
            BasicInfo.sqlLite.close();
            BasicInfo.sqlLite = null;
        }
        BasicInfo.sqlLite = SqlLite.getInstance(this);
        boolean isOpen = BasicInfo.sqlLite.open();
        if (isOpen) {
            Log.d(BasicInfo.SQLLIST_TAG, "chaoxianzuapp database is open.");
        } else {
            Log.d(BasicInfo.SQLLIST_TAG, "chaoxianzuapp database is not open.");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        final int welcomeScreenDisplay = 3000;
        Thread splash = new Thread() {
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < welcomeScreenDisplay) {

                        sleep(100);
                        if(BasicInfo.STATE_NETWORK == true) {

                            if(welcomeScreenDisplay == (wait + 100) && BasicInfo.WAITDATA_FLAG == true){

                                continue;//데이터 받는중
                            }else{
                                wait += 100;
                            }
                        }else{

                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {

                    if(BasicInfo.STATE_NETWORK == true) {

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        splash.start();
    }
    @Override
    protected void onDestroy() {Log.d(BasicInfo.SERVICE_TAG, "xx onPause ::: .");
        super.onDestroy();

        if(BasicInfo.CLOSE_ALL_PROCESS == false){
            receiver.unregistBroad();
        }
    }
}