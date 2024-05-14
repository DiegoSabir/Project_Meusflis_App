package com.example.meusflis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginAnimation extends AppCompatActivity {
    private static int DELAY_TIME = 4000;
    Animation topAnimation;
    ImageView ivAnimationLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        ivAnimationLogo = findViewById(R.id.ivAnimationLogo);
        ivAnimationLogo.setAnimation(topAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoginAnimation.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, DELAY_TIME);
    }
}
