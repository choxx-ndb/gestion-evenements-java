<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Evenement" %>
<%@ page import="model.Categorie" %>
<%@ page import="java.util.List" %>
<%
    // On récupère l'objet "evenement" si on est en mode édition
    Evenement ev = (Evenement) request.getAttribute("evenement");
    String titrePage = (ev == null) ? "Ajouter un nouvel événement" : "Modifier l'événement : " + ev.getTitre();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= titrePage %></title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; padding: 40px; }
        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); width: 100%; max-width: 500px; }
        h2 { margin-top: 0; color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        input, textarea { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .btn-submit { background-color: #007bff; color: white; border: none; padding: 12px 20px; border-radius: 4px; cursor: pointer; width: 100%; font-size: 16px; }
        .btn-submit:hover { background-color: #0056b3; }
        .btn-back { display: block; text-align: center; margin-top: 15px; color: #666; text-decoration: none; }
    </style>
</head>
<body>

<div class="container">
    <h2><%= titrePage %></h2>

    <form action="evenement" method="post">
        
        <!-- Champ caché indispensable pour l'UPDATE dans le DAO -->
        <% if (ev != null) { %>
            <input type="hidden" name="id" value="<%= ev.getId() %>">
        <% } %>

        <div class="form-group">
            <label>Titre de l'événement</label>
            <input type="text" name="titre" value="<%= (ev != null) ? ev.getTitre() : "" %>" required>
        </div>

        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="4"><%= (ev != null) ? ev.getDescription() : "" %></textarea>
        </div>

        <div class="form-group">
            <label>Lieu</label>
            <input type="text" name="lieu" value="<%= (ev != null) ? ev.getLieu() : "" %>" required>
        </div>

        <div class="form-group">
            <label>Date et Heure</label>
            <!-- Note: Le format datetime-local attend YYYY-MM-DDTHH:MM -->
            <input type="datetime-local" name="date_debut" 
                   value="<%= (ev != null && ev.getDateDebut() != null) ? ev.getDateDebut().toString() : "" %>" required>
        </div>

        <div class="form-group">
            <label>Capacité totale</label>
            <input type="number" name="capacite" value="<%= (ev != null) ? ev.getCapacite() : "" %>" required>
        </div>

        <div class="form-group">
    <label>Catégorie</label>
    <select name="categorie_id" required>
        <option value="">-- Choisir une catégorie --</option>
        <% 
            // On récupère la liste des catégories envoyée par la Servlet
            List<Categorie> cats = (List<Categorie>) request.getAttribute("categories");
            
            if (cats != null) {
                for (Categorie c : cats) {
                    // Si on est en mode édition (ev != null), on vérifie quelle catégorie cocher
                    String selected = (ev != null && ev.getCategorieId() == c.getId()) ? "selected" : "";
        %>
            <option value="<%= c.getId() %>" <%= selected %>>
                <%= c.getNom() %>
            </option>
        <% 
                }
            } 
        %>
    </select>
</div>

        <button type="submit" class="btn-submit">
            <%= (ev == null) ? "Créer l'événement" : "Enregistrer les modifications" %>
        </button>

        <a href="evenement?action=list" class="btn-back">Annuler et retourner à la liste</a>
    </form>
</div>

</body>
</html>