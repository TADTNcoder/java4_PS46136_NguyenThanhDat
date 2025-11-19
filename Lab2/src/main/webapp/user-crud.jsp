<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>User CRUD</title>
  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-4">

  <i class="text-primary">${message}</i>
  <c:url var="url" value="/user/crud"/>

  <!-- Form -->
  <form method="post" class="mb-4">
    <div class="mb-3">
      <label class="form-label">ID</label>
      <input name="id" value="${user.id}" class="form-control">
    </div>
    <div class="mb-3">
      <label class="form-label">Password</label>
      <input name="password" type="password" value="${user.password}" class="form-control">
    </div>
    <div class="mb-3">
      <label class="form-label">Fullname</label>
      <input name="fullname" value="${user.fullname}" class="form-control">
    </div>
    <div class="mb-3">
      <label class="form-label">Email</label>
      <input name="email" value="${user.email}" class="form-control">
    </div>
    <div class="mb-3">
      <label class="form-label">Role</label><br>
      <div class="form-check form-check-inline">
        <input class="form-check-input" name="admin" type="radio" value="true" ${user.admin ? 'checked' : ''}>
        <label class="form-check-label">Admin</label>
      </div>
      <div class="form-check form-check-inline">
        <input class="form-check-input" name="admin" type="radio" value="false" ${!user.admin ? 'checked' : ''}>
        <label class="form-check-label">User</label>
      </div>
    </div>
    <div class="d-flex gap-2">
      <button formaction="${url}/create" class="btn btn-success">Create</button>
      <button formaction="${url}/update" class="btn btn-primary">Update</button>
      <button formaction="${url}/delete" class="btn btn-danger">Delete</button>
      <button formaction="${url}/reset" class="btn btn-secondary">Reset</button>
    </div>
  </form>

  <!-- Table -->
  <table class="table table-bordered table-striped">
    <thead class="table-dark">
      <tr>
        <th>Id</th>
        <th>Password</th>
        <th>Fullname</th>
        <th>Email</th>
        <th>Role</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="u" items="${users}">
        <tr>
          <td>${u.id}</td>
          <td>${u.password}</td>
          <td>${u.fullname}</td>
          <td>${u.email}</td>
          <td>${u.admin ? 'Admin' : 'User'}</td>
          <td><a href="${url}/edit/${u.id}" class="btn btn-sm btn-warning">Edit</a></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <!-- Bootstrap 5 JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>