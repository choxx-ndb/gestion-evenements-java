<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Evenement" %>
<%@ page import="model.Categorie" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.time.LocalDateTime" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Événements</title>
    
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        :root {
            --primary: #007bff;
            --success: #28a745;
            --danger: #dc3545;
            --warning: #ffc107;
            --info: #17a2b8;
            --light: #f4f7f6;
            --dark: #343a40;
            --border: #ddd;
            --text-muted: #6c757d;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light);
            margin: 0;
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
            color: var(--primary);
        }

        .user-badge {
            background-color: rgba(255,255,255,0.2);
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.9rem;
        }

        /* CONTAINER */
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .header-section {
            margin-bottom: 1.5rem;
        }

        .greeting {
            font-size: 0.9rem;
            color: var(--text-muted);
            margin-bottom: 0.25rem;
        }

        .title {
            font-size: 2rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: #333;
        }

        /* SEARCH & FILTER */
        .search-bar {
            display: flex;
            gap: 12px;
            margin-bottom: 1.5rem;
            flex-wrap: wrap;
            align-items: center;
        }

        .search-input-wrapper {
            position: relative;
            flex: 1;
            min-width: 250px;
        }

        .search-input {
            width: 100%;
            padding: 12px 15px 12px 40px;
            border: 1px solid var(--border);
            border-radius: 8px;
            font-size: 0.95rem;
            font-family: inherit;
            background-color: white;
        }

        .search-input:focus {
            outline: none;
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
        }

        .search-icon {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-muted);
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-size: 0.95rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s;
            font-family: inherit;
        }

        .btn-add {
            background-color: var(--success);
            color: white;
        }

        .btn-add:hover {
            background-color: #1e7e34;
            transform: translateY(-2px);
        }

        .btn-primary {
            background-color: var(--primary);
            color: white;
        }

        .btn-primary:hover {
            background-color: #0056b3;
        }

        /* CATEGORIES FILTER */
        .categories-filter {
            display: flex;
            gap: 8px;
            overflow-x: auto;
            padding-bottom: 8px;
            margin-bottom: 1.5rem;
        }

        .category-badge {
            display: inline-block;
            padding: 8px 16px;
            border: 1px solid var(--border);
            border-radius: 20px;
            font-size: 0.9rem;
            white-space: nowrap;
            cursor: pointer;
            transition: all 0.2s;
            background-color: white;
            color: var(--text-muted);
            text-decoration: none;
            font-family: inherit;
        }

        .category-badge:hover,
        .category-badge.active {
            background-color: var(--primary);
            color: white;
            border-color: var(--primary);
        }

        /* MESSAGE */
        .alert {
            padding: 12px 16px;
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-radius: 8px;
            margin-bottom: 1.5rem;
        }

        /* EVENTS LABEL */
        .events-label {
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: var(--text-muted);
            margin-bottom: 1rem;
            font-weight: 600;
        }

        /* EVENTS GRID */
        .events-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
            margin-bottom: 2rem;
        }

        /* EVENT CARD - CLIQUABLE */
        .event-card {
            background: white;
            border: 1px solid var(--border);
            border-radius: 12px;
            overflow: hidden;
            transition: all 0.3s;
            display: flex;
            flex-direction: column;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            cursor: pointer;
            text-decoration: none;
            color: inherit;
        }

        .event-card:hover {
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
            transform: translateY(-4px);
        }

        /* IMAGE - PLUS GRANDE */
        .event-image {
            width: 100%;
            height: 180px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            background: linear-gradient(135deg, #E6F1FB 0%, #B5D4F4 100%);
            color: white;
        }

        .event-image.concert {
            background: linear-gradient(135deg, #E1F5EE 0%, #5DCAA5 100%);
        }

        .event-image.sport {
            background: linear-gradient(135deg, #FAEEDA 0%, #EF9F27 100%);
        }

        .event-image.atelier {
            background: linear-gradient(135deg, #EEEDFE 0%, #7F77DD 100%);
        }

        /* BODY - PLUS COMPACT */
        .event-body {
            padding: 14px;
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .event-badge {
            display: inline-block;
            width: fit-content;
            padding: 4px 10px;
            border-radius: 16px;
            font-size: 0.7rem;
            font-weight: 600;
            margin-bottom: 6px;
            background-color: #007bff;
            color: white;
        }

        .event-title {
            font-size: 0.95rem;
            font-weight: 600;
            margin-bottom: 6px;
            line-height: 1.3;
            color: #333;
        }

        .event-details {
            display: flex;
            flex-direction: column;
            gap: 4px;
            margin-bottom: 8px;
            font-size: 0.8rem;
            color: var(--text-muted);
            flex: 1;
        }

        .event-detail {
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .event-footer {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            gap: 8px;
            margin-top: auto;
            padding-top: 8px;
            border-top: 1px solid var(--border);
        }

        .event-progress {
            flex: 1;
        }

        .progress-bar {
            height: 4px;
            background-color: #e9ecef;
            border-radius: 2px;
            overflow: hidden;
            margin-bottom: 3px;
        }

        .progress-fill {
            height: 100%;
            background-color: var(--primary);
            transition: width 0.3s;
        }

        .progress-text {
            font-size: 0.7rem;
            color: var(--text-muted);
        }

        .event-price {
            text-align: right;
        }

        .price-amount {
            font-size: 1rem;
            font-weight: 600;
            color: var(--primary);
        }

        /* EMPTY STATE */
        .empty-state {
            text-align: center;
            padding: 3rem 1.5rem;
            color: var(--text-muted);
        }

        .empty-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        .empty-state h3 {
            font-size: 1.25rem;
            color: #333;
            margin-bottom: 0.5rem;
        }

        /* RESPONSIVE */
        @media (max-width: 768px) {
            .events-grid {
                grid-template-columns: 1fr;
            }

            .title {
                font-size: 1.5rem;
            }

            .navbar-container {
                flex-direction: column;
                gap: 10px;
            }

            .navbar-menu {
                flex-direction: column;
                width: 100%;
                gap: 10px;
            }

            .search-bar {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>

    <!-- NAVBAR -->
    <%@ include file="header.jsp" %>

    <!-- MAIN CONTENT -->
    <div class="container">
        <!-- HEADER -->
        <div class="header-section">
            <p class="greeting">Bienvenue 👋</p>
            <h1 class="title">Calendrier des Événements</h1>
        </div>

        <!-- MESSAGE -->
        <%
            String message = (String) session.getAttribute("message");
            if (message != null) {
        %>
            <div class="alert"><%= message %></div>
        <%
                session.removeAttribute("message");
            }
        %>

        <!-- SEARCH BAR -->
        <form action="evenement" method="GET" class="search-bar">
            <input type="hidden" name="action" value="list">
            <div class="search-input-wrapper">
                <span class="search-icon">🔍</span>
                <input type="text" name="search" class="search-input" 
                       placeholder="Rechercher un événement..." 
                       value="<%= (request.getAttribute("searchKeyword") != null ? request.getAttribute("searchKeyword") : "") %>">
            </div>
            <button type="submit" class="btn btn-primary">Chercher</button>
        </form>

        <!-- CATEGORIES FILTER -->
        <div class="categories-filter">
            <a href="evenement?action=list" class="category-badge <%= request.getAttribute("selectedCategory") == null ? "active" : "" %>">
                Tous
            </a>

            <%
                @SuppressWarnings("unchecked")
                List<Categorie> categories = (List<Categorie>) request.getAttribute("categories");
                String selectedCat = (String) request.getAttribute("selectedCategory");

                if (categories != null) {
                    for (Categorie cat : categories) {
            %>
                <a href="evenement?action=list&categorie=<%= cat.getId() %>" 
                   class="category-badge <%= (selectedCat != null && selectedCat.equals(String.valueOf(cat.getId())) ? "active" : "") %>">
                    <%= cat.getNom() %>
                </a>
            <%
                    }
                }
            %>
        </div>

        <!-- BUTTON AJOUTER (ADMIN ONLY) -->
        <%
          	
            if (currentUser != null && "admin".equals(currentUser.getRole())) {
        %>
            <div style="margin-bottom: 1.5rem;">
                <a href="evenement?action=new" class="btn btn-add">
                    ➕ Créer un événement
                </a>
            </div>
        <% } %>

        <!-- EVENTS LABEL -->
        <p class="events-label">Événements à venir</p>

        <!-- EVENTS GRID -->
        <%
            @SuppressWarnings("unchecked")
            List<Evenement> list = (List<Evenement>) request.getAttribute("evenements");

            @SuppressWarnings("unchecked")
            List<Evenement> mesInscriptions = (List<Evenement>) request.getAttribute("mesInscriptions");

            if (list != null && !list.isEmpty()) {
        %>
        <div class="events-grid">
            <%
                for (Evenement e : list) {
                    boolean dejaInscrit = false;
                    if (mesInscriptions != null) {
                        for (Evenement inscrit : mesInscriptions) {
                            if (inscrit.getId() == e.getId()) {
                                dejaInscrit = true;
                                break;
                            }
                        }
                    }

                    boolean evenementPasse = (e.getDateDebut() != null && e.getDateDebut().isBefore(LocalDateTime.now()));
            %>

            <!-- CARD CLIQUABLE -->
            <a href="evenement?action=detail&id=<%= e.getId() %>" class="event-card">
                <div class="event-image">🎯</div>

                <div class="event-body">
                    <span class="event-badge"><%= e.getCategorieNom() %></span>

                    <h3 class="event-title"><%= e.getTitre() %></h3>

                    <div class="event-details">
                        <div class="event-detail">
                            📅 <%= e.getDateFormatee() %>
                        </div>
                        <div class="event-detail">
                            📍 <%= e.getLieu() %>
                        </div>
                    </div>

                    <div class="event-footer">
                        <div class="event-progress">
                            <div class="progress-bar">
                                <div class="progress-fill" 
                                     style="width: <%= (e.getCapacite() > 0) ? Math.min((e.getCapacite() - e.getPlacesRestantes()) * 100 / e.getCapacite(), 100) : 100 %>%"></div>
                            </div>
                            <p class="progress-text">
                                <%= (e.getCapacite() - e.getPlacesRestantes()) %>/<%= e.getCapacite() %>
                            </p>
                        </div>
                        <div class="event-price">
                            <p class="price-amount">Gratuit</p>
                        </div>
                    </div>
                </div>
            </a>

            <%
                }
            %>
        </div>
        <%
            } else {
        %>
        <div class="empty-state">
            <div class="empty-icon">📭</div>
            <h3>Aucun événement trouvé</h3>
            <p>Il n'y a pas d'événements disponibles pour le moment.</p>
        </div>
        <% } %>
    </div>

</body>
</html>
