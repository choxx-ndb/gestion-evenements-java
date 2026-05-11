package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Evenement;
import util.DBConnection;

public class EvenementDAO implements CommonDAO<Evenement> {

    @Override
    public List<Evenement> selectAll() {
        List<Evenement> liste = new ArrayList<>();
        // Jointure pour récupérer les noms au lieu des simples IDs
        String sql = "SELECT e.*, c.nom as categorieNom, u.nom as organisateurNom " +
                     "FROM evenements e " +
                     "JOIN categories c ON e.categorie_id = c.id " +
                     "JOIN utilisateurs u ON e.organisateur_id = u.id";

        // On récupère la connexion via le Singleton (elle ne sera pas fermée)
        

        // Seuls le PreparedStatement et le ResultSet sont dans le try-with-resources
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("titre"));
                e.setDescription(rs.getString("description"));
                e.setLieu(rs.getString("lieu"));
                e.setCapacite(rs.getInt("capacite"));
                e.setPlacesRestantes(rs.getInt("places_restantes"));
                e.setCategorieId(rs.getInt("categorie_id"));
                e.setCategorieNom(rs.getString("categorieNom"));
                e.setOrganisateurId(rs.getInt("organisateur_id"));
                e.setOrganisateurNom(rs.getString("organisateurNom"));

                // Conversion sécurisée des dates
                Timestamp tsDebut = rs.getTimestamp("date_debut");
                if (tsDebut != null) e.setDateDebut(tsDebut.toLocalDateTime());

                Timestamp tsCrea = rs.getTimestamp("date_creation");
                if (tsCrea != null) e.setDateCreation(tsCrea.toLocalDateTime());

                liste.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur dans findAll: " + ex.getMessage());
            ex.printStackTrace();
        }
        return liste;
    }

    @Override
    public void add(Evenement e) {
        String sql = "INSERT INTO evenements (titre, description, date_debut, lieu, " +
                     "capacite, places_restantes, categorie_id, organisateur_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        
        try (Connection con = DBConnection.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
            
            // Conversion LocalDateTime -> Timestamp pour MySQL
            if (e.getDateDebut() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(e.getDateDebut()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            
            ps.setString(4, e.getLieu());
            ps.setInt(5, e.getCapacite());
            ps.setInt(6, e.getCapacite()); // Initialement, places_restantes = capacite
            ps.setInt(7, e.getCategorieId());
            ps.setInt(8, e.getOrganisateurId());
            
            ps.executeUpdate();
            System.out.println("Événement ajouté avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur dans add: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Méthodes à implémenter plus tard
    @Override 
    public Evenement getById(int id) {
    	Evenement e = null;
        String sql = "SELECT * FROM evenements WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    e = new Evenement();
                    e.setId(rs.getInt("id"));
                    e.setTitre(rs.getString("titre"));
                    e.setDescription(rs.getString("description"));
                    e.setLieu(rs.getString("lieu"));
                    e.setCapacite(rs.getInt("capacite"));
                    e.setPlacesRestantes(rs.getInt("places_restantes"));
                    e.setCategorieId(rs.getInt("categorie_id"));
                    e.setOrganisateurId(rs.getInt("organisateur_id"));
                    
                    Timestamp ts = rs.getTimestamp("date_debut");
                    if (ts != null) e.setDateDebut(ts.toLocalDateTime());
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur dans findById: " + ex.getMessage());
            ex.printStackTrace();
        }
        return e;
    }        
    
    @Override 
    public void update(Evenement e) {
    	
	String sql = "UPDATE evenements SET titre=?, description=?, date_debut=?, lieu=?, " +
                 "capacite=?, categorie_id=? WHERE id=?";
	
	try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

       ps.setString(1, e.getTitre());
       ps.setString(2, e.getDescription());
       ps.setTimestamp(3, java.sql.Timestamp.valueOf(e.getDateDebut()));
       ps.setString(4, e.getLieu());
       ps.setInt(5, e.getCapacite());
       ps.setInt(6, e.getCategorieId());
       ps.setInt(7, e.getId());

       ps.executeUpdate();
       System.out.println("Événement n°" + e.getId() + " mis à jour avec succès !");
       } catch (SQLException ex) {
       System.err.println("Erreur dans update: " + ex.getMessage());
       ex.printStackTrace();
     }
   }
    @Override 
    public void delete(int id) {
        String sql = "DELETE FROM evenements WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Événement n°" + id + " supprimé.");

        } catch (SQLException ex) {
            System.err.println("Erreur dans delete: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    /*public List<Evenement> searchEvenements(String keyword) {
        List<Evenement> liste = new ArrayList<>();
        // On cherche dans le titre OU le lieu
        String sql = "SELECT * FROM evenements WHERE titre LIKE ? OR lieu LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Le % permet de trouver le mot n'importe où dans la phrase
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Utilise ton constructeur ou tes setters habituels
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("title"));
                e.setLieu(rs.getString("lieu"));
                // ... remplis les autres champs ...
                liste.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }*/
}