package servlet.tea;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Tea;
import bean.Cart;
import bean.CartItem;
import dao.TeaDao;
import dao.impl.TeaDaoImpl;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * 购物车的相关操作
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getParameter("action");
		switch(action) {
		case "add":
			addTOCart(request,response);
			break;
		case "changeIn":
			changeIn(request,response);//更改购物车商品数量
			break;
		case "delItem":
			delItem(request,response);
			break;
		case "delAll":
			delAll(request,response);
		}
	}


	//清空购物车
	private void delAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("shopCart");
		response.sendRedirect("jsp/tea/cart.jsp");
	}

	//购物项删除
	private void delItem(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int teaId=Integer.parseInt(request.getParameter("id"));
		Cart shopCart = (Cart) request.getSession().getAttribute("shopCart");
		if(shopCart.getMap().containsKey(teaId)) {
			shopCart.getMap().remove(teaId);
		}
		response.sendRedirect("jsp/tea/cart.jsp");
	}

	//更改购物项数量
	private void changeIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int teaId=Integer.parseInt(request.getParameter("teaId"));
		int quantity=Integer.parseInt(request.getParameter("quantity"));
		Cart shopCart = (Cart) request.getSession().getAttribute("shopCart");
		CartItem item = shopCart.getMap().get(teaId);
		item.setQuantity(quantity);
		JSONObject json=new JSONObject();
		json.put("subtotal", item.getSubtotal());
		json.put("totPrice", shopCart.getTotPrice());
		json.put("totQuan", shopCart.getTotQuan());
		json.put("quantity", item.getQuantity());
		response.getWriter().print(json.toString());

	}

	//添加商品到购物车，并实时显示购物车中商品数量
	private void addTOCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String teaId=request.getParameter("teaId");
		TeaDao bd=new TeaDaoImpl();
		Tea tea = bd.findTeaById(Integer.parseInt(teaId));
		
		Cart shopCart = (Cart) request.getSession().getAttribute("shopCart");
	
		if(shopCart==null) {
			shopCart=new Cart();
			request.getSession().setAttribute("shopCart", shopCart);
		}
		shopCart.addTea(tea);
		response.getWriter().print(shopCart.getTotQuan());//返回现在购物车实时数量
	}
}
 