<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="servletPath" value="${pageContext.request.servletPath}" />
<c:set var="role" value="${sessionScope.role}" />
<c:set var="isAdmin" value="${role eq '0'}" />
<c:set var="isAuthor" value="${role eq '1'}" />
<c:set var="isReader" value="${role eq '2'}" />
<c:set var="isLoggedIn" value="${not empty sessionScope.user}" />

<!-- 📌 Sidebar: chỉ hiển thị khi đăng nhập -->
<c:if test="${isLoggedIn}">
<div id="sidebar" class="sidebar">
  <div class="sidebar-header">
    <h3>📊 Bảng điều khiển</h3>
    <p class="role-label">
      <c:choose>
        <c:when test="${isAdmin}">👑 Quản trị viên</c:when>
        <c:when test="${isAuthor}">✍️ Tác giả</c:when>
        <c:when test="${isReader}">📚 Độc giả</c:when>
      </c:choose>
    </p>
  </div>

  <ul class="sidebar-menu">

    <!-- 👑 ADMIN -->
    <c:if test="${isAdmin}">
      <li class="${fn:startsWith(servletPath, '/dashboard') ? 'active' : ''}">
        <a href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-speedometer2"></i> Tổng quan</a>
      </li>

      <li class="sidebar-group ${fn:startsWith(servletPath, '/news') ? 'open' : ''}">
        <a href="#"><i class="bi bi-newspaper"></i> Bài viết</a>
        <ul class="submenu">
          <li><a href="${pageContext.request.contextPath}/news?action=create">➕ Tạo mới</a></li>
          <li><a href="${pageContext.request.contextPath}/news">📑 Danh sách</a></li>
          <li><a href="${pageContext.request.contextPath}/news?filter=category">📂 Theo danh mục</a></li>
          <li><a href="${pageContext.request.contextPath}/news?filter=author">👤 Theo tác giả</a></li>
        </ul>
      </li>

      <li class="sidebar-group ${fn:startsWith(servletPath, '/categories') ? 'open' : ''}">
        <a href="#"><i class="bi bi-folder2-open"></i> Danh mục</a>
        <ul class="submenu">
          <li><a href="${pageContext.request.contextPath}/categories?action=create">➕ Thêm mới</a></li>
          <li><a href="${pageContext.request.contextPath}/categories">📜 Danh sách</a></li>
        </ul>
      </li>

      <li class="sidebar-group ${fn:startsWith(servletPath, '/users') ? 'open' : ''}">
        <a href="#"><i class="bi bi-people"></i> Người dùng</a>
        <ul class="submenu">
          <li><a href="${pageContext.request.contextPath}/users?action=create">➕ Thêm người dùng</a></li>
          <li><a href="${pageContext.request.contextPath}/users">📋 Danh sách</a></li>
        </ul>
      </li>

      <li class="${fn:startsWith(servletPath, '/newsletter') ? 'active' : ''}">
        <a href="${pageContext.request.contextPath}/newsletter"><i class="bi bi-envelope"></i> 📬 Theo dõi</a>
      </li>

      <li class="${fn:startsWith(servletPath, '/admin/stats') ? 'active' : ''}">
        <a href="${pageContext.request.contextPath}/admin/stats"><i class="bi bi-bar-chart-line"></i> 📊 Thống kê</a>
      </li>
    </c:if>

    <!-- ✍️ AUTHOR -->
    <c:if test="${isAuthor}">
      <li><a href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-speedometer2"></i> Tổng quan</a></li>
      <li><a href="${pageContext.request.contextPath}/news?action=create"><i class="bi bi-pencil-square"></i> ✍️ Viết bài mới</a></li>
      <li><a href="${pageContext.request.contextPath}/news"><i class="bi bi-folder"></i> 📂 Bài viết của tôi</a></li>
      <li><a href="${pageContext.request.contextPath}/newsletter?action=followers&authorId=${sessionScope.user.id}"><i class="bi bi-people"></i> 👥 Người theo dõi</a></li>
    </c:if>

    <!-- 📚 READER -->
    <c:if test="${isReader}">
      <li><a href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-house"></i> Trang chủ</a></li>
      <li><a href="${pageContext.request.contextPath}/news"><i class="bi bi-newspaper"></i> 📚 Tin tức mới</a></li>
      <li><a href="${pageContext.request.contextPath}/newsletter"><i class="bi bi-envelope-paper"></i> 📬 Theo dõi tác giả</a></li>
    </c:if>

    <!-- 🚪 Logout -->
    <li><a href="${pageContext.request.contextPath}/logout" class="logout"><i class="bi bi-box-arrow-right"></i> Đăng xuất</a></li>
  </ul>
</div>
</c:if>

<!-- ✅ Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet" />

<!-- ✅ Sidebar JS -->
<script>
document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll("#sidebar .sidebar-group > a").forEach(link => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      document.querySelectorAll("#sidebar .sidebar-group").forEach(group => {
        if (group !== this.parentElement) group.classList.remove("open");
      });
      this.parentElement.classList.toggle("open");
    });
  });
});
</script>

<!-- ✅ CSS xanh kiểu Dân Trí -->
<style>
#sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 250px;
  background: #006633; /* Xanh Dân Trí */
  padding-top: 70px;
  color: #fff;
  overflow-y: auto;
  box-shadow: 3px 0 10px rgba(0,0,0,0.25);
  z-index: 1051;
  transform: translateX(-100%);
  transition: transform 0.3s ease-in-out;
}

/* ✅ Khi mở */
#sidebar.show { transform: translateX(0); }

.sidebar-header {
  background: #005229;
  padding: 1.5rem 1rem;
  text-align: center;
  border-bottom: 2px solid #00994d;
}
.sidebar-header h3 {
  font-size: 1.4rem;
  font-weight: 700;
  color: #ffffff;
}
.role-label {
  font-size: 0.9rem;
  color: #d4edda;
  margin-top: 5px;
}

.sidebar-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-menu li a {
  display: block;
  padding: 12px 20px;
  color: #e9ecef;
  text-decoration: none;
  transition: all 0.3s ease;
}

.sidebar-menu li a i {
  margin-right: 10px;
}

.sidebar-menu li a:hover,
.sidebar-menu li.active > a {
  background: #00994d;
  color: #fff;
  font-weight: 600;
}

.sidebar-group > a:after {
  content: "▼";
  float: right;
  font-size: 0.8rem;
  transition: transform 0.3s;
}

.sidebar-group.open > a:after {
  transform: rotate(180deg);
}

.submenu {
  display: none;
  background: #005229;
}
.sidebar-group.open .submenu {
  display: block;
}
.submenu li a {
  padding-left: 40px;
  font-size: 0.95rem;
}
.submenu li a:hover {
  background: #00994d;
  color: #fff;
}

.logout {
  background: #dc3545 !important;
  color: #fff !important;
  text-align: center;
  margin: 20px;
  border-radius: 6px;
}
.logout:hover { background: #bb2d3b !important; }

/* 📱 Responsive */
@media (max-width: 991px) {
  #sidebar { width: 100%; height: auto; position: fixed; }
}
</style>
