/*
 * Password hasher
 */
package ua.kpi.atep.services;

/**
 *
 * @author Home
 */
public interface PasswordHasher {
    
    /**
     * Obtain cryptographic hash from string value
     * 
     * @param password password as string
     * @return string value of the hash
     */
    String toHash(String password);
    
    /**
     * Type of the hash function as String
     * 
     * @return 
     */
    String getType();
}
