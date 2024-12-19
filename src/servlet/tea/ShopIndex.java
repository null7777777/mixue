package servlet.tea;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Tea;
import dao.TeaDao;
import dao.impl.TeaDaoImpl;

import net.sf.json.JSONObject;

/**
 *商店首页
 */
@WebServlet("/ShopIndex")
public class ShopIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/json"); 
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession();
		TeaDao bd=new TeaDaoImpl();
		List<Tea> recTeas = bd.teaList(4);
		json.put("recTeas", recTeas);
		List<Tea> newTeas = bd.newTeas(4);
		json.put("newTeas", newTeas);
		List<String> imgSrc = bd.findRecommendTeaImages();
		session.setAttribute("imgSrc", imgSrc);
		
		PrintWriter pw = response.getWriter();
		pw.print(json);
		pw.flush();
	}

}
