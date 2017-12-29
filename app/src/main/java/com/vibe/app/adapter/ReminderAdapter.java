package com.vibe.app.adapter;

import android.support.v7.widget.RecyclerView;

import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.adapter.BaseViewHolderHelper;
import com.vibe.app.R;
import com.vibe.app.model.Reminder;
import com.vibe.app.utils.Utils;

/**
 * @Package com.vibe.app.adapter
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年09月30日  15:25
 */


public class ReminderAdapter extends BaseRecyclerViewAdapter<Reminder> {
    public ReminderAdapter(RecyclerView recyclerView, int defaultItemLayoutId) {
        super(recyclerView, defaultItemLayoutId);
    }

    @Override
    protected void fillData(BaseViewHolderHelper helper, int position, Reminder model) {
        helper.setText(R.id.tv_date, String.format("%s:%s", Utils.formatDate(model.getHour()), Utils.formatDate(model.getMinute())));
        if (model.getFlag() == 1) {
            helper.setText(R.id.tv_type, "Once");
        } else if (model.getFlag() == 2) {
            helper.setText(R.id.tv_type, "Week" + Utils.parseRepeat(model.getWeek(), 1));
        } else {
            helper.setText(R.id.tv_type, "Every day");
        }
        helper.setText(R.id.tv_reminder, String.format("%s   %s", model.getName(), model.getTips()));
        helper.setChecked(R.id.switch1, model.getState() == 1);
        helper.setItemChildCheckedChangeListener(R.id.switch1);
    }

}
