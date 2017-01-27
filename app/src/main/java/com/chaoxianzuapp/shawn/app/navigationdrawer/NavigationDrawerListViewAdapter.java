package com.chaoxianzuapp.shawn.app.navigationdrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shawn on 2015-08-06.
 */
public class NavigationDrawerListViewAdapter extends BaseAdapter {

    private Context context;

    private List<NavigationItem> items = new ArrayList<NavigationItem>();

    public NavigationDrawerListViewAdapter(Context context) {
        this.context = context;
    }
    public void clear() {
        this.items.clear();
    }
    public void addItem(NavigationItem it) {
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
    public NavigationItem getItem(int position) {
        return this.items.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        NavigationItemSet itemView;

        if (convertView == null) {

            itemView = new NavigationItemSet(this.context);
        } else {

            itemView = (NavigationItemSet) convertView;
        }

        itemView.setTextView(this.items.get(position).getmText());
        itemView.setMenuIcon(this.items.get(position).getIcon());
        return itemView;
    }
    @Override
    public void notifyDataSetChanged() {

        Collections.sort(this.items);

        super.notifyDataSetChanged();
    }
}
