package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.EvenementDAO;
import dao.InscriptionDAO;
import model.Evenement;


public class InscriptionService {

    private InscriptionDAO inscriptionDAO;
    private EvenementDAO evenementDAO;

    public InscriptionService() {
        this.inscriptionDAO = new InscriptionDAO();
        this.evenementDAO = new EvenementDAO();
    }

    public String inscrireUtilisateur(int idUtilisateur, int idEvenement) {

        // 1. Vérifier l'utilisateur
        if (idUtilisateur <= 0) {
            return "Utilisateur non valide.";
        }

        // 2. Vérifier l'événement
        if (idEvenement <= 0) {
            return "Événement non valide.";
        }

        // 3. Récupérer l'événement depuis la base
        Evenement evenement = evenementDAO.getById(idEvenement);

        if (evenement == null) {
            return "Événement introuvable.";
        }

        // 4. Vérifier si l'événement est déjà passé
        if (evenement.getDateDebut() != null 
                && evenement.getDateDebut().isBefore(LocalDateTime.now())) {
            return "L'événement a déjà commencé ou est déjà passé.";
        }

        // 5. Vérifier si l'utilisateur est déjà inscrit
        if (inscriptionDAO.dejaInscrit(idUtilisateur, idEvenement)) {
            return "Vous êtes déjà inscrit à cet événement.";
        }

        // 6. Vérifier les places restantes
        if (evenement.getPlacesRestantes() <= 0) {
            return "L'événement est complet.";
        }

        // 7. Ajouter l'inscription
        boolean inscriptionAjoutee = inscriptionDAO.ajouterInscription(idUtilisateur, idEvenement);

        if (!inscriptionAjoutee) {
            return "Erreur lors de l'inscription.";
        }

        // 8. Diminuer le nombre de places restantes
        boolean placeDiminuee = inscriptionDAO.diminuerPlaceRestante(idEvenement);

        if (!placeDiminuee) {
            return "Inscription ajoutée, mais erreur lors de la mise à jour des places.";
        }

        return "Inscription réussie.";
    }

    public String annulerInscription(int idUtilisateur, int idEvenement) {

        // 1. Vérifier les IDs
        if (idUtilisateur <= 0 || idEvenement <= 0) {
            return "Données invalides.";
        }

        // 2. Vérifier si l'utilisateur est vraiment inscrit
        if (!inscriptionDAO.dejaInscrit(idUtilisateur, idEvenement)) {
            return "Vous n'êtes pas inscrit à cet événement.";
        }

        // 3. Annuler l'inscription
        boolean inscriptionSupprimee = inscriptionDAO.annulerInscription(idUtilisateur, idEvenement);

        if (!inscriptionSupprimee) {
            return "Erreur lors de l'annulation.";
        }

        // 4. Augmenter les places restantes
        boolean placeAugmentee = inscriptionDAO.augmenterPlaceRestante(idEvenement);

        if (!placeAugmentee) {
            return "Inscription annulée, mais erreur lors de la mise à jour des places.";
        }

        return "Inscription annulée avec succès.";
    }
    public List<Evenement> listerMesInscriptions(int idUtilisateur) {
        if (idUtilisateur <= 0) {
            return new ArrayList<>();
        }

        return inscriptionDAO.listerInscriptionsParUtilisateur(idUtilisateur);
    }
    public boolean estDejaInscrit(int idUtilisateur, int idEvenement) {
        if (idUtilisateur <= 0 || idEvenement <= 0) {
            return false;
        }

        return inscriptionDAO.dejaInscrit(idUtilisateur, idEvenement);
    }
}