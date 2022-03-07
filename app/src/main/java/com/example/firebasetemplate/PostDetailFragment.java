package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentPostDetailBinding;
import com.example.firebasetemplate.databinding.ViewholderPostBinding;
import com.example.firebasetemplate.model.Comment;
import com.example.firebasetemplate.model.Post;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;


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
            Glide.with(getContext()).load(post.imageUrl).into(binding.imagen);

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


            binding.addComment.setOnClickListener(v -> {
                //add comment

                Comment comment = new Comment();
                comment.authorName = String.valueOf(auth.getCurrentUser());
                comment.day = LocalDateTime.now();
                comment.text = binding.inputComment2.getText().toString();
                //post.comments.add(comment);

                FirebaseFirestore.getInstance().collection("posts").document(postid).collection("comments").add(comment).addOnCompleteListener(task ->{
                    binding.inputComment.getEditText().setText("");
                });

            });



        });
    }

    class CommentsAdapter extends RecyclerView.Adapter<PostsHomeFragment.PostsAdapter.ViewHolder> {

        @NonNull
        @Override
        public PostsHomeFragment.PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostsHomeFragment.PostsAdapter.ViewHolder(ViewholderPostBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PostsHomeFragment.PostsAdapter.ViewHolder holder, int position) {
            Post post = postsList.get(position);
            holder.binding.contenido.setText(post.content);
            holder.binding.autor.setText(post.authorName);
            holder.binding.postLayout.setOnClickListener(v -> {
                NavGraphDirections.ActionToPostDetailFragment action = NavGraphDirections.actionToPostDetailFragment();
                action.setPostid(post.postid);
                navController.navigate(action);

            });
            Glide.with(requireContext()).load(post.imageUrl).into(holder.binding.imagen);

            holder.binding.favorito.setOnClickListener(v -> {
                db.collection("posts").document(post.postid)
                        .update("likes."+auth.getUid(),
                                !post.likes.containsKey(auth.getUid()) ? true : FieldValue.delete());
            });

            holder.binding.favorito.setChecked(post.likes.containsKey(auth.getUid()));
        }

        @Override
        public int getItemCount() {
            return postsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewholderPostBinding binding;
            public ViewHolder(ViewholderPostBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

}