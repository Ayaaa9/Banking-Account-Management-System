package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.data.DemandeCredit;
import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.Etat;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.ClientRepository;
import com.bankati.bankati.repository.DemandeCreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminHomeController {

    private final ClientRepository clientRepository;
    private final DemandeCreditRepository demandeCreditRepository;

    @Autowired
    public AdminHomeController(ClientRepository clientRepository, DemandeCreditRepository demandeCreditRepository) {
        this.clientRepository = clientRepository;
        this.demandeCreditRepository = demandeCreditRepository;
    }

    @GetMapping("/admin/home")
    public String showAdminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (!(principal instanceof User user) || !user.isAdmin()) {
            return "redirect:/login";
        }

        List<Client> allClients = clientRepository.findAll();
        List<DemandeCredit> allDemandes = demandeCreditRepository.findAll();

        long creditsApprouves = allDemandes.stream().filter(d -> d.getEtat() == Etat.APPROUVEE).count();
        long creditsRejetes = allDemandes.stream().filter(d -> d.getEtat() == Etat.REFUSEE).count();
        long enAttente = allDemandes.stream().filter(d -> d.getEtat() == Etat.EN_ATTENTE).count();

        double montantTotal = allDemandes.stream()
                .filter(d -> d.getEtat() == Etat.APPROUVEE)
                .mapToDouble(DemandeCredit::getMontant)
                .sum();

        MoneyData result = new MoneyData(montantTotal, Devise.DH);
        String montantAffiche = (montantTotal >= 1_000_000)
                ? String.format("%.2f MDH", montantTotal / 1_000_000)
                : String.format("%.2f DH", montantTotal);

        List<DemandeCredit> latestDemandes = allDemandes.stream()
                .filter(d -> d.getEtat() == Etat.EN_ATTENTE)
                .sorted(Comparator.comparing(DemandeCredit::getDateDemande).reversed())
                .collect(Collectors.toList());

        Map<Long, String> nomsClients = new HashMap<>();
        Map<Long, String> montantsFormates = new HashMap<>();

        for (DemandeCredit d : latestDemandes) {
            Client client = d.getClient();
            nomsClients.put(d.getId(),
                    (client != null) ? client.getFirstName() + " " + client.getLastName() : "Inconnu");

            montantsFormates.put(d.getId(),
                    d.getMontant() >= 1_000_000
                            ? String.format("%.2f MDH", d.getMontant() / 1_000_000)
                            : String.format("%.2f DH", d.getMontant()));
        }

        model.addAttribute("result", result);
        model.addAttribute("montantAffiche", montantAffiche);
        model.addAttribute("totalClients", allClients.size());
        model.addAttribute("totalDemandes", allDemandes.size());
        model.addAttribute("creditsApprouves", creditsApprouves);
        model.addAttribute("creditsRejetes", creditsRejetes);
        model.addAttribute("enAttente", enAttente);
        model.addAttribute("latestDemandes", latestDemandes);
        model.addAttribute("nomsClients", nomsClients);
        model.addAttribute("montantsFormates", montantsFormates);

        return "admin/home";
    }
}
