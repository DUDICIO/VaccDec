package com.example.tarunkhajuria.vaccdec;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.Binder;

import com.example.tarunkhajuria.vaccdec.BluetoothService.*;

public class Main extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> devicelist= new ArrayList<BluetoothDevice>();
    private ArrayAdapter<String> madapter;
    private BluetoothDevice selectedDevice;
    private TextView sname;
    private TextView connect;
    private ImageView connImg;
    private BluetoothService mservice;

    BluetoothSocket msocket;
    private Context activitycontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activitycontext=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //List view for Bluetooth Devices
        ListView btlist= (ListView) findViewById(R.id.devicesltview);

        madapter= new ArrayAdapter<String>(this,R.layout.text);
        btlist.setAdapter(madapter);
        btlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDevice=devicelist.get(position);
                sname.setText(selectedDevice.getName());
                connect.setVisibility(TextView.VISIBLE);
            }
        });

        //Selected Device Handling
        sname= (TextView) findViewById(R.id.sDevice);
        connect=(TextView)findViewById(R.id.connect);


        //Bluetooth Search
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                mBluetoothAdapter.startDiscovery();
            }
        }


        //Bluetooth Connect
        connImg=(ImageView)findViewById(R.id.vaccdecimg);
        connImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bservice=new Intent(activitycontext,BluetoothService.class);
                bindService(bservice,btconnection,BIND_AUTO_CREATE);
                try{
                    mservice.connectBt(selectedDevice);
                }
                catch(Exception e)
                {
                    Log.e("Bluetooth",""+e);
                }
                Log.d("Bluetooth",""+mservice);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Bluetooth","Start Discovery");
                mBluetoothAdapter.startDiscovery();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        if(mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }
    }
    ServiceConnection btconnection=new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            btbinder lbinder=(btbinder) service;
            mservice=(BluetoothService)lbinder.getService();
            mBluetoothAdapter.cancelDiscovery();
            if(mservice.connectBt(selectedDevice)==1)
            {

                Intent nactivity=new Intent(activitycontext,dataview.class);
                startActivity(nactivity);
            }
        }
    };


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device!=null) {
                    String deviceName = device.getName();
                    Log.d("Bluetooth", "" + deviceName);
                    madapter.add(deviceName);
                    devicelist.add(device);
                }
            }
        }
    };

}