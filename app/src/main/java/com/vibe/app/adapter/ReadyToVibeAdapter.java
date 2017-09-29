package com.vibe.app.adapter;

import android.support.v7.widget.RecyclerView;

import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.adapter.BaseViewHolderHelper;
import com.vibe.app.model.ReadyModel;

/**
 * @Package com.vibe.app.adapter
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月29日  16:52
 */


public class ReadyToVibeAdapter extends BaseRecyclerViewAdapter<ReadyModel> {
    public ReadyToVibeAdapter(RecyclerView recyclerView, int defaultItemLayoutId) {
        super(recyclerView, defaultItemLayoutId);
    }

    @Override
    protected void fillData(BaseViewHolderHelper helper, int position, ReadyModel model) {

    }
}
