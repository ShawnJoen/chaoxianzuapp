package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.BoardCommentAdapter;
import com.chaoxianzuapp.shawn.app.board.BoardItem;
import com.chaoxianzuapp.shawn.app.board.BoardViewSet;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shawn on 2015-08-16.
 */
public class BoardDetail extends Thread {

    private Handler handler = new Handler();

    private int wr_id;

    private Context context;

    private Activity activity;

    private AsyncImageLoader imageLoader;

    private BoardCommentAdapter boardCommentAdapter;

    private ListView commentList;

    public BoardDetail(Context context, Activity activity, BoardCommentAdapter boardCommentAdapter, ListView commentList, int wr_id) {

        DialogBase.setProgress(0, context, "로딩 중...");

        this.wr_id = wr_id;

        this.context = context;
        this.activity = activity;

        this.boardCommentAdapter = boardCommentAdapter;

        this.commentList = commentList;

        imageLoader = new AsyncImageLoader(context);
    }
    public BoardDetail(Context context, Activity activity, int wr_id) {

        DialogBase.setProgress(0, context, "로딩 중...");

        this.wr_id = wr_id;

        this.activity = activity;

        imageLoader = new AsyncImageLoader(context);
    }
    public void makeView(final String jsonData){

        handler.post(new Runnable() {//핸들러가 순서대로 내부적으로 처리(메인스레드에서 )
            @Override
            public void run() {
                set(jsonData);
            }
        });
    }
    @Override
    public void run() { this.get(); }
    public void get(){

        StringBuilder out = new StringBuilder("bo_table=");

        out.append(BasicInfo.activeBoardCateItem.getBo_table());
        out.append("&wr_id=");
        out.append(wr_id);
        out.append("&DEVICE_ID=");
        out.append(BasicInfo.DEVICE_ID);

        if(BasicInfo.userInfo.size() > 0) {

            out.append("&mb_id=");
            out.append(BasicInfo.userInfo.get("mb_id"));
            out.append("&token=");
            out.append(BasicInfo.userInfo.get("token"));
            out.append("&DEVICE_TYPE=");
            out.append(BasicInfo.DEVICE_TYPE);
        }
        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 요청 값:" + out.toString());

        try {

            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_VIEW, out.toString());

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 요청 값:" + jsonData);
            if (!"".equalsIgnoreCase(jsonData)) {

                makeView(jsonData);
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
	2: 글이 삭제 되였습니다
	3: 글을 읽을 권한이 없습니다
	30: 글을 읽을 권한이 없습니다.\\n\\n회원이시라면 로그인 후 이용해 보십시오
}
*/
        try {

            JSONObject jsonObj = new JSONObject(jsonData);

            String result_ = new String(jsonObj.getString("result").getBytes(), "UTF-8");

            int result = Integer.parseInt(result_);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 result:" + result);

            switch(result){
                case 30:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 result:30");
                    break;
                case 3:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 result:3");
                    break;
                case 2:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 result:2");
                    break;
                case 1:
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 result:1");
                    break;
                case 0:

                    String is_good_ = new String(jsonObj.getString("is_good").getBytes(), "UTF-8");

                    int is_good = Integer.parseInt(is_good_);//추천 사용여부

                    String is_nogood_ = new String(jsonObj.getString("is_nogood").getBytes(), "UTF-8");

                    int is_nogood = Integer.parseInt(is_nogood_);//비추천 사용여부

                    String bg_flag = new String(jsonObj.getString("bg_flag").getBytes(), "UTF-8");

                    BasicInfo.bg_flag = Integer.parseInt(bg_flag);

                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 bg_flag:" + bg_flag);

                    JSONObject item = jsonObj.getJSONObject("items");

                    String mb_id = new String(item.getString("mb_id").getBytes(), "UTF-8");
                    String city = new String(item.getString("city").getBytes(), "UTF-8");
                    String mb_nick = new String(item.getString("mb_nick").getBytes(), "UTF-8");
                    String mb_photo = new String(item.getString("mb_photo").getBytes(), "UTF-8");

                    String wr_subject = new String(item.getString("wr_subject").getBytes(), "UTF-8");
                    String wr_content = new String(item.getString("wr_content").getBytes(), "UTF-8");
                    String wr_hit = new String(item.getString("wr_hit").getBytes(), "UTF-8");
                    String wr_datetime = new String(item.getString("wr_datetime").getBytes(), "UTF-8");

                    String wr_good;
                    if(is_good > 0) {
                        wr_good = new String(item.getString("wr_good").getBytes(), "UTF-8");
                    }else{
                        wr_good = "-1";
                    }

                    String wr_nogood;
                    if(is_nogood > 0) {
                        wr_nogood = new String(item.getString("wr_nogood").getBytes(), "UTF-8");
                    }else{
                        wr_nogood = "-1";
                    }

                    JSONArray imgUrl_ = item.getJSONArray("imgUrl");

                    int imgUrlLen = imgUrl_.length();

                    String[] imgUrl = new String[imgUrlLen ];

                    for(int i = 0 ;i < imgUrlLen ;i++){

                        imgUrl[i] = new String(imgUrl_.getString(i).getBytes(), "UTF-8");
                    }
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 3333 ***************!!!!!!:" + wr_hit);
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 3333 ***************!!!!!!:" + wr_good);
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 3333 ***************!!!!!!:" + wr_nogood);
                    Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 3333 ***************!!!!!!:" + wr_datetime);
                    //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 3333 ***************!!!!!!:" + imgUrl[0]);

                    new BoardViewSet(this.context ,this.activity , imageLoader , new BoardItem(mb_id,mb_nick,mb_photo,Integer.parseInt(city), wr_subject,wr_content,Integer.parseInt(wr_hit),
                            Integer.parseInt(wr_good),Integer.parseInt(wr_nogood),Long.parseLong(wr_datetime),imgUrl));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            DialogBase.getProgress().dismiss();

            if(boardCommentAdapter != null) {

                BasicInfo.setInitPaging();/*댓글 페이징 초기화*/

                new BoardComment(this.activity, boardCommentAdapter, commentList, this.wr_id).start();//댓글 불러오기
            }
        }
    }
}

