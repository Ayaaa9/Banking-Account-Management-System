package com.bankati.bankati.model.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "money_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING) // Stocke l'enum comme texte (EURO, DH, etc.)
    private Devise devise;

    // Constructeur personnalisé avec Enum
    public MoneyData(double amount, Devise devise) {
        this.amount = amount;
        this.devise = devise;
    }

    // Constructeur avec String (utile si tu lis depuis JDBC par exemple)
    public MoneyData(double amount, String deviseName) {
        this.amount = amount;
        this.devise = Devise.valueOf(deviseName.toUpperCase());
    }

    @Override
    public String toString() {
        double rounded = Math.round(amount * 100.0) / 100.0;
        return rounded + " " + devise;
    }

    public double convertTo(Devise target) {
        if (this.devise == target) return amount;

        return switch (this.devise) {
            case DH -> switch (target) {
                case EURO -> amount / 10;
                case DOLLAR -> amount / 11;
                case POUND -> amount / 12;
                default -> amount;
            };
            case EURO -> switch (target) {
                case DH -> amount * 10;
                case POUND -> amount / 1.25;
                default -> amount;
            };
            case DOLLAR -> switch (target) {
                case DH -> amount * 11;
                case POUND -> amount / 1.15;
                default -> amount;
            };
            case POUND -> switch (target) {
                case DH -> amount * 12;
                case DOLLAR -> amount * 1.25;
                case EURO -> amount * 1.15;
                default -> amount;
            };
        };
    }
}
