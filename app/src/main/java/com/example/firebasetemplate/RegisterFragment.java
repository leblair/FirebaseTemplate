package com.example.firebasetemplate;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;


public class RegisterFragment extends AppFragment {
    private FragmentRegisterBinding binding;
    private Uri uriImagen;
    //pedir nombre y foto aparte del email
    private String uidUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        binding.imageregister.setOnClickListener(v->seleccionarImagen());


        appViewModel.uriImagenSeleccionada.observe(getViewLifecycleOwner(),uri->{
            if(uriImagen!=null){
                Glide.with(this).load(uri).into(binding.imageregister);
                uriImagen=uri;
            }
        });

        binding.createAccountButton.setOnClickListener(v->{
            if(binding.emailEditText.getText().toString().isEmpty()){
                binding.emailEditText.setError("Required email");
                return;
            }else if(binding.passwordEditText.getText().toString().isEmpty()){
                binding.passwordEditText.setError("Required password");
                return;
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailEditText.getText().toString(),
                    binding.passwordEditText.getText().toString()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task){
                    if(task.isSuccessful()){
                        // subir la foto seleccionada y obtener el enlace de descarga

                        if(uriImagen==null){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(binding.emailEditText.getText().toString())
                                    .setPhotoUri(null)
                                    .build();

                            uidUser=user.getUid();
                            user.updateProfile(profileUpdates);

                            //guardar
                            db.collection("users").document(binding.emailEditText.getText().toString()).set(
                                    new User(
                                            uidUser,
                                            binding.emailEditText.getText().toString(),
                                            binding.emailEditText.getText().toString(),
                                            null
                                    )
                            );


                        }else{

                            FirebaseStorage.getInstance().getReference("/images/"+ UUID.randomUUID()+".jpg")
                                    .putFile(uriImagen)
                                    .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                                    .addOnSuccessListener(url -> {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(binding.emailEditText.getText().toString())
                                                .setPhotoUri(url)
                                                .build();

                                        uidUser=user.getUid();
                                        user.updateProfile(profileUpdates);

                                        //guardar
                                        db.collection("users").document(binding.emailEditText.getText().toString()).set(
                                                new User(
                                                        uidUser,
                                                        binding.emailEditText.getText().toString(),
                                                        binding.emailEditText.getText().toString(),
                                                        url.toString()//ERROR
                                                )
                                        );

                                    });


                        }

//                        navController.navigate(R.id.action_registerFragment_to_postsHomeFragment);
//
//                        FirebaseStorage.getInstance()
//                                .getReference("/images/"+ UUID.randomUUID()+".jpg")
//                                .putFile(uriImagen)
//                                .continueWithTask(task2->task2.getResult().getStorage().getDownloadUrl())
//                                .addOnSuccessListener(urlDescarga->{
//                                    Post post=new Post();
////                        post.content = binding.contenido.getText().toString();
////                        post.authorName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
////                        post.date = LocalDateTime.now().toString();
//                                    post.imageUrl=urlDescarga.toString();
//
//                                    FirebaseFirestore.getInstance().collection("posts")
//                                            .add(post)
//                                            .addOnCompleteListener(task3->{
//                                                binding.createAccountButton.setEnabled(true);
//                                                navController.popBackStack();
//                                            });
//                                });


                        /*FirebaseStorage.getInstance()
                                .getReference("/images/" + UUID.randomUUID() + ".jpg")
                                .putFile(task.getResult().getUser().getPhotoUrl())
                                .continueWithTask(t -> t.getResult().getStorage().getDownloadUrl())
                                .addOnSuccessListener(urlDescarga -> {
                                    *//*Post post = new Post();
                                    post.content = binding.contenido.getText().toString();
                                    post.authorName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    post.date = LocalDateTime.now().toString();*//*

                                    Post post = new Post();
                                    post.imageUrl = urlDescarga.toString();
                                    binding.imageView
                                    FirebaseFirestore.getInstance().collection("posts")
                                            .add(post)
                                            .addOnCompleteListener(task -> {
                                                binding.publicar.setEnabled(true);
                                                navController.popBackStack();
                                            });
                                });*/
                        //actualizo el perfil, el nombre la url de su de perfil


                        navController.navigate(R.id.action_registerFragment_to_postsHomeFragment);
                    }else{
                        Log.w("FAIL","createUserWithEmail:failure",task.getException());
                        Toast.makeText(requireContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    void updatePorfile() {

    }

    private void seleccionarImagen() {
        galeria.launch("image/*");//abre la libreria
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        appViewModel.setUriImagenSeleccionada(uri);//le pasas la uri de la foto (direccion)
    });
}

//



