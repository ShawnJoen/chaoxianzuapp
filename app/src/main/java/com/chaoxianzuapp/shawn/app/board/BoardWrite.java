package com.chaoxianzuapp.shawn.app.board;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.BroadcastControl;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogBase;
import com.chaoxianzuapp.shawn.app.dialog.DialogBoard;
import com.chaoxianzuapp.shawn.app.dialog.DialogSpinner;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by shawn on 2015-09-07.
 */
public class BoardWrite extends AppCompatActivity {

    public BroadcastControl receiver = null;

    private Toolbar mToolbar;

    private int wr_id;
    private int imageDel;
    private ScrollView scrollView;
    private TextView sub_title;
    private EditText wr_subject;
    private EditText wr_content;
    private Button write_btn;
    private ImageView photo_btn;

    private LinearLayout photo_layout;
    private File imageFile;
    private String imageFilePath = null;
    private ImageView photo;
    private String imgUrl1;

    private String wr_subject_;
    private String wr_content_;

    private Bitmap bitmap = null;
    private RelativeLayout select_bo_city;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        TextView sub_title = (TextView) findViewById(R.id.sub_title);//게시판 카테 제목
        sub_title.setText(BasicInfo.activeBoardCateItem.getLabel());

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        wr_subject = (EditText) findViewById(R.id.wr_subject);
        wr_content = (EditText) findViewById(R.id.wr_content);
        //imgUrl1 = (ImageView) findViewById(R.id.photo);
        write_btn = (Button) findViewById(R.id.write_btn);
        photo_btn = (ImageView) findViewById(R.id.photo_btn);
        photo_layout = (LinearLayout) findViewById(R.id.photo_layout);
        photo = (ImageView) findViewById(R.id.photo);

        try {
            imageFile = createFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Intent intent = getIntent();

        wr_id = intent.getIntExtra("wr_id", 0);

        if(wr_id == 0){

            mToolbar.setTitle("글 작성하기");
            write_btn.setText("등록");
        }else{

            wr_subject_ = intent.getStringExtra("wr_subject");
            wr_subject.setText(wr_subject_);
            wr_content_ = intent.getStringExtra("wr_content");
            wr_content.setText(wr_content_);

            imgUrl1 = intent.getStringExtra("imgUrl1");

            if(imgUrl1 != null) {

                //Toast.makeText(BoardWrite.this , imgUrl1 , Toast.LENGTH_LONG).show();

                BaseUtil.setImgUrl(2, new AsyncImageLoader(BoardWrite.this), photo, photo_layout, imgUrl1);
            }

            mToolbar.setTitle("글 수정하기");
            write_btn.setText("수정");
        }

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoard.setCamera(BoardWrite.this, BoardWrite.this, imageFile, new String[]{"사진 등록/수정"});
            }
        });

        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wr_subject_ = wr_subject.getText().toString();
                wr_content_ = wr_content.getText().toString();

                if (BasicInfo.activeBoardCityItem.getCode() == 99) {
                    DialogBase.setDialog(BoardWrite.this, null, "ALERT", new String[]{"알람", "지역을 선택하세요.", "확인"});
                    return;
                }
                if (wr_subject_ == null || "".equals(wr_subject_) == true || wr_subject_.length() < 2 || wr_subject_.length() > 50) {
                    DialogBase.setDialog(BoardWrite.this, null, "ALERT", new String[]{"알람", "제목을 2~50자 입력하세요.", "확인"});
                    return;
                }
                if (wr_content_ == null || "".equals(wr_content_) == true || wr_content_.length() < 10 || wr_content_.length() > 2000) {
                    DialogBase.setDialog(BoardWrite.this, null, "ALERT", new String[]{"알람", "내용을 10~2000자 입력하세요.", "확인"});
                    return;
                }

                new com.chaoxianzuapp.shawn.app.requestdata.BoardWrite(BoardWrite.this, BoardWrite.this, wr_subject_, wr_content_, wr_id, imageFilePath ,imageDel).start();

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

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BasicInfo.setDoubleClick(1000)) {

                    if(wr_id > 0) {

                        imageDel = 1;

                        Toast.makeText(BoardWrite.this, "수정을 누르면 이미지 삭제가 적용됩니다.", Toast.LENGTH_LONG).show();
                    }
                    photo_layout.setVisibility(View.GONE);

                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    imageFilePath = null;
                }
            }
        });

        /*게시판 카테 텍스트 넣기*/
        TextView bo_city_ = (TextView)findViewById(R.id.bo_city_);

        bo_city_.setText(BasicInfo.activeBoardCityItem.getLabel());

        /* 지역*/
        select_bo_city = (RelativeLayout)findViewById(R.id.bo_city);
        select_bo_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSpinner.setDialog(BoardWrite.this, BoardWrite.this, v, "BO_CITY", null, new String[]{});
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

                    photo_layout.setVisibility(View.VISIBLE);
                    photo.setImageBitmap(bitmap);

                } catch (Exception e) {

                    imageFilePath = null;

                    e.printStackTrace();
                }
                if(imageFilePath != null) {

                    imageDel = 0;

                    scrollView.postDelayed(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    },200);
                }
            } else {
                Toast.makeText(getApplicationContext(), "파일 생성을 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            BasicInfo.ACTIVITY_ENABLE = BasicInfo.PACKAGE_BOARD_WRITE;//활성화된 액티비티 셋
            receiver.registBroad(new String[]{ConnectivityManager.CONNECTIVITY_ACTION, BasicInfo.ACTIVITY_ENABLE});//인터넷 상태 , 액티비티 별 소켓 (브로드캐스트 등록
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
