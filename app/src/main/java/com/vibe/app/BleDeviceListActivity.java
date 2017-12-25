package com.vibe.app;

import android.os.Bundle;

import com.hao.common.base.BaseLoadActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.nucleus.presenter.LoadPresenter;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.widget.LoadingLayout;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.vibe.app.adapter.BleDeviceAdapter;

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

    private void addItem(RxBleDevice device) {
        if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            for (RxBleDevice bleDevice : mAdapter.getData()) {
                if (!bleDevice.getMacAddress()
                        .equals(device.getMacAddress())) {
                    mAdapter.addFirstItem(device);
                }
            }
        } else {
            mAdapter.addFirstItem(device);

        }
    }
}
