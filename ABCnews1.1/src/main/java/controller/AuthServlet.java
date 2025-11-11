package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.daoimpl.UserDAOImpl;
import model.User;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);

        // Nếu đã đăng nhập → chuyển hướng về dashboard luôn
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Gọi layout và chèn login.jsp vào contentPage
        request.setAttribute("pageTitle", "Đăng nhập");
        request.setAttribute("contentPage", "/views/public/login.jsp");
        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);

        // ✅ Lấy email & mật khẩu thay vì id
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // ✅ Tìm người dùng theo email
            User user = userDAO.findByEmail(email);

            // ✅ Kiểm tra đăng nhập
            if (user != null && user.getPassword().equals(password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole());
                session.setAttribute("userId", user.getId()); // tiện dùng ở các servlet khác

                // ✅ Điều hướng: AuthFilter sẽ xử lý quyền chi tiết
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // ❌ Sai email hoặc mật khẩu
                request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
                request.setAttribute("pageTitle", "Đăng nhập thất bại");
                request.setAttribute("contentPage", "/views/public/login.jsp");
                request.getRequestDispatcher("/layout.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "⚠️ Lỗi xử lý đăng nhập.");
        }
    }

    // ================= UTILITIES =================
    private void setupEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }
}
