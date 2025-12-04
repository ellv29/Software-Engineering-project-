//done by Leena Tayeb
package com.mycompany.hudoor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HudoorSystem system = new HudoorSystem();

        try {
            CsvLoader.loadTeachers("Template (3).csv", system);
            CsvLoader.loadStudentsAndClasses("Template (2).csv", system);
        } catch (IOException e) {
            System.out.println("Error loading CSV files: " + e.getMessage());
            return;
        }

        Scanner in = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("       Hudoor Attendance System     ");
        System.out.println("====================================");

        boolean loggedIn = false;
        String teacherId = null;

        for (int attempts = 0; attempts < 3 && !loggedIn; attempts++) {
            System.out.print("Enter Teacher ID: ");
            teacherId = in.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = in.nextLine().trim();

            if (system.verifyUser(teacherId, password)) {
                loggedIn = true;
                System.out.println("\nLogin successful. Welcome!");
            } else {
                System.out.println("Invalid ID or password. Please try again.");
            }
        }

        if (!loggedIn) {
            System.out.println("Too many failed attempts. Exiting...");
            return;
        }

        system.writeAudit("LOGIN", teacherId, "Teacher logged in");

        List<SchoolClass> classList = new ArrayList<>(system.getAllClasses());

        if (classList.isEmpty()) {
            System.out.println("No classes found in the system.");
            return;
        }

        System.out.println("\nAvailable Classes:");
        for (int i = 0; i < classList.size(); i++) {
            SchoolClass c = classList.get(i);
            System.out.println((i + 1) + ") " + c.getClassId() + " - " + c.getName() + " (" + c.getGrade() + ")");
        }

        int choice = -1;
        while (true) {
            System.out.print("\nSelect a class by number: ");
            String input = in.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= classList.size()) {
                    break;
                } else {
                    System.out.println("Please enter a valid number between 1 and " + classList.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        SchoolClass selectedClass = classList.get(choice - 1);
        String classId = selectedClass.getClassId();
        System.out.println("\nYou selected class: " + classId + " - " + selectedClass.getName());

        List<Student> students = selectedClass.getStudents();
        if (students.isEmpty()) {
            System.out.println("No students found in this class.");
            return;
        }

        System.out.println("\nStudents in this class:");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.println((i + 1) + ") " + s.getStudentId() + " - " + s.getFullName());
        }

        Map<String, AttendanceRecord.Status> mapStudentStatus = new HashMap<>();

        System.out.println("\nEnter attendance for each student:");
        System.out.println("Type P for Present, A for Absent (default = P).");

        for (Student s : students) {
            while (true) {
                System.out.print("Student " + s.getStudentId() + " - " + s.getFullName() + " (P/A): ");
                String ans = in.nextLine().trim().toUpperCase();

                if (ans.isEmpty() || ans.equals("P")) {
                    mapStudentStatus.put(s.getStudentId(), AttendanceRecord.Status.PRESENT);
                    break;
                } else if (ans.equals("A")) {
                    mapStudentStatus.put(s.getStudentId(), AttendanceRecord.Status.ABSENT);
                    break;
                } else {
                    System.out.println("Please enter only P or A.");
                }
            }
        }

        system.recordAttendance(classId, mapStudentStatus);
        system.writeAudit("ATTENDANCE_SUBMIT", teacherId, "Class: " + classId);

        System.out.println("\nAttendance saved successfully for class " + classId + ".");

        System.out.println("\nCurrent absence percentages for this class:");
        for (Student s : students) {
            system.markAtRiskIfNeeded(s.getStudentId(), classId);

            float percent = system.computeAbsencePercent(s.getStudentId(), classId);
            System.out.printf(" %.2f%% : (%s) - %s",
                    percent, s.getStudentId(), s.getFullName());

            if (s.isFailed()) {
                System.out.print("  [FAILED]");
            } else if (s.isAtRisk()) {
                System.out.print("  [AT RISK]");
            }

            System.out.println();
        }

        System.out.println("\nThank you for using Hudoor System.");
    }
}

