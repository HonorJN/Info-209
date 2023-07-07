<%-- 
    Document   : emp-all
    Created on : 4 avr. 2023, 11:10:50
    Author     : hoby
--%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
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
            Vector employer = (Vector)request.getAttribute(cle);
        %>
        <table border="1">
            <thead>
                <tr>
                    <th>id</th>
                    <th>id_Dept</th>
                    <th>Nom</th>
                    <th>Prenom</th>
                    <th>Date Naisance</th>
                    <th>Salaire</th>
                    <th>Details</th>
                </tr>
            </thead>
            <tbody>
                <% for (int i = 0; i < employer.size(); i++) { %>
                    <tr>
                        <td><% out.print(((Emp)employer.get(i)).getId()); %></td>
                        <td><% out.print(((Emp)employer.get(i)).getId_dept()); %></td>
                        <td><% out.print(((Emp)employer.get(i)).getNom()); %></td>
                        <td><% out.print(((Emp)employer.get(i)).getPrenom()); %></td>
                        <td><% out.print(((Emp)employer.get(i)).getDateNaissance()); %></td>
                        <td><% out.print(((Emp)employer.get(i)).getSalaire()); %></td>
                        <td><a href="emp-details?id=<% out.print(((Emp)employer.get(i)).getId());%>">details</a></td>
                    </tr>
                <% } %>
            </tbody>
        </table>

    </body>
</html>
