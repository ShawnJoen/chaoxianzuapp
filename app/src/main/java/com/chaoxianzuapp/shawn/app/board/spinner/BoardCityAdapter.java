package com.chaoxianzuapp.shawn.app.board.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shawn on 2015-11-03.
 */
public class BoardCityAdapter extends BaseAdapter{

    private Context context;

    private List<BoardCityItem> items = new ArrayList<BoardCityItem>();

    public BoardCityAdapter(Context context) {

        this.context = context;
    }
    public void clear() {
        this.items.clear();
        this.items = new ArrayList<BoardCityItem>();
    }
    public void addItem(BoardCityItem it) {
        this.items.add(it);
    }
    @Override
    public boolean isEmpty() {
        return this.getCount() == 0;
    }
    @Override
    public int getCount() {
        return this.items.size();
    }
    @Override
    public BoardCityItem getItem(int position) {
        return this.items.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BoardCityItemView itemView;

        if (convertView == null) {

            itemView = new BoardCityItemView(this.context);

            convertView = itemView.getConvertView();

            convertView.setTag(itemView);
        } else {
            itemView = (BoardCityItemView) convertView.getTag();
        }

        itemView.setItemName(this.items.get(position).getLabel());

        return convertView;
    }
}