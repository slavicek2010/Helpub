<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 14.12.2016
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:template>
    <jsp:body>
        <div align="center">
            <h1>Users</h1>
            <h3><a href="/users/create">Register new user</a></h3>
            <table border="1">
                <th>Username</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Action</th>

                <c:forEach var="user" items="${listUsers}">
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>
                            <c:if test="${admin || loggedUserUsername == user.username}">
                            <a href="/users/edit?username=${user.username}">Edit</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${admin}">
                            <a href="/users/delete?username=${user.username}">Delete</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${admin}">
                                <a href="/users/show?username=${user.username}">Show</a>
                            </c:if>
                        </td>

                    </tr>
                </c:forEach>
            </table>
        </div>
        </body>
        </html>
    </jsp:body>
</t:template>
