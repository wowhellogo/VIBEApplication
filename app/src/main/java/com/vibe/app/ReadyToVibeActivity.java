package com.vibe.app;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.hao.common.base.BaseActivity;
import com.vibe.app.adapter.ReadyToVibeAdapter;
import com.vibe.app.model.ReadyModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReadyToVibeActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ReadyToVibeAdapter mAdapter;

    private List<ReadyModel> mReadyModels;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_ready_to_vibe;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mAdapter = new ReadyToVibeAdapter(mRecyclerView, R.layout.item_ready_to_vibe);
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        mReadyModels=new ArrayList<>();
        mAdapter.setData(mReadyModels);

    }


    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
