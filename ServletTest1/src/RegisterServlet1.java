import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class RegisterServlet1
 */
@WebServlet(description = "用来和客户端交互", urlPatterns = { "/RegisterServlet1" })
public class RegisterServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Default constructor.
	 */
	public void RegisterServlet() {
		log("RegisterServlet construct...");
	}
 
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getMethod();
		if ("GET".equals(method)) {
			log("请求方法：GET");
			doGet(request, response);
		} else if ("POST".equals(method)) {
			log("请求方法：POST");
			doPost(request, response);
		} else {
			log("请求方法分辨失败！");
		}
	}
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* 先设置请求、响应报文的编码格式  */
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String code = "";
		String message = "";
 
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		log(account + ";" + password);
 
		Connection connect = DBUtil.getConnect();
		try {
			Statement statement = connect.createStatement();
			String sql = "select account from " + DBUtil.Table_Account + " where account='" + account + "'";
			log(sql);
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) { // 能查到该账号，说明已经注册过了
				code = "100";
				message = "该账号已存在";
			} else {
				String sqlInsert = "insert into " + DBUtil.Table_Account + "(account, password) values('"
						+ account + "', '" + password + "')";
				log(sqlInsert);
				if (statement.executeUpdate(sqlInsert) > 0) { // 否则进行注册逻辑，插入新账号密码到数据库
					code = "200";
					message = "注册成功";
				} else {
					code = "300";
					message = "注册失败";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
 
		response.getWriter().append("code:").append(code).append(";message:").append(message);
	}
 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
	}
 
	@Override
	public void destroy() {
		log("RegisterServlet destory.");
		super.destroy();
	}

}
