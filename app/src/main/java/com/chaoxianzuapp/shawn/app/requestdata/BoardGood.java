package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by shawn on 2015-09-22.
 */
public class BoardGood extends Thread {

    private Handler handler = new Handler();

    private static boolean flag = true;

    private int wr_id;

    private String good;

    private ImageView wr_good_;

    private TextView wr_good;

    private Context context;

    private Activity activity;

    public BoardGood(Context context, Activity activity, int wr_id, String good, ImageView wr_good_, TextView wr_good) {

        DialogBase.setProgress(0, context, "로딩 중...");

        this.wr_id = wr_id;

        this.good = good;

        this.wr_good_ = wr_good_;

        this.wr_good = wr_good;

        this.context = context;

        this.activity = activity;
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
    public synchronized void get(){

        if (this.flag == false){
            try{
                super.wait();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.flag = false;

        StringBuilder out = new StringBuilder("bo_table=");

        out.append(BasicInfo.activeBoardCateItem.getBo_table());
        out.append("&wr_id=");
        out.append(wr_id);
        out.append("&good=");
        out.append(good);
        if(BasicInfo.userInfo.size() > 0) {

            out.append("&mb_id=");
            out.append(BasicInfo.userInfo.get("mb_id"));
            out.append("&token=");
            out.append(BasicInfo.userInfo.get("token"));
            out.append("&DEVICE_TYPE=");
            out.append(BasicInfo.DEVICE_TYPE);
            out.append("&DEVICE_ID=");
            out.append(BasicInfo.DEVICE_ID);
        }
        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 요청 값:" + out.toString());

        try {

            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_GOOD, out.toString());

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 요청 값:" + jsonData);
            if (!"".equalsIgnoreCase(jsonData)) {

                makeListview(jsonData);
            }
        }catch (Exception e) {

            DialogBase.getProgress().dismiss();
            e.printStackTrace();
        }
    }
    public synchronized void set(String jsonData){

/*
result:{
	0:성공
	1:실패
	2:자신의 글에는 추천 또는 비추천 하실 수 없습니다.
	3:이 게시판은 추천 기능을 사용하지 않습니다.
	4:이 게시판은 비추천 기능을 사용하지 않습니다.
	5:이미 추천하신 글입니다.
	6:이미 비추천하신 글입니다.
	20:로그인 후 삭제하세요.
}
*/
        try {

            JSONObject jsonObj = new JSONObject(jsonData);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            int result = Integer.parseInt(result_);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 result:" + result);

            switch(result){
                case 20:
                    setHandler("로그인이 필요합니다.");
                    break;
                case 6:
                    setHandler("이미 `싫어요`를 하였습니다.");
                    break;
                case 5:
                    setHandler("이미 `좋아요`를 하였습니다.");
                    break;
                case 4:
                    setHandler("`싫어요`기능을 사용하지 않습니다.");
                    break;
                case 3:
                    setHandler("`좋아요`기능을 사용하지 않습니다.");
                    break;
                case 2:
                    setHandler("본인 글은 `좋아요`,`싫어요` 할 수 없습니다.");
                    break;
                case 1:
                    setHandler("`좋아요`,`싫어요`를 실패하였습니다.");
                    break;
                case 0:

                    BasicInfo.bg_flag = "good".equalsIgnoreCase(good) ? 1 : 2;

                    wr_good_.setImageResource(R.mipmap.ic_good_selected);

                    String wr_good2 = wr_good.getText().toString().replaceAll("," , "");

                    wr_good.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(Integer.parseInt(wr_good2) + 1));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            DialogBase.getProgress().dismiss();

            flag = true;
            super.notify();
        }
    }
    private void setHandler(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context , msg , Toast.LENGTH_LONG).show();
            }
        });
    }
}

