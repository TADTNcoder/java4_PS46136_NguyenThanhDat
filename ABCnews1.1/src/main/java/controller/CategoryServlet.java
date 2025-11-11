package controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.daoimpl.CategoryDAOImpl;
import model.Category;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        String role = (session != null && session.getAttribute("role") != null)
                ? String.valueOf(session.getAttribute("role"))
                : null;

        String action = request.getParameter("action");
        String keyword = request.getParameter("search");

        // 🛡️ Nếu chưa đăng nhập → quay lại trang chủ
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 📚 Nếu có từ khóa tìm kiếm → lọc danh mục
        List<Category> categories;
        if (keyword != null && !keyword.trim().isEmpty()) {
            categories = categoryDAO.findByNameLike(keyword.trim());
            request.setAttribute("searchKeyword", keyword);
        } else {
            categories = categoryDAO.findAll();
        }
        request.setAttribute("categories", categories);

        // ➕ Trang thêm chuyên mục
        if ("create".equals(action)) {
            if (!"0".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thêm chuyên mục.");
                return;
            }
            request.setAttribute("pageTitle", "➕ Thêm chuyên mục mới");
            request.setAttribute("contentPage", "/views/manager/category-form.jsp");
            request.getRequestDispatcher("/layout.jsp").forward(request, response);
            return;
        }

        // ✏️ Trang chỉnh sửa chuyên mục
        if ("edit".equals(action)) {
            if (!"0".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền chỉnh sửa chuyên mục.");
                return;
            }

            String id = request.getParameter("id");
            Category category = categoryDAO.findById(id);
            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chuyên mục.");
                return;
            }

            request.setAttribute("category", category);
            request.setAttribute("pageTitle", "✏️ Cập nhật chuyên mục");
            request.setAttribute("contentPage", "/views/manager/category-form.jsp");
            request.getRequestDispatcher("/layout.jsp").forward(request, response);
            return;
        }

        // 🛑 Cấm xoá qua GET
        if ("delete".equals(action)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Xóa chuyên mục phải dùng POST.");
            return;
        }

        // 📁 Trang danh sách chuyên mục
        request.setAttribute("pageTitle", "📁 Danh sách chuyên mục");
        request.setAttribute("contentPage", "/views/manager/manage-categories.jsp");
        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        String role = (session != null && session.getAttribute("role") != null)
                ? String.valueOf(session.getAttribute("role"))
                : null;

        // 🚨 Chỉ admin mới có quyền thao tác
        if (!"0".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thao tác chuyên mục.");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số hành động.");
            return;
        }

        try {
            switch (action) {
                case "create": {
                    String name = request.getParameter("name");
                    if (name == null || name.trim().isEmpty()) {
                        request.setAttribute("error", "⚠️ Tên chuyên mục không được để trống.");
                        reloadForm(request, response, "/views/manager/category-form.jsp", "➕ Thêm chuyên mục mới");
                        return;
                    }

                    if (categoryDAO.existsByName(name.trim())) {
                        request.setAttribute("error", "⚠️ Tên chuyên mục đã tồn tại.");
                        reloadForm(request, response, "/views/manager/category-form.jsp", "➕ Thêm chuyên mục mới");
                        return;
                    }

                    Category category = new Category(UUID.randomUUID().toString(), name.trim());
                    categoryDAO.insert(category);
                    response.sendRedirect("categories?success=created");
                    break;
                }

                case "update": {
                    String id = request.getParameter("id");
                    String name = request.getParameter("name");

                    if (id == null || id.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID chuyên mục.");
                        return;
                    }

                    Category existing = categoryDAO.findById(id);
                    if (existing == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chuyên mục.");
                        return;
                    }

                    if (name == null || name.trim().isEmpty()) {
                        request.setAttribute("error", "⚠️ Tên chuyên mục không được để trống.");
                        request.setAttribute("category", existing);
                        reloadForm(request, response, "/views/manager/category-form.jsp", "✏️ Cập nhật chuyên mục");
                        return;
                    }

                    // ✅ Kiểm tra trùng tên khi cập nhật
                    if (categoryDAO.existsByName(name.trim()) &&
                            !existing.getName().equalsIgnoreCase(name.trim())) {
                        request.setAttribute("error", "⚠️ Tên chuyên mục đã tồn tại.");
                        request.setAttribute("category", existing);
                        reloadForm(request, response, "/views/manager/category-form.jsp", "✏️ Cập nhật chuyên mục");
                        return;
                    }

                    categoryDAO.update(new Category(id, name.trim()));
                    response.sendRedirect("categories?success=updated");
                    break;
                }

                case "delete": {
                    String id = request.getParameter("id");
                    if (id == null || id.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID chuyên mục để xoá.");
                        return;
                    }

                    Category existing = categoryDAO.findById(id);
                    if (existing == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chuyên mục để xoá.");
                        return;
                    }

                    categoryDAO.delete(id);
                    response.sendRedirect("categories?success=deleted");
                    break;
                }

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                    return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Lỗi xử lý chuyên mục.");
        }
    }

    // 🔁 Hàm tiện ích để load lại form kèm danh sách danh mục
    private void reloadForm(HttpServletRequest request, HttpServletResponse response, String view, String title)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("pageTitle", title);
        request.setAttribute("contentPage", view);
        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }
}
