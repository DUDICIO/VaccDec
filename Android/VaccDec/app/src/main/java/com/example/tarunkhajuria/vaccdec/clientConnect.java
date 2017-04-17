package com.example.tarunkhajuria.vaccdec;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;
import com.example.tarunkhajuria.vaccdec.manageConnectedSocket;
/**
 * Created by tarunkhajuria on 16/04/17.
 */

public class clientConnect extends Thread{
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
    public clientConnect(BluetoothDevice device, BluetoothAdapter btAdapter)
    {
        BluetoothSocket tmp=null;
        mBluetoothAdapter=btAdapter;
        mmDevice=device;
        try{
            tmp=device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
        }catch(Exception e)
        {
            Log.e("Bluetooth","Connection to create Client Socket",e);
        }
        mmSocket=tmp;
    }
    public void run()
    {
        mBluetoothAdapter.cancelDiscovery();
        try{
            mmSocket.connect();

        }catch(IOException connectException) {
            try {
                mmSocket.close();
                Log.e("Bluetooth", "Connection failed", connectException);
            } catch (IOException closeException) {
            }
            return;
        }
        manageConnectedSocket socket=new manageConnectedSocket(mmSocket);
    }
}

