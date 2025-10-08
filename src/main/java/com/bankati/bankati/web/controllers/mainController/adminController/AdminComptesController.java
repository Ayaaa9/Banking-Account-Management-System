package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/comptes")
public class AdminComptesController {

    private final ClientRepository clientRepository;

    public AdminComptesController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String showComptes(
            HttpSession session,
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isAdmin()) {
            return "redirect:/login";
        }

        List<Client> clients = clientRepository.findAll();

        // ✅ Filtrage optionnel
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            clients = clients.stream().filter(c ->
                    c.getFirstName().toLowerCase().contains(lowerKeyword)
                            || c.getLastName().toLowerCase().contains(lowerKeyword)
                            || c.getCin().toLowerCase().contains(lowerKeyword)
                            || (c.getSolde() != null && (
                            String.valueOf(c.getSolde().getAmount()).contains(lowerKeyword)
                                    || c.getSolde().getDevise().name().toLowerCase().contains(lowerKeyword)
                    ))
            ).collect(Collectors.toList());
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("clients", clients);
        model.addAttribute("connectedUser", connectedUser);

        return "admin/comptes";
    }
}
