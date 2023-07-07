<%-- 
    Document   : index
    Created on : 6 juil. 2023, 14:28:26
    Author     : hoby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Login</h1>
        <form action="login" method="post" enctype="multipart/form-data">
            <label for="name">user name</label>
            <input type="text" name="name"><br/>
            <label for="pass">Password</label>
            <input type="password" name="pass"><br/>
            <input type="submit" value="Save">
        </form>
    </body>
</html>
