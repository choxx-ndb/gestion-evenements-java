package test;

import java.sql.Connection; // <--- Ne pas oublier cet import !
import util.DBConnection;

public class Test {
    public static void main(String[] args) {
        
        // Appel de la méthode statique de ton Singleton
        Connection connection = DBConnection.getConnection();
        
        if (connection == null) {
            System.out.println("❌ La connexion a échoué. Vérifie ton Driver ou l'URL.");
        } else {
            System.out.println("✅ La connexion a été établie avec succès !");
            
            // Petit bonus : afficher le nom de la base connectée
            try {
                System.out.println("Base de données : " + connection.getCatalog());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
