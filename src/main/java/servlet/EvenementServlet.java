package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import dao.EvenementDAO;
import dao.CategorieDAO;
import model.Evenement;
import model.Utilisateur;

@WebServlet("/evenement")
public class EvenementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EvenementDAO evenementDAO;
    private CategorieDAO categorieDAO;

    @Override
    public void init() {
        evenementDAO = new EvenementDAO();
        categorieDAO = new CategorieDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. SÉCURITÉ : Vérifier si l'utilisateur est connecté
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteEvenement(request, response);
                    break;
                default:
                    listEvenement(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // SÉCURITÉ : Vérifier la session aussi en POST
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        saveOrUpdateEvenement(request, response);
    }

    // --- MÉTHODES DE LOGIQUE ---

    protected void listEvenement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Evenement> listEvenement = evenementDAO.selectAll();
        request.setAttribute("evenements", listEvenement);
        request.getRequestDispatcher("listeEvenements.jsp").forward(request, response);
    }

    protected void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chargement des catégories pour la liste déroulante
        request.setAttribute("categories", categorieDAO.selectAll());
        request.getRequestDispatcher("formulaireEvenement.jsp").forward(request, response);
    }

    protected void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Evenement existingEvenement = evenementDAO.getById(id);
        
        // On passe les catégories ET l'événement existant au formulaire
        request.setAttribute("categories", categorieDAO.selectAll());
        request.setAttribute("evenement", existingEvenement);
        
        request.getRequestDispatcher("formulaireEvenement.jsp").forward(request, response);
    }

    protected void deleteEvenement(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Optionnel : Vérifier ici si le rôle est bien 'admin' avant de supprimer
        int id = Integer.parseInt(request.getParameter("id"));
        evenementDAO.delete(id);
        response.sendRedirect("evenement?action=list");
    }

    protected void saveOrUpdateEvenement(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idStr = request.getParameter("id");
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String lieu = request.getParameter("lieu");
        int capacite = Integer.parseInt(request.getParameter("capacite"));
        int categorieId = Integer.parseInt(request.getParameter("categorie_id"));
        String dateStr = request.getParameter("date_debut");

        // Récupérer l'ID de l'utilisateur connecté pour l'associer à l'événement
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("user");

        Evenement ev = new Evenement();
        ev.setTitre(titre);
        ev.setDescription(description);
        ev.setLieu(lieu);
        ev.setCapacite(capacite);
        ev.setCategorieId(categorieId);
        ev.setOrganisateurId(user.getId()); // Utilisation de l'ID réel de la session
        
        if (dateStr != null && !dateStr.isEmpty()) {
            ev.setDateDebut(LocalDateTime.parse(dateStr));
        }

        if (idStr == null || idStr.isEmpty()) {
            evenementDAO.add(ev);
        } else {
            ev.setId(Integer.parseInt(idStr));
            evenementDAO.update(ev);
        }
        
        response.sendRedirect("evenement?action=list");
    }
}