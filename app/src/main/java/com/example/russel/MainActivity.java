package com.example.russel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    public static final String CONNECTION_IP = "CONNECTION_IP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        }

    public void connectButton(View v){
        Intent intent = new Intent(this, DriveActivity.class);
        EditText defaultIP = findViewById(R.id.defaultIP);
        String connectIP = defaultIP.getText().toString();
        intent.putExtra(CONNECTION_IP, connectIP);
        startActivity(intent);
    }
}