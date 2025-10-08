package com.bankati.bankati.web.controllers.mainController.clientController;

import com.bankati.bankati.model.data.DemandeCredit;
import com.bankati.bankati.model.data.Etat;
import com.bankati.bankati.model.users.Client;
import com.bankati.bankati.model.users.User;
import com.bankati.bankati.repository.DemandeCreditRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/client/demandes")
public class ClientDemandeController {

    private final DemandeCreditRepository demandeCreditRepository;

    @Autowired
    public ClientDemandeController(DemandeCreditRepository demandeCreditRepository) {
        this.demandeCreditRepository = demandeCreditRepository;
    }

    @GetMapping
    public String afficherDemandesClient(HttpSession session, Model model) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isClient()) {
            return "redirect:/login";
        }

        List<DemandeCredit> demandes = demandeCreditRepository.findByClient((Client) connectedUser);
        model.addAttribute("demandes", demandes);
        model.addAttribute("connectedUser", connectedUser);

        return "client/demandes";
    }

    @PostMapping("/delete")
    public String supprimerDemande(@RequestParam("id") Long id, HttpSession session) {
        User connectedUser = (User) session.getAttribute("connectedUser");
        if (connectedUser == null || !connectedUser.isClient()) {
            return "redirect:/login";
        }

        Optional<DemandeCredit> demandeOpt = demandeCreditRepository.findById(id);
        if (demandeOpt.isPresent()) {
            DemandeCredit demande = demandeOpt.get();

            // Vérifier que la demande appartient bien au client connecté
            if (demande.getClient().getId().equals(connectedUser.getId())) {
                if (demande.getEtat() == Etat.EN_ATTENTE) {
                    demandeCreditRepository.deleteById(id);
                    session.setAttribute("msg", "Demande supprimée avec succès.");
                } else {
                    session.setAttribute("msg", "Seules les demandes en attente peuvent être supprimées.");
                }
            } else {
                session.setAttribute("msg", "Accès refusé à cette demande.");
            }
        } else {
            session.setAttribute("msg", "Demande introuvable.");
        }

        return "redirect:/client/demandes";
    }
}
