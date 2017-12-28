package com.vibe.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hao.common.base.BaseActivity;
import com.hao.common.rx.RxBus;
import com.hao.common.utils.SPUtil;
import com.hao.common.utils.StatusBarUtil;
import com.hao.common.utils.StringUtil;
import com.hao.common.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleConnection;
import com.vibe.app.model.Constant;
import com.vibe.app.model.event.SelectDeviceEvent;
import com.vibe.app.service.BleControlService;
import com.vibe.app.utils.ByteUtil;

import rx.functions.Action1;

/**
 * @author Administrator
 * @Package com.vibe.app
 * @作 用:配对设备
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月15日  14:42
 */


public class PairToVibeActivity extends BaseActivity {
    private BleControlReceiver bleControlReceiver;
    private ImageView imVibeDevice;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_pair_to_vibe;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        imVibeDevice = (ImageView) getViewById(R.id.im_vibe_device);
    }

    @Override
    protected void setListener() {
        RxBus.toObservableAndBindToLifecycle(SelectDeviceEvent.class,this)
                .subscribe(selectDeviceEvent -> MainActivity.sendOperationBroadcast(this,BleControlService.BLE_OPERATION_ACTION,BleControlService.CONNECT));

        getViewById(R.id.tv_exlore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeBackHelper.forward(BleDeviceListActivity.class);
            }
        });

        getViewById(R.id.tv_pair_to_vibe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(SPUtil.getString(Constant.MAC))) {
                    mSwipeBackHelper.forward(BleScanActivity.class);
                } else {
                    mSwipeBackHelper.forward(BleDeviceListActivity.class);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerReceiver();
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
                    if (imVibeDevice.getVisibility() == View.GONE) {
                        imVibeDevice.setVisibility(View.VISIBLE);
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
                imVibeDevice.setVisibility(View.VISIBLE);
                Logger.e("MainActivity-设备连接成功");
                break;
            case CONNECTING:

                Logger.e("MainActivity-设备连接中");
                break;
            case DISCONNECTED:
                imVibeDevice.setVisibility(View.GONE);
                Logger.e("MainActivity-设备已断开");
                break;
            case DISCONNECTING:
                Logger.e("MainActivity-设备断开中");
                break;
            default:
                break;
        }
    }


}
