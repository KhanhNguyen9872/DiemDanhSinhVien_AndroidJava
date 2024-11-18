package com.example.DiemDanhSV;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.DiemDanhSV.entity.Class;
import com.example.DiemDanhSV.entity.Faculty;
import com.example.DiemDanhSV.entity.Point;
import com.example.DiemDanhSV.entity.Professor;
import com.example.DiemDanhSV.entity.Student;
import com.example.DiemDanhSV.entity.Timetable;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.security.auth.Subject;

public class SinhVienSQLite extends SQLiteOpenHelper {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SinhVienSQLite instance = null;
    private static final String DATABASE_NAME = "DiemDanhSinhVien.db";
    private static final int DATABASE_VERSION = 1;

    private final String STUDENT_TABLE_NAME = "student";
    private final String ACCOUNT_TABLE_NAME = "account";
    private final String CLASS_TABLE_NAME = "class";
    private final String TIMETABLE_TABLE_NAME = "timetable";
    private final String PROFESSOR_TABLE_NAME = "professor";
    private final String POINT_TABLE_NAME = "point";
    private final String SEMESTER_TABLE_NAME = "semester";
    private final String FACULTY_TABLE_NAME = "faculty";
    private final String ROLLCALL_TABLE_NAME = "rollcall";

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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FACULTY_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT "  +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SEMESTER_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "semester INTEGER" +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + CLASS_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT);"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PROFESSOR_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullName TEXT, " +
                "numberPhone TEXT, " +
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
                "semesterId INTEGER, " +
                "FOREIGN KEY(semesterId) REFERENCES " + SEMESTER_TABLE_NAME + "(id), " +
                "FOREIGN KEY(professorId) REFERENCES " + PROFESSOR_TABLE_NAME + "(id), " +
                "FOREIGN KEY(classId) REFERENCES " + CLASS_TABLE_NAME + "(id)" +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mssv INTEGER, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "numberPhone TEXT, " +
                "gender INTEGER, " +
                "img TEXT, " +
                "classId INTEGER, " +
                "facultyId INTEGER, " +
                "birthday DATE, " +
                "joinDay DATE, " +
                "FOREIGN KEY(classId) REFERENCES " + CLASS_TABLE_NAME + "(id));"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + POINT_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pointMid DOUBLE, " +
                "pointLast DOUBLE, " +
                "studentId INTEGER," +
                "timetableId INTEGER," +
                "FOREIGN KEY(studentId) REFERENCES " + STUDENT_TABLE_NAME + "(id)," +
                "FOREIGN KEY(timetableId) REFERENCES " + TIMETABLE_TABLE_NAME + "(id)" +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "studentId INTEGER, " +
                "FOREIGN KEY(studentId) REFERENCES " + STUDENT_TABLE_NAME + "(id)" +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ROLLCALL_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date DATE, " +
                "timetableId INTEGER, " +
                "studentId INTEGER, " +
                "FOREIGN KEY(timetableId) REFERENCES " + TIMETABLE_TABLE_NAME + "(id), " +
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
            Calendar calendar = Calendar.getInstance();

            // Set the desired date
            calendar.set(Calendar.YEAR, 2004);
            calendar.set(Calendar.MONTH, Calendar.FEBRUARY); // February is month 1 (0-based index)
            calendar.set(Calendar.DAY_OF_MONTH, 7);

            // Convert Calendar to Date
            Date birthday = calendar.getTime();

            calendar.set(Calendar.YEAR, 2022);
            calendar.set(Calendar.MONTH, Calendar.OCTOBER); // February is month 1 (0-based index)
            calendar.set(Calendar.DAY_OF_MONTH, 18);

            // Convert Calendar to Date
            Date joinDay = calendar.getTime();

