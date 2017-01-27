package com.chaoxianzuapp.shawn.app.board.spinner;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;

/**
 * Created by shawn on 2015-08-12.
 */
public class BoardCateItemView extends LinearLayout {

    private ImageView imgUrl;

    private TextView itemName;

    private View convertView;

    public View getConvertView() {

        return convertView;
    }
    public BoardCateItemView(Context context) {
        super(context);

        convertView = LayoutInflater.from(context).inflate(R.layout.board_bo_table_select, null);

        imgUrl = (ImageView) convertView.findViewById(R.id.itemImage);

        itemName = (TextView) convertView.findViewById(R.id.itemName);
    }
    public void setImgUrlTag(int seq) {

        imgUrl.setTag(seq);
    }
    public void setDefaultImgUrl() {

        this.imgUrl.setImageResource(R.mipmap.thumb_sample);
    }
    public void setImgUrl(int code) {

        this.imgUrl.setImageResource(getImageResource(code));
    }
    public int getImageResource(int code) {

        int res;
        switch(code){
            case 13:res = R.mipmap.ic_filter_9_black_18dp;
                break;
            case 12:res = R.mipmap.ic_filter_8_black_18dp;
                break;
            case 11:res = R.mipmap.ic_filter_7_black_18dp;
                break;
            case 10:res = R.mipmap.ic_filter_6_black_18dp;
                break;
            case 9:res = R.mipmap.ic_filter_5_black_18dp;
                break;
            case 8:res = R.mipmap.ic_filter_4_black_18dp;
                break;
            case 7:res = R.mipmap.ic_filter_3_black_18dp;
                break;
            case 6:res = R.mipmap.ic_store_mall_directory_black_18dp;
                break;
            case 5:res = R.mipmap.ic_face_black_18dp;
                break;
            case 4:res = R.mipmap.ic_group_add_black_18dp;
                break;
            case 3:res = R.mipmap.ic_business_black_18dp;
                break;
            case 2:res = R.mipmap.ic_filter_2_black_18dp;
                break;
            default:
                res = R.mipmap.ic_filter_1_black_18dp;
        }
        return res;
    }
    public void setImgUrl(AsyncImageLoader imageLoader, String imgUrl) {

        if (!TextUtils.isEmpty(imgUrl)) {

            Bitmap bitmap = imageLoader.loadImage(this.imgUrl, imgUrl);
            if (bitmap != null) {
                this.imgUrl.setImageBitmap(bitmap);
            }
        }
    }
    public void setItemName(String itemName) {
        this.itemName.setText(itemName);
    }
}
