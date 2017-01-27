package com.chaoxianzuapp.shawn.app.board;

/**
 * Created by shawn on 2015-08-09.
 */
public class BoardItem implements Comparable<BoardItem>{

    private int wr_id;//1,2,3,4

    private String mb_id;
    private String mb_nick;
    private String mb_photo;
    private int city;
    private String wr_subject;

    private String wr_content;

    private int wr_comment;//댓글 수

    private int wr_hit;

    private int icon_new;//Request R.mipmap.xx
    private int icon_hot;

    private int wr_good;
    private int wr_nogood;

    private long wr_datetime;//1439200000000L

    private String[] imgUrl;

    public BoardItem(int wr_id, String mb_id, int wr_comment, int city ,String wr_subject,int wr_hit,int icon_new,int icon_hot,long wr_datetime,String[] imgUrl) {
        this.wr_id = wr_id;
        this.mb_id = mb_id;
        this.wr_comment = wr_comment;
        this.city = city;
        this.wr_subject = wr_subject;
        this.wr_hit = wr_hit;
        this.icon_new = icon_new;
        this.icon_hot = icon_hot;
        //this.wr_good = wr_good;int wr_good,
        this.wr_datetime = wr_datetime;
        this.imgUrl = imgUrl;
    }
    public BoardItem(String mb_id,String mb_nick,String mb_photo,int city ,String wr_subject,String wr_content,int wr_hit,int wr_good,int wr_nogood,long wr_datetime,String[] imgUrl) {
        //this.wr_id = wr_id;int wr_id,
        this.mb_id = mb_id;
        this.mb_nick = mb_nick;
        this.mb_photo = mb_photo;
        this.city = city;
        this.wr_subject = wr_subject;
        this.wr_content = wr_content;
        this.wr_hit = wr_hit;
        //this.icon_new = icon_new;int icon_new,
        this.wr_good = wr_good;
        this.wr_nogood = wr_nogood;
        this.wr_datetime = wr_datetime;
        this.imgUrl = imgUrl;
    }
    public int getCity() {
        return city;
    }
    public void setCity(int city) {
        this.city = city;
    }
    public int getWr_comment() {
        return wr_comment;
    }
    public void setWr_comment(int wr_comment) {
        this.wr_comment = wr_comment;
    }
    public String getMb_nick() {
        return mb_nick;
    }
    public void setMb_nick(String mb_nick) {
        this.mb_nick = mb_nick;
    }
    public String getMb_photo() {
        return mb_photo;
    }
    public void setMb_photo(String mb_photo) {
        this.mb_photo = mb_photo;
    }
    public int getWr_nogood() {
        return wr_nogood;
    }
    public void setWr_nogood(int wr_nogood) {
        this.wr_nogood = wr_nogood;
    }
    public String getWr_content() {
        return wr_content;
    }
    public void setWr_content(String wr_content) {
        this.wr_content = wr_content;
    }
    public int getIcon_new() {
        return icon_new;
    }
    public void setIcon_new(int icon_new) {
        this.icon_new = icon_new;
    }
    public int getIcon_hot() {
        return icon_hot;
    }
    public void setIcon_hot(int icon_hot) {
        this.icon_hot = icon_hot;
    }
    public String getMb_id() {
        return mb_id;
    }
    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }
    public long getWr_datetime() {
        return wr_datetime;
    }
    public void setWr_datetime(long wr_datetime) {
        this.wr_datetime = wr_datetime;
    }
    public int getWr_good() {
        return wr_good;
    }
    public void setWr_good(int wr_good) {
        this.wr_good = wr_good;
    }
    public int getWr_hit() {
        return wr_hit;
    }
    public void setWr_hit(int wr_hit) {
        this.wr_hit = wr_hit;
    }
    public int getWr_id() {
        return wr_id;
    }
    public void setWr_id(int wr_id) {
        this.wr_id = wr_id;
    }
    public String getWr_subject() {
        return wr_subject;
    }
    public void setWr_subject(String wr_subject) {
        this.wr_subject = wr_subject;
    }
    public String[] getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String[] imgUrl) {
        this.imgUrl = imgUrl;
    }
    @Override
    public int compareTo(BoardItem other) {

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
