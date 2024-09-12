package com.sabir.meusflis.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.Adapters.CarouselAdapter;
import com.sabir.meusflis.Adapters.SeriesAdapter;
import com.sabir.meusflis.Models.CarouselModel;
import com.sabir.meusflis.Models.SeriesModel;
import com.sabir.meusflis.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference databaseReference = database.getReference();

    private List<CarouselModel> carouselList;
    private List<SeriesModel> popularSeriesList;
    private List<SeriesModel> favouriteSeriesList;

    private SliderView svCarousel;

    private RecyclerView rvPopular, rvFavourite;

    private CarouselAdapter carouselAdapter;
    private SeriesAdapter popularSeriesAdapter;
    private SeriesAdapter favouriteSeriesAdapter;

    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        svCarousel = root.findViewById(R.id.svCarousel);

        rvPopular = root.findViewById(R.id.rvPopular);
        rvFavourite = root.findViewById(R.id.rvFavourite);

        carouselList = new ArrayList<>();
        carouselAdapter = new CarouselAdapter(getContext());
        svCarousel.setSliderAdapter(carouselAdapter);
        svCarousel.setIndicatorAnimation(IndicatorAnimationType.WORM);
        svCarousel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarousel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        svCarousel.setScrollTimeInSec(6);
        renewItems(svCarousel);

        popularSeriesList = new ArrayList<>();
        favouriteSeriesList = new ArrayList<>();

        rvPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFavourite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        popularSeriesAdapter = new SeriesAdapter(getContext(), popularSeriesList, userId);
        favouriteSeriesAdapter = new SeriesAdapter(getContext(), favouriteSeriesList, userId);

        rvPopular.setAdapter(popularSeriesAdapter);
        rvFavourite.setAdapter(favouriteSeriesAdapter);

        loadCarousel();
        loadPopularSeries();
        loadFavouritesSeries();

        return root;
    }

    public void renewItems(View View) {
        carouselList = new ArrayList<>();
        CarouselModel dataItems = new CarouselModel();
        carouselList.add(dataItems);

        carouselAdapter.renewItems(carouselList);
        carouselAdapter.deleteItems(0);
    }

    private void loadCarousel() {
        databaseReference.child("trailer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CarouselModel sliderItem = dataSnapshot.getValue(CarouselModel.class);
                    carouselList.add(sliderItem);
                }
                carouselAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPopularSeries() {
        databaseReference.child("series").orderByChild("likeCounter").limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popularSeriesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SeriesModel series = dataSnapshot.getValue(SeriesModel.class);
                    if (series != null) {
                        popularSeriesList.add(series);
                    }
                }
                popularSeriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFavouritesSeries() {
        DatabaseReference userLikesRef = databaseReference.child("user").child(userId).child("like_series");

        userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favouriteSeriesList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot likeSnapshot : snapshot.getChildren()) {
                        String seriesId = likeSnapshot.getValue(String.class);

                        databaseReference.child("series").child(seriesId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                SeriesModel series = snapshot.getValue(SeriesModel.class);
                                if (series != null) {
                                    favouriteSeriesList.add(series);
                                    favouriteSeriesAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}