// Newsletter form validation
function validateNewsletterForm() {
  const emailInput = document.querySelector("input[name='email']");
  if (!emailInput.value || !emailInput.value.includes("@")) {
    alert("Vui lòng nhập địa chỉ email hợp lệ.");
    return false;
  }
  return true;
}

// Login form validation
function validateLoginForm() {
  const id = document.querySelector("input[name='id']");
  const password = document.querySelector("input[name='password']");
  if (!id.value || !password.value) {
    alert("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
    return false;
  }
  return true;
}

// Delete confirmation
function confirmDelete(itemName = "mục này") {
  return confirm(`Bạn có chắc chắn muốn xóa ${itemName}?`);
}

// Attach confirmation to delete links
document.addEventListener("DOMContentLoaded", () => {
  const deleteLinks = document.querySelectorAll("a[href*='delete']");
  deleteLinks.forEach(link => {
    link.addEventListener("click", (e) => {
      if (!confirmDelete()) {
        e.preventDefault();
      }
    });
  });
});