<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 23.12.2016
  Time: 21:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>
        <div class="container" style="padding-top: 50px">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="login-panel panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Login</h3>
                        </div>
                        <div class="panel-body">
                            <form:form id="loginForm" method="post" action="${loginProcessingUrl}">
                                <div class="form-group">
                                    <label for="username">Username</label>
                                    <input type="text" id="username" name="username"  class="form-control" required/>
                                </div>
                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <input type="password" id="password" name="password" class="form-control" required/>
                                </div>
                                <input type="submit" value="Submit" id="login" class="btn btn-lg btn-success btn-block"/>
                                <input type="hidden" name="${_csrf.parameterName}"
                                       value="${_csrf.token}" />
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
            Do not have account?
            <a href="/users/create" id="signUp">Register here</a>
        </div>
</jsp:body>
</t:template>

