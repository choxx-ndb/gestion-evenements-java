package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.InscriptionService;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        inscriptionService = new InscriptionService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idUtilisateur = (int) session.getAttribute("idUtilisateur");

        String idEvenementParam = request.getParameter("idEvenement");

        if (idEvenementParam == null || idEvenementParam.trim().isEmpty()) {
            session.setAttribute("message", "Événement invalide.");
            response.sendRedirect("evenement?action=list");
            return;
        }

        try {
            int idEvenement = Integer.parseInt(idEvenementParam);

            String action = request.getParameter("action");

            if ("annuler".equals(action)) {
                String message = inscriptionService.annulerInscription(idUtilisateur, idEvenement);
                session.setAttribute("message", message);
                response.sendRedirect("mes-inscriptions");
                return;
            }

            String message = inscriptionService.inscrireUtilisateur(idUtilisateur, idEvenement);
            session.setAttribute("message", message);
            response.sendRedirect("evenement?action=list");

        } catch (NumberFormatException e) {
            session.setAttribute("message", "ID événement invalide.");
            response.sendRedirect("evenement?action=list");
        }
    }
}