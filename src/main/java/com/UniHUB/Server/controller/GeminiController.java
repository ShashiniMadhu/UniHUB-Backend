package com.UniHUB.Server.controller;

import com.UniHUB.Server.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gemini")
@RequiredArgsConstructor
public class GeminiController {
    private final GeminiService geminiService;

    @GetMapping("/ask")
    public String askGemini(@RequestBody String prompt) {
        return geminiService.askGemini(prompt);
    }
}
