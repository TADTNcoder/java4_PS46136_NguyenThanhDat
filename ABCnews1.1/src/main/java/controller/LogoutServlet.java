package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 1. Hủy session nếu tồn tại
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // ✅ 2. Xóa cookie "rememberId" (nếu có)
        Cookie cookie = new Cookie("rememberId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/"); // ⚠️ Quan trọng: nên để "/" để chắc chắn cookie bị xóa ở mọi path
        response.addCookie(cookie);

        // ✅ 3. Chuyển hướng về trang login
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cho phép logout qua POST nếu cần (ví dụ từ form)
        doGet(request, response);
    }
}
