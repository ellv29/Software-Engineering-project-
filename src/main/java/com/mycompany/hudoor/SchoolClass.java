//done by Hala Mehyar
package com.mycompany.hudoor;

import java.util.ArrayList;
import java.util.List;

public class SchoolClass {

    private String classId;
    private String name;
    private String grade;
    private List<Student> student = new ArrayList<>();  

    public SchoolClass(String classId, String name, String grade) {
        this.classId = classId;
        this.name = name;
        this.grade = grade;
    }

    public String getClassId() {
        return classId;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public List<Student> getStudents() {
        return student;
    }

    public void addStudent(Student s) {
        student.add(s);
    }

    public void removeStudent(Student s) {
        student.remove(s);
    }
}

