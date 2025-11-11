<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="footer-dt text-dark pt-5 pb-3 mt-5">
  <div class="container">
    <div class="row gy-4">

      <!-- About -->
      <div class="col-md-4">
        <h5 class="fw-bold text-success">ABCNEWS</h5>
        <p class="text-muted">Nhận tóm tắt tin tức nổi bật, hấp dẫn nhất 24 giờ qua trên ABCnews.</p>
      </div>

      <!-- Policies -->
      <div class="col-md-4">
        <h5 class="fw-bold text-success">Chính sách</h5>
        <ul class="list-unstyled">
          <li><a href="#" class="footer-link">Điều khoản sử dụng</a></li>
          <li><a href="#" class="footer-link">Chính sách bảo mật</a></li>
          <li><a href="#" class="footer-link">Cookies</a></li>
          <li><a href="#" class="footer-link">Theo dõi</a></li>
        </ul>
      </div>

      <!-- Contact -->
      <div class="col-md-4">
        <h5 class="fw-bold text-success">Liên hệ</h5>
        <p class="text-muted mb-1"><i class="bi bi-geo-alt-fill text-success me-2"></i>2022 QL1A, Quận 12, TP.HCM</p>
        <p class="text-muted mb-1"><i class="bi bi-telephone-fill text-success me-2"></i>0865301349</p>
        <p class="text-muted mb-0"><i class="bi bi-envelope-fill text-success me-2"></i>tadtnwork@gmail.com</p>
      </div>

    </div>

    <div class="text-center pt-4 mt-4 border-top">
      <small class="text-muted">© 2025 TADtn. All rights reserved.</small>
    </div>
  </div>
</footer>

<!-- ✅ CSS xanh kiểu Dân Trí -->
<style>
.footer-dt {
  background-color: #f8f9fa;
  border-top: 4px solid #006633;
  font-size: 0.95rem;
}

.footer-dt h5 {
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.footer-link {
  color: #333;
  text-decoration: none;
  display: inline-block;
  margin-bottom: 6px;
  transition: color 0.3s ease, transform 0.2s ease;
}

.footer-link:hover {
  color: #00994d;
  transform: translateX(4px);
}

.footer-dt i {
  font-size: 1.1rem;
}

.footer-dt small {
  font-size: 0.85rem;
}
</style>

<!-- JS chung -->
<script src="<c:url value='/assets/js/site.js'/>"></script>
