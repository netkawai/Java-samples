<%-- 
    Document   : CalculationOrder
    Created on : Oct 30, 2010, 11:29:39 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Post fee calculation</title>
    </head>
    <body>
<form id="calculationForm" name="calculationOrder" action="calculationOrder" method="POST">
    <table border="0">
        <tr>
            <td>Quantity</td>
            <td><input type="text" name="quantity" value="" /></td>
        </tr>
        <tr>
            <td>Service:</td>
            <td>
                <select name="service">
                <option value="normal">Normal Post</option>
                <option value="speed">Speed Post</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>Weight(g):</td>
            <td>
                <select name="weight">
                <option value="250">under 250g</option>
                <option value="500">500g - 1kg</option>
                <option value="1000">over 1kg</option>
                </select>
            </td>
        </tr>
        <tr>
            <td> </td>
            <td> </td>
        </tr>
        <tr>
        <td><input type="submit" value="Calculation" /></td>
        <td></td>
        </tr>
    </table>
</form>
    </body>
</html>
