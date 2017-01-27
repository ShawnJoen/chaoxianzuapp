package com.chaoxianzuapp.shawn.app.board;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.requestdata.AsyncImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shawn on 2015-08-09.
 */
public class BoardListAdapter extends BaseAdapter {

    private Context context;

    private AsyncImageLoader imageLoader;

    private List<BoardItem> items = new ArrayList<BoardItem>();

    public BoardListAdapter(Context context) {

        this.context = context;

        imageLoader = new AsyncImageLoader(context);
    }
    public void clear() {

        this.items.clear();

        items = new ArrayList<BoardItem>();

        BasicInfo.setInitPaging();/*기본 페이징 초기화*/
    }
    public void addItem(BoardItem it) {
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
    public BoardItem getItem(int position) {
        return this.items.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void notifyDataSetChanged() {

        //Collections.sort(this.items);

        super.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BoardItemSet itemView;

        if (convertView == null) {

            itemView = new BoardItemSet(this.context);

            convertView = itemView.getConvertView();

            convertView.setTag(itemView);
        } else {
            itemView = (BoardItemSet) convertView.getTag();
        }

        //int size = getCount() - 1;if(size < position) position = size;//防止 数组越界
        if(position > getCount()){

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardListAdapter:: position > getCount() position::" + position + ",getCount::" +getCount());
            return convertView;
        }

        itemView.setImgUrlTag(this.items.get(position).getWr_id());

        String[] imgUrl = this.items.get(position).getImgUrl();

        if(!"null".equals(imgUrl[0])) {

            itemView.setImgUrl( imageLoader, imgUrl[0] , this.items.get(position).getWr_id());
        }else{//이미지가 존재안하면 기본설정 및 이미지 감추기

            itemView.setDefaultImgUrl();
        }

        itemView.setMb_id(this.items.get(position).getMb_id());
        itemView.setWr_subject(this.items.get(position).getWr_subject());
        itemView.setWr_hit(this.items.get(position).getWr_hit());
        itemView.setIcon_new(this.items.get(position).getIcon_new());
        itemView.setIcon_hot(this.items.get(position).getIcon_hot());

        int wr_comment = this.items.get(position).getWr_comment();
        if(wr_comment == -1){
            itemView.setHideBo_use_comment();
        }else {
            itemView.setVisibleBo_use_comment();
            itemView.setWr_comment(wr_comment);
        }
        /*int wr_good = this.items.get(position).getWr_good();//추천 사용시만 UI노출
        if(wr_good == -1){
            itemView.setHideIs_good();
        }else {
            itemView.setVisibleIs_good();
            itemView.setWr_good(wr_good);
        }*/
        itemView.setWr_datetime(this.items.get(position).getWr_datetime());

        return convertView;
    }
}
