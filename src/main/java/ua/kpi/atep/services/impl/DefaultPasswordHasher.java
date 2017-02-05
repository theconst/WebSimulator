/*
 * DefaultPasswordHasher.java
 */
package ua.kpi.atep.services.impl;

import org.springframework.stereotype.Service;
import ua.kpi.atep.services.PasswordHasher;


/**
 * 
 * Default service that guarantees primitive security
 * 
 * //replace with proper hashing after testing
 * 
 * @author 
 */
@Service
public class DefaultPasswordHasher implements PasswordHasher {
    
    private static final char SECRET = 67;
    
    private static final String DEFAULT_HASHER = "default hasher, easy to break";

    @Override
        public String toHash(String password) {
            return password.chars().map(x -> x ^ SECRET).mapToObj(Integer::toString)
                    .reduce(String::concat).get();
        }

    @Override
    public String getType() {
        return DEFAULT_HASHER;
    }
    
}
