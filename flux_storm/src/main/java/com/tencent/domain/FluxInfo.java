package com.tencent.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FluxInfo {
    private String url;
    private String urlname;
    private String ref;
    private String uvid;
    private String ssid;
    private String scount;
    private String stime;
    private String cip;
    private String rk;

    public FluxInfo() {
    }

    public FluxInfo(String url, String urlname,String ref, String uvid, String ssid, String scount, String stime, String cip) {
        this.url = url;
        this.urlname = urlname;
        this.ref = ref;
        this.uvid = uvid;
        this.ssid = ssid;
        this.scount = scount;
        this.stime = stime;
        this.cip = cip;
    }

    public String getRK() {
        if (rk == null) {
            rk = stime + "_" + uvid + "_" + ssid + "_" + cip + String.format("_%3d", (int) (Math.random() * 1000));
        }
        return rk;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public String getUvid() {
        return uvid;
    }

    public void setUvid(String uvid) {
        this.uvid = uvid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getScount() {
        return scount;
    }

    public void setScount(String scount) {
        this.scount = scount;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("url", url)
                .append("urlname", urlname)
                .append("ref", ref)
                .append("uvid", uvid)
                .append("ssid", ssid)
                .append("scount", scount)
                .append("stime", stime)
                .append("cip", cip)
                .append("rk", rk)
                .toString();
    }
}
