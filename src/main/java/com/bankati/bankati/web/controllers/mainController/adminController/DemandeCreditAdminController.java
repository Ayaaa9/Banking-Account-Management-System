package com.bankati.bankati.web.controllers.mainController.adminController;

import com.bankati.bankati.model.data.DemandeCredit;
import com.bankati.bankati.model.data.Etat;
import com.bankati.bankati.model.data.MoneyData;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.AdminRepository;
import com.bankati.bankati.repository.ClientRepository;
import com.bankati.bankati.repository.DemandeCreditRepository;
import com.bankati.bankati.repository.MoneyDataRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/demandes")
public class DemandeCreditAdminController {

    private final DemandeCreditRepository demandeCreditRepository;
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final MoneyDataRepository moneyDataRepository;

    @Autowired
    public DemandeCreditAdminController(DemandeCreditRepository demandeCreditRepository,
                                        AdminRepository adminRepository,
                                        ClientRepository clientRepository,
                                        MoneyDataRepository moneyDataRepository) {
        this.demandeCreditRepository = demandeCreditRepository;
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.moneyDataRepository = moneyDataRepository;
    }

    @GetMapping
    public String afficherDemandes(@RequestParam(required = false) String keyword,
                                   Model model, HttpSession session) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isAdmin()) {
            return "redirect:/login";
        }

        List<DemandeCredit> demandes;
        if (keyword != null && !keyword.isBlank()) {
            demandes = demandeCreditRepository.findAll().stream()
                    .filter(d -> {
                        String nomClient = d.getClient() != null
                                ? d.getClient().getFirstName() + " " + d.getClient().getLastName()
                                : "";
                        return nomClient.toLowerCase().contains(keyword.toLowerCase())
                                || String.valueOf(d.getMontant()).contains(keyword)
                                || d.getDevise().name().toLowerCase().contains(keyword.toLowerCase())
                                || d.getEtat().name().toLowerCase().contains(keyword.toLowerCase());
                    }).toList();
            model.addAttribute("keyword", keyword);
        } else {
            demandes = demandeCreditRepository.findAll();
        }

        Map<Long, String> montantsFormates = new HashMap<>();
        Map<Long, String> nomsClients = new HashMap<>();

        for (DemandeCredit d : demandes) {
            double montant = d.getMontant();
            String montantTxt = (montant >= 1_000_000)
                    ? String.format("%.2f MDH", montant / 1_000_000)
                    : String.format("%.2f DH", montant);
            montantsFormates.put(d.getId(), montantTxt);

            User client = d.getClient();
            nomsClients.put(d.getId(), (client != null)
                    ? client.getFirstName() + " " + client.getLastName()
                    : "Client inconnu");
        }

        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("demandes", demandes);
        model.addAttribute("montantsFormates", montantsFormates);
        model.addAttribute("nomsClients", nomsClients);

        return "admin/demandes";
    }

    @PostMapping("/update")
    public String majEtatDemande(@RequestParam("id") Long id,
                                 @RequestParam("action") String action,
                                 HttpSession session) {
        Optional<DemandeCredit> opt = demandeCreditRepository.findById(id);
        if (opt.isPresent()) {
            DemandeCredit demande = opt.get();
            if (demande.getEtat() == Etat.EN_ATTENTE) {
                Etat nouvelEtat = "APPROUVE".equalsIgnoreCase(action) ? Etat.APPROUVEE : Etat.REFUSEE;
                demande.setEtat(nouvelEtat);

                if (nouvelEtat == Etat.APPROUVEE) {
                    User client = demande.getClient();
                    MoneyData solde = client.getSolde();
                    double montant = demande.getMontant();
                    if (demande.getDevise() != solde.getDevise()) {
                        montant = new MoneyData(montant, demande.getDevise()).convertTo(solde.getDevise());
                    }
                    solde.setAmount(solde.getAmount() + montant);
                    moneyDataRepository.save(solde);
                }

                demandeCreditRepository.save(demande);
                session.setAttribute("msg", "Demande mise à jour avec succès.");
            } else {
                session.setAttribute("msg", "La demande est déjà traitée.");
            }
        } else {
            session.setAttribute("msg", "Demande introuvable.");
        }

        return "redirect:/admin/demandes";
    }
}
