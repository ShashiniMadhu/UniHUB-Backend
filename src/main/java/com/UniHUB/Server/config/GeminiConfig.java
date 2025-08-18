package com.UniHUB.Server.config;

import com.google.genai.Client;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Bean
    public Client geminiClient() {
        // Load .env file
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        // Get API key from .env file
        String apiKey = dotenv.get("GOOGLE_API_KEY");

        // Debug: Print to see if API key is loaded (remove this line after testing)
        System.out.println("Loaded API Key: " + (apiKey != null ? "Found" : "Not found"));

        // Check if API key exists
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("GOOGLE_API_KEY not found in .env file!");
        }

        // Create client with API key
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}