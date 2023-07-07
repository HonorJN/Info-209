<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="test.Dept"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>Dept-all</title>
  </head>
  <body>
    <h1>Tongasoa</h1>
    <h1>Listes des Employes</h1>
        <% 
            String employer = (String)request.getAttribute("cle");
            Vector dept = (Vector)request.getAttribute(employer);
            
        %>
        <table border="1">
            <thead>
                <tr>
                    <th>id</th>
                    <th>Nom</th>
                </tr>
            </thead>
            <tbody>
                <% for (int i = 0; i < dept.size(); i++) { %>
                    <tr>
                        <td><% out.print(((Dept)dept.get(i)).getId()); %></td>
                        <td><% out.print(((Dept)dept.get(i)).getName()); %></td>
                    </tr>
                <% } %>
                    
        </table>
  </body>
</html>
