package com.vibe.app.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.vibe.app.model.VibeType;
import com.vibe.app.model.VibeRecord;
import com.vibe.app.model.Reminder;

import com.vibe.app.dao.VibeTypeDao;
import com.vibe.app.dao.VibeRecordDao;
import com.vibe.app.dao.ReminderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig vibeTypeDaoConfig;
    private final DaoConfig vibeRecordDaoConfig;
    private final DaoConfig reminderDaoConfig;

    private final VibeTypeDao vibeTypeDao;
    private final VibeRecordDao vibeRecordDao;
    private final ReminderDao reminderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        vibeTypeDaoConfig = daoConfigMap.get(VibeTypeDao.class).clone();
        vibeTypeDaoConfig.initIdentityScope(type);

        vibeRecordDaoConfig = daoConfigMap.get(VibeRecordDao.class).clone();
        vibeRecordDaoConfig.initIdentityScope(type);

        reminderDaoConfig = daoConfigMap.get(ReminderDao.class).clone();
        reminderDaoConfig.initIdentityScope(type);

        vibeTypeDao = new VibeTypeDao(vibeTypeDaoConfig, this);
        vibeRecordDao = new VibeRecordDao(vibeRecordDaoConfig, this);
        reminderDao = new ReminderDao(reminderDaoConfig, this);

        registerDao(VibeType.class, vibeTypeDao);
        registerDao(VibeRecord.class, vibeRecordDao);
        registerDao(Reminder.class, reminderDao);
    }
    
    public void clear() {
        vibeTypeDaoConfig.getIdentityScope().clear();
        vibeRecordDaoConfig.getIdentityScope().clear();
        reminderDaoConfig.getIdentityScope().clear();
    }

    public VibeTypeDao getVibeTypeDao() {
        return vibeTypeDao;
    }

    public VibeRecordDao getVibeRecordDao() {
        return vibeRecordDao;
    }

    public ReminderDao getReminderDao() {
        return reminderDao;
    }

}
