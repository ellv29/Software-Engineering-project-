//done by Lama alghamdi ID:2308040
package com.mycompany.hudoor;

public class Administrator extends User {

    private HudoorSystem system;

    public Administrator(String userId, String name, String password, HudoorSystem system) {
        super(userId, name, password);
        this.system = system;
    }
    
    public void viewReport(){
        
    }
    
}


