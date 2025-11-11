<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5" style="max-width: 700px;">
  <h1 class="text-center mb-4">✏️ Cập nhật thông tin cá nhân</h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty message}"><div class="toast-msg success">${message}</div></c:if>
  <c:if test="${not empty error}"><div class="toast-msg error">${error}</div></c:if>

  <!-- 📦 Form cập nhật bản thân -->
  <form action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="csrfToken" value="${csrfToken}"/>
    <input type="hidden" name="action" value="updateSelf"/>

    <div class="mb-3">
      <label>Họ và tên</label>
      <input type="text" class="form-control" name="fullname" value="<c:out value='${user.fullname}'/>" required>
    </div>

    <div class="mb-3">
      <label>Email</label>
      <input type="email" class="form-control" name="email" value="<c:out value='${user.email}'/>" required>
    </div>

    <div class="mb-3">
      <label>Mật khẩu mới</label>
      <input type="password" class="form-control" name="password" placeholder="Để trống nếu không đổi">
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

    <div class="text-center mt-4 d-flex justify-content-center gap-3 flex-wrap">
      <button type="submit" class="btn btn-primary px-4">💾 Cập nhật</button>
      <a href="${pageContext.request.contextPath}/users?action=detailSelf" class="btn btn-secondary px-4">⬅️ Quay lại</a>
    </div>
  </form>
</div>

<style>
.toast-msg { text-align:center; padding:10px; color:#fff; border-radius:5px; margin-bottom:20px; }
.toast-msg.success { background:#198754; }
.toast-msg.error { background:#dc3545; }
</style>
