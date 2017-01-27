package com.chaoxianzuapp.shawn.app.board.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.R;

/**
 * Created by shawn on 2015-11-03.
 */
public class BoardCityItemView extends LinearLayout {

    private TextView itemName;

    private View convertView;

    public View getConvertView() {

        return convertView;
    }
    public BoardCityItemView(Context context) {
        super(context);

        convertView = LayoutInflater.from(context).inflate(R.layout.board_city_select, null);

        itemName = (TextView) convertView.findViewById(R.id.itemName);
    }
    public void setItemName(String itemName) {
        this.itemName.setText(itemName);
    }
}