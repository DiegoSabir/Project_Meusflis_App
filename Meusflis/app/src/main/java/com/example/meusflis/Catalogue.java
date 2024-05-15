package com.example.meusflis;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class Catalogue extends AppCompatActivity {
    ViewFlipper vfCarousel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        int images[] = {R.drawable.frieren, R.drawable.the_apothecary_diaries, R.drawable.my_favourite_idol};

        vfCarousel = findViewById(R.id.vfCarousel);

        for(int image: images){
            flipperImages(image);
        }
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        vfCarousel.addView(imageView);
        vfCarousel.setFlipInterval(3000);
        vfCarousel.setAutoStart(true);
        vfCarousel.setInAnimation(this, android.R.anim.slide_out_right);
        vfCarousel.setOutAnimation(this, android.R.anim.slide_in_left);
    }
}
