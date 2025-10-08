package com.bankati.bankati.service.authentification;

import com.bankati.bankati.model.users.Admin;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthentificationServiceImpl implements IAuthentificationService {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final UserFormValidator validator = new UserFormValidator();
    private User connectedUser;

    public AuthentificationServiceImpl(AdminRepository adminRepository, ClientRepository clientRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean validateLoginForm(String username, String password) {
        return validator.validate(username, password);
    }

    @Override
    public User connect(String username, String password) {
        String login = username.trim();
        String pass = password.trim();

        for (Admin admin : adminRepository.findAll()) {
            if (admin.getUsername().equalsIgnoreCase(login) && admin.getPassword().equals(pass)) {
                connectedUser = admin;
                injectAuthorities(admin);
                validator.setGlobalMessage("Connexion réussie (Admin) !");
                return connectedUser;
            }
        }

        for (Client client : clientRepository.findAll()) {
            if (client.getUsername().equalsIgnoreCase(login) && client.getPassword().equals(pass)) {
                connectedUser = client;
                injectAuthorities(client);
                validator.setGlobalMessage("Connexion réussie (Client) !");
                return connectedUser;
            }
        }

        validator.setGlobalMessage("Nom d'utilisateur ou mot de passe incorrect.");
        connectedUser = null;
        return null;
    }

    private void injectAuthorities(User user) {
        String role = "ROLE_" + user.getRole().name();
        System.out.println("✅ Rôle injecté dans Spring Security : " + role); // DEBUG

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }


    @Override
    public Map<String, String> getFieldErrors() {
        return validator.getFieldErrors();
    }

    @Override
    public String getGlobalMessage() {
        return validator.getGlobalMessage();
    }

    @Override
    public void disconnect() {
        connectedUser = null;
        SecurityContextHolder.clearContext();
    }
}