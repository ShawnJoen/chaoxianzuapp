package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.db.RemoteDB;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONObject;

/**
 * Created by shawn on 2015-09-24.
 */
public class BoardDel extends Thread {

    private Handler handler = new Handler();

    private int wr_id;
    private Context context;
    private Activity activity;

    public BoardDel(Context context, Activity activity, int wr_id) {

        this.wr_id = wr_id;
        this.context = context;
        this.activity = activity;

        DialogBase.setProgress(0, context, "처리 중...");
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

        try {
            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDel:: 데이터 불러오기 요청 값:" + out.toString());

            String jsonData = RemoteDB.getHttpJsonData(BasicInfo.URI_BOARD_DEL, out.toString());

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDel:: 데이터 불러오기 요청 값:" + jsonData);

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

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDel:: 데이터 불러오기 result:" + result);
/*
	0:성공
	1:실패
	2:자신이 관리하는 게시판이 아니므로 삭제할 수 없습니다.
	3:자신의 권한보다 높은 권한의 회원이 작성한 글은 삭제할 수 없습니다.
	4:자신의 글이 아니므로 삭제할 수 없습니다.
	5:패스워드가 틀리므로 삭제할 수 없습니다.
	6:이 글과 관련된 답변글이 존재하므로 삭제 할 수 없습니다.\\n\\n우선 답변글부터 삭제하여 주십시오.
	7:이 글과 관련된 코멘트가 존재하므로 삭제 할 수 없습니다.\\n\\n코멘트가 {$board[bo_count_delete]}건 이상 달린 원글은 삭제할 수 없습니다.
	20:로그인 후 삭제하세요.
  */
            switch(result){
                case 20:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "로그인 후 수정하세요.", "확인"});
                    break;
                case 7:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "이 글과 관련된 댓글이 존재하므로 삭제 할 수 없습니다.", "확인"});
                    break;
                case 6:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "이 글과 관련된 답변글이 존재하므로 삭제 할 수 없습니다.", "확인"});
                    break;
                case 5:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "패스워드가 틀리므로 삭제할 수 없습니다.", "확인"});
                    break;
                case 4:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신의 글이 아니므로 삭제할 수 없습니다.", "확인"});
                    break;
                case 3:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신의 권한보다 높은 권한의 회원이 작성한 글은 삭제할 수 없습니다.", "확인"});
                    break;
                case 2:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신이 관리하는 게시판이 아니므로 삭제할 수 없습니다.", "확인"});
                    break;
                case 1:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "삭제를 실패하였습니다.", "확인"});
                    break;
                case 0:

                    Toast.makeText(context, "삭제되였습니다.", Toast.LENGTH_SHORT).show();

                    activity.finish();
                    
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            DialogBase.getProgress().dismiss();
        }
    }
}
