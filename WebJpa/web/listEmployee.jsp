<%-- 
    Document   : listEmployeee
    Created on : Nov 1, 2010, 12:35:03 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Employee</title>
    </head>
    <body>
        <h1>
        <div>
            <%  HttpSession sn = request.getSession();
                if(!sn.isNew()){
                    out.println("Welcome   " + sn.getAttribute("name"));
                }
                else{
                    out.println("Please login at first.");
                }
            %>
        </div>
        </h1>
        <div>
<table>
<tr>
    <td>Name</td><td>Password</td>
   <td></td><td></td>
</tr>
<% java.util.List<aptech.tars.entities.Employee> records
            = (java.util.List<aptech.tars.entities.Employee>)request.getAttribute("records");
for(aptech.tars.entities.Employee employee : records){ %>
<tr>
    <td><% out.println(employee.getName()); %></td>
    <td><% out.println(employee.getPassword()); %></td>
    <td></td>
    <td><a href="confirmDeleteEmployee?empId=<% out.println(employee.getEmployeeId()); %>">Delete</a></td>
</tr>
<% } %>
</table>
         </div>

    </body>
</html>
