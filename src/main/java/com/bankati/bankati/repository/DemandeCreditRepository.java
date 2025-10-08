package com.bankati.bankati.repository;

import com.bankati.bankati.model.data.DemandeCredit;
import com.bankati.bankati.model.users.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DemandeCreditRepository extends JpaRepository<DemandeCredit, Long> {
    List<DemandeCredit> findByClient(Client client);
    List<DemandeCredit> findByClientId(Long clientId);
}
