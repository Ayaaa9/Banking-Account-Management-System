package com.bankati.bankati.service.authentification;

import com.bankati.bankati.model.users.User;

import java.util.Map;

public interface IAuthentificationService {

    /**
     * Valide le formulaire de connexion (champs non vides, format, etc.)
     */
    boolean validateLoginForm(String username, String password);

    /**
     * Tente de connecter l'utilisateur avec username + password
     * @return utilisateur connecté ou null si échec
     */
    User connect(String username, String password);

    /**
     * Retourne les erreurs de validation du formulaire
     */
    Map<String, String> getFieldErrors();

    /**
     * Message global de succès ou d'erreur
     */
    String getGlobalMessage();

    /**
     * Déconnecte l'utilisateur courant
     */
    void disconnect();
}
