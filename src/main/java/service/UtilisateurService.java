package service;

import dao.UtilisateurDAO;
import model.Utilisateur;

public class UtilisateurService {

    private UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public Utilisateur authentifier(String email, String motDePass) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        if (motDePass == null || motDePass.trim().isEmpty()) {
            return null;
        }

        return utilisateurDAO.authentifier(email, motDePass);
    }
    public String creerCompte(Utilisateur user) {
        if (user == null) {
            return "Données invalides.";
        }

        if (user.getNom() == null || user.getNom().trim().isEmpty()) {
            return "Le nom est obligatoire.";
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "L'email est obligatoire.";
        }

        if (user.getMotDePass() == null || user.getMotDePass().trim().isEmpty()) {
            return "Le mot de passe est obligatoire.";
        }

        if (utilisateurDAO.emailExiste(user.getEmail())) {
            return "Cet email est déjà utilisé.";
        }

        user.setRole("user");

        boolean resultat = utilisateurDAO.ajouterUtilisateur(user);

        if (resultat) {
            return "Compte créé avec succès.";
        }

        return "Erreur lors de la création du compte.";
    }
}