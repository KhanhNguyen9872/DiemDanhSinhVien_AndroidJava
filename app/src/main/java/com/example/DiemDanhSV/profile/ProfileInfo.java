package com.example.DiemDanhSV.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.DiemDanhSV.entity.Class;
import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.DiemDanhSV.entity.Student;
import com.example.lap23.R;

public class ProfileInfo extends AppCompatActivity {
    private SinhVienSQLite sinhVienSQLite;
    private int accountId = -1;
    private int studentId = -1;
    private int classId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_info);

        Intent intent = getIntent();
        accountId = intent.getIntExtra("accountId", -1);
        studentId = intent.getIntExtra("studentId", -1);

        sinhVienSQLite = SinhVienSQLite.getInstance();
        Student student = sinhVienSQLite.getInfoStudentByID(studentId);

        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        EditText editTextClassName = findViewById(R.id.editTextClassName);
        RadioButton radioButtonBoy = findViewById(R.id.boy);
        RadioButton radioButtonGirl = findViewById(R.id.girl);

        Button changePasswordButton = findViewById(R.id.changePasswordButton);

        classId = student.getClassId();

        Class classInfo = sinhVienSQLite.getInfoClassByID(classId);

        editTextFirstName.setText(student.getFirstName());
        editTextLastName.setText(student.getLastName());
        editTextClassName.setText(classInfo.getName());
        if (student.getGender() == 1) {
            radioButtonBoy.setChecked(true);
            radioButtonGirl.setChecked(false);
        } else {
            radioButtonGirl.setChecked(true);
            radioButtonBoy.setChecked(false);
        }

    }
}