package com.vibe.app;

import android.os.Bundle;

import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;

/**
 * @author linguoding
 * @Package com.vibe.app
 * @作 用:
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月29日  13:35
 */


public class AboutsActivity extends BaseActivity {
    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle(getString(R.string.about));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
