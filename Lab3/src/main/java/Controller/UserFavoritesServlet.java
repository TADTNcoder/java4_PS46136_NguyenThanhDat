package Controller;

import Dao.UserDAO;
import Dao.FavoriteDAO;
import Dao.impl.UserDAOImpl;
import Dao.impl.FavoriteDAOImpl;
import Entity.User;
import Entity.Favorite;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/favorites")
public class UserFavoritesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAOImpl();
    private final FavoriteDAO favoriteDAO = new FavoriteDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Lấy danh sách tất cả user
        List<User> users = userDAO.findAll();
        req.setAttribute("users", users);

        // Nếu có userId thì lấy favorites
        String userIdParam = req.getParameter("userId");
        if (userIdParam != null && !userIdParam.isEmpty()) {
            User user = userDAO.findById(userIdParam);
            if (user != null) {
                List<Favorite> favorites = favoriteDAO.findByUser(userIdParam);
                req.setAttribute("user", user);
                req.setAttribute("favorites", favorites);
            }
        }

        req.getRequestDispatcher("/user-favorites.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}