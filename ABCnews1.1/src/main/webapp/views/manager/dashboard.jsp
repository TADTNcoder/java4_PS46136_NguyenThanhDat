<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="admin-dashboard container py-5">
  <h1 class="text-center mb-4 text-primary fw-bold">Xin chào, ${sessionScope.user.fullname}</h1>
  <p class="text-center mb-5">
    Bạn đang đăng nhập với quyền:
    <span class="role-badge">Quản trị viên</span>
  </p>

  <!-- ======================= QUẢN LÝ BÀI VIẾT ======================= -->
  <section class="dashboard-section">
    <h2>Quản lý bài viết (News Management)</h2>
    <div class="feature-column">
      <a href="${pageContext.request.contextPath}/news?action=create" class="feature-item">
        <div class="content">
          <h5>Tạo bài viết mới</h5>
          <p>Thêm bài viết mới vào hệ thống tin tức.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/news" class="feature-item">
        <div class="content">
          <h5>Chỉnh sửa nội dung</h5>
          <p>Sửa nội dung bài viết đã đăng.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/news" class="feature-item">
        <div class="content">
          <h5>Xóa bài viết</h5>
          <p>Xóa bỏ các bài viết không cần thiết.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/news" class="feature-item">
        <div class="content">
          <h5>Danh sách bài viết</h5>
          <p>Xem toàn bộ bài viết hiện có.</p>
        </div>
      </a>
    </div>
  </section>

  <!-- ======================= QUẢN LÝ DANH MỤC ======================= -->
  <section class="dashboard-section">
    <h2>Quản lý danh mục (Category Management)</h2>
    <div class="feature-column">
      <a href="${pageContext.request.contextPath}/categories?action=create" class="feature-item">
        <div class="content">
          <h5>Tạo danh mục</h5>
          <p>Thêm chuyên mục tin tức mới.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/categories" class="feature-item">
        <div class="content">
          <h5>Cập nhật tên</h5>
          <p>Chỉnh sửa tên danh mục hiện có.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/categories" class="feature-item">
        <div class="content">
          <h5>Xóa danh mục</h5>
          <p>Xóa bỏ danh mục không cần thiết.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/categories" class="feature-item">
        <div class="content">
          <h5>Danh sách danh mục</h5>
          <p>Xem toàn bộ danh mục hiện có.</p>
        </div>
      </a>
    </div>
  </section>

  <!-- ======================= QUẢN LÝ NGƯỜI DÙNG ======================= -->
  <section class="dashboard-section">
    <h2>Quản lý người dùng (User Management)</h2>
    <div class="feature-column">
      <a href="${pageContext.request.contextPath}/users?action=create" class="feature-item">
        <div class="content">
          <h5>Thêm người dùng</h5>
          <p>Tạo tài khoản mới (Admin, Editor, Subscriber).</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/users" class="feature-item">
        <div class="content">
          <h5>Cập nhật thông tin</h5>
          <p>Sửa thông tin người dùng.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/users" class="feature-item">
        <div class="content">
          <h5>Đặt lại mật khẩu</h5>
          <p>Đặt lại mật khẩu cho người dùng.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/users" class="feature-item">
        <div class="content">
          <h5>Xóa tài khoản</h5>
          <p>Xóa người dùng khỏi hệ thống.</p>
        </div>
      </a>
    </div>
  </section>

  <!-- ======================= QUẢN LÝ ĐĂNG KÝ NHẬN TIN ======================= -->
  <section class="dashboard-section">
    <h2>Quản lý đăng ký nhận tin (Newsletter Management)</h2>
    <div class="feature-column">
      <a href="${pageContext.request.contextPath}/newsletter" class="feature-item">
        <div class="content">
          <h5>Danh sách đăng ký</h5>
          <p>Xem toàn bộ email đã đăng ký.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/newsletter" class="feature-item">
        <div class="content">
          <h5>Lọc theo tác giả</h5>
          <p>Lọc danh sách đăng ký theo người viết.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/newsletter?action=create" class="feature-item">
        <div class="content">
          <h5>Thêm email</h5>
          <p>Thêm đăng ký thủ công vào danh sách.</p>
        </div>
      </a>
      <a href="${pageContext.request.contextPath}/newsletter" class="feature-item">
        <div class="content">
          <h5>Cập nhật trạng thái</h5>
          <p>Kích hoạt hoặc hủy đăng ký email.</p>
        </div>
      </a>
    </div>
  </section>

  <!-- Logout -->
  <div class="text-center mt-5">
    <a href="${pageContext.request.contextPath}/logout" class="btn btn-logout px-4 py-2 rounded-pill">Đăng xuất</a>
  </div>
</div>

<!-- ✅ CSS hiện đại, màu xanh dương -->
<style>
body {
  background-color: #f4f8ff;
  font-family: "Segoe UI", Arial, sans-serif;
}

.role-badge {
  background: #1a73e8;
  color: #fff;
  padding: 5px 14px;
  border-radius: 20px;
  font-size: 0.95rem;
}

.dashboard-section {
  margin-bottom: 3rem;
}

.dashboard-section h2 {
  font-size: 1.6rem;
  font-weight: bold;
  color: #1a73e8;
  border-left: 6px solid #1a73e8;
  padding-left: 10px;
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
  background: #ffffff;
  border-radius: 12px;
  padding: 1.2rem 1.5rem;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
  text-decoration: none;
  color: #212529;
  transition: all 0.25s ease;
}

.feature-item:hover {
  transform: translateX(6px);
  box-shadow: 0 5px 18px rgba(0, 0, 0, 0.12);
  background: #e8f1fe;
}

.feature-item .content h5 {
  font-weight: 600;
  margin-bottom: 0.2rem;
  color: #1a73e8;
}

.feature-item .content p {
  margin: 0;
  color: #5f6368;
  font-size: 0.95rem;
}

.btn-logout {
  background-color: #d93025;
  border: none;
  color: white;
  font-weight: 500;
  transition: all 0.25s ease;
}

.btn-logout:hover {
  background-color: #b1251c;
}
</style>
