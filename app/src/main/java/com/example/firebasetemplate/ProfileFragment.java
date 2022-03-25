package com.example.firebasetemplate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.FragmentProfileBinding;

public class ProfileFragment extends AppFragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentProfileBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                binding.textView.setText(firebaseAuth.getCurrentUser().getEmail());
                if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
                    Glide.with(getContext()).load(firebaseAuth.getCurrentUser().getPhotoUrl()).circleCrop().into(binding.imageView);
                }
                else {
                    binding.imageView.setImageResource(R.drawable.ic_person);
                }
            }
        });

        //binding.buttonEdit.setOnClickListener(v -> navController.navigate(R.id.updateProfile));

    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
    //aqui quiere el nombre mail y foto

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//        usermail=ProfileFragmentArgs.fromBundle(getArguments()).getUseremail();
//
//
//        if(usermail!=null && !usermail.equals("abcd")){
//            db.collection("users").document(usermail).get().addOnSuccessListener( documentSnapshot -> {
//                user = documentSnapshot.toObject(User.class);
//
//                if (user.getPhotoUser() != null) {
//                    Glide.with(this).load(user.getPhotoUser()).circleCrop().into(binding.imagenPerfil);
//                } else {
//                    binding.imagenPerfil.setImageResource(R.drawable.ic_baseline_face_24);
//                }
//
//                binding.nombrePerfil.setText(usermail);
//                binding.emailPerfil.setText(usermail);
//
//
//                getCantidad();//sino no accede bien o algo
//            });
//
//            //System.out.println(user.getUsername());
//            //cargar la foto
//
//        }
//        else if (usermail.equals("abcd")){
//            //cargar la foto
//            if (auth.getCurrentUser().getPhotoUrl() != null) {
//                Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).circleCrop().into(binding.imagenPerfil);
//            } else {
//                binding.imagenPerfil.setImageResource(R.drawable.ic_baseline_face_24);
//            }
//
//            binding.nombrePerfil.setText(auth.getCurrentUser().getDisplayName());
//            binding.emailPerfil.setText(auth.getCurrentUser().getEmail());
//
//            getCantidad();
//        }
//
//
//        //reset
//        cantidadPosts = 0;
//        cantidadFavs = 0;
//
//
//    }
//
//
//    Query getPersonalQuery() {
//        if(usermail!=null && !usermail.equals("abcd")){
//            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("authorName", user.getUsername());
//        }
//        else if(usermail.equals("abcd")) {
//            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("authorName", auth.getCurrentUser().getDisplayName());
//        }
//
//        return null;
//    }
//
//    Query getFavsQuery() {
//        if(usermail!=null && !usermail.equals("abcd")){//TODO
//            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("likes." + user.getIdUser(), true);
//        }
//        else if(usermail.equals("abcd")){//el placeholder
//            return FirebaseFirestore.getInstance().collection("posts").whereEqualTo("likes." + auth.getCurrentUser().getUid(), true);
//        }
//        return null;
//    }
//
//    private void getCantidad() {
//        getPersonalQuery().addSnapshotListener((collectionSnapshot, e) -> {
//            cantidadPosts = collectionSnapshot.size();
//            post = true;
//            updateUI();
//        });
//        getFavsQuery().addSnapshotListener((collectionSnapshot, e) -> {
//            cantidadFavs = collectionSnapshot.size();
//            fav = true;
//            updateUI();
//        });
//
//    }
//
//
//    void updateUI() {
//        if (post && fav) {
//            binding.CantidadProgressBar.setVisibility(View.GONE);
//            binding.relativeCantidad.setVisibility(View.VISIBLE);
//
//            binding.postsNum.setText(String.valueOf(cantidadPosts));
//            binding.favsNum.setText(String.valueOf(cantidadFavs));
//        }
//        // progress.setVisible(false);
//
//    }
//}
}