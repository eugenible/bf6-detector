package ru.eugenible.bf6.detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	@Value("${app.name}")
	private String applicationName;

	@Value("${app.version}")
	private String applicationVersion;

	@Value("${app.java-version}")
	private String javaVersion;

	@Override
	public void run(String... args) {
		System.out.println("=========================================");
		System.out.println("🚀 Starting: " + applicationName);
		System.out.println("📦 Version: " + applicationVersion);
		System.out.println("☕ Runtime: Java " + javaVersion);
		System.out.println("=========================================");
	}

}
