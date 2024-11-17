package com.example.DiemDanhSV;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.DiemDanhSV.entity.Class;
import com.example.DiemDanhSV.entity.Professor;
import com.example.DiemDanhSV.entity.Student;
import com.example.DiemDanhSV.entity.Timetable;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SinhVienSQLite extends SQLiteOpenHelper {
    private static SinhVienSQLite instance = null;
    private static final String DATABASE_NAME = "DiemDanhSinhVien.db";
    private static final int DATABASE_VERSION = 1;

    private final String STUDENT_TABLE_NAME = "student";
    private final String ACCOUNT_TABLE_NAME = "account";
    private final String CLASS_TABLE_NAME = "class";
    private final String TIMETABLE_TABLE_NAME = "timetable";
    private final String PROFESSOR_TABLE_NAME = "professor";

    private SinhVienSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SinhVienSQLite getInstance(Context context) {
        if (instance == null) {
            instance = new SinhVienSQLite(context);
        }
        return instance;
    }

    public static SinhVienSQLite getInstance() {
        return getInstance(null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CLASS_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT);"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PROFESSOR_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullName TEXT, " +
                "gender INTEGER, " +
                "email TEXT);"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TIMETABLE_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "professorId INTEGER, " +
                "classId INTEGER, " +
                "startAt TIME, " +
                "endAt TIME, " +
                "dayOfWeek INTEGER, " +
                "roomName TEXT, " +
                "dateStart DATE, " +
                "dateEnd DATE, " +
                "FOREIGN KEY(professorId) REFERENCES " + PROFESSOR_TABLE_NAME + "(id), " +
                "FOREIGN KEY(classId) REFERENCES " + CLASS_TABLE_NAME + "(id)" +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "gender INTEGER, " +
                "img TEXT, " +
                "classId INTEGER, " +
                "FOREIGN KEY(classId) REFERENCES " + CLASS_TABLE_NAME + "(id));"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "studentId INTEGER, " +
                "FOREIGN KEY(studentId) REFERENCES " + STUDENT_TABLE_NAME + "(id)" +
                ");"
        );
    }

    private void prepareDataClass() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CLASS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            insertClass("221402");
        }
        cursor.close();
    }

    private void prepareDataStudent() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(STUDENT_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            insertStudent("Khánh", "Nguyễn", 1, 1);
        }
        cursor.close();
    }

    private void prepareDataAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ACCOUNT_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            insertAccount("root", "root", 1);
        }
        cursor.close();
    }

    private void prepareDataTimetable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TIMETABLE_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            insertTimetable("Anh văn chuyên ngành", 1, 1, "13:15", "16:45", 1, "E308", "09/09/2024", "18/11/2024");
            insertTimetable("Công cụ và môi trường phát triển ứng dụng", 2, 1, "13:15", "16:45", 2, "E202", "10/09/2024", "19/11/2024");
            insertTimetable("Lập trình cho thiết bị di động", 3, 1, "13:15", "16:45", 3, "B309", "11/09/2024", "20/11/2024");
            insertTimetable("Lập trình ứng dụng với Java", 4, 1, "13:15", "16:45", 4, "E208", "12/09/2024", "21/11/2024");
            insertTimetable("Phong cách làm việc chuyên nghiệp", 5, 1, "13:15", "16:45", 5, "B402", "13/09/2024", "22/11/2024");
            insertTimetable("Thiết kế và xây dựng phần mềm", 6, 1, "13:15", "16:45", 6, "E304", "14/09/2024", "23/11/2024");
        }
        cursor.close();
    }

    private void prepareDataProfessor() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PROFESSOR_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            insertProfessor("Ngô Kim Phượng", 0, "ngokimphuong@localhost.com");
            insertProfessor("Châu Trần Trúc Ly", 0, "chautrantrucly@localhost.com");
            insertProfessor("Tạ Chí Qui Nhơn", 0, "tachiquinhon@localhost.com");
            insertProfessor("Nguyễn Thành Sơn", 1, "nguyentheson@localhost.com");
            insertProfessor("Nguyễn Thị Thu Hà", 0, "nguyenthithuha@localhost.com");
            insertProfessor("Lê Huỳnh Phước", 1, "lehuyenphuoc@localhost.com");
        }
        cursor.close();
    }

    public void initData() {
        prepareDataClass();
        prepareDataProfessor();
        prepareDataTimetable();
        prepareDataStudent();
        prepareDataAccount();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        onCreate(db);
    }

    public void insertStudent(String firstName, String lastName, int gender, int classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("gender", gender);
        values.put("classId", classId);
        db.insert(STUDENT_TABLE_NAME, null, values);
        db.close();
    }

    public void updateStudent(int id, String firstName, String lastName, String img, int gender, int classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("gender", gender);
        values.put("img", img);
        values.put("classId", classId);
        db.update(STUDENT_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STUDENT_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void insertAccount(String username, String password, int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("studentId", studentId);
        db.insert(ACCOUNT_TABLE_NAME, null, values);
        db.close();
    }

    public void updateAccount(int id, String username, String password, int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("studentId", studentId);
        db.update(ACCOUNT_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAccount(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNT_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int loginAccount(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ACCOUNT_TABLE_NAME,
                new String[]{"id", "username"},
                "username = ? AND password = ?",
                new String[]{username, password},
                null, null, null
        );

        int id = -1;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            if (name.equals(username)) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
        }
        cursor.close();
        db.close();
        return id;
    }

    public void insertClass(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert(CLASS_TABLE_NAME, null, values);
        db.close();
    }

    public void insertProfessor(String fullName, int gender, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", fullName);
        values.put("gender", gender);
        values.put("email", email);
        db.insert(PROFESSOR_TABLE_NAME, null, values);
        db.close();
    }

    public void insertTimetable(String name, int professorId, int classId, String startAt, String endAt, int dayOfWeek, String roomName, String dateStart, String dateEnd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("professorId", professorId);
        values.put("classId", classId);
        values.put("startAt", startAt);
        values.put("endAt", endAt);
        values.put("dayOfWeek", dayOfWeek);
        values.put("roomName", roomName);
        values.put("dateStart", dateStart);
        values.put("dateEnd", dateEnd);
        db.insert(TIMETABLE_TABLE_NAME, null, values);
        db.close();
    }

    public Class getInfoClassByID(int id) {
        Class aClass = new Class();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                CLASS_TABLE_NAME,
                new String[]{"name"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            aClass.setId(id);
            aClass.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }

        return aClass;
    }

    public Professor getProfessorByID(int id) {
        Professor professor = new Professor();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                PROFESSOR_TABLE_NAME,
                new String[]{"fullName", "gender", "email"},
                "id = ?",
                new String[]{String.valueOf(id)},

                null, null, null
        );

        if (cursor.moveToFirst()) {
            professor.setId(id);
            professor.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("fullName")));
            professor.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender")));
            professor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        }

        return professor;
    }

    public Student getInfoStudentByID(int id) {
        Student student = new Student();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                STUDENT_TABLE_NAME,
                new String[]{"firstName", "lastName", "gender", "classId"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            student.setId(id);
            student.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            student.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            student.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender")));
            student.setClassId(cursor.getInt(cursor.getColumnIndexOrThrow("classId")));
        }

        return student;
    }

    public int getStudentIdByAccountId(int accountId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                ACCOUNT_TABLE_NAME,
                new String[]{"studentId"},
                "id = ?",
                new String[]{String.valueOf(accountId)},
                null, null, null
        );

        int studentId = -1;
        if (cursor.moveToFirst()) {
            studentId = cursor.getInt(cursor.getColumnIndexOrThrow("studentId"));
        }

        cursor.close();
        db.close();

        return studentId;
    }

    public int getClassIdByStudentId(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                STUDENT_TABLE_NAME,
                new String[]{"classId"},
                "id = ?",
                new String[]{String.valueOf(studentId)},
                null, null, null
        );
        int classId = -1;
        if (cursor.moveToFirst()) {
            classId = cursor.getInt(cursor.getColumnIndexOrThrow("classId"));
        }
        cursor.close();
        db.close();
        return classId;
    }

    public List<Timetable> getAllTimetable(int classId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        List<Timetable> timetableList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TIMETABLE_TABLE_NAME,
                null,
                "classId = ?",
                new String[]{String.valueOf(classId)},
                null, null, null
        );
        if (cursor.moveToFirst()) {
            do {
                try {
                    Timetable timetable = new Timetable();
                    timetable.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    timetable.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    timetable.setProfessorId(cursor.getInt(cursor.getColumnIndexOrThrow("professorId")));
                    timetable.setClassId(cursor.getInt(cursor.getColumnIndexOrThrow("classId")));
                    timetable.setStartAt(cursor.getString(cursor.getColumnIndexOrThrow("startAt")));
                    timetable.setEndAt(cursor.getString(cursor.getColumnIndexOrThrow("endAt")));
                    timetable.setDayOfWeek(cursor.getInt(cursor.getColumnIndexOrThrow("dayOfWeek")));
                    timetable.setRoomName(cursor.getString(cursor.getColumnIndexOrThrow("roomName")));
                    timetable.setDateStart(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("dateStart"))));
                    timetable.setDateEnd(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("dateEnd"))));
                    timetableList.add(timetable);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return timetableList;
    }
}
