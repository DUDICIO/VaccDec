package com.example.tarunkhajuria.vaccdec;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import android.content.Intent;
import com.example.tarunkhajuria.vaccdec.BluetoothService.*;
import android.util.Log;
public class dataview extends AppCompatActivity {
    private BluetoothService mservice;
    private LineGraphSeries<DataPoint> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataview);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);

        Intent btintent=new Intent(this,BluetoothService.class);
        bindService(btintent,myconn,BIND_IMPORTANT);
        new Thread(){
            public void run() {
                int i=0;
                while (true) {
                    i++;
                    series.appendData(new DataPoint(i,10),true,100);
                }
            }
        }.start();


    }
    private ServiceConnection myconn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            btbinder mybind= (btbinder)service;
            mservice=mybind.getService();
            mservice.readData(series);
            Log.d("Event","Hppened");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
