package com.example.russel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService extends Service {

    /** IBinder is needed to be a service **/
    private final IBinder binder = new LocalBinder();

    /** socket for sending commands to Russel **/
    private static Socket socket = null;
    private static InputStream inputStream;
    private static OutputStream outputSteam;

    /** called by a component that subscribes to this service, this method is run by the calling component's thread **/
    public class LocalBinder extends Binder {
        NetworkService getService() {
            Log.i(NetworkService.class.toString(), "NetworkService.onBind()");
            return NetworkService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** methods for clients, these methods are run by a thread pool **/
    public void startSocket(String serverIP, int serverPort) {
        try {
            socket = new Socket(serverIP, serverPort);
        } catch (IOException e) {
            Log.e(NetworkService.class.toString(), "Error: "+e);
        }


    }

}