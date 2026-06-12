package servlet;
 
import jakarta.servlet.ServletException;
import service.InscriptionService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;                            // ✅ AJOUTÉ
 
import java.io.File;                                         // ✅ AJOUTÉ
import java.io.IOException;
import java.nio.file.Paths;                                  // ✅ AJOUTÉ
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;                                       // ✅ AJOUTÉ
 
import dao.EvenementDAO;
import dao.CategorieDAO;
import model.Evenement;
import model.Utilisateur;
 
@WebServlet("/evenement")
// ✅ CONFIG MULTIPART : OBLIGATOIRE pour recevoir des fichiers
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 Mo en mémoire
    maxFileSize       = 1024 * 1024 * 5,  // 5 Mo max par fichier
    maxRequestSize    = 1024 * 1024 * 10  // 10 Mo total
)
public class EvenementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    // ✅ Dossier où seront sauvegardées les images
    private static final String UPLOAD_DIR = "uploads";
 
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
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
 
        String action = request.getParameter("action");
        if (action == null) action = "list";
 
        try {
            switch (action) {
                case "detail": showEventDetail(request, response); break;
                case "new":    showNewForm(request, response);     break;
                case "edit":   showEditForm(request, response);    break;
                case "delete": deleteEvenement(request, response); break;
                default:       listEvenement(request, response);   break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        saveOrUpdateEvenement(request, response);
    }
 
    protected void listEvenement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        List<Evenement> tousLesEvenements = evenementDAO.selectAll();
        List<Evenement> evenementsFiltres = new ArrayList<>();
 
        String searchKeyword = request.getParameter("search");
        String categorieParam = request.getParameter("categorie");
 
        for (Evenement e : tousLesEvenements) {
            boolean matches = true;
 
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = searchKeyword.toLowerCase().trim();
                boolean titleMatch = e.getTitre().toLowerCase().contains(keyword);
                boolean descMatch  = e.getDescription() != null
                                     && e.getDescription().toLowerCase().contains(keyword);
                boolean lieuMatch  = e.getLieu().toLowerCase().contains(keyword);
                matches = titleMatch || descMatch || lieuMatch;
            }
 
            if (categorieParam != null && !categorieParam.isEmpty() && matches) {
                try {
                    int categorieId = Integer.parseInt(categorieParam);
                    matches = (e.getCategorieId() == categorieId);
                } catch (NumberFormatException ex) { }
            }
 
            if (matches) evenementsFiltres.add(e);
        }
 
        request.setAttribute("evenements", evenementsFiltres);
        request.setAttribute("categories", categorieDAO.selectAll());
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedCategory", categorieParam);
 
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("idUtilisateur") != null) {
            int idUtilisateur = (int) session.getAttribute("idUtilisateur");
            request.setAttribute("mesInscriptions",
                inscriptionService.listerMesInscriptions(idUtilisateur));
        }
 
        request.getRequestDispatcher("listeEvenements.jsp").forward(request, response);
    }
 
    protected void showEventDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Evenement evenement = evenementDAO.getById(id);
 
            if (evenement == null) {
                response.sendRedirect("evenement?action=list");
                return;
            }
 
            request.setAttribute("evenement", evenement);
 
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
        request.setAttribute("categories", categorieDAO.selectAll());
        request.getRequestDispatcher("formulaireEvenement.jsp").forward(request, response);
    }
 
    protected void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Evenement existingEvenement = evenementDAO.getById(id);
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
 
    /**
     * ✅ MÉTHODE MODIFIÉE : Gère maintenant l'upload de photo
     */
    protected void saveOrUpdateEvenement(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
 
        try {
            String idStr        = request.getParameter("id");
            String titre        = request.getParameter("titre");
            String description  = request.getParameter("description");
            String lieu         = request.getParameter("lieu");
            int    capacite     = Integer.parseInt(request.getParameter("capacite"));
            int    categorieId  = Integer.parseInt(request.getParameter("categorie_id"));
            String dateStr      = request.getParameter("date_debut");
 
            // ✅ Récupérer la photo existante (en mode édition)
            String photoExistante = request.getParameter("photoExistante");
 
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
 
            // ═══════════════════════════════════════════════════════
            // ✅ GESTION DE L'UPLOAD DE LA PHOTO
            // ═══════════════════════════════════════════════════════
            String nomPhoto = handlePhotoUpload(request, photoExistante);
            ev.setPhoto(nomPhoto);
 
            System.out.println("=== DEBUG SAVE ===");
            System.out.println("Titre  : " + titre);
            System.out.println("Photo  : " + nomPhoto);
            System.out.println("==================");
 
            if (idStr == null || idStr.isEmpty()) {
                evenementDAO.add(ev);
                session.setAttribute("message", "✅ Événement créé avec succès!");
            } else {
                ev.setId(Integer.parseInt(idStr));
                evenementDAO.update(ev);
                session.setAttribute("message", "✅ Événement modifié avec succès!");
            }
 
            response.sendRedirect("evenement?action=list");
 
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("evenement?action=list&erreur=" + e.getMessage());
        }
    }
 
    /**
     * ✅ NOUVELLE MÉTHODE : Sauvegarde le fichier image sur le disque
     * @return Le nom unique du fichier sauvegardé (ou photoExistante si pas de nouveau fichier)
     */
    private String handlePhotoUpload(HttpServletRequest request, String photoExistante)
            throws IOException, ServletException {
 
        // 1. Récupérer le fichier uploadé depuis le formulaire
        Part filePart = request.getPart("cover");
 
        // 2. Si aucun fichier n'a été choisi, garder l'ancienne photo
        if (filePart == null || filePart.getSize() == 0) {
            System.out.println("Pas de nouvelle photo → on garde : " + photoExistante);
            return (photoExistante != null && !photoExistante.isEmpty()) ? photoExistante : null;
        }
 
        // 3. Récupérer le nom original du fichier
        String originalFileName = Paths.get(filePart.getSubmittedFileName())
                                       .getFileName().toString();
 
        if (originalFileName.isEmpty()) {
            return photoExistante;
        }
 
        // 4. Générer un nom UNIQUE pour éviter les conflits
        // Exemple : "image.jpg" → "1abc234-image.jpg"
        String uniqueFileName = UUID.randomUUID().toString().substring(0, 8) + "-" + originalFileName;
 
        // 5. Déterminer le chemin du dossier uploads
        // Le fichier sera dans : <Tomcat>/webapps/<contextPath>/uploads/
        String uploadPath = getServletContext().getRealPath("/") + UPLOAD_DIR;
 
        // 6. Créer le dossier s'il n'existe pas
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("Dossier uploads créé : " + uploadPath);
        }
 
        // 7. Sauvegarder le fichier sur le disque
        String filePath = uploadPath + File.separator + uniqueFileName;
        filePart.write(filePath);
 
        System.out.println("✅ Photo sauvegardée : " + filePath);
 
        // 8. Retourner uniquement le nom du fichier (pour la BD)
        return uniqueFileName;
    }
}
 