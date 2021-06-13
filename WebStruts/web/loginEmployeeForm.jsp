<%-- 
    Document   : loginEmployeeForm
    Created on : Nov 2, 2010, 10:22:41 PM
    Author     : Kawai
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html lang="true">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Employee</title>
        <html:base/>
    </head>
    <body style="background-color: white">
        <html:form action="/loginEmployee">
                    <table border="0">
        <tr>
            <td>Employee Name:</td>
            <td><html:text property="name" /></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><html:password property="password" /></td>
        </tr>
        <tr>
            <td> </td>
            <td> </td>
        </tr>
        <tr>
            <td><html:submit value="Login" /></td>
            <td> </td>
        </tr>
    </table>
        </html:form>
    </body>
</html:html>
