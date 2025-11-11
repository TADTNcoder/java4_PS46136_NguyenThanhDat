<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="servletPath" value="${pageContext.request.servletPath}" />

<!-- ✅ Xác định role user -->
<c:choose>
	<c:when test="${not empty sessionScope.user}">
		<c:set var="role" value="${sessionScope.user.role}" />
	</c:when>
	<c:otherwise>
		<c:set var="role" value="2" />
	</c:otherwise>
</c:choose>

<c:set var="isAdmin" value="${role eq '0'}" />
<c:set var="isAuthor" value="${role eq '1'}" />
<c:set var="isReader" value="${role eq '2'}" />
<c:set var="isLoggedIn" value="${not empty sessionScope.user}" />

<!-- ✅ Sidebar (nếu đăng nhập) -->
<c:if test="${isLoggedIn}">
	<jsp:include page="/views/partials/sidebar.jsp" />
</c:if>

<!-- ✅ Navbar kiểu Dân Trí -->
<nav class="navbar navbar-expand-lg fixed-top custom-navbar">
	<div class="container-fluid">

		<!-- ☰ Nút mở sidebar -->
		<c:if test="${isLoggedIn}">
			<button class="btn sidebar-toggle me-2" type="button"
				id="toggleSidebar">☰</button>
		</c:if>

		<!-- 📰 Logo -->
		<a class="navbar-brand"
			href="${pageContext.request.contextPath}${isLoggedIn ? '/dashboard' : '/'}">
			<span class="brand-icon">📰</span> ABCNEWS
		</a>

		<!-- Nút toggle cho mobile -->
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<!-- Menu chính -->
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ms-auto align-items-lg-center">

				<!-- 👑 ADMIN -->
				<c:if test="${isLoggedIn and isAdmin}">
					<li class="nav-item dropdown"><a
						class="nav-link ${fn:startsWith(servletPath, '/news') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/news">📰 Bài viết</a> <a
						class="nav-link dropdown-toggle dropdown-caret" href="#"
						id="newsDropdown" role="button" data-bs-toggle="dropdown"></a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/news?action=create">➕
									Tạo mới</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/news">📑 Danh sách
									bài viết</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/news?filter=category">📂
									Lọc theo danh mục</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/news?filter=author">👤
									Lọc theo tác giả</a></li>
						</ul></li>

					<li class="nav-item dropdown"><a
						class="nav-link ${fn:startsWith(servletPath, '/categories') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/categories">Danh mục</a>
						<a class="nav-link dropdown-toggle dropdown-caret" href="#"
						id="categoryDropdown" role="button" data-bs-toggle="dropdown"></a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/categories?action=create">➕
									Tạo danh mục</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/categories">Danh
									sách danh mục</a></li>
						</ul></li>

					<li class="nav-item dropdown"><a
						class="nav-link ${fn:startsWith(servletPath, '/users') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/users">👤 Người dùng</a>
						<a class="nav-link dropdown-toggle dropdown-caret" href="#"
						id="userDropdown" role="button" data-bs-toggle="dropdown"></a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/users?action=create">➕
									Thêm người dùng</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/users">📋 Danh sách
									người dùng</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/users?action=search">🔍
									Tìm kiếm</a></li>
						</ul></li>

					<li class="nav-item dropdown"><a
						class="nav-link ${fn:startsWith(servletPath, '/newsletter') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/newsletter">📬 Theo
							dõi</a> <a class="nav-link dropdown-toggle dropdown-caret" href="#"
						id="newsletterDropdown" role="button" data-bs-toggle="dropdown"></a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/newsletter">📧 Danh
									sách đăng ký</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/newsletter?action=create">➕
									Thêm email</a></li>
						</ul></li>

					<li class="nav-item"><a
						class="nav-link ${fn:startsWith(servletPath, '/admin/stats') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/admin/stats">📊 Thống
							kê</a></li>
				</c:if>

				<!-- ✍️ TÁC GIẢ -->
				<c:if test="${isLoggedIn and isAuthor}">
					<li class="nav-item"><a
						class="nav-link ${fn:startsWith(servletPath, '/news') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/news">📰 Bài viết</a></li>
					<li class="nav-item"><a
						class="nav-link ${fn:startsWith(servletPath, '/newsletter') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/newsletter?action=followers&authorId=${sessionScope.user.id}">📬
							Người theo dõi</a></li>
				</c:if>

				<!-- 📚 ĐỌC GIẢ -->
				<form class="d-flex me-3"
					action="${pageContext.request.contextPath}/search" method="get">
					<input class="form-control me-2" type="search" name="keyword"
						placeholder="🔍 Tìm kiếm..." required>
					<button class="btn btn-success" type="submit">Tìm</button>
				</form>

				<c:if test="${sessionScope.role eq '2'}">
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-success fw-semibold" href="#"
						id="categoryDropdown" role="button" data-bs-toggle="dropdown"
						aria-expanded="false"> 🗂️ Chuyên mục </a>
						<ul class="dropdown-menu border-0 shadow-sm"
							aria-labelledby="categoryDropdown">
							<c:forEach var="cat" items="${categories}">
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/news?categoryId=${cat.id}">
										${cat.name} </a></li>
							</c:forEach>
							<c:if test="${empty categories}">
								<li><span class="dropdown-item text-muted">Không có
										chuyên mục</span></li>
							</c:if>
						</ul></li>
				</c:if>

				<!-- 📬 Theo dõi -->
				<c:if test="${isLoggedIn and isReader}">
					<li class="nav-item"><a
						class="nav-link ${fn:startsWith(servletPath, '/newsletter') ? 'active' : ''}"
						href="${pageContext.request.contextPath}/newsletter">📬 Theo
							dõi</a></li>
				</c:if>

				<!-- 👤 Tài khoản -->
				<c:if test="${isLoggedIn}">
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle d-flex align-items-center"
						href="#" id="accountDropdown" role="button"
						data-bs-toggle="dropdown" aria-expanded="false"> 👤
							${sessionScope.user.fullname} </a>
						<ul class="dropdown-menu dropdown-menu-end">
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/users">👤 Người
									dùng</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item text-danger"
								href="${pageContext.request.contextPath}/logout">🔐 Đăng
									xuất</a></li>
						</ul></li>
				</c:if>




				<!-- 🔐 Chưa đăng nhập -->
				<c:if test="${not isLoggedIn}">
					<li class="nav-item"><a
						class="nav-link login-btn ${servletPath == '/login' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/login">🔐 Đăng nhập</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</nav>

