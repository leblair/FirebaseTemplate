package com.example.firebasetemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentRegisterBinding;
import com.example.firebasetemplate.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDateTime;
import java.util.UUID;


public class RegisterFragment extends AppFragment {
    private FragmentRegisterBinding binding;

    //pedir nombre y foto aparte del email

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.imageView.setOnClickListener(v ->);
        appViewModel.
        binding.createAccountButton.setOnClickListener(v -> {
            if (binding.emailEditText.getText().toString().isEmpty()){
                binding.emailEditText.setError("Required email");
                return;
            }else if (binding.passwordEditText.getText().toString().isEmpty()){
                binding.passwordEditText.setError("Required password");
                return;
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailEditText.getText().toString(),
                    binding.passwordEditText.getText().toString()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // subir la foto seleccionada y obtener el enlace de descarga

                        FirebaseStorage.getInstance()
                                .getReference("/images/" + UUID.randomUUID() + ".jpg")
                                .putFile(task.getResult().getUser().getPhotoUrl())
                                .continueWithTask(t -> t.getResult().getStorage().getDownloadUrl())
                                .addOnSuccessListener(urlDescarga -> {
                                    Post post = new Post();
                                    post.content = binding.contenido.getText().toString();
                                    post.authorName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    post.date = LocalDateTime.now().toString();
                                    post.imageUrl = urlDescarga.toString();



                                    FirebaseFirestore.getInstance().collection("posts")
                                            .add(post)
                                            .addOnCompleteListener(task -> {
                                                binding.publicar.setEnabled(true);
                                                navController.popBackStack();
                                            });
                                });
                            //actualizo el perfil, el nombre la url de su de perfil


                       navController.navigate(R.id.action_registerFragment_to_postsHomeFragment);
                    }else{
                        Log.w("FAIL","createUserWithEmail:failure", task.getException());
                        Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}