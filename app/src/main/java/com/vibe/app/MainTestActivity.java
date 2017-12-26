package com.vibe.app;

import android.os.Bundle;
import android.view.View;

import com.hao.common.base.BaseDataBindingActivity;
import com.hao.common.nucleus.presenter.RxPresenter;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.vibe.app.databinding.ActivityMainTestBinding;
import com.vibe.app.model.Constant;
import com.vibe.app.utils.ByteUtil;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author linguoding
 * @Package com.vibe.app
 * @作 用:测试界面
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月26日  10:26
 */


public class MainTestActivity extends BaseDataBindingActivity<RxPresenter, ActivityMainTestBinding> {

    private final static String MAC = "C1:C3:27:CA:19:3C";
    private Subscription subscription;
    private RxBleClient mRxBleClient;
    private RxBleConnection mRxBleConnection;//当前连接

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_main_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRxBleClient = RxBleClient.create(this);
    }

    @Override
    protected void setListener() {
        mBinding.btnCloseLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCMD();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        sendCMD();
    }


    private void sendCMD() {
        subscription = mRxBleClient.scanBleDevices(new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                new ScanFilter.Builder()
                        .build())
                .compose(bindToLifecycle())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.e("开始");
                    }
                })
                .exists(new Func1<ScanResult, Boolean>() {
                    @Override
                    public Boolean call(ScanResult scanResult) {
                        Logger.e(scanResult.toString());
                        return scanResult.getBleDevice().getMacAddress().equals(MAC);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<RxBleConnection>>() {
                    @Override
                    public Observable<RxBleConnection> call(Boolean aBoolean) {
                        return mRxBleClient.getBleDevice(MAC).establishConnection(false);//连接GATT
                    }
                }).flatMap(new Func1<RxBleConnection, Observable<Observable<byte[]>>>() {
                    @Override
                    public Observable<Observable<byte[]>> call(RxBleConnection rxBleConnection) {
                        mRxBleConnection = rxBleConnection;
                        return rxBleConnection.setupNotification(Constant.NOTIFY_SERVICE_UUID_COMMUNICATION);
                    }
                }).doOnNext(new Action1<Observable<byte[]>>() {
                    @Override
                    public void call(Observable<byte[]> observable) {
                        observable.subscribe(new Action1<byte[]>() {
                            @Override
                            public void call(byte[] bytes) {
                                Logger.e("返回的结果：" + ByteUtil.bytesToHexString(bytes));
                                if (subscription != null) {
                                    subscription.unsubscribe();
                                    subscription = null;
                                }
                            }
                        });
                    }
                }).flatMap(new Func1<Observable<byte[]>, Observable<byte[]>>() {
                    @Override
                    public Observable<byte[]> call(Observable<byte[]> observable) {
                        return mRxBleConnection.writeCharacteristic(Constant.WRITE_SERVICE_UUID_COMMUNICATION, stopCmd());//开锁指令写入GATT
                    }
                }).subscribe(new Action1<byte[]>() {
                    @Override
                    public void call(byte[] bytes) {
                        Logger.e("发送指令：" + ByteUtil.bytesToHexString(bytes));

                    }
                });
    }


    private byte[] stopCmd() {
        byte[] bts = new byte[5];
        bts[0] = (byte) 0x08;
        bts[1] = 0x01;
        bts[2] = 0x00;
        bts[3] = 0x00;
        bts[4] = 0x00;
        return bts;

    }


}
