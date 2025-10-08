package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.Admin;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.ERole;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import com.bankati.bankati.repository.MoneyDataRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/ajoutUser")
public class AdminAddUserController {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final MoneyDataRepository moneyDataRepository;

    public AdminAddUserController(AdminRepository adminRepository, ClientRepository clientRepository, MoneyDataRepository moneyDataRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.moneyDataRepository = moneyDataRepository;
    }

    @GetMapping
    public String showAddUserForm(HttpSession session, Model model) {
        var connectedUser = session.getAttribute("connectedUser");

        if (connectedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", new Client());
        return "admin/ajoutUser";
    }

    @PostMapping
    public String handleAddUser(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String role,
                                @RequestParam(required = false) String cin,
                                @RequestParam(required = false) String fonction,
                                HttpSession session,
                                Model model) {

        var connectedUser = session.getAttribute("connectedUser");

        if (connectedUser == null) {
            return "redirect:/login";
        }

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            model.addAttribute("error", "Tous les champs sont obligatoires !");
            return "admin/ajoutUser";
        }

        try {
            ERole userRole = ERole.valueOf(role.toUpperCase());

            if (userRole == ERole.ADMIN) {
                if (fonction == null || fonction.isEmpty()) {
                    model.addAttribute("error", "La fonction est obligatoire pour un administrateur !");
                    return "admin/ajoutUser";
                }
                // Create and save MoneyData first
                MoneyData solde = new MoneyData(0.0, Devise.DH);
                solde = moneyDataRepository.save(solde);

                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setPassword(password);
                admin.setFirstName(firstName);
                admin.setLastName(lastName);
                admin.setRole(userRole);
                admin.setSolde(solde);
                admin.setFonction(fonction);
                adminRepository.save(admin);
            } else {
                if (cin == null || cin.isEmpty()) {
                    model.addAttribute("error", "Le CIN est obligatoire pour un client !");
                    return "admin/ajoutUser";
                }
                // Create and save MoneyData first
                MoneyData solde = new MoneyData(0.0, Devise.DH);
                solde = moneyDataRepository.save(solde);

                Client client = new Client();
                client.setUsername(username);
                client.setPassword(password);
                client.setFirstName(firstName);
                client.setLastName(lastName);
                client.setRole(userRole);
                client.setSolde(solde);
                client.setCin(cin);
                clientRepository.save(client);
            }

            session.setAttribute("userMessage", "Utilisateur ajouté avec succès.");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Le rôle spécifié est invalide !");
            return "admin/ajoutUser";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            return "admin/ajoutUser";
        }
    }
}