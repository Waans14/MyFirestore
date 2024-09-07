package com.millenialzdev.myfirestore.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.millenialzdev.myfirestore.data.entitas.User;
import com.millenialzdev.myfirestore.databinding.ListItemUserBinding;
import com.millenialzdev.myfirestore.ui.EditDataActivity;

import java.util.List;

public class ItemUserAdapter extends RecyclerView.Adapter<ItemUserAdapter.ViewHolder> {

    private Context context;
    private List<User> mList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ItemUserAdapter(Context context, List<User> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ItemUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemUserBinding binding = ListItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemUserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final User user = mList.get(position);

        if (user != null){
            holder.binding.tvNama.setText(user.getNama());
            holder.binding.tvEmail.setText(user.getEmail());
            holder.binding.tvPassword.setText(user.getPassword());

            holder.binding.ibEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("User")
                            .setItems(new CharSequence[]{"Edit", "Hapus", "Batal"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            Intent intent = new Intent(context, EditDataActivity.class);
                                            intent.putExtra("user", user);
                                            context.startActivity(intent);
                                            break;
                                        case 1:
                                            deleteUser(user.getId(), position);
                                            break;
                                        case 2:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            }).show();
                }
            });

        }
    }

    private void deleteUser(String id, int position) {
        db.collection("users").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "User Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mList.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "User Gagal Dihapus", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListItemUserBinding binding;

        public ViewHolder(@NonNull ListItemUserBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
