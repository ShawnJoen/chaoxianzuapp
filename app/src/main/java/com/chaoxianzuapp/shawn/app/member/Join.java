package com.chaoxianzuapp.shawn.app.member;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.BroadcastControl;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;
import com.chaoxianzuapp.shawn.app.dialog.DialogBoard;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by shawn on 2015-10-11.
 */
public class Join extends AppCompatActivity {

    public BroadcastControl receiver = null;

    private Toolbar mToolbar;

    private Handler handler;

    //private AdView adView;
    private EditText mb_id;
    private TextView mb_id2;
    private EditText mb_password;
    private EditText mb_password_re;
    private RadioButton male;
    private RadioButton female;
    private EditText mb_nick;
    private EditText mb_hp;
    private ImageView photo;
    private ImageView photo_btn;

    private LinearLayout mb_password_layout;

    private Button join_btn;

    private File imageFile;
    private String imageFilePath = null;
    private String mb_photo;
    private Bitmap bitmap = null;

    private String mb_id_;
    private String mb_password_;
    private String mb_password_re_;
    private String gender;
    private String mb_nick_;
    private String mb_hp_;

    private Pattern pat_mb_id = Pattern.compile("[a-zA-Z0-9]{4,30}");
    private Pattern pat_mb_password = Pattern.compile("[a-zA-Z0-9]{4,30}");
    private Pattern pat_mb_hp = Pattern.compile("[0-9]{10,14}");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_join);
/*
        adView = (AdView)findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

        Intent intent = getIntent();//새 액티비티에서 인텐트로 보낸값을 받기
        if(intent != null){

            mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

            try {
                imageFile = createFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            mb_id = (EditText)findViewById(R.id.mb_id);
            mb_id2 = (TextView)findViewById(R.id.mb_id2);
            mb_password = (EditText)findViewById(R.id.mb_password);
            mb_password_re = (EditText)findViewById(R.id.mb_password_re);
            male = (RadioButton)findViewById(R.id.male);
            female = (RadioButton)findViewById(R.id.female);
            mb_nick = (EditText)findViewById(R.id.mb_nick);
            mb_hp = (EditText)findViewById(R.id.mb_hp);
            photo = (ImageView)findViewById(R.id.photo);
            photo_btn = (ImageView)findViewById(R.id.photo_btn);
            join_btn = (Button) findViewById(R.id.join_btn);

            mb_password_layout = (LinearLayout)findViewById(R.id.mb_password_layout);

            if(BasicInfo.userInfo.size() > 0){//수정

                mToolbar.setTitle("회원 정보 수정");

                join_btn.setText("수정");

                mb_photo = BasicInfo.userInfo.get("mb_photo");

                if("".equals(mb_photo)) {

                    photo.setImageResource(R.mipmap.thumb_default);
                }else{

                    BaseUtil.setImgUrl(1 , new AsyncImageLoader(Join.this), photo, null, mb_photo);
                }

                mb_id.setVisibility(View.GONE);
                mb_password_layout.setVisibility(View.GONE);
                mb_id2.setVisibility(View.VISIBLE);
                mb_id.setText(BasicInfo.userInfo.get("mb_id"));
                mb_id2.setText(BasicInfo.userInfo.get("mb_id"));
                mb_nick.setText(BasicInfo.userInfo.get("mb_nick"));
                mb_hp.setText(BasicInfo.userInfo.get("mb_hp"));

                if("F".equals(BasicInfo.userInfo.get("mb_sex"))) {
                    female.setChecked(true);
                }else{
                    male.setChecked(true);
                }
            }else{//가입

                mToolbar.setTitle("회원 가입");

                join_btn.setText("등록");

                mb_hp.setText(BasicInfo.HP);

                mb_id.setVisibility(View.VISIBLE);
                mb_password_layout.setVisibility(View.VISIBLE);
                mb_id2.setVisibility(View.GONE);
                male.setChecked(true);
            }
        }else{

            Toast.makeText(getApplicationContext(), "접근 오류입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoard.setCamera(Join.this, Join.this, imageFile, new String[]{"사진 등록/수정"});
            }
        });

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mb_id_ = mb_id.getText().toString();
                mb_password_ = mb_password.getText().toString();
                mb_password_re_ = mb_password_re.getText().toString();
                mb_nick_ = mb_nick.getText().toString();
                mb_hp_ = mb_hp.getText().toString();

                if (!pat_mb_id.matcher(mb_id_).matches()) {
                    DialogBase.setDialog(Join.this, null, "ALERT", new String[]{"알람", "아이디는 숫자 및 영어 문자 조합으로 4자 이상 30자 이하로 입력하세요.", "확인"});
                    return;
                }

                if(BasicInfo.userInfo.size() == 0) {

                    if (!pat_mb_password.matcher(mb_password_).matches()) {
                        DialogBase.setDialog(Join.this, null, "ALERT", new String[]{"알람", "비밀번호는 숫자 및 영어 문자 조합으로 4자 이상 30자 이하로 입력하세요.", "확인"});
                        return;
                    }

                    if (!mb_password_.equalsIgnoreCase(mb_password_re_)) {
                        DialogBase.setDialog(Join.this, null, "ALERT", new String[]{"알람", "비밀번호 확인이 동일하지 않습니다.", "확인"});
                        return;
                    }
                }

                if ("".equals(mb_nick_.trim()) == true) {
                    DialogBase.setDialog(Join.this, null, "ALERT", new String[]{"알람", "닉네임을 입력하세요.", "확인"});
                    return;
                }

                if (!pat_mb_hp.matcher(mb_hp_).matches()) {
                    DialogBase.setDialog(Join.this, null, "ALERT", new String[]{"알람", "휴대폰 번호는 숫자 10자 이상 14자 이하로 입력하세요.", "확인"});
                    return;
                }

                if(male.isChecked()){
                    gender = "M";
                }else if(female.isChecked()){
                    gender = "F";
                }else{
                    DialogBase.setDialog(Join.this, null, "ALERT", new String[]{"알람", "성별을 선택하세요.", "확인"});
                    return;
                }

                new com.chaoxianzuapp.shawn.app.requestdata.Join(Join.this, Join.this, mb_id_,
                        (BasicInfo.userInfo.size() == 0 ? BaseUtil.md5(mb_password_) : ""), mb_nick_ ,mb_hp_ , gender ,imageFilePath).start();

                BasicInfo.softInputMethod(getApplicationContext(), v, 0);
            }
        });

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.mipmap.arrow_back_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private File createFile() throws IOException {

        String imageFileName = BasicInfo.setDateText(0 , 0) + ".jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File curFile = new File(storageDir, imageFileName);

        return curFile;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            if (imageFile != null) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                if (bitmap != null) {
                    bitmap.recycle();
                }
                try {

                    switch(requestCode) {
                        case BasicInfo.IMAGE_CAPTURE_ACTIVITY:

                            imageFilePath = imageFile.getAbsolutePath();

                            bitmap = BitmapFactory.decodeFile(imageFilePath , options);
                            break;
                        default:

                            Uri uri = data.getData();

                            imageFilePath = BaseUtil.getRealPathFromURI(this, uri);

                            ContentResolver cr = this.getContentResolver();
                            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
                    }

                    //photo_layout.setVisibility(View.VISIBLE);
                    photo.setImageBitmap(bitmap);

                } catch (Exception e) {

                    imageFilePath = null;

                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "파일 생성을 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_MEMBER_JOIN;//활성화된 액티비티 셋
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
