<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<html>
    <head lang="en">
        <meta charset="UTF-8"/>
        <title></title>
        <script src="//code.jquery.com/jquery-1.11.1.js"></script>
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="<%=request.getContextPath()%>/resources/styles/template.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/resources/styles/home.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/resources/styles/bills.css" rel="stylesheet">
    </head>
<body>
<div id="pageheader">
    <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal.username" />
        <sec:authentication property="principal.authorities" />
        <a href="/logout" >Logout</a>
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
        <a href="/login" >Log in</a>
        <a href="/users/create"> Register</a>
    </sec:authorize>
    <jsp:invoke fragment="header"/>
</div>
<div id="body">
    <!--TODO seskupit errory-->

    <c:forEach var="errorMsg" items="${errors}">
        <li style="color:red">${errorMsg}</li>
    </c:forEach>
    <c:forEach var="msg" items="${messages}">
        <li style="color:cornflowerblue">${msg}</li>
    </c:forEach>
    <jsp:doBody/>
</div>
<div id="pagefooter">
    <span style="color:red; height: 100px">This is footer</span>
    <jsp:invoke fragment="footer"/>
</div>
</body>
</html>