package com.vibe.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.hao.common.exception.ApiException;
import com.hao.common.manager.AppManager;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxEvent;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleClient;
import com.squareup.leakcanary.RefWatcher;
import com.vibe.app.database.AbstractDatabaseManager;

/**
 * @Package com.vibe.app
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月29日  17:38
 */


public class VibeApplication extends Application implements AppManager.Delegate {
    private RefWatcher mRefWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        AbstractDatabaseManager.initOpenHelper(this);//初始化数据库操作
        if (AppManager.isInOtherProcess(this)) {
            Log.e("App", "enter the other process!");
            return;
        }
        // 初始化应用程序管理器
        AppManager.getInstance().init(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug"), this);
        RxBus.toObservable(RxEvent.AppEnterForegroundEvent.class).subscribe(appEnterForegroundEvent -> appEnterForeground());
        RxBus.toObservable(RxEvent.AppEnterBackgroundEvent.class).subscribe(appEnterBackgroundEvent -> appEnterBackground());

    }

    @Override
    public void refWatcherWatchFragment(Fragment fragment) {
        mRefWatcher.watch(fragment);
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
