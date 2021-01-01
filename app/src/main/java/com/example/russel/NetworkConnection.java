package com.example.russel;

import android.location.GnssAntennaInfo;
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

public class NetworkConnection {

    private static final String TAG = NetworkConnection.class.getName();

    private InetAddress ip;
    private int port;

    public NetworkConnection(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public <T> void sendMessage(T message) {
        TCPsend send = new TCPsend(message, ip, port);
        send.start();
    }

}
