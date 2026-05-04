package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Configuration de la base de données
    // On garde le fuseau horaire de Casablanca comme dans ton code original
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_evenements"
                                    + "?useSSL=false&serverTimezone=Africa/Casablanca";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Bloc statique pour charger le Driver une seule fois au démarrage de l'application
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver MySQL introuvable !");
            e.printStackTrace();
        }
    }

    /**
     * Retourne une nouvelle connexion à la base de données.
     * Cette méthode est utilisée dans le try-with-resources de tes DAO.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Le constructeur est privé pour empêcher de créer des instances de cette classe utilitaire
    private DBConnection() {}
}
