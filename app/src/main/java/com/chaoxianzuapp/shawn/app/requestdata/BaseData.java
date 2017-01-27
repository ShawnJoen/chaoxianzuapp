package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCateItem;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCityItem;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardSortItem;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogApk;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shawn on 2015-07-12.
 */
public class BaseData extends Thread {

    private Handler handler = new Handler();
    private Context context;
    private Activity activity;
    private String appVersion = "";
    private String appUrl = "";
    private String appContent = "";
    private String REG_ID = null;
    private int result = 1;

    public BaseData(Context context, Activity activity, String token) {
        this.context = context;
        this.activity = activity;
        this.REG_ID = token;
    }

    public void parseJaonData(final String jsonData){

        try {

            JSONObject jsonObj = new JSONObject(jsonData);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            result = Integer.parseInt(result_);

            switch(result) {
                case 1:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BaseData:: 데이터 불러오기 result:1");
                    break;
                default:

                    JSONArray bo_tables = jsonObj.getJSONArray("bo_table");

                    BasicInfo.bo_tables.clear();

                    for (int i = 0; i < bo_tables.length(); i++) {

                        String code = new String(bo_tables.getJSONObject(i).getString("code").getBytes(), "UTF-8");
                        String label = new String(bo_tables.getJSONObject(i).getString("label").getBytes(), "UTF-8");
                        //String imgUrl = new String(bo_tables.getJSONObject(i).getString("imgUrl").getBytes(), "UTF-8");
                        String bo_table = new String(bo_tables.getJSONObject(i).getString("bo_table").getBytes(), "UTF-8");


                        Log.d(BasicInfo.NETWORK_LOG_TAG, "BaseData:: code:"+code+",label:"+label+",bo_table:"+bo_table);

                        BoardCateItem item = new BoardCateItem(Integer.parseInt(code), label, null, bo_table);

                        if (i == 0) {

                            BasicInfo.notice = item;
                        }else {
                            BasicInfo.bo_tables.add(item);
                        }
                    }

                    JSONArray bo_sort = jsonObj.getJSONArray("bo_sort");

                    BasicInfo.bo_sorts.clear();

                    for (int i = 0; i < bo_sort.length(); i++) {

                        String code = new String(bo_sort.getJSONObject(i).getString("code").getBytes(), "UTF-8");
                        String label = new String(bo_sort.getJSONObject(i).getString("label").getBytes(), "UTF-8");
                        String sort = new String(bo_sort.getJSONObject(i).getString("sort").getBytes(), "UTF-8");

                        BoardSortItem item = new BoardSortItem(Integer.parseInt(code), label, sort);

                        BasicInfo.bo_sorts.add(item);

                        if (i == 0) {

                            BasicInfo.activeBoardSortItem = item;
                        }
                    }
                    JSONArray bo_city = jsonObj.getJSONArray("bo_city");

                    BasicInfo.bo_city.clear();

                    for (int i = 0; i < bo_city.length(); i++) {

                        String code = new String(bo_city.getJSONObject(i).getString("code").getBytes(), "UTF-8");
                        String label = new String(bo_city.getJSONObject(i).getString("label").getBytes(), "UTF-8");

                        BoardCityItem item = new BoardCityItem(Integer.parseInt(code), label);

                        BasicInfo.bo_city.add(item);

                        BasicInfo.BO_CITY.put(Integer.parseInt(code) , label);

                        if (i == 0) {

                            BasicInfo.activeBoardCityItem = item;
                        }
                    }
                    appVersion = new String(jsonObj.getString("appVersion").getBytes(), "UTF-8");
                    appUrl = new String(jsonObj.getString("appUrl").getBytes(), "UTF-8");
                    appContent = new String(jsonObj.getString("appContent").getBytes(), "UTF-8");
            }
        }catch(Exception e) {

            e.printStackTrace();
        }finally{

            if(result ==0) {

                handler.post(new Runnable() {//기본데이터 불러오기 에러시
                    @Override
                    public void run() {

                        Log.d(BasicInfo.NETWORK_LOG_TAG, "RequestBase:: 데이터 불러오기 end result4:" + BasicInfo.APP_VERSION);
                        Log.d(BasicInfo.NETWORK_LOG_TAG, "RequestBase:: 데이터 불러오기 end result5:" + appVersion);
                        if (BasicInfo.APP_VERSION.compareTo(appVersion) < 0) {

                            new DialogApk().getLaunchApk(context, activity, BasicInfo.BASE_URI + appUrl, new String[]{appContent});
                        } else {

                            BasicInfo.setAutoLogin(context);
                        }
                    }
                });
            } else {

                onError();
            }
        }
    }
    @Override
    public void run() {

        String params = "country=" + BasicInfo.language;
        if(REG_ID != null){

            params += "&DEVICE_ID=" +BasicInfo.DEVICE_ID+ "&REG_ID=" +REG_ID;
        }
        try {

            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BASE, params);

            if(jsonData != ""){

                this.parseJaonData(jsonData);
            }else{

                onError();
            }
        } catch (Exception e) {

            onError();

            e.printStackTrace();
        }
    }
    public void onError(){

        handler.post(new Runnable() {//기본데이터 불러오기 에러시
            @Override
            public void run() {

                Toast.makeText(context, "base language error", Toast.LENGTH_LONG).show();
                activity.finish();
            }
        });
    }
}
