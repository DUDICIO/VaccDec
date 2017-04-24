package com.example.tarunkhajuria.vaccdec;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
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

public class Main extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> devicelist= new ArrayList<BluetoothDevice>();
    private ArrayAdapter<String> madapter;
    private BluetoothDevice selectedDevice;
    private TextView sname;
    private TextView connect;
    private ImageView connImg;
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
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            if (!mBluetoothAdapter.isEnabled()) {
            }
            else{
                mBluetoothAdapter.startDiscovery();
                Log.d("Bluetooth","Start Discovery");
            }
        }
        //Bluetooth Connect
        connImg=(ImageView)findViewById(R.id.vaccdecimg);
        connImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     msocket = selectedDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                }
                catch(Exception e)
                {
                    Log.e("Bluetooth","Socket Not given:"+e);
                }
                try{
                    msocket.connect();
                }catch(IOException e)
                {
                    Log.e("Bluetooth","Could not connect with error:"+e);
                }
                Intent stacitivty=new Intent(activitycontext,BluetoothService.class);
                


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
                Log.d("Bluetooth","Here");
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Bluetooth","Start Discovery");
                mBluetoothAdapter.startDiscovery();


            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceHarwareAdress = device.getAddress();
                String deviceName = device.getName();
                Log.d("Bluetooth",deviceName);
                madapter.add(deviceName);
                devicelist.add(device);
            }
        }
    };

}