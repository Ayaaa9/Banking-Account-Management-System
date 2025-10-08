package com.bankati.bankati.service.moneyServices.ServiceEuro;

import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.Admin;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import com.bankati.bankati.service.moneyServices.IMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceEuro implements IMoneyService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public MoneyData convertData() {
        return null; // conversion générique non utilisée ici
    }

    @Override
    public MoneyData convertData(Long userId) {
        Optional<? extends User> userOpt = adminRepository.findById(userId).map(a -> (User) a);

        if (userOpt.isEmpty()) {
            userOpt = clientRepository.findById(userId).map(c -> (User) c);
        }

        if (userOpt.isPresent()) {
            MoneyData solde = userOpt.get().getSolde();
            if (solde != null) {
                double result = solde.convertTo(Devise.EURO);
                return new MoneyData(result, Devise.EURO);
            }
        }

        return new MoneyData(0.0, Devise.EURO); // valeur par défaut si utilisateur/solde absent
    }
}
