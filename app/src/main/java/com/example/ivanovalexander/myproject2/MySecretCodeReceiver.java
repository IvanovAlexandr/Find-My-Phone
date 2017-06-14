package com.example.ivanovalexander.myproject2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ivanov Alexander on 11.11.2015.
 */
public class MySecretCodeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
            Intent myintent = new Intent(context, MainActivity.class);
            myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myintent);
        }
    }
}
