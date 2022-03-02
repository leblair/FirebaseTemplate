package com.example.firebasetemplate;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasetemplate.databinding.ActivityPostDetailsBinding;

public class PostDetails extends AppCompatActivity {
    private ActivityPostDetailsBinding binding;
    private ImageView imagenDetail;
    private TextView contenidoDetail;
    private ImageView favos;



    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

     /*   imagenDetail = findViewById(R.id.imagen);
        contenidoDetail = findViewById(R.id.contenido);
        favos = findViewById(R.id.favorito);
        Intent intent= getIntent();
        Post a = (Post) intent.getSerializableExtra("post");

        contenidoDetail.setText(a.content);
        Glide.with(getApplicationContext()).load(a.imageUrl).into(imagenDetail);
        favos.setOnClickListener(v -> {
            AppFragment.db.collection("posts").document(a.postid)
                    .update("likes."+auth.getUid(),
                            !a.likes.containsKey(auth.getUid()) ? true : FieldValue.delete());
        });

        holder.binding.favorito.setChecked(post.likes.containsKey(auth.getUid()));

        //binding.textView.setText(a.content);
        //Glide.with(getApplicationContext()).load(a.imageUrl).into(binding.imageView2);

*/

    }
}