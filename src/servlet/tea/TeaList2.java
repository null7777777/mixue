package servlet.tea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Tea;
import bean.PageBean;
import dao.TeaDao;
import dao.impl.TeaDaoImpl;

/**
 * 奶茶的列表，以分类名形式显示
 */
@WebServlet("/TeaList2")
public class TeaList2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_LIST_SIZE = 12;
	private static final String TEALIST_PATH="jsp/tea/tealist.jsp";
   

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String seachname = new String(request.getParameter("seachname").getBytes("iso-8859-1"),"utf-8");
		String seachname1 = request.getParameter("seachname");
		teaList(request,response,seachname1);
	}

	//全部蛋糕的列表
	private void teaList(HttpServletRequest request, HttpServletResponse response, String seachname) throws ServletException, IOException {
		TeaDao bd = new TeaDaoImpl();
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		
		PageBean pb=null;
		List<Tea> teaList =new ArrayList<Tea>();
		if(seachname == null || seachname == "") {
			pb = new PageBean(curPage, MAX_LIST_SIZE, bd.teaReadCount());
			teaList = bd.teaList(pb);
		}else {
			pb = new PageBean(curPage, MAX_LIST_SIZE, bd.teaReadCount(seachname));
			teaList = bd.teaList(pb,seachname);
		}
		request.setAttribute("title", "所有商品");
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("teaList", teaList);
		
		request.getRequestDispatcher(TEALIST_PATH).forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
