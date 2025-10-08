package com.bankati.bankati;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

import java.awt.*;
import java.net.URI;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class BankatiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankatiApplication.class, args);

        // 🔵 Rediriger automatiquement vers le navigateur
        String url = "http://localhost:8080/login";

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                System.out.println(" Navigateur ouvert automatiquement à : " + url);
            } else {
                System.out.println(" Desktop non supporté. Ouvre manuellement : " + url);
            }
        } catch (Exception e) {
            System.err.println(" Échec ouverture navigateur : " + e.getMessage());
        }
    }
}
