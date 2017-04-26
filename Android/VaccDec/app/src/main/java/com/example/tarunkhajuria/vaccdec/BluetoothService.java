package com.example.tarunkhajuria.vaccdec;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.io.IOException;
import java.util.UUID;
import android.os.Message;
import android.os.Handler;
import java.io.InputStream;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
public class BluetoothService extends Service {
    private final IBinder mbinder=new btbinder();
    private BluetoothSocket msocket;
    private InputStream mstream;
    private Handler mHandler= new Handler();
    private byte[] mbuffer = new byte[1024];
    public LineGraphSeries mseries;
    public BluetoothService() {
    }
    public class btbinder extends Binder
    {
        public BluetoothService getService()
        {
            return BluetoothService.this;
        }
    }
    public int connectBt(BluetoothDevice btdevice)
    {
        try {
            msocket = btdevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        }
        catch(Exception e)
        {
            Log.e("Bluetooth","Socket Not given:"+e);
            return -1;
        }
        try{
            msocket.connect();
        }catch(IOException e)
        {
            Log.e("Bluetooth","Could not connect with error:"+e);
            return -1;
        }
        return 1;

    }
    public void readData(LineGraphSeries series)
    {
        mseries=series;
     try{
         Log.d("BluetoothisConnected",""+msocket.isConnected());
         mstream=msocket.getInputStream();
     }catch (Exception e)
     {
         Log.e("Bluetooth","Error:"+e);
     }
        new Thread(){
            public void run() {
                while (true) {
                    try {

                        Log.d("mseries",""+mseries.getHighestValueX());
                        int numBytes=mstream.read(mbuffer);
                        if (mbuffer != null) {
                            String a=new String(mbuffer,"UTF-8");
                        }
                    } catch (IOException e) {
                        Log.d("Bluetooth", "" + e);
                    }
                }
            }
        }.start();


    }


    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }
}
