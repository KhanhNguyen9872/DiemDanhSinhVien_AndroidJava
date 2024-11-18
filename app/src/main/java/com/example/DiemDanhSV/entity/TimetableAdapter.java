package com.example.DiemDanhSV.entity;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.DiemDanhSV.home.TimetableInfo;
import com.example.lap23.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ClassViewHolder> {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private List<Timetable> subjectList;
    private int classId = -1;

    public TimetableAdapter(List<Timetable> subjectList, int classId) {
        this.subjectList = subjectList;
        this.classId = classId;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Timetable timetable = subjectList.get(position);

        SinhVienSQLite sinhVienSQLite = SinhVienSQLite.getInstance();

        Professor professor = sinhVienSQLite.getProfessorByID(timetable.getProfessorId());

        holder.className.setText(timetable.getName());
        holder.teacherName.setText(professor.getFullName());
        holder.startAt.setText(timetable.getStartAt());
        holder.endAt.setText(timetable.getEndAt());
        holder.roomName.setText(timetable.getRoomName());

        int dayOfWeek = timetable.getDayOfWeek();
        String dayOfWeekString;
        if (dayOfWeek == 1)
            dayOfWeekString = "Thứ hai";
        else if (dayOfWeek == 2)
            dayOfWeekString = "Thứ ba";
        else if (dayOfWeek == 3)
            dayOfWeekString = "Thứ tư";
        else if (dayOfWeek == 4)
            dayOfWeekString = "Thứ năm";
        else if (dayOfWeek == 5)
            dayOfWeekString = "Thứ sáu";
        else if (dayOfWeek == 6)
            dayOfWeekString = "Thứ bảy";
        else
            dayOfWeekString = "Chủ nhật";

        Date day = timetable.getCurrentDayOfWeek();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(day);

        holder.date.setText(dayOfWeekString + ", " + dateString);

        Date thoiGianHienTai = new Date();

        int hour = 0, minute = 0, second = 0;
        LocalTime startTime = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startTime = LocalTime.parse(timetable.getStartAt());
            hour = startTime.getHour();
            minute = startTime.getMinute();
            second = startTime.getSecond();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        startCalendar.setTime(day);
        startCalendar.set(Calendar.HOUR_OF_DAY, hour); // 10 AM
        startCalendar.set(Calendar.MINUTE, minute); // 30 minutes
        startCalendar.set(Calendar.SECOND, second); // 0 seconds

        Date thoiGianBatDau = startCalendar.getTime();

        LocalTime endTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endTime = LocalTime.parse(timetable.getEndAt());
            hour = endTime.getHour();
            minute = endTime.getMinute();
            second = endTime.getSecond();
        }

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        endCalendar.setTime(day);
        endCalendar.set(Calendar.HOUR_OF_DAY, hour); // 10 AM
        endCalendar.set(Calendar.MINUTE, minute); // 30 minutes
        endCalendar.set(Calendar.SECOND, second); // 0 seconds

        Date thoiGianKetThuc = endCalendar.getTime();

        int happening = 0;

        // So sanh thoi gian hien tai voi thoi gian bat dau va ket thuc
        if (thoiGianHienTai.after(thoiGianBatDau) && thoiGianHienTai.before(thoiGianKetThuc)) {
            happening = 1;
        } else if (thoiGianHienTai.after(thoiGianBatDau)) {
            happening = 2;
        }

        if (happening == 1) {
            holder.status.setText("Trạng thái: (Đang diễn ra)");
            holder.cartItem.setBackgroundColor(Color.parseColor("#00ff00"));
        } else if (happening == 2) {
            holder.status.setText("Trạng thái: (Đã kết thúc)");
            holder.cartItem.setBackgroundColor(Color.parseColor("#f8baba"));
        } else {
            holder.status.setText("Trạng thái: (Chưa diễn ra)");
            holder.cartItem.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }

        final int happening2 = happening;

        holder.cartItem.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), TimetableInfo.class);
            intent.putExtra("timetableId", timetable.getId());
            intent.putExtra("classId", classId);
            intent.putExtra("happening", happening2);
            intent.putExtra("dateString", dateString);

            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cartItem;
        TextView className, status, teacherName, startAt, endAt, roomName, date;

        public ClassViewHolder(View itemView) {
            super(itemView);
            cartItem = itemView.findViewById(R.id.cartItem);

            className = itemView.findViewById(R.id.className);
            status = itemView.findViewById(R.id.status);
            teacherName = itemView.findViewById(R.id.classDetails);
            startAt = itemView.findViewById(R.id.timeStartAt);
            endAt = itemView.findViewById(R.id.timeEndAt);
            roomName = itemView.findViewById(R.id.roomName);
            date = itemView.findViewById(R.id.date);
        }
    }
}
