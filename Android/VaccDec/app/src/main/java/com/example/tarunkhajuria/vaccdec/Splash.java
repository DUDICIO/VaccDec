package com.example.tarunkhajuria.vaccdec;

import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.animation.AnimatorSet;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imgview=(ImageView)findViewById(R.id.imageView3);
        imgview.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageView cap=(ImageView)findViewById(R.id.imageView4);
        AnimatorSet set= (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.vaccrotate);
        set.setTarget(cap);
        set.start();
    }



}
