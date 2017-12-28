package com.vibe.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.ToastUtil;
import com.hao.common.widget.VibeProgressbarView;
import com.hao.common.widget.wheel.WheelView;
import com.orhanobut.logger.Logger;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.BleTransfersData;
import com.vibe.app.model.VibeType;
import com.vibe.app.model.event.UpdateVibeTypeEvent;
import com.vibe.app.service.BleControlService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.AbstractDao;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class UpdateVibeTypeActivity extends BaseActivity implements View.OnClickListener {

    private AbstractDatabaseManager<VibeType, Long> mDatabaseManager = new AbstractDatabaseManager<VibeType, Long>() {
        @Override
        public AbstractDao<VibeType, Long> getAbstractDao() {
            return daoSession.getVibeTypeDao();
        }
    };

    @Bind(R.id.wv_number)
    WheelView mWvNumber;
    @Bind(R.id.vibe_progressbar_view)
    VibeProgressbarView mVibeProgressbarView;
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
                return 26;
            }

            @Override
            protected String getItem(int index) {
                return String.valueOf(index);
            }

        };
        mWvNumber.setAdapter(adapter);
        mWvNumber.setCurrentItem(vibeType.getTime());
        mVibeProgressbarView.setprogress(vibeType.getRate() * 10);
        mTvTime.setText(getString(R.string.time_20min));
    }

    @Override
    protected void setListener() {
        mWvNumber.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mTvTime.setText(String.format("time:%dmin", (index)));
                vibeType.setTime(index);
            }
        });
        mVibeProgressbarView.setOnChangNumProgressListener(new VibeProgressbarView.OnChangNumProgressListener() {
            @Override
            public void onChangNumProgress(float num, int progress) {
                int rate = ((progress / 10) - 1) < 0 ? 0 : ((progress / 10) - 1);
                Logger.e("当前强度：" + rate);
                vibeType.setRate(rate);
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
                updateVibeType();
                break;
            default:
                break;
        }
    }


    public void updateVibeType() {
        Observable.just(new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_MODE).setContent(0x00, vibeType.getMode()).builder().dataPackage())
                .flatMap(new Func1<byte[], Observable<byte[]>>() {
                    @Override
                    public Observable<byte[]> call(byte[] bytes) {
                        sendCmdBroadcast(BleControlService.CMD_ACTION, bytes);
                        return Observable.just(new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_TIME_DURATION).setContent(vibeType.getTime()).builder().dataPackage());
                    }
                }).flatMap(new Func1<byte[], Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(byte[] bytes) {
                sendCmdBroadcast(BleControlService.CMD_ACTION, bytes);
                return Observable.just(new BleTransfersData.Builder().setCmd(BleTransfersData.Cmd.SET_STRENGTH).setContent(0x00, vibeType.getRate()).builder().dataPackage());
            }
        }).flatMap(new Func1<byte[], Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(byte[] bytes) {
                sendCmdBroadcast(BleControlService.CMD_ACTION, bytes);
                return Observable.just(updateVibeTypeList())
                        .compose(RxUtil.applySchedulersJobUI());
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    ToastUtil.show("设置成功");
                    RxBus.send(new UpdateVibeTypeEvent());
                    finish();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtil.show(throwable.getMessage());
            }
        });

    }

    private void sendCmdBroadcast(String action, byte[] cmd) {
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(action);
        //发送广播的数据
        intent.putExtra(BleControlService.CMD, cmd);
        //发送
        sendBroadcast(intent);
    }

    public boolean updateVibeTypeList() {
        if (vibeType.getSelected()) {
            return true;
        }
        vibeType.setSelected(true);
        List<VibeType> list = mDatabaseManager.loadAll();
        for (VibeType vibeType : list) {
            vibeType.setSelected(false);
        }
        return mDatabaseManager.updateList(list)&&mDatabaseManager.update(this.vibeType);
    }

}
