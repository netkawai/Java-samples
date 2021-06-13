<%-- 
    Document   : ConfirmOrder
    Created on : Oct 30, 2010, 11:32:48 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirm Post Order</title>
    </head>
    <body>
<form action="AddOrder">
        <table>
        <tr>
            <td>Contact:</td>
            <td><% out.println(session.getAttribute("contact")); %></td>
        </tr>
        <tr>
            <td>From:</td>
            <td><% out.println(session.getAttribute("from"));%></td>
        </tr>
        <tr>
            <td>To:</td>
            <td><% out.println(session.getAttribute("to"));%></td>
        </tr>
        <tr>
            <td>Quantity:</td>
            <td><% out.println(session.getAttribute("quantity"));%></td>
        </tr>
        <tr>
            <td>Service:</td>
            <td><% out.println(session.getAttribute("service"));%></td>
        </tr>
        <tr>
            <td>Weight:</td>
            <td><% out.println(session.getAttribute("weight")); %></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td><input type="submit" value="Order" /></td>
            <td><input type="submit" value="Cancel"/></td>
        </tr>
        </table>
</form>
    </body>
</html>
