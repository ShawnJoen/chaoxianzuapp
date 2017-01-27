package com.chaoxianzuapp.shawn.app.board;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;

import java.text.DecimalFormat;

/**
 * Created by shawn on 2015-08-16.
 */
public class BoardViewSet  {

    LinearLayout good_layout;

    public BoardViewSet(Context context ,Activity atv,AsyncImageLoader imageLoader ,BoardItem item){

        TextView sub_title = (TextView) atv.findViewById(R.id.sub_title);//게시판 카테 제목
        sub_title.setText(BasicInfo.activeBoardCateItem.getLabel() + " ("+ BasicInfo.BO_CITY.get(item.getCity()) +")");

        TextView wr_subject = (TextView) atv.findViewById(R.id.wr_subject);//게시판 제목
        wr_subject.setText(item.getWr_subject());

        TextView wr_content = (TextView) atv.findViewById(R.id.wr_content);//게시판 내용
        wr_content.setText(Html.fromHtml(item.getWr_content()));

        TextView mb_id = (TextView) atv.findViewById(R.id.mb_id);//사용자 아이디
        StringBuilder mb_id_ = new StringBuilder("(");
        mb_id_.append(item.getMb_id());
        mb_id_.append(")");
        mb_id.setText(mb_id_.toString());

        TextView mb_nick = (TextView) atv.findViewById(R.id.mb_nick);//사용자 닉네임
        mb_nick.setText(item.getMb_nick());

        TextView wr_hit = (TextView) atv.findViewById(R.id.wr_hit);//조회수
        wr_hit.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(item.getWr_hit()));

        TextView wr_datetime = (TextView) atv.findViewById(R.id.wr_datetime);//작성일
        wr_datetime.setText(BasicInfo.setDateText(1, item.getWr_datetime()));

        if(!"".equals(item.getMb_photo())){

            ImageView mb_photo = (ImageView) atv.findViewById(R.id.mb_photo);//사용자 사진
            BaseUtil.setImgUrl(1, imageLoader, mb_photo, null,item.getMb_photo());
        }
        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 1 ***************!!!!!!-----------------------------:");
        String[] imgUrls = item.getImgUrl();//게시 사진

        for(int i = 0 ; i < imgUrls.length; i++){

            ImageView imgUrl;
            switch(i){
                case 0:
                    imgUrl = (ImageView) atv.findViewById(R.id.imgUrl1);

                    BoardView.imgUrl1 = imgUrls[i];//첫 이미지만 처리

                    break;
                default:
                    imgUrl = (ImageView) atv.findViewById(R.id.imgUrl2);
            }
            BaseUtil.setImgUrl(1, imageLoader, imgUrl, null,imgUrls[i]);
        }

        boolean use_good = false;

        LinearLayout is_good = (LinearLayout) atv.findViewById(R.id.is_good);//좋아요 숨기기

        if(item.getWr_good() == -1) {

            setGone(is_good);//숨기기
        }else{

            use_good = true;

            if(BasicInfo.bg_flag == 1) {

                ImageView wr_good_ = (ImageView) atv.findViewById(R.id.wr_good_);
                wr_good_.setImageResource(R.mipmap.ic_good_selected);
            }
            TextView wr_good = (TextView) atv.findViewById(R.id.wr_good);//좋아요 사용
            wr_good.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(item.getWr_good()));
            setVisibility(is_good);//노출
        }

        LinearLayout is_nogood = (LinearLayout) atv.findViewById(R.id.is_nogood);//싫어요 숨기기

        if(item.getWr_nogood() == -1) {

            setGone(is_nogood);//숨기기
        }else{

            use_good = true;

            if(BasicInfo.bg_flag == 2) {

                ImageView wr_nogood_ = (ImageView) atv.findViewById(R.id.wr_nogood_);
                wr_nogood_.setImageResource(R.mipmap.ic_good_selected);
            }

            TextView wr_nogood = (TextView) atv.findViewById(R.id.wr_nogood);//싫어요 사용
            wr_nogood.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(item.getWr_nogood()));
            setVisibility(is_nogood);//노출
        }
        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 1 ***************!!!!!!2-----------------------------:");
        good_layout = (LinearLayout) atv.findViewById(R.id.good_layout);//좋아요,안좋아요 레이아웃

        int mr = BaseUtil.pixelToDp(16);
        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 1 ***************!!!!!!3-----------------------------:");
        if(use_good == true){
            setItemLayoutMargin(0,mr,0,mr);
        }else{
            setItemLayoutMargin(0,0,0,mr);
        }


        Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardDetail:: 데이터 불러오기 셋팅 1 ***************!!!!!!:" + mr + "::"+use_good);

    /*
    * 좋아요 싫어요 현 로그인 회원 이미 실행 여부 체크
    * 클릭시 처리
    * 아이콘 교체 처리
    *
    * 덧글 작성하기
    * 덧글 리스트 뷰 / 본인 덧글일시 삭제 기능
    * */
        //handler = new Handler();
    }

    //Handler handler;


    private void setGone(LinearLayout visib) {

        visib.setVisibility(View.GONE);
    }
    private void setVisibility(LinearLayout visib) {

        visib.setVisibility(View.VISIBLE);
    }
    private void setItemLayoutMargin(int left,int top,int right,int bottom) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  // , 1是可选写的
        lp.setMargins(left , top, right, bottom);
        good_layout.setLayoutParams(lp);
    }
    /*private void setImgUrl(AsyncImageLoader imageLoader,ImageView imageView, String imgUrl) {

        if (!TextUtils.isEmpty(imgUrl)) {

            imageView.setVisibility(View.VISIBLE);

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardViewSet:: setImgUrl !!!1!!!:"+ imgUrl);


            Bitmap bitmap = imageLoader.loadImage(imageView, imgUrl);
            if (bitmap != null) {


                Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardViewSet:: setImgUrl !!!2!!!:"+ imgUrl);

                imageView.setImageBitmap(bitmap);
            }
        }
    }*/
}
