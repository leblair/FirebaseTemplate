package com.example.firebasetemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.firebasetemplate.databinding.ActivityMainBinding;
import com.example.firebasetemplate.databinding.NavHeaderMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private NavHeaderMainBinding navHeaderMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());
        navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        FirebaseFirestore.getInstance().setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build());

        setSupportActionBar(binding.toolbar);

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.postsHomeFragment, R.id.postsLikeFragment, R.id.postsMyFragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId() == R.id.signInFragment) {
                binding.toolbar.setVisibility(View.GONE);
                binding.bottomNavView.setVisibility(View.GONE);
            } else {
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.bottomNavView.setVisibility(View.VISIBLE);
            }
        });

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).circleCrop().into(navHeaderMainBinding.photo);
                navHeaderMainBinding.name.setText(firebaseAuth.getCurrentUser().getDisplayName());
                navHeaderMainBinding.email.setText(firebaseAuth.getCurrentUser().getEmail());
                Log.e("sdfdfs","USER:" + firebaseAuth.getCurrentUser().getEmail());
            }
        });

        /*String str = "111111111111111111" +
                "111111001111101111" +
                "111111001111001111" +
                "111111100111101111" +
                "111111110111101111" +
                "111111110111001111" +
                "111111110111011111" +
                "111111111110111111" +
                "111111111111111111" +
                "111111111111111111" +
                "111111111111101111" +
                "111011111111111011" +
                "111111111111111111" +
                "111111111111111111" +
                "110111111111111111" +
                "111111111111111111" +
                "111111111111110111" +
                "111111111111101111" +
                "111111111100011111" +
                "111111111100111111" +
                "111111111001111111" +
                "111111000000111111" +
                "111111000000111111" +
                "111111000000111111" +
                "111111100000111111" +
                "111111111101111111" +
                "111111110001111111" +
                "111111110001111111" +
                "111111100000111111" +
                "111111111100111111" +
                "111111111110111111" +
                "111111110011111111" +
                "111111110001111111" +
                "111111110001111111" +
                "111111110001111111" +
                "111111110001111111" +
                "111111110001111111" +
                "100000000011111111" +
                "110000000111111111" +
                "111111111111111111";
        String[] arrayString= str.split("");

        byte[] foto2= new byte[arrayString.length];
        for (int i =0;i<arrayString.length;i++){
            foto2[i] = Byte.parseByte(arrayString[i]);
        }
        for(Byte b: foto2){
            System.out.println(b);
        }
        FirebaseStorage.getInstance().getReference("sentret.jpg").putBytes(foto2);*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}