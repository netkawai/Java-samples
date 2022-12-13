<%-- 
    Document   : addOrderForm
    Created on : Oct 31, 2010, 11:53:37 AM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Post Information</title>
    </head>
<body>
<form action="confirmOrder">
        <table>
        <tr>
            <td>Contact:</td>
            <td><input type="text" name="contact" value="" /></td>
        </tr>
        <tr>
            <td>From:</td>
            <td><input type="text" name="from" value="" /></td>
        </tr>
        <tr>
            <td>To:</td>
            <td><input type="text" name="to" value="" /></td>
        </tr>
        <tr>
            <td></td><td></td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="cofirm" />
            </td>
            <td></td>
        </tr>
        </table>
</form>
    </body>
</html>
