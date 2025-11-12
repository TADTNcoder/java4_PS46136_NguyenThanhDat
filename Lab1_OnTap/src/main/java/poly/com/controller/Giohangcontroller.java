package poly.com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import poly.com.model.CartItem1;
import poly.com.model.Item;
@WebServlet("/giohang")
public class Giohangcontroller extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = req.getSession(); // Láº¥y session
	        List<CartItem1> cartItems = (List<CartItem1>) session.getAttribute("cartItems"); // Láº¥y giá»� hÃ ng tá»« session

	        // Náº¿u giá»� hÃ ng chÆ°a cÃ³, khá»Ÿi táº¡o má»›i
	        if (cartItems == null) {
	            cartItems = new ArrayList<>();
	        }

	        // Táº¡o danh sÃ¡ch sáº£n pháº©m
	        List<Item> items = new ArrayList<>();
	        items.add(new Item("Nokia 2020", "at.jpg", 500, 0.1));
	        items.add(new Item("Samsung Xyz", "at1.jpg", 700, 0.15));
	        items.add(new Item("iPhone Xy", "fpoly1.jpg", 900, 0.25));
	        items.add(new Item("Sony Erricson", "fpoly.jpg", 55, 0.3));

	        req.setAttribute("items", items);
		req.getRequestDispatcher("cart.jsp").forward(req, resp);
	}

}
