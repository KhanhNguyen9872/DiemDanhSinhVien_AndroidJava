package com.example.DiemDanhSV.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DiemDanhSV.LoginActivity;
import com.example.DiemDanhSV.NotificationActivity;
import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.DiemDanhSV.entity.Student;
import com.example.lap23.R;

public class ProfileFragment extends Fragment {
    private SinhVienSQLite sinhVienSQLite;
    private int accountId;
    private int studentId;

    public ProfileFragment(int accountId, int studentId) {
        this.sinhVienSQLite = SinhVienSQLite.getInstance();

        this.accountId = accountId;
        this.studentId = studentId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView textView = view.findViewById(R.id.profileName);
        Student student = sinhVienSQLite.getInfoStudentByID(studentId);
        textView.setText(student.getFirstName() + " " + student.getLastName());

        LinearLayout thongTinCaNhan = view.findViewById(R.id.thongTinCaNhan);
        thongTinCaNhan.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), ProfileInfo.class);
            intent.putExtra("accountId", accountId);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        LinearLayout dangXuat = view.findViewById(R.id.dangXuat);
        dangXuat.setOnClickListener(v -> {
            Toast toast = Toast.makeText(this.getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this.getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        LinearLayout thongBao = view.findViewById(R.id.thongBao);
        thongBao.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), NotificationActivity.class);
            startActivity(intent);
        });

        return view;
    }
}