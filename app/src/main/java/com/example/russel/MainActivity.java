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

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    /** default values */
    public static final String CONNECTION_IP = "CONNECTION_IP";
    public static final int RUSSEL_DEFAULT_PORT = 23500;

    /** there's a socket **/
    private static String mRusselIP = null;
    private NetworkService mService;
    boolean mBound = false;

    /**
     * joystick, I borrowed this from an instructable: https://www.instructables.com/A-Simple-Android-UI-Joystick/
     */
    private JoystickView joystick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        joystick = new JoystickView(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
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
     * connect to R.U.S.S.E.L
     * @param v
     */
    public void connectButton(View v){
        EditText defaultIP = findViewById(R.id.defaultIP);
        mRusselIP =  defaultIP.getText().toString();
        if (mBound) {
            mService.startSocket(mRusselIP, RUSSEL_DEFAULT_PORT);
        }
    }

    /**
     * test send, this is just a test and should never be used outside testing
     * @param v
     * @throws JSONException
     */
    public void sendByte(View v) throws JSONException {
        if (mBound) {
//            byte b = 1;
            String s = "ks is test";
            JSONObject json = new JSONObject();
            json.put("Test", s);
            mService.sendString(json.toString());
        }
    }

    /**
     * Send a JSON to R.U.S.S.E.L.
     * @param v this view
     * @param json what RUSSEL needs to know
     * @throws JSONException
     */
    private void sendJSON(View v, JSONObject json) throws JSONException {
        if (mBound) {
            mService.sendString(json.toString());
        }
    }

    /**
     * called by the JoystickView whenever it updates, either from moving or from releasing
     * @param xPercent percentage of L-R movement, on the interval [-1,1]
     * @param yPercent percentage of Up-Down movement, on the interval [-1,1]
     * @param id
     */
    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        // radius is the hypotenuse of X and Y
        float radius = (float) Math.sqrt(Math.pow(xPercent,2)+Math.pow(yPercent,2));

        // theta would be arctan(y/x) but we want to rotate the unit circle by pi/2
        // so A(x,y) rotates pi/2 anticlockwise to become A'(-y,x)
        // so theta is arctan(x,-y)
        float theta = (float) (Math.atan2(xPercent,-yPercent));

        // make
        JSONObject polar = new JSONObject();
        try {
            polar.put("r",radius);
            polar.put("theta", theta);
            mService.sendString(polar.toString());
//            Log.i(MainActivity.class.getName(), polar.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Service Connection binds the Network Service
     **/
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            NetworkService.LocalBinder binder = (NetworkService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}