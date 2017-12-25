package com.vibe.app;

import android.os.Bundle;
import android.view.View;

import com.hao.common.base.BaseActivity;

/**
 * @author Administrator
 * @Package com.vibe.app
 * @作 用:配对设备
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月15日  14:42
 */


public class PairToVibeActivity extends BaseActivity {
    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_pair_to_vibe;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        getViewById(R.id.tv_exlore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeBackHelper.forward(BleDeviceListActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
