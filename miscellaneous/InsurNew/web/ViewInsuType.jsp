<%-- 
    Document   : ViewInsuType
    Created on : Nov 3, 2010, 9:03:28 PM
    Author     : Kawai
--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html>
<head>
<title>Insurance Type List</title>
</head>
<body>
<h1 style="color:white;background-color:#525D76;"></h1>
<table border="1">
  <tr bgcolor="#eeeeee">
    <th>Insurance Type Name</th>
  </tr>
  <logic:iterate id="insuType" name="insu.type" scope="request">
    <tr>
      <td nowrap>
          <html:link action="/ViewInsuDetail"
          paramId="insuTypeID" paramName="insuType" paramProperty="insuTypeID">
          <bean:write name="insuType" property="insuTypeName" />
          </html:link>
      </td>
    </tr>
  </logic:iterate>
</table>
</body>
</html:html>