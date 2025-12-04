package com.mycompany.hudoor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class HudoorSystem {
    //done by Waad Alshehri , ID: 2306782
    private static final String ATTENDANCE_FILE = "Attendance.csv";

    private double absenceThresholdPercent = 10.0;

    Map<String, Teacher> teachersById = new HashMap<>();
    Map<String, SchoolClass> classesById = new HashMap<>();
    Map<String, Student> studentsById = new HashMap<>();
    Map<String, List<AttendanceRecord>> attendanceByStudent = new HashMap<>();

    public HudoorSystem() {
        loadAttendanceFromFile();
    }

    public void loadAttendanceFromFile() {
        File f = new File(ATTENDANCE_FILE);
        if (!f.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length < 4) {
                    continue;
                }

                String studentId = parts[0];
                String classId = parts[1];
                LocalDate date = LocalDate.parse(parts[2]);
                AttendanceRecord.Status status = AttendanceRecord.Status.valueOf(parts[3]);

                AttendanceRecord record = new AttendanceRecord(
                        UUID.randomUUID().toString(),
                        studentId,
                        classId,
                        date,
                        status
                );

                attendanceByStudent
                        .computeIfAbsent(studentId, k -> new ArrayList<>())
                        .add(record);
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
        }
    }

    private void appendAttendanceToFile(AttendanceRecord record) {
        try (FileWriter fw = new FileWriter(ATTENDANCE_FILE, true);
             PrintWriter out = new PrintWriter(fw)) {

            out.println(record.getStudentId() + ";" +
                        record.getClassId() + ";" +
                        record.getDate().toString() + ";" +
                        record.getStatus().name());
        } catch (IOException e) {
            System.out.println("Error writing attendance file: " + e.getMessage());
        }
    }

    public void addTeacher(Teacher t) { teachersById.put(t.getUserId(), t); }
    public void addClass(SchoolClass c) { classesById.put(c.getClassId(), c); }
    public void addStudent(Student s) { studentsById.put(s.getStudentId(), s); }

    public SchoolClass getClassById(String classId) { return classesById.get(classId); }
    public Collection<SchoolClass> getAllClasses() { return classesById.values(); }

    //done by Lama alghamdi ID:2308040
    public boolean verifyUser(String userId, String password) {
        Teacher teacher = teachersById.get(userId);
        if (teacher == null) return false;
        return teacher.getPassword().equals(password);
    }

    public void recordAttendance(String classId,
                                 Map<String, AttendanceRecord.Status> mapStudentStatus) {
        LocalDate today = LocalDate.now();

        for (Map.Entry<String, AttendanceRecord.Status> entry : mapStudentStatus.entrySet()) {
            String studentId = entry.getKey();
            AttendanceRecord.Status status = entry.getValue();

            AttendanceRecord record = new AttendanceRecord(
                    UUID.randomUUID().toString(),
                    studentId,
                    classId,
                    today,
                    status
            );

            attendanceByStudent
                    .computeIfAbsent(studentId, k -> new ArrayList<>())
                    .add(record);

            appendAttendanceToFile(record);

            markAtRiskIfNeeded(studentId, classId);
        }
    }
    //done by Leena Tayeb
    public float computeAbsencePercent(String studentId, String classId) {
        List<AttendanceRecord> records = attendanceByStudent.get(studentId);
        if (records == null || records.isEmpty()) return 0f;

        int absentCount = 0;
        for (AttendanceRecord r : records) {
            if (r.getClassId().equals(classId) &&
                r.getStatus() == AttendanceRecord.Status.ABSENT) {
                absentCount++;
            }
        }

        float percent = (absentCount / 7.0f) * 10.0f;

        if (percent > 10.0f) {
            percent = 10.0f;
        }

        return percent;
    }
    //done by Hala Mehyar
    public boolean markAtRiskIfNeeded(String studentId, String classId) {
        Student student = studentsById.get(studentId);
        if (student == null) return false;

        List<AttendanceRecord> records = attendanceByStudent.get(studentId);
        if (records == null) return false;

        int absentCount = 0;
        for (AttendanceRecord r : records) {
            if (r.getClassId().equals(classId) &&
                r.getStatus() == AttendanceRecord.Status.ABSENT) {
                absentCount++;
            }
        }
        if (absentCount >= 7) {
            student.setFailed(true);
            student.setAtRisk(true);

            float percent = computeAbsencePercent(studentId, classId);

            sendParentNotification(
                    studentId,
                    "Your child " + student.getFullName()
                            + " has reached about " + String.format("%.1f", percent)
                            + "% absence (FAILED - exceeded 10%)."
            );
            return true;
        }

        if (absentCount == 5 || absentCount == 6) {
            student.setAtRisk(true);

            float percent = computeAbsencePercent(studentId, classId);

            sendParentNotification(
                    studentId,
                    "Warning: Your child " + student.getFullName()
                            + " has reached about " + String.format("%.1f", percent)
                            + "% absence (" + absentCount + " absences, close to 10%)."
            );
            return true;
        }
        return false;
    }

    //done by Waad Alshehri
    public void sendParentNotification(String studentId, String message) {
        Student s = studentsById.get(studentId);
        if (s == null) return;

        Parent p = s.getParent();
        if (p == null) return;

        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                message,
                LocalDateTime.now(),
                "NEW",
                s,
                p
        );

        notification.sendSMS(p.getPhoneNum());
    }
    //done by Alya Jaad , ID : 2308524
    public Report generateReport(String classId, LocalDate from, LocalDate to) {
        SchoolClass sc = classesById.get(classId);

        if (sc == null) {
            return new Report(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now(),
                    classId,
                    from,
                    to,
                    null,
                    Collections.emptyList(),
                    Collections.emptyMap()
            );
        }

        List<Student> absentStudents = new ArrayList<>();
        Map<String, Float> percentMap = new HashMap<>();

        for (Student s : sc.getStudents()) {
            String studentId = s.getStudentId();

            float percent = computeAbsencePercent(studentId, classId);
            percentMap.put(studentId, percent);

            List<AttendanceRecord> records = attendanceByStudent.get(studentId);
            if (records == null) continue;

            boolean wasAbsentInRange = false;
            for (AttendanceRecord r : records) {
                if (!r.getClassId().equals(classId)) continue;

                if (!r.getDate().isBefore(from) && !r.getDate().isAfter(to)) {
                    if (r.getStatus() == AttendanceRecord.Status.ABSENT) {
                        wasAbsentInRange = true;
                        break;
                    }
                }
            }

            if (wasAbsentInRange) {
                absentStudents.add(s);
            }
        }

        return new Report(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                classId,
                from,
                to,
                null,
                absentStudents,
                percentMap
        );
    }

    public void writeAudit(String action, String userId, String details) {
        System.out.println("AUDIT: " + action + " by " + userId + " | " + details);
    }
}





