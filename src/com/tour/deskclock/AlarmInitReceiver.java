/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tour.deskclock;

import java.util.List;

import com.tour.SQLite.TourData;
import com.tour.util.TTLog;
import com.tour.view.UpdateDialog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class AlarmInitReceiver extends BroadcastReceiver {

    /**
     * Sets alarm on ACTION_BOOT_COMPLETED.  Resets alarm on
     * TIME_SET, TIMEZONE_CHANGED
     * 接受开机启动完成的广播，
     * 设置闹钟，当时区改变也设置
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.v("wangxianming", "AlarmInitReceiver" + action);

//         Remove the snooze alarm after a boot.
//        if (action!=null&&action.equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Alarms.saveSnoozeAlert(context, -1, -1);
//        }
        int c=intent.getIntExtra("id", -1);
        TTLog.s("c====="+c);
        if(c!=-1){
        	Intent ints=new Intent(context,AlarmTipActivity.class);
            ints.putExtra("id", c);
            ints.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ints);
//            if(c==1000){
            	Alarms.disableExpiredAlarms(context,c);
//            	TourData.insterAlarm(context,c);
            }
         else{
        	 Alarms.disableExpiredAlarms(context);
             Alarms.setNextAlert(context);
        }
        
//        Alarms.disableExpiredAlarms(context);
//        Alarms.setNextAlert(context);
//        Toast.makeText(context, "闹钟响起", Toast.LENGTH_SHORT).show();
   
    }
}
