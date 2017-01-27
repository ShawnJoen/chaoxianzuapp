package com.chaoxianzuapp.shawn.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.board.BoardFragment;
import com.chaoxianzuapp.shawn.app.board.BoardSearch;
import com.chaoxianzuapp.shawn.app.board.BoardWrite;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;
import com.chaoxianzuapp.shawn.app.dialog.DialogLogin;
import com.chaoxianzuapp.shawn.app.navigationdrawer.NavigationDrawerCallbacks;
import com.chaoxianzuapp.shawn.app.navigationdrawer.NavigationDrawerFragment;
import com.chaoxianzuapp.shawn.app.navigationdrawer.PlaceholderFragment;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    public BroadcastControl receiver = null;

    public static Toolbar mToolbar;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private int sectionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }
    @Override
    public void onBackPressed() {

        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {

            if(BasicInfo.setDoubleClick(2000)) {

                super.onBackPressed();
            }else{

                Toast.makeText(getApplicationContext() , "\'뒤로\' 버튼을 한 번 더 누르시면 종료됩니다." , Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//Toast.makeText(getApplicationContext() , "11111111" , Toast.LENGTH_SHORT).show();
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            switch(sectionNumber){
                case 1:

                    getMenuInflater().inflate(R.menu.main, menu);
                    break;
                case 2:

                    getMenuInflater().inflate(R.menu.board_notice, menu);
                    break;
            }

            mToolbar.setTitle(BasicInfo.activeBoardCateItem.getLabel());
            return true;
        }else{

            TextView mleft_login_logout = (TextView)findViewById(R.id.mleft_login_logout);
            TextView mleft_login_msg = (TextView)findViewById(R.id.mleft_login_msg);
            ImageView mleft_img = (ImageView)findViewById(R.id.mleft_img);
            RelativeLayout mleft_set = (RelativeLayout)findViewById(R.id.mleft_set);

            if(BasicInfo.userInfo.size() > 0){

                String mb_photo = BasicInfo.userInfo.get("mb_photo");

                if("".equals(mb_photo)) {

                    mleft_img.setImageResource(R.mipmap.thumb_default);
                }else{

                    BaseUtil.setImgUrl(0, new AsyncImageLoader(this), mleft_img,null, mb_photo);
                }

                mleft_set.setVisibility(View.VISIBLE);

                mleft_login_logout.setText("로그아웃");

                StringBuilder onUserMsg = new StringBuilder(BasicInfo.userInfo.get("mb_nick"));
                onUserMsg.append("(");
                onUserMsg.append(BasicInfo.userInfo.get("mb_id"));
                onUserMsg.append(")");

                mleft_login_msg.setText(onUserMsg.toString());
            }else{

                mleft_img.setImageResource(R.mipmap.header_menu_thumbnail);

                mleft_set.setVisibility(View.GONE);

                mleft_login_logout.setText("로그인");

                mleft_login_msg.setText("게시판에 글을 작성하기 위해 로그인이 필요합니다.");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.write:

                if(BasicInfo.userInfo.size() == 0){

                    DialogLogin.login(this, null);
                }else{

                    Intent intent = new Intent(MainActivity.this, BoardWrite.class);

                    startActivityForResult(intent, BasicInfo.BOARD_WRITE_ACTIVITY);
                }
                return true;
            case R.id.reload:

                onNavigationDrawerItemSelected(0);
                return true;
            case R.id.search:

                Intent intent = new Intent(getApplicationContext(),BoardSearch.class);//새로만든 액티비티로 가기
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(BasicInfo.CLOSE_ALL_PROCESS == false){
            receiver.unregistBroad();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(BasicInfo.STATE_NETWORK == false) {
            finish();
        }
        if(BasicInfo.CLOSE_ALL_PROCESS == false){//메인에서 앱중지 팝 뜨면 이판단은 이미 통과 후 종료 팝 듬
            receiver = new BroadcastControl(this , this);
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_MAIN;//활성화된 액티비티 셋
            receiver.registBroad(new String[]{ConnectivityManager.CONNECTIVITY_ACTION, BasicInfo.ACTIVITY_ENABLE});//인터넷 상태 , 액티비티 별 소켓 (브로드캐스트 등록
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(!"1".equals(BasicInfo.userSet.get("auto_login"))){//자동 로그인 아니면 회원 정보 지우기

            BasicInfo.userInfo.clear();
            BasicInfo.userSet.clear();
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch(position){
            case 3:
                DialogBase.setDialog(this, this, "CONFIRM", new String[]{"알람", "종료하겠습니까?", "확인","취소"});
                break;
            case 2:
                Intent site = new Intent(Intent.ACTION_VIEW , Uri.parse(BasicInfo.BASE_URI));
                startActivity(site);
                break;
            default:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
        }
    }
    public void onSectionAttached(int number) {//메뉴 타이틀 교체 처리
        sectionNumber = number;
    }
}
