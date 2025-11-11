<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5">
  <h1 class="text-center mb-4">📩 Đăng ký nhận bản tin</h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty message}">
    <div class="alert alert-success text-center">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger text-center">${error}</div>
  </c:if>

  <!-- 📬 Form đăng ký -->
  <form action="${pageContext.request.contextPath}/newsletter" method="post" class="mx-auto p-4 shadow rounded bg-light" style="max-width: 500px;">
    <input type="hidden" name="action" value="subscribe">

    <div class="mb-3">
      <label for="email" class="form-label fw-bold">📧 Email của bạn:</label>
      <input type="email" id="email" name="email" class="form-control" placeholder="Nhập email của bạn..." required>
    </div>

    <div class="mb-3">
      <label for="authorId" class="form-label fw-bold">✍️ Chọn tác giả muốn theo dõi:</label>
      <select id="authorId" name="authorId" class="form-select" required>
        <option value="">-- Chọn tác giả --</option>
        <c:forEach var="author" items="${authors}">
          <option value="${author.id}">${author.fullname}</option>
        </c:forEach>
      </select>
    </div>

    <div class="text-center mt-4">
      <button type="submit" class="btn btn-primary px-4">📬 Đăng ký theo dõi</button>
    </div>
  </form>

  <!-- 📚 Danh sách tác giả đang theo dõi -->
  <c:if test="${not empty followedAuthors}">
    <h3 class="mt-5 text-center">👤 Bạn đang theo dõi</h3>
    <div class="table-responsive mt-3">
      <table class="table table-striped table-hover align-middle">
        <thead class="table-dark">
          <tr>
            <th>Email</th>
            <th>Tác giả theo dõi</th>
            <th>Ngày đăng ký</th>
            <th class="text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="f" items="${followedAuthors}">
            <tr>
              <td>${f.email}</td>
              <td>${f.authorName}</td>
              <td><fmt:formatDate value="${f.subscribedDate}" pattern="dd/MM/yyyy" /></td>
              <td class="text-center">
                <form action="${pageContext.request.contextPath}/newsletter" method="post" style="display:inline;">
                  <input type="hidden" name="action" value="unsubscribe">
                  <input type="hidden" name="email" value="${f.email}">
                  <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn hủy theo dõi tác giả này?')">❌ Hủy</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
</div>

<script>
  setTimeout(() => {
    document.querySelectorAll('.alert').forEach(a => a.style.display = 'none');
  }, 5000);
</script>

<style>
  h1, h3 {
    font-weight: 700;
  }
  form {
    background: #f8f9fa;
  }
  .btn-primary {
    background-color: #0d6efd;
    border: none;
  }
  .btn-primary:hover {
    background-color: #0b5ed7;
  }
  .btn-danger {
    background-color: #dc3545;
    border: none;
  }
  .btn-danger:hover {
    background-color: #bb2d3b;
  }
</style>
