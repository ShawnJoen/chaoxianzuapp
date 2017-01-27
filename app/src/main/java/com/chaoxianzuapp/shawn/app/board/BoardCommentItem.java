package com.chaoxianzuapp.shawn.app.board;

/**
 * Created by shawn on 2015-08-18.
 */
public class BoardCommentItem implements Comparable<BoardCommentItem>{

    private int wr_id;
    private String mb_id;
    private String wr_name;
    private String wr_content;
    private int is_del;
    private int strstr_secret;
    private long wr_datetime;

    public BoardCommentItem(int is_del, String mb_id, int strstr_secret, String wr_content, long wr_datetime, int wr_id, String wr_name) {
        this.is_del = is_del;
        this.mb_id = mb_id;
        this.strstr_secret = strstr_secret;
        this.wr_content = wr_content;
        this.wr_datetime = wr_datetime;
        this.wr_id = wr_id;
        this.wr_name = wr_name;
    }
    public int getIs_del() {
        return is_del;
    }
    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }
    public String getMb_id() {
        return mb_id;
    }
    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }
    public int getStrstr_secret() {
        return strstr_secret;
    }
    public void setStrstr_secret(int strstr_secret) {
        this.strstr_secret = strstr_secret;
    }
    public String getWr_content() {
        return wr_content;
    }
    public void setWr_content(String wr_content) {
        this.wr_content = wr_content;
    }
    public long getWr_datetime() {
        return wr_datetime;
    }
    public void setWr_datetime(long wr_datetime) {
        this.wr_datetime = wr_datetime;
    }
    public int getWr_id() {
        return wr_id;
    }
    public void setWr_id(int wr_id) {
        this.wr_id = wr_id;
    }
    public String getWr_name() {
        return wr_name;
    }
    public void setWr_name(String wr_name) {
        this.wr_name = wr_name;
    }
    @Override
    public int compareTo(BoardCommentItem other) {

        long seq1 = this.getWr_datetime();
        long seq2 = other.getWr_datetime();

        if (seq1 > seq2) {

            return -1;
        } else if (seq1 < seq2) {

            return 1;
        }
        return 0;
    }
}
