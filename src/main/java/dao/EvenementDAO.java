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
        String sql = "SELECT e.*, c.nom as categorieNom, u.nom as organisateurNom " +
                     "FROM evenements e " +
                     "JOIN categories c ON e.categorie_id = c.id " +
                     "JOIN utilisateurs u ON e.organisateur_id = u.id";
 
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("titre"));
                e.setDescription(rs.getString("description"));
                e.setLieu(rs.getString("lieu"));
                e.setPhoto(rs.getString("photo"));               // ✅ AJOUTÉ
                e.setCapacite(rs.getInt("capacite"));
                e.setPlacesRestantes(rs.getInt("places_restantes"));
                e.setCategorieId(rs.getInt("categorie_id"));
                e.setCategorieNom(rs.getString("categorieNom"));
                e.setOrganisateurId(rs.getInt("organisateur_id"));
                e.setOrganisateurNom(rs.getString("organisateurNom"));
 
                Timestamp tsDebut = rs.getTimestamp("date_debut");
                if (tsDebut != null) e.setDateDebut(tsDebut.toLocalDateTime());
 
                Timestamp tsCrea = rs.getTimestamp("date_creation");
                if (tsCrea != null) e.setDateCreation(tsCrea.toLocalDateTime());
 
                liste.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur dans selectAll: " + ex.getMessage());
            ex.printStackTrace();
        }
        return liste;
    }
 
    @Override
    public void add(Evenement e) {
        // ✅ AJOUTÉ : colonne photo dans l'INSERT
        String sql = "INSERT INTO evenements (titre, description, date_debut, lieu, photo, " +
                     "capacite, places_restantes, categorie_id, organisateur_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
 
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
 
            if (e.getDateDebut() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(e.getDateDebut()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
 
            ps.setString(4, e.getLieu());
            ps.setString(5, e.getPhoto());                       // ✅ AJOUTÉ
            ps.setInt(6, e.getCapacite());
            ps.setInt(7, e.getCapacite());
            ps.setInt(8, e.getCategorieId());
            ps.setInt(9, e.getOrganisateurId());
 
            ps.executeUpdate();
            System.out.println("Événement ajouté avec succès ! Photo = " + e.getPhoto());
 
        } catch (SQLException ex) {
            System.err.println("Erreur dans add: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    @Override
    public Evenement getById(int id) {
        Evenement e = null;
        String sql = "SELECT e.*, " +
                "c.nom as categorieNom, " +
                "u.nom as organisateurNom " +
                "FROM evenements e " +
                "LEFT JOIN categories c ON e.categorie_id = c.id " +
                "LEFT JOIN utilisateurs u ON e.organisateur_id = u.id " +
                "WHERE e.id = ?";
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
                    e.setPhoto(rs.getString("photo"));           // ✅ AJOUTÉ
                    e.setCapacite(rs.getInt("capacite"));
                    e.setPlacesRestantes(rs.getInt("places_restantes"));
                    e.setCategorieId(rs.getInt("categorie_id"));
                    e.setCategorieNom(rs.getString("categorieNom"));
                    e.setOrganisateurId(rs.getInt("organisateur_id"));
                    
 
                    Timestamp ts = rs.getTimestamp("date_debut");
                    if (ts != null) e.setDateDebut(ts.toLocalDateTime());
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur dans getById: " + ex.getMessage());
            ex.printStackTrace();
        }
        return e;
    }
 
    @Override
    public void update(Evenement e) {
        // ✅ AJOUTÉ : photo dans l'UPDATE
        String sql = "UPDATE evenements SET titre=?, description=?, date_debut=?, lieu=?, " +
                     "photo=?, capacite=?, categorie_id=? WHERE id=?";
 
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, e.getTitre());
            ps.setString(2, e.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDateDebut()));
            ps.setString(4, e.getLieu());
            ps.setString(5, e.getPhoto());                       // ✅ AJOUTÉ
            ps.setInt(6, e.getCapacite());
            ps.setInt(7, e.getCategorieId());
            ps.setInt(8, e.getId());
 
            ps.executeUpdate();
            System.out.println("Événement n°" + e.getId() + " mis à jour ! Photo = " + e.getPhoto());
 
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
}
 