package com.example.tarunkhajuria.vaccdec;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * Created by tarunkhajuria on 16/04/17.
 */

public class manageConnectedSocket {
    BluetoothSocket mmSocket;
    InputStream mmInStream;
    OutputStream mmOutStream;
    private byte[] mmBuffer;
    public void manageConnectedSocket(BluetoothSocket csocket)
    {
        mmSocket=csocket;
        try {
            mmInStream = mmSocket.getInputStream();
        }catch(IOException inputError)
        {
            Log.e("Bluetooth","Input Stream object could not be created",inputError);
    }
        while(true)
        {
            int numbytes=mmInStream.read(mmBuffer);
        }
    }

}
