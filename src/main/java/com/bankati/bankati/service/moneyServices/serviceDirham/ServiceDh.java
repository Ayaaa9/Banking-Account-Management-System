// ✅ ServiceDh.java
package com.bankati.bankati.service.moneyServices.serviceDirham;

import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.repository.MoneyDataRepository;
import com.bankati.bankati.service.moneyServices.IMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("serviceDh")
@Primary
public class ServiceDh implements IMoneyService {

    private final MoneyDataRepository moneyDataRepository;

    @Autowired
    public ServiceDh(MoneyDataRepository moneyDataRepository) {
        this.moneyDataRepository = moneyDataRepository;
    }

    @Override
    public MoneyData convertData(Long userId) {
        MoneyData input = moneyDataRepository.findById(userId).orElse(null);
        if (input == null) return null;
        double result = new MoneyData(input.getAmount(), input.getDevise()).convertTo(Devise.DH);
        return new MoneyData(result, Devise.DH);
    }

    @Override
    public MoneyData convertData() {
        return null;
    }
}
