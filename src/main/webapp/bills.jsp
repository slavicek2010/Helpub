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
        <a href="/">Back to homepage</a><br>
        <h1>Bills</h1>
        <a class="btn btn-success" href="/bills/create" id="addBill">New bill</a>

        <form action="/bills" method="get" id="showMyBillsForm">
            <label for="showMyBillsCheckBox">Show only my bills</label>
            <input type="checkbox" id="showMyBillsCheckBox" <c:if test="${showBillsChecked}">checked</c:if>>
            <input type="hidden" id="username" name="username" value="${loggedInUsername}">
        </form>
        <script>
            $("#showMyBillsCheckBox").change(function () {
                if($("#showMyBillsCheckBox").is(":checked")){
                    $("#showMyBillsForm").submit();
                }else {
                    $(location).attr('href',"/bills");
                }
            })
        </script>
        <div style="width: 50%; display: inline-block">
            <table align="center" class="table table-bordered" style="text-align: center" id="tableOfBills">
                <tr>
                    <th>Name</th>
                    <th>Opened</th>
                    <th>Locked</th>
                    <th>Number of users</th>
                    <th>Creator</th>
                    <th colspan="4">Action</th>
                </tr>
                <c:forEach var="bill" items="${listBills}">
                    <tr id="tbodyRow">
                        <td id="billName">${bill.name}</td>
                        <td id="billOpened">${bill.opened}</td>
                        <td id="billLocked">
                            <c:if test="${bill.locked}">
                                <i class="fa fa-lg fa-lock" aria-hidden="true"></i>
                            </c:if>
                            <c:if test="${!bill.locked}">
                                <i class="fa fa-lg fa-unlock" aria-hidden="true"></i>
                            </c:if>
                        </td>
                        <td id="billUserCount">${bill.users.size()}</td>
                        <td id="billCreator">${bill.creatorUsername}</td>
                        <td id="deleteBill">
                            <c:if test="${admin || loggedInUsername == bill.creatorUsername }">
                                    <a href="/bills/delete?id=${bill.id}" title="Delete bill">
                                        <i class="fa fa-lg fa-trash" aria-hidden="true"></i>
                                    </a>
                            </c:if>
                        </td>
                        <td id="closeBill">
                            <c:if test="${bill.opened && (admin || loggedInUsername == bill.creatorUsername)}">
                                    <a href="/bills/closeBill?id=${bill.id}" title="Close bill">
                                        <i class="fa fa-lg fa-times" aria-hidden="true"></i>
                                    </a>
                            </c:if>
                            <c:if test="${!bill.opened && (admin || loggedInUsername == bill.creatorUsername)}">
                                <a href="/bills/reopenBill?id=${bill.id}" title="Reopen bill">
                                    <i class="fa fa-lg fa-folder-open-o" aria-hidden="true"></i>
                                </a>
                            </c:if>
                        </td>
                        <td id="showBill">
                            <c:if test="${admin || bill.containsLoggedUser()}">
                                <a href="/bills/show?id=${bill.id}" title="Show bill">
                                    <i class="fa fa-lg fa-eye" aria-hidden="true"></i>
                                </a>
                            </c:if>
                            <c:if test="${bill.opened && !bill.containsLoggedUser()}">
                                <a href="/bills/show?id=${bill.id}" title="Join bill">
                                    <i class="fa fa-lg fa-sign-in" aria-hidden="true"></i>
                                </a>
                            </c:if>

                        </td>
                        <td id="leaveBill">
                            <c:if test="${bill.containsLoggedUser()}">
                                <a href="/bills/removeUser?id=${bill.id}" title="Leave bill">
                                    <i class="fa fa-lg fa-sign-out" aria-hidden="true"></i>
                                </a>
                            </c:if>
                        </td>

                    </tr>
                </c:forEach>
            </table>
        </div>
    </jsp:body>
</t:template>