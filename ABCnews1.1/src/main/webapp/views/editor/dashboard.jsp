<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="author-dashboard container py-5">
  <h1 class="text-center text-success fw-bold mb-3">
    👋 Xin chào, <strong>${sessionScope.user.fullname}</strong>
  </h1>
  <p class="text-center mb-4 text-muted">
    Bạn đang đăng nhập với vai trò:
    <span class="role-badge role-author">✍️ Tác giả</span>
  </p>

  <div class="divider mb-5"></div>

  <section class="dashboard-section">
    <h2 class="section-title mb-4">✍️ Khu vực tác giả</h2>

    <div class="feature-column">
      <!-- 📝 Viết bài mới -->
      <a href="${pageContext.request.contextPath}/news?action=create" class="feature-item">
        <div class="icon">📝</div>
        <div class="content">
          <h5>Viết bài mới</h5>
          <p>Tạo và xuất bản bài viết để chia sẻ đến độc giả.</p>
        </div>
        <button class="btn btn-success">Bắt đầu viết</button>
      </a>

      <!-- 📂 Bài viết của tôi -->
      <a href="${pageContext.request.contextPath}/news" class="feature-item">
        <div class="icon">📂</div>
        <div class="content">
          <h5>Bài viết của tôi</h5>
          <p>Xem, chỉnh sửa và quản lý toàn bộ bài viết bạn đã đăng.</p>
        </div>
        <button class="btn btn-outline-success">Xem bài viết</button>
      </a>

      <!-- 👥 Người theo dõi -->
      <a href="${pageContext.request.contextPath}/newsletter?action=followers&authorId=${sessionScope.user.id}" class="feature-item">
        <div class="icon">👥</div>
        <div class="content">
          <h5>Người theo dõi</h5>
          <p>Xem danh sách những người đăng ký nhận tin từ bạn.</p>
        </div>
        <button class="btn btn-outline-success">Xem người theo dõi</button>
      </a>

      <!-- 📢 Gửi thông báo -->
      <a href="${pageContext.request.contextPath}/newsletter?action=notify&authorId=${sessionScope.user.id}" class="feature-item">
        <div class="icon">📢</div>
        <div class="content">
          <h5>Gửi thông báo</h5>
          <p>Thông báo đến tất cả người theo dõi khi bạn có bài viết mới.</p>
        </div>
        <button class="btn btn-warning text-dark fw-semibold">Gửi thông báo</button>
      </a>
    </div>
  </section>

  <!-- 🚪 Logout -->
  <div class="text-center mt-5">
    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger px-4 py-2 rounded-pill">
      🚪 Đăng xuất
    </a>
  </div>
</div>

<!-- ✅ STYLE DẠNG CỘT -->
<style>
body {
  background-color: #f8f9fa;
  font-family: "Segoe UI", Arial, sans-serif;
}

.role-badge {
  background: #006633;
  color: #fff;
  padding: 5px 14px;
  border-radius: 20px;
  font-size: 0.9rem;
}

.divider {
  width: 80px;
  height: 4px;
  background: #006633;
  margin: 0 auto 30px auto;
  border-radius: 2px;
}

.section-title {
  font-size: 1.6rem;
  font-weight: 700;
  color: #006633;
  border-left: 6px solid #006633;
  padding-left: 12px;
  margin-bottom: 1.5rem;
}

.feature-column {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  border-radius: 14px;
  padding: 1.5rem 1.5rem;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
  text-decoration: none;
  color: #212529;
  transition: all 0.25s ease;
}
.feature-item:hover {
  transform: translateX(6px);
  box-shadow: 0 5px 18px rgba(0, 0, 0, 0.12);
  background: #e8f5ee;
}

.feature-item .icon {
  font-size: 1.9rem;
  color: #00994d;
  margin-right: 1.2rem;
  flex-shrink: 0;
}

.feature-item .content {
  flex: 1;
  text-align: left;
}
.feature-item .content h5 {
  font-weight: 600;
  color: #006633;
  margin-bottom: 0.3rem;
}
.feature-item .content p {
  color: #6c757d;
  font-size: 0.95rem;
  margin: 0;
}

.feature-item .btn {
  min-width: 150px;
  border-radius: 8px;
  font-weight: 600;
}

.btn-success {
  background-color: #006633;
  border-color: #006633;
}
.btn-success:hover {
  background-color: #00994d;
  border-color: #00994d;
}
.btn-outline-success {
  border-color: #006633;
  color: #006633;
}
.btn-outline-success:hover {
  background-color: #006633;
  color: #fff;
}

.btn-warning {
  background-color: #ffc107;
  border: none;
}

.btn-danger {
  background-color: #c82333;
  border: none;
}
.btn-danger:hover {
  background-color: #a71d2a;
}
</style>
