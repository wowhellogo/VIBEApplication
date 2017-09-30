package com.vibe.app;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.hao.common.adapter.BaseDivider;
import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.utils.ViewUtils;
import com.vibe.app.adapter.ReminderAdapter;
import com.vibe.app.alarmmanager.clock.AlarmManagerUtil;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.Reminder;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import rx.Observable;
import rx.functions.Action1;

public class ReminderListActivity extends BaseActivity {

    private AbstractDatabaseManager<Reminder, Long> mDatabaseManager = new AbstractDatabaseManager<Reminder, Long>() {
        @Override
        public AbstractDao<Reminder, Long> getAbstractDao() {
            return daoSession.getReminderDao();
        }
    };
    private RecyclerView mRecyclerView;
    private ReminderAdapter mAdapter;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_reminder_list;
    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("Set reminder");
        mRecyclerView = (RecyclerView) getViewById(R.id.recycler_view);
        ViewUtils.initVerticalLinearRecyclerView(this, mRecyclerView);
        mRecyclerView.addItemDecoration(BaseDivider.newBitmapDivider());
        mAdapter = new ReminderAdapter(mRecyclerView, R.layout.item_reminder);
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        Observable.just(mDatabaseManager.loadAll())
                .compose(RxUtil.applySchedulersJobUI())
                .compose(bindToLifecycle())
                .subscribe(new Action1<List<Reminder>>() {
                    @Override
                    public void call(List<Reminder> reminders) {
                        mAdapter.setData(reminders);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.show(throwable.getMessage());
                    }
                });
    }

    @Override
    protected void setListener() {
        getViewById(R.id.im_add).setOnClickListener(v -> {
            List<Reminder> reminders = new ArrayList<>();
            reminders.add(new Reminder("myReminder", 1, 8, 15, 0, "love Reminder", 2, 1));
            reminders.add(new Reminder("myReminder", 1, 9, 15, 0, "love Reminder", 2, 1));
            Observable.just(mDatabaseManager.insertOrReplaceList(reminders))
                    .compose(RxUtil.applySchedulersJobUI())
                    .compose(bindToLifecycle())
                    .subscribe(aBoolean -> {
                        ToastUtil.show("添加成功");
                    });
        });
        mAdapter.setOnItemChildCheckedChangeListener((parent, childView, position, isChecked) -> {
            Reminder reminder = mAdapter.getItem(position);
            int id = Integer.valueOf(reminder.get_id() + "");
            if (isChecked) {
                AlarmManagerUtil.setAlarm(this, reminder.getFlag(), reminder.getHour(), reminder.getMinute(),
                        id, reminder.getWeek(), reminder.getTips(), reminder.getSoundOrVibrator());
                ToastUtil.show("setAlarm");
            } else {
                AlarmManagerUtil.cancelAlarm(this, AlarmManagerUtil.ALARM_ACTION, id);
                ToastUtil.show("cancelAlarm");
            }
            reminder.setState(isChecked ? 1 : 0);
            Observable.just(mDatabaseManager.update(reminder))
                    .compose(RxUtil.applySchedulersJobUI())
                    .compose(bindToLifecycle())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                ToastUtil.show(R.string.set_successfully);
                            } else {
                                ToastUtil.show(R.string.set_fail);
                            }
                        }
                    });
        });


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
