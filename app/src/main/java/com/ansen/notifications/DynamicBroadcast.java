package com.ansen.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 * @author ansen
 * @create time 2018/3/30
 */
public class DynamicBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        String data = intent.getStringExtra("data");
        Log.i("data",data);
    }
}
