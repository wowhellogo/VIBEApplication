package com.vibe.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hao.common.adapter.BaseDivider;
import com.hao.common.adapter.OnRVItemClickListener;
import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.utils.ViewUtils;
import com.vibe.app.adapter.ReminderAdapter;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.Reminder;
import com.vibe.app.model.event.UpdateAlarmEvent;
import com.vibe.app.utils.Utils;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import rx.Observable;
import rx.functions.Action1;

public class ReminderListActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        mSwipeRefreshLayout= (SwipeRefreshLayout) getViewById(R.id.swipe_refresh_layout);
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
                        mSwipeRefreshLayout.setRefreshing(false);
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
            mSwipeBackHelper.forward(SetAlarmActivity.class);

        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        mAdapter.setOnItemChildCheckedChangeListener((parent, childView, position, isChecked) -> {
            Reminder reminder = mAdapter.getItem(position);
            int id = Integer.valueOf(reminder.get_id() + "");
            reminder.setState(isChecked ? 1 : 0);
            Observable.just(mDatabaseManager.update(reminder))
                    .compose(RxUtil.applySchedulersJobUI())
                    .compose(bindToLifecycle())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            Utils.setClock(getContext(), reminder);
                            if (aBoolean) {
                                ToastUtil.show(R.string.set_successfully);
                            } else {
                                ToastUtil.show(R.string.set_fail);
                            }
                        }
                    });
        });

        mAdapter.setOnRVItemClickListener((parent, itemView, position) -> mSwipeBackHelper.forward(SetAlarmActivity.newIntent(this,mAdapter.getItem(position))));
        RxBus.toObservableAndBindToLifecycle(UpdateAlarmEvent.class,this)
                .subscribe(new Action1<UpdateAlarmEvent>() {
                    @Override
                    public void call(UpdateAlarmEvent updateAlarmEvent) {
                        initData();
                    }
                });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public Context getContext() {
        return this;
    }
}
