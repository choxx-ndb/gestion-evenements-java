package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modèle représentant un événement.
 * Correspond exactement à la table "evenements" dans MySQL.
 *
 * RÈGLE : cette classe ne fait QUE stocker des données.
 * Tout le SQL est dans EvenementDAO.java.
 */
public class Evenement {

    // ════════════════════════════════════════════════════════
    //  ATTRIBUTS — correspondent aux colonnes de la table SQL
    // ════════════════════════════════════════════════════════

    private int           id;               // INT AUTO_INCREMENT
    private String        titre;            // VARCHAR(200) NOT NULL
    private String        description;      // TEXT
    private LocalDateTime dateDebut;        // DATETIME NOT NULL
    private String        lieu;             // VARCHAR(200) NOT NULL
    private int           capacite;         // INT NOT NULL DEFAULT 100
    private int           placesRestantes;  // INT NOT NULL DEFAULT 100
    private int           categorieId;      // INT (FK → categories.id)
    private String        categorieNom;     // Non en BDD : rempli par JOIN SQL
    private int           organisateurId;   // INT (FK → utilisateurs.id)
    private String        organisateurNom;  // Non en BDD : rempli par JOIN SQL
    private LocalDateTime dateCreation;     // DATETIME DEFAULT CURRENT_TIMESTAMP

    // ════════════════════════════════════════════════════════
    //  CONSTRUCTEURS
    // ════════════════════════════════════════════════════════

    /**
     * Constructeur vide — obligatoire pour JDBC et les frameworks.
     * Le DAO l'utilise avec les setters pour construire l'objet depuis un ResultSet.
     */
    public Evenement() {}

    /**
     * Constructeur pour la CRÉATION d'un nouvel événement (INSERT).
     * On ne passe PAS l'id ni dateCreation : MySQL les génère automatiquement.
     */
    public Evenement(String titre, String description, LocalDateTime dateDebut,
                     String lieu, int capacite, int categorieId, int organisateurId) {
        this.titre          = titre;
        this.description    = description;
        this.dateDebut      = dateDebut;
        this.lieu           = lieu;
        this.capacite       = capacite;
        this.placesRestantes = capacite; // Au départ : toutes les places sont libres
        this.categorieId    = categorieId;
        this.organisateurId = organisateurId;
    }

    /**
     * Constructeur complet — utilisé par le DAO lors d'un SELECT.
     * Tous les champs sont fournis, y compris id et dateCreation lus depuis MySQL.
     */
    public Evenement(int id, String titre, String description, LocalDateTime dateDebut,
                     String lieu, int capacite, int placesRestantes,
                     int categorieId, int organisateurId, LocalDateTime dateCreation) {
        this.id              = id;
        this.titre           = titre;
        this.description     = description;
        this.dateDebut       = dateDebut;
        this.lieu            = lieu;
        this.capacite        = capacite;
        this.placesRestantes = placesRestantes;
        this.categorieId     = categorieId;
        this.organisateurId  = organisateurId;
        this.dateCreation    = dateCreation;
    }

    // ════════════════════════════════════════════════════════
    //  MÉTHODES UTILITAIRES
    //  Accessibles dans les JSP via ${evenement.nomMethode}
    // ════════════════════════════════════════════════════════

    /**
     * Date formatée pour l'affichage dans les JSP.
     * Utilisation JSP : ${evenement.dateFormatee}
     * Résultat : "15/07/2025 à 20h00"
     */
    public String getDateFormatee() {
        if (dateDebut == null) return "";
        return dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH'h'mm"));
    }

    /**
     * Date formatée pour les <input type="datetime-local"> dans formulaire.jsp.
     * Indispensable pour pré-remplir le champ date lors d'une MODIFICATION.
     * Utilisation JSP : value="${evenement.datePourInput}"
     * Résultat : "2025-07-15T20:00"
     */
    public String getDatePourInput() {
        if (dateDebut == null) return "";
        return dateDebut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    /**
     * Date de création formatée pour l'affichage.
     * Utilisation JSP : ${evenement.dateCreationFormatee}
     * Résultat : "10/06/2025 14:35"
     */
    public String getDateCreationFormatee() {
        if (dateCreation == null) return "";
        return dateCreation.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    /**
     * Retourne true si l'événement a encore des places disponibles.
     * Utilisation JSP : <c:if test="${evenement.disponible}">...</c:if>
     */
    public boolean isDisponible() {
        return placesRestantes > 0;
    }

    /**
     * Retourne un résumé lisible — utile pour déboguer dans le main() du DAO.
     */
    @Override
    public String toString() {
        return "Evenement{"
             + "id=" + id
             + ", titre='" + titre + '\''
             + ", lieu='" + lieu + '\''
             + ", date=" + getDateFormatee()
             + ", places=" + placesRestantes + "/" + capacite
             + '}';
    }

    // ════════════════════════════════════════════════════════
    //  GETTERS ET SETTERS
    //  Dans Eclipse : Source → Generate Getters and Setters
    // ════════════════════════════════════════════════════════

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public int getPlacesRestantes() { return placesRestantes; }
    public void setPlacesRestantes(int placesRestantes) { this.placesRestantes = placesRestantes; }

    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    public String getCategorieNom() { return categorieNom; }
    public void setCategorieNom(String categorieNom) { this.categorieNom = categorieNom; }

    public int getOrganisateurId() { return organisateurId; }
    public void setOrganisateurId(int organisateurId) { this.organisateurId = organisateurId; }

    public String getOrganisateurNom() { return organisateurNom; }
    public void setOrganisateurNom(String organisateurNom) { this.organisateurNom = organisateurNom; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
