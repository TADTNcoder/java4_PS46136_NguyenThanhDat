package filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();

        // ✅ 1. Cho phép truy cập tài nguyên công khai
        if (isPublicResource(uri)) {
            chain.doFilter(req, res);
            return;
        }

        // ✅ 2. Nếu đã đăng nhập rồi mà vào /login → chuyển về dashboard
        if (uri.endsWith("/login") && session != null && session.getAttribute("email") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // ✅ 3. Kiểm tra đăng nhập dựa theo email
        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ✅ 4. Lấy quyền người dùng (giữ dạng chuỗi)
        String role = (String) session.getAttribute("role");
        if (role == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ✅ 5. Phân quyền truy cập
        if (uri.contains("/admin")) {
            // ❗ Chỉ admin mới có quyền
            if (!"0".equals(role)) {
                denyAccess(response, "❌ Chỉ quản trị viên mới có quyền truy cập trang này.");
                return;
            }
        } else if (uri.contains("/editor")) {
            // ❗ Admin (0) và Editor (1) có quyền
            if (!("0".equals(role) || "1".equals(role))) {
                denyAccess(response, "✍️ Chỉ tác giả hoặc quản trị viên mới có quyền truy cập trang này.");
                return;
            }
        } else if (uri.contains("/subscriber")) {
            // ❗ Admin (0), Editor (1), Subscriber (2) đều có quyền
            if (!("0".equals(role) || "1".equals(role) || "2".equals(role))) {
                denyAccess(response, "📚 Bạn không có quyền truy cập trang này.");
                return;
            }
        }

        // ✅ 6. Cho phép đi tiếp nếu mọi thứ hợp lệ
        chain.doFilter(req, res);
    }

    /**
     * ✅ Cho phép truy cập tài nguyên công khai (không yêu cầu đăng nhập)
     */
    private boolean isPublicResource(String uri) {
        return uri.endsWith("login")
                || uri.endsWith("register")
                || uri.contains("/news")
                || uri.contains("/categories")
                || uri.contains("/newsletter")
                || uri.contains("/assets/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg");
    }

    /**
     * ✅ Gửi lỗi 403 khi không đủ quyền truy cập
     */
    private void denyAccess(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
