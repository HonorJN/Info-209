<%-- 
    Document   : emp-all
    Created on : 4 avr. 2023, 11:10:50
    Author     : hoby
--%>
<%@page import="test.Emp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Listes des Employes</h1>
        <% 
            String cle = (String)request.getAttribute("cle");
            Emp employer = (Emp)request.getAttribute(cle);
        %>
            <h2>id</h2>
            <p><% out.print(employer.getId()); %></p>
            <h2>id_Dept</h2>
            <p><% out.print(employer.getId_dept()); %></p>
            <h2>Nom</h2>
            <p><% out.print(employer.getNom()); %></p>
            <h2>Prenom</h2>
            <p><% out.print(employer.getPrenom()); %></p>
            <h2>Date Naisance</h2>
            <p><% out.print(employer.getDateNaissance()); %></p>
            <h2>Salaire</h2>
            <p><% out.print(employer.getSalaire()); %></p>
            <h2>Specialite</h2>
            <% for(int i = 0; i < employer.getCheck().length;i++) { %>
                    <p><% out.print(employer.getCheck()[i]); %></p>
            <% } %>
    </body>
</html>