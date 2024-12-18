package bean;

import java.util.Map;

public class OrderItem {

    private int itemId;           //订单项编号
    private int teaId;          //商品编号
    private int orderId;       //订单编号
    private int quantity;      //数量
    
    private Tea tea;
    public OrderItem() {
    }

   
    public OrderItem(Map<String, Object> map) {
		this.setOrderId((int) map.get("orderId"));
		this.setTeaId((int) map.get("teaId"));
		this.setItemId((int) map.get("itemId"));
		this.setQuantity((int) map.get("quantity"));
	}


	public int getItemId() {
		return itemId;
	}


	public void setItemId(int itemId) {
		this.itemId = itemId;
	}


	public int getTeaId() {
		return teaId;
	}


	public void setTeaId(int teaId) {
		this.teaId = teaId;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public Tea getTea() {
		return tea;
	}


	public void setTea(Tea tea) {
		this.tea = tea;
	}


    
}
