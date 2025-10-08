package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.users.Admin;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.ERole;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users/edit")
public class EditUserController {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public EditUserController(AdminRepository adminRepository, ClientRepository clientRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String showEditForm(@RequestParam("id") Long id, @RequestParam("role") String role, Model model, HttpSession session) {
        Optional<? extends User> optionalUser =
                role.equals("ADMIN") ? adminRepository.findById(id) : clientRepository.findById(id);

        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
            return "admin/editUser";
        } else {
            session.setAttribute("userError", "Utilisateur introuvable.");
            return "redirect:/admin/users";
        }
    }

    @PostMapping
    public String updateUser(@RequestParam Long id,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String role,
                             HttpSession session) {

        if (role.equals("ADMIN")) {
            Optional<Admin> adminOpt = adminRepository.findById(id);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                admin.setFirstName(firstName);
                admin.setLastName(lastName);
                admin.setUsername(username);
                admin.setPassword(password);
                admin.setRole(ERole.ADMIN);
                adminRepository.save(admin);
                session.setAttribute("userMessage", "Admin modifié avec succès.");
            } else {
                session.setAttribute("userError", "Admin non trouvé.");
            }
        } else {
            Optional<Client> clientOpt = clientRepository.findById(id);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setFirstName(firstName);
                client.setLastName(lastName);
                client.setUsername(username);
                client.setPassword(password);
                client.setRole(ERole.USER);
                clientRepository.save(client);
                session.setAttribute("userMessage", "Client modifié avec succès.");
            } else {
                session.setAttribute("userError", "Client non trouvé.");
            }
        }

        return "redirect:/admin/users";
    }
}
