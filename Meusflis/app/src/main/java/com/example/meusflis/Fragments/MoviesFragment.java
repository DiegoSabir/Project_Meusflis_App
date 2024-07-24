package com.example.meusflis.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meusflis.Adapters.AllMoviesAdapter;
import com.example.meusflis.Adapters.MoviesCategoriesAdapter;
import com.example.meusflis.Common.Common;
import com.example.meusflis.Models.MoviesCategoriesModel;
import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviesFragment extends Fragment implements MoviesCategoriesAdapter.EventListener{

    //MOVIES
    AllMoviesAdapter moviesAdapter;
    List<MoviesModel> moviesModels;
    RecyclerView rvAllMovies;

    //CATEGORIES
    MoviesCategoriesAdapter moviesCategoriesAdapter;
    List<MoviesCategoriesModel> moviesCategoriesModels;

    ShimmerFrameLayout shimmerFrameLayout;
    DatabaseReference reference;

    LinearLayout linearCategorySelection;
    EditText etSearchMovie;
    ImageView searchMovieIcon, imageBackToHome;
    TextView titleTextGeneral, titleTextCategories;

    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_movies, container, false);

        reference = FirebaseDatabase.getInstance().getReference();
        linearCategorySelection = root.findViewById(R.id.linearCategorySelection);
        etSearchMovie = root.findViewById(R.id.etSearchMovie);
        searchMovieIcon = root.findViewById(R.id.searchAllIcon);
        titleTextGeneral = root.findViewById(R.id.textOfItems);
        titleTextCategories = root.findViewById(R.id.tvCategorySelected);
        rvAllMovies = root.findViewById(R.id.rvAllMovies);
        shimmerFrameLayout = root.findViewById(R.id.shimmer);
        imageBackToHome = root.findViewById(R.id.imageBackPrincipalHomeFromMovies);

        rvAllMovies.setLayoutManager(new GridLayoutManager(getContext(), 3));

        showAllMovies();

        searchMovieIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTextGeneral.setVisibility(View.GONE);
                etSearchMovie.setVisibility(View.VISIBLE);
                etSearchMovie.requestFocus();

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearchMovie, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        etSearchMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    moviesAdapter.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linearCategorySelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCategories();
            }
        });

        imageBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return root;
    }

    private void showAllMovies() {
        moviesModels = new ArrayList<>();

        reference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        moviesModels.clear();

                        if (snapshot.exists()){

                            for (DataSnapshot postsnapshot : snapshot.getChildren()){

                                MoviesModel model = postsnapshot.getValue(MoviesModel.class);
                                moviesModels.add(model);
                                Collections.shuffle(moviesModels);

                            }
                            moviesAdapter = new AllMoviesAdapter(getContext(), moviesModels);
                            rvAllMovies.setAdapter(moviesAdapter);
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        rvAllMovies.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showDialogCategories() {
        View layout_dialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_list_categories, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout_dialog);

        FloatingActionButton fabCloseCategories;
        fabCloseCategories = layout_dialog.findViewById(R.id.fabCloseCategories);

        RecyclerView rvAllCategories = layout_dialog.findViewById(R.id.rvListCategories);

        ProgressBar progressBar = layout_dialog.findViewById(R.id.progressBarCategories);

        moviesCategoriesModels = new ArrayList<>();

        reference.child(Common.NODO_MOVIES)
                .child(Common.NODO_MOVIES_CATEGORIES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        moviesCategoriesModels.clear();

                        if (snapshot.exists()){

                            for (DataSnapshot postsnap : snapshot.getChildren()){
                                MoviesCategoriesModel model = postsnap.getValue(MoviesCategoriesModel.class);
                                moviesCategoriesModels.add(model);
                            }
                            moviesCategoriesAdapter = new MoviesCategoriesAdapter(getContext(), moviesCategoriesModels, MoviesFragment.this);
                            rvAllCategories.setAdapter(moviesCategoriesAdapter);
                        }

                        progressBar.setVisibility(View.GONE);
                        rvAllCategories.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        dialog = builder.create();
        dialog.show();

        fabCloseCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawableResource(R.color.colorSemiTransparent);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    public void onEventName(String nombre) {
        titleTextCategories.setText(nombre);
        Common.setCategorySelect(nombre.toUpperCase());
        moviesAdapter.getFilter().filter(etSearchMovie.getText().toString());
        dialog.dismiss();
    }
}