//done by Leena Tayeb
package com.mycompany.hudoor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Report {

    private String reportId;
    private LocalDateTime generatedAt;
    private String classId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String adminId;

    
    private List<Student> absentStudents;

    private Map<String, Float> absencePercentByStudent;

    public Report(String reportId,
                  LocalDateTime generatedAt,
                  String classId,
                  LocalDate fromDate,
                  LocalDate toDate,
                  String adminId,
                  List<Student> absentStudents,
                  Map<String, Float> absencePercentByStudent) {

        this.reportId = reportId;
        this.generatedAt = generatedAt;
        this.classId = classId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.adminId = adminId;
        this.absentStudents = absentStudents;
        this.absencePercentByStudent = absencePercentByStudent;
    }

    public String getReportId() {
        return reportId;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public String getClassId() {
        return classId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public String getAdminId() {
        return adminId;
    }

    public List<Student> getAbsentStudents() {
        return absentStudents;
    }

    public Map<String, Float> getAbsencePercentByStudent() {
        return absencePercentByStudent;
    }
}




