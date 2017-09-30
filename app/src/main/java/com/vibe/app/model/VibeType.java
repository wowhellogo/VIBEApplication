package com.vibe.app.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.io.Serializable;

/**
 * Entity mapped to table "VIBE_TYPE".
 */
public class VibeType implements Serializable {

    private Long _id;
    private String name;
    private Integer icon;
    private Integer time;
    private Integer rate;
    private Boolean isSelected;

    public VibeType() {
    }

    public VibeType(Long _id) {
        this._id = _id;
    }

    public VibeType(Long _id, String name, Integer icon, Integer time, Integer rate, Boolean isSelected) {
        this._id = _id;
        this.name = name;
        this.icon = icon;
        this.time = time;
        this.rate = rate;
        this.isSelected = isSelected;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

}
