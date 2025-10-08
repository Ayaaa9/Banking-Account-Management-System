package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.users.Admin;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public AdminUsersController(AdminRepository adminRepository, ClientRepository clientRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String showUsersPage(HttpSession session, Model model) {
        User connectedUser = (User) session.getAttribute("connectedUser");

        // Redirection si non connecté ou non admin
        if (connectedUser == null || !connectedUser.isAdmin()) {
            return "redirect:/login";
        }

        List<User> users = new ArrayList<>();
        users.addAll(adminRepository.findAll());
        users.addAll(clientRepository.findAll());

        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("users", users);

        return "admin/users"; // templates/admin/users.html
    }
}
