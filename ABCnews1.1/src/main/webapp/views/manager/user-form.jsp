<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5" style="max-width: 700px;">
  <h1 class="text-center mb-4">
    <c:choose>
      <c:when test="${empty user}">➕ Thêm người dùng mới</c:when>
      <c:otherwise>✏️ Cập nhật người dùng</c:otherwise>
    </c:choose>
  </h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty message}"><div class="toast-msg success">${message}</div></c:if>
  <c:if test="${not empty error}"><div class="toast-msg error">${error}</div></c:if>

  <!-- 📦 Form quản trị -->
  <form action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="csrfToken" value="${csrfToken}"/>

    <c:if test="${not empty user}">
      <input type="hidden" name="id" value="${user.id}">
    </c:if>

    <input type="hidden" name="action" value="<c:out value='${empty user ? "create" : "update"}'/>">

    <div class="mb-3">
      <label>Họ và tên</label>
      <input type="text" class="form-control" name="fullname" value="<c:out value='${user.fullname}'/>" required>
    </div>

    <div class="mb-3">
      <label>Email</label>
      <input type="email" class="form-control" name="email" value="<c:out value='${user.email}'/>" required>
    </div>

    <div class="mb-3">
      <label>Mật khẩu</label>
      <c:choose>
        <c:when test="${empty user}">
          <input type="password" class="form-control" name="password" placeholder="Nhập mật khẩu" required>
        </c:when>
        <c:otherwise>
          <input type="password" class="form-control" name="password" placeholder="Nhập để đổi mật khẩu (bỏ trống nếu giữ nguyên)">
        </c:otherwise>
      </c:choose>
    </div>

    <div class="mb-3">
      <label>Ngày sinh</label>
      <input type="date" class="form-control" name="birthday" 
             value="<fmt:formatDate value='${user.birthday}' pattern='yyyy-MM-dd'/>">
    </div>

    <div class="mb-3">
      <label>Giới tính</label>
      <select name="gender" class="form-select">
        <option value="true" <c:if test="${user.gender}">selected</c:if>>Nam</option>
        <option value="false" <c:if test="${not user.gender}">selected</c:if>>Nữ</option>
      </select>
    </div>

    <div class="mb-3">
      <label>Số điện thoại</label>
      <input type="text" class="form-control" name="mobile" value="<c:out value='${user.mobile}'/>">
    </div>

    <div class="mb-3">
      <label>Vai trò</label>
      <select name="role" class="form-select" required>
        <option value="0" <c:if test="${user.role == 0}">selected</c:if>>Admin</option>
        <option value="1" <c:if test="${user.role == 1}">selected</c:if>>Tác giả</option>
        <option value="2" <c:if test="${user.role == 2}">selected</c:if>>Độc giả</option>
      </select>
    </div>

    <div class="text-center mt-4 d-flex justify-content-center gap-3 flex-wrap">
      <button type="submit" class="btn btn-primary px-4">
        <c:choose>
          <c:when test="${empty user}">➕ Thêm mới</c:when>
          <c:otherwise>💾 Cập nhật</c:otherwise>
        </c:choose>
      </button>
      <a href="${pageContext.request.contextPath}/users" class="btn btn-secondary px-4">⬅️ Quay lại</a>
    </div>
  </form>

  <c:if test="${not empty user}">
    <form action="${pageContext.request.contextPath}/users" method="post" class="mt-4 text-center"
          onsubmit="return confirm('❗ Bạn có chắc chắn muốn xóa người dùng này không?');">
      <input type="hidden" name="action" value="delete">
      <input type="hidden" name="id" value="${user.id}">
      <input type="hidden" name="csrfToken" value="${csrfToken}">
      <button type="submit" class="btn btn-danger px-4">🗑️ Xóa người dùng</button>
    </form>
  </c:if>
</div>

<style>
.toast-msg { text-align:center; padding:10px; color:#fff; border-radius:5px; margin-bottom:20px; }
.toast-msg.success { background:#198754; }
.toast-msg.error { background:#dc3545; }
</style>
