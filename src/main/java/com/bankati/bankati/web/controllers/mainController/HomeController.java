package com.bankati.bankati.web.controllers.mainController;

import com.bankati.bankati.model.users.User;
import com.bankati.bankati.service.moneyServices.IMoneyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final IMoneyService moneyService;

    @Autowired
    public HomeController(IMoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @GetMapping
    public String redirectToDashboard(HttpSession session) {
        User user = (User) session.getAttribute("connectedUser");

        if (user == null) {
            return "redirect:/login";
        }

        // injecte le résultat du service dans la session (par exemple pour le solde converti)
        session.setAttribute("result", moneyService.convertData(user.getId()));

        if (user.isAdmin()) {
            return "redirect:/admin/home";
        } else if (user.isClient()) {
            return "redirect:/client/home";
        } else {
            return "redirect:/login";
        }
    }
}
