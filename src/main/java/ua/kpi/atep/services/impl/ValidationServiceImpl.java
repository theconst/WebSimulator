/*
 * ValidationServiceImpl.java
 */
package ua.kpi.atep.services.impl;

import ua.kpi.atep.services.ValidationService;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 *
 * Validates the email field
 * 
 * @author Kostiantyn Kovalchuk
 */
@Service
public class ValidationServiceImpl implements ValidationService {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-\\+]"
            + "+(\\.[_A-Za-z0-9-]+)*"
            + "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    );
  
    @Override
    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    } 
}
