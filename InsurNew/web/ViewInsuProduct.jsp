<%-- 
    Document   : ViewInsuProduct
    Created on : Nov 3, 2010, 11:13:22 PM
    Author     : Kawai
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html>
<head>
<title>Insurance Product List</title>
</head>
<body>
<h1 style="color:white;background-color:#525D76;"></h1>
<table border="1">
  <tr bgcolor="#eeeeee">
      <th>Insurance Product</th><th>Details</th>
  </tr>
  <logic:iterate id="insuProduct" name="insu.product" scope="request">
    <tr>
      <td nowrap>
          <bean:write name="insuProduct" property="insuProductName" />
      </td>
      <td><bean:write name="insuProduct" property="details"/>
      </td>
    </tr>
  </logic:iterate>
</table>
</body>
</html:html>

