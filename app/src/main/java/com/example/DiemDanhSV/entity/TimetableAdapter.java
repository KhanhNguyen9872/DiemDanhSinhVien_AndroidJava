package com.example.DiemDanhSV.entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.lap23.R;

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
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView className, teacherName;

        public ClassViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            teacherName = itemView.findViewById(R.id.classDetails);
        }
    }
}
