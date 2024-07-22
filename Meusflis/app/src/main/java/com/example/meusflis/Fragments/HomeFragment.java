package com.example.meusflis.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.meusflis.Adapters.HomeMoviesAdapter;
import com.example.meusflis.Adapters.MoviesTop10Adapter;
import com.example.meusflis.Adapters.VariadosMoviesAdapter;
import com.example.meusflis.Adapters.VideoPrincipalAdapter;
import com.example.meusflis.Common.Common;
import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView rvFeedHeader, rvTendencias, rvAnimes, rvVariados, rvAccion, rvTop10, rvComedia;

    //FEED HEADER
    VideoPrincipalAdapter videoPrincipalAdapter;
    List<MoviesModel> moviesModelList;

    //ADAPTERS
    HomeMoviesAdapter moviesAdapter;
    VariadosMoviesAdapter variadosMoviesAdapter;
    MoviesTop10Adapter moviesTop10Adapter;

    //MODELS
    List<MoviesModel> moviesTendenciasModels, moviesAnimesModels, moviesVariadosModels, moviesAccionModels, moviesTop10Models, moviesComediaModels;

    //SHIMMERS
    ShimmerFrameLayout shimmerFeedHeader, shimmerTendencias, shimmerAnimes, shimmerVariados, shimmerAccion, shimmerTop10, shimmerComedia;

    //LINEAR CONTAINERS
    LinearLayout linearTendencias, linearAnimes, linearVariados, linearAccion, linearTop10, linearComedia;

    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvFeedHeader = root.findViewById(R.id.rv_feed_header);
        rvTendencias = root.findViewById(R.id.rvTendencias);
        rvAnimes = root.findViewById(R.id.rvAnimes);
        rvVariados = root.findViewById(R.id.rvVariados);
        rvAccion = root.findViewById(R.id.rvAccion);
        rvTop10 = root.findViewById(R.id.rvTop10);
        rvComedia = root.findViewById(R.id.rvComedia);

        shimmerFeedHeader = root.findViewById(R.id.shimmer_home_feed);
        shimmerTendencias = root.findViewById(R.id.shimmer_home_tendencias);
        shimmerAnimes = root.findViewById(R.id.shimmer_home_animes);
        shimmerVariados = root.findViewById(R.id.shimmer_home_variados);
        shimmerAccion = root.findViewById(R.id.shimmer_home_accion);
        shimmerTop10 = root.findViewById(R.id.shimmer_home_top10);
        shimmerComedia = root.findViewById(R.id.shimmer_home_comedia);

        linearTendencias = root.findViewById(R.id.linearTendencias);
        linearAnimes = root.findViewById(R.id.linearAnimes);
        linearVariados = root.findViewById(R.id.linearVariados);
        linearAccion = root.findViewById(R.id.linearAccion);
        linearTop10 = root.findViewById(R.id.linearTop10);
        linearComedia = root.findViewById(R.id.linearComedia);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        loadPrincipalVideo();

        showMoviesTendencias();

        showMoviesAnimes();

        showMoviesVariados();
        
        showMoviesAccion();

        showMoviesTop10();

        showMoviesComedia();

        return root;
    }

    public void loadPrincipalVideo() {
        moviesModelList = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES).child(Common.NODO_GENERAL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    moviesModelList.clear();

                    for (DataSnapshot postsnap:snapshot.getChildren()){
                        MoviesModel model = postsnap.getValue(MoviesModel.class);
                        moviesModelList.add(model);
                        Collections.shuffle(moviesModelList);
                    }

                    videoPrincipalAdapter = new VideoPrincipalAdapter(getContext(), moviesModelList);
                    rvFeedHeader.setAdapter(videoPrincipalAdapter);
                    shimmerFeedHeader.stopShimmer();
                    shimmerFeedHeader.setVisibility(View.GONE);
                    rvFeedHeader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showMoviesTendencias() {
        moviesTendenciasModels = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .orderByChild(Common.ELEMENTO_NODO_CATEGORIA)
                .equalTo(Common.CATEGORY_TENDENCIAS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            moviesTendenciasModels.clear();

                            for (DataSnapshot postsnap:snapshot.getChildren()){
                                MoviesModel model = postsnap.getValue(MoviesModel.class);
                                moviesTendenciasModels.add(model);
                                Collections.shuffle(moviesTendenciasModels);
                            }

                            moviesAdapter = new HomeMoviesAdapter(getContext(), moviesTendenciasModels);
                            rvTendencias.setAdapter(moviesAdapter);

                            shimmerTendencias.stopShimmer();
                            shimmerTendencias.setVisibility(View.GONE);

                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0){
                                linearTendencias.setVisibility(View.VISIBLE);
                            }
                            else{
                                linearTendencias.setVisibility(View.GONE);
                            }
                        }
                        else{
                            shimmerTendencias.stopShimmer();
                            shimmerTendencias.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void showMoviesAnimes() {
        moviesAnimesModels = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .orderByChild(Common.ELEMENTO_NODO_CATEGORIA)
                .equalTo(Common.CATEGORY_ANIMES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            moviesAnimesModels.clear();

                            for (DataSnapshot postsnap:snapshot.getChildren()){
                                MoviesModel model = postsnap.getValue(MoviesModel.class);
                                moviesAnimesModels.add(model);
                                Collections.shuffle(moviesAnimesModels);
                            }

                            moviesAdapter = new HomeMoviesAdapter(getContext(), moviesAnimesModels);
                            rvAnimes.setAdapter(moviesAdapter);

                            shimmerAnimes.stopShimmer();
                            shimmerAnimes.setVisibility(View.GONE);

                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0){
                                linearAnimes.setVisibility(View.VISIBLE);
                            }
                            else{
                                linearAnimes.setVisibility(View.GONE);
                            }
                        }
                        else{
                            shimmerAnimes.stopShimmer();
                            shimmerAnimes.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void showMoviesVariados() {
        moviesVariadosModels = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            moviesVariadosModels.clear();

                            for (DataSnapshot postsnap:snapshot.getChildren()){
                                MoviesModel model = postsnap.getValue(MoviesModel.class);
                                moviesVariadosModels.add(model);
                                Collections.shuffle(moviesVariadosModels);
                            }

                            variadosMoviesAdapter = new VariadosMoviesAdapter(getContext(), moviesVariadosModels);
                            rvVariados.setAdapter(variadosMoviesAdapter);

                            shimmerVariados.stopShimmer();
                            shimmerVariados.setVisibility(View.GONE);

                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0){
                                linearVariados.setVisibility(View.VISIBLE);
                            }
                            else{
                                linearVariados.setVisibility(View.GONE);
                            }
                        }
                        else{
                            shimmerVariados.stopShimmer();
                            shimmerVariados.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void showMoviesAccion() {
        moviesAccionModels = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .orderByChild(Common.ELEMENTO_NODO_CATEGORIA)
                .equalTo(Common.CATEGORY_ACCION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            moviesAccionModels.clear();

                            for (DataSnapshot postsnap:snapshot.getChildren()){
                                MoviesModel model = postsnap.getValue(MoviesModel.class);
                                moviesAccionModels.add(model);
                                Collections.shuffle(moviesAccionModels);
                            }

                            moviesAdapter = new HomeMoviesAdapter(getContext(), moviesAccionModels);
                            rvAccion.setAdapter(moviesAdapter);

                            shimmerAccion.stopShimmer();
                            shimmerAccion.setVisibility(View.GONE);

                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0){
                                linearAccion.setVisibility(View.VISIBLE);
                            }
                            else{
                                linearAccion.setVisibility(View.GONE);
                            }
                        }
                        else{
                            shimmerAccion.stopShimmer();
                            shimmerAccion.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void showMoviesTop10(){
        moviesTop10Models = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES)
                .child(Common.NODO_MOVIES_TOPTEN)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            moviesTop10Models.clear();

                            for (DataSnapshot postsnap:snapshot.getChildren()){
                                MoviesModel model = postsnap.getValue(MoviesModel.class);
                                moviesTop10Models.add(model);
                            }

                            moviesTop10Adapter = new MoviesTop10Adapter(getContext(), moviesTop10Models);
                            rvTop10.setAdapter(moviesTop10Adapter);

                            shimmerTop10.stopShimmer();
                            shimmerTop10.setVisibility(View.GONE);


                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0){
                                linearTop10.setVisibility(View.VISIBLE);
                            }
                            else{
                                linearTop10.setVisibility(View.GONE);
                            }
                        }
                        else{
                            shimmerTop10.stopShimmer();
                            shimmerTop10.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void showMoviesComedia() {
        moviesComediaModels = new ArrayList<>();

        databaseReference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .orderByChild(Common.ELEMENTO_NODO_CATEGORIA)
                .equalTo(Common.CATEGORY_COMEDIA)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            moviesComediaModels.clear();

                            for (DataSnapshot postsnap:snapshot.getChildren()){
                                MoviesModel model = postsnap.getValue(MoviesModel.class);
                                moviesComediaModels.add(model);
                                Collections.shuffle(moviesComediaModels);
                            }

                            moviesAdapter = new HomeMoviesAdapter(getContext(), moviesComediaModels);
                            rvComedia.setAdapter(moviesAdapter);

                            shimmerComedia.stopShimmer();
                            shimmerComedia.setVisibility(View.GONE);

                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0){
                                linearComedia.setVisibility(View.VISIBLE);
                            }
                            else{
                                linearComedia.setVisibility(View.GONE);
                            }
                        }
                        else{
                            shimmerComedia.stopShimmer();
                            shimmerComedia.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}