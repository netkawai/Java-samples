<%-- 
    Document   : addEmployeeForm
    Created on : Nov 1, 2010, 12:44:09 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Employee Form</title>
    </head>
    <body>
        <form action="addEmployee">
        <table>
        <tr>
            <td>Name:</td>
            <td><input type="text" name="name" value="" /></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" value="" /></td>
        </tr>
        <tr>
            <td></td><td></td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="add" />
            </td>
            <td></td>
        </tr>
        </table>
</form>
    </body>
</html>
