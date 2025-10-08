package com.bankati.bankati.web.controllers.mainController.clientController;

import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client/profile")
public class ClientProfileController {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientProfileController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isClient()) {
            return "redirect:/login";
        }

        model.addAttribute("user", connectedUser);
        return "client/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String username,
                              @RequestParam(required = false) String password,
                              @RequestParam String cin,
                              HttpSession session) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isClient()) {
            return "redirect:/login";
        }

        try {
            Client client = (Client) connectedUser;
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setUsername(username);
            if (password != null && !password.isEmpty()) {
                client.setPassword(password);
            }
            client.setCin(cin);
            
            clientRepository.save(client);
            session.setAttribute("msg", "Profil mis à jour avec succès.");
        } catch (Exception e) {
            session.setAttribute("error", "Erreur lors de la mise à jour du profil : " + e.getMessage());
        }

        return "redirect:/client/profile";
    }
} 