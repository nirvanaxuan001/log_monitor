package com.netease.ps.demo.domain;

import java.util.Date;

/**
 * Created by hzzhangxuan on 2017/9/29.
 */
public class Point {
    public Point() {
    }

    public Point(Date x, double y) {
        this.x = x;
        this.y = y;
    }

    public Date getX() {
        return x;
    }

    public void setX(Date x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private Date x;
    private double y;
}
