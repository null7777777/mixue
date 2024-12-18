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
 * 奶茶的列表
 */
@WebServlet("/TeaList")
public class TeaList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_LIST_SIZE = 12;
	private static final String TEALIST_PATH="jsp/tea/tealist.jsp";
   

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action=request.getParameter("action");
		if(action==null) {
			action="list";
		}
		switch(action) {
		case "d":
			break;
			
		case "list"://默认全部商品列表
			teaList(request,response);
			break;
		}
	}

	//显示全部商品的列表
	private void teaList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TeaDao bd = new TeaDaoImpl();
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		
		PageBean pb=null;
		List<Tea> teaList =new ArrayList<Tea>();
		String catalogIdStr = request.getParameter("catalogId");//获取有没有分类id，没有就是查全部
		
		if(catalogIdStr!=null) {
			int catalogId=Integer.parseInt(catalogIdStr);
			pb = new PageBean(curPage, MAX_LIST_SIZE, bd.teaReadCount(catalogId));
			teaList = bd.teaList(pb,catalogId);
			
			if(teaList.size()>0) {
				request.setAttribute("title", teaList.get(0).getCatalog().getCatalogName());//从返回的分类集合中第一个获取数据的分类
			}
			
		}else {
			pb = new PageBean(curPage, MAX_LIST_SIZE, bd.teaReadCount());
			teaList = bd.teaList(pb);
			request.setAttribute("title", "所有商品");
		}

		request.setAttribute("pageBean", pb);
		request.setAttribute("teaList", teaList);
		
		request.getRequestDispatcher(TEALIST_PATH).forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
