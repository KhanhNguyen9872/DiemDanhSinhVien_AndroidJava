package com.example.DiemDanhSV.entity;

public class Point {
    private int id;
    private int studentId;
    private int timetableId;
    private float midPoint;
    private float lastPoint;
    private float finalPoint;

    public Point() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


    public int getTimetableId() {
        return timetableId;
    }
    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public float getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(float midPoint) {
        this.midPoint = midPoint;
    }

    public float getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(float lastPoint) {
        this.lastPoint = lastPoint;
    }

    public float getFinalPoint() {
        return finalPoint;
    }

    public void setFinalPoint(float finalPoint) {
        this.finalPoint = finalPoint;
    }
}
