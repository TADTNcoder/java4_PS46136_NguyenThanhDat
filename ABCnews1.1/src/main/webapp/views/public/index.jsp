<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="activeCategoryId" value="${requestScope.activeCategoryId}" />

<div class="container mt-5 pt-4">
  <h1 class="mb-4 text-center text-success fw-bold">📰 Tin tức mới nhất</h1>

  <!-- 📂 Lọc chuyên mục -->
  <c:if test="${not empty categories}">
    <div class="mb-5 text-center category-bar">
      <a href="${ctx}/home"
         class="btn btn-sm me-2 ${empty activeCategoryId ? 'btn-success text-white' : 'btn-outline-success'}">
        📚 Tất cả </a>
      <c:forEach var="cat" items="${categories}">
        <a href="${ctx}/home?categoryId=${cat.id}"
           class="btn btn-sm me-2 ${activeCategoryId == cat.id ? 'btn-success text-white' : 'btn-outline-success'}">
           ${cat.name} </a>
      </c:forEach>
    </div>
  </c:if>

  <!-- 📰 Bố cục 3 cột kiểu Dân Trí -->
  <div class="row g-4">

    <!-- 🔹 Cột trái: Tin phụ -->
    <div class="col-lg-3 d-none d-lg-block">
      <c:forEach var="news" items="${latestNews}" varStatus="st">
        <c:if test="${st.index < 3}">
          <div class="side-news mb-4">
            <c:choose>
              <c:when test="${not empty news.image}">
                <img src="${ctx}${news.image}" alt="${news.title}" class="img-fluid rounded mb-2" />
              </c:when>
              <c:otherwise>
                <img src="${ctx}/assets/images/default.jpg" alt="${news.title}" class="img-fluid rounded mb-2" />
              </c:otherwise>
            </c:choose>
            <a href="${ctx}/news?action=view&id=${news.id}" class="fw-semibold text-dark side-link">${news.title}</a>
          </div>
        </c:if>
      </c:forEach>
    </div>

    <!-- 🔸 Cột giữa: Bài nổi bật -->
    <div class="col-lg-6">
      <c:choose>
        <c:when test="${not empty latestNews}">
          <c:set var="highlight" value="${latestNews[0]}" />
          <div class="main-news card border-0 shadow-sm mb-4">
            <c:choose>
              <c:when test="${not empty highlight.image}">
                <img src="${ctx}${highlight.image}" class="card-img-top" alt="${highlight.title}" />
              </c:when>
              <c:otherwise>
                <img src="${ctx}/assets/images/default.jpg" class="card-img-top" alt="${highlight.title}" />
              </c:otherwise>
            </c:choose>
            <div class="card-body">
              <h3 class="fw-bold text-dark mb-2">${highlight.title}</h3>
              <p class="text-muted small mb-3">
                🗓️ <fmt:formatDate value="${highlight.postedDate}" pattern="dd/MM/yyyy" /> 
                &nbsp;|&nbsp; 👁️ ${highlight.viewCount} lượt xem
              </p>
              <p class="card-text mb-3">
                <c:choose>
                  <c:when test="${not empty highlight.content and fn:length(highlight.content) > 200}">
                    <c:out value="${fn:substring(highlight.content, 0, 200)}..." />
                  </c:when>
                  <c:otherwise>
                    <c:out value="${highlight.content}" />
                  </c:otherwise>
                </c:choose>
              </p>
              <a href="${ctx}/news?action=view&id=${highlight.id}" class="btn btn-success w-100">📖 Đọc thêm</a>
            </div>
          </div>

          <!-- 🔻 Các bài khác -->
          <div class="row g-3">
            <c:forEach var="news" items="${latestNews}" varStatus="st">
              <c:if test="${st.index > 0 && st.index < 7}">
                <div class="col-md-6">
                  <div class="news-card card h-100 border-0 shadow-sm">
                    <c:choose>
                      <c:when test="${not empty news.image}">
                        <img src="${ctx}${news.image}" class="card-img-top" alt="${news.title}" />
                      </c:when>
                      <c:otherwise>
                        <img src="${ctx}/assets/images/default.jpg" class="card-img-top" alt="${news.title}" />
                      </c:otherwise>
                    </c:choose>
                    <div class="card-body">
                      <h6 class="fw-semibold mb-2">${news.title}</h6>
                      <p class="text-muted small mb-2">
                        🗓️ <fmt:formatDate value="${news.postedDate}" pattern="dd/MM/yyyy" />
                      </p>
                      <a href="${ctx}/news?action=view&id=${news.id}" class="text-success small">Đọc thêm →</a>
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

    <!-- 🔹 Cột phải: Tin nổi bật / sidebar -->
    <div class="col-lg-3 d-none d-lg-block">
      <div class="p-3 bg-light rounded shadow-sm">
        <h5 class="fw-bold text-success mb-3">🔥 Đọc nhiều nhất</h5>
        <c:forEach var="news" items="${latestNews}" varStatus="st">
          <c:if test="${st.index >= 3 && st.index < 7}">
            <div class="mb-3 border-bottom pb-2">
              <a href="${ctx}/news?action=view&id=${news.id}" class="fw-semibold text-dark side-link">${news.title}</a>
            </div>
          </c:if>
        </c:forEach>
      </div>
    </div>

  </div>

  <!-- 📄 Phân trang -->
  <c:if test="${totalPages > 1}">
    <nav aria-label="Page navigation" class="mt-5">
      <ul class="pagination justify-content-center">
        <c:if test="${currentPage > 1}">
          <li class="page-item">
            <a class="page-link"
               href="?page=${currentPage - 1}${not empty activeCategoryId ? '&categoryId=' + activeCategoryId : ''}">« Trước</a>
          </li>
        </c:if>

        <c:forEach begin="1" end="${totalPages}" var="p">
          <li class="page-item ${p == currentPage ? 'active' : ''}">
            <a class="page-link"
               href="?page=${p}${not empty activeCategoryId ? '&categoryId=' + activeCategoryId : ''}">${p}</a>
          </li>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
          <li class="page-item">
            <a class="page-link"
               href="?page=${currentPage + 1}${not empty activeCategoryId ? '&categoryId=' + activeCategoryId : ''}">Tiếp »</a>
          </li>
        </c:if>
      </ul>
    </nav>
  </c:if>
</div>

<!-- ✅ CSS layout kiểu Dân Trí -->
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
  box-shadow: 0px 6px 15px rgba(0,0,0,0.15);
  transition: all 0.3s ease;
}

.side-news img {
  height: 100px;
  object-fit: cover;
}

.side-link {
  text-decoration: none;
  color: #212529;
  transition: color 0.2s ease;
}
.side-link:hover {
  color: #00994d;
}

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
</style>
