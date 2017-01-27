package com.chaoxianzuapp.shawn.app.requestdata;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.BoardItem;
import com.chaoxianzuapp.shawn.app.board.BoardListAdapter;
import com.chaoxianzuapp.shawn.app.board.BoardSearch;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shawn on 2015-08-09.
 */

public class BoardList extends Thread {

    private Handler handler = new Handler();

    private BoardListAdapter mBoardListAdapter;

    public BoardList(Context context, BoardListAdapter boardListAdapter) {

        DialogBase.setProgress(0, context, "로딩 중...");

        this.mBoardListAdapter = boardListAdapter;
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
    public void run() {

        if(BasicInfo.prevPageNo < BasicInfo.pageNo) {

            BasicInfo.prevPageNo = BasicInfo.pageNo;

            this.get();
        }
    }
    public void get(){

        StringBuilder out = new StringBuilder("bo_table=");
        out.append(BasicInfo.activeBoardCateItem.getBo_table());
        out.append("&page=");
        out.append(String.valueOf(BasicInfo.pageNo));
        out.append("&sort=");
        out.append(BasicInfo.activeBoardSortItem.getSort());
        if(!"notice".equalsIgnoreCase(BasicInfo.activeBoardCateItem.getBo_table())) {

            out.append("&bo_city=");
            out.append(BasicInfo.activeBoardCityItem.getCode());
        }
        if(BasicInfo.PACKAGE_BOARD_SEARCH.equals(BasicInfo.ACTIVITY_ENABLE)){//검색 페이지면 키워드 추가

            out.append("&searchType=wr_subject&searchInput=");
            out.append(BoardSearch.keyword);
        }

        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 요청 값:" + out.toString());

        try {

            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_LIST, out.toString());

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 요청 값:" + jsonData);
            if (!"".equalsIgnoreCase(jsonData)) {

                makeListview(jsonData);

                BasicInfo.pageNo++; //페이징 +1
            }
        }catch (Exception e) {

            DialogBase.getProgress().dismiss();
            e.printStackTrace();
        }
    }
    public void set(String jsonData){
/*
result:{
	0: 성공
	1: 실패
	2: 목록을 볼 권한이 없슴
	20:로그인이 필요
}
*/
        try {

            JSONObject jsonObj = new JSONObject(jsonData);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            int result = Integer.parseInt(result_);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 result:" + result);

            switch(result){
                case 20:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 result:20");
                    break;
                case 2:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 result:2");
                    break;
                case 1:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 result:1");
                    break;
                case 0:
/*
                    String is_good_ = new String(jsonObj.getString("is_good").getBytes(), "UTF-8");

                    int is_good = Integer.parseInt(is_good_);//추천 사용여부
*/
                    String bo_use_comment_ = new String(jsonObj.getString("bo_use_comment").getBytes(), "UTF-8");

                    int bo_use_comment = Integer.parseInt(bo_use_comment_);//댓글 사용여부

                    String totalPage_ = new String(jsonObj.getString("totalPage").getBytes(), "UTF-8");

                    BasicInfo.totalPage = Integer.parseInt(totalPage_);

                    JSONArray item = jsonObj.getJSONArray("items");

                    //String wr_good;
                    String wr_comment;

                    for (int i = 0; i < item.length(); i++) {

                        String wr_id = new String(item.getJSONObject(i).getString("wr_id").getBytes(), "UTF-8");
                        String mb_id = new String(item.getJSONObject(i).getString("mb_id").getBytes(), "UTF-8");
                        String city = new String(item.getJSONObject(i).getString("city").getBytes(), "UTF-8");
                        String wr_subject = new String(item.getJSONObject(i).getString("wr_subject").getBytes(), "UTF-8");
                        String wr_hit = new String(item.getJSONObject(i).getString("wr_hit").getBytes(), "UTF-8");
                        String icon_new = new String(item.getJSONObject(i).getString("icon_new").getBytes(), "UTF-8");
                        String icon_hot = new String(item.getJSONObject(i).getString("icon_hot").getBytes(), "UTF-8");

                        if(bo_use_comment > 0) {
                            wr_comment = new String(item.getJSONObject(i).getString("wr_comment").getBytes(), "UTF-8");
                        }else{
                            wr_comment = "-1";
                        }
                        /*if(is_good > 0) {
                            wr_good = new String(item.getJSONObject(i).getString("wr_good").getBytes(), "UTF-8");
                        }else{
                            wr_good = "-1";Integer.parseInt(wr_good),
                        }*/

                        String wr_datetime = new String(item.getJSONObject(i).getString("wr_datetime").getBytes(), "UTF-8");
                        String imgUrl = new String(item.getJSONObject(i).getString("imgUrl").getBytes(), "UTF-8");

                        mBoardListAdapter.addItem(new BoardItem(Integer.parseInt(wr_id) , mb_id ,Integer.parseInt(wr_comment) ,Integer.parseInt(city),wr_subject,
                                Integer.parseInt(wr_hit), Integer.parseInt(icon_new), Integer.parseInt(icon_hot), Long.parseLong(wr_datetime), new String[]{imgUrl}));
                    }
                    break;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }finally {

            DialogBase.getProgress().dismiss();
            mBoardListAdapter.notifyDataSetChanged();
        }
    }
}
