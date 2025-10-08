package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.data.DemandeCredit;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.ERole;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import com.bankati.bankati.repository.DemandeCreditRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserManagementController {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final DemandeCreditRepository demandeCreditRepository;

    @Autowired
    public UserManagementController(AdminRepository adminRepository,
                                    ClientRepository clientRepository,
                                    DemandeCreditRepository demandeCreditRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.demandeCreditRepository = demandeCreditRepository;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("users", clients);
        return "admin/users";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            model.addAttribute("user", optionalClient.get());
            return "admin/editUser";
        } else {
            return "redirect:/users";
        }
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, HttpSession session) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            List<DemandeCredit> demandes = demandeCreditRepository.findByClient(client);
            if (!demandes.isEmpty()) {
                session.setAttribute("userError", "Impossible de supprimer un utilisateur ayant des demandes de crédit.");
            } else {
                clientRepository.deleteById(id);
                session.setAttribute("userMessage", "Utilisateur supprimé avec succès.");
            }
        } else {
            session.setAttribute("userError", "Utilisateur introuvable.");
        }

        return "redirect:/users";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam(required = false) Long id,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String username,
                           @RequestParam String password,
                           HttpSession session) {

        boolean loginExists = clientRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username) && !u.getId().equals(id));

        if (loginExists) {
            session.setAttribute("userError", "Ce nom d'utilisateur existe déjà.");
            return "redirect:/users";
        }

        Client user = new Client();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(ERole.USER);

        clientRepository.save(user);
        session.setAttribute("userMessage", id == null ? "Utilisateur ajouté avec succès." : "Utilisateur modifié avec succès.");

        return "redirect:/users";
    }
}
