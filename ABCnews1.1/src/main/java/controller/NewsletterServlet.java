package controller;

import dao.daoimpl.NewsletterDAOImpl;
import dao.daoimpl.UserDAOImpl;
import model.Newsletter;
import model.NewsletterView;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/newsletter")
public class NewsletterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final NewsletterDAOImpl newsletterDAO = new NewsletterDAOImpl();
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);
        HttpSession session = request.getSession(false);

        String role = (session != null) ? String.valueOf(session.getAttribute("role")) : null;
        String currentUserId = (session != null && session.getAttribute("user") != null)
                ? ((User) session.getAttribute("user")).getId()
                : null;
        String currentUserEmail = (session != null && session.getAttribute("user") != null)
                ? ((User) session.getAttribute("user")).getEmail()
                : null;

        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String authorId = request.getParameter("authorId"); // 👈 lấy authorId từ URL khi edit/filter

        try {
            // 📩 SUBSCRIBER hoặc khách: form đăng ký + danh sách đang theo dõi
            if (role == null || "2".equals(role)) {
                loadSubscriberData(request, currentUserEmail);
                forward(request, response, "📩 Đăng ký nhận tin", "/views/public/subscribe.jsp");
                return;
            }

            // 👑 ADMIN
            if ("0".equals(role)) {
                handleAdminGet(request, response, action, email, authorId);
                return;
            }

            // ✍️ EDITOR
            if ("1".equals(role)) {
                List<Newsletter> followers = newsletterDAO.findFollowersByAuthor(currentUserId);
                request.setAttribute("followers", followers);
                forward(request, response, "👥 Người theo dõi bạn", "/views/editor/followers.jsp");
                return;
            }

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập vào trang này.");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Lỗi tải dữ liệu newsletter.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);
        HttpSession session = request.getSession(false);

        String role = (session != null) ? String.valueOf(session.getAttribute("role")) : null;
        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String authorId = request.getParameter("authorId");
        boolean enabled = Boolean.parseBoolean(request.getParameter("enabled"));

        try {
            // 📩 SUBSCRIBER: đăng ký / hủy theo dõi tác giả
            if (role == null || "2".equals(role)) {
                if ("subscribe".equals(action)) {
                    boolean ok = newsletterDAO.subscribeOrReactivate(email, authorId);
                    session.setAttribute("message", ok ? "📬 Đăng ký thành công!" : "⚠️ Không thể đăng ký.");
                } else if ("unsubscribe".equals(action)) {
                    boolean ok = newsletterDAO.toggleSubscription(email, authorId, false);
                    session.setAttribute("message", ok ? "❌ Hủy đăng ký thành công." : "⚠️ Không thể hủy đăng ký.");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                    return;
                }

                response.sendRedirect("newsletter");
                return;
            }

            // 👑 ADMIN
            if ("0".equals(role)) {
                switch (action) {
                    case "create":
                        handleCreate(request, email, authorId);
                        break;
                    case "update":
                        handleUpdate(request, email, enabled, authorId);
                        break;
                    case "delete":
                        handleDelete(request, email, authorId);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                        return;
                }
                response.sendRedirect("newsletter");
                return;
            }

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện thao tác này.");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Lỗi xử lý newsletter.");
        }
    }

    // ================== HANDLERS ==================

    private void handleAdminGet(HttpServletRequest request, HttpServletResponse response, String action,
                                String email, String authorId)
            throws ServletException, IOException, SQLException {

        if ("create".equals(action)) {
            request.setAttribute("authors", userDAO.findAllAuthors());
            forward(request, response, "➕ Thêm người đăng ký", "/views/manager/newsletter-form.jsp");
            return;
        }

        if ("edit".equals(action) && email != null && authorId != null) {
            Newsletter subscriber = newsletterDAO.findByEmailAndAuthor(email, authorId);
            if (subscriber == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy người đăng ký với email và tác giả này.");
                return;
            }
            request.setAttribute("subscriber", subscriber);
            request.setAttribute("authors", userDAO.findAllAuthors());
            forward(request, response, "✏️ Cập nhật đăng ký", "/views/manager/newsletter-form.jsp");
            return;
        }

        // 📬 Danh sách tất cả người đăng ký (lọc theo tác giả nếu có)
        List<NewsletterView> subscribers = (authorId != null && !authorId.isEmpty())
                ? newsletterDAO.findAllWithAuthor(authorId)
                : newsletterDAO.findAllWithAuthor();
        request.setAttribute("subscribers", subscribers);
        request.setAttribute("authors", userDAO.findAllAuthors());

        forward(request, response, "📨 Quản lý đăng ký nhận tin", "/views/manager/manage-newsletters.jsp");
    }

    private void handleCreate(HttpServletRequest request, String email, String authorId) {
        boolean ok = newsletterDAO.subscribeOrReactivate(email, authorId);
        setFlashMessage(request, ok, "✅ Thêm đăng ký thành công!", "⚠️ Không thể thêm đăng ký.");
    }

    private void handleUpdate(HttpServletRequest request, String email, boolean enabled, String authorId) {
        boolean ok = newsletterDAO.toggleSubscription(email, authorId, enabled);
        setFlashMessage(request, ok, "✅ Cập nhật thành công!", "⚠️ Cập nhật thất bại.");
    }

    private void handleDelete(HttpServletRequest request, String email, String authorId) {
        Newsletter existing = newsletterDAO.findByEmailAndAuthor(email, authorId);
        boolean ok = false;
        if (existing != null) {
            ok = newsletterDAO.delete(email); // 📝 nếu bạn muốn xóa tất cả bản ghi email, giữ dòng này
            // hoặc chỉ xóa theo tác giả cụ thể ➜ tạo hàm deleteByEmailAndAuthor(email, authorId)
        }
        setFlashMessage(request, ok, "🗑️ Đã xóa đăng ký.", "⚠️ Không thể xóa đăng ký.");
    }

    // ================== UTILITIES ==================

    private void setupEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String title, String page)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", title);
        request.setAttribute("contentPage", page);
        request.getRequestDispatcher("/layout.jsp").forward(request, response);
    }

    private void setFlashMessage(HttpServletRequest request, boolean ok, String successMsg, String errorMsg) {
        if (ok) request.getSession().setAttribute("message", successMsg);
        else request.getSession().setAttribute("error", errorMsg);
    }

    private void loadSubscriberData(HttpServletRequest request, String email) throws SQLException {
        request.setAttribute("authors", userDAO.findAllAuthors());
        if (email != null) {
            request.setAttribute("followedAuthors", newsletterDAO.findFollowedAuthorsByEmail(email));
        }
    }
    
 // ================== SEND EMAIL NOTIFY ==================
    private void sendNewPostNotification(String authorId, model.News news, HttpServletRequest request) {
        List<String> subscribers = newsletterDAO.findEmailsByAuthor(authorId);
        if (subscribers.isEmpty()) {
            System.out.println("📭 Không có người theo dõi để gửi email.");
            return;
        }

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String link = baseUrl + "/news?action=view&id=" + news.getId();
        String subject = "📢 Bài viết mới từ tác giả bạn theo dõi: " + news.getAuthor();

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
                utils.MailUtils.sendEmail(email, subject, html);
                System.out.println("✅ Đã gửi thông báo tới: " + email);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("❌ Gửi email thất bại tới: " + email);
            }
        }
    }

}
