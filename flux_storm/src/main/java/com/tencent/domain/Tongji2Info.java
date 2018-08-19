package com.tencent.domain;


import java.sql.Date;

public class Tongji2Info {
    private Date date;
    private int pv;
    private int uv;
    private int vv;
    private int newip;
    private int newcust;

    public Tongji2Info(Date date, int pv, int uv, int vv, int newip, int newcust) {
        this.date = date;
        this.pv = pv;
        this.uv = uv;
        this.vv = vv;
        this.newip = newip;
        this.newcust = newcust;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getVv() {
        return vv;
    }

    public void setVv(int vv) {
        this.vv = vv;
    }

    public int getNewip() {
        return newip;
    }

    public void setNewip(int newip) {
        this.newip = newip;
    }

    public int getNewcust() {
        return newcust;
    }

    public void setNewcust(int newcust) {
        this.newcust = newcust;
    }
}
