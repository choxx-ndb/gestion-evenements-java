package model;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String motDePass; // Correspond à mot_de_pass dans ton SQL
    private String role;

    public Utilisateur() {}

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePass() { return motDePass; }
    public void setMotDePass(String motDePass) { this.motDePass = motDePass; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}