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
 * Created by shawn on 2015-09-06.
 */
public class BoardCommentDel extends Thread {

    private Handler handler = new Handler();
    private int wr_id;
    private Context context;
    private Activity activity;

    public BoardCommentDel(Context context, Activity activity, int wr_id) {

        this.wr_id = wr_id;
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
        out.append("&comment_id=");
        out.append(wr_id);

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

        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardCommentDel:: 데이터 불러오기 요청 값:" + out.toString());

        try {

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardList:: 데이터 불러오기 pageNo:"+BoardFragment.pageNo);
            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_COMMENTDEL, out.toString());

            //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardCommentDel:: 데이터 불러오기 요청 값:" + jsonData);

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

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardComment:: 데이터 불러오기 result:" + result);

            switch(result){
                case 5:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "이 댓글과 관련된 답변 댓글이 존재하므로 삭제할 수 없습니다.", "확인"});
                    break;
                case 4:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "패스워드가 틀립니다.", "확인"});
                    break;
                case 3:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신의 글이 아니므로 삭제할 수 없습니다.", "확인"});
                    break;
                case 2:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "이미 삭제되였습니다.", "확인"});
                    break;
                case 1:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "댓글을 삭제 실패하였습니다.", "확인"});
                    break;
                case 0:

                    Toast.makeText(context , "삭제를 성공하였습니다." , Toast.LENGTH_SHORT).show();

                    BasicInfo.setInitPaging();/*기본 페이징 초기화*/

                    String mb_id = new String(jsonObj.getString("mb_id").getBytes(), "UTF-8");
                    String wr_id = new String(jsonObj.getString("wr_id").getBytes(), "UTF-8");
                    String bo_use_comment = new String(jsonObj.getString("bo_use_comment").getBytes(), "UTF-8");

                    int wr_id_ = Integer.parseInt(wr_id);
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
                    sendIntent.putExtra("wr_id", wr_id_);
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
