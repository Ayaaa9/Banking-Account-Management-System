package com.bankati.bankati.service.moneyServices;

import com.bankati.bankati.model.data.MoneyData;

public interface IMoneyService {
     MoneyData convertData();
     MoneyData convertData(Long userId);
}
