<%-- 
    Document   : confirmDeleteEmployee
    Created on : Nov 1, 2010, 12:58:44 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirm Delete Employee</title>
    </head>
    <body>
<form action="deleteEmployee">
        <table>
        <tr>
            <td>Contact:</td>
            <td><% out.println(request.getAttribute("name")); %></td>
        </tr>
        <tr>
            <td>From:</td>
            <td><% out.println(request.getAttribute("password"));%></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td><input type="submit" value="Delete" /></td>
            <td><input type="submit" value="Cancel"/></td>
        </tr>
        </table>
</form>
    </body>
</html>
