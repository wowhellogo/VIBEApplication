package com.vibe.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.rx.RxBus;
import com.hao.common.rx.RxUtil;
import com.hao.common.utils.StringUtil;
import com.hao.common.utils.ToastUtil;
import com.vibe.app.database.AbstractDatabaseManager;
import com.vibe.app.model.Reminder;
import com.vibe.app.model.event.UpdateAlarmEvent;
import com.vibe.app.model.event.UpdateVibeTypeEvent;
import com.vibe.app.utils.Utils;
import com.vibe.app.view.SelectRemindCyclePopup;
import com.vibe.app.view.SelectRemindWayPopup;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.dao.AbstractDao;

/**
 * 设置闹钟
 */
public class SetAlarmActivity extends BaseActivity implements View.OnClickListener {
    private AbstractDatabaseManager<Reminder, Long> mDatabaseManager = new AbstractDatabaseManager<Reminder, Long>() {
        @Override
        public AbstractDao<Reminder, Long> getAbstractDao() {
            return daoSession.getReminderDao();
        }
    };
    private TextView date_tv;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl, ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private LinearLayout allLayout;
    private String time;
    private int cycle;
    private int ring;
    private EditText tvName;
    private EditText tvRemark;

    private Reminder mReminder;

    public static Intent newIntent(Context context, Reminder reminder) {
        Intent intent = new Intent(context, SetAlarmActivity.class);
        intent.putExtra(Reminder.class.getSimpleName(), reminder);
        return intent;

    }

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_set_alarm;
    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("Set alarm");
        mTitleBar.setRightText("save");
        mReminder = new Reminder();
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        date_tv = (TextView) findViewById(R.id.date_tv);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        tvName = (EditText) findViewById(R.id.tv_name);
        tvRemark = (EditText) findViewById(R.id.tv_remark);


        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerView.Builder(SetAlarmActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time = getTime(date);
                        date_tv.setText(time);
                    }
                }).setType(new boolean[]{false, false, false, true, true, false})
                        .setLabel("year", "month", "day", "hours", "mins", "seconds")
                        .setSubmitText("confirm")
                        .setCancelText("cancel")
                        .build()
                        .show();
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClickRightCtv() {
        if (StringUtil.isEmpty(tvName)) {
            ToastUtil.show(R.string.Please_enter_the_name);
            return;
        }

        if (StringUtil.isEmpty(tvRemark)) {
            ToastUtil.show(R.string.please_enter_the_remark);
            return;
        }
        if (mReminder != null) {
            mReminder.setName(StringUtil.getEditTextStr(tvName));
            mReminder.setTips(StringUtil.getEditTextStr(tvRemark));
            mReminder.setSoundOrVibrator(ring);
            if (time != null && time.length() > 0) {
                String[] times = time.split(":");
                mReminder.setHour(Integer.parseInt(times[0]));
                mReminder.setMinute(Integer.parseInt(times[1]));
            }
            mReminder.setState(1);
            if (cycle == 0) {
                mReminder.setFlag(0);
            } else if (cycle == 1) {
                mReminder.setFlag(1);
            } else {
                mReminder.setFlag(2);
            }
            mReminder.setWeek(cycle);
            rx.Observable.just(mDatabaseManager.update(mReminder))
                    .compose(RxUtil.applySchedulersJobUI())
                    .subscribe(aBool -> {
                        Utils.setClock(this, mReminder);
                    }, throwable -> {

                    });
        } else {
            mReminder = new Reminder();
            mReminder.setName(StringUtil.getEditTextStr(tvName));
            mReminder.setTips(StringUtil.getEditTextStr(tvRemark));
            mReminder.setSoundOrVibrator(ring);
            if (time != null && time.length() > 0) {
                String[] times = time.split(":");
                mReminder.setHour(Integer.parseInt(times[0]));
                mReminder.setMinute(Integer.parseInt(times[1]));
            }
            mReminder.setState(1);
            if (cycle == 0) {
                mReminder.setFlag(0);
            } else if (cycle == 1) {
                mReminder.setFlag(1);
            } else {
                mReminder.setFlag(2);
            }
            mReminder.setWeek(cycle);
            rx.Observable.just(mDatabaseManager.getAbstractDao().insert(mReminder))
                    .compose(RxUtil.applySchedulersJobUI())
                    .subscribe(aLong -> {
                        mReminder.set_id(aLong);
                        Utils.setClock(this, mReminder);
                    }, throwable -> {

                    });
        }
        finish();
        RxBus.send(new UpdateAlarmEvent());
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mReminder = (Reminder) getIntent().getSerializableExtra(Reminder.class.getSimpleName());
        if (mReminder != null) {
            tvName.setText(mReminder.getName());
            tvRemark.setText(mReminder.getTips());
            date_tv.setText(String.format("%s:%s", Utils.formatDate(mReminder.getHour()), Utils.formatDate(mReminder.getMinute())));
            tv_repeat_value.setText(Utils.parseRepeat(mReminder.getWeek(),1));
            switch (mReminder.getSoundOrVibrator()){
                // 震动
                case 0:
                    tv_ring_value.setText(R.string.vibrate);
                    ring = 0;
                    break;
                // 铃声
                case 1:
                    tv_ring_value.setText(R.string.ring);
                    ring = 1;
                    break;
                case 2:
                    tv_ring_value.setText(R.string.vibration_ring);
                    ring = 2;
                    break;
            }


        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            default:
                break;
        }
    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(Utils.parseRepeat(repeat, 1));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText(R.string.every_day);
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText(R.string.ringing_only_once);
                        cycle = 1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(flag -> {
            switch (flag) {
                // 震动
                case 0:
                    tv_ring_value.setText(R.string.vibrate);
                    ring = 0;
                    break;
                // 铃声
                case 1:
                    tv_ring_value.setText(R.string.ring);
                    ring = 1;
                    break;
                case 2:
                    tv_ring_value.setText(R.string.vibration_ring);
                    ring = 2;
                    break;
                default:
                    break;
            }
        });
    }


}
