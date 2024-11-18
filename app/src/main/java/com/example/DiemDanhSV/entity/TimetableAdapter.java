package com.example.DiemDanhSV.entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.lap23.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ClassViewHolder> {

    private List<Timetable> subjectList;

    public TimetableAdapter(List<Timetable> subjectList) {
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Timetable subject = subjectList.get(position);

        SinhVienSQLite sinhVienSQLite = SinhVienSQLite.getInstance();

        Professor professor = sinhVienSQLite.getProfessorByID(subject.getProfessorId());

        holder.className.setText(subject.getName());
        holder.teacherName.setText(professor.getFullName());
        holder.startAt.setText(subject.getStartAt());
        holder.endAt.setText(subject.getEndAt());
        holder.roomName.setText(subject.getRoomName());

        int dayOfWeek = subject.getDayOfWeek();
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

        Date day = subject.getCurrentDayOfWeek();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(day);

        holder.date.setText(dayOfWeekString + ", " + dateString);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView className, teacherName, startAt, endAt, roomName, date;

        public ClassViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            teacherName = itemView.findViewById(R.id.classDetails);
            startAt = itemView.findViewById(R.id.timeStartAt);
            endAt = itemView.findViewById(R.id.timeEndAt);
            roomName = itemView.findViewById(R.id.roomName);
            date = itemView.findViewById(R.id.date);
        }
    }
}
