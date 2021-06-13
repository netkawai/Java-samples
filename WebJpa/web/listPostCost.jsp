<%-- 
    Document   : listPostCost
    Created on : Nov 1, 2010, 1:04:21 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cost Page</title>
    </head>
    <body>
        <h1>Post Cost Settings</h1>
        <div>
<form action="changePostCost">
<table>
<% java.util.List<aptech.tars.entities.PostCost> records = (java.util.List<aptech.tars.entities.PostCost>)request.getAttribute("records");
for(aptech.tars.entities.PostCost cost : records){ %>
<tr>
    <td><% out.print(cost.getServiceType()); %></td>
    <td><% out.print(cost.getWeight()); %></td>
    <td></td>
    <td><input type="text" name="cost<% out.print(cost.getCostId()); %>" value="<% out.print(cost.getCost()); %>"/></td>
</tr>
<% } %>
<tr>
    <td></td>
    <td></td>
    <td></td>
    <td><input type="submit" value="Change"/></td>
</tr>
</table>
</form>
         </div>
    </body>
</html>
