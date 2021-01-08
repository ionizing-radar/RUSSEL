package com.example.russel;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.IntBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService extends Service {

    /** IBinder is needed to be a service **/
    private final IBinder binder = new LocalBinder();

    /** Executor runs threads **/
    private Executor mExecutor;

    /** socket data **/
    private String serverIP;
    private int serverPort;

    /** socket for sending commands to Russel **/
    private static Socket socket;
    private static InputStreamReader inputStream;
    private static DataOutputStream outputSteam;


    @Override
    public void onCreate() {
        // start the executor thread pool, is  that a thing we need?
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** called by a component that subscribes to this service, this method is run by the calling component's thread **/
    public class LocalBinder extends Binder {
        NetworkService getService() {
            Log.i(NetworkService.class.toString(), "NetworkService.onBind()");
            return NetworkService.this;
        }
    }
    /** methods for clients, I want these methods are run by a thread pool **/

    /**
     * startSocket opens a socket to a remote server
     * @param serverIP String IPv4 address of server
     * @param serverPort int IPv4 port of server, should be 23500 by default
     */
    public void startSocket(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        Runnable task = () -> {
            try {
                socket = new Socket(serverIP, serverPort);
                outputSteam = new DataOutputStream(socket.getOutputStream());
                inputStream = new InputStreamReader(socket.getInputStream());
                Log.i(NetworkService.class.toString(), "connected!");
            } catch (IOException e) {
                Log.e(NetworkService.class.toString(), "Error: "+e);
            }
        };
    mExecutor.execute(task);
    }

    /**
     * sendByte takes a byte and sends it to the open socket
     * @param s
     */
    public byte[] sendString(String s) {
        Runnable task = () -> {
            Log.i(NetworkService.class.toString(), "sending a byte");

            if (!socket.isConnected()) {
                Log.e(NetworkService.class.toString(), "socket not connected, trying again");
                startSocket(serverIP, serverPort);
            }
            try {
                Log.i(NetworkService.class.toString(), "sending '"+s+"' on socket");
                outputSteam.write(s.getBytes(), 0 , s.getBytes().length);
                String response = "";
                while(inputStream.ready()) {
                    byte b = (byte) inputStream.read();
                    response += Byte.toString(b);
                }
                if (response == "") {
                    Log.i(NetworkService.class.toString(), "no response, all done");
                } else {
                    Log.i(NetworkService.class.toString(), response.toString());
                }
//                if (inputStream.ready()) {
//                    Log.i(NetworkService.class.toString(), "reading inputStream bytes");
//                    inputStream.read(response, 0, inputStream.available());
//                    Log.i(NetworkService.class.toString(), response.toString());
//                } else {
//                    Log.i(NetworkService.class.toString(), "no response, all done");
//                }
            } catch (IOException e) {
                Log.e(NetworkService.class.toString(), "Error: " + e);
            }
        };
        mExecutor.execute(task);
        return null;
    }



}


