package com.algonquincollege.database.encryption;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Byzantian
 */
public class PasswordHashUtils {
    private static final int logRounds = 10;
    
    
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }
    
    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
