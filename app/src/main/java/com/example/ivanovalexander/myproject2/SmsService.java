package com.example.ivanovalexander.myproject2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsService extends Service {

    final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String mPhoneNumber;
    String mCodeWord;

    String latitude;
    String longitude;

    Criteria criteria = new Criteria();
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;


    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PHONE = "Phone";
    public static final String APP_PREFERENCES_CODEWORD = "Codeword";
    public static final String APP_PREFERENCES_SMS_SERVICE = "Sms_service";

    SharedPreferences mSettings;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        //super.onCreate();


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (!mSettings.getBoolean(APP_PREFERENCES_SMS_SERVICE, false)) {

            stopSelf();
            return Service.START_STICKY;
        }


        String msg = intent.getExtras().getString("msg");
        String address = intent.getExtras().getString("address");

        if (mSettings.contains(APP_PREFERENCES_PHONE)) {
            mPhoneNumber = mSettings.getString(APP_PREFERENCES_PHONE, "");

        }

        if (mSettings.contains(APP_PREFERENCES_CODEWORD)) {
            mCodeWord = mSettings.getString(APP_PREFERENCES_CODEWORD, "");


        }


        requestReceived(msg, address);

        return  Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    private void requestReceived(String msg, String address) {
        Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
        if (address.compareTo(mPhoneNumber) == 0) {

            if (msg.compareTo(mCodeWord) == 0) {

                SmsManager sms = SmsManager.getDefault();
                StringBuilder sb = new StringBuilder();

                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(true);
                criteria.setBearingRequired(true);
                criteria.setSpeedRequired(true);
                criteria.setCostAllowed(true);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
                criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
                criteria.setSpeedAccuracy(Criteria.ACCURACY_LOW);

                mLocationListener = new MyLocationListener();

                Looper looper = null;

                mLocationManager.requestSingleUpdate(criteria, mLocationListener, looper);




            }
            else {


                stopSelf();
            }

        } else{

            stopSelf();
        }



    }


    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = "Новое местоположение Долгота: " +
                    location.getLongitude() + " Широта: " + location.getLatitude();
            Toast.makeText(SmsService.this, message, Toast.LENGTH_LONG)
                    .show();
            showCurrentLocation(location);
        }

        public void onStatusChanged(String s, int i, Bundle b) {
         //   Toast.makeText(GpsTestActivity.this, "Статус провайдера изменился",
        //            Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
       //     Toast.makeText(GpsTestActivity.this,
        //            "Провайдер заблокирован пользователем. GPS выключен",
        //            Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
         //   Toast.makeText(GpsTestActivity.this,
          //          "Провайдер включен пользователем. GPS включён",
          //          Toast.LENGTH_LONG).show();
        }
    }


    protected void showCurrentLocation(Location location) {

        //   Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            SmsManager sms = SmsManager.getDefault();
            StringBuilder sb = new StringBuilder();

            String provider = mLocationManager.getBestProvider(criteria, false);

            sb.append("Latitude: ").append(latitude).append("\n");
            sb.append("Longitude: ").append(longitude).append("\n");
            sb.append(provider);
            sms.sendTextMessage(mPhoneNumber, null, sb.toString(), null, null);

            stopSelf();
        }

    }

}
