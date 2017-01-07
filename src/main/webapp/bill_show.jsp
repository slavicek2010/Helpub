<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 25.12.2016
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:template>
    <jsp:body>
        <h1>Bill detail</h1>
        <c:if test="${!bill.opened}">
            <h2 style="color: red">Bill is CLOSED</h2>
        </c:if>
        <a href="/bills">Back to bills list</a><br>
        Connected users:
        <c:forEach items="${billUsers}" var="user">
            |${user.username}|
        </c:forEach>
        <br>
        <div class="half">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Bill - ${bill.name}</h3>
                    <c:if test="${admin || loggedUser.isInBill(bill)}">
                        <a href="/bills/edit?id=${bill.id}">Edit bill properties</a>
                        <br>
                    </c:if>
                    Total: ${total} CZK
                </div>
                <div class="panel-body" style="min-height: 500px">
                    <c:forEach items="${billItems}" var="billsMapRecord">
                       ${billsMapRecord.key} ( ${particularPrices.get(billsMapRecord.key)} CZK)
                        <c:forEach var="billItem" items="${billsMapRecord.value}">
                            <div class="item-info-wrapper">
                                <span class="item-quantity">${billItem.quantity} x</span>
                                <span class="item-info"> ${billItem.item.name} ${billItem.price} CZK </span> <br>
                                <form action="/bills/increaseQuantity" method="put" class="increase-quantity-form">
                                    <input type="hidden" name="id" value="${bill.id}"/>
                                    <input type="hidden" name="item" value="${billItem.item.name}"/>
                                    <input type="hidden" name="user" value="${billItem.addedBy}"/>
                                    <input type="number" class="number-input" name="howMuch" value="0" min="${-billItem.quantity}" required/>
                                    <input type="submit" class="btn btn-small btn-success" value="+/- quantity">
                                </form>
                                <form action="/bills/changeItemPrice" method="put" class="change-item-price-form">
                                    <input type="hidden" name="id" value="${bill.id}"/>
                                    <input type="hidden" name="item" value="${billItem.item.name}"/>
                                    <input type="hidden" name="user" value="${billItem.addedBy}"/>
                                    <input type="number" class="number-input" name="newPrice" value="${billItem.price}" min="0.01" step="0.01" required/>
                                    <input type="submit" class="btn btn-small btn-success" value="Change price">
                                </form>
                                <a href="/bills/removeItem?id=${bill.id}&item=${billItem.item.name}&user=${billItem.addedBy}" id="remove-button" class="btn btn-small btn-danger">
                                    <i class="fa fa-2x fa-trash" aria-hidden="true"></i>
                                </a>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="half second">
            Add item to bill
            <form:form action="/bills/addItem?id=${bill.id}" method="post" modelAttribute="item">
                <div class="form-group">
                    Item:
                    <form:select path="name" required="required" id="itemSelect">
                        <form:option value="" label="*** Select Item ***" />
                        <form:options items="${items}" />
                    </form:select>
                </div>
                <div class="form-group">
                    Price:
                    <input type="number" step="0.01" min="0.01" class="form-control" name="price" required/>
                </div>
                <input type="submit" class="btn btn-small btn-success btn-block" value="Add">
            </form:form>
            Item not in the list?
            <a href="/items/create?billId=${bill.id}" id="addNewItem">Add it here!</a>
        </div>
    </jsp:body>
</t:template>