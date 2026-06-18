package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evenement {

    private int           id;
    private String        titre;
    private String        description;
    private double        prix;
    private LocalDateTime dateDebut;
    private String        lieu;
    private int           capacite;
    private int           placesRestantes;
    private int           categorieId;
    private String        categorieNom;
    private int           organisateurId;
    private String        organisateurNom;
    private LocalDateTime dateCreation;
    private String        photo;
    

    public Evenement() {}

    public Evenement(String titre, String description, LocalDateTime dateDebut,
                     String lieu, int capacite, int categorieId, int organisateurId) {
        this.titre           = titre;
        this.description     = description;
        this.dateDebut       = dateDebut;
        this.lieu            = lieu;
        this.capacite        = capacite;
        this.placesRestantes = capacite;
        this.categorieId     = categorieId;
        this.organisateurId  = organisateurId;
        this.prix            = 0.0;
    }


    public String getDateFormatee() {
        if (dateDebut == null) return "";
        return dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH'h'mm"));
    }

    public String getDatePourInput() {
        if (dateDebut == null) return "";
        return dateDebut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public String getDateCreationFormatee() {
        if (dateCreation == null) return "";
        return dateCreation.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
    }

    public boolean isDisponible() {
        return placesRestantes > 0;
    }

    public String getPrixAffiche() {
        if (prix <= 0) return "Gratuit";
        return String.format("%.2f MAD", prix);
    }

    public boolean isGratuit() {
        return prix <= 0;
    }

    @Override
    public String toString() {
        return "Evenement{id=" + id + ", titre='" + titre + '\''
             + ", lieu='" + lieu + '\''
             + ", date=" + getDateFormatee()
             + ", places=" + placesRestantes + "/" + capacite
             + ", prix=" + getPrixAffiche() + '}';
    }

    //GETTERS ET SETTERS

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
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getOrganisateurNom() { return organisateurNom; }
    public void setOrganisateurNom(String organisateurNom) { this.organisateurNom = organisateurNom; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    
}