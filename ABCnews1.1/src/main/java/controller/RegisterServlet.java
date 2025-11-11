package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.daoimpl.UserDAOImpl;
import model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);

        // ✅ Lấy dữ liệu từ form
        String id = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullname = request.getParameter("fullname");
        String birthdayStr = request.getParameter("birthday");
        String genderStr = request.getParameter("gender");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
        String roleStr = request.getParameter("role"); // "1" = Tác giả, "2" = Độc giả

        // ✅ Kiểm tra dữ liệu bắt buộc
        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("regError", "⚠️ Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            forwardBack(request, response);
            return;
        }

        // ✅ Kiểm tra xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            request.setAttribute("regError", "❌ Mật khẩu xác nhận không khớp!");
            forwardBack(request, response);
            return;
        }

        try {
            // ✅ Kiểm tra trùng tên đăng nhập
            if (userDAO.findById(id) != null) {
                request.setAttribute("regError", "⚠️ Tên đăng nhập đã tồn tại.");
                forwardBack(request, response);
                return;
            }

            // ✅ Parse ngày sinh nếu có
            Date birthday = null;
            if (birthdayStr != null && !birthdayStr.isEmpty()) {
                try {
                    birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthdayStr);
                } catch (Exception e) {
                    request.setAttribute("regError", "⚠️ Ngày sinh không hợp lệ.");
                    forwardBack(request, response);
                    return;
                }
            }

            // ✅ Parse giới tính (1 = Nam, 0 = Nữ)
            Boolean gender = null;
            if (genderStr != null && !genderStr.isEmpty()) {
                gender = "male".equalsIgnoreCase(genderStr);
            }

            // ✅ Parse quyền (mặc định = 2 - Độc giả)
            int role = 2;
            if (roleStr != null && !roleStr.isEmpty()) {
                try {
                    role = Integer.parseInt(roleStr);
                    if (role < 1 || role > 2) role = 2; // tránh nhập sai role
                } catch (NumberFormatException ignored) {}
            }

            // ✅ Tạo user mới
            User newUser = new User();
            newUser.setId(id);
            newUser.setPassword(password);
            newUser.setFullname(fullname);
            newUser.setBirthday(birthday);
            newUser.setGender(gender);
            newUser.setMobile(mobile);
            newUser.setEmail(email);
            newUser.setRole(role);

            // ✅ Lưu vào DB
            boolean success = userDAO.insert(newUser);
            if (success) {
                request.setAttribute("regSuccess", "🎉 Đăng ký thành công! Bạn có thể đăng nhập ngay.");
            } else {
                request.setAttribute("regError", "❌ Đăng ký thất bại. Vui lòng thử lại.");
            }

            forwardBack(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Lỗi xử lý đăng ký.");
        }
    }

    // ================= UTILITIES =================

    private void setupEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }

    /**
     * ✅ Quay lại form đăng nhập/đăng ký
     */
    private void forwardBack(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("show", "register"); // để JSP biết mở tab đăng ký
        request.setAttribute("pageTitle", "Đăng ký / Đăng nhập");
        request.setAttribute("contentPage", "/views/public/login.jsp");
        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }
}
