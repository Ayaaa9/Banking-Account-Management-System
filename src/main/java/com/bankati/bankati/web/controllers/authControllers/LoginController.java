package com.bankati.bankati.web.controllers.authControllers;

import com.bankati.bankati.model.users.User;
import com.bankati.bankati.service.authentification.IAuthentificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final IAuthentificationService authService;

    public LoginController(IAuthentificationService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginPage(Model model) {
        model.addAttribute("appName", "Bankati");
        return "public/login";
    }

    @PostMapping
    public String handleLogin(@RequestParam("lg") String username,
                              @RequestParam("pss") String password,
                              Model model,
                              HttpSession session) {

        model.addAttribute("appName", "Bankati");

        if (!authService.validateLoginForm(username, password)) {
            model.addAllAttributes(authService.getFieldErrors());
            model.addAttribute("globalMessage", authService.getGlobalMessage());
            return "public/login";
        }

        User user = authService.connect(username, password);

        if (user != null) {
            session.setAttribute("connectedUser", user);
            session.setAttribute("globalMessage", authService.getGlobalMessage());

            switch (user.getRole()) {
                case ADMIN -> {
                    return "redirect:/admin/home";
                }
                case USER -> {
                    return "redirect:/client/home";
                }
                default -> {
                    model.addAttribute("globalMessage", "Rôle utilisateur inconnu.");
                    return "public/login";
                }
            }
        } else {
            model.addAttribute("globalMessage", authService.getGlobalMessage());
            return "public/login";
        }
    }
}
