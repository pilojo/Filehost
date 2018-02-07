/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializable.commands;

/**
 *
 * @author jp972
 */
public abstract class Command{
    protected String password = "A^@kds034!@$#sjvlksoirg%#$%^#@()sdgnverkf15681165";
    
    public String getPassword(){
        return password;
    }
    
    @Override
    public abstract String toString();
    public abstract boolean fromString(String str);
}
