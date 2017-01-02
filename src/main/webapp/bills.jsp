<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 14.12.2016
  Time: 19:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:template>
    <jsp:body>

        <h1>Bills</h1>
        <a href="/bills/create">New bill</a>
        <table border="1" align="center">
            <th>Name</th>
            <th>Opened</th>
            <th>Action</th>

            <c:forEach var="bill" items="${listBills}">
                <tr>
                    <td>${bill.name}</td>
                    <td>${bill.opened}</td>
                    <td>
                        <c:if test="${admin || loggedInUsername == bill.creatorUsername}">
                            <a href="/bills/delete?id=${bill.id}">Delete</a>
                        </c:if>
                        <c:if test="${admin || loggedInUsername == bill.creatorUsername}">
                            <a href="/bills/show?id=${bill.id}">Show</a>
                        </c:if>
                    </td>

                </tr>
            </c:forEach>
        </table>
    </jsp:body>
</t:template>