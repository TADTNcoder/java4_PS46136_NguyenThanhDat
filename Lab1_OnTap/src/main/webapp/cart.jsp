<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
</head>
<body>

<jsp:include page="menu.jsp"></jsp:include>
    <h1>Danh sách sản phẩm</h1>
    <table border="1">
        <thead>
            <tr>
                <th>Tên sản phẩm</th>
                <th>Hình ảnh</th>
                <th>Giá</th>
                <th>Khuyến mãi</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.name}</td>
                    <td>
                        <img src="${pageContext.request.contextPath}/img/${item.image}" alt="${item.name}" width="100"/>
                    </td>
                    <td><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="$"/></td>
                    <td><fmt:formatNumber value="${item.discount * 100}" type="number"/>%</td>
                    <td>
                        <form action="CartController1" method="get">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="itemName" value="${item.name}">
                            <label>Số lượng:</label>
                            <input type="number" name="quantity" min="1" required value="1">
                            <input type="submit" value="Thêm vào giỏ">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Giỏ hàng</h2>
    <c:if test="${not empty cartItems}">
        <table border="1">
            <thead>
                <tr>
                    <th>Tên sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Giá</th>
                    <th>Tổng giá</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cartItem" items="${cartItems}">
                    <tr>
                        <td>${cartItem.item.name}</td>
                        <td>
                            <form action="CartController1" method="get" style="display: inline;">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="itemName" value="${cartItem.item.name}">
                                <input type="number" name="quantity" value="${cartItem.quantity}" min="1" required>
                                <input type="submit" value="Cập nhật">
                            </form>
                        </td>
                        <td><fmt:formatNumber value="${cartItem.item.price}" type="currency" currencySymbol="$"/></td>
                        <td><fmt:formatNumber value="${cartItem.getTotalPrice()}" type="currency" currencySymbol="$"/></td>
                        <td>
                            <form action="CartController1" method="get" style="display: inline;">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="itemName" value="${cartItem.item.name}">
                                <input type="submit" value="Xóa">
                            </form>
                            <a href="/Lab3_java3_fall2024/remove?itemName=${cartItem.item.name}">xóa</a>
                            <a href="CartController1?action=remove&itemName=${cartItem.item.name}">xóa 1</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <h3>Tổng giá giỏ hàng: <fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="$"/></h3>
    </c:if>
    
    <c:if test="${empty cartItems}">
        <p>Giỏ hàng của bạn đang trống.</p>
    </c:if>
    
    <hr/>
</body>
</html>
