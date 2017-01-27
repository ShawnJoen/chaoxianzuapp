package com.chaoxianzuapp.shawn.app.board;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.BroadcastControl;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogSpinner;
import com.chaoxianzuapp.shawn.app.requestdata.BoardList;
import com.chaoxianzuapp.shawn.app.util.EditChangedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by shawn on 2015-08-08.
 */
public class BoardSearch extends AppCompatActivity {

    public BroadcastControl receiver = null;

    private Toolbar mToolbar;

    private RelativeLayout select_bo_city;

    private EditText search_view;

    private LinearLayout arrow_back;

    private ImageView search_view_x;

    private ListView listView;

    private AdView adView;

    private BoardListAdapter boardListAdapter;

    public static String keyword = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_search);

        adView = (AdView)findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        setSupportActionBar(mToolbar);

        arrow_back = (LinearLayout) findViewById(R.id.arrow_back);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_view = (EditText) findViewById(R.id.search_view);

        StringBuilder cateHint = new StringBuilder("선택하신 `<STRONG>");
        cateHint.append(BasicInfo.activeBoardCateItem.getLabel());
        cateHint.append("</STRONG>`에서 검색됩니다.");

        search_view.setHint(Html.fromHtml(cateHint.toString()));

        search_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                BasicInfo.softInputMethod(getApplicationContext(), search_view, 1);
            }
        }, 200);

        search_view_x = (ImageView)findViewById(R.id.search_view_x);

        search_view_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = "";
                search_view.setText(keyword);
            }
        });

        search_view.addTextChangedListener(new EditChangedListener(search_view ,search_view_x));

        /* ListView 시작*/
        listView = (ListView)findViewById(R.id.mainCastList);

        boardListAdapter = new BoardListAdapter(this);

        search_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    boardListAdapter.clear();

                    keyword = search_view.getText().toString();
                    if (!"".equals(keyword)) {

                        new BoardList(BoardSearch.this ,boardListAdapter).start();

                        BasicInfo.softInputMethod(getApplicationContext(), search_view, 0);
                    }else{

                        boardListAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        });

        TextView textView = (TextView)findViewById(R.id.empty);

        textView.setText("검색된 내용이 없습니다.");

        listView.setEmptyView(textView);

        listView.setAdapter(boardListAdapter);

        new BoardIListListener(boardListAdapter,listView , this ,this);//ListView 이벤트 걸기

        /*게시판 카테 텍스트 넣기*/
        TextView bo_city_ = (TextView)findViewById(R.id.bo_city_);

        bo_city_.setText(BasicInfo.activeBoardCityItem.getLabel());

        /* 지역*/
        select_bo_city = (RelativeLayout)findViewById(R.id.bo_city);
        select_bo_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSpinner.setDialog(BoardSearch.this, BoardSearch.this, v, "BO_CITY", boardListAdapter, new String[]{keyword});
            }
        });

        keyword = "";
    }

    @Override
    protected void onPause() {Log.d(BasicInfo.SERVICE_TAG, "서치 onPause ::: .");
        adView.pause();
        super.onPause();
        if(BasicInfo.CLOSE_ALL_PROCESS == false){
            receiver.unregistBroad();
        }
    }
    @Override
    protected void onResume() {Log.d(BasicInfo.SERVICE_TAG, "서치 onResume ::: ." + BasicInfo.ACTIVITY_ENABLE);
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
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_BOARD_SEARCH;//활성화된 액티비티 셋
            receiver.registBroad(new String[]{ConnectivityManager.CONNECTIVITY_ACTION, BasicInfo.ACTIVITY_ENABLE});//인터넷 상태 , 액티비티 별 소켓 (브로드캐스트 등록
        }
    }
    @Override
    public void onDestroy() {Log.d(BasicInfo.SERVICE_TAG, "서치 onDestroy ::: .");
        adView.destroy();
        super.onDestroy();
        /*if(SocketControl.socket != null) {
            SocketControl.socket.disconnect();
        }*/

        //stopService(BasicInfo.SERVICE_SOCKET_INTENT);//옵 종료시 서비스도 종료
    }
}