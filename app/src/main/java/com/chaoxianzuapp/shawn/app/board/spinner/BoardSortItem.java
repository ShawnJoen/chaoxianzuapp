package com.chaoxianzuapp.shawn.app.board.spinner;

/**
 * Created by shawn on 2015-08-12.
 */
public class BoardSortItem implements Comparable<BoardSortItem>{

    private int code;
    private String label;
    private String sort;
    public BoardSortItem(int code, String label ,String sort) {
        this.code = code;
        this.label = label;
        this.sort = sort;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
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
    @Override
    public int compareTo(BoardSortItem other) {

        long seq1 = this.getCode();
        long seq2 = other.getCode();

        if (seq1 < seq2) {

            return -1;
        } else if (seq1 > seq2) {

            return 1;
        }
        return 0;
    }
}

