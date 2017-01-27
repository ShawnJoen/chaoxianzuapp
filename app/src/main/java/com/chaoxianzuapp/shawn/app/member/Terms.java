package com.chaoxianzuapp.shawn.app.member;

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
import android.widget.CheckBox;
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
import com.chaoxianzuapp.shawn.app.board.BoardComment;
import com.chaoxianzuapp.shawn.app.board.BoardCommentAdapter;
import com.chaoxianzuapp.shawn.app.board.BoardWrite;
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
 * Created by shawn on 2015-10-11.
 */
public class Terms extends AppCompatActivity {

    public BroadcastControl receiver = null;

    private Toolbar mToolbar;

    private Handler handler;

    //private AdView adView;
    private Button agree;

    private CheckBox agree_member;

    private CheckBox agree_private;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_terms);
/*
        adView = (AdView)findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        mToolbar.setTitle("이용 약관");

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.mipmap.arrow_back_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        agree_member = (CheckBox) findViewById(R.id.agree_member);

        agree_private = (CheckBox) findViewById(R.id.agree_private);

        agree = (Button) findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(agree_member.isChecked() && agree_private.isChecked()){

                    Intent toJoin = new Intent(Terms.this, Join.class);
                    startActivity(toJoin);

                    finish();
                }else{

                    Toast.makeText(getApplicationContext() , "약관 동의가 필요합니다." , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onPause() { Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onPause ::: .");
        //adView.pause();
        super.onPause();
        if(BasicInfo.CLOSE_ALL_PROCESS == false){
            receiver.unregistBroad();
        }
    }
    @Override
    protected void onResume() {Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onResume ::: ." + BasicInfo.ACTIVITY_ENABLE);
        super.onResume();
        //adView.resume();
        /*
        * 메인에서 중지 팝 뜨면 BasicInfo.STATE_NETWORK == false로 여기 통과 .
        * 아니면 룸에서 중지되면 unregistBroad에서 BasicInfo.STATE_NETWORK = true해서 여기 통과안하고
        * */
        if(BasicInfo.STATE_NETWORK == false) {
            finish();
        }
        if(BasicInfo.CLOSE_ALL_PROCESS == false){//메인에서 앱중지 팝 뜨면 이판단은 이미 통과 후 종료 팝 듬
            receiver = new BroadcastControl(this , this);
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_MEMBER_TERMS;//활성화된 액티비티 셋
            receiver.registBroad(new String[]{ConnectivityManager.CONNECTIVITY_ACTION, BasicInfo.ACTIVITY_ENABLE});//인터넷 상태 , 액티비티 별 소켓 (브로드캐스트 등록
        }
    }
    @Override
    public void onDestroy() {Log.d(BasicInfo.SERVICE_TAG, "게시글 보기 onDestroy ::: .");
        //adView.destroy();
        super.onDestroy();
        /*if(SocketControl.socket != null) {
            SocketControl.socket.disconnect();
        }*/
        //stopService(BasicInfo.SERVICE_SOCKET_INTENT);//옵 종료시 서비스도 종료
    }
}
