package com.kashsoftwares.dashboardsplashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_SCREEN = 3000;

    private Animation topAnimation;
    private Animation bottomAnimation;
    private ImageView imageView;
    private TextView logoTxt;
    private TextView sloganTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //// Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

      topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
      bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

      imageView = findViewById(R.id.imageView);
      logoTxt = findViewById(R.id.logoTxt);
      sloganTxt = findViewById(R.id.sloganTxt);

      // set Animation to ui controls
      imageView.setAnimation(topAnimation);
      logoTxt.setAnimation(bottomAnimation);
      sloganTxt.setAnimation(bottomAnimation);


      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {

              Intent intent = new Intent(MainActivity.this,Login.class);
              Pair[] pair = new Pair[2];
              pair[0] = new Pair<View, String>(imageView, "logo_img_tran");
              pair[1] = new Pair<View, String>(logoTxt, "logo_tran");

              ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pair);

              startActivity(intent,options.toBundle());

          }
      }, SPLASH_SCREEN);


    }
}
