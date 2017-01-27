package com.chaoxianzuapp.shawn.app.board;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.requestdata.BoardList;

/**
 * Created by shawn on 2015-08-19.
 */
public class BoardIListListener {

    public BoardIListListener(final BoardListAdapter boardListAdapter, ListView listView, final Activity activity,final Context context){

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (BasicInfo.is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && BasicInfo.pageNo <= BasicInfo.totalPage) {

                    new BoardList(activity, boardListAdapter).start();
                } else if (BasicInfo.pageNo > BasicInfo.totalPage) {
                    /**
                     * 如果pageNo>4则表示，服务端没有更多的数据可供加载了。
                     */
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                BasicInfo.is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            /*
                선택된 아이템 wr_id로 Intent으로 넘기기
                물론 로그인 이 필요한 activity이면 mb_id 변수 값 존재유무 체크.
                빈값이면 로그인 창 뛰우기
             */

                BoardItem item = (BoardItem) boardListAdapter.getItem(position);

                Intent intent = new Intent(context, BoardView.class);

                intent.putExtra("wr_id", item.getWr_id());

                intent.putExtra("mb_id", item.getMb_id());

                intent.putExtra("city", item.getCity());

                intent.putExtra("wr_comment", item.getWr_comment());

                RelativeLayout bo_use_comment = (RelativeLayout)view.findViewById(R.id.bo_use_comment);
                intent.putExtra("bo_use_comment", (bo_use_comment.getVisibility() == View.VISIBLE ? 1 : 0));

                BoardSearch.keyword = "";//서치 키워드 비우기

                activity.startActivityForResult(intent, BasicInfo.BOARD_VIEW_ACTIVITY);

            }
        });
    }
}
