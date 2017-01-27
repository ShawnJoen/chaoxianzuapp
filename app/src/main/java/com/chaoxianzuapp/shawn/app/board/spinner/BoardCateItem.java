package com.chaoxianzuapp.shawn.app.board.spinner;

/**
 * Created by shawn on 2015-08-12.
 */
public class BoardCateItem implements Comparable<BoardCateItem>{

    private int code;
    private String label;
    private String imgUrl;
    private String bo_table;
    public BoardCateItem(int code, String label, String imgUrl ,String bo_table) {
        this.code = code;
        this.label = label;
        this.imgUrl = imgUrl;
        this.bo_table = bo_table;
    }
    public String getBo_table() {
        return bo_table;
    }
    public void setBo_table(String bo_table) {
        this.bo_table = bo_table;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    @Override
    public int compareTo(BoardCateItem other) {

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
