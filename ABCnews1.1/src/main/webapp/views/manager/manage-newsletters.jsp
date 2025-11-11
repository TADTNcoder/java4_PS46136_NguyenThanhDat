<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5">
  <h1 class="text-center mb-4">📨 Quản lý đăng ký nhận tin</h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty message}">
    <div class="alert alert-success text-center">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger text-center">${error}</div>
  </c:if>

  <!-- 🔍 Bộ lọc theo tác giả -->
  <form action="${pageContext.request.contextPath}/newsletter" method="get" class="mb-4 d-flex justify-content-end align-items-center">
    <label for="authorFilter" class="form-label fw-bold me-2">📌 Lọc theo tác giả:</label>
    <div class="me-2" style="min-width: 250px;">
      <select class="form-select" name="authorId" id="authorFilter" onchange="this.form.submit()">
        <option value="">-- Tất cả tác giả --</option>
        <c:if test="${not empty authors}">
          <c:forEach var="author" items="${authors}">
            <option value="${author.id}" <c:if test="${param.authorId eq author.id}">selected</c:if>>
              ${author.fullname}
            </option>
          </c:forEach>
        </c:if>
      </select>
    </div>
    <noscript><button type="submit" class="btn btn-primary">Lọc</button></noscript>
  </form>

  <div class="text-end mb-3">
    <a href="${pageContext.request.contextPath}/newsletter?action=create" class="btn btn-success">➕ Thêm đăng ký mới</a>
  </div>

  <!-- 📬 Bảng danh sách đăng ký -->
  <div class="table-responsive">
    <table class="table table-striped table-hover align-middle">
      <thead class="table-dark">
        <tr>
          <th>Email</th>
          <th>Trạng thái</th>
          <th>Tác giả theo dõi</th>
          <th>Ngày đăng ký</th>
          <th class="text-center">Hành động</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty subscribers}">
            <c:forEach var="sub" items="${subscribers}">
              <tr>
                <td>${sub.email}</td>
                <td>
                  <c:choose>
                    <c:when test="${sub.enabled}">
                      <span class="badge bg-success">Đang hoạt động</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge bg-secondary">Đã hủy</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>${empty sub.authorName ? '-' : sub.authorName}</td>
                <td>
                  <c:if test="${not empty sub.subscribedDate}">
                    <fmt:formatDate value="${sub.subscribedDate}" pattern="dd/MM/yyyy" />
                  </c:if>
                </td>
                <td class="text-center">
                  <a href="${pageContext.request.contextPath}/newsletter?action=edit&email=${sub.email}" 
                     class="btn btn-sm btn-warning">✏️ Sửa</a>
                  <form action="${pageContext.request.contextPath}/newsletter" 
                        method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="email" value="${sub.email}">
                    <button type="submit" class="btn btn-sm btn-danger"
                            onclick="return confirm('📛 Bạn có chắc muốn xóa đăng ký email này không?')">🗑️ Xóa</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="5" class="text-center text-muted">📭 Không có dữ liệu để hiển thị.</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<script>
  // 🧹 Tự động ẩn thông báo sau 5s
  setTimeout(() => {
    document.querySelectorAll('.alert').forEach(a => a.style.display = 'none');
  }, 5000);
</script>
