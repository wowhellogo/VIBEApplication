package com.daoda.main;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.mk.fortpeace.app.notification.model");
        schema.setDefaultJavaPackageDao("com.mk.fortpeace.dao");
        createNotificationModel(schema);
        new DaoGenerator().generateAll(schema, args[0]);

        Schema lockSchema = new Schema(1, "com.mk.fortpeace.app.lock.models");
        lockSchema.setDefaultJavaPackageDao("com.mk.fortpeace.app.lock.dao");
        createLockPasswordTable(lockSchema);
        createLockFingerprintTable(lockSchema);
        createLockDeviceTable(lockSchema);
        createLockLog(lockSchema);
        new DaoGenerator().generateAll(lockSchema, "../app/src/main/java");
    }





    private static void createNotificationModel(Schema schema) {
        Entity notification = schema.addEntity("Notification");// 表名
        notification.addLongProperty("_id").primaryKey().autoincrement().index();//主键
        notification.addStringProperty("accountGuid");
        notification.addStringProperty("title");
        notification.addStringProperty("description");
        notification.addIntProperty("group");
        notification.addIntProperty("type");
        notification.addStringProperty("createDate");
        notification.addBooleanProperty("isRead");
        notification.addStringProperty("data");
    }

    // lock data
    private static void createLockDeviceTable(Schema schema){
        Entity lockDevice = schema.addEntity("LockDevice");// 表名
        lockDevice.addLongProperty("_id").primaryKey().autoincrement().index();
        lockDevice.addStringProperty("mac");
        lockDevice.addStringProperty("name");
        lockDevice.addStringProperty("password");
        lockDevice.addBooleanProperty("autoOpen");
        lockDevice.addIntProperty("autoClose");
        lockDevice.addLongProperty("lastOpenTime");
        lockDevice.addDateProperty("latestUse");
    }

    private static void createLockPasswordTable(Schema schema){
        Entity lockUsers = schema.addEntity("LockPasswords");// 表名
        lockUsers.addLongProperty("_id").primaryKey().autoincrement().index();
        lockUsers.addStringProperty("mac");
        lockUsers.addStringProperty("password");
        lockUsers.addStringProperty("name");
        lockUsers.addIntProperty("authority");
        lockUsers.addDateProperty("date");
    }

    private static void createLockFingerprintTable(Schema schema){
        Entity lockUsers = schema.addEntity("LockFingerprints");// 表名
        lockUsers.addLongProperty("_id").primaryKey().autoincrement().index();
        lockUsers.addStringProperty("mac");
        lockUsers.addStringProperty("name");
        lockUsers.addIntProperty("fingerprint_no");
        lockUsers.addDateProperty("date");
    }

    private static void createLockLog(Schema schema){
        Entity lockUsers = schema.addEntity("LockLog");// 表名
        lockUsers.addLongProperty("_id").primaryKey().autoincrement().index();
        lockUsers.addStringProperty("mac");
        lockUsers.addStringProperty("name");
        lockUsers.addStringProperty("option");
        lockUsers.addDateProperty("date");
    }
}
