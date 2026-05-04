package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import dao.UtilisateurDAO;
import model.Utilisateur;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtilisateurDAO utilisateurDAO;

    public void init() {
        utilisateurDAO = new UtilisateurDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Affiche la page de login
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Utilisateur user = utilisateurDAO.authentifier(email, password);

        if (user != null) {
            // Création de la session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // Redirection vers la liste des événements
            response.sendRedirect("evenement?action=list");
        } else {
            // Retour au login avec message d'erreur
            request.setAttribute("erreur", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}