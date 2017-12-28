package com.vibe.app;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.utils.ViewUtils;
import com.vibe.app.adapter.ReadyToVibeAdapter;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.VibeType;
import com.vibe.app.model.event.UpdateVibeTypeEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.AbstractDao;
import rx.Observable;
import rx.functions.Action1;

public class ReadyToVibeActivity extends BaseActivity {
    private AbstractDatabaseManager<VibeType, Long> mDatabaseManager = new AbstractDatabaseManager<VibeType, Long>() {
        @Override
        public AbstractDao<VibeType, Long> getAbstractDao() {
            return daoSession.getVibeTypeDao();
        }
    };
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ReadyToVibeAdapter mAdapter;


    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_ready_to_vibe;
    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("Ready to vibe");
        mAdapter = new ReadyToVibeAdapter(mRecyclerView, R.layout.item_ready_to_vibe);
        ViewUtils.initVerticalLinearRecyclerView(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        Observable.just(mDatabaseManager.loadAll())
                .compose(RxUtil.applySchedulersJobUI())
                .compose(bindToLifecycle())
                .subscribe(new Action1<List<VibeType>>() {
                    @Override
                    public void call(List<VibeType> vibeTypes) {
                        mAdapter.setData(vibeTypes);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.show(throwable.getMessage());
                    }
                });
    }


    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        RxBus.toObservableAndBindToLifecycle(UpdateVibeTypeEvent.class, this)
                .subscribe(new Action1<UpdateVibeTypeEvent>() {
                    @Override
                    public void call(UpdateVibeTypeEvent updateVibeTypeEvent) {
                        initData();
                    }
                });
        mAdapter.setOnRVItemClickListener((parent, itemView, position) ->
                mSwipeBackHelper.forward(UpdateVibeTypeActivity.newIntent(this, mAdapter.getItem(position))));
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
