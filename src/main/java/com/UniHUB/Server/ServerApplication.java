package com.UniHUB.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@SpringBootApplication
@RequestMapping
public class ServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServerApplication.class, args);


	}
}