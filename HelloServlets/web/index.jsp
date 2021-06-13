<%-- 
    Document   : index
    Created on : Sep 3, 2010, 10:30:54 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Input Form Page</title>
    </head>
    <body>

        <form action="RegisterEmployee" method="POST">
            <table border="0">
                <tr>
                    <td>Name:</td>
                    <td><input type="text" name="name" value="" /></td>
                </tr>
                <tr>
                    <td>E-Mail:</td>
                    <td><input type="text" name="email" value="" /></td>
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
        <table>
            <tr>
                <td width="10%">id</td>
                <td width="20%">Name</td>
                <td width="30%">E-Mail</td>
            </tr>
            <sql:setDataSource var="studydb" driver="sun.jdbc.odbc.JdbcOdbcDriver" url="jdbc:odbc:Study" user="kawai" password="123"/>
            <sql:query var="rs" sql="select * from contact" dataSource="${studydb}"/>
            <c:forEach var="r" items="${rs.rows}">
               <tr>
                   <td><c:out value="${r.id}" /></td>
                    <td><c:out value="${r.name}" /></td> 
                    <td><c:out value="${r.email}" /></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
