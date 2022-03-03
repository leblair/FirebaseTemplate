package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.model.Post;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetail extends AppFragment {


    public PostDetail() {
        // Required empty public constructor
    }


    public static PostDetail newInstance(Post post) {
        PostDetail fragment = new PostDetail();
        Bundle args = new Bundle();
        args.putSerializable("post",(Serializable)post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post)getArguments().getSerializable("post");
        }


    }

    private Post post;
    private ImageView imagenDetail;
    private TextView contenidoDetail;
    private TextView author;
    private CheckBox favos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        imagenDetail = view.findViewById(R.id.imagen);
        contenidoDetail = view.findViewById(R.id.contenido);
        favos = view.findViewById(R.id.favorito);
        author = view.findViewById(R.id.autor);
        // Inflate the layout for this fragment
        contenidoDetail.setText(post.content);
        author.setText(post.authorName);
        Glide.with(requireContext()).load(post.imageUrl).into(imagenDetail);

        favos.setOnClickListener(v -> {
            db.collection("posts").document(post.postid)
                    .update("likes."+auth.getUid(),
                            !post.likes.containsKey(auth.getUid()) ? true : FieldValue.delete());
        });

        System.out.println(auth.getUid());

        db.collection("posts").document(PostDetail)
        //favos.setChecked(post.likes.containsKey(Objects.requireNonNull(auth.getCurrentUser().getUid())));






        return view;
    }
}