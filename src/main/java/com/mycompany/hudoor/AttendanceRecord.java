//done by Alya Jaad, ID : 2308524
package com.mycompany.hudoor;

import java.time.LocalDate;

public class AttendanceRecord {

    private String attendanceId;
    private String studentId;
    private String classId;
    private LocalDate date;
    public enum Status { PRESENT, ABSENT }
    private Status status;

    public AttendanceRecord(String attendanceId, String studentId, String classId,
                            LocalDate date, Status status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.classId = classId;
        this.date = date;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getClassId() {
        return classId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}


