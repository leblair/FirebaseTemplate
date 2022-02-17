package com.example.firebasetemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebasetemplate.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


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