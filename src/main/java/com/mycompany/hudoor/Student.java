//done by Hala Mehyar ID:2331342
package com.mycompany.hudoor;


public class Student {

    private String studentId;  
    private String fullName;
    private String grade;
    private boolean atRisk;
    private boolean failed;
    private Parent parent;

    public Student(String studentId, String fullName, String grade, Parent parent) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.grade = grade;
        this.parent = parent;
        this.atRisk = false;
        this.failed = false;   
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGrade() {
        return grade;
    }

    public boolean isAtRisk() {
        return atRisk;
    }

    public void setAtRisk(boolean atRisk) {
        this.atRisk = atRisk;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public Parent getParent() {
        return parent;
    }
}

