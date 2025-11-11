<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container mt-5">
  <h2>🔍 Kết quả tìm kiếm cho: "<span class="text-primary">${keyword}</span>"</h2>

  <!-- 📚 Bài viết -->
  <h4 class="mt-4">📰 Bài viết</h4>
  <c:if test="${empty newsResults}">
    <p class="text-muted">❌ Không tìm thấy bài viết nào.</p>
  </c:if>
  <c:forEach var="n" items="${newsResults}">
    <div class="border-bottom py-2">
      <a href="${pageContext.request.contextPath}/news?action=view&id=${n.id}">
        <strong>${n.title}</strong>
      </a>
      <p class="text-muted small">${n.content}</p>
    </div>
  </c:forEach>

  <!-- 📂 Chuyên mục -->
  <h4 class="mt-5">📁 Chuyên mục</h4>
  <c:if test="${empty categoryResults}">
    <p class="text-muted">❌ Không tìm thấy chuyên mục nào.</p>
  </c:if>
  <c:forEach var="c" items="${categoryResults}">
    <div class="border-bottom py-1">
      📂 <a href="${pageContext.request.contextPath}/news?categoryId=${c.id}">${c.name}</a>
    </div>
  </c:forEach>

  <!-- 👤 Tác giả -->
  <h4 class="mt-5">👤 Tác giả</h4>
  <c:if test="${empty authorResults}">
    <p class="text-muted">❌ Không tìm thấy tác giả nào.</p>
  </c:if>
  <c:forEach var="u" items="${authorResults}">
    <div class="border-bottom py-1">
      👤 ${u.fullname} — <a href="mailto:${u.email}">${u.email}</a>
    </div>
  </c:forEach>
</div>
<style>
/* 🧭 Tổng thể */
body {
  background-color: #f8f9fa;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.container {
  max-width: 850px;
}

/* 🔍 Tiêu đề chính */
h2 {
  font-weight: 700;
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #212529;
}

.text-primary {
  color: #0d6efd !important;
}

/* 📚 Tiêu đề từng phần */
h4 {
  font-size: 1.4rem;
  font-weight: 600;
  margin-top: 2rem;
  margin-bottom: 1rem;
  color: #343a40;
  border-left: 5px solid #0d6efd;
  padding-left: 12px;
}

/* 📜 Khối kết quả */
.border-bottom {
  border-bottom: 1px solid #dee2e6 !important;
  padding-bottom: 0.8rem;
  margin-bottom: 0.8rem;
}

.border-bottom:last-child {
  border-bottom: none !important;
}

/* 📰 Bài viết */
.border-bottom a {
  text-decoration: none;
  color: #0d6efd;
  font-size: 1.1rem;
  transition: color 0.25s ease-in-out;
}

.border-bottom a:hover {
  color: #0056b3;
  text-decoration: underline;
}

.border-bottom p {
  margin-top: 0.4rem;
  font-size: 0.95rem;
  color: #6c757d;
  line-height: 1.5;
}

/* 📂 Chuyên mục và 👤 Tác giả */
.border-bottom.py-1 {
  font-size: 1rem;
}

.border-bottom.py-1 a {
  color: #198754;
  font-weight: 500;
  text-decoration: none;
}

.border-bottom.py-1 a:hover {
  text-decoration: underline;
  color: #145c32;
}

/* 📭 Không có kết quả */
.text-muted {
  font-style: italic;
  color: #6c757d !important;
}

/* ✨ Responsive */
@media (max-width: 768px) {
  h2 {
    font-size: 1.7rem;
  }
  h4 {
    font-size: 1.2rem;
  }
  .border-bottom a {
    font-size: 1rem;
  }
}
</style>

