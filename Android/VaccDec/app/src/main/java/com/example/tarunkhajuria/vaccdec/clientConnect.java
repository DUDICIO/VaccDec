package com.example.tarunkhajuria.vaccdec;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import andorid.bluetooth.Bluetooth
import java.io.IOException;

/**
 * Created by tarunkhajuria on 16/04/17.
 */

public class clientConnect extends Thread{
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    public clientConnect(BluetoothDevice device)
    {
        BluetoothSocket tmp=null;
        mmDevice=device;
        try{
            tmp=device.createRfcommSocketToServiceRecord(MY_UUID);
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
        manageConnectedSocket a(mmSocket);
    }
    }


}
