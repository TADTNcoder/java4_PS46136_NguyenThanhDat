package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.daoimpl.UserDAOImpl;
import model.User;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        User user = userDAO.findByEmail(email);

        if (user != null) {
            // TODO: sinh token + gửi email reset mật khẩu
            request.setAttribute("forgotSuccess", "Liên kết đặt lại mật khẩu đã được gửi đến email của bạn!");
        } else {
            request.setAttribute("forgotError", "Không tìm thấy tài khoản với email này.");
        }

        request.setAttribute("pageTitle", "Quên mật khẩu");
        request.setAttribute("contentPage", "/views/login.jsp");
        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }
}
