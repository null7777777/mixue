package bean;

import util.MathUtils;

public class CartItem {
	private Tea tea;
	private int quantity;//数量
	private double subtotal;//小计
	
	public CartItem() {}
	
	
	
	public CartItem(Tea tea, int quantity) {
		super();
		this.setTea(tea);
		this.setQuantity(quantity);
	}

	public Tea getTea() {
		return tea;
	}
	public void setTea(Tea tea) {
		this.tea = tea;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		this.subtotal = MathUtils.getTwoDouble(quantity* tea.getPrice());
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	

	@Override
	public String toString() {
		return "CartItem [tea=" + tea + ", quantity=" + quantity + ", subtotal=" + subtotal + "]";
	}
	
	
	
}
