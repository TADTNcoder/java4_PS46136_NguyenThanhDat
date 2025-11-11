<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  // ✅ Chỉ huỷ session cũ nếu chưa đăng nhập
  // Tránh xóa session mới sau khi đăng nhập xong (gây lỗi quay lại trang login)
  if (session != null && session.getAttribute("email") == null) {
      session.invalidate();
  }
%>

<!-- ✅ THÔNG BÁO -->
<c:if test="${not empty error}">
  <div id="alertBox" class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div id="alertBox" class="alert alert-success">${message}</div>
</c:if>

<!-- ===== 🔐 FORM ĐĂNG NHẬP ===== -->
<div class="form-container">
  <h2>🔐 Đăng nhập hệ thống</h2>

  <form action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="action" value="login" />
    <!-- ✅ CSRF token (không bắt buộc nhưng khuyến khích) -->
    <c:if test="${not empty csrfToken}">
      <input type="hidden" name="csrfToken" value="${csrfToken}" />
    </c:if>

    <div class="mb-3">
      <label for="email">📧 Email:</label>
      <input type="email" id="email" name="email" required placeholder="Nhập email đăng nhập" />
    </div>

    <div class="mb-3">
      <label for="password">🔑 Mật khẩu:</label>
      <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu" />
    </div>

    <button type="submit">Đăng nhập</button>
  </form>

  <div class="note">
    <p>👉 Chỉ người dùng đã được đăng ký mới có thể truy cập hệ thống.</p>
  </div>
</div>

<script>
  // ✅ Tự động ẩn thông báo sau 5 giây
  window.addEventListener("DOMContentLoaded", function () {
    const alertBox = document.getElementById("alertBox");
    if (alertBox) {
      setTimeout(() => {
        alertBox.classList.add("fade-out");
        setTimeout(() => alertBox.remove(), 500);
      }, 5000);
    }
  });
</script>

<style>
  body {
    background: #f1f3f5;
    font-family: Arial, sans-serif;
  }

  .form-container {
    width: 420px;
    margin: 100px auto;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
    padding: 2rem;
    text-align: center;
  }

  h2 {
    margin-bottom: 1.5rem;
    font-weight: bold;
  }

  label {
    float: left;
    margin-bottom: 0.4rem;
    font-weight: 600;
  }

  input {
    width: 100%;
    padding: 10px;
    margin-bottom: 1.2rem;
    border: 1px solid #ccc;
    border-radius: 8px;
  }

  button {
    width: 100%;
    padding: 10px;
    border: none;
    background: #212529;
    color: #fff;
    border-radius: 8px;
    cursor: pointer;
    transition: 0.3s;
  }

  button:hover {
    background: #ffc107;
    color: #212529;
  }

  .alert {
    width: 420px;
    margin: 20px auto;
    padding: 12px 18px;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 500;
    text-align: center;
    animation: fadeIn 0.4s ease-in-out;
  }

  .alert-success {
    background: #d1e7dd;
    color: #0f5132;
    border: 1px solid #badbcc;
  }

  .alert-danger {
    background: #f8d7da;
    color: #842029;
    border: 1px solid #f5c2c7;
  }

  .fade-out {
    animation: fadeOut 0.5s ease-in-out forwards;
  }

  @keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
  }

  @keyframes fadeOut {
    from { opacity: 1; }
    to { opacity: 0; }
  }

  .note {
    margin-top: 1rem;
    font-size: 0.9rem;
    color: #555;
  }
</style>
