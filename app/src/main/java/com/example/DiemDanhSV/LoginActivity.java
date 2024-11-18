package com.example.DiemDanhSV;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        username.setText("22150129");
        password.setText("khanhnguyen");

        //
        Button loginBtn = findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(v -> {
            if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                showOkDialog("Lỗi", "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            try {
                Integer.parseInt(username.getText().toString());
            } catch (Exception ex) {
                showOkDialog("Lỗi", "MSSV phải là số");
                return;
            }

            int id = sinhVienSQLite.loginAccount(Integer.parseInt(username.getText().toString()), password.getText().toString());
            if (id != -1) {
                Toast toast = Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            } else {
                showOkDialog("Lỗi", "Tài khoản hoặc mật khẩu không đúng");
            }
        });
    }

    private void showOkDialog(String title, String msg) {
        // Create AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set title and message
        builder.setTitle(title);
        builder.setMessage(msg);

        // Set OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action when OK is clicked (dismiss the dialog)
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}