<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<html:html>
<body bgcolor="#ffffff" text="#000000" link="#023264"
  alink="#023264" vlink="#023264">
  <table border="0" width="100%" cellspacing="5">
    <tr>
      <td colspan="2"><tiles:insert page="header.jsp"/></td>
    </tr>
    <tr>
      <td width="140" height="100" valign="top">
        <tiles:insert attribute="menu"/></td>
      <td valign="top" align="left">
        <tiles:insert attribute="content"/></td>
    </tr>
  </table>
</body>
</html:html>