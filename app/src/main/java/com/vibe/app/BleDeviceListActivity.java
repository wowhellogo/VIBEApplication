package com.vibe.app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hao.common.base.BaseLoadActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.nucleus.presenter.LoadPresenter;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.SPUtil;
import com.hao.common.utils.StringUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.widget.LoadingLayout;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.vibe.app.adapter.BleDeviceAdapter;
import com.vibe.app.model.Constant;
import com.vibe.app.model.event.SelectDeviceEvent;

import rx.functions.Action1;

/**
 * Created by linguoding on 2017/12/25.
 */

public class BleDeviceListActivity extends BaseLoadActivity<LoadPresenter, RxBleDevice> {


    private RxBleClient rxBleClient;

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }


    @Override
    protected void createAdapter() {
        mAdapter = new BleDeviceAdapter(mRecyclerView);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        setTitle("explore vibes");
        setLoadingMoreEnabled(false);
        mLoadingLayout.setStatus(LoadingLayout.Success);
        rxBleClient = RxBleClient.create(this);

        rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                new ScanFilter.Builder()
                        // add custom filters if needed
                        .build()
        )
                .compose(RxUtil.applySchedulersJobUI())
                .compose(bindUntilEvent(ActivityEvent.STOP))
                .subscribe(new Action1<ScanResult>() {
                    @Override
                    public void call(ScanResult scanResult) {
                        addItem(scanResult.getBleDevice());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.show(throwable.getMessage());
                    }
                });

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //保存mac地址
        RxBleDevice device = mAdapter.getItem(position);
        if (!StringUtil.isEmpty(device.getName())
                && device.getName().contains(BleDeviceAdapter.VIBE_DEVICE)) {
            SPUtil.putString(Constant.DEVICE_NAME, device.getName());
            RxBus.send(new SelectDeviceEvent(device.getMacAddress()));
            finish();
        } else {
            ToastUtil.show("This device is not vibe device");
        }
    }

    private void addItem(RxBleDevice device) {
        if (mAdapter.getData() != null
                && mAdapter.getData().size() > 0) {
            for (RxBleDevice rxBleDevice1 : mAdapter.getData()) {
                if (rxBleDevice1.getMacAddress().equals(device.getMacAddress())) {
                    return;
                }
            }
        }
        mAdapter.addLastItem(device);
    }
}
