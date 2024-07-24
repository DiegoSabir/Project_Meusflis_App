package com.example.meusflis.Fragments;

import static com.example.meusflis.Utils.ClassDialogs.showVideoMovie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meusflis.R;
import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {

    private TextView titleMovieDetails, sinopsisMovieDetails, timeMovieDetails;
    ImageView imageMovieDetails, imageBackPrincipalHome;
    LinearLayout btnPlay;

    String videoUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);

        titleMovieDetails = root.findViewById(R.id.titleMovieDetails);
        sinopsisMovieDetails = root.findViewById(R.id.sinopsisMovieDetails);
        timeMovieDetails = root.findViewById(R.id.timeMovieDetails);

        imageMovieDetails = root.findViewById(R.id.imageMovieDetails);
        imageBackPrincipalHome = root.findViewById(R.id.imageBackPrincipalHome);

        btnPlay = root.findViewById(R.id.btnPlayMovie);

        retrieveInfo();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideoMovie(getContext(), videoUrl);
            }
        });

        imageBackPrincipalHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return root;
    }

    private void retrieveInfo() {
        Bundle bundle = this.getArguments();

        if (bundle != null){
            String title = getArguments().getString("title");
            String time = getArguments().getString("time");
            String sinopsis = getArguments().getString("sinopsis");
            String image = getArguments().getString("image");

            videoUrl = getArguments().getString("videoUrl");

            titleMovieDetails.setText(title);
            timeMovieDetails.setText(time);
            sinopsisMovieDetails.setText(sinopsis);

            Picasso.get().load(image).placeholder(R.drawable.baseline_image_not_supported_24).resize(720, 720).onlyScaleDown().into(imageMovieDetails);

        }
    }
}