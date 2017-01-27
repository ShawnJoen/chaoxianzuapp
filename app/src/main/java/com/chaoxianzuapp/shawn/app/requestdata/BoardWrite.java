package com.chaoxianzuapp.shawn.app.requestdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.board.BoardView;
import com.chaoxianzuapp.shawn.app.db.ImageUpload;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shawn on 2015-09-09.
 */
public class BoardWrite extends Thread {

    private Handler handler = new Handler();

    private int wr_id;
    private int imageDel;
    private String wr_subject;
    private String wr_content;
    private String imageFilePath;

    private Context context;
    private Activity activity;

    public BoardWrite(Context context, Activity activity, String wr_subject_, String wr_content, int wr_id, String imageFilePath, int imageDel) {

        this.imageFilePath = imageFilePath;
        this.wr_id = wr_id;
        this.wr_subject = wr_subject_;
        this.wr_content = wr_content;
        this.context = context;
        this.activity = activity;
        this.imageDel = imageDel;

        DialogBase.setProgress(0, context, "로딩 중...");
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

        Map< String, String > param = new HashMap<String ,String>();
        param.put("bo_table", BasicInfo.activeBoardCateItem.getBo_table());

        if(wr_id == 0){

            param.put("w", "");
        }else {

            param.put("w", "u");
            param.put("wr_id", String.valueOf(wr_id));

            if(imageDel > 0){

                param.put("bf_file_del[0]", "1");//첫 이미지 삭제 지시
            }
        }
        param.put("wr_subject", wr_subject);
        param.put("wr_content", wr_content);
        param.put("bo_city", String.valueOf(BasicInfo.activeBoardCityItem.getCode()));

        if(BasicInfo.userInfo.size() > 0) {

            param.put("mb_id", BasicInfo.userInfo.get("mb_id"));
            param.put("token" , BasicInfo.userInfo.get("token"));
            param.put("DEVICE_TYPE" ,BasicInfo.DEVICE_TYPE);
            param.put("DEVICE_ID" ,BasicInfo.DEVICE_ID);
        }

        //Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardWrite:: 데이터 불러오기 요청 값:" + out.toString());

        File file = null;
        try {

            if(imageFilePath != null) {
                file = new File(imageFilePath);
            }
            String jsonData = ImageUpload.getInstance().toUploadFile(file, "bf_file[]", BasicInfo.URI_BOARD_WRITE, param);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardWrite:: 데이터 불러오기 요청 값:" + jsonData);
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
/*
	0:성공
	1:실패
	3:글이 존재하지 않습니다.글이 삭제되었거나 이동하였을 수 있습니다.
	5:글을 쓸 권한이 없습니다.
	6:관리자만 공지할 수 있습니다.
	7:공지에는 답변 할 수 없습니다.
	8:글을 답변할 권한이 없습니다.
	9:더 이상 답변하실 수 없습니다.답변은 10단계 까지만 가능합니다.
	10:더 이상 답변하실 수 없습니다.답변은 26개 까지만 가능합니다.
	12:동일한 내용을 연속해서 등록할 수 없습니다.
	13:제목을 입력하여 주십시오.
	14:자신이 관리하는 게시판이 아니므로 수정할 수 없습니다.
	15:자신의 권한보다 높은 권한의 회원이 작성한 글은 수정할 수 없습니다.
	16:자신의 글이 아니므로 수정할 수 없습니다.
	17:이미지 업로드를 실패하였습니다.
	20:로그인 후 수정하세요.
  */
            switch(result){
                case 20:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "로그인 후 수정하세요.", "확인"});
                    break;
                case 17:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "이미지 업로드를 실패하였습니다.", "확인"});
                    break;
                case 16:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신의 글이 아니므로 수정할 수 없습니다.", "확인"});
                    break;
                case 15:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신의 권한보다 높은 권한의 회원이 작성한 글은 수정할 수 없습니다.", "확인"});
                    break;
                case 14:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "자신이 관리하는 게시판이 아니므로 수정할 수 없습니다.", "확인"});
                    break;
                case 13:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "제목을 입력하여 주십시오.", "확인"});
                    break;
                case 12:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "동일한 내용을 연속해서 등록할 수 없습니다.", "확인"});
                    break;
                case 10:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "더 이상 답변하실 수 없습니다.답변은 26개 까지만 가능합니다.", "확인"});
                    break;
                case 9:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "더 이상 답변하실 수 없습니다.답변은 10단계 까지만 가능합니다.", "확인"});
                    break;
                case 8:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "글을 답변할 권한이 없습니다.", "확인"});
                    break;
                case 7:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "공지에는 답변 할 수 없습니다.", "확인"});
                    break;
                case 6:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "관리자만 공지할 수 있습니다.", "확인"});
                    break;
                case 5:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "글을 쓸 권한이 없습니다.", "확인"});
                    break;
                case 3:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "글이 존재하지 않습니다.글이 삭제되었거나 이동하였을 수 있습니다.", "확인"});
                    break;
                case 1:
                    DialogBase.setDialog(context, null, "ALERT", new String[]{"알람", "글쓰기를 실패하였습니다.", "확인"});
                    break;
                case 0:

                    if(wr_id == 0) {
                        Toast.makeText(context, "글쓰기를 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "글수정을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    String wr_id_ = new String(jsonObj.getString("wr_id").getBytes(), "UTF-8");

                    int wr_id = Integer.parseInt(wr_id_);

                    String bo_use_comment_ = new String(jsonObj.getString("bo_use_comment").getBytes(), "UTF-8");

                    int bo_use_comment = Integer.parseInt(bo_use_comment_);

                    Intent sendIntent = new Intent(context, BoardView.class);

                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    sendIntent.putExtra("mb_id", BasicInfo.userInfo.get("mb_id"));
                    sendIntent.putExtra("wr_id", wr_id);
                    sendIntent.putExtra("bo_use_comment", bo_use_comment);
                    context.startActivity(sendIntent);

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
