<%@ page import="model.Utilisateur" %>
<%
    Utilisateur currentUser = (Utilisateur) session.getAttribute("user");
%>
<nav style="background: #343a40; color: white; padding: 15px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <div>
        <strong>Gestion d'Événements</strong>
    </div>
    <div>
        <% if (currentUser != null) { %>
            <span>Bienvenue, <b><%= currentUser.getNom() %></b> (<%= currentUser.getRole() %>)</span>
            <a href="logout" style="color: #ff4d4d; margin-left: 20px; text-decoration: none; font-weight: bold;">Déconnexion</a>
        <% } %>
    </div>
</nav>