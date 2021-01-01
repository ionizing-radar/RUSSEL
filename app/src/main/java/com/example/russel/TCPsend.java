package com.example.russel;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPsend extends Thread {

    private static final String TAG = TCPsend.class.getName();

    private InetAddress ip;
    private int port;
    private Object message;
    private Object response = null;


    public TCPsend(Object message, InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            PrintWriter write = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            write.println(message);
            response = read.readLine();
            write.close();
            read.close();
            socket.close();
        } catch (Exception e) {
            Log.i(TAG,"Error :" + e.toString());
        }
    }

    public Object getResponse() { return response; }
}