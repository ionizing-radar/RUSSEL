package com.example.russel;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkConnection {

    private static final String TAG = NetworkConnection.class.getName();
    private final ExecutorService threadPool;
    private PropertyChangeSupport change = new PropertyChangeSupport(this);

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private String serverIP = null;
    private static final int serverPort = 23500;
    private Socket socket = null;

    private ByteArrayInputStream inputStream = null;
    private ByteArrayOutputStream outputStream = null;

    public NetworkConnection(String serverIP) throws UnknownHostException {
        this.serverIP = serverIP;

        threadPool = Executors.newFixedThreadPool(1);

        connect(serverIP, serverPort);
    }

    public void connect(String serverIP, int serverPort) {
        threadPool.execute(() -> {
            try {
                socket = new Socket(serverIP, serverPort);
                socket.bind(null);
                inputStream =  (ByteArrayInputStream) socket.getInputStream();
                outputStream = (ByteArrayOutputStream) socket.getOutputStream();

            } catch (IOException e) {
                Log.i(NetworkConnection.class.getName(), "Error :" + e.toString());
            }
        });
    }

    public void send(byte[] message) {
        byte[] response = new byte[0];
        threadPool.execute( () -> {
            try {
                if (socket == null) { connect(serverIP, serverPort); }
                outputStream.write(message);
            } catch (IOException e) {
                Log.i(NetworkConnection.class.getName(), "Error :" + e.toString());
            }

        });
    }

}
