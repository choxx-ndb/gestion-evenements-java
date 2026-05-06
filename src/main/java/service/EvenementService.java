package service;

import java.time.LocalDateTime;
import java.util.List;

import dao.EvenementDAO;
import model.Evenement;

public class EvenementService {

    private EvenementDAO evenementDAO;

    public EvenementService() {
        this.evenementDAO = new EvenementDAO();
    }

    public List<Evenement> listerEvenements() {
        return evenementDAO.selectAll();
    }

    public Evenement trouverParId(int idEvenement) {
        if (idEvenement <= 0) {
            return null;
        }

        return evenementDAO.getById(idEvenement);
    }

    public boolean ajouterEvenement(Evenement evenement) {
        if (!evenementValide(evenement)) {
            return false;
        }

        evenementDAO.add(evenement);
        return true;
    }

    public boolean modifierEvenement(Evenement evenement) {
        if (evenement == null || evenement.getId() <= 0) {
            return false;
        }

        if (!evenementValide(evenement)) {
            return false;
        }

        evenementDAO.update(evenement);
        return true;
    }

    public boolean supprimerEvenement(int idEvenement) {
        if (idEvenement <= 0) {
            return false;
        }

        evenementDAO.delete(idEvenement);
        return true;
    }

    private boolean evenementValide(Evenement evenement) {
        if (evenement == null) {
            return false;
        }

        if (evenement.getTitre() == null || evenement.getTitre().trim().isEmpty()) {
            return false;
        }

        if (evenement.getDescription() == null || evenement.getDescription().trim().isEmpty()) {
            return false;
        }

        if (evenement.getLieu() == null || evenement.getLieu().trim().isEmpty()) {
            return false;
        }

        if (evenement.getCapacite() <= 0) {
            return false;
        }

        if (evenement.getCategorieId() <= 0) {
            return false;
        }

        if (evenement.getOrganisateurId() <= 0) {
            return false;
        }

        if (evenement.getDateDebut() == null) {
            return false;
        }

        if (evenement.getDateDebut().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }
}