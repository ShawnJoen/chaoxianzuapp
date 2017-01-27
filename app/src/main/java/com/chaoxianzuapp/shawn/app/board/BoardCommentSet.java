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

import java.text.DecimalFormat;

/**
 * Created by shawn on 2015-08-18.
 */
public class BoardCommentSet extends LinearLayout {

    //private int wr_id;
    private TextView mb_id;
    private TextView wr_name;
    private TextView wr_content;
    //private int is_del;
    //private int strstr_secret;
    private TextView wr_datetime;


    private View convertView;

    public View getConvertView() {

        return convertView;
    }
    public BoardCommentSet(Context context) {
        super(context);

        convertView = LayoutInflater.from(context).inflate(R.layout.board_comment_listitem, null);

        mb_id = (TextView) convertView.findViewById(R.id.mb_id);
        wr_name = (TextView) convertView.findViewById(R.id.wr_name);
        wr_content = (TextView) convertView.findViewById(R.id.wr_content);
        wr_datetime = (TextView) convertView.findViewById(R.id.wr_datetime);
    }
    public void setMb_id(String mb_id) {
        this.mb_id.setText(mb_id);
    }
    public void setWr_content(String wr_content) {
        this.wr_content.setText(wr_content);
    }
    public void setWr_datetime(long wr_datetime) {
        this.wr_datetime.setText(BasicInfo.setDateText(1, wr_datetime));
    }
    public void setWr_name(String wr_name) {
        this.wr_name.setText(wr_name);
    }
}