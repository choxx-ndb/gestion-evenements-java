<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Evenement" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mes inscriptions</title>

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: grey;
            margin: 0;
        }

        .container {
            width: 90%;
            margin: auto;
            padding: 25px;
            background: #F0FAF6;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 8px;
            margin-top: 30px;
        }

        h2 {
            margin-bottom: 20px;
            color: black;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: black;
            color: white;
        }

        tr:hover td {
            background-color: #E6F1FB;
        }

        .btn {
            padding: 8px 12px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            display: inline-block;
            border: none;
            cursor: pointer;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        .btn-back {
            background-color: black;
            color: white;
            margin-bottom: 15px;
        }

        .btn-back:hover {
            background-color: grey;
        }

        .message {
            padding: 12px;
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-radius: 5px;
            margin-bottom: 15px;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 12px;
            font-size: 12px;
            background: #333;
            color: white;
        }
    </style>
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">

    <h2>Mes inscriptions</h2>

    <%
        String message = (String) session.getAttribute("message");
        if (message != null) {
    %>
        <div class="message"><%= message %></div>
    <%
            session.removeAttribute("message");
        }
    %>

    <a href="evenement?action=list" class="btn btn-back">← Retour aux événements</a>

    <table>
        <thead>
            <tr>
                <th>Titre</th>
                <th>Date</th>
                <th>Lieu</th>
                <th>Catégorie</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody>
        <%
            @SuppressWarnings("unchecked")
            List<Evenement> liste = (List<Evenement>) request.getAttribute("mesInscriptions");

            if (liste != null && !liste.isEmpty()) {
                for (Evenement e : liste) {
        %>
            <tr>
                <td><strong><%= e.getTitre() %></strong></td>
                <td><%= e.getDateFormatee() %></td>
                <td><%= e.getLieu() %></td>
                <td><span class="badge"><%= e.getCategorieNom() %></span></td>
                <td>
                    <form action="inscription" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="annuler">
                        <input type="hidden" name="idEvenement" value="<%= e.getId() %>">

                        <button type="submit" class="btn btn-danger"
                                onclick="return confirm('Voulez-vous vraiment annuler cette inscription ?');">
                            Annuler
                        </button>
                    </form>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="5" style="text-align:center; color: #6c757d;">
                    Vous n'êtes inscrit à aucun événement.
                </td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>

</div>

</body>
</html>
