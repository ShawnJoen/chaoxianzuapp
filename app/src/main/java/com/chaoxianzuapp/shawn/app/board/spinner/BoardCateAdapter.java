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
public class BoardCateAdapter extends BaseAdapter {

    private Context context;

    private AsyncImageLoader imageLoader;

    private List<BoardCateItem> items = new ArrayList<BoardCateItem>();

    public BoardCateAdapter(Context context) {

        this.context = context;

        imageLoader = new AsyncImageLoader(context);
    }
    public void clear() {
        this.items.clear();
        this.items = new ArrayList<BoardCateItem>();
    }
    public void addItem(BoardCateItem it) {
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
    public BoardCateItem getItem(int position) {
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

        BoardCateItemView itemView;

        if (convertView == null) {

            itemView = new BoardCateItemView(this.context);

            convertView = itemView.getConvertView();

            convertView.setTag(itemView);
        } else {
            itemView = (BoardCateItemView) convertView.getTag();
        }

        itemView.setItemName(this.items.get(position).getLabel());

        itemView.setImgUrl(this.items.get(position).getCode());
        /*itemView.setImgUrlTag(this.items.get(position).getCode());

        String imgUrl = this.items.get(position).getImgUrl();

        if(!"null".equals(imgUrl)) {

            itemView.setImgUrl( imageLoader, imgUrl);
        }else{//이미지가 존재안하면 기본설정 및 이미지 감추기

            itemView.setDefaultImgUrl();
        }*/
        return convertView;
    }
}
