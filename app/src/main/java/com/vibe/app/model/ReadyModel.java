package com.vibe.app.model;

/**
 * @Package com.vibe.app.model
 * @作 用:选择模式
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月29日  16:49
 */


public class ReadyModel {
    private int id;
    private String name;
    private int time;
    private int rate;
    private int icon;

    public ReadyModel(int id, String name, int time, int rate) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ReadyModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", rate=" + rate +
                '}';
    }
}
