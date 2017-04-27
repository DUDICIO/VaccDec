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
    public void readData(final LineGraphSeries series)
    {
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

                        int numBytes=mstream.read(mbuffer);
                        if (mbuffer != null) {
                            String a=new String(mbuffer,"UTF-8");
                            Log.d("BeforeData",""+a);
                            setdata(a);
                        }
                    } catch (IOException e) {
                        Log.d("Bluetooth", "" + e);
                    }
                }
            }
            void setdata(String data)
            {   float temp=-30,time=-30,prevtime=-30,prevtemp=-30;
                boolean halt=false;
                for(int i=0;i<data.length();i++)
                {
                    switch (data.charAt(i))
                    {
                        case 'C':temp=getnum(data,i+1);
                                break;

                        case 't':time=getnum(data,i+1);
                                if(temp!=-30) {
                                    halt = true;
                                }
                                break;
                        default:break;
                    }
                    if(halt)
                    {
                        break;
                    }
                }
                if(time!=-30 && temp!=-30) {
                    Log.d("Data","temp:"+temp+"time:"+time);
                    if(time>prevtime) {
                        series.appendData(new DataPoint(time, temp), true, 20);
                        prevtime = time;
                        prevtemp = temp;
                    }
                }
            }
            float getnum(String data,int i)
            {
                String num="";
                for(;data.charAt(i)!='\t' && data.charAt(i)!='\n';i++)
                {
                    if(data.charAt(i)!='t'&&data.charAt(i)!='C')
                        num+=data.charAt(i);
                }
                if(num=="")
                    return -30;
                else
                    return Float.parseFloat(num);
            }

        }.start();


    }


    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }
}
