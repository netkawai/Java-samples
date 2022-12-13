<%-- 
    Document   : TrackingInfo
    Created on : Oct 30, 2010, 6:46:24 PM
    Author     : Kawai
--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tracking Information</title>
    </head>
    <body>
<form action="AddTracking" method="POST">
    <table border="0">
        <tr>
            <td>Address:</td>
            <td><input type="text" name="address" value="" /></td>
        </tr>
        <tr>
            <td>Receiver</td>
            <td><input type="text" name="receiver" value="" /></td>
        </tr>
        <tr>
            <td> </td>
            <td> </td>
        </tr>
        <tr>
        <td><input type="submit" value="Add" /></td>
            <td> </td>
        </tr>
    </table>
</form>
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
