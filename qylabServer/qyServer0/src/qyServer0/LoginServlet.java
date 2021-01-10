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

@WebServlet(name = "/loginServlet")
public class LoginServlet extends HttpServlet {
	JDBCKonnekt jk = new JDBCKonnekt();
	encrypt cry;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try(PrintWriter out = response.getWriter()){
	        usersService us = new usersService();
	        Map<String,String> params = new HashMap<>();
	        JSONObject jsonObject = new JSONObject();
			String username = request.getParameter("username").trim();
			String password = request.getParameter("password").trim();
			String hashpsw = cry.getSHA256(password);
			//System.out.println(username);
			//System.out.println(hashpsw);
			int operatorRes = us.verifyUser(username, hashpsw, jk);
			//System.out.println(operatorRes);
			params.put("rtnvalue", operatorRes+"");
	        jsonObject.put("result", params);
	        out.write(jsonObject.toString());
        }
	}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}