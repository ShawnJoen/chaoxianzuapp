package com.chaoxianzuapp.shawn.app.board;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.BroadcastControl;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogLogin;
import com.chaoxianzuapp.shawn.app.requestdata.BoardCommentWrite;
import com.chaoxianzuapp.shawn.app.util.EditChangedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by shawn on 2015-09-02.
 */
public class BoardComment extends AppCompatActivity {

    public BroadcastControl receiver = null;

    private Toolbar mToolbar;
    private String mb_id;
    private int wr_id;
    private int city;
    public static String wr_subject;

    private EditText commentEdit;

    private Button commentSend;

    private ListView commentList;
    private BoardCommentAdapter boardCommentAdapter;

    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_comment);

        adView = (AdView)findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();//새 액티비티에서 인텐트로 보낸값을 받기
        if(intent != null){

            wr_id = intent.getIntExtra("wr_id", 0);
            mb_id = intent.getStringExtra("mb_id");
            city = intent.getIntExtra("city", 0);
            //wr_subject = intent.getStringExtra("wr_subject");

            TextView wr_subject_ = (TextView) findViewById(R.id.wr_subject);
            wr_subject_.setText(wr_subject);

            TextView sub_title = (TextView) findViewById(R.id.sub_title);//게시판 카테 제목
            //sub_title.setText(BasicInfo.activeBoardCateItem.getLabel());
            sub_title.setText(BasicInfo.activeBoardCateItem.getLabel() + " ("+ BasicInfo.BO_CITY.get(city) +")");

            LinearLayout comment_layout = (LinearLayout)findViewById(R.id.comment_layout);
            LinearLayout comment_edit_layout = (LinearLayout)findViewById(R.id.comment_edit_layout);

            comment_layout.setVisibility(View.VISIBLE);
            comment_edit_layout.setVisibility(View.VISIBLE);

            //int wr_comment = intent.getIntExtra("wr_comment", 0);

            //Toast.makeText(getApplicationContext(), "댓글수." + wr_comment, Toast.LENGTH_SHORT).show();

            commentList = (ListView) findViewById(R.id.commentList);
            boardCommentAdapter = new BoardCommentAdapter(this, this);
            TextView textView = (TextView) findViewById(R.id.empty);

            textView.setText("댓글이 없습니다.");
            commentList.setEmptyView(textView);
            commentList.setAdapter(boardCommentAdapter);

            new com.chaoxianzuapp.shawn.app.requestdata.BoardComment(BoardComment.this, BoardComment.this, boardCommentAdapter, commentList, wr_id).start();

            commentEdit = (EditText) findViewById(R.id.commentEdit);

            commentEdit.addTextChangedListener(new EditChangedListener(commentEdit, 60));

            commentSend = (Button) findViewById(R.id.commentSend);

            commentSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (BasicInfo.userInfo.size() == 0) {

                        DialogLogin.login(BoardComment.this, BoardComment.this);
                    } else {

                        String c = commentEdit.getText().toString();

                        if (c != null && "".equals(c) == false) {

                            new BoardCommentWrite(BoardComment.this, BoardComment.this, wr_id, mb_id,c).start();
                            //Toast.makeText(getApplicationContext(), "commentSend::" + c, Toast.LENGTH_SHORT).show();

                            BasicInfo.softInputMethod(getApplicationContext(), v, 0);
                        }
                    }
                }
            });

            commentList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    /**
                     * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=4（这里因为服务端有4页数据）时，加载更多数据。
                     */
                    if (BasicInfo.is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && BasicInfo.pageNo <= BasicInfo.totalPage) {

                        new com.chaoxianzuapp.shawn.app.requestdata.BoardComment(BoardComment.this, BoardComment.this, boardCommentAdapter, commentList, wr_id).start();
                    } else if (BasicInfo.pageNo > BasicInfo.totalPage) {
                        /**
                         * 如果pageNo>4则表示，服务端没有更多的数据可供加载了。
                         */
                    }
                }
                /**
                 * 当：第一个可见的item（firstVisibleItem）+可见的item的个数（visibleItemCount）=所有的item总数的时候，
                 * is_divPage变为TRUE，这个时候才会加载数据。
                 */
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    BasicInfo.is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
                }
            });

            commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //BoardItem item = (BoardItem) boardListAdapter.getItem(position);
                }
            });
        }else{

            Toast.makeText(getApplicationContext(), "게시글 보기 오류입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        mToolbar.setTitle("댓글 보기");

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.mipmap.arrow_back_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onPause() {
        adView.pause();
        Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onPause ::: .");
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
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_BOARD_COMMENT;//활성화된 액티비티 셋
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
