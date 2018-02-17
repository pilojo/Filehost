package com.algonquincollege.javaApp.database.encryption;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author John Pilon
 */
public class PasswordHashUtils {
    private static final int logRounds = 10;
    
    /**
     * Hashes a password
     * @param password the password to has
     * @return String: The hashed password
     */
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }
    
    /**
     * 
     * @param password The password to compare
     * @param hash The hash stored in the DB to compare to
     * @return boolean: Whether the hash is valid
     */
    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
