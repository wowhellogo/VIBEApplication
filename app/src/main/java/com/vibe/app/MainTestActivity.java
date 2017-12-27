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
import com.vibe.app.model.BleTransfersData;
import com.vibe.app.model.Constant;
import com.vibe.app.utils.ByteUtil;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

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
                sendCMD(stopCmd());
            }
        });
        mBinding.btnOpenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(startCmd());
            }
        });

        mBinding.btnSetMode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(setMode1());
            }
        });

        mBinding.bntSetMode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(setMode2());
            }
        });

        mBinding.btnSetModel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(setMode3());
            }
        });

        mBinding.btnSetStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(setStrength());
            }
        });

        mBinding.btnSetTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_TIME_DURATION)
                        .setContent(0x01,0x00).builder().dataPackage());
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        scanConnect();
    }


    private void scanConnect() {

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
                                Logger.e("返回的内容："+ByteUtil.bytesToHexString(bytes));
                            }
                        });
                    }
                }).flatMap(new Func1<Observable<byte[]>, Observable<byte[]>>() {
                    @Override
                    public Observable<byte[]> call(Observable<byte[]> observable) {
                        return mRxBleConnection.setupNotification(Constant.BATTERY_LEVEL).flatMap(new Func1<Observable<byte[]>, Observable<byte[]>>() {
                            @Override
                            public Observable<byte[]> call(Observable<byte[]> observable) {
                                return observable;
                            }
                        });
                    }
                }).subscribe(new Action1<byte[]>() {
                    @Override
                    public void call(byte[] bytes) {
                        Logger.e("电量："+ByteUtil.bytesToHexString(bytes));
                        Logger.e(ByteUtil.getString(bytes,"utf-8"));
                    }
                });
    }


    public void sendCMD(byte[] bts) {
        mRxBleConnection.writeCharacteristic(Constant.WRITE_SERVICE_UUID_COMMUNICATION, bts)
                .subscribe(new Action1<byte[]>() {
                    @Override
                    public void call(byte[] bytes) {
                        Logger.e("发送的指令：" + ByteUtil.bytesToHexString(bytes));
                    }
                });
    }


    private byte[] stopCmd() {
        /*byte[] bts = new byte[5];
        bts[0] = (byte) 0x08;
        bts[1] = 0x00;
        bts[2] = 0x00;
        bts[3] = 0x00;
        bts[4] = 0x00;
        return bts;*/
        return new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_ON_OFF)
                .setContent(0x00)
                .builder().dataPackage();

    }

    private byte[] startCmd() {
       /* byte[] bts = new byte[5];
        bts[0] = (byte) 0x08;
        bts[1] = 0x01;
        bts[2] = 0x00;
        bts[3] = 0x00;
        bts[4] = 0x00;
        return bts;*/
        return new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_ON_OFF)
                .setContent(0x01)
                .builder().dataPackage();
    }


    public byte[] setMode1() {
        return new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_MODE)
                .setContent(0x00, 0x00)
                .builder().dataPackage();
    }

    public byte[] setMode2() {
        return new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_MODE)
                .setContent(0x00, 0x01)
                .builder().dataPackage();
    }
    public byte[] setMode3() {
        return new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_MODE)
                .setContent(0x00, 0x02)
                .builder().dataPackage();
    }

    public byte[] setStrength(){
        return new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_STRENGTH)
                .setContent(0x00,0x01)
                .builder().dataPackage();
    }







}
