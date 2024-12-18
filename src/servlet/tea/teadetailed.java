package servlet.tea;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TeaDao;
import dao.impl.TeaDaoImpl;

/**
 * 奶茶的详情页面
 */
@WebServlet("/teadetail")
public class teadetailed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DETAIL_PATH="jsp/tea/teadetails.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	//根据奶茶id显示其详情信息
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int teaId = Integer.parseInt(request.getParameter("teaId"));
		TeaDao bd = new TeaDaoImpl();
		request.setAttribute("teaInfo", bd.findTeaById(teaId));
		request.getRequestDispatcher(DETAIL_PATH).forward(request, response);

	}

}
