package com.sabir.meusflis.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.Adapters.FeaturedAdapter;
import com.sabir.meusflis.Models.DataModel;
import com.sabir.meusflis.Models.FeatureModel;
import com.sabir.meusflis.R;
import com.sabir.meusflis.Adapters.SeriesAdapter;
import com.sabir.meusflis.Models.SeriesModel;
import com.sabir.meusflis.Adapters.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference();

    private List<DataModel> dataModels;
    private List<FeatureModel> featureModels;
    private List<SeriesModel> seriesModels;

    private SliderAdapter sliderAdapter;

    private RecyclerView featuredRecyclerView, webSeriesRecyclerView;

    private FeaturedAdapter featuredAdapter;
    private SeriesAdapter seriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meusflis");

        FirebaseApp.initializeApp(this);

        SliderView sliderView = findViewById(R.id.sliderView);

        sliderAdapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(6);
        renewItems(sliderView);

        //Load data from firebase
        loadFirebaseForSlider();
        loadFeaturedData();
    }

    public void renewItems(View View) {
        dataModels = new ArrayList<>();
        DataModel dataItems = new DataModel();
        dataModels.add(dataItems);

        sliderAdapter.renewItems(dataModels);
        sliderAdapter.deleteItems(0);
    }

    private void loadFirebaseForSlider() {
        myRef.child("trailer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSlider: snapshot.getChildren()){
                    DataModel sliderItem = contentSlider.getValue(DataModel.class);
                    dataModels.add(sliderItem);
                }
                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadData() {
        loadFeaturedData();
        loadMoviesData();
    }

    private void loadFeaturedData() {
        DatabaseReference FRef = database.getReference("featured");
        featuredRecyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        featuredRecyclerView.setLayoutManager(layoutManager);

        featureModels = new ArrayList<>();
        featuredAdapter = new FeaturedAdapter(featureModels);
        featuredRecyclerView.setAdapter(featuredAdapter);

        FRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSnapShot : snapshot.getChildren()){
                    FeatureModel featureModel = contentSnapShot.getValue(FeatureModel.class);
                    featureModels.add(featureModel);
                }
                featuredAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadSeriesData();
    }

    private void loadSeriesData() {
        DatabaseReference SRef = database.getReference("series");
        webSeriesRecyclerView = findViewById(R.id.webSeriesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation((RecyclerView.HORIZONTAL));
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        webSeriesRecyclerView.setLayoutManager(layoutManager);

        seriesModels = new ArrayList<>();
        seriesAdapter = new SeriesAdapter(seriesModels);
        webSeriesRecyclerView.setAdapter(seriesAdapter);

        SRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contentSnapShot : snapshot.getChildren()){
                    SeriesModel newSeriesModel = contentSnapShot.getValue(SeriesModel.class);
                    seriesModels.add(newSeriesModel);
                }
                seriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMoviesData() {
    }
}