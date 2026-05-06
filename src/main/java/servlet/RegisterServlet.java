package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Utilisateur;
import service.UtilisateurService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UtilisateurService utilisateurService;

    @Override
    public void init() throws ServletException {
        utilisateurService = new UtilisateurService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String motDePass = request.getParameter("motDePass");

        Utilisateur user = new Utilisateur();
        user.setNom(nom);
        user.setEmail(email);
        user.setMotDePass(motDePass);
        user.setRole("user");

        String message = utilisateurService.creerCompte(user);

        if ("Compte créé avec succès.".equals(message)) {
            request.setAttribute("message", message);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("erreur", message);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}