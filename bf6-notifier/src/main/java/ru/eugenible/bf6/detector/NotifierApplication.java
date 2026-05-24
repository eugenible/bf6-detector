package ru.eugenible.bf6.detector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotifierApplication implements CommandLineRunner {

    @Value("${app.name}")
    private String applicationName;

    @Value("${app.version}")
    private String applicationVersion;

    @Value("${app.java-version}")
    private String javaVersion;

    public static void main(String[] args) {
        SpringApplication.run(NotifierApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=========================================");
        System.out.println("🚀 Starting: " + applicationName);
        System.out.println("📦 Version: " + applicationVersion);
        System.out.println("☕ Runtime: Java " + javaVersion);
        System.out.println("=========================================");
    }

}
