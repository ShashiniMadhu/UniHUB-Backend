package com.UniHUB.Server.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    /**
     * Generates a random password with 8 characters
     * Contains uppercase, lowercase, and numbers
     */
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
