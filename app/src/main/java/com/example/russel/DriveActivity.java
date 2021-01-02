package com.example.russel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetAddress;

public class DriveActivity extends AppCompatActivity {


    private NetworkConnection networkConnection = null;
    private InetAddress russelIP = null;
    private final int russelPORT = 23500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        Intent intent = getIntent();
        String defaultIP = intent.getStringExtra(MainActivity.CONNECTION_IP);

        Log.i(DriveActivity.class.getName(),"new Drive Activity");

        try {
            networkConnection = new NetworkConnection(defaultIP);
        } catch (IOException error) {
            Log.i(DriveActivity.class.getName(),"Error :" + error.toString());
        }
    }

    public void sendButton (View v) {

        Log.i(DriveActivity.class.getName(),"send button pushed!" );

        EditText command = findViewById(R.id.commandText);
        networkConnection.send(command.toString().getBytes());
    }
}