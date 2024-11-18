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
import com.example.DiemDanhSV.entity.Faculty;
import com.example.DiemDanhSV.entity.Student;
import com.example.lap23.R;

import java.text.SimpleDateFormat;

public class ProfileInfo extends AppCompatActivity {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

        EditText editTextMSSV = findViewById(R.id.editTextMSSV);
        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        EditText editTextPhoneNumber = findViewById(R.id.editTextNumberPhone);
        EditText editTextClassName = findViewById(R.id.editTextClassName);
        EditText editTextFaculty = findViewById(R.id.editTextFaculty);
        EditText editTextNgaySinh = findViewById(R.id.editTextNgaySinh);
        EditText editTextNgayVaoTruong = findViewById(R.id.editTextNgayVaoTruong);
        RadioButton radioButtonBoy = findViewById(R.id.boy);
        RadioButton radioButtonGirl = findViewById(R.id.girl);

        Button changePasswordButton = findViewById(R.id.changePasswordButton);

        classId = student.getClassId();

        Class classInfo = sinhVienSQLite.getInfoClassByID(classId);

        Faculty faculty = sinhVienSQLite.getInfoFacultyByID(student.getFacultyId());

        editTextMSSV.setText(String.valueOf(student.getMssv()));
        editTextFirstName.setText(student.getFirstName());
        editTextLastName.setText(student.getLastName());
        editTextPhoneNumber.setText(student.getPhoneNumber());
        editTextClassName.setText(classInfo.getName());
        editTextFaculty.setText(faculty.getName());
        editTextNgaySinh.setText(dateFormat.format(student.getBirthday()));
        editTextNgayVaoTruong.setText(dateFormat.format(student.getJoinDay()));

        if (student.getGender() == 1) {
            radioButtonBoy.setChecked(true);
            radioButtonGirl.setChecked(false);
        } else {
            radioButtonGirl.setChecked(true);
            radioButtonBoy.setChecked(false);
        }

    }
}