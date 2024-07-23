package com.example.meusflis.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meusflis.Activities.MainActivity;
import com.example.meusflis.Adapters.EstrenosAdapter;
import com.example.meusflis.Common.Common;
import com.example.meusflis.Models.EstrenosModel;
import com.example.meusflis.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NuevoPopularFragment extends Fragment {

    EstrenosAdapter estrenosAdapter;
    List<EstrenosModel> estrenosModel;
    RecyclerView rvComingSoonMovies;

    DatabaseReference reference;

    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_nuevo_popular, container, false);

        rvComingSoonMovies = root.findViewById(R.id.rvComingSoon);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_coming_soon);
        reference = FirebaseDatabase.getInstance().getReference();

        loadEstrenos();

        return root;
    }

    private void loadEstrenos(){
        estrenosModel = new ArrayList<>();

        reference.child(Common.NODO_MOVIES)
                .child(Common.NODO_ESTRENOS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot postsnap : snapshot.getChildren()){
                                EstrenosModel model = postsnap.getValue(EstrenosModel.class);
                                estrenosModel.add(model);
                            }
                            estrenosAdapter = new EstrenosAdapter(getContext(), estrenosModel);
                            rvComingSoonMovies.setAdapter(estrenosAdapter);
                        }

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        rvComingSoonMovies.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
