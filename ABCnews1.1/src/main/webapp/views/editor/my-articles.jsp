<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>✍️ Bài viết của tôi</h2>
    <a href="${pageContext.request.contextPath}/news?action=create" class="btn btn-success">➕ Viết bài mới</a>
  </div>

  <!-- 🔎 Form lọc và tìm kiếm -->
  <form class="row g-2 mb-4" method="get" action="${pageContext.request.contextPath}/news">
    <div class="col-md-4">
      <select name="categoryId" class="form-select" onchange="this.form.submit()">
        <option value="">📁 Tất cả chuyên mục</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}" <c:if test="${param.categoryId == cat.id}">selected</c:if>>
            ${cat.name}
          </option>
        </c:forEach>
      </select>
    </div>

    <div class="col-md-4 d-flex">
      <input type="text" name="keyword" class="form-control me-2"
             placeholder="🔍 Nhập tiêu đề cần tìm..."
             value="${param.keyword}">
      <button type="submit" class="btn btn-primary">Tìm</button>
    </div>
  </form>

  <!-- 📰 Hiển thị danh sách -->
  <c:if test="${empty newsList}">
    <div class="alert alert-info text-center">
      📭 Bạn chưa có bài viết nào.
    </div>
  </c:if>

  <c:if test="${not empty newsList}">
    <div class="table-responsive shadow-sm">
      <table class="table table-striped table-hover align-middle">
        <thead class="table-dark text-center">
          <tr>
            <th>Tiêu đề</th>
            <th>Chuyên mục</th>
            <th>Ngày đăng</th>
            <th class="text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="n" items="${newsList}">
            <tr>
              <td>${n.title}</td>
              <td>
                <c:forEach var="c" items="${categories}">
                  <c:if test="${c.id == n.categoryId}">${c.name}</c:if>
                </c:forEach>
              </td>
              <td><fmt:formatDate value="${n.postedDate}" pattern="dd/MM/yyyy" /></td>
              <td class="text-center">

                <!-- ✏️ Sửa -->
                <a href="${pageContext.request.contextPath}/news?action=edit&id=${n.id}" 
                   class="btn btn-sm btn-primary">✏️ Sửa</a>

                <!-- 🗑️ Xóa -->
                <form action="${pageContext.request.contextPath}/news" method="post" 
                      style="display:inline-block" 
                      onsubmit="return confirm('❗ Bạn có chắc muốn xóa bài viết này không?');">
                  <input type="hidden" name="_csrf" value="${sessionScope._csrf}" />
                  <input type="hidden" name="action" value="delete" />
                  <input type="hidden" name="id" value="${n.id}" />
                  <button type="submit" class="btn btn-sm btn-danger">🗑️ Xóa</button>
                </form>

                <!-- 👁️ Xem -->
                <a href="${pageContext.request.contextPath}/news?action=view&id=${n.id}&preview=true" 
                   class="btn btn-sm btn-outline-secondary">👁️ Xem</a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>

  <!-- 📄 Phân trang -->
  <c:if test="${total > size}">
    <nav aria-label="Page navigation" class="mt-4">
      <ul class="pagination justify-content-center">
        <c:forEach var="i" begin="1" end="${(total / size) + (total % size > 0 ? 1 : 0)}">
          <li class="page-item ${i == page ? 'active' : ''}">
            <a class="page-link" href="?page=${i}&size=${size}">${i}</a>
          </li>
        </c:forEach>
      </ul>
    </nav>
  </c:if>
</div>

<style>
.container h2 {
  font-weight: 700;
}
.table-hover tbody tr:hover {
  background-color: #f8f9fa;
}
.btn-sm {
  margin: 0 2px;
}
.alert {
  font-size: 1rem;
  padding: 12px;
}
form select, form input {
  height: 45px;
}
</style>
