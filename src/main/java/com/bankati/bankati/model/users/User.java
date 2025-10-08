package com.bankati.bankati.model.users;

import com.bankati.bankati.model.data.MoneyData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    private LocalDate creationDate;

    @OneToOne
    private MoneyData solde;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
    }

    public String getFormattedSolde() {
        return solde != null ? solde.getAmount() + " " + solde.getDevise() : "N/A";
    }

    public boolean isAdmin() {
        return role != null && role.name().equals("ADMIN");
    }

    public boolean isClient() {
        return role != null && role.name().equals("USER");
    }

    @Override
    public String toString() {
        return "[" + role + "] " + firstName + " " + lastName;
    }
}
