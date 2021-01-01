package com.example.russel;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkConnection {

    private static final String TAG = MainActivity.class.getName();

    private Socket clientSocket;
    private PrintWriter write;
    private BufferedReader read;

    public NetworkConnection(Socket socket){
        clientSocket = socket;
        try {
            write = new PrintWriter(clientSocket.getOutputStream(), true);
            read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException error) {
            Log.i(TAG,"Error :" + error.toString());
        }
    }

    public <T> T sendMessage(String message) {
        try {
            write.println(message);
            String response = read.readLine();
            return (T) response;
        } catch (IOException error) {
            Log.i(TAG,"Error :" + error.toString());
            return null;
        }
    }

}
