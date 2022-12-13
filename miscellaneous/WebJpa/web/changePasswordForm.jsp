<%-- 
    Document   : changePasswordForm
    Created on : Nov 3, 2010, 12:28:25 AM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
    </head>
    <body>
<form action="changePassword">
        <table>
        <tr>
            <td>New password:</td>
            <td><input type="password" name="password1" value="" /></td>
        </tr>
        <tr>
            <td>New password(confirm):</td>
            <td><input type="password" name="password2" value="" /></td>
        </tr>
        <tr>
            <td></td><td></td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="Change" />
            </td>
            <td></td>
        </tr>
        </table>
</form>
    </body>
</html>
