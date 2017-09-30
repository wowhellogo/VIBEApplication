package com.vibe.app.adapter;

import android.support.v7.widget.RecyclerView;

import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.adapter.BaseViewHolderHelper;
import com.hao.common.utils.CalendarUtil;
import com.vibe.app.R;
import com.vibe.app.model.VibeRecord;

/**
 * @Package com.vibe.app.adapter
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月30日  11:13
 */


public class VibeRecordAdapter extends BaseRecyclerViewAdapter<VibeRecord> {
    public VibeRecordAdapter(RecyclerView recyclerView, int defaultItemLayoutId) {
        super(recyclerView, defaultItemLayoutId);
    }

    @Override
    protected void fillData(BaseViewHolderHelper helper, int position, VibeRecord model) {
        helper.setText(R.id.tv_name, model.getVibeType().getName());
        helper.setText(R.id.tv_date, CalendarUtil.formatHourMinute(model.getCreateDate()));
        helper.setText(R.id.tv_time, model.getDuration()+"min");
        helper.setImageResource(R.id.im_vibe_icon, model.getVibeType().getIcon());

    }
}
