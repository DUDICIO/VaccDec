package com.example.tarunkhajuria.vaccdec;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.*;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;


public class Main extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==REQUEST_ENABLE_BT) {
            if (requestCode == Activity.RESULT_OK) {
                IntentFilter filter= new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver,filter);
            }
        }
    }
    private final BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName= device.getName();
                String deviceHarwareAdress=device.getAddress();
            }
        }
    }
}
