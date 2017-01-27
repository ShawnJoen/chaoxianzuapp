package com.chaoxianzuapp.shawn.app.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.MainActivity;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.board.BoardListAdapter;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCateAdapter;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCateItem;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCityAdapter;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCityItem;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardSortAdapter;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardSortItem;
import com.chaoxianzuapp.shawn.app.requestdata.BoardList;

import java.util.Iterator;

/**
 * Created by shawn on 2015-08-12.
 */
public class DialogSpinner {

    public static void setDialog(final Context context ,final Activity activity ,final View v ,String mode , final BoardListAdapter boardListAdapter , final String[] msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = null;
        final AlertDialog dialog;
        switch (mode) {
            case "BO_SORT":

                layout = inflater.inflate(R.layout.board_bo_sort_pop, null);

                builder.setView(layout);

                dialog = builder.create();

                ListView listView = (ListView) layout.findViewById(R.id.listView);

                final BoardSortAdapter boardSortAdapter = new BoardSortAdapter(context);

                Iterator<BoardSortItem> iter = BasicInfo.bo_sorts.iterator();
                while(iter.hasNext()){

                    BoardSortItem boardSortItem = iter.next();

                    boardSortAdapter.addItem(boardSortItem);
                }

                listView.setAdapter(boardSortAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        /*게시판 소팅 텍스트 넣기*/
                        TextView bo_sort_ = (TextView)v.findViewById(R.id.bo_sort_);

                        bo_sort_.setText(boardSortAdapter.getItem(position).getLabel());

                        boardListAdapter.clear();

                        BasicInfo.activeBoardSortItem = boardSortAdapter.getItem(position);//새로 선택한 소팅기준 active;

                        new BoardList(activity , boardListAdapter).start();

                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            default:

                GridView gridview = null;
                switch(mode) {
                    case "BO_CITY":

                        layout = inflater.inflate(R.layout.board_city_pop, null);

                        builder.setView(layout);

                        dialog = builder.create();

                        gridview = (GridView) layout.findViewById(R.id.gridView);

                        final BoardCityAdapter boardCityAdapter = new BoardCityAdapter(context);

                        Iterator<BoardCityItem> iter2 = BasicInfo.bo_city.iterator();
                        while (iter2.hasNext()) {

                            BoardCityItem boardCityItem = iter2.next();

                            boardCityAdapter.addItem(boardCityItem);
                        }

                        gridview.setAdapter(boardCityAdapter);

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                /*게시판 카테 텍스트 넣기*/
                                TextView bo_city_ = (TextView) v.findViewById(R.id.bo_city_);

                                bo_city_.setText(boardCityAdapter.getItem(position).getLabel());

                                if(boardListAdapter != null) {

                                    if (!BasicInfo.ACTIVITY_ENABLE.equalsIgnoreCase(BasicInfo.PACKAGE_BOARD_SEARCH) || !"".equalsIgnoreCase(msg[0])) {

                                        boardListAdapter.clear();

                                        new BoardList(activity, boardListAdapter).start();
                                    }
                                }

                                BasicInfo.activeBoardCityItem = boardCityAdapter.getItem(position);//새로 선택한 카테고리 active;

                                dialog.dismiss();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        break;
                    case "BO_TABLE":

                        layout = inflater.inflate(R.layout.board_bo_table_pop, null);

                        builder.setView(layout);

                        dialog = builder.create();

                        gridview = (GridView) layout.findViewById(R.id.gridView);

                        final BoardCateAdapter boardCateAdapter = new BoardCateAdapter(context);

                        Iterator<BoardCateItem> iter3 = BasicInfo.bo_tables.iterator();
                        while (iter3.hasNext()) {

                            BoardCateItem boardCateItem = iter3.next();

                            boardCateAdapter.addItem(boardCateItem);
                        }

                        gridview.setAdapter(boardCateAdapter);

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                /*게시판 카테 텍스트 넣기*/
                                TextView bo_table_ = (TextView) v.findViewById(R.id.bo_table_);

                                bo_table_.setText(boardCateAdapter.getItem(position).getLabel());

                                boardListAdapter.clear();

                                BasicInfo.activeBoardCateItem = boardCateAdapter.getItem(position);//새로 선택한 카테고리 active;

                                new BoardList(activity, boardListAdapter).start();

                                MainActivity.mToolbar.setTitle(BasicInfo.activeBoardCateItem.getLabel());

                                dialog.dismiss();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        break;
                }
        }
    }
}
