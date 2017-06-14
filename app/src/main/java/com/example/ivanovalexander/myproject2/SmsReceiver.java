package com.example.ivanovalexander.myproject2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import android.widget.Toast;

/**
 * Created by Ivanov Alexander on 08.11.2015.
 */
public class SmsReceiver extends BroadcastReceiver {

    final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String mPhoneNumber;
    String mCodeWord;


    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PHONE = "Phone";
    public static final String APP_PREFERENCES_CODEWORD = "Codeword";


    SharedPreferences mSettings;

    @Override
    public void onReceive(Context context, Intent intent) {

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_PHONE)) {
            mPhoneNumber = mSettings.getString(APP_PREFERENCES_PHONE, "");
        }
        if (mSettings.contains(APP_PREFERENCES_CODEWORD)) {
            mCodeWord = mSettings.getString(APP_PREFERENCES_CODEWORD, "");
        }


        if ( intent.getAction().equals(SMS_RECEIVED)) {
            SmsManager sms = SmsManager.getDefault();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0 ; i < pdus.length; i++)
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                for (SmsMessage message : messages) {
                    String msg = message.getMessageBody();
                    String address = message.getOriginatingAddress();

                    if (address.compareTo(mPhoneNumber) == 0 && msg.compareTo(mCodeWord) == 0) {

                        Intent mIntent = new Intent(context, SmsService.class);
                        mIntent.putExtra("msg", msg);
                        mIntent.putExtra("address", address);
                        context.startService(mIntent);
                       // abortBroadcast();
                    }

                   // requestReceived(msg, address);

                }


            }
        }
    }






}
