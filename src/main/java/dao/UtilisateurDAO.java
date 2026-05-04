package dao;

import java.sql.*;
import model.Utilisateur;
import util.DBConnection;

public class UtilisateurDAO {

    /**
     * Vérifie les identifiants et retourne l'utilisateur s'il existe
     */
    public Utilisateur authentifier(String email, String password) {
        Utilisateur user = null;
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_pass = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    // On ne stocke pas forcément le mot de passe en session pour la sécurité
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}