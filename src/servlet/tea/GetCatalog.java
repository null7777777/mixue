package servlet.tea;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Catalog;
import dao.TeaDao;
import dao.CatalogDao;
import dao.impl.TeaDaoImpl;
import dao.impl.CatalogDaoImpl;

import net.sf.json.JSONObject;

/**
 * 获取分类项
 */
@WebServlet("/GetCatalog")
public class GetCatalog extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json"); 
		JSONObject json = new JSONObject();
		CatalogDao cd=new CatalogDaoImpl();
		TeaDao bd=new TeaDaoImpl();
		List<Catalog> catalog = cd.getCatalog();
		
		//这里返回查询每个分类的数量
		for(int i=0;i<catalog.size();i++) {
			Catalog c = catalog.get(i);
			long size=bd.teaReadCount(c.getCatalogId());
			c.setCatalogSize(size);
		}
		json.put("catalog", catalog);
		response.getWriter().append(json.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
