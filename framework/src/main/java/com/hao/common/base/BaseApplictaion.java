package com.hao.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.hao.common.BuildConfig;
import com.hao.common.exception.ApiException;
import com.hao.common.manager.AppManager;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxEvent;
import com.orhanobut.logger.Logger;

/**
 * @Package com.example.raymond.rxdemo.common
 * @作 用:App的基本Applictaion类
 * @创 建 人: linguoding
 * @日 期: 2016-01-05
 */
public class BaseApplictaion extends Application implements AppManager.Delegate {
    @Override
    public void onCreate() {
        super.onCreate();
        if (AppManager.isInOtherProcess(this)) {
            Log.e("App", "enter the other process!");
            return;
        }

        // 初始化应用程序管理器
        AppManager.getInstance().init("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE), this);

        RxBus.toObservable(RxEvent.AppEnterForegroundEvent.class).subscribe(appEnterForegroundEvent -> appEnterForeground());
        RxBus.toObservable(RxEvent.AppEnterBackgroundEvent.class).subscribe(appEnterBackgroundEvent -> appEnterBackground());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void refWatcherWatchFragment(Fragment fragment) {

    }

    @Override
    public boolean isActivityNotContainFragment(Activity activity) {
        return true;
    }


    @Override
    public void handleServerException(ApiException apiException) {
        Logger.i("处理网络请求异常");
    }

    private void appEnterForeground() {
        Logger.i("应用进入前台");
    }

    private void appEnterBackground() {
        Logger.i("应用进入后台");
    }
}



