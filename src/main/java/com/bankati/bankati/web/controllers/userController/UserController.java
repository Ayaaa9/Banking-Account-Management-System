package com.bankati.bankati.web.controllers.userController;

import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.ERole;
import com.bankati.bankati.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/user-actions")
public class UserController {

    private final ClientRepository clientRepository;

    @Autowired
    public UserController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", clientRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        Optional<Client> userOpt = clientRepository.findById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "admin/editUser";
        } else {
            return "redirect:/admin/user-actions";
        }
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        clientRepository.deleteById(id);
        return "redirect:/admin/user-actions";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam(required = false) Long id,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role,
                           @RequestParam String solde_amount,
                           @RequestParam String solde_devise,
                           HttpSession session,
                           Model model) {
        try {
            MoneyData solde = new MoneyData(
                    (solde_amount != null && !solde_amount.isEmpty()) ? Double.parseDouble(solde_amount) : 0.0,
                    (solde_devise != null && !solde_devise.isEmpty()) ? solde_devise : "DH"
            );

            Client user = new Client();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(ERole.valueOf(role));
            user.setSolde(solde);

            clientRepository.save(user);

            return "redirect:/admin/user-actions";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "admin/editUser";
        }
    }
}
