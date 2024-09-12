package com.sabir.meusflis.Fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.R;

public class ProfileFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference databaseReference = database.getReference("user");

    ImageView ivProfile;
    TextView tvUser, tvEmail, tvSurnameUserData, tvPhoneUserData, tvAddressUserData, tvMobileUserData;
    EditText etSurnameUserData, etPhoneUserData, etAddressUserData, etMobileUserData;
    Button btnEdit, btnSaveChanges, btnCancel;

    private String email;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ivProfile = root.findViewById(R.id.ivProfile);

        tvUser = root.findViewById(R.id.tvUser);
        tvEmail = root.findViewById(R.id.tvEmail);

        tvSurnameUserData = root.findViewById(R.id.tvSurnameUserData);
        tvPhoneUserData = root.findViewById(R.id.tvPhoneUserData);
        tvAddressUserData = root.findViewById(R.id.tvAddressUserData);
        tvMobileUserData = root.findViewById(R.id.tvMobileUserData);

        etSurnameUserData = root.findViewById(R.id.etSurnameUserData);
        etPhoneUserData = root.findViewById(R.id.etPhoneUserData);
        etAddressUserData = root.findViewById(R.id.etAddressUserData);
        etMobileUserData = root.findViewById(R.id.etMobileUserData);

        btnEdit = root.findViewById(R.id.btnEdit);
        btnSaveChanges = root.findViewById(R.id.btnSaveChanges);
        btnCancel = root.findViewById(R.id.btnCancel);

        email = getActivity().getIntent().getStringExtra("email");

        loadUserData(email);

        btnEdit.setOnClickListener(view -> enableEditing(true));
        btnCancel.setOnClickListener(view -> enableEditing(false));
        btnSaveChanges.setOnClickListener(view -> saveUserData());

        return root;
    }

    private void loadUserData(String email) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userFound = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);

                    if (userEmail != null && userEmail.equals(email)) {
                        userFound = true;

                        userId = userSnapshot.getKey();

                        String name = userSnapshot.child("name").getValue(String.class);
                        String surname = userSnapshot.child("surname").getValue(String.class);
                        String phone = userSnapshot.child("telephone").getValue(String.class);
                        String address = userSnapshot.child("address").getValue(String.class);
                        String mobile = userSnapshot.child("mobile").getValue(String.class);

                        tvUser.setText(name);
                        tvEmail.setText(email);

                        tvSurnameUserData.setText(surname);
                        tvPhoneUserData.setText(phone);
                        tvAddressUserData.setText(address);
                        tvMobileUserData.setText(mobile);

                        etSurnameUserData.setText(surname);
                        etPhoneUserData.setText(phone);
                        etAddressUserData.setText(address);
                        etMobileUserData.setText(mobile);

                        enableEditing(false);
                        break;
                    }
                }
                if (!userFound) {
                    Toast.makeText(getActivity(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error de base de datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void enableEditing(boolean enable) {
        tvSurnameUserData.setVisibility(enable ? View.GONE : View.VISIBLE);
        etSurnameUserData.setVisibility(enable ? View.VISIBLE : View.GONE);

        tvPhoneUserData.setVisibility(enable ? View.GONE : View.VISIBLE);
        etPhoneUserData.setVisibility(enable ? View.VISIBLE : View.GONE);

        tvAddressUserData.setVisibility(enable ? View.GONE : View.VISIBLE);
        etAddressUserData.setVisibility(enable ? View.VISIBLE : View.GONE);

        tvMobileUserData.setVisibility(enable ? View.GONE : View.VISIBLE);
        etMobileUserData.setVisibility(enable ? View.VISIBLE : View.GONE);

        btnEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
        btnSaveChanges.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnCancel.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void saveUserData() {
        String surname = etSurnameUserData.getText().toString().trim();
        String phone = etPhoneUserData.getText().toString().trim();
        String address = etAddressUserData.getText().toString().trim();
        String mobile = etMobileUserData.getText().toString().trim();

        if (userId != null) {
            databaseReference.child(userId).child("surname").setValue(surname);
            databaseReference.child(userId).child("telephone").setValue(phone);
            databaseReference.child(userId).child("address").setValue(address);
            databaseReference.child(userId).child("mobile").setValue(mobile);

            tvSurnameUserData.setText(surname);
            tvPhoneUserData.setText(phone);
            tvAddressUserData.setText(address);
            tvMobileUserData.setText(mobile);

            Toast.makeText(getActivity(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            enableEditing(false);
        }
        else {
            Toast.makeText(getActivity(), "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }
}
