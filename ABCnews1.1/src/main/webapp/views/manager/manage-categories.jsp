<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5">
    <h1 class="mb-4 text-center text-primary fw-bold">Quản lý chuyên mục</h1>

    <!-- ✅ Thông báo thành công / lỗi -->
    <c:if test="${not empty success}">
        <div class="alert alert-success text-center" id="autoHide">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center" id="autoHide">${error}</div>
    </c:if>

    <div class="text-end mb-3">
        <a href="${pageContext.request.contextPath}/categories?action=create" class="btn btn-primary btn-custom">Thêm chuyên mục</a>
    </div>

    <table class="table table-hover table-bordered align-middle shadow-sm">
        <thead class="table-header">
            <tr class="text-center">
                <th style="width: 70px;">#</th>
                <th>Tên chuyên mục</th>
                <th style="width: 180px;">Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="cat" items="${categories}" varStatus="i">
                <tr>
                    <td class="text-center fw-semibold">${i.index + 1}</td>
                    <td>${cat.name}</td>
                    <td class="text-center">
                        <a href="${pageContext.request.contextPath}/categories?action=edit&id=${cat.id}" class="btn btn-sm btn-outline-warning me-2">Sửa</a>
                        <form action="${pageContext.request.contextPath}/categories" method="post" 
                              class="d-inline" 
                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa chuyên mục này?');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${cat.id}">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Xóa</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script>
  // Ẩn thông báo sau 5s
  const alertBox = document.getElementById("autoHide");
  if (alertBox) {
    setTimeout(() => alertBox.style.display = "none", 5000);
  }
</script>

<style>
  body {
    background-color: #f4f8ff;
    font-family: "Segoe UI", Arial, sans-serif;
  }

  .container {
    max-width: 800px;
  }

  .table {
    border-radius: 10px;
    overflow: hidden;
  }

  .table-header {
    background-color: #1a73e8;
    color: white;
  }

  .table th, 
  .table td {
    vertical-align: middle;
  }

  .btn-custom {
    background-color: #1a73e8;
    border: none;
    transition: 0.25s;
  }

  .btn-custom:hover {
    background-color: #0f5bd7;
  }

  .btn-outline-warning {
    color: #e67e22;
    border-color: #e67e22;
  }

  .btn-outline-warning:hover {
    background-color: #e67e22;
    color: white;
  }

  .btn-outline-danger {
    color: #d93025;
    border-color: #d93025;
  }

  .btn-outline-danger:hover {
    background-color: #d93025;
    color: white;
  }

  .alert {
    border-radius: 10px;
    font-weight: 500;
  }
</style>
