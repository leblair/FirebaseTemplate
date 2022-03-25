package com.example.firebasetemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.CommentsRowBinding;
import com.example.firebasetemplate.databinding.FragmentPostDetailBinding;
import com.example.firebasetemplate.model.Comment;
import com.example.firebasetemplate.model.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PostDetailFragment extends AppFragment {

    private final long createdMillis = System.currentTimeMillis();


    private Post post;
    private FragmentPostDetailBinding binding;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    CommentsAdapter commentsAdapter;
    String postid;
    boolean cont = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPostDetailBinding.inflate(inflater, container, false)).getRoot();


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postid = PostDetailFragmentArgs.fromBundle(getArguments()).getPostid();
        commentsAdapter = new CommentsAdapter(comments, getContext());
        binding.commentsRecycler.setAdapter(commentsAdapter);

        db.collection("posts").document(postid).addSnapshotListener((documentSnapshot, error) -> {
            post = documentSnapshot.toObject(Post.class);
            Glide.with(getContext()).load(post.imageUser).centerCrop().into(binding.autorFoto);

            binding.contenido.setText(post.content);
            binding.autor.setText(post.authorName);
            Glide.with(getContext()).load(post.imageUrl).into(binding.imagen);

            binding.favos.setOnClickListener(v -> {
                db.collection("posts").document(postid)
                        .update("likes." + auth.getUid(),
                                !post.likes.containsKey(auth.getUid()) ? true : FieldValue.delete());

            });


            binding.favos.setChecked(post.likes.containsKey(auth.getCurrentUser().getUid()));
            binding.numFav.setText(String.valueOf(post.likes.size()));


            FirebaseFirestore.getInstance().collection("posts").document(postid).collection("comments").addSnapshotListener((collectionSnapshot, error1) -> {
                comments.clear();
                for (DocumentSnapshot documentSnapshot1 : collectionSnapshot) {
                    Comment comment = documentSnapshot1.toObject(Comment.class);
                    comment.id = documentSnapshot1.getId();
                    comment.postId = postid;
                    comments.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();
            });


            binding.addComment.setOnClickListener(v -> {
                //add comment
                Comment comment = new Comment();
                comment.authorName = auth.getCurrentUser().getEmail();
                comment.text = binding.inputComment2.getText().toString();


                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                comment.date = formatter.format(date);



                comments.clear();
                FirebaseFirestore.getInstance().collection("posts").document(postid).collection("comments").add(comment).addOnCompleteListener(task -> {
                    binding.inputComment.getEditText().setText("");
                });
                commentsAdapter.notifyDataSetChanged();


            });


        });


    }


    class CommentsAdapter extends RecyclerView.Adapter<PostDetailFragment.CommentsAdapter.ViewHolder> {
        private ArrayList<Comment> comments;
        private Context mContext;

        CommentsAdapter(ArrayList<Comment> comments, Context context) {
            this.comments = comments;
            this.mContext = context;
        }

        @NonNull
        @Override
        public PostDetailFragment.CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostDetailFragment.CommentsAdapter.ViewHolder(CommentsRowBinding.inflate(getLayoutInflater(), parent, false));
        }


        @Override
        public void onBindViewHolder(@NonNull PostDetailFragment.CommentsAdapter.ViewHolder holder, int position) {
            Comment comment = comments.get(position);
            db.collection("posts").document(comment.postId).collection("comments").document(comment.id).addSnapshotListener((documentSnapshot, error) -> {
                Comment comment2 = documentSnapshot.toObject(Comment.class);
                holder.binding.autor.setText(comment.authorName);
                holder.binding.comentario.setText(comment.text);
                //          holder.binding.fecha.setText(comment.text);

                holder.binding.checkBox2.setOnClickListener(v -> {
                    db.collection("posts").document(comment.postId).collection("comments").document(comment.id)
                            .update("likes." + auth.getUid(),
                                    !comment2.likes.containsKey(auth.getUid()) ? true : FieldValue.delete());
                });
                holder.binding.checkBox2.setChecked(comment2.likes.containsKey(auth.getUid()));
                holder.binding.numlikes.setText(String.valueOf(comment2.likes.size()));

                holder.binding.fecha.setText(comment2.date);
            });
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CommentsRowBinding binding;

            public ViewHolder(CommentsRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

            }
        }
    }

}