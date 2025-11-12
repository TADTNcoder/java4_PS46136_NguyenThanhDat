package poly.com.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/bai2","/chuvi","/dientich","/tinhhieu"})
public class Bai21controller extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	req.getRequestDispatcher("bai2.jsp").forward(req, resp);
	}
	
		@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
	String chon=req.getRequestURI();
	
			float a,b,cv,dt,hieu;
			
	a=Float.parseFloat(req.getParameter("canha"));
	b=Float.parseFloat(req.getParameter("canhb"));
	
	
	if(chon.contains("/chuvi"))
	{
		chuvi(req,resp);
		
	}
	else
		if (chon.contains("/dientich"))
		{
			dt=a*b;
			req.setAttribute("dt", dt);
		}
		else
			
			{hieu=a-b;
			req.setAttribute("hieu", hieu);
			}
	
	
	Double canha=Double.parseDouble(req.getParameter("canha"));
	Double canhb=Double.parseDouble(req.getParameter("canhb"));
	req.setAttribute("canha", canha);
	req.setAttribute("canhb", canhb);

		req.getRequestDispatcher("bai2.jsp").forward(req, resp)	;	
	
	}
	
		
	
		protected void chuvi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// TODO Auto-generated method stub
			
			float a,b,cv;
			a=Float.parseFloat(req.getParameter("canha"));
			b=Float.parseFloat(req.getParameter("canhb"));
			cv=(a+b)*2;
			req.setAttribute("cv", cv);
			Double canha=Double.parseDouble(req.getParameter("canha"));
			Double canhb=Double.parseDouble(req.getParameter("canhb"));
			req.setAttribute("canha", canha);
			req.setAttribute("canhb", canhb);
		req.getRequestDispatcher("bai2.jsp").forward(req, resp);
		}
		

}
