package com.vibe.app.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hao.common.utils.SPUtil;
import com.hao.common.utils.StringUtil;
import com.hao.common.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.vibe.app.model.Constant;
import com.vibe.app.receiver.WakeReceiver;
import com.vibe.app.utils.ByteUtil;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState.CONNECTED;
import static com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState.CONNECTING;
import static com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState.DISCONNECTED;
import static com.polidea.rxandroidble.RxBleConnection.RxBleConnectionState.DISCONNECTING;

/**
 * @author linguoding
 * @Package com.vibe.app.service
 * @作 用:控制蓝牙的灰色保活服务
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月28日  09:43
 */
public class BleControlService extends Service {
    public final static String CMD_ACTION = "com.vibe.app.CMD_ACTION";//指令动作
    public final static String BLE_OPERATION_ACTION = "com.vibe.app.BLE_OPERATION_ACTION";

    public final static String RECEIVE_JOB_STATE = "com.vibe.app.RECEIVE_JOB_STATE";//接收设备工作状态指令的动作
    public final static String RECEIVE_BATTERY_LEVEL = "com.vibe.app.RECEIVE_BATTERY_LEVEL";//接收设备电量指令的动作
    public final static String BLE_CONNECTION_STATE = "com.vibe.app.BLE_CONNECTION_STATE";//接收连接状态的动作
    public final static String BLE_STATE_CHANGES = "com.vibe.app.BLE_STATE_CHANGES";//蓝牙是否可用的动作
    public final static int RECEIVE_JOB_STATE_WHAT = 1;
    public final static int RECEIVE_BATTERY_LEVEL_WHAT = 2;
    public final static int BLE_CONNECTION_STATE_WHAT = 3;
    public final static int BLE_STATE_CHANGES_WHAT = 4;

    public final static int CONNECT = 1;
    public final static int DISCONNECT = 2;
    public final static String OPERATION = "operation";
    public final static String CMD = "cmd";
    public final static String RESULT = "result";


    /**
     * 定时唤醒的时间间隔，5分钟
     */
    private final static int ALARM_INTERVAL = 5 * 60 * 1000;
    private final static int WAKE_REQUEST_CODE = 6666;

    private final static int GRAY_SERVICE_ID = -1001;


    private RxBleClient mRxBleClient;
    private Subscription subscription;
    private RxBleConnection mRxBleConnection;
    private BleControlReceiver mBleControlReceiver;



    /**
     * 主线程发送设备工作状态，电量的广播
     */
    private Handler mHandler;

    @Override
    public void onCreate() {
        Logger.e("BleControlService->onCreate");
        super.onCreate();

    }

    private void setConnectionStateListener() {
        mRxBleClient.getBleDevice(SPUtil.getString(Constant.MAC)).observeConnectionStateChanges()
                .subscribe(
                        connectionState -> {
                            switch (connectionState) {
                                case CONNECTED:
                                    if (mHandler != null) {
                                        mHandler.sendMessage(Message.obtain(mHandler, BLE_CONNECTION_STATE_WHAT, CONNECTED));
                                    }
                                    Logger.e("设备连接成功");
                                    break;
                                case CONNECTING:
                                    if (mHandler != null) {
                                        mHandler.sendMessage(Message.obtain(mHandler, BLE_CONNECTION_STATE_WHAT, CONNECTING));
                                    }
                                    Logger.e("设备连接中");
                                    break;
                                case DISCONNECTED:
                                    if (mHandler != null) {
                                        mHandler.sendMessage(Message.obtain(mHandler, BLE_CONNECTION_STATE_WHAT, DISCONNECTED));
                                    }
                                    Logger.e("设备已断开");
                                    mRxBleConnection = null;
                                    break;
                                case DISCONNECTING:
                                    if (mHandler != null) {
                                        mHandler.sendMessage(Message.obtain(mHandler, BLE_CONNECTION_STATE_WHAT, DISCONNECTING));
                                    }
                                    Logger.e("设备断开中");
                                    break;
                                default:
                                    break;
                            }
                        },
                        throwable -> {
                            // Handle an error here.
                            ToastUtil.show(throwable.getMessage());
                        }
                );
    }


