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
                     <div class="login-panel panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">${formName}</h3>
                        </div>
                         <div class="panel-body">

                            <form:form action="${formAction}" method="post" modelAttribute="user">
                                    <div class="form-group">
                                       Username:<form:input class="form-control" id="username" readonly="${pkHidden}" path="username" required="required"/>
                                    </div>
                                    <div class="form-group">
                                        First Name:<form:input class="form-control"  id="firstName" path="firstName" required="required"/>
                                    </div>
                                    <div class="form-group">
                                        Last Name:<form:input class="form-control" id="lastName" path="lastName" required="required"/>
                                    </div>
                                    <c:if test="${!passwordFieldHidden}">
                                    <div class="form-group">
                                        Password:<form:password class="form-control" id="password" path="password" required="required"/>
                                    </div>
                                    <tr class="form-group">
                                        Retype Password:<form:password class="form-control" id="passwordRetype" path="passwordRetype" required="required"/>
                                    </tr>
                                    </c:if>
                                    <input type="submit" class="btn btn-small btn-success btn-block" value="Save">
                            </form:form>
                         </div>
                     </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:template>