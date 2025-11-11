<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="container mt-5">
    <!-- Tiêu đề động -->
    <h1 class="mb-4 text-center title">
        <c:choose>
            <c:when test="${not empty category}">Cập nhật chuyên mục</c:when>
            <c:otherwise>Thêm chuyên mục mới</c:otherwise>
        </c:choose>
    </h1>

    <!-- Hiển thị thông báo lỗi (chỉ khi có) -->
    <c:if test="${not empty error and error ne ''}">
        <div class="alert alert-danger text-center" id="autoHide">${error}</div>
    </c:if>

    <!-- Form tạo / cập nhật -->
    <form action="${pageContext.request.contextPath}/categories" method="post" 
          class="shadow p-4 rounded form-container">

        <input type="hidden" name="action" 
               value="${not empty category ? 'update' : 'create'}" />

        <c:if test="${not empty category}">
            <input type="hidden" name="id" value="${category.id}" />
        </c:if>

        <div class="mb-3">
            <label for="name" class="form-label">Tên chuyên mục</label>
            <input type="text" class="form-control" id="name" name="name" 
                   placeholder="Nhập tên chuyên mục..." 
                   value="${category.name}" required>
        </div>

        <div class="d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/categories" class="btn btn-secondary custom-btn">
                Quay lại
            </a>
            <button type="submit" class="btn btn-primary custom-btn">Lưu</button>
        </div>
    </form>
</div>

<!-- Tự ẩn thông báo lỗi sau 5s -->
<script>
  const alertBox = document.getElementById("autoHide");
  if (alertBox) {
    setTimeout(() => alertBox.style.display = "none", 5000);
  }
</script>

<style>
  body {
    background-color: #f5f8ff;
    font-family: "Segoe UI", sans-serif;
  }

  .container {
    max-width: 600px;
  }

  .title {
    color: #1a73e8;
    font-weight: 600;
  }

  .form-container {
    background-color: #ffffff;
    border: 1px solid #d6e0f0;
  }

  .form-label {
    color: #3b3b3b;
    font-weight: 500;
  }

  .form-control {
    border: 1px solid #b8c6e0;
    transition: all 0.2s;
  }

  .form-control:focus {
    border-color: #1a73e8;
    box-shadow: 0 0 0 0.15rem rgba(26, 115, 232, 0.25);
  }

  .custom-btn {
    font-weight: 500;
    transition: all 0.3s;
  }

  .btn-primary {
    background-color: #1a73e8;
    border-color: #1a73e8;
  }

  .btn-primary:hover {
    background-color: #0f5bd7;
    border-color: #0f5bd7;
  }

  .btn-secondary {
    background-color: #6c757d;
    border-color: #6c757d;
  }

  .btn-secondary:hover {
    background-color: #5a6268;
    border-color: #5a6268;
  }
</style>
