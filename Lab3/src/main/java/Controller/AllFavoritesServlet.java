package Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import Dao.FavoriteDAO;
import Dao.impl.FavoriteDAOImpl;
import Dao.UserDAO;
import Dao.impl.UserDAOImpl;
import Entity.Favorite;
import Entity.User;
import java.util.List;

@WebServlet("/favorites/all")
public class AllFavoritesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Lấy tất cả bản ghi Favorite (có join user và video)
        List<Favorite> favorites = favoriteDAO.findAll();

        request.setAttribute("favorites", favorites);
        request.getRequestDispatcher("/all-favorites.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}