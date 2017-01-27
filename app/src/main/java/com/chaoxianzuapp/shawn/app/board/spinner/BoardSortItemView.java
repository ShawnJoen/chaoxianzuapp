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
public class BoardSortItemView extends LinearLayout {

    private TextView itemName;

    private View convertView;

    public View getConvertView() {

        return convertView;
    }
    public BoardSortItemView(Context context) {
        super(context);

        convertView = LayoutInflater.from(context).inflate(R.layout.board_bo_sort_select, null);

        itemName = (TextView) convertView.findViewById(R.id.itemName);
    }
    public void setItemName(String itemName) {
        this.itemName.setText(itemName);
    }
}

