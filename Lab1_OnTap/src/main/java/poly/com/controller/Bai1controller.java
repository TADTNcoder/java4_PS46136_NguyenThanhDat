package poly.com.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet({"/bai1","/bai1/insert","/bai1/update","/bai1/delete"})
public class Bai1controller extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	req.getRequestDispatcher("bai11.jsp").forward(req, resp);
	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url=req.getRequestURI();
		
			
			if (url.contains("/bai1/insert"))
				
				 resp.getWriter().println("<h1>Creating a Insert record...</h1>");
			else if (url.contains("/bai1/delete")) 
			 resp.getWriter().println("<h1>DELTE DU LIEU...</h1>");
			else 
			resp.getWriter().println("<h1>update du lieu </h1> ");
		
	}
	
}
	

