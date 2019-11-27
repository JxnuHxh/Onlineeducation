package cn.itcast.store.domain;

public class CartItem {
	private Product product;//目的携带购物项3种参数(图片路径,商品名称,商品价格)
	private int num;//当前类别商品数量
	private double subTotal;//小计
	
	
	//小计是经过计算可以获取到的
	public double getSubTotal() {
		return product.getShop_price()*num;
	}
	
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	
	
	
}
