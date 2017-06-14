package com.example.ivanovalexander.myproject2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ivanov Alexander on 08.11.2015.
 */
public class BootBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      //  context.startService(new Intent(context, SmsService.class));
    }
}
