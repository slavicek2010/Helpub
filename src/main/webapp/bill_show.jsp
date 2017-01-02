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
        <a href="/bills">Back to bills list</a><br>
        <div class="half">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Bill - ${bill.name}</h3>
                    <c:if test="${admin || loggedUser.isInBill(bill)}">
                        <a href="/bills/edit?id=${bill.id}">Edit name</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </c:if>
                </div>
                <div class="panel-body" style="min-height: 500px">
                    <c:forEach var="billItem" items="${billItems}">
                        ${billItem.addedBy} : ${billItem.item.name} ${billItem.price} CZK ${billItem.quantity}
                        <form action="/bills/increaseQuantity" method="put">
                            <input type="hidden" name="id" value="${bill.id}"/>
                            <input type="hidden" name="item" value="${billItem.item.name}"/>
                            <input type="number" name="howMuch" value="0" min="${-billItem.quantity}" required/>
                            <input type="submit" class="btn btn-small btn-success" value="Change quantity">
                            <a href="/bills/removeItem?id=${bill.id}&item=${billItem.item.name}" class="btn btn-small btn-danger">Remove</a>
                        </form>

                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="half second">
            Add item to bill
            <form:form action="/bills/addItem?id=${bill.id}" method="post" modelAttribute="item">
                <div class="form-group">
                    Item:
                    <form:select path="name" required="required">
                        <form:option value="" label="*** Select Item ***" />
                        <form:options items="${items}" />
                    </form:select>
                </div>
                <div class="form-group">
                    Price:
                    <input type="number" step="0.01" class="form-control" name="price" required/>
                </div>
                <input type="submit" class="btn btn-small btn-success btn-block" value="Add">
            </form:form>
            Item not in the list?
            <a href="/items/create">Add it here!</a>
        </div>
    </jsp:body>
</t:template>