package com.example.androidjj.giftapp.JavaBean;

/**
 * Created by Jpacino on 2016/8/15.
 */
public class GiftListBean {
    private String giftname;
    private String gname;
    private String iconurl;
    private String number;
    private String addtime;

    public GiftListBean(String giftname, String gname, String iconurl, String number, String addtime) {
        this.giftname = giftname;
        this.gname = gname;
        this.iconurl = iconurl;
        this.number = number;
        this.addtime = addtime;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
