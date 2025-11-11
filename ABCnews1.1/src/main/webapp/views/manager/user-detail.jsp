<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5" style="max-width: 600px;">
  <h1 class="text-center mb-4">👤 Chi tiết người dùng</h1>

  <!-- ✅ Toast -->
  <c:if test="${not empty message}">
    <div class="toast-msg success">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="toast-msg error">${error}</div>
  </c:if>

  <div class="card shadow-sm">
    <div class="card-body">
      <h3 class="card-title">${user.fullname}</h3>
      <p><strong>📧 Email:</strong> ${user.email}</p>
      <p><strong>📞 Số điện thoại:</strong> ${user.mobile}</p>
      <p><strong>👤 Giới tính:</strong> ${user.gender ? "Nam" : "Nữ"}</p>
      <p><strong>🎭 Vai trò:</strong>
        <c:choose>
          <c:when test="${user.role == 0}"><span class="badge bg-danger">Admin</span></c:when>
          <c:when test="${user.role == 1}"><span class="badge bg-primary">Tác giả</span></c:when>
          <c:when test="${user.role == 2}"><span class="badge bg-success">Độc giả</span></c:when>
        </c:choose>
      </p>
    </div>
  </div>

  <div class="text-center mt-4">
    <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}" class="btn btn-warning px-4">✏️ Chỉnh sửa</a>
    <a href="${pageContext.request.contextPath}/users" class="btn btn-secondary px-4">⬅️ Quay lại</a>
  </div>
</div>

<style>
.toast-msg { text-align: center; padding: 10px; color: #fff; border-radius: 5px; margin-bottom: 20px; }
.toast-msg.success { background: #198754; }
.toast-msg.error { background: #dc3545; }
</style>
