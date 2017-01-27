package com.chaoxianzuapp.shawn.app.board;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.BroadcastControl;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;
import com.chaoxianzuapp.shawn.app.dialog.DialogBoard;
import com.chaoxianzuapp.shawn.app.requestdata.BoardCommentWrite;
import com.chaoxianzuapp.shawn.app.requestdata.BoardDetail;
import com.chaoxianzuapp.shawn.app.requestdata.BoardGood;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;
import com.chaoxianzuapp.shawn.app.util.EditChangedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by shawn on 2015-08-13.
 */
public class BoardView extends AppCompatActivity {

    public BroadcastControl receiver = null;

    private Toolbar mToolbar;

    private Handler handler;

    private ScrollView sub_scroll;
    private String mb_id;
    private int wr_id;
    private int city;
    private int bo_use_comment;

    private EditText commentEdit;

    private Button commentSend;

    private LinearLayout is_good;
    private LinearLayout is_nogood;

    private ImageView wr_good_;
    private ImageView wr_nogood_;
    private TextView wr_good;
    private TextView wr_nogood;

    private ListView commentList;
    private BoardCommentAdapter boardCommentAdapter;

    private TextView wr_subject;
    private TextView wr_content;

    private AdView adView;

    public static String imgUrl1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        adView = (AdView)findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        imgUrl1 = null;

        BasicInfo.bg_flag = 0;

