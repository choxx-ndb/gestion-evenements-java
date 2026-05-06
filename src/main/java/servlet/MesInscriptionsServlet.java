package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Evenement;
import service.InscriptionService;

@WebServlet("/mes-inscriptions")
public class MesInscriptionsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        inscriptionService = new InscriptionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idUtilisateur = (int) session.getAttribute("idUtilisateur");

        List<Evenement> mesInscriptions = inscriptionService.listerMesInscriptions(idUtilisateur);

        request.setAttribute("mesInscriptions", mesInscriptions);

        request.getRequestDispatcher("mesInscriptions.jsp").forward(request, response);
    }
}