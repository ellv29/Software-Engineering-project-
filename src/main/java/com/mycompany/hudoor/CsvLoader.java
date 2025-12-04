//done by Alya Jaad , ID : 2308524
package com.mycompany.hudoor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvLoader {

    
    public static void loadTeachers(String filePath, HudoorSystem system) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length < 2) {
                    continue; 
                }

                String identificationId = parts[0].trim(); 
                String fullName         = parts[1].trim(); 

                if (identificationId.isEmpty() || fullName.isEmpty()) {
                    continue;
                }

                String password = identificationId;

                Teacher teacher = new Teacher(identificationId, fullName, password);
                system.addTeacher(teacher);
            }
        }
    }

    
    public static void loadStudentsAndClasses(String filePath, HudoorSystem system) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length < 5) {
                    continue;
                }

                String identificationId = parts[0].trim(); 
                String fullName         = parts[1].trim(); 
                String mobileNumber     = parts[2].trim(); 
                String className        = parts[3].trim(); 
                String sectionName      = parts[4].trim(); 

                if (identificationId.isEmpty() || fullName.isEmpty()) {
                    continue;
                }

                String parentName = "Parent of " + fullName;
                Parent parent = new Parent(parentName, mobileNumber);

                Student student = new Student(identificationId, fullName, sectionName, parent);
                system.addStudent(student);

                String classId = className + "-" + sectionName;

                SchoolClass schoolClass = system.getClassById(classId);
                if (schoolClass == null) {
                    schoolClass = new SchoolClass(classId, className, sectionName);
                    system.addClass(schoolClass);
                }

                schoolClass.addStudent(student);
            }
        }
    }
}


