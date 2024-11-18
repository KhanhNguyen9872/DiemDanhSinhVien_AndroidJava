package com.example.DiemDanhSV.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.DiemDanhSV.entity.Professor;
import com.example.DiemDanhSV.entity.Timetable;
import com.example.lap23.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimetableInfo extends AppCompatActivity {
    private SinhVienSQLite sinhVienSQLite;
    private TextView timetableName;
    private TextView happening;
    private TextView roomName;
    private TextView time;
    private TextView startDate;
    private TextView professorInfo;
    private TextView rollCallStatus;
    private Button rollCallButton;

    public TimetableInfo() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable_info);

        sinhVienSQLite = SinhVienSQLite.getInstance();

        Intent intent = getIntent();
        int timetableId = intent.getIntExtra("timetableId", -1);
        int studentId = intent.getIntExtra("studentId", -1);
        int classId = intent.getIntExtra("classId", -1);

        if (timetableId != -1) {
            int happeningValue = intent.getIntExtra("happening", 0);
            //
            Timetable timetable = sinhVienSQLite.getTimetableById(classId, timetableId);
            Professor professor = sinhVienSQLite.getProfessorByID(timetable.getProfessorId());

            //
            timetableName = findViewById(R.id.timetableName);
            happening = findViewById(R.id.happening);
            roomName = findViewById(R.id.roomName);
            time = findViewById(R.id.time);
            startDate = findViewById(R.id.startDate);
            professorInfo = findViewById(R.id.professorInfo);
            rollCallStatus = findViewById(R.id.rollCallStatus);
            rollCallButton = findViewById(R.id.rollCallButton);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Match the input format
            String dateString = intent.getStringExtra("dateString");
            Date date = null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final Date date2 = date;
            rollCallButton.setOnClickListener(v -> {
                sinhVienSQLite.rollCall(date2, studentId, timetableId);

                if (sinhVienSQLite.isRollCall(date2, studentId, timetableId)) {
                    rollCallButton.setText("Bạn đã điểm danh");
                    rollCallStatus.setText("Bạn đã điểm danh");
                    rollCallStatus.setTextColor(getResources().getColor(R.color.primary_color));
                    rollCallButton.setEnabled(false);
                } else {
                    rollCallStatus.setText("Bạn chưa điểm danh");
                }
            });

            timetableName.setText(timetable.getName());
            if (happeningValue == 1) {
                happening.setText("(Đang diễn ra)");
                happening.setTextColor(getResources().getColor(R.color.primary_color));
                rollCallButton.setText("Điểm danh");
                rollCallButton.setBackgroundColor(getResources().getColor(R.color.primary_color));
                rollCallButton.setEnabled(true);
            } else if (happeningValue == 2) {
                happening.setText("(Đã kết thúc)");
                happening.setTextColor(getResources().getColor(R.color.red));
                rollCallButton.setText("Không thể điểm danh");
                rollCallButton.setBackgroundColor(Color.RED);
                rollCallButton.setEnabled(false);
            } else {
                happening.setText("(Chưa diễn ra)");
                happening.setTextColor(getResources().getColor(R.color.black));
                rollCallButton.setText("Không thể điểm danh");
                rollCallButton.setBackgroundColor(Color.RED);
                rollCallButton.setEnabled(false);
            }

            roomName.setText("Phòng: " + timetable.getRoomName());
            time.setText("Thời gian: Từ " + timetable.getStartAt() + " đến " + timetable.getEndAt());
            startDate.setText("Ngày học: " + dateString);
            professorInfo.setText((professor.getGender() == 1 ? "Thầy" : "Cô") + ": " + professor.getFullName() + "\nSố điện thoại: " + professor.getNumberPhone() + "\nEmail: " + professor.getEmail());

            if (sinhVienSQLite.isRollCall(date, studentId, timetableId)) {
                rollCallButton.setText("Bạn đã điểm danh");
                rollCallStatus.setText("Bạn đã điểm danh");
                rollCallStatus.setTextColor(getResources().getColor(R.color.primary_color));
                rollCallButton.setEnabled(false);
            } else {
                rollCallStatus.setText("Bạn chưa điểm danh");
            }

        } else {
            finish();
        }
    }
}