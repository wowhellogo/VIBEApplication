package com.vibe.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vibe.app.dao.DaoMaster;
import com.vibe.app.dao.VibeRecordDao;
import com.vibe.app.dao.VibeTypeDao;


/**
 * @Package com.daoda.data_library.database
 * @作 用:app数据库辅助类
 * @创 建 人: linguoding
 * @日 期: 2016/1/10
 */
public class BaseOpenHelper extends DaoMaster.OpenHelper {
    public BaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //创建新表，注意createTable()是静态方法
                // SchoolDao.createTable(db, true);
                // 加入新字段
                // db.execSQL("ALTER TABLE 'moments' ADD 'audio_path' TEXT;");
                VibeTypeDao.createTable(db,true);
                VibeRecordDao.createTable(db,true);
                // TODO
                break;
        }
    }
}
