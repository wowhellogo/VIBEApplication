package com.vibe.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.adapter.BaseViewHolderHelper;
import com.hao.common.utils.StringUtil;
import com.polidea.rxandroidble.RxBleDevice;
import com.vibe.app.R;

/**
 * Created by linguoding on 2017/12/25.
 */

public class BleDeviceAdapter extends BaseRecyclerViewAdapter<RxBleDevice> {
    public final static String VIBE_DEVICE = "Vibe";

    public BleDeviceAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_ble_device;
    }

    @Override
    protected void fillData(BaseViewHolderHelper helper, int position, RxBleDevice model) {
        helper.setText(R.id.tv_name, model.getName() == null ? "Unknown" : model.getName());
        helper.setText(R.id.tv_mac, model.getMacAddress());
        if (position == 0) {
            helper.getView(R.id.v_line).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.v_line).setVisibility(View.VISIBLE);
        }
        if (!StringUtil.isEmpty(model.getName()) && model.getName().contains(VIBE_DEVICE)) {
            helper.getView(R.id.im_vibe_icon).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.im_vibe_icon).setVisibility(View.GONE);
        }
    }
}
