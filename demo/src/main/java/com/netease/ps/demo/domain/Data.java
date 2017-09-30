package com.netease.ps.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzzhangxuan on 2017/9/26.
 */
@Entity
public class Data implements Serializable {
    private static final long serialVersionUID =1L;
    @Id
    @GeneratedValue
    private int id;

    public Data() {
    }

    public Data(Date time, String udid, String name, double value) {
        this.time = time;
        this.udid = udid;
        this.name = name;
        this.value = value;
    }

    @Column(nullable=false,unique=false)
    private Date time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Column(nullable=false,unique=false)
    private String udid;
    @Column(nullable=false,unique=false)
    private String name;
    @Column(nullable=false,unique=false)
    private double value;

}
