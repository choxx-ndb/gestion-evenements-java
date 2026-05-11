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
    
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            line-height: 1.6;
        }

        /* NAVBAR */
        nav {
            background: #343a40;
            color: white;
            padding: 15px 0;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .navbar-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar-brand {
            font-size: 1.5rem;
            font-weight: bold;
            color: white;
        }

        .navbar-menu {
            display: flex;
            gap: 20px;
            align-items: center;
        }

        .navbar-menu a {
            color: white;
            text-decoration: none;
            transition: color 0.2s;
        }

        .navbar-menu a:hover {
            color: #007bff;
        }

        /* CONTAINER */
        .container {
            max-width: 900px;
            margin: 0 auto;
            padding: 0 20px;
        }

        /* BACK BUTTON */
        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 20px;
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s;
        }

        .back-link:hover {
            color: #0056b3;
        }

        /* IMAGE SECTION - GRANDE */
        .event-image-section {
            width: 100%;
            height: 350px;
            background: linear-gradient(135deg, #E6F1FB 0%, #B5D4F4 100%);
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 5rem;
            margin-bottom: 30px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        /* INFO SECTION */
        .event-info {
            background: white;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .event-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 20px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 20px;
        }

        .event-title-section h1 {
            font-size: 2rem;
            font-weight: 600;
            color: #333;
            margin-bottom: 10px;
        }

        .event-badge {
            display: inline-block;
            padding: 8px 16px;
            background-color: #007bff;
            color: white;
            border-radius: 16px;
            font-size: 0.9rem;
            font-weight: 600;
            margin-right: 10px;
        }

        .event-price {
            font-size: 1.8rem;
            font-weight: 700;
            color: #007bff;
        }

        /* DETAILS GRID */
        .details-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }

        .detail-item {
            display: flex;
            gap: 15px;
        }

        .detail-icon {
            font-size: 1.5rem;
            min-width: 30px;
        }

        .detail-content h3 {
            font-size: 0.9rem;
            color: #999;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            margin-bottom: 5px;
        }

        .detail-content p {
            font-size: 1.1rem;
            color: #333;
            font-weight: 500;
        }

        /* DESCRIPTION */
        .description-section {
            margin-bottom: 30px;
            padding-bottom: 30px;
            border-bottom: 1px solid #ddd;
        }

        .description-section h2 {
            font-size: 1.3rem;
            color: #333;
            margin-bottom: 15px;
        }

        .description-section p {
            color: #666;
            line-height: 1.8;
        }

        /* PLACES SECTION */
        .places-section {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }

        .places-section h3 {
            margin-bottom: 15px;
            color: #333;
        }

        .progress-bar {
            height: 8px;
            background-color: #e9ecef;
            border-radius: 4px;
            overflow: hidden;
            margin-bottom: 10px;
        }

        .progress-fill {
            height: 100%;
            background-color: #007bff;
            transition: width 0.3s;
        }

        .places-info {
            font-size: 0.95rem;
            color: #666;
        }

        /* BUTTONS */
        .action-buttons {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s;
            font-family: inherit;
            font-weight: 500;
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
            flex: 1;
            justify-content: center;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        .btn-edit {
            background-color: #ffc107;
            color: black;
        }

        .btn-delete {
            background-color: #dc3545;
            color: white;
        }

        .btn-delete:hover {
            background-color: #c82333;
        }

        /* STATUS MESSAGES */
        .status-message {
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
        }

        .status-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .status-info {
            background-color: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }

        /* RESPONSIVE */
        @media (max-width: 768px) {
            .event-image-section {
                height: 250px;
                font-size: 3rem;
            }

            .event-header {
                flex-direction: column;
                gap: 15px;
            }

            .details-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }

            .action-buttons {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>

    <!-- NAVBAR -->
    <%@ include file="header.jsp" %>

    <!-- MAIN CONTENT -->
    <div class="container">
        
        <!-- BACK BUTTON -->
        <a href="evenement?action=list" class="back-link">
            ← Retour à la liste
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
        %>

        <!-- IMAGE GRANDE -->
        <div class="event-image-section">
            🎯
        </div>

        <!-- INFO DETAILS -->
        <div class="event-info">

            <!-- HEADER -->
            <div class="event-header">
                <div class="event-title-section">
                    <span class="event-badge"><%= evenement.getCategorieNom() %></span>
                    <h1><%= evenement.getTitre() %></h1>
                </div>
                <div style="text-align: right;">
                    <div class="event-price">Gratuit</div>
                </div>
            </div>

            <!-- DETAILS EN GRILLE -->
            <div class="details-grid">
                <div class="detail-item">
                    <div class="detail-icon">📅</div>
                    <div class="detail-content">
                        <h3>Date & Heure</h3>
                        <p><%= evenement.getDateFormatee() %></p>
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-icon">📍</div>
                    <div class="detail-content">
                        <h3>Lieu</h3>
                        <p><%= evenement.getLieu() %></p>
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-icon">👤</div>
                    <div class="detail-content">
                        <h3>Organisateur</h3>
                        <p><%= evenement.getOrganisateurNom() %></p>
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-icon">📂</div>
                    <div class="detail-content">
                        <h3>Catégorie</h3>
                        <p><%= evenement.getCategorieNom() %></p>
                    </div>
                </div>
            </div>

            <!-- DESCRIPTION -->
            <div class="description-section">
                <h2>Description</h2>
                <p><%= evenement.getDescription() %></p>
            </div>

            <!-- PLACES -->
            <div class="places-section">
                <h3>Places disponibles</h3>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: <%= pourcentageRempli %>%"></div>
                </div>
                <p class="places-info">
                    <strong><%= placesOccupees %> / <%= evenement.getCapacite() %></strong> places occupées
                    <br>
                    <strong><%= evenement.getPlacesRestantes() %></strong> places restantes
                </p>
            </div>

            <!-- STATUS MESSAGE -->
            <%
                if (evenementPasse) {
            %>
                <div class="status-message status-danger">
                    ⏰ Cet événement est terminé
                </div>
            <%
                } else if (estComplet) {
            %>
                <div class="status-message status-danger">
                    ⚠️ Cet événement est complet
                </div>
            <%
                } else if (dejaInscrit != null && dejaInscrit) {
            %>
                <div class="status-message status-success">
                    ✓ Vous êtes inscrit à cet événement
                </div>
            <%
                }
            %>

            <!-- ACTION BUTTONS -->
            <div class="action-buttons">
                
                <!-- POUR LES ADMINS -->
                <%
                    if (user != null && "admin".equals(user.getRole())) {
                %>
                    <a href="evenement?action=edit&id=<%= evenement.getId() %>" class="btn btn-edit">
                        ✏️ Modifier
                    </a>
                    <a href="evenement?action=delete&id=<%= evenement.getId() %>" 
                       class="btn btn-delete"
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet événement ?');">
                        🗑️ Supprimer
                    </a>
                
                <!-- POUR LES UTILISATEURS -->
                <%
                    } else if (user != null) {
                        if (evenementPasse) {
                %>
                            <button class="btn btn-secondary" disabled>
                                ⏰ Événement terminé
                            </button>
                <%
                        } else if (dejaInscrit != null && dejaInscrit) {
                %>
                            <form action="inscription" method="post" style="flex: 1;">
                                <input type="hidden" name="idEvenement" value="<%= evenement.getId() %>">
                                <input type="hidden" name="action" value="annuler">
                                <button type="submit" class="btn btn-secondary" style="width: 100%; justify-content: center;">
                                    ✋ Annuler mon inscription
                                </button>
                            </form>
                <%
                        } else if (estComplet) {
                %>
                            <button class="btn btn-secondary" disabled>
                                ⚠️ Événement complet
                            </button>
                <%
                        } else {
                %>
                            <form action="inscription" method="post" style="flex: 1;">
                                <input type="hidden" name="idEvenement" value="<%= evenement.getId() %>">
                                <button type="submit" class="btn btn-primary">
                                    ✓ S'inscrire à cet événement
                                </button>
                            </form>
                <%
                        }
                    } else {
                %>
                    <a href="login" class="btn btn-primary">
                        🔐 Se connecter pour s'inscrire
                    </a>
                <%
                    }
                %>
            </div>
        </div>

        <% } else { %>
            <div class="status-message status-danger">
                ❌ Événement non trouvé
            </div>
        <% } %>

    </div>

</body>
</html>
