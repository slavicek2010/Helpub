<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 14.12.2016
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:template>
    <jsp:body>
        <div class="container" style="padding-top: 50px">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <a href="/users">Back to users</a>
                    <div class="login-panel panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">${formName}</h3>
                        </div>
                        <div class="panel-body">
                            Username: ${user.username}<br>
                            Firstname: ${user.firstName}<br>
                            Lastname: ${user.lastName}<br>
                            Enabled: ${user.enabled}<br>
                            <br>
                            User bills (${user.bills.size()} bills):
                            <c:forEach items="${user.bills}" var="bill">
                                <li>${bill.name}</li>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>