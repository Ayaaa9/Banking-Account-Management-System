package com.bankati.bankati.web.controllers.mainController.clientController;

import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client/home")
public class ClientHomeController {

    @GetMapping
    public String afficherAccueilClient(@RequestParam(required = false) String devise,
                                        HttpSession session,
                                        Model model) {

        User user = (User) session.getAttribute("connectedUser");

        if (user == null || !user.isClient()) {
            return "redirect:/login";
        }

        MoneyData solde = user.getSolde();
        Devise target = (devise != null) ? Devise.valueOf(devise.toUpperCase()) : solde.getDevise();
        MoneyData result = new MoneyData(solde.convertTo(target), target);

        model.addAttribute("soldeOriginal", solde);
        model.addAttribute("soldeDh", new MoneyData(solde.convertTo(Devise.DH), Devise.DH));
        model.addAttribute("soldeEuro", new MoneyData(solde.convertTo(Devise.EURO), Devise.EURO));
        model.addAttribute("soldeDollar", new MoneyData(solde.convertTo(Devise.DOLLAR), Devise.DOLLAR));
        model.addAttribute("soldePound", new MoneyData(solde.convertTo(Devise.POUND), Devise.POUND));
        model.addAttribute("selectedDevise", target.name());
        model.addAttribute("result", result);

        return "client/home"; // templates/client/home.html
    }
}
