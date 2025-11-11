<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<div class="container mt-5">

  <h1 class="text-center mb-4">👥 Quản lý người dùng</h1>

  <!-- ✅ Thông báo -->
  <c:if test="${not empty message}">
    <div class="toast-msg success">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="toast-msg error">${error}</div>
  </c:if>

  <!-- ✅ Nếu KHÔNG phải admin -->
  <c:if test="${sessionScope.role != '0'}">
    <div class="alert alert-info text-center">
      📢 Bạn không có quyền truy cập trang quản trị.<br>
      👉 <a href="${pageContext.request.contextPath}/users?action=detailSelf" class="btn btn-primary mt-2">Xem thông tin cá nhân</a>
    </div>
  </c:if>

  <!-- ✅ Nếu là admin -->
  <c:if test="${sessionScope.role == '0'}">
    <div class="mb-3 text-end">
      <a href="${pageContext.request.contextPath}/users?action=create" class="btn btn-success">
        ➕ Thêm người dùng
      </a>
    </div>

    <!-- 🔍 Form tìm kiếm -->
    <form method="get" action="${pageContext.request.contextPath}/users" class="mb-4 d-flex justify-content-end">
      <input type="hidden" name="action" value="search">
      <input type="text" name="keyword" class="form-control w-25 me-2" placeholder="🔍 Tìm theo tên..." value="${keyword}">
      <button type="submit" class="btn btn-primary">Tìm kiếm</button>
    </form>

    <!-- 📊 Bảng người dùng -->
    <div class="table-responsive">
      <table class="table table-striped table-hover align-middle shadow-sm rounded">
        <thead class="table-dark text-center">
          <tr>
            <!-- ❌ Bỏ cột ID -->
            <th>Họ tên</th>
            <th>Email</th>
            <th>Ngày sinh</th>
            <th>Giới tính</th>
            <th>SĐT</th>
            <th>Vai trò</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty users}">
              <tr>
                <td colspan="7" class="text-center text-muted">📭 Không có người dùng nào.</td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="u" items="${users}">
                <tr class="text-center">
                  <!-- ❌ Không hiển thị ID -->
                  <td><c:out value="${u.fullname}" /></td>
                  <td><c:out value="${u.email}" /></td>
                  <td>
                    <c:choose>
                      <c:when test="${u.birthday != null}">
                        <fmt:formatDate value="${u.birthday}" pattern="dd/MM/yyyy"/>
                      </c:when>
                      <c:otherwise><span class="text-muted">Chưa cập nhật</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td><c:out value="${u.gender ? 'Nam' : 'Nữ'}" /></td>
                  <td><c:out value="${u.mobile}" /></td>
                  <td>
                    <c:choose>
                      <c:when test="${u.role == 0}"><span class="badge bg-danger">Admin</span></c:when>
                      <c:when test="${u.role == 1}"><span class="badge bg-primary">Tác giả</span></c:when>
                      <c:when test="${u.role == 2}"><span class="badge bg-success">Độc giả</span></c:when>
                      <c:otherwise><span class="badge bg-secondary">Không rõ</span></c:otherwise>
                    </c:choose>
                  </td>

                  <!-- ✨ Hành động -->
                  <td>
                    <div class="btn-group" role="group">

                      <!-- 👁️ Chi tiết -->
                      <a href="${pageContext.request.contextPath}/users?action=detail&id=${u.id}" 
                         class="btn btn-outline-info btn-sm" title="Chi tiết">
                        <i class="bi bi-person-lines-fill"></i>
                      </a>

                      <!-- ✏️ Chỉnh sửa -->
                      <a href="${pageContext.request.contextPath}/users?action=edit&id=${u.id}" 
                         class="btn btn-outline-warning btn-sm" title="Chỉnh sửa">
                        <i class="bi bi-pencil-square"></i>
                      </a>

                      <!-- 🗑️ Xóa người dùng -->
                      <form action="${pageContext.request.contextPath}/users" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${u.id}">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <button type="submit" class="btn btn-outline-danger btn-sm" title="Xóa"
                                onclick="return confirm('🗑️ Bạn có chắc chắn muốn xóa người dùng này không?');">
                          <i class="bi bi-trash"></i>
                        </button>
                      </form>

                      <!-- 🔑 Đặt lại mật khẩu -->
                      <form action="${pageContext.request.contextPath}/users" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="resetPassword">
                        <input type="hidden" name="id" value="${u.id}">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <button type="submit" class="btn btn-outline-secondary btn-sm" title="Đặt lại mật khẩu"
                                onclick="return confirm('🔑 Đặt lại mật khẩu về 123456?');">
                          <i class="bi bi-key-fill"></i>
                        </button>
                      </form>

                    </div>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </c:if>
</div>

<style>
.toast-msg {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 14px 20px;
  border-radius: 8px;
  color: #fff;
  font-weight: 600;
  animation: fadeIn 0.5s, fadeOut 0.5s 4.5s forwards;
  z-index: 1000;
}
.toast-msg.success { background: #198754; }
.toast-msg.error { background: #dc3545; }

.table thead th {
  vertical-align: middle;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.btn-group .btn {
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin: 0 2px;
}

.btn-group .btn i {
  font-size: 1.2rem;
}

@keyframes fadeIn { from { opacity: 0; transform: translateY(-20px);} to { opacity: 1; transform: translateY(0);} }
@keyframes fadeOut { from { opacity: 1; transform: translateY(0);} to { opacity: 0; transform: translateY(-20px);} }
</style>
