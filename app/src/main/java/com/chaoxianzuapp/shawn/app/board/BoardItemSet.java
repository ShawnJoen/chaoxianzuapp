package com.chaoxianzuapp.shawn.app.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;
import com.chaoxianzuapp.shawn.app.util.BaseUtil;

import java.text.DecimalFormat;

/**
 * Created by shawn on 2015-08-09.
 */
public class BoardItemSet extends LinearLayout {

    private Context context;
    private int wr_id;//1,2,3,4

    private TextView mb_id;

    private TextView wr_subject;

    private TextView wr_hit;

    private ImageView icon_new;//1,0
    private ImageView icon_hot;

    //private TextView wr_good;
    private TextView wr_comment;

    private TextView wr_datetime;//123456789123000

    private ImageView imgUrl;

    private LinearLayout item_layout;

    private RelativeLayout img_url_layout;

    //private RelativeLayout is_good;
    private RelativeLayout bo_use_comment;

    private View convertView;

    public View getConvertView() {

        return convertView;
    }
    public BoardItemSet(Context context) {
        super(context);

        this.context = context;

        convertView = LayoutInflater.from(context).inflate(R.layout.board_listitem, null);

        mb_id = (TextView) convertView.findViewById(R.id.mb_id);
        wr_subject = (TextView) convertView.findViewById(R.id.wr_subject);
        wr_hit = (TextView) convertView.findViewById(R.id.wr_hit);
        //wr_good = (TextView) convertView.findViewById(R.id.wr_good);
        wr_comment = (TextView) convertView.findViewById(R.id.wr_comment);
        wr_datetime = (TextView) convertView.findViewById(R.id.wr_datetime);

        imgUrl = (ImageView) convertView.findViewById(R.id.imgUrl);
        icon_new = (ImageView) convertView.findViewById(R.id.icon_new);
        icon_hot = (ImageView) convertView.findViewById(R.id.icon_hot);

        item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
        img_url_layout = (RelativeLayout) convertView.findViewById(R.id.img_url_layout);

        //is_good = (RelativeLayout) convertView.findViewById(R.id.is_good);//추천 사용여부
        bo_use_comment = (RelativeLayout) convertView.findViewById(R.id.bo_use_comment);
    }
    public ImageView getImgUrl() {
        return imgUrl;
    }
    public void setWr_datetime(long wr_datetime) {//숫자 날짜 편집

        this.wr_datetime.setText(BasicInfo.setDateText(1, wr_datetime));
    }
    public void setMb_id(String mb_id) {
        this.mb_id.setText(mb_id);
    }
    /*public void setWr_good(int wr_good) {

        //this.wr_good.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(wr_good));
    }*/
    public void setWr_comment(int wr_comment){
        this.wr_comment.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(wr_comment));
    }
    public void setWr_hit(int wr_hit) {//숫자 날짜 편집 콤마 넣기

        this.wr_hit.setText(new DecimalFormat(BasicInfo.FORMAT_NUM).format(wr_hit));
    }
    public void setWr_subject(String wr_subject) {
        this.wr_subject.setText(wr_subject);
    }
    public void setWr_id(int wr_id) {

        //this.wr_id = wr_id;
    }
    public void setIcon_new(int icon_new) {//숫자 1,0 새글 및 아닌글 아이콘 show,hide

        if(icon_new == 1){
            this.icon_new.setVisibility(View.VISIBLE);
        }else{
            this.icon_new.setVisibility(View.GONE);
        }
    }
    public void setIcon_hot(int icon_hot) {

        if(icon_hot == 1){
            this.icon_hot.setVisibility(View.VISIBLE);
        }else{
            this.icon_hot.setVisibility(View.GONE);
        }
    }
    public void setImgUrlTag(int seq) {

        imgUrl.setTag(seq);
    }
    public void setHideBo_use_comment() {

        bo_use_comment.setVisibility(View.GONE);
    }
    public void setVisibleBo_use_comment() {

        bo_use_comment.setVisibility(View.VISIBLE);
    }
    /*public void setHideIs_good() {

        is_good.setVisibility(View.GONE);
     }
    public void setVisibleIs_good() {

        is_good.setVisibility(View.VISIBLE);
    }*/
    public void setDefaultImgUrl() {

        img_url_layout.setVisibility(View.GONE);

        Log.d(BasicInfo.BOARD_LOG_TAG, "BoardItemView :: setDefaultImgUrl");

        setItemLayoutMargin(0);

        this.imgUrl.setImageResource(R.mipmap.thumb_sample);
    }
    public void setItemLayoutMargin(int marginLeft) {

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);  // , 1是可选写的

        lp.setMargins(BaseUtil.pixelToDp(marginLeft) , 0, 0, 0);

        item_layout.setLayoutParams(lp);
    }
    public void setImgUrl(AsyncImageLoader imageLoader, String imgUrl, int seq) {

        if (!TextUtils.isEmpty(imgUrl) && this.imgUrl.getTag() != null && this.imgUrl.getTag().equals(seq)) {

            img_url_layout.setVisibility(View.VISIBLE);

            Log.d(BasicInfo.BOARD_LOG_TAG, "BoardItemView :: setImgUrl");

            setItemLayoutMargin(66);

            Bitmap bitmap = imageLoader.loadImage(this.imgUrl, imgUrl);
            if (bitmap != null) {
                this.imgUrl.setImageBitmap(bitmap);
            }
        }
    }
}