        Intent intent = getIntent();//새 액티비티에서 인텐트로 보낸값을 받기
        if(intent != null){

            wr_subject = (TextView) findViewById(R.id.wr_subject);
            wr_content = (TextView) findViewById(R.id.wr_content);

            wr_id = intent.getIntExtra("wr_id", 0);
            mb_id = intent.getStringExtra("mb_id");
            city = intent.getIntExtra("city", 0);

            bo_use_comment = intent.getIntExtra("bo_use_comment" , 0);

            init();

        }else{

            Toast.makeText(getApplicationContext(), "게시글 보기 오류입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        mToolbar.setTitle("글보기");

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.mipmap.arrow_back_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        is_good = (LinearLayout)findViewById(R.id.is_good);

        is_nogood = (LinearLayout)findViewById(R.id.is_nogood);

        wr_good_ = (ImageView)findViewById(R.id.wr_good_);

        wr_nogood_ = (ImageView)findViewById(R.id.wr_nogood_);

        wr_good = (TextView)findViewById(R.id.wr_good);

        wr_nogood = (TextView)findViewById(R.id.wr_nogood);

        is_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BasicInfo.userInfo.size() == 0){

                    //DialogLogin.login(BoardView.this,BoardView.this);
                    DialogBase.setDialog(BoardView.this, null, "ALERT", new String[]{"알람", "로그인 후 사용하세요.", "확인"});
                }else {

                    if(mb_id.equals(BasicInfo.userInfo.get("mb_id"))){

                        Toast.makeText(getApplicationContext(), "본인 글은 `좋아요` 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }else {

                        if(BasicInfo.bg_flag > 0){
                            isAlreadySetGood();
                        }else{

                            new BoardGood(BoardView.this, BoardView.this, wr_id , "good" , wr_good_ ,wr_good).start();
                        }
                    }
                }
            }
        });
        is_nogood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BasicInfo.userInfo.size() == 0) {

                    //DialogLogin.login(BoardView.this, BoardView.this);
                    DialogBase.setDialog(BoardView.this, null, "ALERT", new String[]{"알람", "로그인 후 사용하세요.", "확인"});
                } else {

                    if (mb_id.equals(BasicInfo.userInfo.get("mb_id"))) {

                        Toast.makeText(getApplicationContext(), "본인 글은 `싫어요` 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        if(BasicInfo.bg_flag > 0){
                            isAlreadySetGood();
                        }else{

                            new BoardGood(BoardView.this, BoardView.this, wr_id , "nogood" ,wr_nogood_ ,wr_nogood).start();
                        }
                    }
                }
            }
        });
    }
    private void init(){

        if(bo_use_comment == 1) {

            handler = new Handler();

            LinearLayout comment_layout = (LinearLayout)findViewById(R.id.comment_layout);
            LinearLayout comment_edit_layout = (LinearLayout)findViewById(R.id.comment_edit_layout);

            final View comment_view =  getLayoutInflater().inflate(R.layout.board_comment_more, null);

            comment_layout.setVisibility(View.VISIBLE);
            comment_edit_layout.setVisibility(View.VISIBLE);

            //final int wr_comment = intent.getIntExtra("wr_comment", 0);

            //Toast.makeText(getApplicationContext(), "댓글수." + wr_comment, Toast.LENGTH_SHORT).show();

            commentList = (ListView) findViewById(R.id.commentList);
            boardCommentAdapter = new BoardCommentAdapter(this,this);
            TextView textView = (TextView) findViewById(R.id.empty);

            textView.setText("댓글이 없습니다.");
            commentList.setEmptyView(textView);
            commentList.addFooterView(comment_view);    //设置列表底部视图

            commentList.setAdapter(boardCommentAdapter);

            final TextView comment_more = (TextView)comment_view.findViewById(R.id.comment_more);

            comment_more.setText("댓글 더보기");

            comment_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            BasicInfo.setInitPaging();/*댓글 페이징 초기화*/

                            Intent intent = new Intent(BoardView.this, BoardComment.class);

                            intent.putExtra("wr_id", wr_id);
                            intent.putExtra("city", city);
                            intent.putExtra("mb_id", mb_id);

                            //TextView wr_subject = (TextView) findViewById(R.id.wr_subject);//게시판 제목
                            //intent.putExtra("wr_subject", wr_subject.getText().toString());

                            BoardComment.wr_subject = wr_subject.getText().toString();
                            //intent.putExtra("wr_comment", wr_comment);

                            startActivityForResult(intent, BasicInfo.BOARD_COMMENT_ACTIVITY);

                        }
                    }, 500);
                }
            });

            new BoardDetail(this, this, boardCommentAdapter, commentList, wr_id).start();//데이터 불러오기

            commentEdit = (EditText) findViewById(R.id.commentEdit);

            commentEdit.addTextChangedListener(new EditChangedListener(commentEdit, 60));

            commentSend = (Button) findViewById(R.id.commentSend);

            commentSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (BasicInfo.userInfo.size() == 0) {

                        //DialogLogin.login(BoardView.this, BoardView.this);
                        DialogBase.setDialog(BoardView.this, null, "ALERT", new String[]{"알람", "로그인 후 사용하세요.", "확인"});
                    } else {

                        String c = commentEdit.getText().toString();

                        if (c != null && "".equals(c) == false) {

                            new BoardCommentWrite(BoardView.this, BoardView.this,wr_id,mb_id,c).start();
                            //Toast.makeText(getApplicationContext(), "commentSend::" + c, Toast.LENGTH_SHORT).show();

                            BasicInfo.softInputMethod(getApplicationContext(), v, 0);
                        }
                    }
                }
            });
        }else{

            sub_scroll = (ScrollView)findViewById(R.id.sub_scroll);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)sub_scroll.getLayoutParams();
            layoutParams.setMargins(0, BaseUtil.pixelToDp(56), 0, BaseUtil.pixelToDp(46));
            sub_scroll.setLayoutParams(layoutParams);

            new BoardDetail(this, this, wr_id).start();//데이터 불러오기
        }
    }
    private void isAlreadySetGood(){

        switch(BasicInfo.bg_flag){
            case 2:
                Toast.makeText(getApplicationContext(), "이미 `싫어요`를 하였습니다.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getApplicationContext(), "이미 `좋아요`를 하였습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case BasicInfo.BOARD_COMMENT_ACTIVITY:

                Intent sendIntent = new Intent(this, BoardView.class);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sendIntent.putExtra("mb_id", mb_id);
                sendIntent.putExtra("wr_id", wr_id);
                sendIntent.putExtra("city", city);
                sendIntent.putExtra("bo_use_comment", bo_use_comment);
                startActivity(sendIntent);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//invalidateOptionsMenu(); // onCreateOptionsMenu 을 실행해줌

        getMenuInflater().inflate(R.menu.board_view, menu);

        MenuItem write = menu.findItem(R.id.write);
        write.setTitle("쓰기");

        MenuItem modify = menu.findItem(R.id.modify);
        MenuItem delete = menu.findItem(R.id.delete);

        if(mb_id.equals(BasicInfo.userInfo.get("mb_id"))){

            modify.setTitle("수정");
            delete.setTitle("삭제");
        }else{
            modify.setVisible(false);
            delete.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(BasicInfo.userInfo.size() == 0){

            //DialogLogin.login(this,this);
            DialogBase.setDialog(BoardView.this, null, "ALERT", new String[]{"알람", "로그인 후 사용하세요.", "확인"});
        }else{

            if(item.getItemId() == R.id.delete){

                DialogBoard.setDialog(BoardView.this, BoardView.this, wr_id, "BOARD_DEL", new String[]{"알람", "삭제하겠습니까?", "확인", "취소"});
            }else{

                Intent intent = new Intent(BoardView.this, BoardWrite.class);

                if(item.getItemId() == R.id.modify) {

                    intent.putExtra("wr_id", wr_id);
                    intent.putExtra("city", city);
                    intent.putExtra("wr_subject", wr_subject.getText().toString());
                    intent.putExtra("wr_content", wr_content.getText().toString());
                    intent.putExtra("imgUrl1", imgUrl1);
                }
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() { Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onPause ::: .");
        adView.pause();
        super.onPause();
        if(BasicInfo.CLOSE_ALL_PROCESS == false){
            receiver.unregistBroad();
        }
    }
    @Override
    protected void onResume() {Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onResume ::: ." + BasicInfo.ACTIVITY_ENABLE);
        super.onResume();
        adView.resume();



        /*
        * 메인에서 중지 팝 뜨면 BasicInfo.STATE_NETWORK == false로 여기 통과 .
        * 아니면 룸에서 중지되면 unregistBroad에서 BasicInfo.STATE_NETWORK = true해서 여기 통과안하고
        * */
        if(BasicInfo.STATE_NETWORK == false) {
            finish();
        }
        if(BasicInfo.CLOSE_ALL_PROCESS == false){//메인에서 앱중지 팝 뜨면 이판단은 이미 통과 후 종료 팝 듬
            receiver = new BroadcastControl(this , this);
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_BOARD_VIEW;//활성화된 액티비티 셋
            receiver.registBroad(new String[]{ConnectivityManager.CONNECTIVITY_ACTION, BasicInfo.ACTIVITY_ENABLE});//인터넷 상태 , 액티비티 별 소켓 (브로드캐스트 등록
        }
    }
    @Override
    public void onDestroy() {Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onDestroy ::: .");
        adView.destroy();
        super.onDestroy();
        /*if(SocketControl.socket != null) {
            SocketControl.socket.disconnect();
        }*/
        //stopService(BasicInfo.SERVICE_SOCKET_INTENT);//옵 종료시 서비스도 종료
    }
}
