package com.bankati.bankati.web.controllers.authControllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate(); // Invalide la session utilisateur
        model.addAttribute("globalMessage", "Vous avez été déconnecté avec succès.");
        return "public/login"; // Redirige vers login.html
    }
}
