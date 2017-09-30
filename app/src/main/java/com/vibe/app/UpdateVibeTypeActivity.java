package com.vibe.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.widget.wheel.WheelView;
import com.vibe.app.model.VibeType;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpdateVibeTypeActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.wv_number)
    WheelView mWvNumber;
    @Bind(R.id.vibe_progressbar_view)
    com.hao.common.widget.VibeProgressbarView mVibeProgressbarView;
    @Bind(R.id.tv_ok)
    TextView mTvOk;
    @Bind(R.id.tv_time)
    TextView mTvTime;

    WheelView.WheelAdapter adapter;

    VibeType vibeType;

    public static Intent newIntent(Context context, VibeType v) {
        Intent intent = new Intent(context, UpdateVibeTypeActivity.class);
        intent.putExtra("vibeType", v);
        return intent;
    }

    @Override
    protected int getRootLayoutResID() {
        vibeType = (VibeType) getIntent().getSerializableExtra("vibeType");
        if (vibeType.get_id() == 1) {
            return R.layout.activity_update_vibe_type_orthovibe;
        } else if (vibeType.get_id() == 2) {
            return R.layout.activity_update_vibe_type_vibration;
        } else if (vibeType.get_id() == 3) {
            return R.layout.activity_update_vibe_type_wave;
        } else {
            return R.layout.activity_update_vibe_type_pulse;
        }
    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }


    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        vibeType = (VibeType) getIntent().getSerializableExtra("vibeType");
        setTitle(vibeType.getName());
         /* 水平滑轮控件 */
        adapter = new WheelView.WheelAdapter() {
            @Override
            protected int getItemCount() {
                return 60;
            }

            @Override
            protected String getItem(int index) {
                return String.valueOf(index + 1);
            }
        };
        mWvNumber.setAdapter(adapter);
        mWvNumber.setCurrentItem(20);
        mTvTime.setText("time:20minute");
    }

    @Override
    protected void setListener() {
        mWvNumber.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                index += 1;
                mTvTime.setText("time:" + index + "minute");
            }
        });
        mTvOk.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:

                break;
        }
    }
}