    private void scanConnect() {
        Logger.e("自动搜索连接...");
        subscription = mRxBleClient.scanBleDevices(new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                new ScanFilter.Builder()
                        .build()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                Logger.e("开始搜索...");
            }}).exists(new Func1<ScanResult, Boolean>() {
                    @Override
                    public Boolean call(ScanResult scanResult) {
                        Logger.e(scanResult.toString());
                        return scanResult.getBleDevice().getMacAddress().equals(SPUtil.getString(Constant.MAC));
                    }
                })
                .flatMap(new Func1<Boolean, Observable<RxBleConnection>>() {
                    @Override
                    public Observable<RxBleConnection> call(Boolean aBoolean) {
                        //连接
                        return mRxBleClient.getBleDevice(SPUtil.getString(Constant.MAC)).establishConnection(false);
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
                                Logger.e("返回的内容：" + ByteUtil.bytesToHexString(bytes));
                                if (mHandler != null) {
                                    Message message = mHandler.obtainMessage();
                                    message.what = RECEIVE_JOB_STATE_WHAT;
                                    message.obj = bytes;
                                    mHandler.sendMessage(message);
                                }

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
                        Logger.e("电量：" + ByteUtil.bytesToHexString(bytes));
                        if (mHandler != null) {
                            Message message = mHandler.obtainMessage();
                            message.what = RECEIVE_BATTERY_LEVEL_WHAT;
                            message.obj = bytes;
                            mHandler.sendMessage(message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.show(throwable.getMessage());
                    }
                });
    }


    /**
     * 发送指令
     *
     * @param bts
     */
    private void sendCMD(byte[] bts) {
        Logger.e("发送的指令：" + ByteUtil.bytesToHexString(bts));
        if (mRxBleConnection == null) {
            ToastUtil.show("please connect again");
            return;
        }
        mRxBleConnection.writeCharacteristic(Constant.WRITE_SERVICE_UUID_COMMUNICATION, bts)
                .subscribe(bytes -> {
                });
    }

    private void sendBroadcast(String action, byte[] result) {
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(action);
        //发送广播的数据
        intent.putExtra(RESULT, result);
        //发送
        sendBroadcast(intent);
    }

    private void sendBroadcast(String action, boolean result) {
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(action);
        //发送广播的数据
        intent.putExtra(RESULT, result);
        //发送
        sendBroadcast(intent);
    }

    private void sendBroadcast(String action, RxBleConnection.RxBleConnectionState result) {
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(action);
        //发送广播的数据
        intent.putExtra(RESULT, result);
        //发送
        sendBroadcast(intent);
    }


    private void registerBleControlReceiver() {
        unRegisterBleControlReceiver();
        mBleControlReceiver = new BleControlReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CMD_ACTION);
        intentFilter.addAction(BLE_OPERATION_ACTION);
        this.registerReceiver(mBleControlReceiver, intentFilter);
    }

    private void unRegisterBleControlReceiver() {
        if (mBleControlReceiver != null) {
            this.unregisterReceiver(mBleControlReceiver);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case RECEIVE_JOB_STATE_WHAT:
                        sendBroadcast(RECEIVE_JOB_STATE, (byte[]) msg.obj);
                        break;
                    case RECEIVE_BATTERY_LEVEL_WHAT:
                        sendBroadcast(RECEIVE_BATTERY_LEVEL, (byte[]) msg.obj);
                        break;
                    case BLE_CONNECTION_STATE_WHAT:
                        sendBroadcast(BLE_CONNECTION_STATE, (RxBleConnection.RxBleConnectionState) msg.obj);
                        break;
                    case BLE_STATE_CHANGES_WHAT:
                        sendBroadcast(BLE_STATE_CHANGES, (boolean) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e("GrayService->onStartCommand");
        mRxBleClient = RxBleClient.create(this);
        initHandler();
        if (!StringUtil.isEmpty(SPUtil.getString(Constant.MAC)) && mRxBleConnection == null) {
            scanConnect();
            registerBleControlReceiver();
            setConnectionStateListener();
        }
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, operation);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Logger.e("GrayService->onDestroy");
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        unRegisterBleControlReceiver();
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public void onCreate() {
            Logger.e("InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Logger.e("InnerService -> onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Logger.e("InnerService -> onDestroy");
            super.onDestroy();
        }
    }


    /**
     * 接收来处用户的操作指令广播接收器
     */
    public class BleControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case CMD_ACTION:
                    sendCMD(intent.getByteArrayExtra(CMD));
                    break;
                case BLE_OPERATION_ACTION:
                    operationBle(intent.getIntExtra(OPERATION, 0));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 操作蓝牙
     *
     * @param operation 1：连接，2：断开
     */
    private void operationBle(int operation) {
        if (operation == CONNECT) {
            scanConnect();
        } else if (operation == DISCONNECT) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JOB_STATE);
        intentFilter.addAction(RECEIVE_BATTERY_LEVEL);
        intentFilter.addAction(BLE_CONNECTION_STATE);
        intentFilter.addAction(BLE_STATE_CHANGES);
        return intentFilter;

    }

}
