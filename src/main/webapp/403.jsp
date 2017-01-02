<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 25.12.2016
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:template>
    <jsp:body>
        <h1>HTTP Status 403 - Access is denied</h1>
        <h2>${msg}</h2>
        <a href="/">Back to homepage</a><br><br>
        <iframe src="//giphy.com/embed/l3vRaZ4bCICfxcd68" width="320" height="320" frameBorder="0" class="giphy-embed" allowFullScreen></iframe><p><a href="http://giphy.com/gifs/mlb-baseball-crying-l3vRaZ4bCICfxcd68">via GIPHY</a></p>
    </jsp:body>
</t:template>
