package com.bankati.bankati.web.controllers.mainController.clientController;

import com.bankati.bankati.model.data.DemandeCredit;
import com.bankati.bankati.model.data.Devise;
import com.bankati.bankati.model.data.Etat;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.DemandeCreditRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/client/ajouterDemande")
public class AjouterDemandeController {

    private final DemandeCreditRepository demandeCreditRepository;

    @Autowired
    public AjouterDemandeController(DemandeCreditRepository demandeCreditRepository) {
        this.demandeCreditRepository = demandeCreditRepository;
    }

    // ✅ Afficher le formulaire avec message si présent
    @GetMapping
    public String afficherFormulaireAjout(HttpSession session, Model model) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isClient()) {
            return "redirect:/login";
        }

        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }

        String error = (String) session.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            session.removeAttribute("error");
        }

        return "client/ajouterDemande"; // ➜ templates/client/ajouterDemande.html
    }

    // ✅ Soumettre la demande
    @PostMapping
    public String enregistrerDemande(@RequestParam double montant,
                                     @RequestParam String devise,
                                     HttpSession session) {

        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isClient()) {
            return "redirect:/login";
        }

        try {
            DemandeCredit demande = new DemandeCredit();
            demande.setMontant(montant);
            demande.setDevise(Devise.valueOf(devise.toUpperCase()));
            demande.setEtat(Etat.EN_ATTENTE);
            demande.setDateDemande(LocalDate.now());
            demande.setClient((Client) connectedUser); // ✅ cast obligatoire

            demandeCreditRepository.save(demande);

            session.setAttribute("msg", "Demande envoyée avec succès !");
        } catch (Exception e) {
            session.setAttribute("error", "Erreur : " + e.getMessage());
        }

        return "redirect:/client/ajouterDemande";
    }
}
