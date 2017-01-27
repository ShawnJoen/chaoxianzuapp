package com.chaoxianzuapp.shawn.app.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by shawn on 2015-08-19.
 */
public class EditChangedListener implements TextWatcher {
    private CharSequence temp;
    private int editStart;
    private int editEnd;
    private int charMaxNum = 20;
    private EditText search_view;
    private ImageView search_view_x;
    public EditChangedListener(EditText search_view ,int charMaxNum){
        this.search_view = search_view;
        this.charMaxNum = charMaxNum;
    }
    public EditChangedListener(EditText search_view,ImageView search_view_x){
        this.search_view = search_view;
        this.search_view_x = search_view_x;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(search_view_x != null) {

            if (charMaxNum == (charMaxNum - s.length())) {
                search_view_x.setVisibility(View.GONE);
            } else {
                search_view_x.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void afterTextChanged(Editable s) {

        editStart = search_view.getSelectionStart();
        editEnd = search_view.getSelectionEnd();
        if (temp.length() > charMaxNum) {

            s.delete(editStart - 1, editEnd);
            int tempSelection = editStart;
            search_view.setText(s);
            search_view.setSelection(tempSelection);
        }
    }
}