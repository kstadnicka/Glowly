package ks.glowlyapp.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public void validateEmailAndPassword(String email, String password) {
        validateNotNull(email, "Email");
        validateNotNull(password, "Password");
    }

    public void validatePhoneNumber(String phoneNumber) {
        validateNotNull(phoneNumber, "Phone number");
    }
    public void validateStringNotEmpty(String value, String fieldName) {
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }
}

