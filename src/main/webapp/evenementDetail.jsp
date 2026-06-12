<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Evenement" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.time.LocalDateTime" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détail Événement</title>

    <!-- Bootstrap Icons CDN -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: grey; line-height: 1.6;
        }

        nav {
            background: #343a40; color: white;
            padding: 15px 0; margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .navbar-container {
            max-width: 1200px; margin: 0 auto; padding: 0 20px;
            display: flex; justify-content: space-between; align-items: center;
        }

        .navbar-brand { font-size: 1.5rem; font-weight: bold; color: white; }
        .navbar-menu { display: flex; gap: 20px; align-items: center; }
        .navbar-menu a { color: white; text-decoration: none; transition: color 0.2s; }
        .navbar-menu a:hover { color: #EBEBEB; }

        .container { max-width: 900px; margin: 0 auto; padding: 0 20px; }

        .back-link {
            display: inline-flex; align-items: center; gap: 8px;
            margin-bottom: 20px; color: black; text-decoration: none;
            font-weight: 500; transition: color 0.2s;
        }
        .back-link:hover { color: grey; }
        .back-link i { font-size: 1.1rem; }

        .event-image-section {
            width: 100%;
            height: auto;
            max-height: 500px;
            border-radius: 12px;
            overflow: hidden;
            background-color: #000;
            margin-bottom: 30px;
        }

        .event-image-section img {
            width: 100%;
            height: 100%;
            object-fit: contain;
            background-color: #000;
        }

        .event-image-section.no-photo {
            background: linear-gradient(135deg, #E6F1FB 0%, #B5D4F4 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 200px;
        }

        .event-image-section.no-photo i {
            font-size: 5rem;
            color: #5a9fd4;
        }

        .event-info {
            background: #F0FAF6; border-radius: 12px;
            padding: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .event-header {
            display: flex; justify-content: space-between;
            align-items: start; margin-bottom: 20px;
            border-bottom: 1px solid #ddd; padding-bottom: 20px;
        }

        .event-title-section h1 {
            font-size: 2rem; font-weight: 600; color: #333; margin-bottom: 10px;
        }

        .event-badge {
            display: inline-block; padding: 8px 16px;
            background-color: #333; color: white;
            border-radius: 16px; font-size: 0.9rem;
            font-weight: 600; margin-right: 10px;
        }

        .event-price { font-size: 1.8rem; font-weight: 700; color: black; }

        .details-grid {
            display: grid; grid-template-columns: 1fr 1fr;
            gap: 30px; margin-bottom: 30px;
        }

        .detail-item { display: flex; gap: 15px; align-items: flex-start; }

        .detail-icon {
            font-size: 1.5rem;
            min-width: 30px;
            color: #343a40;
            margin-top: 2px;
        }

        .detail-content h3 {
            font-size: 0.9rem; color: #999; text-transform: uppercase;
            letter-spacing: 0.05em; margin-bottom: 5px;
        }
        .detail-content p { font-size: 1.1rem; color: #333; font-weight: 500; }

        .description-section {
            margin-bottom: 30px; padding-bottom: 30px;
            border-bottom: 1px solid #ddd;
        }
        .description-section h2 {
            font-size: 1.3rem; color: #333; margin-bottom: 15px;
            display: flex; align-items: center; gap: 8px;
        }
        .description-section p { color: #666; line-height: 1.8; }

        .places-section {
            background-color: #E6F1FB; padding: 20px;
            border-radius: 8px; margin-bottom: 30px;
        }
        .places-section h3 {
            margin-bottom: 15px; color: #333;
            display: flex; align-items: center; gap: 8px;
        }

        .progress-bar {
            height: 8px; background-color: #e9ecef;
            border-radius: 4px; overflow: hidden; margin-bottom: 10px;
        }
        .progress-fill { height: 100%; background-color: black; transition: width 0.3s; }
        .places-info { font-size: 0.95rem; color: #666; }

        .action-buttons { display: flex; gap: 12px; flex-wrap: wrap; }

        .btn {
            padding: 12px 24px; border: none; border-radius: 8px;
            font-size: 1rem; cursor: pointer; text-decoration: none;
            display: inline-flex; align-items: center; gap: 8px;
            transition: all 0.2s; font-family: inherit; font-weight: 500;
        }

        .btn i { font-size: 1.1rem; }

        .btn-primary { background-color: black; color: white; flex: 1; justify-content: center; }
        .btn-primary:hover { background-color: grey; transform: translateY(-2px); }
        .btn-secondary { background-color: #6c757d; color: white; }
        .btn-secondary:hover { background-color: #5a6268; }
        .btn-edit { background-color: blue; color: white; }
        .btn-edit:hover { background-color: #e0a800; }
        .btn-delete { background-color: #dc3545; color: white; }
        .btn-delete:hover { background-color: #c82333; }

        .status-message {
            padding: 12px 16px; border-radius: 8px;
            margin-bottom: 20px; font-weight: 500;
            display: flex; align-items: center; gap: 8px;
        }
        .status-message i { font-size: 1.1rem; }
        .status-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .status-danger  { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .status-info    { background-color: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }

        @media (max-width: 768px) {
            .event-image-section { height: 250px; }
            .event-header { flex-direction: column; gap: 15px; }
            .details-grid { grid-template-columns: 1fr; gap: 20px; }
            .action-buttons { flex-direction: column; }
            .btn { width: 100%; }
        }
    </style>
</head>
<body>

    <%@ include file="header.jsp" %>

    <div class="container">

        <a href="evenement?action=list" class="back-link">
            <i class="bi bi-arrow-left-circle"></i> Retour à la liste
        </a>

        <%
            Evenement evenement = (Evenement) request.getAttribute("evenement");
            Boolean dejaInscrit = (Boolean) request.getAttribute("dejaInscrit");
            Utilisateur user = (Utilisateur) session.getAttribute("user");

            if (evenement != null) {
                boolean evenementPasse = (evenement.getDateDebut() != null &&
                                          evenement.getDateDebut().isBefore(LocalDateTime.now()));
                boolean estComplet = (evenement.getPlacesRestantes() <= 0);
                int placesOccupees = evenement.getCapacite() - evenement.getPlacesRestantes();
                double pourcentageRempli = (placesOccupees * 100.0) / evenement.getCapacite();

                String photo = evenement.getPhoto();
                boolean hasPhoto = (photo != null && !photo.trim().isEmpty());
        %>

        <div class="event-image-section <%= hasPhoto ? "" : "no-photo" %>">
            <% if (hasPhoto) { %>
                <img src="<%= request.getContextPath() %>/uploads/<%= photo %>"
                     alt="<%= evenement.getTitre() %>">
            <% } else { %>
                <i class="bi bi-calendar-event"></i>
            <% } %>
        </div>

        <div class="event-info">

            <div class="event-header">
                <div class="event-title-section">
                    <span class="event-badge"><%= evenement.getCategorieNom() %></span>
                    <h1><%= evenement.getTitre() %></h1>
                </div>
                <div style="text-align: right;">
                    <div class="event-price">Gratuit</div>
                </div>
            </div>

            <div class="details-grid">
                <div class="detail-item">
                    <div class="detail-icon"><i class="bi bi-calendar3"></i></div>
                    <div class="detail-content">
                        <h3>Date &amp; Heure</h3>
                        <p><%= evenement.getDateFormatee() %></p>
                    </div>
                </div>
                <div class="detail-item">
                    <div class="detail-icon"><i class="bi bi-geo-alt-fill"></i></div>
                    <div class="detail-content">
                        <h3>Lieu</h3>
                        <p><%= evenement.getLieu() %></p>
                    </div>
                </div>
                <div class="detail-item">
                    <div class="detail-icon"><i class="bi bi-person-circle"></i></div>
                    <div class="detail-content">
                        <h3>Organisateur</h3>
                        <p><%= evenement.getOrganisateurNom() %></p>
                    </div>
                </div>
                <div class="detail-item">
                    <div class="detail-icon"><i class="bi bi-tag-fill"></i></div>
                    <div class="detail-content">
                        <h3>Catégorie</h3>
                        <p><%= evenement.getCategorieNom() %></p>
                    </div>
                </div>
            </div>

            <div class="description-section">
                <h2><i class="bi bi-file-text"></i> Description</h2>
                <p><%= evenement.getDescription() %></p>
            </div>

            <div class="places-section">
                <h3><i class="bi bi-people-fill"></i> Places disponibles</h3>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: <%= pourcentageRempli %>%"></div>
                </div>
                <p class="places-info">
                    <strong><%= placesOccupees %> / <%= evenement.getCapacite() %></strong> places occupées
                    <br>
                    <strong><%= evenement.getPlacesRestantes() %></strong> places restantes
                </p>
            </div>

            <%
                if (evenementPasse) {
            %>
                <div class="status-message status-danger">
                    <i class="bi bi-clock-history"></i> Cet événement est terminé
                </div>
            <%
                } else if (estComplet) {
            %>
                <div class="status-message status-danger">
                    <i class="bi bi-exclamation-circle-fill"></i> Cet événement est complet
                </div>
            <%
                } else if (dejaInscrit != null && dejaInscrit) {
            %>
                <div class="status-message status-success">
                    <i class="bi bi-check-circle-fill"></i> Vous êtes inscrit à cet événement
                </div>
            <%
                }
            %>

            <div class="action-buttons">
                <%
                    if (user != null && "admin".equals(user.getRole())) {
                %>
                    <a href="evenement?action=edit&id=<%= evenement.getId() %>" class="btn btn-edit">
                        <i class="bi bi-pencil-square"></i> Modifier
                    </a>
                    <a href="evenement?action=delete&id=<%= evenement.getId() %>"
                       class="btn btn-delete"
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet événement ?');">
                        <i class="bi bi-trash3-fill"></i> Supprimer
                    </a>
                <%
                    } else if (user != null) {
                        if (evenementPasse) {
                %>
                            <button class="btn btn-secondary" disabled>
                                <i class="bi bi-clock-history"></i> Événement terminé
                            </button>
                <%
                        } else if (dejaInscrit != null && dejaInscrit) {
                %>
                            <form action="inscription" method="post" style="flex: 1;">
                                <input type="hidden" name="idEvenement" value="<%= evenement.getId() %>">
                                <input type="hidden" name="action" value="annuler">
                                <button type="submit" class="btn btn-secondary" style="width:100%;justify-content:center;">
                                    <i class="bi bi-x-circle"></i> Annuler mon inscription
                                </button>
                            </form>
                <%
                        } else if (estComplet) {
                %>
                            <button class="btn btn-secondary" disabled>
                                <i class="bi bi-slash-circle"></i> Événement complet
                            </button>
                <%
                        } else {
                %>
                            <form action="inscription" method="post" style="flex: 1;">
                                <input type="hidden" name="idEvenement" value="<%= evenement.getId() %>">
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-check2-circle"></i> S'inscrire à cet événement
                                </button>
                            </form>
                <%
                        }
                    } else {
                %>
                    <a href="login" class="btn btn-primary">
                        <i class="bi bi-lock-fill"></i> Se connecter pour s'inscrire
                    </a>
                <%
                    }
                %>
            </div>
        </div>

        <% } else { %>
            <div class="status-message status-danger">
                <i class="bi bi-emoji-frown"></i> Événement non trouvé
            </div>
        <% } %>

    </div>

</body>
</html>
