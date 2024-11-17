package com.example.DiemDanhSV;

// MainActivity.java
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.DiemDanhSV.home.HomeFragment;
import com.example.DiemDanhSV.profile.ProfileFragment;
import com.example.lap23.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {
    private Fragment homeFragment;
    private Fragment profileFragment;
    private int accountId;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SinhVienSQLite sinhVienSQLite = SinhVienSQLite.getInstance();
        Intent intent = getIntent();
        this.accountId = intent.getIntExtra("id", -1);
        this.studentId = sinhVienSQLite.getStudentIdByAccountId(accountId);

        homeFragment = new HomeFragment(this.accountId, this.studentId);
        profileFragment = new ProfileFragment(this.accountId, this.studentId);

        setContentView(R.layout.activity_homescreen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Trang chủ");
        loadFragment(homeFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    getSupportActionBar().setTitle("Trang chủ");
                    loadFragment(homeFragment);
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    getSupportActionBar().setTitle("Thông tin sinh viên");
                    loadFragment(profileFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
