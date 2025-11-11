package controller;

import dao.daoimpl.NewsDAOImpl;
import dao.daoimpl.CategoryDAOImpl;
import dao.daoimpl.UserDAOImpl;
import model.News;
import model.Category;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final NewsDAOImpl newsDAO = new NewsDAOImpl();
    private final CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    private final UserDAOImpl userDAO = new UserDAOImpl(); // 👉 bạn cần có UserDAOImpl

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String keyword = Optional.ofNullable(request.getParameter("keyword")).orElse("").trim();

        // 📂 Luôn tải danh sách chuyên mục cho navbar
        List<Category> categories = Optional.ofNullable(categoryDAO.findAll()).orElse(Collections.emptyList());
        request.setAttribute("categories", categories);

        // 📌 Nếu không có từ khóa → quay lại trang chủ
        if (keyword.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // ✅ Tìm kiếm bài viết
        List<News> newsResults = newsDAO.searchByKeyword(keyword, 1, 50);

        // ✅ Tìm kiếm chuyên mục theo tên
        List<Category> categoryResults = categoryDAO.searchByName(keyword);

        // ✅ Tìm kiếm tác giả theo tên
        List<User> authorResults = userDAO.searchByName(keyword);

        // 📤 Gửi kết quả ra view
        request.setAttribute("keyword", keyword);
        request.setAttribute("newsResults", newsResults);
        request.setAttribute("categoryResults", categoryResults);
        request.setAttribute("authorResults", authorResults);

        request.setAttribute("pageTitle", "🔍 Kết quả tìm kiếm cho: " + keyword);
        request.setAttribute("contentPage", "/views/public/search-results.jsp");

        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }
}
