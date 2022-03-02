package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.firebasetemplate.model.Post;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetail extends Fragment {


    public PostDetail() {
        // Required empty public constructor
    }


    public static PostDetail newInstance(Post post) {
        PostDetail fragment = new PostDetail();
        Bundle args = new Bundle();
        args.putSerializable("post",(Serializable)post);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Post post;
    private ImageView imagenDetail;
    private TextView contenidoDetail;
    private CheckBox favos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        imagenDetail = view.findViewById(R.id.imagen);
        contenidoDetail = view.findViewById(R.id.contenido);
        favos = view.findViewById(R.id.favorito);
        // Inflate the layout for this fragment

        if (getArguments() != null) {
            post = (Post)getArguments().getSerializable("post");

            contenidoDetail.setText(post.content);

        }

            return view;
    }
}