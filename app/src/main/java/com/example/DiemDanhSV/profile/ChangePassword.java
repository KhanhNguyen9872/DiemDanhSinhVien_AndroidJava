package com.example.DiemDanhSV.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.lap23.R;

public class ChangePassword extends AppCompatActivity {
    private SinhVienSQLite sinhVienSQLite;
    private int accountId = -1;
    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        this.accountId = intent.getIntExtra("accountId", -1);
        if (this.accountId == -1) {
            finish();
        }

        sinhVienSQLite = SinhVienSQLite.getInstance();

        EditText currentPassword = findViewById(R.id.currentPassword);
        EditText newPassword = findViewById(R.id.newPassword);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            if (currentPassword.getText().toString().isEmpty() ||
                    newPassword.getText().toString().isEmpty() ||
                    confirmPassword.getText().toString().isEmpty()) {
                showOkDialog("Lỗi", "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                showOkDialog("Lỗi", "Mật khẩu mới và xác nhận mật khẩu không khớp");
                return;
            }

            if (newPassword.getText().toString().length() < 6) {
                showOkDialog("Lỗi", "Mật khẩu mới phải có ít nhất 6 ký tự");
                return;
            }

            if (newPassword.getText().toString().equals(currentPassword.getText().toString())) {
                showOkDialog("Lỗi", "Mật khẩu mới không được trùng với mật khẩu hiện tại");
                return;
            }

            if (!sinhVienSQLite.checkPassword(this.accountId, currentPassword.getText().toString())) {
                showOkDialog("Lỗi", "Mật khẩu hiện tại không đúng");
                return;
            }

            sinhVienSQLite.changePassword(this.accountId, newPassword.getText().toString());

            this.isFinished = true;
            showOkDialog("Thành công", "Đổi mật khẩu thành công");
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
                if (isFinished) {
                    finish();
                }
            }
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}