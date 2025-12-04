//done by Waad Alshehri, ID: 2306782
package com.mycompany.hudoor;

import java.time.LocalDateTime;

public class Notification {

    private String notificationId;
    private String message;
    private LocalDateTime DateTime; 
    private String status;
    private Student student;
    private Parent parent;

    public Notification(String notificationId, String message,
                        LocalDateTime DateTime, String status,
                        Student student, Parent parent) {
        this.notificationId = notificationId;
        this.message = message;
        this.DateTime = DateTime;
        this.status = status;
        this.student = student;
        this.parent = parent;
    }

    public void sendSMS(String phone) {
        parent.receiveSMS(message);
        this.status = "SENT";
    }

    public String getStatus() {
        return status;
    }
}

