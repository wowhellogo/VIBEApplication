package com.vibe.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hao.common.base.BaseActivity;
import com.hao.common.widget.titlebar.TitleBar;

public class MainActivity extends BaseActivity {


    private TitleBar mTitleBar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitleBar= (TitleBar) getViewById(R.id.title_bar);
        toggle=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void setListener() {
        mTitleBar.setDelegate(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void onClickLeftCtv() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            drawer.openDrawer(GravityCompat.START);
        }

    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
