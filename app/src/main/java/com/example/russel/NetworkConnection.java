package com.example.russel;

import android.location.GnssAntennaInfo;
import android.net.wifi.EasyConnectStatusCallback;
import android.telecom.Call;
import android.util.EventLog;
import android.util.Log;

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
import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkConnection {

    private class GetSocket implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket (ip, port);
                Log.i(GetSocket.class.getName(),"got socket");
            } catch (Exception e) {
                Log.i(GetSocket.class.getName(),"Error :" + e.toString());
            }
        }
    }


    private static final String TAG = NetworkConnection.class.getName();
    private final ExecutorService threadPool;

    private InetAddress ip;
    private int port;
    private Socket socket = null;

    public NetworkConnection(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;

        threadPool = Executors.newFixedThreadPool(1);
        threadPool.execute(new GetSocket());
    }

    public <T> void sendMessage(T message) {
        TCPsend send = new TCPsend(message, ip, port);
        send.start();
    }

}
