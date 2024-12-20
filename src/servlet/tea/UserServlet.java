package servlet.tea;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.UserDao;
import dao.impl.AdminDaoImpl;
import dao.impl.UserDaoImpl;

import net.sf.json.JSONObject;

/**
 * 用户前台的操作
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LOGIN_PATH="jsp/tea/reg.jsp?type=login";
	private static final String REG_PATH="jsp/tea/reg.jsp?type=reg";
	private static final String INDEX_PATH="jsp/tea/index.jsp";
	private static final String USER_PATH="jsp/tea/user.jsp";
	private static final String LANDING="landing";//前台用户session标识

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getParameter("action");
		switch(action){
		case "login":
			login(request,response);
			break;
		case "off":
			offlogin(request,response);
			break;
		case "ajlogin":
			ajlogin(request,response);
			break;
		case "reg":
			reg(request,response);
			break;
		case "landstatus":
			landstatus(request,response);
			break;
		case "detail":
			detail(request,response);
			break;
		}
	}
	




	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		UserDao ud=new UserDaoImpl();
		User user = ud.getUserByName(userName);
		request.getSession().setAttribute("user", user);
		request.getRequestDispatcher(USER_PATH).forward(request, response);
	}


	//判断用户登陆状态
	private void landstatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		User user=  (User) request.getSession().getAttribute(LANDING);
		
		PrintWriter pw = response.getWriter();
		JSONObject json=new JSONObject();
		
		if(user!=null) {
			json.put("status", "y");
		}else {
			json.put("status", "n");
		}
		pw.print(json.toString());
		pw.flush();
		
	}
	//ajax登陆
	private void ajlogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		User user=new User(userName,passWord);
		PrintWriter pw = response.getWriter();
		JSONObject json=new JSONObject();
		UserDao ud=new UserDaoImpl();
		User user2=ud.userLogin(user);
		
		if(user2!=null) {
			if("y".equals(user2.getEnabled())) {
				request.getSession().setAttribute(LANDING, user2);
				json.put("status","y" );
			}else {
				json.put("info", "该用户已被禁用，请联系管理员");
			}
		}else {
			json.put("info", "用户名或密码错误，请重新登陆！");
			
		}
		pw.print(json.toString());
	}

	//用户的注册
	private void reg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDao ad=new UserDaoImpl();
		User user=new User(
				request.getParameter("userName"),
				request.getParameter("passWord"),
				request.getParameter("name"),
				request.getParameter("sex"),
				Integer.parseInt(request.getParameter("age")),
				request.getParameter("tell"),
				request.getParameter("address"));
		user.setEnabled("y");//默认添加的用户启用
		//添加之前，判断用户名是否已经存在
		if(new AdminDaoImpl().findUser(user.getUserName())){
			request.setAttribute("infoList", "用户添加失败！用户名已存在");
			request.getRequestDispatcher(REG_PATH).forward(request, response);
		}else{
			//执行dao层添加操作，注册成功跳转登录页面，否则停留在注册页面
			if(ad.userAdd(user)){
				request.setAttribute("infoList", "注册成功！请登陆！");
				request.getRequestDispatcher(LOGIN_PATH).forward(request, response);
			}else{
				request.setAttribute("userMessage", "用户添加失败！");
				request.getRequestDispatcher(REG_PATH).forward(request, response);
			}
		}
	}

	//退出登录
	private void offlogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user =  (User) request.getSession().getAttribute(LANDING);
		if(user!=null) {
			request.getSession().removeAttribute(LANDING);
		}
		response.sendRedirect(INDEX_PATH);
		
	}
	//用户的登录
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName=request.getParameter("userName");
		String passWord=request.getParameter("passWord");
		User user=new User(userName,passWord);
		UserDao ud=new UserDaoImpl();
		User user2=ud.userLogin(user);
		if(user2!=null) {
			if("y".equals(user2.getEnabled())) {
				request.getSession().setAttribute(LANDING, user2);
				response.sendRedirect(INDEX_PATH);
				
			}else {
				request.setAttribute("infoList", "该用户已被禁用，请联系管理员");
				request.getRequestDispatcher(LOGIN_PATH).forward(request, response);
			}
		}else {
			request.setAttribute("infoList", "用户名或密码错误，请重新登陆！");
			request.getRequestDispatcher(LOGIN_PATH).forward(request, response);
		}
	}
}
