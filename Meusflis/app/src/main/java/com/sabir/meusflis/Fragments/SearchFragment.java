package com.sabir.meusflis.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.Adapters.AllAdapter;
import com.sabir.meusflis.Models.SeriesModel;
import com.sabir.meusflis.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference databaseReference = database.getReference();

    private AllAdapter allAdapter;
    private List<SeriesModel> allList;
    private RecyclerView rvAll;

    private ShimmerFrameLayout sflRecyclerView;

    private LinearLayout llGenderFilter;
    private EditText etSearchMovie;
    private ImageView ivSearchTitle;
    private TextView tvSearchTitle, tvGenderFilter;

    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        ivSearchTitle = root.findViewById(R.id.ivSearchTitle);
        etSearchMovie = root.findViewById(R.id.etSearchMovie);
        tvSearchTitle = root.findViewById(R.id.tvSearchTitle);
        llGenderFilter = root.findViewById(R.id.llGenderFilter);
        tvGenderFilter = root.findViewById(R.id.tvGenderFilter);
        sflRecyclerView = root.findViewById(R.id.sflRecyclerView);

        rvAll = root.findViewById(R.id.rvAll);
        rvAll.setLayoutManager(new GridLayoutManager(getContext(), 3));

        showAllMovies();

        ivSearchTitle.setOnClickListener(v -> {
            tvSearchTitle.setVisibility(View.GONE);
            etSearchMovie.setVisibility(View.VISIBLE);
            etSearchMovie.requestFocus();

            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearchMovie, InputMethodManager.SHOW_IMPLICIT);
        });

        etSearchMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    allAdapter.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        llGenderFilter.setOnClickListener(v -> {
            showGenreFilterDialog();
        });
        return root;
    }

    private void showAllMovies() {
        allList = new ArrayList<>();

        databaseReference.child("series").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot postsnapshot : snapshot.getChildren()){
                        SeriesModel model = postsnapshot.getValue(SeriesModel.class);
                        allList.add(model);
                        Collections.shuffle(allList);
                    }
                    allAdapter = new AllAdapter(getContext(), allList, userId);
                    rvAll.setAdapter(allAdapter);
                }
                sflRecyclerView.stopShimmer();
                sflRecyclerView.setVisibility(View.GONE);
                rvAll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showGenreFilterDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_genres, null);
        ListView lvFirstColumn = dialogView.findViewById(R.id.lvFirstColumn);
        ListView lvSecondColumn = dialogView.findViewById(R.id.lvSecondColumn);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        List<String> genreList = new ArrayList<>();

        databaseReference.child("genre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                genreList.clear();
                for (DataSnapshot genreSnapshot : snapshot.getChildren()) {
                    Map<String, String> genreMap = (Map<String, String>) genreSnapshot.getValue();
                    if (genreMap != null) {
                        String genreName = genreMap.get("g_name");
                        if (genreName != null) {
                            genreList.add(genreName);
                        }
                    }
                }

                int halfSize = genreList.size() / 2;
                List<String> firstColumn = genreList.subList(0, halfSize);
                List<String> secondColumn = genreList.subList(halfSize, genreList.size());

                ArrayAdapter<String> firstColumnAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, firstColumn);
                ArrayAdapter<String> secondColumnAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, secondColumn);
                lvFirstColumn.setAdapter(firstColumnAdapter);
                lvSecondColumn.setAdapter(secondColumnAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        AdapterView.OnItemClickListener genreClickListener = (parent, view, position, id) -> {
            String selectedGenre = (String) parent.getItemAtPosition(position);
            tvGenderFilter.setText(selectedGenre);
            filterByGenre(selectedGenre);
            dialog.dismiss();
        };

        lvFirstColumn.setOnItemClickListener(genreClickListener);
        lvSecondColumn.setOnItemClickListener(genreClickListener);

        dialog.show();
    }

    private void filterByGenre(String selectedGenre) {
        List<SeriesModel> filteredList = new ArrayList<>();

        // Obtener el ID del género a partir del nombre del género seleccionado
        databaseReference.child("genre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String genreId = null;

                // Encontrar el ID correspondiente al género seleccionado
                for (DataSnapshot genreSnapshot : snapshot.getChildren()) {
                    Map<String, String> genreMap = (Map<String, String>) genreSnapshot.getValue();
                    if (genreMap != null && genreMap.get("g_name").equals(selectedGenre)) {
                        genreId = genreSnapshot.getKey();  // Esto obtendrá algo como "g_mystery"
                        break;
                    }
                }

                // Filtrar la lista de series por el ID del género encontrado
                if (genreId != null) {
                    for (SeriesModel series : allList) {
                        Map<String, String> seriesGenres = series.getGenre();
                        if (seriesGenres != null && seriesGenres.containsKey(genreId)) {
                            filteredList.add(series);
                        }
                    }

                    // Actualizar el adaptador con la lista filtrada
                    allAdapter = new AllAdapter(getContext(), filteredList, userId);
                    rvAll.setAdapter(allAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores
            }
        });
    }

}
