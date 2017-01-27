package com.chaoxianzuapp.shawn.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.db.SqlLite;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shawn on 2015-08-19.
 */
public class BaseUtil {
    public static void setLaunchApk(Context context ,Activity activity) {
        // TODO Auto-generated method stub
        File file = new File(BasicInfo.APP_APKPATH);
        if(file.exists()){

            StringBuilder SQL = new StringBuilder("UPDATE ");
            SQL.append(SqlLite.DB_USER_LOGIN_INFO);
            SQL.append(" set appUrl = '");
            SQL.append(BasicInfo.APP_APKPATH);
            SQL.append("' ");
            BasicInfo.sqlLite.execSQL(SQL.toString());

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }else{
            Toast.makeText(context, "앱 다운로드를 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
        Log.d("xxxxxxxxx", "apkpath:" + BasicInfo.APP_APKPATH);
        activity.finish();
    }
    public static String md5(String string) {

        byte[] hash;

        try {

            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {

            if ((b & 0xFF) < 0x10) hex.append("0");

            hex.append(Integer.toHexString(b & 0xFF));

        }
        return hex.toString();
    }
    public static String getVersion(Context context)//获取版本号	, 버전 네임
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //return context.getString(R.string.version_unknown);
            return "error Version";
        }
    }
    public static String getDeviceId(Context context)//디바이스 아이디
    {
        return Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
    public static String getPhoneNumber(Context context)
    {//01021833888,null
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)  context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }
    /*public static void setThumbnail(Context context ,ImageView imageView,String thumbnail)
    {
        if (!TextUtils.isEmpty(thumbnail)) {

            AsyncImageLoader imageLoader = new AsyncImageLoader(context);

            Bitmap bitmap = imageLoader.loadImage(imageView, thumbnail);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }*/
    public static void setImgUrl(int mode , AsyncImageLoader imageLoader,ImageView imageView,LinearLayout layout, String imgUrl) {

        if (!TextUtils.isEmpty(imgUrl)) {

            switch(mode){
                case 1:imageView.setVisibility(View.VISIBLE);
                    break;
                case 2:layout.setVisibility(View.VISIBLE);
                    break;
            }

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardViewSet:: setImgUrl !!!1!!!:" + imgUrl);

            Bitmap bitmap = imageLoader.loadImage(imageView, imgUrl);
            if (bitmap != null) {

                //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardViewSet:: setImgUrl !!!2!!!:"+ imgUrl);

                imageView.setImageBitmap(bitmap);
            }
        }
    }
    public static int pixelToDp(int pixel){
        //DisplayMetrics dm = context.getResources().getDisplayMetrics();
        //DisplayMetrics dm = new DisplayMetrics();
        //context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return Math.round(pixel * BasicInfo.density);
    }
    public static String getRealPathFromURI(Context context ,Uri contentUri) {
        String res = null;
        Cursor cursor = context.getContentResolver().query(contentUri, new String[]{ MediaStore.Images.Media.DATA }, null, null, null);

        //CursorLoader loader = new CursorLoader(context, contentUri, new String[]{ MediaStore.Images.Media.DATA }, null, null, null);
        //Cursor cursor = loader.loadInBackground();

        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
