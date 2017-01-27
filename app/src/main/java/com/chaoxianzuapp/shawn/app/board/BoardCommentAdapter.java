package com.chaoxianzuapp.shawn.app.board;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.dialog.DialogBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shawn on 2015-08-18.
 */
public class BoardCommentAdapter extends BaseAdapter {

    private Context context;
    private Activity activity;

    private List<BoardCommentItem> items = new ArrayList<BoardCommentItem>();

    public BoardCommentAdapter(Context context ,Activity activity) {

        this.context = context;
        this.activity = activity;
    }
    public void clear() {

        this.items.clear();

        items = new ArrayList<BoardCommentItem>();

        BasicInfo.setInitPaging();/*기본 페이징 초기화*/
    }
    public void addItem(BoardCommentItem it) {
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
    public BoardCommentItem getItem(int position) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        BoardCommentSet itemView;

        if (convertView == null) {

            itemView = new BoardCommentSet(this.context);

            convertView = itemView.getConvertView();

            convertView.setTag(itemView);
        } else {
            itemView = (BoardCommentSet) convertView.getTag();
        }

        if (position > getCount()) {

            Log.d(BasicInfo.NETWORK_LOG_TAG, "BoardCommentAdapter:: position > getCount() ");
            return convertView;
        }

        final int wr_id = this.items.get(position).getWr_id();

        final RelativeLayout self_comment = (RelativeLayout) convertView.findViewById(R.id.self_comment);

        if(this.items.get(position).getIs_del() == 1) {//삭제 권한 있으면 버튼 노출 및 이벤트 걸기

            self_comment.setVisibility(View.VISIBLE);

            self_comment.setOnClickListener(new RelativeLayout.OnClickListener() {
                public void onClick(View v) {

                    PopupMenu p = new PopupMenu(context, v);//view는 오래 눌러진 뷰를 의미
                    Menu menu = p.getMenu();
                    activity.getMenuInflater().inflate(R.menu.board_comment_ctrl_item, menu);

                    MenuItem delete = menu.findItem(R.id.delete);
                    delete.setTitle("삭제하기");

                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // TODO Auto-generated method stub
                            switch( item.getItemId() ){
                                case R.id.delete:

                                    DialogBoard.setDialog(context, activity, wr_id, "COMMENT_DEL", new String[]{"알람", "삭제하겠습니까?", "확인", "취소"});
                                    break;
                            }
                            return false;
                        }
                    });
                    p.show();//Popup Menu 보이기

                    //items.remove(items.get(position));

                    //notifyDataSetChanged();
                }
            });
        }else{
            self_comment.setVisibility(View.GONE);
        }

        //itemView.setMb_id(this.items.get(position).getMb_id());
        itemView.setWr_name(this.items.get(position).getWr_name());
        itemView.setWr_content(this.items.get(position).getWr_content());
        itemView.setWr_datetime(this.items.get(position).getWr_datetime());

        return convertView;
    }
}
