package qyServer0;

import javax.servlet.ServletContext;

import java.beans.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLDocument;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(name = "/addUserServlet")
public class AddUserSevlet extends HttpServlet {
	JDBCKonnekt jk = new JDBCKonnekt();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try(PrintWriter out = response.getWriter()){
	        JSONObject jsonObject = new JSONObject();
	        Map<String,String> params = new HashMap<>();
			String email = request.getParameter("email").trim();
			String username = request.getParameter("username").trim();
			String password = request.getParameter("password").trim();
	        users user = new users(username,email,password);
	        usersService us = new usersService();
	        ServletContext sc = request.getSession().getServletContext();
	        int operatorRes = us.addUser(user, jk);
	        //System.out.println("execute here!\n");
	        params.put("rtnvalue", operatorRes+"");
	        //System.out.println(operatorRes+"");
	        jsonObject.put("result", params);
	        out.write(jsonObject.toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
