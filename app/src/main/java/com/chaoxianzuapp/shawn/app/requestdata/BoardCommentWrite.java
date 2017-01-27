package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.BoardComment;
import com.chaoxianzuapp.shawn.app.board.BoardView;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONObject;

/**
 * Created by shawn on 2015-09-07.
 */
public class BoardCommentWrite extends Thread {

    private Handler handler = new Handler();

    private int wr_id;
    private String mb_id;
    private String wr_content;

    private Context context;
    private Activity activity;

    public BoardCommentWrite(Context context, Activity activity, int wr_id, String mb_id, String wr_content) {

        this.wr_id = wr_id;
        this.mb_id = mb_id;
        this.wr_content = wr_content;
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
    public  void get(){

        StringBuilder out = new StringBuilder("bo_table=");
        out.append(BasicInfo.activeBoardCateItem.getBo_table());
        out.append("&wr_id=");
        out.append(wr_id);
        out.append("&wr_content=");
        out.append(wr_content);

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

        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardCommentWrite:: 데이터 불러오기 요청 값:" + out.toString());

        try {

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 pageNo:"+BoardFragment.pageNo);
            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_COMMENTWRITE, out.toString());

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardCommentWrite:: 데이터 불러오기 요청 값:" + jsonData);
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

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardCommentWrite:: 데이터 불러오기 result:" + result);

            switch(result){
                case 5:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "보유하신 포인트가 부족하여 댓글을 작성할 수 없습니다.", "확인"});
                    break;
                case 4:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "글이 존재하지 않습니다. 글이 삭제되었거나 이동하였을 수 있습니다.", "확인"});
                    break;
                case 3:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "동일한 내용을 연속해서 등록할 수 없습니다.", "확인"});
                    break;
                case 2:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "댓글을 쓸 권한이 없습니다.", "확인"});
                    break;
                case 1:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "댓글 작성을 실패하였습니다.", "확인"});
                    break;
                case 0:

                    Toast.makeText(context, "댓글이 등록되였습니다.", Toast.LENGTH_SHORT).show();

                    BasicInfo.setInitPaging();/*기본 페이징 초기화*/

                    String bo_use_comment = new String(jsonObj.getString("bo_use_comment").getBytes(), "UTF-8");

                    int bo_use_comment_ = Integer.parseInt(bo_use_comment);

                    Intent sendIntent = null;
                    switch (BasicInfo.ACTIVITY_ENABLE) {
                        case BasicInfo.PACKAGE_BOARD_VIEW:

                            sendIntent = new Intent(context, BoardView.class);
                            break;
                        case BasicInfo.PACKAGE_BOARD_COMMENT:

                            sendIntent = new Intent(context, BoardComment.class);
                            sendIntent.putExtra("wr_subject", BoardComment.wr_subject);
                            break;
                    }

                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    sendIntent.putExtra("mb_id", mb_id);
                    sendIntent.putExtra("wr_id", wr_id);
                    sendIntent.putExtra("bo_use_comment", bo_use_comment_);
                    context.startActivity(sendIntent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            DialogBase.getProgress().dismiss();
        }
    }
}
