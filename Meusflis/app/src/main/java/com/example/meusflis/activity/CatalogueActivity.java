package com.example.meusflis.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.meusflis.R;
import com.example.meusflis.database.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogueActivity extends MenuActivity{
    ImageSlider isTopMultimediaContent;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        initializeViews();
        loadTopContentCovers();
        databaseHelper = new DatabaseHelper(this);
    }

    private void initializeViews() {
        isTopMultimediaContent = findViewById(R.id.isTopMultimediaContent);
    }

    private void loadTopContentCovers() {
        databaseHelper = new DatabaseHelper(this);
        List<byte[]> topContentCovers = databaseHelper.getTopLikedContentCovers(5);

        if (topContentCovers == null) {
            topContentCovers = new ArrayList<>();
        }

        ArrayList<SlideModel> slideModels = new ArrayList<>();

        for (byte[] coverImage : topContentCovers) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(coverImage, 0, coverImage.length);
            Uri imageUri = saveImageToCache(bitmap);
            if (imageUri != null) {
                slideModels.add(new SlideModel(imageUri.toString(), ScaleTypes.FIT));
            }
        }
        isTopMultimediaContent.setImageList(slideModels, ScaleTypes.FIT);
    }

    private Uri saveImageToCache(Bitmap bitmap) {
        try {
            File cacheDir = getCacheDir();
            File tempFile = File.createTempFile("cover", ".png", cacheDir);
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return Uri.fromFile(tempFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
