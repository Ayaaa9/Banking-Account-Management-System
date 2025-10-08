package com.bankati.bankati.repository;

import com.bankati.bankati.model.data.MoneyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyDataRepository extends JpaRepository<MoneyData, Long> {
}
