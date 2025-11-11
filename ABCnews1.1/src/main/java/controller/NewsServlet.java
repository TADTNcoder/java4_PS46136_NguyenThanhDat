package controller;

import dao.daoimpl.NewsDAOImpl;
import dao.daoimpl.NewsletterDAOImpl;
import dao.daoimpl.CategoryDAOImpl;
import model.News;
import model.User;
import utils.MailUtils;
import model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/news")
@MultipartConfig()
public class NewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(NewsServlet.class.getName());
    private final NewsDAOImpl newsDAO = new NewsDAOImpl();
    private final CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    private enum Role { ADMIN, EDITOR, READER }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);
        HttpSession session = request.getSession(true);
        User currentUser = (User) session.getAttribute("user");
        Role role = resolveRole(session, currentUser);
        ensureCsrfToken(session);

        String action = safe(request.getParameter("action"));
        String id = safe(request.getParameter("id"));

        int page = parseIntOrDefault(request.getParameter("page"), 1, 1, 100);
        int size = parseIntOrDefault(request.getParameter("size"), 6, 5, 50);

        try {
            switch (role) {
                case ADMIN -> handleAdminGet(request, response, action, id, page, size);
                case EDITOR -> handleEditorGet(request, response, action, currentUser, id, page, size);
                case READER -> handleReaderGet(request, response, action, id, page, size);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GET /news failed", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Lỗi tải dữ liệu bài viết.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Phiên đã hết hạn.");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        Role role = resolveRole(session, currentUser);

        if (!validateCsrfToken(session, request.getParameter("_csrf"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token không hợp lệ.");
            return;
        }

        String action = safe(request.getParameter("action"));
        String id = safe(request.getParameter("id"));

        try {
            switch (role) {
                case ADMIN -> handleAdminPost(request, response, action, id);
                case EDITOR -> handleEditorPost(request, response, action, id, currentUser);
                default -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện thao tác này.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "POST /news failed", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Lỗi xử lý bài viết.");
        }
    }

    // ================= ADMIN =================

private void handleAdminGet(HttpServletRequest req, HttpServletResponse res, String action,
                            String id, int page, int size)
        throws ServletException, IOException, SQLException {

    if ("create".equals(action)) {
        req.setAttribute("categories", categoryDAO.findAll());
        forward(req, res, "➕ Thêm bài viết", "/views/manager/news-form.jsp");
        return;
    }

    if ("edit".equals(action) && id != null) {
        newsDAO.findById(id).ifPresentOrElse(
                news -> req.setAttribute("news", news),
                () -> req.setAttribute("error", "❌ Không tìm thấy bài viết")
        );
        req.setAttribute("categories", categoryDAO.findAll());
        forward(req, res, "✏️ Sửa bài viết", "/views/manager/news-form.jsp");
        return;
    }

    if ("view".equals(action) && id != null) {
        newsDAO.incrementViewCount(id);
        newsDAO.findById(id).ifPresentOrElse(
            news -> {
                req.setAttribute("news", news);
                req.setAttribute("related", newsDAO.findRelated(news.getCategoryId(), news.getId(), 5));
            },
            () -> req.setAttribute("error", "❌ Không tìm thấy bài viết")
        );
        forward(req, res, "📖 Chi tiết bài viết", "/views/public/news-detail.jsp");
        return;
    }

    // ✅ Lấy các tham số lọc và tìm kiếm từ request
    String keyword = req.getParameter("keyword");
    String categoryId = req.getParameter("categoryId");

    List<News> newsList;
    long total;

    // ✅ Ưu tiên tìm kiếm theo từ khóa (tiêu đề hoặc tác giả)
    if (keyword != null && !keyword.isBlank()) {
        newsList = newsDAO.searchByTitleOrAuthor(keyword, page, size);
        total = newsDAO.countSearchByTitleOrAuthor(keyword);
        req.setAttribute("searchKeyword", keyword);
    }
    // ✅ Nếu không có từ khóa mà có chuyên mục => lọc theo chuyên mục
    else if (categoryId != null && !categoryId.isBlank()) {
        newsList = newsDAO.findByCategory(categoryId, page, size);
        total = newsDAO.countByCategory(categoryId);
        req.setAttribute("activeCategoryId", categoryId);
    }
    // ✅ Nếu không lọc gì → hiển thị toàn bộ bài viết
    else {
        newsList = newsDAO.findAllPaged(page, size);
        total = newsDAO.countAll();
    }

    // ✅ Gán dữ liệu cho JSP
    req.setAttribute("newsList", newsList);
    req.setAttribute("total", total);
    req.setAttribute("page", page);
    req.setAttribute("size", size);
    req.setAttribute("categories", categoryDAO.findAll());

    forward(req, res, "📚 Quản lý bài viết", "/views/manager/news-list.jsp");
}


    private void handleAdminPost(HttpServletRequest req, HttpServletResponse res, String action, String id)
            throws IOException, ServletException {
        switch (action) {
        case "create" -> {
            News news = buildNewsFromRequest(req);
            boolean ok = newsDAO.insert(news);
            setFlash(req, ok, "✅ Tạo bài viết thành công!", "⚠️ Không thể tạo bài viết.");

            if (ok) {
                // 📬 Gửi thông báo tới người theo dõi tác giả
                notifySubscribers(news, req);
            }
        }

            case "update" -> {
                News news = buildNewsFromRequest(req);
                news.setId(id);
                boolean ok = newsDAO.update(news);
                setFlash(req, ok, "✅ Cập nhật thành công!", "⚠️ Không thể cập nhật.");
            }
            case "delete" -> {
                boolean ok = newsDAO.delete(id);
                setFlash(req, ok, "🗑️ Đã xóa bài viết.", "⚠️ Không thể xóa bài viết.");
            }
            default -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
        res.sendRedirect("news");
    }

    // ================= EDITOR =================

 private void handleEditorGet(HttpServletRequest req, HttpServletResponse res, String action,
                             User currentUser, String id, int page, int size)
        throws ServletException, IOException {

    String authorId = currentUser.getId();

    if ("create".equals(action)) {
        req.setAttribute("categories", categoryDAO.findAll());
        forward(req, res, "✍️ Viết bài mới", "/views/manager/news-form.jsp");
        return;
    }

    if ("edit".equals(action) && id != null) {
        if (!newsDAO.isAuthorOf(id, authorId)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền sửa bài này.");
            return;
        }
        newsDAO.findById(id).ifPresentOrElse(
                news -> req.setAttribute("news", news),
                () -> req.setAttribute("error", "Không tìm thấy bài viết")
        );
        req.setAttribute("categories", categoryDAO.findAll());
        forward(req, res, "✏️ Cập nhật bài viết", "/views/manager/news-form.jsp");
        return;
    }

    if ("view".equals(action) && id != null) {
        newsDAO.incrementViewCount(id);
        newsDAO.findById(id).ifPresentOrElse(
                news -> {
                    req.setAttribute("news", news);
                    req.setAttribute("related", newsDAO.findRelated(news.getCategoryId(), news.getId(), 5));
                },
                () -> req.setAttribute("error", "❌ Không tìm thấy bài viết")
        );
        forward(req, res, "📖 Chi tiết bài viết", "/views/public/news-detail.jsp");
        return;
    }

    // ✅ Lọc & tìm kiếm bài viết của chính tác giả
    String categoryId = req.getParameter("categoryId");
    String keyword = req.getParameter("keyword");

    List<News> newsList;
    long total;

    // 🔎 Ưu tiên tìm kiếm từ khóa trước (trong phạm vi bài của tác giả)
    if (keyword != null && !keyword.isBlank()) {
        newsList = newsDAO.searchByAuthorAndKeyword(authorId, keyword, page, size);
        total = newsDAO.countSearchByAuthorAndKeyword(authorId, keyword);
        req.setAttribute("searchKeyword", keyword);
    }
    // 📂 Nếu không có từ khóa mà có categoryId -> lọc theo chuyên mục
    else if (categoryId != null && !categoryId.isBlank()) {
        newsList = newsDAO.findByAuthorAndCategory(authorId, categoryId, page, size);
        total = newsDAO.countByAuthorAndCategory(authorId, categoryId);
        req.setAttribute("activeCategoryId", categoryId);
    }
    // 📄 Không có gì -> hiển thị toàn bộ bài viết của tác giả
    else {
        newsList = newsDAO.findByAuthor(authorId, page, size);
        total = newsDAO.countByAuthor(authorId);
    }

    req.setAttribute("newsList", newsList);
    req.setAttribute("total", total);
    req.setAttribute("categories", categoryDAO.findAll());
    req.setAttribute("page", page);
    req.setAttribute("size", size);

    forward(req, res, "📚 Bài viết của bạn", "/views/editor/my-articles.jsp");
}



    private void handleEditorPost(HttpServletRequest req, HttpServletResponse res,
                                  String action, String id, User currentUser) throws IOException, ServletException {
        String authorId = currentUser.getId();
        switch (action) {
        case "create" -> {
            News news = buildNewsFromRequest(req);
            news.setAuthor(currentUser.getId()); 
            boolean ok = newsDAO.insert(news);
            setFlash(req, ok, "✅ Tạo bài viết thành công!", "⚠️ Không thể tạo bài viết.");

            if (ok) {
                notifySubscribers(news, req);
            }
        }

            case "update" -> {
                if (!newsDAO.isAuthorOf(id, authorId)) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền sửa bài này.");
                    return;
                }
                News news = buildNewsFromRequest(req);
                news.setId(id);
                news.setAuthor(authorId);
                boolean ok = newsDAO.update(news);
                setFlash(req, ok, "✅ Cập nhật thành công!", "⚠️ Không thể cập nhật.");
            }
            case "delete" -> {
                if (!newsDAO.isAuthorOf(id, authorId)) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xóa bài này.");
                    return;
                }
                boolean ok = newsDAO.delete(id);
                setFlash(req, ok, "🗑️ Đã xóa bài viết.", "⚠️ Không thể xóa bài viết.");
            }
            default -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
        res.sendRedirect("news");
    }

    // ================= READER =================

    private void handleReaderGet(HttpServletRequest req, HttpServletResponse res, String action,
                             String id, int page, int size)
        throws ServletException, IOException {

    if ("view".equals(action) && id != null) {
        newsDAO.incrementViewCount(id);
        newsDAO.findById(id).ifPresentOrElse(
                news -> {
                    req.setAttribute("news", news);
                    req.setAttribute("related", newsDAO.findRelated(news.getCategoryId(), news.getId(), 5));
                },
                () -> req.setAttribute("error", "❌ Không tìm thấy bài viết")
        );
        forward(req, res, "📖 Chi tiết bài viết", "/views/public/news-detail.jsp");
        return;
    }

    // ✅ Thêm xử lý lọc theo categoryId nếu có
    String categoryId = req.getParameter("categoryId");
    if (categoryId != null && !categoryId.isBlank()) {
        req.setAttribute("newsList", newsDAO.findByCategory(categoryId, page, size));
        req.setAttribute("total", newsDAO.countByCategory(categoryId));
        req.setAttribute("activeCategoryId", categoryId); // dùng highlight trong JSP nếu muốn
    } else {
        req.setAttribute("newsList", newsDAO.findAllPaged(page, size));
        req.setAttribute("total", newsDAO.countAll());
    }

    req.setAttribute("page", page);
    req.setAttribute("size", size);

    // 📌 Gán danh sách chuyên mục để dropdown không lỗi
    req.setAttribute("categories", categoryDAO.findAll());

    forward(req, res, "📰 Tin tức mới nhất", "/views/public/news-list.jsp");
}


    // ================= UTILITIES =================

    private void setupEncoding(HttpServletRequest req, HttpServletResponse res) {
        try { req.setCharacterEncoding("UTF-8"); } catch (Exception ignored) {}
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
    }

    private Role resolveRole(HttpSession session, User user) {
        Object roleObj = (user != null) ? user.getRole() : session.getAttribute("role");
        String roleStr = (roleObj != null) ? roleObj.toString() : "";

        return switch (roleStr) {
            case "0", "ADMIN", "admin" -> Role.ADMIN;
            case "1", "EDITOR", "editor" -> Role.EDITOR;
            default -> Role.READER;
        };
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String title, String page)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", title);
        req.setAttribute("contentPage", page);
        req.getRequestDispatcher("/layout.jsp").forward(req, res);
    }

    private void ensureCsrfToken(HttpSession session) {
        if (session.getAttribute("_csrf") == null) {
            session.setAttribute("_csrf", UUID.randomUUID().toString());
        }
    }

    private boolean validateCsrfToken(HttpSession session, String token) {
        return session.getAttribute("_csrf") != null && Objects.equals(session.getAttribute("_csrf"), token);
    }

    private void setFlash(HttpServletRequest req, boolean ok, String success, String error) {
        HttpSession session = req.getSession();
        if (ok) session.setAttribute("message", success);
        else session.setAttribute("error", error);
    }

    // ================= BUILD NEWS (UPLOAD FIX) =================

    private News buildNewsFromRequest(HttpServletRequest req) throws IOException, ServletException {
    News n = new News();
    n.setId(Optional.ofNullable(req.getParameter("id")).orElse(UUID.randomUUID().toString()));
    n.setTitle(safe(req.getParameter("title")));
    n.setContent(safe(req.getParameter("content")));
    n.setPostedDate(new Date());
    n.setCategoryId(safe(req.getParameter("categoryId")));
    n.setHome("on".equals(req.getParameter("home")));
    n.setViewCount(0);

    try {
        Part imagePart = req.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0) {
            String contentType = imagePart.getContentType();
            if (!contentType.contains("jpeg") && !contentType.contains("png") && !contentType.contains("webp")) {
                throw new ServletException("❌ Định dạng ảnh không hợp lệ. Chỉ hỗ trợ JPG, PNG, WEBP.");
            }

            // ✅ Lấy đường dẫn thư mục "uploads" trong webapp đang chạy (Tomcat)
            String uploadPath = req.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // ✅ Tạo tên file duy nhất để tránh trùng
            String originalName = new File(imagePart.getSubmittedFileName()).getName().toLowerCase();
            String fileName = UUID.randomUUID() + "_" + originalName;
            File fileSave = new File(uploadDir, fileName);

            // ✅ Ghi file trực tiếp vào thư mục deploy
            imagePart.write(fileSave.getAbsolutePath());
            LOGGER.info("📸 Ảnh đã được lưu tại: " + fileSave.getAbsolutePath());

            // ✅ Lưu đường dẫn ảnh để hiển thị trên web
            n.setImage("/uploads/" + fileName);
        } else {
            // ✅ Nếu không upload ảnh mới -> giữ ảnh cũ
            n.setImage(safe(req.getParameter("existingImage")));
        }
    } catch (Exception e) {
        LOGGER.log(Level.WARNING, "⚠️ Lỗi xử lý ảnh upload", e);
        n.setImage(safe(req.getParameter("existingImage")));
    }

    // ✅ Gắn tác giả nếu đang đăng nhập
    HttpSession session = req.getSession(false);
    if (session != null) {
        User u = (User) session.getAttribute("user");
        if (u != null && n.getAuthor() == null) {
            n.setAuthor(u.getId());
        }
    }

    return n;
}


    private static String safe(String s) {
        return s == null ? null : s.trim();
    }

    private static int parseIntOrDefault(String raw, int def, int min, int max) {
        try {
            int v = Integer.parseInt(raw);
            return Math.min(Math.max(v, min), max);
        } catch (Exception e) {
            return def;
        }
    }
 // 📬 Gửi email thông báo bài viết mới tới người theo dõi tác giả
    private void notifySubscribers(News news, HttpServletRequest req) {
        NewsletterDAOImpl newsletterDAO = new NewsletterDAOImpl();
        List<String> subscribers = newsletterDAO.findEmailsByAuthor(news.getAuthor());

        if (subscribers.isEmpty()) {
            System.out.println("📭 Không có người theo dõi để gửi email.");
            return;
        }

        // 🔗 Tạo link xem bài viết
        String baseUrl = req.getRequestURL().toString().replace(req.getRequestURI(), req.getContextPath());
        String link = baseUrl + "/news?action=view&id=" + news.getId();

        String subject = "📢 Bài viết mới từ tác giả bạn theo dõi: " + news.getAuthor();

        // 📄 Lấy đoạn nội dung ngắn gửi email
        String preview = news.getContent() != null && news.getContent().length() > 150
                ? news.getContent().substring(0, 150) + "..."
                : news.getContent();

        String html = """
            <h2>📢 Bài viết mới từ tác giả bạn theo dõi!</h2>
            <p><strong>%s</strong></p>
            <p>%s</p>
            <p><a href="%s">👉 Đọc bài viết tại đây</a></p>
        """.formatted(news.getTitle(), preview, link);

        for (String email : subscribers) {
            try {
                MailUtils.sendEmail(email, subject, html);
                System.out.println("✅ Đã gửi email tới: " + email);
            } catch (Exception e) {
                System.err.println("❌ Gửi email thất bại tới: " + email);
                e.printStackTrace();
            }
        }

        System.out.println("📨 Đã gửi email tới " + subscribers.size() + " người theo dõi tác giả " + news.getAuthor());
    }

}
