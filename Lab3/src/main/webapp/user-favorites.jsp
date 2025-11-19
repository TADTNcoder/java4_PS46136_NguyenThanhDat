<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <title>Danh sách video yêu thích</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="bg-light">

<div class="container mt-5">
  <div class="card shadow-sm">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Chọn người dùng để xem video yêu thích</h4>
    </div>
    <div class="card-body">
      <!-- Form chọn user -->
      <form method="get" action="favorites" class="mb-3">
        <div class="input-group">
          <select name="userId" class="form-select">
            <option value="">-- Chọn người dùng --</option>
            <c:forEach var="u" items="${users}">
              <option value="${u.id}" <c:if test="${user.id == u.id}">selected</c:if>>
                ${u.fullname} (${u.email})
              </option>
            </c:forEach>
          </select>
          <button type="submit" class="btn btn-primary">Xem</button>
        </div>
      </form>

      <!-- Hiển thị danh sách video yêu thích -->
      <c:if test="${not empty favorites}">
        <h5 class="mb-3">Video yêu thích của ${user.fullname}</h5>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Tiêu đề video</th>
              <th>Ngày thích</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="f" items="${favorites}">
              <tr>
                <td>${f.video.title}</td>
                <td>
                  <c:if test="${not empty f.likeDate}">
                    <fmt:formatDate value="${f.likeDate}" pattern="dd/MM/yyyy"/>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>

      <c:if test="${empty favorites && not empty user}">
        <div class="alert alert-warning">Người dùng này chưa thích video nào.</div>
      </c:if>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>