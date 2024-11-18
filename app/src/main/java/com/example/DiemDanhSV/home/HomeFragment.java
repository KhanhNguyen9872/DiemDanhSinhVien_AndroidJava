package com.example.DiemDanhSV.home;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.DiemDanhSV.SinhVienSQLite;
import com.example.DiemDanhSV.entity.Timetable;
import com.example.DiemDanhSV.entity.TimetableAdapter;
import com.example.lap23.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private TimetableAdapter timetableAdapter;
    private SinhVienSQLite sinhVienSQLite;
    private int accountId;
    private int studentId;
    private int classId;
    private List<Timetable> timetableList;

    public HomeFragment(int accountId, int studentId) {
        this.sinhVienSQLite = SinhVienSQLite.getInstance();
        this.accountId = accountId;
        this.studentId = studentId;
        this.classId = this.sinhVienSQLite.getClassIdByStudentId(studentId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button selectDateButton = view.findViewById(R.id.selectDateButton);
        TextView weekTextView = view.findViewById(R.id.weekTextView);
        TextView khongCoLichHoc = view.findViewById(R.id.khongCoLichHoc);

        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);  // Set Monday as the first day of the week

        // Get today's date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        selectDateButton.setOnClickListener(v -> {
            // Show DatePickerDialog
            new DatePickerDialog(this.getContext(), (view_, selectedYear, selectedMonth, selectedDay) -> {
                // Update calendar with selected date
                calendar.set(selectedYear, selectedMonth, selectedDay);

                // Calculate week number
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

                // Update TextView to show the week number
                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.MONDAY);
                c.set(Calendar.YEAR, year);
                c.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Get the first day of the week (Monday)

                Date startDate = c.getTime(); // Get the first day of the week
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                // Get the last day of the week (add 6 days)
                c.add(Calendar.DATE, 6);
                Date endDate = c.getTime();

                // Format the dates to display them in TextView
                String startDateStr = dateFormat.format(startDate);
                String endDateStr = dateFormat.format(endDate);

                // Display the formatted dates
                weekTextView.setText("Từ " + startDateStr + " đến " + endDateStr);

                Calendar currentWeekStart = Calendar.getInstance();
                currentWeekStart.setFirstDayOfWeek(Calendar.MONDAY);
                currentWeekStart.setTime(startDate);  // 09/12/2024

                Calendar currentWeekEnd = Calendar.getInstance();
                currentWeekEnd.setFirstDayOfWeek(Calendar.MONDAY);
                currentWeekEnd.setTime(endDate);   // 15/12/2024

                // Clear the timetable list and add subjects that fall within the week
                timetableList.clear();
                List<Timetable> newList = this.sinhVienSQLite.getAllTimetable(this.classId);
                for (Timetable timetable : newList) {
                    try {
                        Date startDateTimetable = timetable.getDateStart();
                        Date endDateTimetable = timetable.getDateEnd();

                        Calendar startTimetable = Calendar.getInstance();
                        startTimetable.setTime(startDateTimetable);

                        Calendar endTimetable = Calendar.getInstance();
                        endTimetable.setTime(endDateTimetable);

                        // Check if the subject is within the selected week using the new comparison method
                        if (isSubjectInWeek(startTimetable, endTimetable, currentWeekStart, currentWeekEnd)) {
                            Calendar c2 = Calendar.getInstance();
                            c2.setFirstDayOfWeek(Calendar.MONDAY);
                            c2.set(Calendar.YEAR, year);
                            c2.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                            c2.set(Calendar.DAY_OF_WEEK, c2.getFirstDayOfWeek());

                            c2.add(Calendar.DATE, timetable.getDayOfWeek() - 1);

                            Date currentDayOfWeek = c2.getTime();
                            timetable.setCurrentDayOfWeek(currentDayOfWeek);

                            timetableList.add(timetable);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (timetableList.size() == 0) {
                    khongCoLichHoc.setVisibility(View.VISIBLE);
                } else {
                    khongCoLichHoc.setVisibility(View.GONE);
                }

                this.timetableAdapter.notifyDataSetChanged();

            }, year, month, day).show();
        });

        // Example data
        timetableList = this.sinhVienSQLite.getAllTimetable(this.classId);

        timetableAdapter = new TimetableAdapter(timetableList);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(timetableAdapter);

        Date startDate = calendar.getTime(); // Get the first day of the week
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Get the last day of the week (add 6 days)
        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();

        // Format the dates to display them in TextView
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);

        // Display the formatted dates
        weekTextView.setText("Từ " + startDateStr + " đến " + endDateStr);

        Calendar currentWeekStart = Calendar.getInstance();
        currentWeekStart.setFirstDayOfWeek(Calendar.MONDAY);
        currentWeekStart.setTime(startDate);  // 09/12/2024

        Calendar currentWeekEnd = Calendar.getInstance();
        currentWeekEnd.setFirstDayOfWeek(Calendar.MONDAY);
        currentWeekEnd.setTime(endDate);   // 15/12/2024

        // Clear the timetable list and add subjects that fall within the week
        timetableList.clear();
        List<Timetable> newList = this.sinhVienSQLite.getAllTimetable(this.classId);
        for (Timetable timetable : newList) {
            try {
                Date startDateTimetable = timetable.getDateStart();
                Date endDateTimetable = timetable.getDateEnd();

                Calendar startTimetable = Calendar.getInstance();
                startTimetable.setTime(startDateTimetable);

                Calendar endTimetable = Calendar.getInstance();
                endTimetable.setTime(endDateTimetable);

                // Check if the subject is within the selected week using the new comparison method
                if (isSubjectInWeek(startTimetable, endTimetable, currentWeekStart, currentWeekEnd)) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, timetable.getDayOfWeek() - 1);

                    Date currentDayOfWeek = c.getTime();
                    timetable.setCurrentDayOfWeek(currentDayOfWeek);

                    timetableList.add(timetable);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (timetableList.size() == 0) {
            khongCoLichHoc.setVisibility(View.VISIBLE);
        } else {
            khongCoLichHoc.setVisibility(View.GONE);
        }

        this.timetableAdapter.notifyDataSetChanged();

        return view;
    }

    public boolean isSubjectInWeek(Calendar startTimetable, Calendar endTimetable, Calendar weekStart, Calendar weekEnd) {
        // Clear time from all calendars to avoid time components affecting the comparison
        clearTime(startTimetable);
        clearTime(endTimetable);
        clearTime(weekStart);
        clearTime(weekEnd);

        // Check if the timetable's start or end date falls within the week
        return (startTimetable.compareTo(weekStart) >= 0 && startTimetable.compareTo(weekEnd) <= 0) ||
                (endTimetable.compareTo(weekStart) >= 0 && endTimetable.compareTo(weekEnd) <= 0) ||
                (startTimetable.compareTo(weekStart) <= 0 && endTimetable.compareTo(weekEnd) >= 0);
    }

    private void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