<!-- ✅ CSS xanh kiểu Dân Trí -->
<style>
.custom-navbar {
	background-color: #ffffff;
	box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
	padding: 0.6rem 1.2rem;
	border-bottom: 2px solid #006633;
}

.navbar-brand {
	font-size: 1.7rem;
	font-weight: 700;
	color: #006633 !important;
	text-transform: uppercase;
	letter-spacing: 0.5px;
}

.navbar-brand:hover {
	color: #00994d !important;
	text-shadow: 0 0 5px rgba(0, 153, 77, 0.3);
}

.navbar-nav .nav-link {
	color: #333 !important;
	font-weight: 500;
	font-size: 1rem;
	margin: 0 6px;
	transition: all 0.3s ease-in-out;
}

.navbar-nav .nav-link:hover, .navbar-nav .nav-link.active {
	color: #00994d !important;
	font-weight: 600;
}

.navbar-nav .dropdown-menu {
	background-color: #ffffff;
	border: 1px solid #e0e0e0;
	box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
}

.navbar-nav .dropdown-item {
	color: #333;
	font-size: 0.95rem;
}

.navbar-nav .dropdown-item:hover {
	background-color: #00994d;
	color: #fff;
}

.btn-success {
	background-color: #006633;
	border-color: #006633;
}

.btn-success:hover {
	background-color: #00994d;
	border-color: #00994d;
}

.login-btn {
	border: 1px solid #006633;
	border-radius: 6px;
	padding: 6px 14px;
	font-weight: 500;
	color: #006633 !important;
	transition: all 0.3s ease-in-out;
}

.login-btn:hover, .login-btn.active {
	background: #006633;
	color: #fff !important;
}

.sidebar-toggle {
	font-size: 1.5rem;
	color: #006633;
	background: none;
	border: none;
}

.sidebar-toggle:hover {
	color: #00994d;
}

.navbar .dropdown-menu {
  min-width: 200px;
  border-radius: 10px;
  padding: 0.5rem;
}
.navbar .dropdown-item {
  border-radius: 6px;
  padding: 8px 12px;
  transition: background-color 0.2s;
}
.navbar .dropdown-item:hover {
  background-color: #e8f5ee;
  color: #006633;
  font-weight: 500;
}
.navbar .dropdown-toggle::after {
  margin-left: 0.4rem;
}

</style>

<!-- ✅ JS Sidebar -->
<script>
	document.addEventListener("DOMContentLoaded", function() {
		const toggleSidebar = document.getElementById("toggleSidebar");
		const sidebar = document.getElementById("sidebar");
		if (toggleSidebar && sidebar) {
			toggleSidebar.addEventListener("click", function() {
				sidebar.classList.toggle("show");
			});
			document.addEventListener("click", function(e) {
				if (!sidebar.contains(e.target)
						&& !toggleSidebar.contains(e.target)) {
					sidebar.classList.remove("show");
				}
			});
		}
	});
</script>
