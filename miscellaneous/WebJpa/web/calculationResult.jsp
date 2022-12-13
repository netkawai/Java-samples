<%-- 
    Document   : calculationResult
    Created on : Oct 31, 2010, 11:29:36 AM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calculation Result</title>
    </head>
    <body>
<form action="addOrderForm.jsp">
    <table border="0">
        <tr>
            <td>Quantity</td>
            <td><% out.println(request.getAttribute("quantity")); %></td>
        </tr>
        <tr>
            <td>Service:</td>
            <td><% out.println(request.getAttribute("service")); %></td>
        </tr>
        <tr>
            <td>Weight(g):</td>
            <td><% out.println(request.getAttribute("weight")); %></td>
        </tr>
        <tr>
            <td>Cost: </td>
            <td><% out.println(request.getAttribute("cost")); %></td>
        </tr>
        <tr>
            <td> </td>
            <td> </td>
        </tr>
        <tr>
            
        <td><input type="submit" value="Order" /></td>

        <td><input type="submit" value="Cancel" /></td>
        </tr>
    </table>
</form>
    </body>
</html>
