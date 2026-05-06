package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Evenement;
import util.DBConnection;

public class InscriptionDAO {

    public boolean ajouterInscription(int idUtilisateur, int idEvenement) {
        String sql = "INSERT INTO inscriptions (utilisateur_id, evenement_id, statut) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idEvenement);
            ps.setString(3, "CONFIRMEE");

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dejaInscrit(int idUtilisateur, int idEvenement) {
        String sql = "SELECT COUNT(*) FROM inscriptions WHERE utilisateur_id = ? AND evenement_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idEvenement);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int compterInscrits(int idEvenement) {
        String sql = "SELECT COUNT(*) FROM inscriptions WHERE evenement_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEvenement);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean annulerInscription(int idUtilisateur, int idEvenement) {
        String sql = "DELETE FROM inscriptions WHERE utilisateur_id = ? AND evenement_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idEvenement);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean diminuerPlaceRestante(int idEvenement) {
        String sql = "UPDATE evenements SET places_restantes = places_restantes - 1 WHERE id = ? AND places_restantes > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEvenement);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean augmenterPlaceRestante(int idEvenement) {
        String sql = "UPDATE evenements SET places_restantes = places_restantes + 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEvenement);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Evenement> listerInscriptionsParUtilisateur(int idUtilisateur) {
        List<Evenement> liste = new ArrayList<>();

        String sql = "SELECT e.*, c.nom AS categorieNom, u.nom AS organisateurNom " +
                     "FROM inscriptions i " +
                     "JOIN evenements e ON i.evenement_id = e.id " +
                     "JOIN categories c ON e.categorie_id = c.id " +
                     "JOIN utilisateurs u ON e.organisateur_id = u.id " +
                     "WHERE i.utilisateur_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);

            try (ResultSet rs = ps.executeQuery()) {
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

                    Timestamp tsDebut = rs.getTimestamp("date_debut");
                    if (tsDebut != null) {
                        e.setDateDebut(tsDebut.toLocalDateTime());
                    }

                    liste.add(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}