package com.UniHUB.Server.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileLogger {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void logActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();

        if (activeProfiles.length > 0) {
            System.out.println("✅ Active Profiles: " + String.join(", ", activeProfiles));
        } else {
            System.out.println("⚠️  No Active Profiles Found!");
        }
    }
}