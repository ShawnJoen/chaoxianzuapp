package com.chaoxianzuapp.shawn.app.board.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shawn on 2015-08-12.
 */
public class BoardSortAdapter extends BaseAdapter {

    private Context context;

    private List<BoardSortItem> items = new ArrayList<BoardSortItem>();

    public BoardSortAdapter(Context context) {

        this.context = context;
    }
    public void clear() {
        this.items.clear();
        this.items = new ArrayList<BoardSortItem>();
    }
    public void addItem(BoardSortItem it) {
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
    public BoardSortItem getItem(int position) {
        return this.items.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void notifyDataSetChanged() {

        Collections.sort(this.items);

        super.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BoardSortItemView itemView;

        if (convertView == null) {

            itemView = new BoardSortItemView(this.context);

            convertView = itemView.getConvertView();

            convertView.setTag(itemView);
        } else {
            itemView = (BoardSortItemView) convertView.getTag();
        }

        itemView.setItemName(this.items.get(position).getLabel());

        return convertView;
    }
}
