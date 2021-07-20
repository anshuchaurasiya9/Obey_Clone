package com.example.forexperiment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class splash_screen extends AppCompatActivity {
    //Hooks
    ImageView a;
    TextView slogan;
    ProgressBar Bar;
    LottieAnimationView animationView;
    //Animations
    Animation topAnimation,bottomAnimation,middleAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        a = findViewById(R.id.a);
        slogan = findViewById(R.id.tagLine);
        Bar = findViewById(R.id.Bar);
        animationView = findViewById(R.id.animationView);

        //Animation Calls
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middile_animation);

        //-----------Setting Animations to the elements of Splash
        a.setAnimation(topAnimation);
        slogan.setAnimation(bottomAnimation);
        animationView.setAnimation(middleAnimation);

        //Splash Screen Code to call new Activity after some time
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(splash_screen.this, signup.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }



}