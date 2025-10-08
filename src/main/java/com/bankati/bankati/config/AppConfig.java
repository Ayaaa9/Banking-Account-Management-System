package com.bankati.bankati.config;

import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import com.bankati.bankati.repository.MoneyDataRepository;
import com.bankati.bankati.service.authentification.AuthentificationServiceImpl;
import com.bankati.bankati.service.authentification.IAuthentificationService;
import com.bankati.bankati.service.moneyServices.IMoneyService;
import com.bankati.bankati.service.moneyServices.serviceDirham.ServiceDh;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IAuthentificationService authService(AdminRepository adminRepo, ClientRepository clientRepo) {
        return new AuthentificationServiceImpl(adminRepo, clientRepo);
    }


    @Bean
    public IMoneyService moneyService(MoneyDataRepository moneyDataRepository) {
        return new ServiceDh(moneyDataRepository);
    }
}
