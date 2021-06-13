<%-- 
    Document   : loginEmployee
    Created on : Oct 31, 2010, 9:22:15 AM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Employee</title>
    </head>
    <body>
    <form id="loginForm" name="login" method="post" action="loginEmployee" target="_parent">
        <table border="0">
        <tr>
            <td>Employee Name:</td>
            <td><input type="text" name="name" value="" /></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" value="" /></td>
        </tr>
        <tr>
            <td> </td>
            <td> </td>
        </tr>
        <tr>
        <td><input type="submit" id="loginButton" value="Login" /></td>
            <td> </td>
        </tr>
    </table>
    </form>
    </body>
</html>
