/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.fileManager;

/**
 *
 * @author John PIlon
 */
public abstract class Command{
    protected static final String validPassword = "A^@kds034!@$#sjvlksoirg%#$%^#@()sdgnverkf15681165";
    protected String password;
    
    /*Checks if the packet is valid when its recieved from the other side*/
    public boolean isValid(){return password.equals(validPassword);}
    
    @Override
    public abstract String toString();
    public abstract boolean fromString(String str);
}
