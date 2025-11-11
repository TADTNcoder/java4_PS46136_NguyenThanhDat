<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-5">
  <h1 class="text-center mb-4">👥 Danh sách người theo dõi bạn</h1>

  <c:choose>
    <c:when test="${not empty followers}">
      <div class="table-responsive shadow-sm rounded">
        <table class="table table-striped table-hover align-middle">
          <thead class="table-dark">
            <tr>
              <th>Email</th>
              <th>Trạng thái</th>
              <th>Ngày đăng ký</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="f" items="${followers}">
              <tr>
                <td>${f.email}</td>
                <td>
                  <c:choose>
                    <c:when test="${f.enabled}">
                      <span class="badge bg-success">Đang theo dõi</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge bg-secondary">Đã hủy</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <fmt:formatDate value="${f.subscribedDate}" pattern="dd/MM/yyyy" />
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:when>
    <c:otherwise>
      <div class="alert alert-info text-center mt-4">📭 Hiện tại chưa có ai theo dõi bạn.</div>
    </c:otherwise>
  </c:choose>
</div>

<style>
  h1 {
    font-weight: 700;
  }
  table {
    background: #ffffff;
  }
  .badge {
    font-size: 0.9rem;
  }
</style>
	