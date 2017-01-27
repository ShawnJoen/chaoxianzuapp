package com.chaoxianzuapp.shawn.app.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chaoxianzuapp.shawn.app.BasicInfo;

/**
 * Created by shawn on 2015-08-18.
 */
public class UIHelper {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        //Log.d(BasicInfo.NETWORK_LOG_TAG, "UIHelper:totalHeight :" + totalHeight);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        //Log.d(BasicInfo.NETWORK_LOG_TAG, "UIHelper:params.height :" + params.height);

        listView.setLayoutParams(params);
    }
}
