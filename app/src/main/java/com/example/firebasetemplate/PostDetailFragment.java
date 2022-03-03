package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentPostDetailBinding;
import com.example.firebasetemplate.model.Post;
import com.google.firebase.firestore.FieldValue;


public class PostDetailFragment extends AppFragment {
    private Post post;
    private FragmentPostDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPostDetailBinding.inflate(inflater, container, false)).getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String postid = PostDetailFragmentArgs.fromBundle(getArguments()).getPostid();

        db.collection("posts").document(postid).addSnapshotListener((documentSnapshot,error) -> {
            post = documentSnapshot.toObject(Post.class);
            binding.contenido.setText(post.content);
            binding.autor.setText(post.authorName);
            Glide.with(requireContext()).load(post.imageUrl).into(binding.imagen);

            binding.favos.setOnClickListener(v -> {
                db.collection("posts").document(postid)
                        .update("likes."+auth.getUid(),
                                !post.likes.containsKey(auth.getUid()) ? true : FieldValue.delete());




                /* db.collection("posts").document(postid).get().addOnSuccessListener(documentSnapshot1 -> {
                    binding.numFav.setText(String.valueOf(documentSnapshot1.toObject(Post.class).likes.size()));
                    Toast.makeText(getContext(),String.valueOf(documentSnapshot1.toObject(Post.class).likes.size()), Toast.LENGTH_SHORT).show();
                });*/
            });



            binding.favos.setChecked(post.likes.containsKey(auth.getCurrentUser().getUid()));
            binding.numFav.setText(String.valueOf(post.likes.size()));


        });
    }

}