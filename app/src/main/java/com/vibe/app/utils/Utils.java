package com.vibe.app.utils;

import android.content.Context;
import android.widget.Toast;

import com.vibe.app.alarmmanager.clock.AlarmManagerUtil;
import com.vibe.app.model.Reminder;

/**
 * @author linguoding
 * @Package com.vibe.app.utils
 * @作 用:
 * @邮箱：linggoudingg@gmail.com
 * @日 期: 2017年12月29日  10:46
 */


public class Utils {
    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

    public static void setClock(Context context, Reminder reminder) {
        if (reminder.getState() == 1) {
            if (reminder.getFlag() == 2) {
                String weeksStr = Utils.parseRepeat(reminder.getWeek(), 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(context, 2, reminder.getHour(), reminder.getMinute(),
                            ((reminder.get_id().intValue() * 10) + i), Integer.parseInt(weeks[i]), reminder.getTips(), reminder.getSoundOrVibrator());
                }
            } else {
                AlarmManagerUtil.setAlarm(context, reminder.getFlag(), reminder.getHour(), reminder.getMinute(),
                        reminder.get_id().intValue(), 0, reminder.getTips(), reminder.getSoundOrVibrator());
            }
            Toast.makeText(context, "The alarm clock is set up successfully", Toast.LENGTH_LONG).show();
        } else {
            if (reminder.getFlag() == 2) {
                String weeksStr = Utils.parseRepeat(reminder.getWeek(), 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.cancelAlarm(context, AlarmManagerUtil.ALARM_ACTION, ((reminder.get_id().intValue() * 10) + i));
                }
            } else {
                AlarmManagerUtil.cancelAlarm(context, AlarmManagerUtil.ALARM_ACTION, reminder.get_id().intValue());
            }
            Toast.makeText(context, "Cancel the alarm clock", Toast.LENGTH_LONG).show();
        }


    }

    public static String formatDate(int time){
        if(time<9){
            return "0"+time;
        }else{
            return time+"";
        }
    }


}
