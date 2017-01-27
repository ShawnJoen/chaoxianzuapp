package com.chaoxianzuapp.shawn.app.board.spinner;

/**
 * Created by shawn on 2015-11-03.
 */
public class BoardCityItem{

    private int code;
    private String label;
    public BoardCityItem(int code, String label) {
        this.code = code;
        this.label = label;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}