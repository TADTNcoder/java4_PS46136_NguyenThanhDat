<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="container mt-5 pt-4">

  <!-- 🔹 CATEGORY BAR -->
  <c:if test="${not empty categories}">
    <div class="mb-4 text-center category-bar">
      <a href="${ctx}/dashboard"
         class="btn btn-sm me-2 ${empty activeCategoryId ? 'btn-success text-white' : 'btn-outline-success'}">
         📚 Tất cả
      </a>
      <c:forEach var="cat" items="${categories}">
        <a href="${ctx}/home?categoryId=${cat.id}"
           class="btn btn-sm me-2 ${activeCategoryId == cat.id ? 'btn-success text-white' : 'btn-outline-success'}">
           ${cat.name}
        </a>
      </c:forEach>
    </div>
  </c:if>

  <div class="row g-4">

    <!-- 📰 CỘT TRÁI: BÀI VIẾT MỚI -->
    <div class="col-lg-3 d-none d-lg-block">
      <div class="p-3 bg-light rounded shadow-sm mb-3">
        <h5 class="fw-bold text-success mb-3">🆕 Bài viết mới nhất</h5>
        <c:forEach var="news" items="${latestNews}" varStatus="st">
          <c:if test="${st.index < 5}">
            <div class="mb-3 border-bottom pb-2">
              <a href="${ctx}/news?action=view&id=${news.id}" class="side-link">${news.title}</a>
            </div>
          </c:if>
        </c:forEach>
      </div>
    </div>

    <!-- 🧩 CỘT GIỮA: BÀI NỔI BẬT -->
    <div class="col-lg-6">
      <c:choose>
        <c:when test="${not empty latestNews}">
          <c:set var="highlight" value="${latestNews[0]}" />

          <!-- 🔸 Bài nổi bật -->
          <div class="main-news card border-0 shadow-sm mb-4">
            <img src="${ctx}${not empty highlight.image ? highlight.image : '/assets/images/default.jpg'}"
                 class="card-img-top" alt="${highlight.title}" />
            <div class="card-body">
              <h3 class="fw-bold text-dark mb-2">${highlight.title}</h3>
              <p class="text-muted small mb-3">
                🗓️ <fmt:formatDate value="${highlight.postedDate}" pattern="dd/MM/yyyy" /> 
                &nbsp;|&nbsp; 👁️ ${highlight.viewCount} lượt xem
              </p>
              <p class="card-text mb-3">
                <c:out value="${fn:length(highlight.content) > 200 ? fn:substring(highlight.content,0,200).concat('...') : highlight.content}" />
              </p>
              <a href="${ctx}/news?action=view&id=${highlight.id}" class="btn btn-success w-100 rounded-pill">
                📖 Đọc thêm
              </a>
            </div>
          </div>

          <!-- 🔻 Các bài khác -->
          <div class="row g-3">
            <c:forEach var="news" items="${latestNews}" varStatus="st">
              <c:if test="${st.index > 0 && st.index < 7}">
                <div class="col-md-6">
                  <div class="news-card card h-100 border-0 shadow-sm">
                    <img src="${ctx}${not empty news.image ? news.image : '/assets/images/default.jpg'}"
                         class="card-img-top" alt="${news.title}" />
                    <div class="card-body">
                      <h6 class="fw-semibold mb-2">${news.title}</h6>
                      <p class="text-muted small mb-2">
                        🗓️ <fmt:formatDate value="${news.postedDate}" pattern="dd/MM/yyyy" />
                      </p>
                      <a href="${ctx}/news?action=view&id=${news.id}" class="text-success small fw-semibold">Đọc thêm →</a>
                    </div>
                  </div>
                </div>
              </c:if>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <p class="text-center text-muted fs-5">📭 Không có bài viết nào trong mục này.</p>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- 🟢 CỘT PHẢI: 3 BOX -->
    <div class="col-lg-3 d-none d-lg-block">
      <!-- 🔥 Xem nhiều nhất -->
      <div class="p-3 mb-3 rounded shadow-sm" style="background-color: #ffcc00;">
        <h6 class="fw-bold mb-3 text-dark">5 bản tin được xem nhiều</h6>
        <c:forEach var="news" items="${mostViewed}" varStatus="st">
          <c:if test="${st.index < 5}">
            <div class="mb-2 border-bottom pb-1">
              <a href="${ctx}/news?action=view&id=${news.id}" class="text-dark small fw-semibold">${news.title}</a>
            </div>
          </c:if>
        </c:forEach>
      </div>

      <!-- 🕓 Mới nhất -->
      <div class="p-3 mb-3 bg-secondary text-white rounded shadow-sm">
        <h6 class="fw-bold mb-3">5 bản tin mới nhất</h6>
        <c:forEach var="news" items="${latestNews}" varStatus="st">
          <c:if test="${st.index < 5}">
            <div class="mb-2 border-bottom border-light pb-1">
              <a href="${ctx}/news?action=view&id=${news.id}" class="text-white small fw-semibold">${news.title}</a>
            </div>
          </c:if>
        </c:forEach>
      </div>

      <!-- 👀 Đã xem -->
      <div class="p-3 bg-success text-white rounded shadow-sm">
        <h6 class="fw-bold mb-3">5 bản tin bạn đã xem</h6>
        <c:forEach var="news" items="${recentlyViewed}" varStatus="st">
          <c:if test="${st.index < 5}">
            <div class="mb-2 border-bottom border-light pb-1">
              <a href="${ctx}/news?action=view&id=${news.id}" class="text-white small fw-semibold">${news.title}</a>
            </div>
          </c:if>
        </c:forEach>
      </div>

      <!-- ✉️ Newsletter -->
      <!-- 📧 Email -->
    <div class="mb-3">
      <label for="email" class="form-label fw-bold">📧 Email:</label>
      <input type="email" id="email" class="form-control" name="email"
             value="${subscriber.email}" 
             <c:if test="${not empty subscriber}">readonly</c:if> required>
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
    <div class="text-center mt-4">
      <button type="submit" class="btn btn-primary px-4">💾 Lưu</button>
      <a href="${pageContext.request.contextPath}/newsletter" class="btn btn-secondary px-4">⬅️ Quay lại</a>
    </div>
    </div>
  </div>

  <!-- 📄 PHÂN TRANG -->
  <c:if test="${totalPages > 1}">
    <nav aria-label="Page navigation" class="mt-5">
      <ul class="pagination justify-content-center">
        <c:if test="${currentPage > 1}">
          <li class="page-item"><a class="page-link"
              href="?page=${currentPage - 1}${not empty activeCategoryId ? '&categoryId=' + activeCategoryId : ''}">« Trước</a></li>
        </c:if>

        <c:forEach begin="1" end="${totalPages}" var="p">
          <li class="page-item ${p == currentPage ? 'active' : ''}">
            <a class="page-link"
               href="?page=${p}${not empty activeCategoryId ? '&categoryId=' + activeCategoryId : ''}">${p}</a>
          </li>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
          <li class="page-item"><a class="page-link"
              href="?page=${currentPage + 1}${not empty activeCategoryId ? '&categoryId=' + activeCategoryId : ''}">Tiếp »</a></li>
        </c:if>
      </ul>
    </nav>
  </c:if>
</div>

<!-- 🌿 CSS -->
<style>
.category-bar .btn { border-radius: 20px; font-weight: 500; }

.main-news img {
  height: 350px;
  object-fit: cover;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}
.news-card img {
  height: 180px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}
.news-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 15px rgba(0,0,0,0.15);
  transition: all 0.3s ease;
}
.side-link {
  text-decoration: none;
  color: #212529;
  transition: color 0.2s ease;
}
.side-link:hover { color: #00994d; }
.pagination .page-link {
  border-radius: 8px;
  margin: 0 3px;
  color: #006633;
}
.pagination .page-item.active .page-link {
  background-color: #006633;
  border-color: #006633;
  color: #fff;
}
.newsletter-box input { font-size: 0.9rem; }
.newsletter-box button { font-weight: 600; }
</style>
