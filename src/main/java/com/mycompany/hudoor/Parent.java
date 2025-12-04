//done by Hala Mehyar
package com.mycompany.hudoor;


public class Parent {

    private String fullName;
    private String phoneNum;

    private String lastMessage;

    public Parent(String fullName, String phoneNum) {
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void receiveSMS(String message) {
        System.out.println("SMS to " + phoneNum + ": " + message);
        this.lastMessage = message;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}

