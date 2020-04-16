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
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "处理登录逻辑", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
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
			String sql = "select account from " + DBUtil.Table_Account + " where account='" + account
					+ "' and password='" + password + "'";
			log(sql);
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) { // 能查到该账号，说明已经注册过了
				code = "200";
				message = "登陆成功";
			} else {
 
				code = "100";
				message = "登录失败，密码不匹配或账号未注册";
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
		log("不支持POST方法");
	}
 
}