package model;

public class Categorie {
    private int id;
    private String nom;

    // Constructeur vide (nécessaire pour les Java Beans)
    public Categorie() {
    }

    // Constructeur avec paramètres
    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Optionnel : toString pour faciliter le débogage
    @Override
    public String toString() {
        return "Categorie [id=" + id + ", nom=" + nom + "]";
    }
}