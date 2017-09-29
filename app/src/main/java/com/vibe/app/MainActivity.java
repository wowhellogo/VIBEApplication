package com.vibe.app;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.TextView;

import com.hao.common.base.BaseActivity;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.SPUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.widget.titlebar.TitleBar;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.VibeType;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.dao.AbstractDao;
import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    private AbstractDatabaseManager<VibeType, Long> mDatabaseManager = new AbstractDatabaseManager<VibeType, Long>() {
        @Override
        public AbstractDao<VibeType, Long> getAbstractDao() {
            return daoSession.getVibeTypeDao();
        }
    };

    @Bind(R.id.tv_pair_to_vibe)
    TextView mTvPairToVibe;
    @Bind(R.id.tv_ready_to_vieb)
    TextView mTvReadyToVieb;
    @Bind(R.id.tv_set_reminder)
    TextView mTvSetReminder;
    @Bind(R.id.tv_history)
    TextView mTvHistory;
    @Bind(R.id.tv_languages)
    TextView mTvLanguages;
    @Bind(R.id.tv_about)
    TextView mTvAbout;
    private TitleBar mTitleBar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitleBar = (TitleBar) getViewById(R.id.title_bar);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        insertDate();
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        mTitleBar.setDelegate(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void insertDate() {
        if (!SPUtil.getBoolean("insert", false)) {
            List<VibeType> list = new ArrayList<>();
            list.add(new VibeType(1l, "advice of orthovibe", R.mipmap.ic_choice_orthovybe, 1, 1));
            list.add(new VibeType(2l, "vibration", R.mipmap.ic_choice_vibration, 1, 1));
            list.add(new VibeType(3l, "wave", R.mipmap.ic_choice_wave, 1, 1));
            list.add(new VibeType(4l, "pulse", R.mipmap.ic_choice_pulse, 1, 1));
            Observable.just(mDatabaseManager.insertOrReplaceList(list))
                    .compose(RxUtil.applySchedulersJobUI())
                    .compose(bindToLifecycle())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            ToastUtil.show("初始化成功");
                            SPUtil.putBoolean("insert", true);
                        }
                    });
        }

    }


    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void onClickLeftCtv() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_pair_to_vibe, R.id.tv_ready_to_vieb, R.id.tv_set_reminder, R.id.tv_history, R.id.tv_languages, R.id.tv_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pair_to_vibe:
                mSwipeBackHelper.forward(PairToVibeActivity.class);
                onClickLeftCtv();
                break;
            case R.id.tv_ready_to_vieb:
                mSwipeBackHelper.forward(ReadyToVibeActivity.class);
                onClickLeftCtv();
                break;
            case R.id.tv_set_reminder:
                onClickLeftCtv();
                break;
            case R.id.tv_history:
                onClickLeftCtv();
                break;
            case R.id.tv_languages:
                onClickLeftCtv();
                break;
            case R.id.tv_about:
                onClickLeftCtv();
                break;
        }
    }
}
