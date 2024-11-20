package com.example.DiemDanhSV.toDoList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.lap23.R;

import java.util.ArrayList;
import java.util.List;


public class ToDoListFragment extends Fragment {
    private int studentId = -1;

    private EditText etNewTask;
    private Button btnAddTask;
    private RecyclerView rvTasks;
    private TaskAdapter taskAdapter;
    private List<String> taskList;

    public ToDoListFragment(int studentId) {
        this.studentId = studentId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        SinhVienSQLite sinhVienSQLite = SinhVienSQLite.getInstance();

        etNewTask = view.findViewById(R.id.etNewTask);
        btnAddTask = view.findViewById(R.id.btnAddTask);
        rvTasks = view.findViewById(R.id.rvTasks);

        taskList = new ArrayList<>();
        taskList.clear();
        List<String> newTaskList = sinhVienSQLite.getToDoList(this.studentId);
        for (String t:
                newTaskList) {
            taskList.add(t);
        }

        taskAdapter = new TaskAdapter(taskList, this.studentId);

        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTasks.setAdapter(taskAdapter);

        btnAddTask.setOnClickListener(v -> {
            String task = etNewTask.getText().toString().trim();
            if (!TextUtils.isEmpty(task)) {
                sinhVienSQLite.insertToDoList(task, this.studentId);

                taskList.clear();
                for (String t:
                        sinhVienSQLite.getToDoList(this.studentId)) {
                    taskList.add(t);
                }

                taskAdapter.notifyItemInserted(taskList.size() - 1);
                etNewTask.setText("");
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập công việc mới", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}