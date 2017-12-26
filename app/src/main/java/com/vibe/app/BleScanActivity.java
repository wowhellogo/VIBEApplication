package com.vibe.app;

import android.os.Bundle;

import com.hao.common.base.BaseActivity;
import com.hao.common.utils.StatusBarUtil;

/**
 * @author linguoding
 * @Package com.vibe.app
 * @作 用:
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月22日  19:53
 */


public class BleScanActivity extends BaseActivity {
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

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
