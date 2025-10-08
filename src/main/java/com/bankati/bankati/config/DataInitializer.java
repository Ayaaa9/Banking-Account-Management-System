package com.bankati.bankati.config;

import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.Admin;
import com.bankati.bankati.model.users.ERole;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.MoneyDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final AdminRepository adminRepository;
    private final MoneyDataRepository moneyDataRepository;

    public DataInitializer(AdminRepository adminRepository, MoneyDataRepository moneyDataRepository) {
        this.adminRepository = adminRepository;
        this.moneyDataRepository = moneyDataRepository;
    }

    @PostConstruct
    public void initAdminSingleton() {
        String defaultUsername = "admin";

        boolean adminExists = adminRepository.findByUsername(defaultUsername).isPresent();

        if (adminExists) {
            System.out.println("✅ Admin déjà existant : " + defaultUsername);
        } else {
            MoneyData solde = new MoneyData(0.0, Devise.DH);
            moneyDataRepository.save(solde);

            Admin admin = Admin.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .username(defaultUsername)
                    .password("admin123")
                    .fonction("Responsable Système")
                    .role(ERole.ADMIN)
                    .creationDate(LocalDate.now())
                    .solde(solde)
                    .build();

            adminRepository.save(admin);
            System.out.println("✅ Admin créé automatiquement avec username : " + defaultUsername);
        }
    }
}
