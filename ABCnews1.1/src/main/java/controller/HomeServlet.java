package controller;

import dao.daoimpl.NewsDAOImpl;
import dao.daoimpl.CategoryDAOImpl;
import model.News;
import model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final NewsDAOImpl newsDAO = new NewsDAOImpl();
    private final CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);

        // 📂 Danh sách chuyên mục luôn có sẵn
        List<Category> categories = Optional.ofNullable(categoryDAO.findAll()).orElse(Collections.emptyList());
        request.setAttribute("categories", categories);

        // ✅ Nếu đã đăng nhập → về dashboard
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // 📄 Xử lý phân trang
        int page = 1;
        int size = 7; // 👉 mỗi trang 6 bài
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isBlank()) {
                page = Math.max(1, Integer.parseInt(pageParam));
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        // 📌 Lọc theo chuyên mục nếu có
        String categoryId = request.getParameter("categoryId");
        List<News> latestNews;
        long totalItems;

        if (categoryId != null && !categoryId.isBlank()) {
            latestNews = newsDAO.findByCategory(categoryId, page, size);
            totalItems = newsDAO.countByCategory(categoryId);
            request.setAttribute("activeCategoryId", categoryId);
        } else {
            latestNews = newsDAO.findAllPaged(page, size);
            totalItems = newsDAO.countAll();
        }

        // 📊 Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // ✅ Gửi dữ liệu sang JSP
        request.setAttribute("latestNews", latestNews);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // 🏠 Tiêu đề & nội dung
        request.setAttribute("pageTitle", "Trang chủ");
        request.setAttribute("contentPage", "/views/public/index.jsp");

        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }
}
