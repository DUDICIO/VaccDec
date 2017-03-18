package com.example.tarunkhajuria.vaccdec;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

import java.io.IOException;
import java.util.jar.Attributes;
import android.util.Log;
import android.bluetooth.BluetoothSocket;
/**
 * Created by tarunkhajuria on 18/03/17.
 */

public class bluetoothserver extends Thread {
    private BluetoothServerSocket mmServerSocket;
    public void AcceptThread(BluetoothAdapter mBluetoothAdapter){
        BluetoothServerSocket tmp=null;
        try{
            tmp=mBluetoothAdapter.listenUsingRFcommWithServiceRecord();

        }catch (IOException e)
        {
            Log.e("Bluetooth","Listner could not be made",e);
        }
        mmServerSocket=tmp;
    }

    public void run()
    {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageclients(socket);
                mmServerSocket.close();
                break;
            }
        }

    }

    public void cancel()
    {
        try {
            mmServerSocket.close();

        }catch(IOException e)
        {
            Log.e("Bluetooth","Closing socket failed",e);
        }
    }

}
