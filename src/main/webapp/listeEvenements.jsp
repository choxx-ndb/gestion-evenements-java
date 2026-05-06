<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Evenement" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="java.time.LocalDateTime" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Événements</title>
    
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; }
        .container { width: 90%; margin: auto; overflow: hidden; padding: 20px; background: white; box-shadow: 0 0 10px rgba(0,0,0,0.1); border-radius: 8px; margin-top: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #007bff; color: white; }
        tr:hover { background-color: #f1f1f1; }
        .btn { padding: 8px 12px; text-decoration: none; border-radius: 4px; font-size: 14px; display: inline-block; }
        .btn-add { background-color: #28a745; color: white; margin-bottom: 15px; }
        .btn-edit { background-color: #ffc107; color: black; margin-right: 5px; }
        .btn-delete { background-color: #dc3545; color: white; }
        .badge { padding: 5px 10px; border-radius: 12px; font-size: 12px; background: #eee; color: #333; }
    </style>
</head>
<body>

    <!-- Inclusion de la barre de navigation (contient la gestion de session) -->
    <%@ include file="header.jsp" %>

    <div class="container">
        <h2>Calendrier des Événements</h2>
			        <%
			    String message = (String) session.getAttribute("message");
			    if (message != null) {
			%>
			    <div style="padding: 12px; background-color: #d4edda; color: #155724; 
			                border: 1px solid #c3e6cb; border-radius: 5px; margin-bottom: 15px;">
			        <%= message %>
			    </div>
			<%
			        session.removeAttribute("message");
			    }
			%>

        <%-- Le bouton Ajouter n'est visible que pour l'administrateur --%>
        <% if (currentUser != null && "admin".equals(currentUser.getRole())) { %>
            <a href="evenement?action=new" class="btn btn-add">+ Créer un événement</a>
        <% } %>

        <table>
            <thead>
                <tr>
                    <th>Titre</th>
                    
                    <th>Date</th>
                    <th>Lieu</th>
                    <th>Catégorie</th>
                    <th>Capacité</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                @SuppressWarnings("unchecked")
                List<Evenement> list = (List<Evenement>) request.getAttribute("evenements");

                @SuppressWarnings("unchecked")
                List<Evenement> mesInscriptions = (List<Evenement>) request.getAttribute("mesInscriptions");

                if (list != null && !list.isEmpty()) {
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
                        boolean evenementPasse = false;

                        if (e.getDateDebut() != null && e.getDateDebut().isBefore(LocalDateTime.now())) {
                            evenementPasse = true;
                        }
             
                %>
                <tr>
                    <td><strong><%= e.getTitre() %></strong></td>
                    <td><td><%= e.getDateFormatee() %></td>
                    <td><%= e.getLieu() %></td>
                    <td><span class="badge" style="background-color: #007bff; color: white; padding: 5px 10px; border-radius: 15px;">
                    <%= e.getCategorieNom() %>
                    </span></td>
                    <td><%= e.getPlacesRestantes() %> / <%= e.getCapacite() %> places</td>
                    <td>
                        <%-- Les boutons de gestion sont réservés à l'admin --%>
                        <% if (currentUser != null && "admin".equals(currentUser.getRole())) { %>

						    <a href="evenement?action=edit&id=<%= e.getId() %>" class="btn btn-edit">Modifier</a>
						
						    <a href="evenement?action=delete&id=<%= e.getId() %>" 
						       class="btn btn-delete" 
						       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet événement ?');">
						       Supprimer
						    </a>
						
						<% } else if (currentUser != null) { %>

						    <% if (evenementPasse) { %>
						
						        <span style="color: #dc3545; font-weight: bold;">
						            Événement terminé
						        </span>
						
						    <% } else if (dejaInscrit) { %>
						
						        <span style="color: #28a745; font-weight: bold;">
						            Déjà inscrit
						        </span>
						
						    <% } else { %>
						
						        <form action="inscription" method="post" style="display:inline;">
						            <input type="hidden" name="idEvenement" value="<%= e.getId() %>">
						            <button type="submit" class="btn" style="background-color:#007bff; color:white;">
						                S'inscrire
						            </button>
						        </form>
						
						    <% } %>
												
						<% } else { %>
						
						    <a href="login.jsp" class="btn" style="background-color:#6c757d; color:white;">
						        Se connecter pour s'inscrire
						    </a>
						
						<% } %>
                    </td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6" style="text-align:center;">Aucun événement trouvé.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

</body>
</html>