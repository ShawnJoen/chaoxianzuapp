package com.chaoxianzuapp.shawn.app.navigationdrawer;
/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationItem implements Comparable<NavigationItem>{
    private int mSeq;
    private String mText;
    private int icon;
    public NavigationItem(String text) {
        this.mText = text;
    }
    public NavigationItem(int seq, String text, int icon) {
        this.mSeq = seq;
        this.mText = text;
        this.icon = icon;
    }
    public int getmSeq() {
        return this.mSeq;
    }
    public void setmSeq(int mSeq) {
        this.mSeq = mSeq;
    }
    public int getIcon() {
        return this.icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getmText() {
        return mText;
    }
    public void setmText(String text) {
        this.mText = text;
    }
    @Override
    public int compareTo(NavigationItem other) {

        int seq1 = this.getmSeq();
        int seq2 = other.getmSeq();

        if (seq1 < seq2) {

            return -1;
        } else if (seq1 > seq2) {

            return 1;
        }
        return 0;
    }
}
