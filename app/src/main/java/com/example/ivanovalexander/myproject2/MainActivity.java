package com.example.ivanovalexander.myproject2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Snackbar mSnackbar;
    private EditText mPhoneEditText;
    private EditText mCodewordEditText;
    private Switch mSwitchSmsService;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PHONE = "Phone";
    public static final String APP_PREFERENCES_CODEWORD = "Codeword";
    public static final String APP_PREFERENCES_SMS_SERVICE = "Sms_service";

    SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhoneEditText = (EditText) findViewById(R.id.id_edit_phone_number);
        mCodewordEditText = (EditText) findViewById(R.id.id_edit_codeword);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_PHONE)) {
            mPhoneEditText.setText(mSettings.getString(APP_PREFERENCES_PHONE, ""));
        }
        if (mSettings.contains(APP_PREFERENCES_CODEWORD)) {
            mCodewordEditText.setText(mSettings.getString(APP_PREFERENCES_CODEWORD, "#find"));
        }

        mSwitchSmsService = (Switch) findViewById(R.id.id_switch_service);
        if (mSwitchSmsService != null) {
            mSwitchSmsService.setOnCheckedChangeListener(this);
        }
        LoadPrefereces_Switch(APP_PREFERENCES_SMS_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSnackbar = Snackbar.make(view, "Благодарим за использование нашей программы!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("ОК!", snackbarOnClickListener).setActionTextColor(Color.BLUE);
                View snackbarView = mSnackbar.getView();
                snackbarView.setBackgroundColor(Color.argb(255, 76, 175, 80));
                TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                snackTextView.setTextColor(Color.BLACK);
                mSnackbar.show();
            }
        });


    }

    View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private void SavePreferences_Switch(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void LoadPrefereces_Switch(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        boolean savedSwitch = sharedPreferences.getBoolean(key, false);
        if (savedSwitch)
            mSwitchSmsService.setText(R.string.Switch_Sms_Service_On);
        else
            mSwitchSmsService.setText(R.string.Switch_Sms_Service_Off);
        mSwitchSmsService.setChecked(savedSwitch);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickSave(View view) {
        String strPhone = mPhoneEditText.getText().toString();
        String strCode = mCodewordEditText.getText().toString();

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_PHONE, strPhone);
        editor.putString(APP_PREFERENCES_CODEWORD, strCode);
        editor.apply();
        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
        {
            mSwitchSmsService.setText(R.string.Switch_Sms_Service_On);
        //    startService(new Intent(MainActivity.this, SmsService.class));
        }
        else
        {
            mSwitchSmsService.setText(R.string.Switch_Sms_Service_Off);
         //   stopService(new Intent(MainActivity.this, SmsService.class));
        }
        SavePreferences_Switch(APP_PREFERENCES_SMS_SERVICE, isChecked);
    }


}
