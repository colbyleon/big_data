package com.tencent.domain;

import java.sql.Timestamp;

public class Tongji3Info {
    private Timestamp time;
    private Double br;
    private Double avgtime;
    private Double avgdeep;

    public Tongji3Info() {
    }

    public Tongji3Info(Timestamp time, Double br, Double avgtime, Double avgdeep) {
        this.time = time;
        this.br = br;
        this.avgtime = avgtime;
        this.avgdeep = avgdeep;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getBr() {
        return br;
    }

    public void setBr(Double br) {
        this.br = br;
    }

    public Double getAvgtime() {
        return avgtime;
    }

    public void setAvgtime(Double avgtime) {
        this.avgtime = avgtime;
    }

    public Double getAvgdeep() {
        return avgdeep;
    }

    public void setAvgdeep(Double avgdeep) {
        this.avgdeep = avgdeep;
    }
}
