package com.chaoxianzuapp.shawn.app.board;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogSpinner;

/**
 * Created by shawn on 2015-08-09.
 */
public class BoardFragment {

    private Context context;

    private Activity activity;

    private LayoutInflater inflater;

    private ViewGroup container;

    private BoardListAdapter boardListAdapter;

    private String empty;

    public BoardFragment(){}

    public BoardFragment(Context context ,Activity activity ,LayoutInflater inflater, ViewGroup container ,String empty) {

        this.context = context;

        this.activity = activity;

        this.inflater = inflater;

        this.container = container;

        this.empty = empty;
    }

    private RelativeLayout select_bo_city;

    private LinearLayout select_bo_table;

    private LinearLayout select_bo_sort;

    private View rootView;

    private ListView listView;

    public View Inflater(int position){

        switch(position){
            case 1:

                BasicInfo.activeBoardCateItem = BasicInfo.bo_tables.get(0);

                rootView =  inflater.inflate(R.layout.fragment_board, this.container, false);
                /*게시판 카테 텍스트 넣기*/
                TextView bo_city_ = (TextView)rootView.findViewById(R.id.bo_city_);

                bo_city_.setText(BasicInfo.activeBoardCityItem.getLabel());

                /*게시판 카테 텍스트 넣기*/
                TextView bo_table_ = (TextView)rootView.findViewById(R.id.bo_table_);

                bo_table_.setText(BasicInfo.activeBoardCateItem.getLabel());

                /*게시판 소팅 텍스트 넣기*/
                TextView bo_sort_ = (TextView)rootView.findViewById(R.id.bo_sort_);

                bo_sort_.setText(BasicInfo.activeBoardSortItem.getLabel());

                /* 지역*/
                select_bo_city = (RelativeLayout)rootView.findViewById(R.id.bo_city);
                select_bo_city.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogSpinner.setDialog(context, activity, rootView, "BO_CITY", boardListAdapter, new String[]{});
                    }
                });
                /* 카테고리*/
                select_bo_table = (LinearLayout)rootView.findViewById(R.id.bo_table);
                select_bo_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogSpinner.setDialog(context, activity,rootView, "BO_TABLE", boardListAdapter , new String[]{});
                    }
                });
                /* 소팅*/
                select_bo_sort = (LinearLayout)rootView.findViewById(R.id.bo_sort);
                select_bo_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogSpinner.setDialog(context, activity,rootView, "BO_SORT", boardListAdapter , new String[]{});
                    }
                });
                break;
            default:

                BasicInfo.activeBoardCateItem = BasicInfo.notice;

                rootView = inflater.inflate(R.layout.fragment_notice, this.container, false);
        }

        /* ListView 시작*/
        listView = (ListView)rootView.findViewById(R.id.mainListView);

        this.boardListAdapter = new BoardListAdapter(this.context);

        TextView textView = (TextView)rootView.findViewById(R.id.empty);

        textView.setText(this.empty);

        listView.setEmptyView(textView);

        listView.setAdapter(this.boardListAdapter);

        new BoardIListListener(boardListAdapter,listView , this.activity ,this.context);//ListView 이벤트 걸기

        return rootView;
    }

    public BoardListAdapter getBoardListAdapter() {
        return this.boardListAdapter;
    }
}
