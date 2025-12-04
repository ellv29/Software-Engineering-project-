package com.mycompany.hudoor;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HudoorSystemTest {
    // done by Leena Tayeb
    private HudoorSystem system;
    private Student s1;
    private Student s2;
    private Parent p1;
    private Parent p2;
    private SchoolClass c1;

    @Before
    public void setUp() {
        File f = new File("Attendance.csv");
        if (f.exists()) {
            f.delete();
        }

        system = new HudoorSystem();

        Teacher t1 = new Teacher("T1", "Ali Ahmed", "1234");
        system.addTeacher(t1);

        p1 = new Parent("Father1", "0500000001");
        p2 = new Parent("Father2", "0500000002");

        s1 = new Student("S1", "Student One", "3/1", p1);
        s2 = new Student("S2", "Student Two", "3/1", p2);

        system.addStudent(s1);
        system.addStudent(s2);

        c1 = new SchoolClass("C1", "Class 3/1", "3/1");
        c1.addStudent(s1);
        c1.addStudent(s2);
        system.addClass(c1);
    }
    
    //done by Waad Alshehri
    @Test
    public void testRecordAttendanceAndComputeAbsencePercent() {
        Map<String, AttendanceRecord.Status> map = new HashMap<>();
        map.put("S1", AttendanceRecord.Status.PRESENT);
        map.put("S2", AttendanceRecord.Status.ABSENT);

        system.recordAttendance("C1", map);

        float p1Percent = system.computeAbsencePercent("S1", "C1");
        float p2Percent = system.computeAbsencePercent("S2", "C1");

        assertEquals(0.0f, p1Percent, 0.001);
        assertEquals(10f / 7f, p2Percent, 0.001);
    }

    //done by Lama alghamdi
    @Test
    public void testAtRiskAfterFiveAbsences() {
        for (int i = 0; i < 5; i++) {
            Map<String, AttendanceRecord.Status> map = new HashMap<>();
            map.put("S2", AttendanceRecord.Status.ABSENT);
            system.recordAttendance("C1", map);
        }

        assertTrue(s2.isAtRisk());
        assertFalse(s2.isFailed());
    }

    //done by Leena Tayeb
    @Test
    public void testFailedAfterSevenAbsences() {
        for (int i = 0; i < 7; i++) {
            Map<String, AttendanceRecord.Status> map = new HashMap<>();
            map.put("S2", AttendanceRecord.Status.ABSENT);
            system.recordAttendance("C1", map);
        }

        assertTrue(s2.isAtRisk());
        assertTrue(s2.isFailed());
    }
    
    //done by Hala Mehyar
    @Test
    public void testSendParentNotification() {
        String msg = "Test message for parent";

        system.sendParentNotification("S2", msg);

        assertNotNull(p2.getLastMessage());
        assertEquals(msg, p2.getLastMessage());
    }

    //done by Alya Jaad , ID:2308524
    @Test
    public void testGenerateReport() {
        Map<String, AttendanceRecord.Status> map1 = new HashMap<>();
        map1.put("S2", AttendanceRecord.Status.ABSENT);
        system.recordAttendance("C1", map1);

        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);

        Report r = system.generateReport("C1", from, to);

        List<Student> absentStudents = r.getAbsentStudents();
        assertEquals(1, absentStudents.size());
        assertEquals("S2", absentStudents.get(0).getStudentId());
    }
}

