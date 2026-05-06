<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Créer un compte</title>

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #eef1f5;
            margin: 0;
        }

        .register-container {
            width: 420px;
            margin: 120px auto;
            padding: 35px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }

        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
            margin-bottom: 8px;
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 15px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            margin-top: 25px;
            padding: 12px;
            border: none;
            background: #007bff;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background: #0056b3;
        }

        .error {
            padding: 10px;
            background-color: #f8d7da;
            color: #721c24;
            border-radius: 5px;
            margin-bottom: 15px;
        }

        .message {
            padding: 10px;
            background-color: #d4edda;
            color: #155724;
            border-radius: 5px;
            margin-bottom: 15px;
        }

        .login-link {
            margin-top: 20px;
            text-align: center;
        }

        .login-link a {
            color: #007bff;
            text-decoration: none;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="register-container">

    <h2>Créer un compte</h2>

    <% if (request.getAttribute("erreur") != null) { %>
        <div class="error"><%= request.getAttribute("erreur") %></div>
    <% } %>

    <% if (request.getAttribute("message") != null) { %>
        <div class="message"><%= request.getAttribute("message") %></div>
    <% } %>

    <form action="register" method="post">
        <label>Nom complet</label>
        <input type="text" name="nom" placeholder="Votre nom" required>

        <label>Email</label>
        <input type="email" name="email" placeholder="votre@email.com" required>

        <label>Mot de passe</label>
        <input type="password" name="motDePass" placeholder="Votre mot de passe" required>

        <button type="submit">Créer mon compte</button>
    </form>

    <div class="login-link">
        Déjà un compte ?
        <a href="login">Se connecter</a>
    </div>

</div>

</body>
</html>