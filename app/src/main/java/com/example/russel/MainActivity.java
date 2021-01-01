package com.example.russel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.net.InetAddress.getByName;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button btnConnect;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private String url = "https://ionizing-radar.ca/russel.ip";
    private String returnString = "192.168.0.0";

    private InetAddress russelIP = null;
    private final int russelPORT = 23500;
    private NetworkConnection commandChannel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView connectTextView = (TextView) findViewById(R.id.defaultIP);

        try {
            russelIP = Inet4Address.getByName(getRusselIP());
            connectTextView.setText(russelIP.toString());
        } catch (UnknownHostException error) {
            Log.i(TAG,"Error :" + error.toString());
            russelIP = null;
        }

        // initialise connect Button action
        btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    commandChannel = new NetworkConnection(new Socket(russelIP, russelPORT));
                } catch (IOException error) {
                    Log.i(TAG,"Error :" + error.toString());
                }
            }
        }
        );
    }

    /**
     * @return  String  RUSSEL's IPv4 address
     */
    protected String getRusselIP() {
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextView connectTextView = (TextView) findViewById(R.id.defaultIP);
                connectTextView.setText(response);
                returnString = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
                }
            }
        );

        mRequestQueue.add(mStringRequest);
        return returnString;
    }
}