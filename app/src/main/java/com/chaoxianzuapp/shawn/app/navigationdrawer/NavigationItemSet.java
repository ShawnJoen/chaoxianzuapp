package com.chaoxianzuapp.shawn.app.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxianzuapp.shawn.app.R;

/**
 * Created by shawn on 2015-08-06.
 */
public class NavigationItemSet extends LinearLayout {

    private TextView textView;
    private ImageView menuIcon;

    public NavigationItemSet(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_navigation_listitem, this, true);

        textView = (TextView) findViewById(R.id.textView);
        menuIcon = (ImageView) findViewById(R.id.left_menu_icon);
    }
    public void setTextView(String text) {
        this.textView.setText(text);
    }

    public void setMenuIcon(int resource) {
        this.menuIcon.setImageResource(resource);
    }
}
