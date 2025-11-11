<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container mt-5">
  <h2 class="mb-4">
    <c:choose>
      <c:when test="${not empty news}">✏️ Cập nhật bài viết</c:when>
      <c:otherwise>➕ Viết bài mới</c:otherwise>
    </c:choose>
  </h2>

  <form action="${pageContext.request.contextPath}/news" 
        method="post" 
        enctype="multipart/form-data">

    <!-- ✅ CSRF token -->
    <input type="hidden" name="_csrf" value="${sessionScope._csrf}" />

    <!-- ✅ Hành động -->
    <input type="hidden" name="action" value="${not empty news ? 'update' : 'create'}" />

    <!-- ✅ ID và giữ ảnh cũ khi sửa -->
    <c:if test="${not empty news}">
      <input type="hidden" name="id" value="${news.id}" />
      <input type="hidden" name="existingImage" value="${news.image}" />
    </c:if>

    <!-- 📝 Tiêu đề -->
    <div class="mb-3">
      <label class="form-label">Tiêu đề <span class="text-danger">*</span></label>
      <input type="text" name="title" class="form-control" required
             value="${not empty news.title ? news.title : ''}" />
    </div>

    <!-- 📜 Nội dung -->
    <div class="mb-3">
      <label class="form-label">Nội dung <span class="text-danger">*</span></label>
      <textarea name="content" class="form-control" rows="8" required>${not empty news.content ? news.content : ''}</textarea>
    </div>

    <!-- 📂 Chuyên mục -->
    <div class="mb-3">
      <label class="form-label">Chuyên mục <span class="text-danger">*</span></label>
      <select name="categoryId" class="form-select" required>
        <option value="">-- Chọn chuyên mục --</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}" <c:if test="${news.categoryId eq cat.id}">selected</c:if>>
            ${cat.name}
          </option>
        </c:forEach>
      </select>
    </div>

    <!-- 🖼️ Ảnh đại diện -->
    <div class="mb-3">
      <label class="form-label">Ảnh đại diện</label>
      <input type="file" name="image" class="form-control" accept=".jpg,.jpeg,.png,.gif,.webp" onchange="previewImage(event)" />

      <!-- ✅ Ảnh hiện tại hoặc ảnh mặc định -->
      <c:choose>
        <c:when test="${not empty news.image}">
          <c:set var="imgSrc" value="${pageContext.request.contextPath}${news.image}" />
        </c:when>
        <c:otherwise>
          <c:set var="imgSrc" value="${pageContext.request.contextPath}/assets/images/default.jpg" />
        </c:otherwise>
      </c:choose>

      <div class="mt-2">
        <p class="small text-muted">📸 Ảnh hiện tại / ảnh mới chọn:</p>

        <!-- 🧠 Ảnh hiện tại sẽ đổi ngay khi chọn file mới -->
        <img id="preview" 
             src="${imgSrc}" 
             alt="Ảnh hiện tại" 
             class="img-thumbnail preview-img"
             onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/default.jpg';" />

        <!-- 📁 Tên file -->
        <p id="fileName" class="mt-2 text-muted small"></p>

        <!-- ❌ Nút xoá ảnh vừa chọn -->
        <button type="button" class="btn btn-outline-danger btn-sm mt-2" onclick="resetPreview()">❌ Xóa ảnh vừa chọn</button>
      </div>
    </div>

    <!-- ⚙️ Tùy chọn hiển thị trang chủ (Admin) -->
    <c:if test="${sessionScope.role eq '0' || sessionScope.user.role eq '0'}">
      <div class="form-check mb-3">
        <input class="form-check-input" type="checkbox" name="home" value="true" id="homeCheckbox"
               <c:if test="${news.home}">checked</c:if> />
        <label class="form-check-label" for="homeCheckbox">
          📍 Hiển thị trên trang chủ
        </label>
      </div>
    </c:if>

    <!-- ✅ Nút hành động -->
    <div class="d-flex justify-content-between">
      <a href="${pageContext.request.contextPath}/news" class="btn btn-secondary">⬅️ Quay lại</a>
      <button type="submit" class="btn btn-primary px-4">
        <c:choose>
          <c:when test="${not empty news}">💾 Cập nhật</c:when>
          <c:otherwise>➕ Tạo bài viết</c:otherwise>
        </c:choose>
      </button>
    </div>
  </form>
</div>

<!-- 📸 Script preview ảnh -->
<script>
  function previewImage(event) {
    const fileInput = event.target;
    const file = fileInput.files[0];
    const preview = document.getElementById("preview");
    const fileNameLabel = document.getElementById("fileName");

    if (file) {
      const validTypes = ["image/jpeg", "image/png", "image/webp", "image/gif"];
      if (!validTypes.includes(file.type)) {
        alert("❌ Vui lòng chọn ảnh JPG, PNG, GIF hoặc WEBP!");
        fileInput.value = "";
        preview.src = "${imgSrc}";
        if (fileNameLabel) fileNameLabel.innerText = "";
        return;
      }

      preview.src = URL.createObjectURL(file);
      preview.onload = () => URL.revokeObjectURL(preview.src);
      if (fileNameLabel) fileNameLabel.innerText = "📁 " + file.name;
    } else {
      preview.src = "${imgSrc}";
      if (fileNameLabel) fileNameLabel.innerText = "";
    }
  }

  function resetPreview() {
    const input = document.querySelector('input[name="image"]');
    input.value = "";
    const preview = document.getElementById("preview");
    preview.src = "${imgSrc}";
    const fileNameLabel = document.getElementById("fileName");
    if (fileNameLabel) fileNameLabel.innerText = "";
  }
</script>

<style>
textarea {
  resize: vertical;
}

img.img-thumbnail.preview-img {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
  border: 2px solid #dee2e6;
  display: block;
}

.btn {
  transition: 0.2s ease-in-out;
}

.btn:hover {
  transform: translateY(-1px);
}
</style>
