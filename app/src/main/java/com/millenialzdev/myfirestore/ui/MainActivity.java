package com.millenialzdev.myfirestore.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.millenialzdev.myfirestore.R;
import com.millenialzdev.myfirestore.data.entitas.User;
import com.millenialzdev.myfirestore.databinding.ActivityMainBinding;
import com.millenialzdev.myfirestore.ui.adapter.ItemUserAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore db;
    private ArrayList<User> mList;
    private ItemUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        setupData();
    }

    private void setupData() {
        mList = new ArrayList<>();
        binding.rvData.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        binding.rvData.setLayoutManager(mLayout);
        binding.rvData.setItemAnimator(new DefaultItemAnimator());

        tampilData();

    }

    private void tampilData() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            mList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                User user = document.toObject(User.class);
                                user.setId(document.getId());

                                mList.add(user);
                            }
                            adapter = new ItemUserAdapter(MainActivity.this, mList);
                            binding.rvData.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Data Gagal Di Muat", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}