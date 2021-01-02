package com.example.russel;

import android.location.GnssAntennaInfo;
import android.net.wifi.EasyConnectStatusCallback;
import android.telecom.Call;
import android.util.EventLog;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkConnection {

    private class SocketSet implements Runnable {

        private String serverIP;
        private int serverPort;

        public SocketSet(String serverIP, int serverPort) { this.serverIP = serverIP; this.serverPort = serverPort; }

        @Override
        public void run() {
            Log.i(SocketSet.class.getName(),"SocketSet run()");
            try {
                Log.i(SocketSet.class.getName(),"setting socket"+serverIP+serverPort);
                socket = new Socket (serverIP, serverPort);
                Log.i(SocketSet.class.getName(),"got socket");
            } catch (Exception e) {
                Log.i(SocketSet.class.getName(),"Error :" + e.toString());
            }
            Log.i(SocketSet.class.getName(),"done!");
        }
    }


    private static final String TAG = NetworkConnection.class.getName();
    private final ExecutorService threadPool;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private InetAddress ip =  null;
    private static final int defaultPort = 23500;
    private Socket socket = null;

//    private String url = "https://ionizing-radar.ca/russel.ip";

    public NetworkConnection(String serverIP) throws UnknownHostException {
        threadPool = Executors.newFixedThreadPool(1);

        threadPool.execute(new SocketSet(serverIP, defaultPort));



    }

}
