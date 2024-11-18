package com.example.DiemDanhSV.ketQuaHocTap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DiemDanhSV.HomeActivity;
import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.DiemDanhSV.entity.Point;
import com.example.DiemDanhSV.entity.PointAdapter;
import com.example.DiemDanhSV.entity.Student;
import com.example.DiemDanhSV.entity.Timetable;
import com.example.DiemDanhSV.entity.TimetableAdapter;
import com.example.lap23.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class KetQuaHocLapFragment extends Fragment {
    private SinhVienSQLite sinhVienSQLite;
    private List<Point> listPoint;
    private PointAdapter pointAdapter;
    private TextView khongCoKetQuaHocTap;
    private int accountId = -1;
    private int studentId = -1;

    public KetQuaHocLapFragment(int accountId, int studentId) {
        this.accountId = accountId;
        this.studentId = studentId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ket_qua_hoc_lap, container, false);
        khongCoKetQuaHocTap = view.findViewById(R.id.khongCoKetQuaHocTap);
        this.sinhVienSQLite = SinhVienSQLite.getInstance();

        listPoint = this.sinhVienSQLite.getAllPoint(this.studentId);
        if (listPoint.size() == 0) {
            khongCoKetQuaHocTap.setVisibility(View.VISIBLE);
        } else {
            khongCoKetQuaHocTap.setVisibility(View.GONE);
        }

        pointAdapter = new PointAdapter(listPoint, studentId);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pointAdapter);

        Spinner spinnerHocKi = view.findViewById(R.id.spinnerHocKi);
        List<String> arrayListHocKi = sinhVienSQLite.getAllSemester();

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, arrayListHocKi);

        // Specify the layout for dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        spinnerHocKi.setAdapter(adapter);

        // Set a listener to handle selection events
        spinnerHocKi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
                int semesterId = position + 1;

                 List<Timetable> timetableListFinal = new ArrayList<>();
                Student student = sinhVienSQLite.getStudentById(studentId);

                List<Timetable> timetableList = sinhVienSQLite.getAllTimetable(student.getClassId());

                for (Timetable timetable:
                     timetableList) {
                    if (timetable.getSemesterId() == semesterId) {
                        timetableListFinal.add(timetable);
                    }
                }

                listPoint.clear();
                List<Point> newListPoint = sinhVienSQLite.getAllPoint(studentId);
                for (Point point:
                     newListPoint) {
                    if (isInTimetable(timetableListFinal, point.getTimetableId())) {
                        listPoint.add(point);
                    }
                }

                if (listPoint.size() == 0) {
                    khongCoKetQuaHocTap.setVisibility(View.VISIBLE);
                } else {
                    khongCoKetQuaHocTap.setVisibility(View.GONE);
                }

                pointAdapter.notifyDataSetChanged();
            }

            private boolean isInTimetable(List<Timetable> timetableListFinal, int timetableId) {
                for (Timetable timetable:
                     timetableListFinal) {
                    if (timetable.getId() == timetableId) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle case where no item is selected
            }
        });

        return view;
    }
}