<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 25.12.2016
  Time: 18:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:template>
    <jsp:body>
        <div class="container" style="padding-top: 50px">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="login-panel panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">${formName}</h3>
                        </div>
                        <div class="panel-body">
                            <form:form action="${formAction}" method="post" modelAttribute="bill">
                                <div class="form-group">
                                    Bill name:<form:input class="form-control" path="name" required="required"/>
                                </div>
                                <c:if test="${includeId}">
                                    <form:hidden path="id"/>
                                </c:if>
                                <form:hidden path="creatorUsername" value="${loggedInUsername}"/>
                                <input type="submit" class="btn btn-small btn-success btn-block" value="Save">
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>
