package com.millenialzdev.myfirestore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.millenialzdev.myfirestore.R;
import com.millenialzdev.myfirestore.data.entitas.User;
import com.millenialzdev.myfirestore.databinding.ActivityEditDataBinding;

import java.util.HashMap;
import java.util.Map;

public class EditDataActivity extends AppCompatActivity {

    private ActivityEditDataBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        setupData();
        setupAction();
    }

    private void setupData() {
        if (getIntent().hasExtra("user")){
            User user = (User) getIntent().getSerializableExtra("user");
            if (user != null){
                binding.etEmail.setText(user.getEmail());
                binding.etNama.setText(user.getNama());
                binding.etPassword.setText(user.getPassword());

                binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = user.getId();
                        String emailBaru = binding.etEmail.getText().toString();
                        String namaBaru = binding.etNama.getText().toString();
                        String passwordBaru = binding.etPassword.getText().toString();

                        updateUser(id, emailBaru, namaBaru, passwordBaru);
                    }
                });
            }
        }
    }

    private void updateUser(String id, String emailBaru, String namaBaru, String passwordBaru) {
        Map<String, Object> updateUser = new HashMap<>();
        updateUser.put("email", emailBaru);
        updateUser.put("nama", namaBaru);
        updateUser.put("password", passwordBaru);

        db.collection("users").document(id)
                .update(updateUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditDataActivity.this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Update Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupAction() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}