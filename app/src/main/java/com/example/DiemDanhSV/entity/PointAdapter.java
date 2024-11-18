package com.example.DiemDanhSV.entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.lap23.R;

import java.util.List;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ClassViewHolder> {

    private List<Point> pointList;
    private int studentId;

    public PointAdapter(List<Point> pointList, int studentId) {
        this.pointList = pointList;
        this.studentId = studentId;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ketquahoctapcard, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Point point = pointList.get(position);

        SinhVienSQLite sinhVienSQLite = SinhVienSQLite.getInstance();

        Student student = sinhVienSQLite.getStudentById(studentId);

        Timetable timetable = sinhVienSQLite.getTimetableById(student.getClassId(), point.getTimetableId());

        holder.timetableName.setText(timetable.getName());
        holder.pointMid.setText("Giữa kì: " + point.getMidPoint() + "đ");
        if (point.getLastPoint() == -1) {
            holder.pointLast.setText("");
            holder.pointFinal.setText("");

            int percent = (int) (point.getMidPoint() / 10 * 50);
            holder.progressBar.setProgress(percent);
            holder.percent.setText("Hoàn thành: " + percent + "%");
        } else {
            holder.pointLast.setText("Cuối kì: " + point.getLastPoint() + "đ");
            holder.pointFinal.setText("Tổng: " + (point.getMidPoint() + point.getLastPoint()) / 2 + "đ");

            if (point.getMidPoint() == 0 && point.getLastPoint() == 0) {
                holder.progressBar.setProgress(0);
                holder.percent.setText("Hoàn thành: " + "0%");
            } else {
                float percent = (point.getMidPoint() + point.getLastPoint()) / 2 / 10 * 100;
                holder.progressBar.setProgress((int) percent);
                holder.percent.setText("Hoàn thành: " + percent + "%");
            }
        }


    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView timetableName, percent, pointMid, pointLast, pointFinal;

        public ClassViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressLearning);

            timetableName = itemView.findViewById(R.id.tvSubjectName1);
            percent = itemView.findViewById(R.id.tvProgressPercentage);
            pointMid = itemView.findViewById(R.id.pointMid);
            pointLast = itemView.findViewById(R.id.pointLast);
            pointFinal = itemView.findViewById(R.id.pointFinal);
        }
    }
}
