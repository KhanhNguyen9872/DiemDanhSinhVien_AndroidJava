package com.example.DiemDanhSV;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lap23.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SinhVienSQLite sinhVienSQLite = SinhVienSQLite.getInstance(this);
        sinhVienSQLite.initData();
        //

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        username.setText("root");
        password.setText("root");

        //
        Button loginBtn = findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(v -> {
            int id = sinhVienSQLite.loginAccount(username.getText().toString(), password.getText().toString());
            if (id != -1) {
                Toast toast = Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            } else {
                Toast toast = Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}