            insertStudent( 22150129, "Khánh", "Nguyễn", "0937927513", 1, 1, 1, birthday, joinDay);
        }
        cursor.close();
    }

    private void prepareDataAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ACCOUNT_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            insertAccount("22150129", "khanhnguyen");
        }
        cursor.close();
    }

    private void prepareDataTimetable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TIMETABLE_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            insertTimetable("Mẫu thiết kế cho phần mềm", 6, 1, "13:15", "16:45", 1, "E102", "20/05/2024", "22/07/2024", 5);
            insertTimetable("Lập trình Web", 7, 1, "13:15", "16:45", 2, "E102", "21/05/2024", "23/07/2024", 5);
            insertTimetable("Lập trình trực quan", 8, 1, "13:15", "16:45", 3, "E102", "22/05/2024", "24/07/2024", 5);
            insertTimetable("Tư duy phản biện, tư duy tích cực và tư duy đổi mới sáng tạo", 9, 1, "13:15", "16:45", 4, "E102", "23/05/2024", "25/07/2024", 5);
            insertTimetable("Tư tưởng Hồ Chí Minh", 10, 1, "13:15", "16:45", 5, "E102", "24/05/2024", "28/06/2024", 5);

            insertTimetable("Anh văn chuyên ngành", 1, 1, "13:15", "16:45", 1, "E308", "09/09/2024", "18/11/2024", 6);
            insertTimetable("Công cụ và môi trường phát triển ứng dụng", 2, 1, "13:15", "16:45", 2, "E202", "10/09/2024", "19/11/2024", 6);
            insertTimetable("Lập trình cho thiết bị di động", 3, 1, "13:15", "16:45", 3, "B309", "11/09/2024", "20/11/2024", 6);
            insertTimetable("Lập trình ứng dụng với Java", 4, 1, "13:15", "16:45", 4, "E208", "12/09/2024", "21/11/2024", 6);
            insertTimetable("Phong cách làm việc chuyên nghiệp", 5, 1, "13:15", "16:45", 5, "B402", "13/09/2024", "22/11/2024", 6);
            insertTimetable("Thiết kế và xây dựng phần mềm", 6, 1, "13:15", "16:45", 6, "E304", "14/09/2024", "23/11/2024", 6);

            insertTimetable("Cấu trúc dữ liệu và giải thuật", 11, 1, "12:25", "15:55", 1, "E405", "15/01/2024", "22/04/2024", 4);
            insertTimetable("Hệ quản trị cơ sở dữ liệu", 12, 1, "12:25", "15:55", 2, "E306", "16/01/2024", "09/04/2024", 4);
            insertTimetable("Kỹ năng mềm", 13, 1, "12:25", "15:55", 3, "B105", "24/01/2024", "10/04/2024", 4);
            insertTimetable("Lịch sử Đảng Cộng sản Việt Nam", 16, 1, "12:25", "15:55", 4, "B105", "18/01/2024", "14/03/2024", 4);
            insertTimetable("Mạng máy tính", 15, 1, "12:25", "15:55", 5, "B105", "19/01/2024", "12/04/2024", 4);
            insertTimetable("Yêu cầu phần mềm", 14, 1, "12:25", "15:55", 6, "B105", "20/01/2024", "30/03/2024", 4);
        }
        cursor.close();
    }

    private void prepareDataProfessor() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PROFESSOR_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            insertProfessor("Ngô Kim Phượng", 0, "ngokimphuong@localhost.com", "0123456789");
            insertProfessor("Châu Trần Trúc Ly", 0, "chautrantrucly@localhost.com", "0123456789");
            insertProfessor("Tạ Chí Qui Nhơn", 0, "tachiquinhon@localhost.com", "0123456789");
            insertProfessor("Nguyễn Thành Sơn", 1, "nguyentheson@localhost.com", "0123456789");
            insertProfessor("Nguyễn Thị Thu Hà", 0, "nguyenthithuha@localhost.com", "0123456789");
            insertProfessor("Lê Huỳnh Phước", 1, "lehuyenphuoc@localhost.com", "0123456789");

            insertProfessor("Đinh Hoàng Gia", 1, "dinhhoanggia@localhost.com", "0123456789");
            insertProfessor("Đặng Quốc Phong", 1, "dangquocphong@localhost.com", "0123456789");
            insertProfessor("Nguyễn Minh Nhựt", 1, "nguyenminhnhut@localhost.com", "0123456789");
            insertProfessor("Nguyễn Thị Tuyết Thảo", 0, "nguyenthituyetthao@localhost.com", "0123456789");

            insertProfessor("Trịnh Đình Yến", 1, "trinhdinhyen@localhost.com", "0123456789");
            insertProfessor("Trần Hoài Thuận", 1, "tranhoaithuan@localhost.com", "0123456789");
            insertProfessor("Lê Ngọc Danh", 1, "lengocdanh@localhost.com", "0123456789");
            insertProfessor("Trương Bá Vĩnh", 1, "truongbavinh@localhost.com", "0123456789");
            insertProfessor("Hồ Quý Thuận", 1, "hoquythuan@localhost.com", "0123456789");
            insertProfessor("Trần Lam Hạnh", 0, "tranlamhanh@localhost.com", "0123456789");
        }
        cursor.close();
    }

    public void initData() {
        prepareDataFaculty();
        prepareDataSemester();
        prepareDataClass();
        prepareDataProfessor();
        prepareDataTimetable();
        prepareDataStudent();
        prepareDataPoint();
        prepareDataAccount();
        prepareDataRollCall();
    }

    private void prepareDataRollCall() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ROLLCALL_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            // insertRollCall(new Date(), 1, 1);
        }
        cursor.close();
    }

    private void insertRollCall(Date date, int studentId, int timetableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", dateFormat.format(date));
        values.put("studentId", studentId);
        values.put("timetableId", timetableId);
        db.insert(ROLLCALL_TABLE_NAME, null, values);
        db.close();
    }


    private void prepareDataFaculty() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(FACULTY_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            insertFaculty("Công nghệ thông tin");
            insertFaculty("Ngôn ngữ");
            insertFaculty("Đông phương học");
            insertFaculty("Marketing");
        }
        cursor.close();
    }

    private void insertFaculty(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert(FACULTY_TABLE_NAME, null, values);
        db.close();
    }

    private void prepareDataSemester() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SEMESTER_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            insertSemester("2022-2023", 1);
            insertSemester("2022-2023", 2);
            insertSemester("2023-2024", 1);
            insertSemester("2023-2024", 2);
            insertSemester("2023-2024", 3);
            insertSemester("2024-2025", 1);
            insertSemester("2024-2025", 2);
            insertSemester("2024-2025", 3);
        }
        cursor.close();
    }

    private void insertSemester(String name, int semester) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("semester", semester);
        db.insert(SEMESTER_TABLE_NAME, null, values);
        db.close();
    }

    private void prepareDataPoint() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(POINT_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            insertPoint(9, 8.5, 1, 1);
            insertPoint(9, 8.5, 1, 2);
            insertPoint(9.5, 9.5, 1, 3);
            insertPoint(8.5, 8.5, 1, 4);
            insertPoint(8.6, 7, 1, 5);

            insertPoint(7.5, -1, 1, 6);
            insertPoint(10, -1, 1, 7);
            insertPoint(9.8, -1, 1, 8);
            insertPoint(10, -1, 1, 9);
            insertPoint(-1, -1, 1, 10);
            insertPoint(8.5, -1, 1, 11);

            insertPoint(8, 9.5, 1, 12);
            insertPoint(9.5, 8, 1, 13);
            insertPoint(9, 9.2, 1, 14);
            insertPoint(8, 7.5, 1, 15);
            insertPoint(10, 8.5, 1, 16);
            insertPoint(7.6, 7, 1, 17);
        }
        cursor.close();
    }

    private void insertPoint(double pointMid, double pointLast, int studentId, int timetableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pointMid", pointMid);
        values.put("pointLast", pointLast);
        values.put("studentId", studentId);
        values.put("timetableId", timetableId);
        db.insert(POINT_TABLE_NAME, null, values);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FACULTY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROFESSOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + POINT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SEMESTER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        onCreate(db);
    }

    public void insertStudent(int mssv, String firstName, String lastName, String numberPhone, int gender, int classId, int facultyId, Date birthday, Date joinDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mssv", mssv);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("numberPhone", numberPhone);
        values.put("gender", gender);
        values.put("classId", classId);
        values.put("facultyId", facultyId);
        values.put("birthday", dateFormat.format(birthday));
        values.put("joinDay", dateFormat.format(joinDay));
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

    public void insertAccount(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("studentId", 1);
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

    public int loginAccount(int mssv, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ACCOUNT_TABLE_NAME,
                new String[]{"id", "username"},
                "username = ? AND password = ?",
                new String[]{String.valueOf(mssv), password},
                null, null, null
        );

        int id = -1;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            if (name.equals(String.valueOf(mssv))) {
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

    public void insertProfessor(String fullName, int gender, String email, String numberPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", fullName);
        values.put("gender", gender);
        values.put("email", email);
        values.put("numberPhone", numberPhone);
        db.insert(PROFESSOR_TABLE_NAME, null, values);
        db.close();
    }

    public void insertTimetable(String name, int professorId, int classId, String startAt, String endAt, int dayOfWeek, String roomName, String dateStart, String dateEnd, int semesterId) {
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
        values.put("semesterId", semesterId);
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
                new String[]{"fullName", "gender", "numberPhone", "email"},
                "id = ?",
                new String[]{String.valueOf(id)},

                null, null, null
        );

        if (cursor.moveToFirst()) {
            professor.setId(id);
            professor.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("fullName")));
            professor.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender")));
            professor.setNumberPhone(cursor.getString(cursor.getColumnIndexOrThrow("numberPhone")));
            professor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        }

        return professor;
    }

    public Student getInfoStudentByID(int id) {
        Student student = new Student();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                STUDENT_TABLE_NAME,
                new String[]{"mssv", "firstName", "lastName", "numberPhone", "gender", "classId", "facultyId", "birthday", "joinDay"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            try {
                student.setId(id);
                student.setMssv(cursor.getInt(cursor.getColumnIndexOrThrow("mssv")));
                student.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
                student.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
                student.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("numberPhone")));
                student.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender")));
                student.setClassId(cursor.getInt(cursor.getColumnIndexOrThrow("classId")));
                student.setFacultyId(cursor.getInt(cursor.getColumnIndexOrThrow("facultyId")));
                student.setBirthday(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("birthday"))));
                student.setJoinDay(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("joinDay"))));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
                    timetable.setSemesterId(cursor.getInt(cursor.getColumnIndexOrThrow("semesterId")));
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

    public Timetable getTimetableById(int classId, int timetableId) {
        List<Timetable> timetableList = this.getAllTimetable(classId);

        for (Timetable timetable:
             timetableList) {
            if (timetable.getId() == timetableId) {
                return timetable;
            }
        }
        return null;
    }

    public List<Point> getAllPoint(int studentId) {
        List<Point> pointList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                POINT_TABLE_NAME,
                null,
                "studentId = ?",
                new String[]{String.valueOf(studentId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                try {
                    Point point = new Point();
                    point.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    point.setMidPoint(cursor.getFloat(cursor.getColumnIndexOrThrow("pointMid")));
                    point.setLastPoint(cursor.getFloat(cursor.getColumnIndexOrThrow("pointLast")));
                    point.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow("studentId")));
                    point.setTimetableId(cursor.getInt(cursor.getColumnIndexOrThrow("timetableId")));
                    pointList.add(point);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        return pointList;
    }

    public Student getStudentById(int studentId) {
        return this.getInfoStudentByID(studentId);
    }

    public List<String> getAllSemester() {
        List<String> semesterList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SEMESTER_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = "NH " + cursor.getString(cursor.getColumnIndexOrThrow("name")) + " - HK" + cursor.getString(cursor.getColumnIndexOrThrow("semester"));
                semesterList.add(name);
            } while (cursor.moveToNext());
        }

        return semesterList;
    }

    public List<Point> getAllPointBySemester(int studentId, int timetableId) {
        List<Point> listPoint = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                POINT_TABLE_NAME,
                null,
                "studentId = ? AND timetableId = ?",
                new String[]{String.valueOf(studentId), String.valueOf(timetableId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                Point point = new Point();
                point.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                point.setMidPoint(cursor.getFloat(cursor.getColumnIndexOrThrow("pointMid")));
                point.setLastPoint(cursor.getFloat(cursor.getColumnIndexOrThrow("pointLast")));
                point.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow("studentId")));
                point.setTimetableId(cursor.getInt(cursor.getColumnIndexOrThrow("timetableId")));
                listPoint.add(point);
            } while (cursor.moveToNext());
        }

        return listPoint;
    }

    public Faculty getInfoFacultyByID(int facultyId) {
        Faculty faculty = new Faculty();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                FACULTY_TABLE_NAME,
                new String[]{"name"},
                "id = ?",
                new String[]{String.valueOf(facultyId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            faculty.setId(facultyId);
            faculty.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }

        return faculty;
    }

    public boolean isRollCall(Date date, int studentId, int timetableId) {
        boolean check = false;
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = sdf.format(date);

        Cursor cursor = db.query(
                ROLLCALL_TABLE_NAME,
                null,
                "date = ? AND studentId = ? AND timetableId = ?",
                new String[]{dateString, String.valueOf(studentId), String.valueOf(timetableId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            check = true;
        }

        return check;
    }

    public void rollCall(Date date2, int studentId, int timetableId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = sdf.format(date2);

        System.out.println("date2: " + dateString);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", dateString);
        values.put("studentId", studentId);
        values.put("timetableId", timetableId);
        db.insert(ROLLCALL_TABLE_NAME, null, values);
        db.close();

    }

    public void changePassword(int accountId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        db.update(ACCOUNT_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(accountId)});
        db.close();
    }

    public boolean checkPassword(int accountId, String string) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ACCOUNT_TABLE_NAME,
                new String[]{"password"},
                "id = ?",
                new String[]{String.valueOf(accountId)},
                null, null, null
        );
        if (cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            if (password.equals(string)) {
                return true;
            }
            }
        return false;

    }
}
