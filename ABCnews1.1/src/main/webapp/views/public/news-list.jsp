<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container mt-5">
  <h1 class="mb-4 text-center">📰 Tin tức mới nhất</h1>

  <!-- 📂 Bộ lọc chuyên mục -->
  <div class="row mb-4">
    <div class="col-md-4 offset-md-4">
      <form method="get" action="${pageContext.request.contextPath}/news">
        <select name="categoryId" class="form-select" onchange="this.form.submit()">
          <option value="">📁 Tất cả chuyên mục</option>
          <c:forEach var="cat" items="${categories}">
            <option value="${cat.id}" <c:if test="${param.categoryId == cat.id}">selected</c:if>>
              ${cat.name}
            </option>
          </c:forEach>
        </select>
      </form>
    </div>
  </div>

  <!-- 📜 Danh sách tin tức -->
  <c:choose>
    <c:when test="${not empty newsList}">
      <div class="row">
        <c:forEach var="n" items="${newsList}">
          <div class="col-md-4 mb-4">
            <div class="card h-100 shadow-sm">

              <!-- 📸 Ảnh đại diện -->
              <c:choose>
  <c:when test="${not empty n.image}">
    <img src="${pageContext.request.contextPath}${n.image}" 
         class="card-img-top" 
         alt="${n.title}" 
         style="max-height:220px; object-fit:cover;">
  </c:when>
  <c:otherwise>
    <img src="${pageContext.request.contextPath}/assets/images/default.jpg" 
         class="card-img-top" 
         alt="No image" 
         style="max-height:220px; object-fit:cover; opacity:0.7;">
  </c:otherwise>
</c:choose>


              <div class="card-body d-flex flex-column">
                <h5 class="card-title">${n.title}</h5>
                <p class="text-muted small mb-2">
                  ✍️ ${n.author} &nbsp; | &nbsp;
                  🗓️ <fmt:formatDate value="${n.postedDate}" pattern="dd/MM/yyyy"/>
                </p>

                <!-- ✏️ Tóm tắt nội dung -->
                <p class="card-text flex-grow-1">
                  <c:choose>
                    <c:when test="${not empty n.content}">
                      <c:choose>
                        <c:when test="${fn:length(n.content) > 120}">
                          <c:out value="${fn:substring(n.content, 0, 120)}..." />
                        </c:when>
                        <c:otherwise>
                          <c:out value="${n.content}" />
                        </c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <span class="text-muted">Không có nội dung</span>
                    </c:otherwise>
                  </c:choose>
                </p>

                <!-- 📖 Đọc thêm -->
                <a href="${pageContext.request.contextPath}/news?action=view&id=${n.id}" 
                   class="btn btn-outline-primary mt-auto w-100">
                  📖 Đọc thêm
                </a>
              </div>

              <!-- 👁️ View count -->
              <div class="card-footer text-muted small">
                👁️ ${n.viewCount} lượt xem
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <!-- 📄 Phân trang -->
      <c:if test="${total > size}">
        <nav aria-label="Page navigation" class="mt-4">
          <ul class="pagination justify-content-center">
            <c:forEach var="i" begin="1" end="${(total / size) + (total % size > 0 ? 1 : 0)}">
              <li class="page-item ${i == page ? 'active' : ''}">
                <a class="page-link" href="?page=${i}&size=${size}${param.categoryId != null ? '&categoryId=' + param.categoryId : ''}">
                  ${i}
                </a>
              </li>
            </c:forEach>
          </ul>
        </nav>
      </c:if>

    </c:when>

    <c:otherwise>
      <div class="text-center py-5">
        <p class="fs-5 text-muted">📭 Không có bài viết nào trong chuyên mục này.</p>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<style>
  .card-img-top {
    height: 220px;
    object-fit: cover;
    border-top-left-radius: 0.4rem;
    border-top-right-radius: 0.4rem;
  }
  .card-title {
    font-size: 1.15rem;
    font-weight: 600;
  }
  .card-text {
    font-size: 0.95rem;
    line-height: 1.5;
  }
  .card-footer {
    background: #f8f9fa;
    font-size: 0.9rem;
  }
</style>
