package com.vibe.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.TextView;

import com.hao.common.base.BaseActivity;
import com.hao.common.runtimepermissions.PermissionsManager;
import com.hao.common.runtimepermissions.PermissionsResultAction;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.SPUtil;
import com.hao.common.utils.StringUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.widget.titlebar.TitleBar;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleConnection;
import com.vibe.app.dao.VibeTypeDao;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.BleTransfersData;
import com.vibe.app.model.Constant;
import com.vibe.app.model.Reminder;
import com.vibe.app.model.VibeRecord;
import com.vibe.app.model.VibeType;
import com.vibe.app.service.BleControlService;
import com.vibe.app.utils.ByteUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.dao.AbstractDao;
import rx.Observable;


public class MainActivity extends BaseActivity {
    private AbstractDatabaseManager<VibeType, Long> mDatabaseManager = new AbstractDatabaseManager<VibeType, Long>() {
        @Override
        public AbstractDao<VibeType, Long> getAbstractDao() {
            return daoSession.getVibeTypeDao();
        }
    };

    private AbstractDatabaseManager<VibeRecord, Long> mVibeRecordDatabaseManager = new AbstractDatabaseManager<VibeRecord, Long>() {
        @Override
        public AbstractDao<VibeRecord, Long> getAbstractDao() {
            return daoSession.getVibeRecordDao();
        }
    };

    private AbstractDatabaseManager<Reminder, Long> reminderDatabaseManager = new AbstractDatabaseManager<Reminder, Long>() {
        @Override
        public AbstractDao<Reminder, Long> getAbstractDao() {
            return daoSession.getReminderDao();
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
    @Bind(R.id.tv_about)
    TextView mTvAbout;
    private TitleBar mTitleBar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    private BleControlReceiver bleControlReceiver;

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
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });

        registerReceiver();
        Intent intent = new Intent(this, BleControlService.class);
        startService(intent);

    }

    public void insertDate() {
        if (!SPUtil.getBoolean("insert", false)) {
            List<VibeType> list = new ArrayList<>();
            list.add(new VibeType(1L, "advice of orthovibe", 3, 10, 5, true));
            list.add(new VibeType(2L, "vibration", 1, 10, 5, false));
            list.add(new VibeType(3L, "wave", 2, 10, 5, false));
            list.add(new VibeType(4L, "pulse", 0, 10, 5, false));
            Observable.just(mDatabaseManager.insertOrReplaceList(list))
                    .compose(RxUtil.applySchedulersJobUI())
                    .compose(bindToLifecycle())
                    .subscribe(aBoolean -> {
                        SPUtil.putBoolean("insert", true);
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

    @OnClick({R.id.tv_pair_to_vibe, R.id.tv_ready_to_vieb, R.id.tv_set_reminder, R.id.tv_history, R.id.tv_about, R.id.im_start, R.id.im_pause})
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
                mSwipeBackHelper.forward(ReminderListActivity.class);
                onClickLeftCtv();
                break;
            case R.id.tv_history:
                mSwipeBackHelper.forward(VibeRecordListActivity.class);
                onClickLeftCtv();
                break;
            case R.id.tv_about:
                mSwipeBackHelper.forward(AboutsActivity.class);
                onClickLeftCtv();
                break;
            case R.id.im_start:
                startCmd();
                break;
            case R.id.im_pause:
                sendCmdBroadcast(this, BleControlService.CMD_ACTION, new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_ON_OFF)
                        .setContent(0x00)
                        .builder().dataPackage());
                break;
            default:
                break;
        }
    }

    private void startCmd() {
        sendCmdBroadcast(this, BleControlService.CMD_ACTION, new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_ON_OFF)
                .setContent(0x01)
                .builder().dataPackage());

        VibeType vibeType = mDatabaseManager.getQueryBuilder().where(VibeTypeDao.Properties.Selected.eq(true)).unique();
        VibeRecord vibeRecord = new VibeRecord();
        vibeRecord.setCreateDate(new Date());
        vibeRecord.setDuration(vibeType.getTime());
        vibeRecord.setVibeType(vibeType);
        Observable.just(mVibeRecordDatabaseManager.insert(vibeRecord))
                .compose(RxUtil.applySchedulersJobUI())
                .subscribe(aBoolean -> {

                }, throwable -> {
                });


    }


    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        bleControlReceiver = new BleControlReceiver();
        this.registerReceiver(bleControlReceiver, BleControlService.getIntentFilter());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(bleControlReceiver);
    }

    /**
     * 接收蓝牙设备工作状态，电量，状态等广播接收器
     */
    public class BleControlReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BleControlService.BLE_CONNECTION_STATE:
                    RxBleConnection.RxBleConnectionState state = (RxBleConnection.RxBleConnectionState) intent.getExtras().get("result");
                    setConnectStateUI(state);
                    break;
                case BleControlService.RECEIVE_JOB_STATE:
                    byte[] result = intent.getExtras().getByteArray("result");
                    if (StringUtil.isEmpty(mTitleBar.getRightCtv().getText())) {
                        mTitleBar.setRightText("Connected");
                    }
                    Logger.e("Main----收到的结果：" + ByteUtil.bytesToHexString(result));
                    break;
                case BleControlService.RECEIVE_BATTERY_LEVEL:
                    byte[] level = intent.getExtras().getByteArray("result");
                    Logger.e("电量：" + ByteUtil.bytesToHexString(level));
                    break;
                case BleControlService.BLE_STATE_CHANGES:
                    boolean isEnableBle = intent.getExtras().getBoolean("result");
                    ToastUtil.show(isEnableBle ? "蓝牙已开启" : "蓝牙已关闭");
                    break;
                default:
                    break;
            }
        }
    }

    private void setConnectStateUI(RxBleConnection.RxBleConnectionState state) {
        switch (state) {
            case CONNECTED:
                mTitleBar.setRightText("Connected");
                Logger.e("MainActivity-设备连接成功");
                break;
            case CONNECTING:
                mTitleBar.setRightText("Connecting");
                Logger.e("MainActivity-设备连接中");
                break;
            case DISCONNECTED:
                mTitleBar.setRightText("Disconnected");
                Logger.e("MainActivity-设备已断开");
                break;
            case DISCONNECTING:
                Logger.e("MainActivity-设备断开中");
                break;
            default:
                break;
        }
    }

    public static void sendCmdBroadcast(Context context, String action, byte[] cmd) {
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(action);
        //发送广播的数据
        intent.putExtra(BleControlService.CMD, cmd);
        //发送
        context.sendBroadcast(intent);
    }

    public static void sendOperationBroadcast(Context context, String action, int operation, String mac) {
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(action);
        //发送广播的数据
        intent.putExtra(BleControlService.OPERATION, operation);
        intent.putExtra(Constant.MAC, mac);
        //发送
        context.sendBroadcast(intent);
    }

}
