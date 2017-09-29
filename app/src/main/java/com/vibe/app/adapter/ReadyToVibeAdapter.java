package com.vibe.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.adapter.BaseViewHolderHelper;
import com.vibe.app.R;
import com.vibe.app.model.VibeType;

/**
 * @Package com.vibe.app.adapter
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月29日  16:52
 */


public class ReadyToVibeAdapter extends BaseRecyclerViewAdapter<VibeType> {
    public ReadyToVibeAdapter(RecyclerView recyclerView, int defaultItemLayoutId) {
        super(recyclerView, defaultItemLayoutId);
    }

    @Override
    protected void fillData(BaseViewHolderHelper helper, int position, VibeType model) {
        helper.setText(R.id.tv_name, model.getName());
        helper.setImageResource(R.id.im_vibe_icon, model.getIcon());
        if (position == 0) {
            helper.getView(R.id.v_line).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.v_line).setVisibility(View.VISIBLE);
        }
    }
}
