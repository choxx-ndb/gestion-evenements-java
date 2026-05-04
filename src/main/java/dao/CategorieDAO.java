package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categorie;
import util.DBConnection;

// On précise que ce DAO gère des objets de type Categorie
public class CategorieDAO implements CommonDAO<Categorie> {

    @Override
    public List<Categorie> selectAll() {
        List<Categorie> liste = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY nom";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categorie c = new Categorie();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                liste.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans CategorieDAO.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    // Même si tu ne les utilises pas tout de suite pour les catégories, 
    // l'interface t'oblige à les déclarer.
    
    @Override
    public void add(Categorie c) {
        // Optionnel pour le moment
    }

    @Override
    public void update(Categorie c) {
        // Optionnel pour le moment
    }

    @Override
    public void delete(int id) {
        // Optionnel pour le moment
    }

    @Override
    public Categorie getById(int id) {
        // Utile si tu veux retrouver une catégorie spécifique par son ID
        return null; 
    }
}