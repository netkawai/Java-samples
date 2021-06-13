<%-- 
    Document   : OrderInfo
    Created on : Oct 30, 2010, 10:05:47 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List of Post Order</title>
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
   <td>Order Number</td>
   <td></td><td></td>
</tr>
<% aptech.tars.entities.Employee employee = (aptech.tars.entities.Employee)request.getAttribute("employeeInfo");
for(aptech.tars.entities.PostOrder order : employee.getPostOrderCollection()){ %>
<tr>
    <td><% out.println(order.getOrderNum().toString()); %></td>
    <td><a href="ListTracking?orderNum=<% out.println(order.getOrderNum().toString()); %>">Tracking</a></td>
    <td><a href="confirmDeleteOrder?orderNum=<% out.println(order.getOrderNum().toString()); %>">Delete</a></td>
</tr>
<% } %>
</table>
         </div>
    </body>
</html>
