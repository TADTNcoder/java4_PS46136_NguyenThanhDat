package Controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.beanutils.BeanUtils;
import Dao.*;
import Entity.*;


import Dao.UserDAOImpl;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet({
	  "/user/crud/index",
	  "/user/crud/edit/*",
	  "/user/crud/create",
	  "/user/crud/update",
	  "/user/crud/delete",
	  "/user/crud/reset"
	})
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    	req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
    	UserDAO dao = new UserDAOImpl();
        User form = new User();
        try {
          BeanUtils.populate(form, req.getParameterMap());
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }

        String path = req.getServletPath();
        String message = "Enter user information";

        if (path.contains("edit")) {
          String id = req.getPathInfo().substring(1);
          form = dao.findById(id);
          message = "Edit: " + id;
        } else if (path.contains("create")) {
          dao.create(form);
          message = "Create: " + form.getId();
          form = new User();
        } else if (path.contains("update")) {
          dao.update(form);
          message = "Update: " + form.getId();
        } else if (path.contains("delete")) {
          dao.deleteById(form.getId());
          message = "Delete: " + form.getId();
          form = new User();
        } else if (path.contains("reset")) {
          form = new User();
        }

        List<User> list = dao.findAll();
        req.setAttribute("message", message);
        req.setAttribute("user", form);
        req.setAttribute("users", list);
        req.getRequestDispatcher("/user-crud.jsp").forward(req, resp);
      }


}
