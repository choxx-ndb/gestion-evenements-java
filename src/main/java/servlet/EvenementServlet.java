package servlet;

import jakarta.servlet.ServletException;
import service.InscriptionService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.MultipartConfig;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.EvenementDAO;
import dao.CategorieDAO;
import model.Evenement;
import model.Utilisateur;

@WebServlet("/evenement")
@MultipartConfig
public class EvenementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EvenementDAO evenementDAO;
    private CategorieDAO categorieDAO;
    private InscriptionService inscriptionService;

    @Override
    public void init() {
        evenementDAO = new EvenementDAO();
        categorieDAO = new CategorieDAO();
        inscriptionService = new InscriptionService();
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
                case "detail":
                    showEventDetail(request, response);
                    break;
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

    /**
     * ✅ MODIFIÉ: Ajout de la recherche et du filtrage par catégorie
     */
    protected void listEvenement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Récupérer TOUS les événements
        List<Evenement> tousLesEvenements = evenementDAO.selectAll();
        List<Evenement> evenementsFiltrés = new ArrayList<>();
        
        // Récupérer les paramètres de recherche et filtrage
        String searchKeyword = request.getParameter("search");
        String categorieParam = request.getParameter("categorie");
        
        // FILTRER les événements
        for (Evenement e : tousLesEvenements) {
            boolean matches = true;
            
            // Filtrer par recherche (titre + description + lieu)
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = searchKeyword.toLowerCase().trim();
                boolean titleMatch = e.getTitre().toLowerCase().contains(keyword);
                boolean descMatch = e.getDescription().toLowerCase().contains(keyword);
                boolean lieuMatch = e.getLieu().toLowerCase().contains(keyword);
                
                matches = titleMatch || descMatch || lieuMatch;
            }
            
            // Filtrer par catégorie
            if (categorieParam != null && !categorieParam.isEmpty() && matches) {
                try {
                    int categorieId = Integer.parseInt(categorieParam);
                    matches = (e.getCategorieId() == categorieId);
                } catch (NumberFormatException ex) {
                    // Si l'ID n'est pas valide, ignorer le filtre
                }
            }
            
            if (matches) {
                evenementsFiltrés.add(e);
            }
        }
        
        // Envoyer les données à la JSP
        request.setAttribute("evenements", evenementsFiltrés);
        request.setAttribute("categories", categorieDAO.selectAll());
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedCategory", categorieParam);
        
        // Récupérer les inscriptions de l'utilisateur
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("idUtilisateur") != null) {
            int idUtilisateur = (int) session.getAttribute("idUtilisateur");
            request.setAttribute("mesInscriptions", inscriptionService.listerMesInscriptions(idUtilisateur));
        }

        request.getRequestDispatcher("listeEvenements.jsp").forward(request, response);
    }

    /**
     * ✅ NOUVEAU: Afficher les détails d'un événement spécifique
     */
    protected void showEventDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Evenement evenement = evenementDAO.getById(id);
            
            if (evenement == null) {
                response.sendRedirect("evenement?action=list");
                return;
            }
            
            // Envoyer l'événement à la JSP
            request.setAttribute("evenement", evenement);
            
            // Vérifier si l'utilisateur est déjà inscrit
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("idUtilisateur") != null) {
                int idUtilisateur = (int) session.getAttribute("idUtilisateur");
                boolean dejaInscrit = inscriptionService.estDejaInscrit(idUtilisateur, id);
                request.setAttribute("dejaInscrit", dejaInscrit);
            }
            
            request.getRequestDispatcher("evenementDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("evenement?action=list");
        }
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

        // Récupérer l'ID de l'utilisateur connecté
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("user");

        Evenement ev = new Evenement();
        ev.setTitre(titre);
        ev.setDescription(description);
        ev.setLieu(lieu);
        ev.setCapacite(capacite);
        ev.setCategorieId(categorieId);
        ev.setOrganisateurId(user.getId());
        
        if (dateStr != null && !dateStr.isEmpty()) {
            ev.setDateDebut(LocalDateTime.parse(dateStr));
        }

        if (idStr == null || idStr.isEmpty()) {
            evenementDAO.add(ev);
            // Message de succès
            session.setAttribute("message", "✅ Événement créé avec succès!");
        } else {
            ev.setId(Integer.parseInt(idStr));
            evenementDAO.update(ev);
            // Message de succès
            session.setAttribute("message", "✅ Événement modifié avec succès!");
        }
        
        // Rediriger vers la liste
        response.sendRedirect("evenement?action=list");
    }
}
