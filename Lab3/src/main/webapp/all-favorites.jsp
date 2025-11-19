<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <title>Danh sách tất cả video đã được thích</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="bg-light">

<div class="container mt-5">
  <div class="card shadow-sm">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Danh sách video đã được thích</h4>
    </div>
    <div class="card-body">
      <c:if test="${not empty favorites}">
        <table class="table table-striped table-hover">
          <thead class="table-dark">
            <tr>
              <th>Video</th>
              <th>Người thích</th>
              <th>Ngày thích</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="f" items="${favorites}">
              <tr>
                <td>${f.video.title}</td>
                <td>${f.user.fullname}</td>
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

      <c:if test="${empty favorites}">
        <div class="alert alert-warning">Chưa có video nào được thích.</div>
      </c:if>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>