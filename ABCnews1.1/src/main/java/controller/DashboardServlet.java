package controller;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.daoimpl.NewsDAOImpl;
import dao.daoimpl.CategoryDAOImpl;
import model.News;
import model.Category;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
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

		// 🚨 Nếu chưa đăng nhập → quay về trang login
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		Object userObj = session.getAttribute("user");
		if (userObj == null) {
			// Hết hạn phiên đăng nhập → xoá session & về login
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// 📂 Luôn nạp danh sách chuyên mục để navbar hoạt động
		List<Category> categories = categoryDAO.findAll();
		request.setAttribute("categories", categories);

		// ✅ Lấy quyền dưới dạng String (chắc chắn không null)
		String role = "";
		Object roleAttr = session.getAttribute("role");
		if (roleAttr instanceof String) {
			role = (String) roleAttr;
		} else if (roleAttr != null) {
			role = roleAttr.toString();
		}

		System.out.println("🔑 ROLE from session = " + role);

		// ✅ Điều hướng theo quyền
		switch (role) {
		case "2" -> { // 📚 Độc giả
			handleSubscriberDashboard(request);
			request.setAttribute("pageTitle", "📚 Trang cá nhân độc giả");
			request.setAttribute("contentPage", "/views/public/dashboard.jsp");
		}
		case "1" -> { // ✍️ Tác giả
			request.setAttribute("pageTitle", "✍️ Trang tác giả");
			request.setAttribute("contentPage", "/views/editor/dashboard.jsp");
		}
		case "0" -> { // 👑 Quản trị viên
			request.setAttribute("pageTitle", "👑 Trang quản trị");
			request.setAttribute("contentPage", "/views/manager/dashboard.jsp");
		}
		default -> {
			// 🚨 Nếu role không xác định (hoặc mất session role) → logout bắt đăng nhập lại
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		}

		// ✅ Gửi về layout chung
		request.getRequestDispatcher("/layout.jsp").forward(request, response);
	}

	/**
	 * 📚 Dashboard dành cho người đọc (subscriber)
	 */
	private void handleSubscriberDashboard(HttpServletRequest request) {
		// 🆕 6 bài mới nhất
		List<News> latestNews = Optional.ofNullable(newsDAO.findLatest(6)).orElse(new ArrayList<>());
		request.setAttribute("latestNews", latestNews);
		System.out.println("📰 Latest news count: " + latestNews.size());

		// 🍪 Bài đã xem gần đây (tối đa 10 bài)
		List<News> recentlyViewed = new ArrayList<>();
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("recentlyViewed".equals(cookie.getName()) && cookie.getValue() != null
						&& !cookie.getValue().isBlank()) {

					String[] ids = cookie.getValue().split(",");

					// 🔄 Lấy toàn bộ bài viết trong cookie (không giới hạn 10)
					for (String id : ids) {
						id = id.trim();
						if (!id.isEmpty()) {
							newsDAO.findById(id).ifPresent(recentlyViewed::add);
						}
					}
				}
			}
		}

		// 🔁 Fallback nếu chưa có cookie → lấy 10 bài mới nhất
		if (recentlyViewed.isEmpty()) {
			recentlyViewed = Optional.ofNullable(newsDAO.findLatest(20)).orElse(new ArrayList<>());
		}

		request.setAttribute("recentlyViewed", recentlyViewed);
		System.out.println("🕒 Recently viewed count: " + recentlyViewed.size());
	}
}
