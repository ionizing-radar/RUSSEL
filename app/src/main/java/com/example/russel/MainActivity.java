package com.example.russel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /** default values */
    public static final String CONNECTION_IP = "CONNECTION_IP";
    public static final int RUSSEL_DEFAULT_PORT = 23500;

    /** there's a socket **/
    private static String mRusselIP = null;
    private NetworkService mService;
    boolean mBound = false;

    /** joystick, I borrowed this from an instructable: https://www.instructables.com/A-Simple-Android-UI-Joystick/ **/
//    JoystickView joystick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JoystickView joystick = new JoystickView(this);
        setContentView(R.layout.activity_main);
//        setContentView(joystick);
    }

    @Override
    protected void onStart() {
        Log.i(MainActivity.class.getName(), "starting Drive Activity");
        super.onStart();

        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (mService == null) {
            Log.e(MainActivity.class.getName(), "Network Service is not bound");
        } else {
            mService.startSocket(intent.getStringExtra(MainActivity.CONNECTION_IP), RUSSEL_DEFAULT_PORT);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    /**
     * Service Connection binds the Network Service
     **/
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(ServiceConnection.class.getName(), "ServiceConnection onServiceConnected");
            NetworkService.LocalBinder binder = (NetworkService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void connectButton(View v){
        EditText defaultIP = findViewById(R.id.defaultIP);
        mRusselIP =  defaultIP.getText().toString();
        if (mBound) {
            mService.startSocket(mRusselIP, RUSSEL_DEFAULT_PORT);
        }
    }

    public void sendByte(View v) throws JSONException {
        if (mBound) {
            byte b = 1;
            String s = "ks is test";
            JSONObject json = new JSONObject();
            json.put("Test", s);
            mService.sendString(json.toString());
        }
    }

}