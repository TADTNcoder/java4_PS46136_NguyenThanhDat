package poly.com.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bai3")
public class Bai3controller extends HttpServlet
{
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	req.getRequestDispatcher("bai3.jsp").forward(req, resp);
}
@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String gioiTinh = request.getParameter("gioiTinh");
	String married = request.getParameter("married");//married status
	String country = request.getParameter("country");
	String ghiChu = request.getParameter("ghiChu");
	
	String[] getSoThich = request.getParameterValues("soThich");
	
	StringBuilder sb = new StringBuilder();
	for (String item : getSoThich) {
		 {
			 sb.append(item).append(", ");
		 
		 }
	}
	
	String maritalStatus = "";
	if(married == null) {
		maritalStatus = "doc than";
	}else {
		maritalStatus = married;
	}
	
	String soThichConverted = "";
	if(sb.toString() != "") {
		soThichConverted = sb.toString().substring(0,sb.toString().lastIndexOf(","))+".";
	}
	
	request.setAttribute("username", username);
	request.setAttribute("password", password);
	request.setAttribute("gioiTinh", gioiTinh);
	request.setAttribute("married", maritalStatus);
	request.setAttribute("country", country);
	request.setAttribute("ghiChu", ghiChu);
	request.setAttribute("favourites", soThichConverted);
	
	request.getRequestDispatcher("hienthibai3.jsp").forward(request, resp);
	}
		
	}

