<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-5">
	<div class="row">
		<div class="col-lg-8 mx-auto">

			<!-- 📝 Tiêu đề -->
			<h1 class="mb-3">${news.title}</h1>

			<!-- ✍️ Thông tin tác giả và ngày đăng -->
			<p class="text-muted">
				✍️ ${news.author} &nbsp; | &nbsp; 🗓️
				<fmt:formatDate value="${news.postedDate}" pattern="dd/MM/yyyy" />
				&nbsp; | &nbsp; 👁️ ${news.viewCount} lượt xem
			</p>


			<!-- ⭐ Theo dõi / Hủy theo dõi tác giả (nếu đã đăng nhập) -->
			<c:if test="${not empty sessionScope.user}">
				<form action="${pageContext.request.contextPath}/newsletter"
					method="post" class="d-inline">
					<input type="hidden" name="_csrf" value="${sessionScope._csrf}" />
					<input type="hidden" name="authorId" value="${news.author}" /> <input
						type="hidden" name="email" value="${sessionScope.user.email}" />

					<c:choose>
						<c:when test="${isSubscribed}">
							<input type="hidden" name="action" value="unsubscribe" />
							<button type="submit" class="btn btn-outline-danger btn-sm mb-3">
								🚫 Hủy theo dõi tác giả</button>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="action" value="subscribe" />
							<button type="submit" class="btn btn-outline-success btn-sm mb-3">
								⭐ Theo dõi tác giả</button>
						</c:otherwise>
					</c:choose>
				</form>
			</c:if>

			<!-- 📸 Ảnh minh họa -->
			<c:choose>
				<c:when test="${not empty news.image}">
					<div style="text-align: center; margin: 20px 0;">
						<!-- ✅ Quan trọng: Luôn ghép contextPath + đường dẫn ảnh -->
						<img src="${pageContext.request.contextPath}${news.image}"
							alt="${news.title}" class="img-fluid rounded mb-4 shadow-sm"
							style="max-width: 100%; height: auto;">
						<p>Debug image path: ${news.image}</p>
					</div>
				</c:when>
				<c:otherwise>
					<div style="text-align: center; margin: 20px 0;">
						<img
							src="${pageContext.request.contextPath}/assets/images/default.jpg"
							alt="No image" class="img-fluid rounded mb-4 opacity-75"
							style="max-width: 100%; height: auto;">
					</div>
				</c:otherwise>
			</c:choose>

			<!-- 📰 Nội dung bài viết -->
			<div class="news-content mb-5">
				<c:out value="${news.content}" escapeXml="false" />
			</div>

			<!-- 📌 Tin liên quan -->
			<c:if test="${not empty related}">
				<h3 class="mb-4">📌 Tin liên quan</h3>
				<div class="row">
					<c:forEach var="r" items="${related}">
						<div class="col-md-6 mb-3">
							<div class="card h-100 shadow-sm">
								<div class="card-body d-flex flex-column">
									<h5 class="card-title">${r.title}</h5>
									<p class="text-muted small">
										🗓️
										<fmt:formatDate value="${r.postedDate}" pattern="dd/MM/yyyy" />
									</p>
									<a
										href="${pageContext.request.contextPath}/news?action=view&id=${r.id}"
										class="btn btn-outline-primary btn-sm mt-auto"> 📖 Xem chi
										tiết </a>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:if>

			<div class="text-center mt-4">
				<a href="${pageContext.request.contextPath}/news"
					class="btn btn-outline-dark px-4"> ⬅️ Quay lại danh sách </a>
			</div>

		</div>
	</div>
</div>

<style>
.news-content {
	font-size: 1.08rem;
	line-height: 1.7;
	color: #343a40;
	white-space: pre-line; /* ✅ giữ xuống dòng từ nội dung */
}

.btn-sm {
	font-weight: 500;
	letter-spacing: 0.3px;
	transition: 0.2s;
}

.btn-sm:hover {
	transform: translateY(-1px);
}
</style>
