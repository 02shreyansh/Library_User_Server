package com.shreyansh.User_Service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("ACCESS_PRIVATE_KEY",dotenv.get("ACCESS_PRIVATE_KEY"));
		System.setProperty("ACCESS_PUBLIC_KEY",dotenv.get("ACCESS_PUBLIC_KEY"));
		System.setProperty("ACCESS_TOKEN_EXPIRY",dotenv.get("ACCESS_TOKEN_EXPIRY"));

		System.setProperty("REFRESH_PRIVATE_KEY",dotenv.get("REFRESH_PRIVATE_KEY"));
		System.setProperty("REFRESH_PUBLIC_KEY",dotenv.get("REFRESH_PUBLIC_KEY"));
		System.setProperty("REFRESH_TOKEN_EXPIRY",dotenv.get("REFRESH_TOKEN_EXPIRY"));
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
