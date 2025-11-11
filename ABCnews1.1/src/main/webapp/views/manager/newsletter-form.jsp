<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5">
  <h1 class="text-center mb-4">
    <c:choose>
      <c:when test="${not empty subscriber}">✏️ Cập nhật đăng ký</c:when>
      <c:otherwise>➕ Thêm đăng ký mới</c:otherwise>
    </c:choose>
  </h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty message}">
    <div class="alert alert-success text-center">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger text-center">${error}</div>
  </c:if>

  <form action="${pageContext.request.contextPath}/newsletter" method="post" class="mx-auto" style="max-width: 500px;">
    <!-- ✅ Xác định action -->
    <input type="hidden" name="action" value="${not empty subscriber ? 'update' : 'create'}">

    <!-- 📧 Email -->
    <div class="mb-3">
      <label for="email" class="form-label fw-bold">📧 Email:</label>
      <input type="email" id="email" class="form-control" name="email"
             value="${subscriber.email}" 
             <c:if test="${not empty subscriber}">readonly</c:if> required>
      <div class="form-text">* Email là duy nhất và không thể thay đổi khi cập nhật.</div>
    </div>

    <!-- ✍️ Chọn tác giả -->
    <div class="mb-3">
      <label for="authorId" class="form-label fw-bold">✍️ Tác giả theo dõi:</label>
      <select id="authorId" name="authorId" class="form-select" required>
        <option value="">-- Chọn tác giả --</option>
        <c:forEach var="author" items="${authors}">
          <option value="${author.id}" 
            <c:if test="${not empty subscriber && subscriber.authorId == author.id}">selected</c:if>>
            ${author.fullname}
          </option>
        </c:forEach>
      </select>
    </div>

    <!-- 📌 Trạng thái -->
    <div class="mb-3">
      <label for="enabled" class="form-label fw-bold">📌 Trạng thái:</label>
      <select id="enabled" name="enabled" class="form-select">
        <option value="true" <c:if test="${not empty subscriber && subscriber.enabled}">selected</c:if>>Đang hoạt động</option>
        <option value="false" <c:if test="${not empty subscriber && !subscriber.enabled}">selected</c:if>>Đã hủy</option>
      </select>
    </div>

    <!-- 🔘 Nút hành động -->
    <div class="text-center mt-4">
      <button type="submit" class="btn btn-primary px-4">💾 Lưu</button>
      <a href="${pageContext.request.contextPath}/newsletter" class="btn btn-secondary px-4">⬅️ Quay lại</a>
    </div>
  </form>
</div>

<script>
  // 🕐 Tự ẩn thông báo sau 5s
  setTimeout(() => {
    document.querySelectorAll('.alert').forEach(a => a.style.display = 'none');
  }, 5000);
</script>

<style>
  h1 {
    font-weight: 700;
    font-size: 2rem;
  }
  .form-label {
    font-weight: 600;
  }
  .btn-primary {
    background: #0d6efd;
    border: none;
  }
  .btn-primary:hover {
    background: #0b5ed7;
  }
  .btn-secondary {
    background: #6c757d;
    border: none;
  }
  .btn-secondary:hover {
    background: #5c636a;
  }
</style>
s