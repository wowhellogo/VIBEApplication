package com.vibe.app.model.event;

/**
 * @author linguoding
 * @Package com.vibe.app.model.event
 * @作 用:选中设备
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月28日  20:02
 */


public class SelectDeviceEvent {
    public String mac;

    public SelectDeviceEvent(String mac) {
        this.mac = mac;
    }
}
