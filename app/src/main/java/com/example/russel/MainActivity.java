package com.example.russel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button btnConnect;

    private InetAddress russelIP = null;
    private final int russelPORT = 23500;
    private NetworkConnection networkConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText defaultIP = (EditText) findViewById(R.id.defaultIP);

        // initialise connect Button action
        btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    Log.i(TAG,"let's try a new connection to "+defaultIP.getText().toString());
                    networkConnection = new NetworkConnection(defaultIP.getText().toString());
                } catch (Exception error) {
                    Log.i(TAG,"Error :" + error.toString());
                }
            }
        }
        );
    }


}