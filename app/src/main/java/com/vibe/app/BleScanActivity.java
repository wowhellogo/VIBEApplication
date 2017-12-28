package com.vibe.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hao.common.base.BaseActivity;
import com.hao.common.utils.StatusBarUtil;
import com.hao.common.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleConnection;
import com.vibe.app.service.BleControlService;
import com.vibe.app.utils.ByteUtil;

/**
 * @author linguoding
 * @Package com.vibe.app
 * @作 用:
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月22日  19:53
 */


public class BleScanActivity extends BaseActivity {
    private BroadcastReceiver bleControlReceiver;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_ble_scan;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerReceiver();
        MainActivity.sendOperationBroadcast(this, BleControlService.BLE_OPERATION_ACTION, BleControlService.CONNECT);
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
                    finish();
                    Logger.e("Main----收到的结果：" + ByteUtil.bytesToHexString(result));
                    break;
                default:
                    break;
            }
        }
    }

    private void setConnectStateUI(RxBleConnection.RxBleConnectionState state) {
        switch (state) {
            case CONNECTED:
                finish();
                Logger.e("MainActivity-设备连接成功");
                break;
            case CONNECTING:
                Logger.e("MainActivity-设备连接中");
                break;
            case DISCONNECTED:
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
