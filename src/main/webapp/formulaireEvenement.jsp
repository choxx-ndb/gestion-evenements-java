<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Evenement, model.Categorie, java.util.List" %>
<%
    Evenement ev = (Evenement) request.getAttribute("evenement");
    String titre = (ev == null) ? "Ajouter un nouvel événement" : "Modifier l'événement";
    @SuppressWarnings("unchecked")
    List<Categorie> cats = (List<Categorie>) request.getAttribute("categories");

    String photoExistante = (ev != null && ev.getPhoto() != null) ? ev.getPhoto() : "";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title><%= titre %></title>
<style>
    * { box-sizing: border-box; margin: 0; padding: 0; }

    body {
        font-family: 'Segoe UI', sans-serif;
        background: grey;
        display: flex;
        justify-content: center;
        padding: 40px 16px;
    }

    .container {
        background: #F0FAF6;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.10);
        width: 100%;
        max-width: 500px;
        overflow: hidden;
    }

    .header { padding: 24px 28px 0; }
    .header h2 {
        font-size: 22px; font-weight: 700; color: #222;
        border-bottom: 2px solid black; padding-bottom: 12px;
    }

    .tabs {
        display: flex; border-bottom: 1px solid #e5e7eb;
        padding: 0 28px; margin-top: 16px;
    }
    .tab-btn {
        padding: 10px 14px; background: none; border: none;
        border-bottom: 2px solid transparent; font-size: 13px;
        font-weight: 500; color: #9ca3af; cursor: pointer;
        transition: color .2s, border-color .2s;
    }
    .tab-btn.active { color: black; border-bottom-color: black; }
    .tab-btn:hover:not(.active) { color: #374151; }

    .panel { display: none; padding: 22px 28px; }
    .panel.active { display: block; }

    .cover-zone {
        position: relative; border: 2px dashed #d1d5db;
        border-radius: 8px; text-align: center;
        cursor: pointer; margin-bottom: 18px;
        transition: border-color .2s, background .2s;
        overflow: hidden;
        max-height: 220px;
    }
    .cover-zone:hover { border-color: black; background: #E6F1FB; }

    .cover-zone input[type="file"] {
        position: absolute; inset: 0; opacity: 0;
        cursor: pointer; width: 100%; height: 100%; z-index: 2;
    }

    #coverPreview {
        width: 100%; height: 160px;
        object-fit: cover; border-radius: 6px;
        display: none;
    }

    #existingPhoto, #coverPreview {
        width: 100%;
        height: 200px;
        object-fit: cover;
        border-radius: 6px;
        display: block;
    }

    .cover-placeholder {
        padding: 24px;
    }
    .cover-placeholder p {
        font-size: 12px; color: #6b7280; margin-top: 6px;
    }
    .cover-placeholder strong { color: #374151; font-weight: 500; }

    .form-group { margin-bottom: 16px; }
    label {
        display: block; font-size: 13px; font-weight: 600;
        color: #555; margin-bottom: 5px;
    }
    input, textarea, select {
        width: 100%; padding: 10px 12px;
        border: 1px solid #ccc; border-radius: 6px;
        font-size: 14px; font-family: 'Segoe UI', sans-serif;
        color: #333; transition: border-color .2s, box-shadow .2s; outline: none;
        background-color: white;
    }
    input:focus, textarea:focus, select:focus {
        border-color: black;
        box-shadow: 0 0 0 3px rgba(0,0,0,0.08);
    }
    textarea { resize: vertical; min-height: 90px; }
    .row-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }

    .ticket-card {
        background: #E6F1FB; border: 1px solid #B5D4F4;
        border-radius: 8px; padding: 14px; margin-bottom: 12px;
    }
    .ticket-header {
        display: flex; align-items: center;
        justify-content: space-between; margin-bottom: 12px;
    }
    .ticket-label { font-weight: 600; font-size: 13px; color: #374151; }
    .badge { font-size: 11px; font-weight: 600; padding: 2px 9px; border-radius: 20px; }
    .badge-gratuit { background: #d1fae5; color: #065f46; }
    .badge-payant  { background: #dbeafe; color: #1e40af; }

    .footer {
        display: flex; gap: 10px; padding: 16px 28px;
        border-top: 1px solid #e5e7eb; background: #E6F1FB;
    }
    .btn-draft {
        flex: 1; padding: 11px; background: white;
        border: 1px solid #d1d5db; border-radius: 6px;
        font-size: 13px; font-weight: 500; color: #6b7280;
        cursor: pointer; transition: border-color .2s, color .2s;
    }
    .btn-draft:hover { border-color: black; color: black; }
    .btn-primary {
        flex: 1; padding: 11px; background: black; border: none;
        border-radius: 6px; font-size: 14px; font-weight: 600;
        color: white; cursor: pointer; transition: background .2s;
    }
    .btn-primary:hover { background: grey; }

    .link-back {
        display: block; text-align: center; padding: 12px;
        font-size: 13px; color: #6b7280; text-decoration: none;
    }
    .link-back:hover { color: black; }

    #realSubmit { display: none; }
</style>
</head>
<body>

<div class="container">

    <div class="header">
        <h2><%= titre %></h2>
    </div>

    <div class="tabs">
        <button class="tab-btn active" onclick="goTo(0)">Infos générales</button>
        <button class="tab-btn"        onclick="goTo(1)">Date &amp; Lieu</button>
        <button class="tab-btn"        onclick="goTo(2)">Billetterie</button>
    </div>

    <form action="evenement" method="post"
          enctype="multipart/form-data"
          id="mainForm">

        <% if (ev != null) { %>
            <input type="hidden" name="id" value="<%= ev.getId() %>">
        <% } %>

        <%-- Conserver la photo existante si aucune nouvelle n'est uploadée --%>
        <input type="hidden" name="photoExistante" value="<%= photoExistante %>">

        <div class="panel active" id="p0">

            <%-- ZONE UPLOAD IMAGE --%>
            <div class="cover-zone" id="coverZone">

                <%-- Input file — déclenche le choix du fichier --%>
                <input type="file" name="cover"
                       accept="image/jpeg,image/png,image/gif,image/webp"
                       onchange="previewImg(this)">

                <%-- Aperçu après sélection d'un nouveau fichier --%>
                <img id="coverPreview" alt="Aperçu de la nouvelle image">

                <%-- Image existante (mode édition) --%>
                <% if (!photoExistante.isEmpty()) { %>
                    <img id="existingPhoto"
                         src="<%= request.getContextPath() %>/uploads/<%= photoExistante %>"
                         alt="Image actuelle">
                <% } else { %>
                    <%-- Placeholder si aucune image --%>
                    <div class="cover-placeholder" id="coverPlaceholder">
                        <svg width="36" height="36" viewBox="0 0 24 24" fill="none"
                             stroke="#9ca3af" stroke-width="1.5">
                            <rect x="3" y="3" width="18" height="18" rx="2"/>
                            <circle cx="8.5" cy="8.5" r="1.5"/>
                            <polyline points="21 15 16 10 5 21"/>
                        </svg>
                        <p><strong>Ajouter une image de couverture</strong><br>
                           JPG, PNG, GIF · Max 5 Mo · Recommandé 1200×630px</p>
                    </div>
                <% } %>
            </div>

            <div class="form-group">
                <label>Titre de l'événement</label>
                <input type="text" name="titre" placeholder="Ex : Tech Summit Maroc 2025"
                       value="<%= (ev != null) ? ev.getTitre() : "" %>" required>
            </div>

            <div class="form-group">
                <label>Catégorie</label>
                <select name="categorie_id" required>
                    <option value="">-- Choisir une catégorie --</option>
                    <% if (cats != null) for (Categorie c : cats) {
                           String sel = (ev != null && ev.getCategorieId() == c.getId()) ? "selected" : ""; %>
                        <option value="<%= c.getId() %>" <%= sel %>><%= c.getNom() %></option>
                    <% } %>
                </select>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea name="description"
                    placeholder="Décrivez votre événement…"><%= (ev != null && ev.getDescription() != null) ? ev.getDescription() : "" %></textarea>
            </div>
        </div>

        <div class="panel" id="p1">

            <div class="form-group">
                <label>Date et heure de début</label>
                <%
                    String dateVal = "";
                    if (ev != null && ev.getDateDebut() != null) {
                        dateVal = ev.getDateDebut().toString();
                        if (dateVal.length() > 16) dateVal = dateVal.substring(0, 16);
                    }
                %>
                <input type="datetime-local" name="date_debut" value="<%= dateVal %>" required>
            </div>

            <div class="form-group">
                <label>Date et heure de fin</label>
                <input type="datetime-local" name="date_fin">
            </div>

            <div class="form-group">
                <label>Lieu</label>
                <input type="text" name="lieu" placeholder="Ex : Morocco Mall, Casablanca"
                       value="<%= (ev != null) ? ev.getLieu() : "" %>" required>
            </div>

            <div class="row-2">
                <div class="form-group">
                    <label>Ville</label>
                    <input type="text" name="ville" placeholder="Casablanca">
                </div>
                <div class="form-group">
                    <label>Code postal</label>
                    <input type="text" name="cp" placeholder="20000">
                </div>
            </div>
        </div>

        <!-- PANEL 3 : Billetterie-->
        <div class="panel" id="p2">
		        <div class="form-group">
		    <label>Prix (MAD)</label>
		    <input type="number" 
		           name="prix" 
		           min="0" 
		           step="0.01"
		           placeholder="0 = Gratuit"
		           value="<%= (ev != null) ? ev.getPrix() : 0 %>">
		   </div>

            <div class="form-group">
                <label>Capacité totale</label>
                <input type="number" name="capacite" min="1" placeholder="Ex : 500"
                       value="<%= (ev != null) ? ev.getCapacite() : "" %>" required>
            </div>

            <div class="ticket-card">
                <div class="ticket-header">
                    <span class="ticket-label">Entrée standard</span>
                    <span class="badge badge-gratuit">Gratuit</span>
                </div>
                <div class="row-2">
                    <div class="form-group" style="margin:0">
                        <label>Quantité</label>
                        <input type="number" name="billet_qte_gratuit" min="0" placeholder="0">
                    </div>
                    <div class="form-group" style="margin:0">
                        <label>Prix (MAD)</label>
                        <input type="number" value="0" disabled>
                    </div>
                </div>
            </div>

            <div class="ticket-card">
                <div class="ticket-header">
                    <span class="ticket-label">Ticket VIP</span>
                    <span class="badge badge-payant">Payant</span>
                </div>
                <div class="row-2">
                    <div class="form-group" style="margin:0">
                        <label>Quantité</label>
                        <input type="number" name="billet_qte_vip" min="0" placeholder="0">
                    </div>
                    <div class="form-group" style="margin:0">
                        <label>Prix (MAD)</label>
                        <input type="number" name="billet_prix_vip" min="0" placeholder="150">
                    </div>
                </div>
            </div>
        </div>

        <button type="submit" id="realSubmit"></button>
    </form>

    <div class="footer">
        <button class="btn-draft" onclick="alert('Brouillon sauvegardé.')">
            Enregistrer brouillon
        </button>
        <button class="btn-primary" id="mainBtn" onclick="nextStep()">
            Suivant →
        </button>
    </div>

    <a href="evenement?action=list" class="link-back">Annuler et retourner à la liste</a>
</div>

<script>
    const panels  = document.querySelectorAll('.panel');
    const tabBtns = document.querySelectorAll('.tab-btn');
    const mainBtn = document.getElementById('mainBtn');
    let current   = 0;

    function goTo(n) {
        panels[current].classList.remove('active');
        tabBtns[current].classList.remove('active');
        current = n;
        panels[current].classList.add('active');
        tabBtns[current].classList.add('active');
        mainBtn.textContent = (current === panels.length - 1)
            ? '<%= (ev == null) ? "Créer l\\'événement" : "Enregistrer" %>'
            : 'Suivant →';
    }

    function nextStep() {
        if (current < panels.length - 1) {
            goTo(current + 1);
        } else {
            document.getElementById('realSubmit').click();
        }
    }

  
    function previewImg(input) {
        if (!input.files || !input.files[0]) return;

        const reader = new FileReader();
        reader.onload = function(e) {
            const preview = document.getElementById('coverPreview');
            preview.src = e.target.result;
            preview.style.display = 'block';

            const placeholder = document.getElementById('coverPlaceholder');
            const existing    = document.getElementById('existingPhoto');
            if (placeholder) placeholder.style.display = 'none';
            if (existing)    existing.style.display    = 'none';
        };
        reader.readAsDataURL(input.files[0]);
    }
</script>

</body>
</html>
