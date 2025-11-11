package controller;

import dao.daoimpl.UserDAOImpl;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private final UserDAOImpl userDAO = new UserDAOImpl();

    private static final int ROLE_ADMIN = 0;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        copyFlashMessage(session, request);
        if (session != null) {
            String csrfToken = ensureCsrfToken(session);
            request.setAttribute("csrfToken", csrfToken);
        }

        try {
            // 👤 Xem hoặc sửa thông tin cá nhân (KHÔNG CẦN ĐĂNG NHẬP)
            if ("detailSelf".equals(action) || "editSelf".equals(action)) {
                User self = findCurrentUserFromSession(session);

                // Nếu chưa đăng nhập thì lấy theo ID từ URL
                if (self == null && notBlank(id)) {
                    self = userDAO.findById(id);
                }

                // Nếu không có session và không có id → redirect về /users
                if (self == null) {
                    response.sendRedirect("users");
                    return;
                }

                request.setAttribute("user", self);
                if ("detailSelf".equals(action)) {
                    forward(request, response, "👤 Thông tin cá nhân", "/views/public/user-detail.jsp");
                } else {
                    forward(request, response, "✏️ Cập nhật thông tin cá nhân", "/views/public/user-form.jsp");
                }
                return;
            }

            // 👥 Nếu không phải admin mà truy cập trang quản lý → chuyển sang trang cá nhân
            String roleStr = (session != null) ? Objects.toString(session.getAttribute("role"), "").trim() : "";
            if (!String.valueOf(ROLE_ADMIN).equals(roleStr)) {
                response.sendRedirect("users?action=detailSelf");
                return;
            }

            // 🛠️ Trang tạo người dùng mới
            if ("create".equals(action)) {
                request.removeAttribute("user");
                forward(request, response, "➕ Thêm người dùng mới", "/views/manager/user-form.jsp");
                return;
            }

            // 🛠️ Trang sửa người dùng
            if ("edit".equals(action) && notBlank(id)) {
                User user = userDAO.findById(id);
                if (user == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy người dùng.");
                    return;
                }
                request.setAttribute("user", user);
                forward(request, response, "✏️ Cập nhật người dùng", "/views/manager/user-form.jsp");
                return;
            }

            // 🛠️ Trang chi tiết người dùng
            if ("detail".equals(action) && notBlank(id)) {
                User user = userDAO.findById(id);
                if (user == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy người dùng.");
                    return;
                }
                request.setAttribute("user", user);
                forward(request, response, "👤 Chi tiết người dùng", "/views/manager/user-detail.jsp");
                return;
            }

            // 📦 Tìm kiếm người dùng
            if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                request.setAttribute("users", userDAO.searchByName(keyword));
                request.setAttribute("keyword", keyword);
            } else {
                request.setAttribute("users", userDAO.findAll());
            }

            forward(request, response, "👥 Quản lý người dùng", "/views/manager/manage-users.jsp");

        } catch (Exception e) {
            logger.error("❌ Lỗi khi xử lý GET /users: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi tải người dùng.");
        }
    }

    // ===================== POST =====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setupEncoding(request, response);
        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");

        if (!notBlank(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hành động cần thực hiện.");
            return;
        }

        try {
            // ✅ Phân quyền: CRUD vẫn cần admin
            if (Arrays.asList("create", "update", "delete", "resetPassword").contains(action)) {
                if (!String.valueOf(ROLE_ADMIN).equals(Objects.toString(session.getAttribute("role"), ""))) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thao tác người dùng.");
                    return;
                }
            }

            if (requiresCsrf(action) && !verifyCsrfToken(session, request.getParameter("csrfToken"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF verification failed.");
                return;
            }

            switch (action) {
                case "create": handleCreate(request, session); break;
                case "update": handleUpdate(request, session); break;
                case "delete": handleDelete(request, session); break;
                case "resetPassword": handleResetPassword(request, session); break;
                case "updateSelf": handleUpdateSelf(request, session); break;
                default:
                    session.setAttribute("error", "❌ Hành động không hợp lệ.");
                    break;
            }

            response.sendRedirect("updateSelf".equals(action) ? "users?action=detailSelf" : "users");

        } catch (Exception e) {
            logger.error("❌ Lỗi xử lý POST /users: ", e);
            session.setAttribute("error", "❌ Có lỗi xảy ra khi xử lý yêu cầu.");
            response.sendRedirect("users");
        }
    }

    // ================== CRUD HANDLERS ==================
    private boolean handleCreate(HttpServletRequest request, HttpSession session) {
        User user = buildUserFromRequest(request, true);
        String validationError = validateUser(user, true);
        if (validationError != null) {
            session.setAttribute("error", validationError);
            return false;
        }
        if (userDAO.findByEmail(user.getEmail()) != null) {
            session.setAttribute("error", "⚠️ Email này đã tồn tại trong hệ thống.");
            return false;
        }

        user.setId(UUID.randomUUID().toString());
        user.setPassword(hashPassword(user.getPassword()));
        if (user.getRole() < 0 || user.getRole() > 2) user.setRole(2);

        boolean ok = userDAO.insert(user);
        session.setAttribute(ok ? "message" : "error",
                ok ? "✅ Thêm người dùng mới thành công!" : "⚠️ Không thể thêm người dùng mới.");
        return ok;
    }

    private boolean handleUpdate(HttpServletRequest request, HttpSession session) {
        String id = request.getParameter("id");
        if (!notBlank(id)) {
            session.setAttribute("error", "⚠️ Thiếu ID người dùng để cập nhật.");
            return false;
        }

        User user = userDAO.findById(id);
        if (user == null) {
            session.setAttribute("error", "⚠️ Không tìm thấy người dùng để cập nhật.");
            return false;
        }

        String newEmail = request.getParameter("email");
        if (notBlank(newEmail) && !newEmail.equalsIgnoreCase(user.getEmail())) {
            User existing = userDAO.findByEmail(newEmail);
            if (existing != null && !Objects.equals(existing.getId(), id)) {
                session.setAttribute("error", "⚠️ Email này đã tồn tại trong hệ thống.");
                return false;
            }
        }

        updateUserFromRequest(request, user);
        String newPassword = request.getParameter("password");
        if (notBlank(newPassword)) user.setPassword(hashPassword(newPassword));

        String validationError = validateUser(user, false);
        if (validationError != null) {
            session.setAttribute("error", validationError);
            return false;
        }

        userDAO.update(user);
        session.setAttribute("message", "✅ Cập nhật người dùng thành công!");
        return true;
    }

    private void handleDelete(HttpServletRequest request, HttpSession session) {
        String id = request.getParameter("id");
        if (!notBlank(id)) {
            session.setAttribute("error", "⚠️ Thiếu ID để xóa.");
            return;
        }
        userDAO.delete(id);
        session.setAttribute("message", "🗑️ Đã xóa người dùng thành công!");
    }

    private void handleResetPassword(HttpServletRequest request, HttpSession session) {
        String id = request.getParameter("id");
        if (!notBlank(id)) {
            session.setAttribute("error", "⚠️ Thiếu ID để đặt lại mật khẩu.");
            return;
        }
        userDAO.updatePassword(id, hashPassword("123456"));
        session.setAttribute("message", "🔑 Mật khẩu đã được đặt lại về mặc định (123456)!");
    }

    private boolean handleUpdateSelf(HttpServletRequest request, HttpSession session) {
        User self = findCurrentUserFromSession(session);
        if (self == null) {
            session.setAttribute("error", "⚠️ Phiên đăng nhập không hợp lệ.");
            return false;
        }

        String newEmail = request.getParameter("email");
        if (notBlank(newEmail) && !newEmail.equalsIgnoreCase(self.getEmail())) {
            User existing = userDAO.findByEmail(newEmail);
            if (existing != null && !Objects.equals(existing.getId(), self.getId())) {
                session.setAttribute("error", "⚠️ Email này đã tồn tại trong hệ thống.");
                return false;
            }
        }

        updateUserFromRequest(request, self);
        String newPassword = request.getParameter("password");
        if (notBlank(newPassword)) self.setPassword(hashPassword(newPassword));

        String validationError = validateUser(self, false);
        if (validationError != null) {
            session.setAttribute("error", validationError);
            return false;
        }

        userDAO.update(self);
        if (notBlank(newEmail)) session.setAttribute("email", self.getEmail());
        session.setAttribute("message", "✅ Đã cập nhật thông tin cá nhân!");
        return true;
    }

    // ================== UTILITIES ==================
    private void copyFlashMessage(HttpSession session, HttpServletRequest request) {
        if (session == null) return;
        Object msg = session.getAttribute("message");
        Object err = session.getAttribute("error");
        if (msg != null) { request.setAttribute("message", msg); session.removeAttribute("message"); }
        if (err != null) { request.setAttribute("error", err); session.removeAttribute("error"); }
    }

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

    private User buildUserFromRequest(HttpServletRequest request, boolean isNew) {
        User user = new User();
        user.setFullname(safeTrim(request.getParameter("fullname")));
        user.setEmail(safeTrim(request.getParameter("email")));
        user.setPassword(isNew ? safeTrim(request.getParameter("password")) : null);
        user.setGender(parseGender(request.getParameter("gender")));
        user.setMobile(safeTrim(request.getParameter("mobile")));
        user.setRole(parseRoleOrDefault(request.getParameter("role"), 2));

        String birthdayStr = request.getParameter("birthday");
        if (notBlank(birthdayStr)) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                user.setBirthday(df.parse(birthdayStr));
            } catch (ParseException ignored) {}
        }
        return user;
    }

    private void updateUserFromRequest(HttpServletRequest request, User user) {
        String fullname = safeTrim(request.getParameter("fullname"));
        if (fullname != null) user.setFullname(fullname);
        String email = safeTrim(request.getParameter("email"));
        if (email != null) user.setEmail(email);
        user.setGender(parseGender(request.getParameter("gender")));
        String mobile = safeTrim(request.getParameter("mobile"));
        if (mobile != null) user.setMobile(mobile);

        String birthdayStr = request.getParameter("birthday");
        if (notBlank(birthdayStr)) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                user.setBirthday(df.parse(birthdayStr));
            } catch (ParseException ignored) {}
        }

        String roleParam = request.getParameter("role");
        if (notBlank(roleParam)) user.setRole(parseRoleOrDefault(roleParam, user.getRole()));
    }

    private String validateUser(User user, boolean isNew) {
        if (!notBlank(user.getFullname())) return "⚠️ Họ tên không được để trống.";
        if (!notBlank(user.getEmail())) return "⚠️ Email không được để trống.";
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) return "⚠️ Email không hợp lệ.";
        if (isNew && !notBlank(user.getPassword())) return "⚠️ Mật khẩu không được để trống.";
        if (notBlank(user.getMobile()) && !user.getMobile().matches("^\\d{9,11}$")) return "⚠️ Số điện thoại không hợp lệ.";
        if (user.getRole() < 0 || user.getRole() > 2) return "⚠️ Vai trò không hợp lệ.";
        return null;
    }

    private boolean parseGender(String genderParam) {
        if (genderParam == null) return false;
        switch (genderParam.trim().toLowerCase(Locale.ROOT)) {
            case "male": case "nam": case "true": case "1": case "on": return true;
            case "female": case "nu": case "false": case "0": case "off": return false;
            default: return Boolean.parseBoolean(genderParam);
        }
    }

    private int parseRoleOrDefault(String roleParam, int def) {
        try { return Integer.parseInt(roleParam.trim()); } catch (Exception e) { return def; }
    }

    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    // ================== CSRF ==================
    private boolean requiresCsrf(String action) {
        return Arrays.asList("create", "update", "delete", "resetPassword", "updateSelf").contains(action);
    }

    private String ensureCsrfToken(HttpSession session) {
        String token = (String) session.getAttribute("csrfToken");
        if (!notBlank(token)) {
            token = generateToken();
            session.setAttribute("csrfToken", token);
        }
        return token;
    }

    private boolean verifyCsrfToken(HttpSession session, String tokenFromRequest) {
        if (session == null || !notBlank(tokenFromRequest)) return false;
        String tokenInSession = (String) session.getAttribute("csrfToken");
        return notBlank(tokenInSession) && tokenInSession.equals(tokenFromRequest);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // ================== SESSION HELPERS ==================
    private User findCurrentUserFromSession(HttpSession session) {
        if (session == null) return null;
        Object userObj = session.getAttribute("user");
        if (userObj instanceof User) return (User) userObj;

        Object emailObj = session.getAttribute("email");
        if (emailObj != null && !emailObj.toString().trim().isEmpty())
            return userDAO.findByEmail(emailObj.toString().trim());

        Object userIdObj = session.getAttribute("userId");
        if (userIdObj != null && !userIdObj.toString().trim().isEmpty())
            return userDAO.findById(userIdObj.toString().trim());

        return null;
    }

    private String hashPassword(String plain) {
        return plain == null ? null : BCrypt.hashpw(plain, BCrypt.gensalt());
    }
}
