package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.BoardCommentAdapter;
import com.chaoxianzuapp.shawn.app.board.BoardCommentItem;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;
import com.chaoxianzuapp.shawn.app.util.UIHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shawn on 2015-08-17.
 */
public class BoardComment extends Thread {

    private Handler handler = new Handler();

    private int wr_id;

    private Context context;
    private Activity activity;

    private BoardCommentAdapter boardCommentAdapter;

    private ListView commentList;

    public BoardComment(Context context, Activity activity, BoardCommentAdapter boardCommentAdapter, ListView commentList, int wr_id) {

        this(activity,boardCommentAdapter , commentList, wr_id);

        this.context = context;

        DialogBase.setProgress(0, context, "로딩 중...");
    }
    public BoardComment(Activity activity, BoardCommentAdapter boardCommentAdapter, ListView commentList, int wr_id) {

        this.wr_id = wr_id;

        this.activity = activity;

        this.boardCommentAdapter = boardCommentAdapter;

        this.commentList = commentList;
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
    public  void get(){

        StringBuilder out = new StringBuilder("bo_table=");
        out.append(BasicInfo.activeBoardCateItem.getBo_table());
        out.append("&wr_id=");
        out.append(wr_id);
        out.append("&page=");
        out.append(String.valueOf(BasicInfo.pageNo));

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

        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardComment:: 데이터 불러오기 요청 값:" + out.toString());

        try {

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 pageNo:"+BoardFragment.pageNo);
            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_COMMENT, out.toString());

            if (!"".equalsIgnoreCase(jsonData)) {

                makeListview(jsonData);

                BasicInfo.pageNo++; //페이징 +1
            }
        }catch (Exception e) {

            finish();
            e.printStackTrace();
        }
    }
    public  void set(String jsonData){

        try {

            JSONObject jsonObj = new JSONObject(jsonData);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            int result = Integer.parseInt(result_);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardComment:: 데이터 불러오기 result:" + result);

            switch(result){
                case 1:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardComment:: 데이터 불러오기 result:1");
                    break;
                case 0:

                    String comment_min_ = new String(jsonObj.getString("comment_min").getBytes(), "UTF-8");

                    int comment_min = Integer.parseInt(comment_min_);//덧글 제한수

                    String comment_max_ = new String(jsonObj.getString("comment_max").getBytes(), "UTF-8");

                    int comment_max = Integer.parseInt(comment_max_);//덧글 제한수

                    String totalPage_ = new String(jsonObj.getString("totalPage").getBytes(), "UTF-8");

                    BasicInfo.totalPage = Integer.parseInt(totalPage_);

                    JSONArray item = jsonObj.getJSONArray("items");

                    for (int i = 0; i < item.length(); i++) {

                        String wr_id = new String(item.getJSONObject(i).getString("wr_id").getBytes(), "UTF-8");
                        String mb_id = new String(item.getJSONObject(i).getString("mb_id").getBytes(), "UTF-8");
                        String wr_name = new String(item.getJSONObject(i).getString("wr_name").getBytes(), "UTF-8");
                        String wr_content = new String(item.getJSONObject(i).getString("wr_content").getBytes(), "UTF-8");
                        String is_del = new String(item.getJSONObject(i).getString("is_del").getBytes(), "UTF-8");
                        String strstr_secret = new String(item.getJSONObject(i).getString("strstr_secret").getBytes(), "UTF-8");
                        String wr_datetime = new String(item.getJSONObject(i).getString("wr_datetime").getBytes(), "UTF-8");

                        boardCommentAdapter.addItem(new BoardCommentItem(Integer.parseInt(is_del), mb_id, Integer.parseInt(strstr_secret),
                        wr_content, Long.parseLong(wr_datetime), Integer.parseInt(wr_id), wr_name));
                    }

                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardComment:: 데이터 불러오기 end !!!!!!" + comment_min + "::" +comment_max);

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            boardCommentAdapter.notifyDataSetChanged();//댓글 리스트 로딩 완료후 리스트 UI갱신

            finish();
        }
    }
    public void finish(){

        if(this.context == null) {
            UIHelper.setListViewHeightBasedOnChildren(commentList);
        }else{
            DialogBase.getProgress().dismiss();
        }
    }
}
