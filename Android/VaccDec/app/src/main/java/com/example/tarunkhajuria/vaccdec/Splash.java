package com.example.tarunkhajuria.vaccdec;

import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import android.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView cap=(ImageView)findViewById(R.id.imageView4);
        AnimatorSet set= (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.vaccrotate);
        set.setTarget(cap);
        set.start();
        new Thread(){
            public void run(){
                Intent i=new Intent(Splash.this,Main.class);
                try {
                    this.sleep(3000);

                }catch (Exception e)
                {
                    Log.e("Splash","Delay caused exception");
                }
                startActivity(i);
            }
        }.start();
    }
}
