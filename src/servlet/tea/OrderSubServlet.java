package servlet.tea;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Cart;
import bean.CartItem;
import bean.Order;
import bean.OrderItem;
import bean.PageBean;
import bean.User;
import dao.TeaDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.impl.TeaDaoImpl;
import dao.impl.OrderDaoImpl;
import dao.impl.OrderItemDaoImpl;
import util.DateUtil;
import util.RanUtil;

/**
 * 订单前台的一些请求
 */
@WebServlet(name = "OrderServlet", urlPatterns = { "/OrderServlet" })
public class OrderSubServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int  MAX_LIST_SIZE=5;
	private static final String CART_PATH="jsp/tea/cart.jsp" ;
	private static final String ORDER_PAY_PATH="jsp/tea/ordersuccess.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getParameter("action");
		switch(action) {
		case "subOrder":
			subOrder(request,response);
			break;
		case "list":
			myOrderList(request,response);
			break;
		case "ship":
			orderShip(request,response);
			break;
		}
		
	}
	//订单的操作
	private void orderShip(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String orderId=request.getParameter("id");
		OrderDao orderDao=new OrderDaoImpl();
		
		if(orderDao.orderStatus(Integer.parseInt(orderId),3)) {
			request.setAttribute("orderMessage", "一个订单操作成功");
		}else {
			request.setAttribute("orderMessage", "一个订单操作失败");
		}
		myOrderList(request,response);
		
	}

	//我的订单列表请求
	private void myOrderList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		User user=(User)request.getSession().getAttribute("landing");
		if(user==null) {
			response.sendRedirect("jsp/tea/reg.jsp?type=login");
		}else {
			OrderDao orderDao=new OrderDaoImpl();
			OrderItemDao oItem=new OrderItemDaoImpl();
			TeaDao teaDao =new TeaDaoImpl();
			int curPage = 1;
			String page = request.getParameter("page");
			if (page != null) {
				curPage = Integer.parseInt(page);
			}
			PageBean pb= new PageBean(curPage, MAX_LIST_SIZE,orderDao.orderReadCount(user.getUserId()) );
			
			List<Order> orderList = orderDao.orderList(pb,user.getUserId());	
			
			for(Order order:orderList) {
				//通过订单编号查询订单项集合
				order.setoItem(oItem.findItemByOrderId(order.getOrderId()));
				for(OrderItem oi:order.getoItem()) {
					//通过商品id获取商品对象
					oi.setTea(teaDao.findTeaById(oi.getTeaId()));
				}
			}

			request.setAttribute("pageBean", pb);
			request.setAttribute("orderList",orderList);		
			request.getRequestDispatcher("jsp/tea/myorderlist.jsp").forward(request, response);
			
		}
	}

	//订单提交处理，生成订单号并存入数据库（订单状态未1：未付款），
	private void subOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//获得及生成一些需要的对象和数据
		HttpSession session = request.getSession();
		Cart cart=(Cart) session.getAttribute("shopCart");
		User user=(User) session.getAttribute("landing");
		String orderNum=RanUtil.getOrderNum();//生成的订单号
		String orderDate=DateUtil.show();//生成订单日期
		Order order=new Order();
		OrderDao orderDao=new OrderDaoImpl();
		OrderItemDao oItemDao=new OrderItemDaoImpl();
		
		//给订单对象属性赋值
		order.setOrderNum(orderNum);//订单号
		order.setOrderDate(orderDate);//订单日期
		order.setMoney(cart.getTotPrice());//订单价格
		order.setOrderStatus(1);//订单所处状态
		order.setUserId(user.getUserId());//用户id
		
		if(orderDao.orderAdd(order)) {
			//订单保存成功通过订单号获取订单编号，订单项留用
			order.setOrderId(orderDao.findOrderIdByOrderNum(orderNum));
			//
			for(Map.Entry<Integer, CartItem> meic:cart.getMap().entrySet()) {
				OrderItem oi=new OrderItem();
				oi.setTeaId(meic.getKey());
				oi.setQuantity(meic.getValue().getQuantity());
				oi.setOrderId(order.getOrderId());
				oItemDao.orderAdd(oi);
			}
			//订单项保存结束清空购物车，返回订单提交成功
			session.removeAttribute("shopCart");
			request.setAttribute("orderNum", order.getOrderNum());
			request.setAttribute("money", order.getMoney());
			request.getRequestDispatcher(ORDER_PAY_PATH).forward(request, response);
		}else {
			request.setAttribute("suberr", "订单提交失败，请重新提交");
			request.getRequestDispatcher(CART_PATH).forward(request, response);
		}
	}
}
