<%-- 
    Document   : loginTracking
    Created on : Oct 31, 2010, 9:33:42 AM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Tracking Page</title>
    </head>
    <body>
    <form id="tracking" name="form1" method="post" action="loginTracking" target="mainpage">
        <table border="0">
        <tr>
            <td>Your Contact:</td>
            <td><input type="text" name="contact" value="" /></td>
        </tr>
        <tr>
            <td>Post Number:</td>
            <td><input type="text" name="orderNum" value="" /></td>
        </tr>
        <tr>
            <td> </td>
            <td> </td>
        </tr>
        <tr>
        <td><input type="submit" value="Submit" /></td>
            <td> </td>
        </tr>
    </table>
    </form>
    </body>
</html>
