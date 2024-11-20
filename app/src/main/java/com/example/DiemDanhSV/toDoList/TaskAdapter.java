package com.example.DiemDanhSV.toDoList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DiemDanhSV.SinhVienSQLite;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private SinhVienSQLite sinhVienSQLite;
    private int studentId = -1;
    private List<String> taskList;

    public TaskAdapter(List<String> taskList, int studentId) {
        this.taskList = taskList;
        this.studentId = studentId;
        this.sinhVienSQLite = SinhVienSQLite.getInstance();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String task = taskList.get(position);
        holder.taskTextView.setText(task);

        holder.itemView.setOnClickListener(v -> {
            sinhVienSQLite.deleteToDoList(task, this.studentId);
            try {
                taskList.remove(position);
                notifyItemRemoved(position);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            notifyItemRangeChanged(position, taskList.size());
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
