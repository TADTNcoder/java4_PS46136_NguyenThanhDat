<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ✅ THÔNG BÁO -->
<c:if test="${not empty error}">
  <div id="alertBox" class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty regError}">
  <div id="alertBox" class="alert alert-danger">${regError}</div>
</c:if>
<c:if test="${not empty regSuccess}">
  <div id="alertBox" class="alert alert-success">${regSuccess}</div>
</c:if>

<!-- ===== ĐĂNG NHẬP ===== -->
<div class="form-container ${show eq 'register' ? 'd-none' : ''}" id="loginForm">
  <h2 class="text-success">Đăng nhập</h2>
  <form action="login" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required placeholder="Nhập email đăng nhập" />

    <label for="password">Mật khẩu:</label>
    <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu" />

    <div class="login-options">
      <div class="form-check mb-0">
        <input class="form-check-input" type="checkbox" id="remember" name="remember">
        <label class="form-check-label" for="remember">Ghi nhớ đăng nhập</label>
      </div>
      <a href="javascript:void(0)" class="forgot" onclick="toggleForm('forgotForm')">Quên mật khẩu?</a>
    </div>

    <button type="submit" class="btn-main">Đăng nhập</button>
  </form>

  <button class="toggle-btn" onclick="toggleForm('registerForm')">Chưa có tài khoản? Đăng ký</button>
</div>

<!-- ===== ĐĂNG KÝ ===== -->
<div class="form-container ${show eq 'register' ? '' : 'd-none'}" id="registerForm">
  <h2 class="text-success">Đăng ký tài khoản</h2>
  <form action="register" method="post" onsubmit="return validateRegister()">
    <label for="fullname">Họ và tên:</label>
    <input type="text" id="fullname" name="fullname" required placeholder="Nhập họ và tên" />

    <label for="emailReg">Email:</label>
    <input type="email" id="emailReg" name="email" required placeholder="Nhập email" />

    <label for="mobile">Số điện thoại:</label>
    <input type="text" id="mobile" name="mobile" placeholder="Nhập số điện thoại" />

    <label for="birthday">Ngày sinh:</label>
    <input type="date" id="birthday" name="birthday" />

    <label for="gender">Giới tính:</label>
    <select id="gender" name="gender" required>
      <option value="male">Nam</option>
      <option value="female">Nữ</option>
    </select>

    <label for="username">Tên đăng nhập:</label>
    <input type="text" id="username" name="username" required placeholder="Tạo tên đăng nhập" />

    <label for="regPassword">Mật khẩu:</label>
    <input type="password" id="regPassword" name="password" required placeholder="Tạo mật khẩu" />

    <label for="confirmPassword">Xác nhận mật khẩu:</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Nhập lại mật khẩu" />

    <label for="role">Vai trò:</label>
    <select id="role" name="role" required>
      <option value="1">Tác giả</option>
      <option value="2" selected>Độc giả</option>
    </select>

    <button type="submit" class="btn-main">Đăng ký</button>
  </form>

  <button class="toggle-btn" onclick="toggleForm('loginForm')">Đã có tài khoản? Đăng nhập</button>
</div>

<!-- ===== QUÊN MẬT KHẨU ===== -->
<div class="form-container d-none" id="forgotForm">
  <h2 class="text-success">Quên mật khẩu</h2>
  <form action="forgot-password" method="post">
    <p class="text-muted">Nhập email đã đăng ký. Chúng tôi sẽ gửi liên kết đặt lại mật khẩu.</p>
    <input type="email" id="forgotEmail" name="email" required placeholder="Nhập email của bạn" />
    <button type="submit" class="btn-main">Gửi liên kết đặt lại</button>
  </form>

  <button class="toggle-btn" onclick="toggleForm('loginForm')">Quay lại Đăng nhập</button>
</div>

<script>
function toggleForm(formId){
  ['loginForm','registerForm','forgotForm'].forEach(id=>{
    document.getElementById(id).classList.add('d-none');
  });
  document.getElementById(formId).classList.remove('d-none');
  window.scrollTo({top:0, behavior:'smooth'});
}

function validateRegister(){
  const p = document.getElementById('regPassword').value;
  const c = document.getElementById('confirmPassword').value;
  if(p !== c){
    alert('⚠️ Mật khẩu xác nhận không khớp!');
    return false;
  }
  return true;
}

window.addEventListener("DOMContentLoaded", function() {
  const alertBox = document.getElementById("alertBox");
  if (alertBox) {
    setTimeout(() => {
      alertBox.classList.add("fade-out");
      setTimeout(() => alertBox.remove(), 500);
    }, 5000);
  }
  <c:if test="${not empty regSuccess}">
    toggleForm('loginForm');
  </c:if>
});
</script>

<!-- ✅ CSS Xanh kiểu Dân Trí -->
<style>
body {
  background: #f4f6f5;
  font-family: "Segoe UI", Arial, sans-serif;
}

.form-container {
  width: 420px;
  margin: 100px auto;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.08);
  padding: 2.5rem;
  text-align: center;
  transition: all 0.3s ease;
}
h2 {
  margin-bottom: 1.5rem;
  font-weight: 700;
}
label {
  float: left;
  margin-bottom: 0.4rem;
  font-weight: 600;
  font-size: 0.95rem;
  color: #333;
}
input, select {
  width: 100%;
  padding: 10px;
  margin-bottom: 1.2rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  transition: border-color 0.2s ease;
}
input:focus, select:focus {
  border-color: #00994d;
  outline: none;
}

.btn-main {
  width: 100%;
  padding: 10px;
  border: none;
  background: #006633;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.3s ease, transform 0.1s ease;
}
.btn-main:hover {
  background: #00994d;
  transform: translateY(-2px);
}

.toggle-btn {
  margin-top: 1rem;
  background: none;
  border: none;
  color: #00994d;
  font-weight: 600;
  cursor: pointer;
}

.alert {
  width: 420px;
  margin: 20px auto;
  padding: 12px 18px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  text-align: center;
  animation: fadeIn 0.4s ease-in-out;
}
.alert-success {
  background: #d1e7dd;
  color: #0f5132;
  border: 1px solid #badbcc;
}
.alert-danger {
  background: #f8d7da;
  color: #842029;
  border: 1px solid #f5c2c7;
}
.fade-out {
  animation: fadeOut 0.5s ease-in-out forwards;
}
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes fadeOut { from { opacity: 1; } to { opacity: 0; } }

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.2rem;
}
.login-options .forgot {
  text-decoration: none;
  color: #00994d;
}
.login-options .forgot:hover {
  text-decoration: underline;
}
</style>
