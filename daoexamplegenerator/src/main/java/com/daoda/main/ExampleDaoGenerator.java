package com.daoda.main;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.vibe.app.model");
        schema.setDefaultJavaPackageDao("com.vibe.app.dao");
        createTable(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }


    private static void createTable(Schema schema) {
        //模式表
        Entity vibeTypeTable = schema.addEntity("VibeType");// 表名
        vibeTypeTable.addLongProperty("_id").primaryKey().autoincrement().index();//主键
        vibeTypeTable.addStringProperty("name");
        vibeTypeTable.addIntProperty("icon");
        vibeTypeTable.addIntProperty("time");
        vibeTypeTable.addIntProperty("rate");
        vibeTypeTable.addBooleanProperty("isSelected");//选中

        //自慰棒自慰纪录表
        Entity vibeRecordTable = schema.addEntity("VibeRecord");
        vibeRecordTable.addLongProperty("_id").primaryKey().autoincrement().index();
        vibeRecordTable.addIntProperty("duration");
        vibeRecordTable.addDateProperty("createDate");
        vibeRecordTable.addDateProperty("endDate");
        Property vibeTypeId_fk = vibeRecordTable.addLongProperty("vibeTypeId").getProperty();//添加设备参数表索引

        //一条纪录对应一种模式
        vibeRecordTable.addToOne(vibeTypeTable, vibeTypeId_fk);
    }

    /*
    * 自慰棒纪录表
    * */
    private static void createVibeRecordModel(Schema schema) {


    }


   /* private static void createNotificationModel(Schema schema) {
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
    }*/
}
