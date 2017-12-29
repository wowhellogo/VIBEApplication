package com.vibe.app;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.utils.ViewUtils;
import com.vibe.app.adapter.VibeRecordAdapter;
import com.vibe.app.dao.VibeRecordDao;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.VibeRecord;

import de.greenrobot.dao.AbstractDao;
import rx.Observable;

public class VibeRecordListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;


    private AbstractDatabaseManager<VibeRecord, Long> mDatabaseManager = new AbstractDatabaseManager<VibeRecord, Long>() {
        @Override
        public AbstractDao<VibeRecord, Long> getAbstractDao() {
            return daoSession.getVibeRecordDao();
        }
    };
    private RecyclerView mRecyclerView;
    private VibeRecordAdapter mAdapter;


    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_vibe_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_history));
        mRecyclerView = (RecyclerView) getViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) getViewById(R.id.refresh_layout);
        mAdapter = new VibeRecordAdapter(mRecyclerView, R.layout.item_vibe_record);
        ViewUtils.initVerticalLinearRecyclerView(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        Observable.just(mDatabaseManager.getQueryBuilder().orderDesc(VibeRecordDao.Properties.CreateDate).list())
                .compose(RxUtil.applySchedulersJobUI())
                .compose(bindToLifecycle())
                .subscribe(vibeRecords -> {
                    mAdapter.setData(vibeRecords);
                    mRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    ToastUtil.show(throwable.getMessage());
                });

    }


    @Override
    protected void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onRefresh() {
        initData();

    }
}
