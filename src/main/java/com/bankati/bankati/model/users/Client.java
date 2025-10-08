package com.bankati.bankati.model.users;

import com.bankati.bankati.model.data.DemandeCredit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {

    @Column(nullable = false, unique = true)
    private String cin;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandeCredit> demandes;
}
