<%-- 
    Document   : listTracking
    Created on : Nov 1, 2010, 12:19:21 PM
    Author     : Kawai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer Tracking</title>
    </head>
    <body>
        <table>
            <tr>
                <td width="30%">Date</td>
                <td width="40%">Address</td>
                <td width="30%">Receiver</td>
            </tr>
            <% java.util.List<net.tars.xml.tracking.Trackings.Tracking> records = (java.util.List<net.tars.xml.tracking.Trackings.Tracking>)request.getAttribute("trackingList");
            for(net.tars.xml.tracking.Trackings.Tracking record : records){ %>
<tr>
    <td><% out.println(record.getDate().toString()); %></td>
    <td><% out.println(record.getAddress()); %></td>
    <td><% out.println(record.getReceiver()); %></td>
</tr>
            <% } %>
        </table>
    </body>
</html>
