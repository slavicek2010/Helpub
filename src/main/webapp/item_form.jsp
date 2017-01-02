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
                            <h3 class="panel-title">Create item</h3>
                        </div>
                        <div class="panel-body">
                            <form:form action="/items/create?billId=${billId}" method="post" modelAttribute="item">
                                <div class="form-group">
                                    Item name:<form:input class="form-control" path="name" required="required"/>
                                </div>
                                <div class="form-group">
                                    Type:
                                    <form:select path="type" required="required">
                                        <form:option value="" label="*** Select Option ***" />
                                        <form:options items="${enumValues}" />
                                    </form:select>
                                </div>
                                <input type="submit" class="btn btn-small btn-success btn-block" value="Save">
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>
