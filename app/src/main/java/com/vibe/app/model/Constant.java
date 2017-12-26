package com.vibe.app.model;

import com.vibe.app.utils.ByteUtil;

import java.util.UUID;

/**
 * Created by linguoding on 2017/12/16.
 */

public interface Constant {


    UUID BATTERY_SERVICE_UUID = UUID.nameUUIDFromBytes(ByteUtil.getBytes(0x18, 0x0F));//电量服务UUID
    UUID BATTERY_LEVEL = UUID.nameUUIDFromBytes(ByteUtil.getBytes(0x2A, 0x19));//电量特征UUID
    UUID NOTIFY_DEVICE_INFORMATION = UUID.nameUUIDFromBytes(ByteUtil.getBytes(0x18, 0x0A));//设备信息
    UUID READ_MANUFACTURER_NAME_STRING = UUID.nameUUIDFromBytes(ByteUtil.getBytes(0x2A, 0x29));//设备名称
    UUID READ_SOFTWARE_REVISION_STRING = UUID.nameUUIDFromBytes(ByteUtil.getBytes(0x2A, 0x28));//设备版本
    UUID READ_HARDWARE_REVISION_STRING = UUID.nameUUIDFromBytes(ByteUtil.getBytes(0x2A, 0x27));//设备版本

    //通信UUID
    UUID SERVICE1_UUID_COMMUNICATION = UUID.fromString("6e40fff0-b5a3-f393-e0a9-e50e24dcca9e");
    UUID NOTIFY_SERVICE_UUID_COMMUNICATION = UUID.fromString("6e40fff2-b5a3-f393-e0a9-e50e24dcca9e");
    UUID WRITE_SERVICE_UUID_COMMUNICATION = UUID.fromString("6e40fff1-b5a3-f393-e0a9-e50e24dcca9e");

    UUID SERVICE2_UUID_NOFIFY = UUID.fromString("00001530-1212-efde-1523-785feabcd123");
    UUID WRITE_SERVICE2_UUID_NOFIFY = UUID.fromString("00001532-1212-efde-1523-785feabcd123");
    UUID NOTIFY_SERVICE2_UUID_NOFIFY = UUID.fromString("00001531-1212-efde-1523-785feabcd123");
    UUID RED_SERVICE2_UUID_NOFIFY = UUID.fromString("00001534-1212-efde-1523-785feabcd123");


}
