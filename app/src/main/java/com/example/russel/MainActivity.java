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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    public static final String CONNECTION_IP = "CONNECTION_IP";

    private static NetworkService mNetworkConnection = null;
    private static String mRusselIP = null;
    private static final int mRusselPORT = 23500;

    NetworkService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        }

    @Override
    protected void onStart() {
        Log.i(MainActivity.class.getName(), "starting Drive Activity");
        super.onStart();

        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        {
            if (mService == null) {
                Log.e(MainActivity.class.getName(), "Network Service is not bound");
            } else {
                mService.startSocket(intent.getStringExtra(MainActivity.CONNECTION_IP), mRusselPORT);
            }
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
            mService.startSocket(mRusselIP, mRusselPORT);
        }
    }

}