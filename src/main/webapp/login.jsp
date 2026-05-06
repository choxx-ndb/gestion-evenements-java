<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion - Gestion d'Événements</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #e9ecef; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .login-container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 100%; max-width: 400px; }
        h2 { text-align: center; color: #333; margin-bottom: 25px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; }
        input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; box-sizing: border-box; }
        .btn-login { width: 100%; padding: 12px; background-color: #007bff; border: none; color: white; font-size: 16px; border-radius: 5px; cursor: pointer; transition: background 0.3s; }
        .btn-login:hover { background-color: #0056b3; }
        .error-msg { color: #dc3545; text-align: center; margin-bottom: 15px; font-size: 14px; }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Connexion</h2>
    
    <%-- Affichage du message d'erreur si l'authentification échoue --%>
    <% if (request.getAttribute("erreur") != null) { %>
        <div class="error-msg"><%= request.getAttribute("erreur") %></div>
    <% } %>

    <form action="login" method="post">
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" placeholder="votre@email.com" required>
        </div>
        <div class="form-group">
            <label>Mot de passe</label>
            <input type="password" name="password" placeholder="••••••••" required>
        </div>
        <button type="submit" class="btn-login">Se connecter</button>
		        <div style="text-align:center; margin-top:20px;">
		    Pas encore de compte ?
		    <a href="register" style="color:#007bff; font-weight:bold; text-decoration:none;">
		        Créer un compte
		    </a>
		</div>
    </form>
</div>

</body>
</html>