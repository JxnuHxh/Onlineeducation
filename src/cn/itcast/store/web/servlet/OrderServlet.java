package cn.itcast.store.web.servlet;

import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Cart;
import cn.itcast.store.domain.CartItem;
import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.OrderService;
import cn.itcast.store.service.ServiceImp.OrderServiceImp;
import cn.itcast.store.utils.PaymentUtil;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

@WebServlet("/OrderServlet")
public class OrderServlet extends BaseServlet {
	// saveOrder  将购物车中的信息以订单的形式保存
	public String saveOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//确认用户登录状态
		User user=(User)req.getSession().getAttribute("loginUser");
		if(null==user){
			req.setAttribute("msg", "请登录之后在下单");
			return "/jsp/info.jsp";
		}
		//获取购物车
		Cart cart=(Cart)req.getSession().getAttribute("cart");
		//创建订单对象,为订单对象赋值
		Order order=new Order();
		order.setOid(UUIDUtils.getCode());
		order.setOrdertime(new Date());
		order.setTotal(cart.getTotal());
		order.setState(1);
		order.setUser(user);
		//遍历购物项的同时,创建订单项,为订单项赋值
		for (CartItem item : cart.getCartItems()) {
			OrderItem orderItem=new OrderItem();
			orderItem.setItemid(UUIDUtils.getCode());
			orderItem.setQuantity(item.getNum());
			orderItem.setTotal(item.getSubTotal());
			orderItem.setProduct(item.getProduct());
			
			//设置当前的订单项属于哪个订单:程序的角度体检订单项和订单对应关系
			orderItem.setOrder(order);
			order.getList().add(orderItem);
		}
		//调用业务层功能:保存订单
		OrderService OrderService=new OrderServiceImp();
		//将订单数据,用户的数据,订单下所有的订单项都传递到了service层
		OrderService.saveOrder(order);
		//清空购物车
		cart.clearCart();
		//将订单放入request
		req.setAttribute("order", order);
		//转发/jsp/order_info.jsp
		return "/jsp/order_info.jsp";
	}
	
	//findMyOrdersWithPage
	public String findMyOrdersWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//获取用户信息
		User user=(User)req.getSession().getAttribute("loginUser");
		//获取当前页
		int curNum=Integer.parseInt(req.getParameter("num"));
		//调用业务层功能:查询当前用户订单信息,返回PageModel
		OrderService OrderService=new OrderServiceImp();
		//SELECT * FROM orders WHERE uid=? limit ? , ? 
		//PageModel:1_分页参数 2_url  3_当前用户的当前页的订单(集合) ,每笔订单上对应的订单项,以及订单项对应的商品信息
		PageModel pm=OrderService.findMyOrdersWithPage(user,curNum);
		//将PageModel放入request
		req.setAttribute("page", pm);
		//转发到/jsp/order_list.jsp
		return "/jsp/order_list.jsp";
		
	}
	
	//findOrderByOid
	public String findOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//获取到订单oid
		String oid=req.getParameter("oid");
		//调用业务层功能:根据订单编号查询订单信息
		OrderService OrderService=new OrderServiceImp();
		Order order=OrderService.findOrderByOid(oid);
		// 将订单放入request
		req.setAttribute("order", order);
		// 转发到/jsp/order_info.jsp
		return "/jsp/order_info.jsp";
	}
	
	
	public String payOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		//获取订单oid,收货人地址,姓名,电话,银行
				String oid=req.getParameter("oid");
				String address=req.getParameter("address");
				String name=req.getParameter("name");
				String telephone=req.getParameter("telephone");
				String pd_FrpId=req.getParameter("pd_FrpId");
				//更新订单上收货人的地址,姓名,电话
				OrderService OrderService=new OrderServiceImp();
				Order order=OrderService.findOrderByOid(oid);
				order.setName(name);
				order.setTelephone(telephone);
				order.setAddress(address);
				OrderService.updateOrder(order);
				//向易宝支付发送参数
				// 把付款所需要的参数准备好:
						String p0_Cmd = "Buy";
						//商户编号
						String p1_MerId = "10001126856";
						//订单编号
						String p2_Order = oid;
						//金额
						String p3_Amt = "0.01";
						String p4_Cur = "CNY";
						String p5_Pid = "";
						String p6_Pcat = "";
						String p7_Pdesc = "";
						//接受响应参数的Servlet
						String p8_Url = "http://localhost:8888/store_v5/OrderServlet?method=callBack";
						String p9_SAF = "";
						String pa_MP = "";
						String pr_NeedResponse = "1";
						//公司的秘钥
						String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
							
						//调用易宝的加密算法,对所有数据进行加密,返回电子签名
						String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
								
						StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
						sb.append("p0_Cmd=").append(p0_Cmd).append("&");
						sb.append("p1_MerId=").append(p1_MerId).append("&");
						sb.append("p2_Order=").append(p2_Order).append("&");
						sb.append("p3_Amt=").append(p3_Amt).append("&");
						sb.append("p4_Cur=").append(p4_Cur).append("&");
						sb.append("p5_Pid=").append(p5_Pid).append("&");
						sb.append("p6_Pcat=").append(p6_Pcat).append("&");
						sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
						sb.append("p8_Url=").append(p8_Url).append("&");
						sb.append("p9_SAF=").append(p9_SAF).append("&");
						sb.append("pa_MP=").append(pa_MP).append("&");
						sb.append("pd_FrpId=").append(pd_FrpId).append("&");
						sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
						sb.append("hmac=").append(hmac);

						System.out.println(sb.toString());
						//
						// 使用重定向：
						resp.sendRedirect(sb.toString());
						return null;
			}
			
			//callBack
	public String callBack(HttpServletRequest request, HttpServletResponse resp) throws Exception {
				//接收易宝支付的数据
				// 验证请求来源和数据有效性
				// 阅读支付结果参数说明
				// System.out.println("==============================================");
				String p1_MerId = request.getParameter("p1_MerId");
				String r0_Cmd = request.getParameter("r0_Cmd");
				String r1_Code = request.getParameter("r1_Code");
				String r2_TrxId = request.getParameter("r2_TrxId");
				String r3_Amt = request.getParameter("r3_Amt");
				String r4_Cur = request.getParameter("r4_Cur");
				String r5_Pid = request.getParameter("r5_Pid");
				String r6_Order = request.getParameter("r6_Order");
				String r7_Uid = request.getParameter("r7_Uid");
				String r8_MP = request.getParameter("r8_MP");
				String r9_BType = request.getParameter("r9_BType");
				String rb_BankId = request.getParameter("rb_BankId");
				String ro_BankOrderId = request.getParameter("ro_BankOrderId");
				String rp_PayDate = request.getParameter("rp_PayDate");
				String rq_CardNo = request.getParameter("rq_CardNo");
				String ru_Trxtime = request.getParameter("ru_Trxtime");

				// hmac
				String hmac = request.getParameter("hmac");
				// 利用本地密钥和加密算法 加密数据
				String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
				
				
				//保证数据合法性
				boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
						r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
						r8_MP, r9_BType, keyValue);
				if (isValid) {
					// 有效
					if (r9_BType.equals("1")) {
						// 浏览器重定向
						
						//如果支付成功,更新订单状态
						OrderService OrderService=new OrderServiceImp();
						Order order=OrderService.findOrderByOid(r6_Order);
						order.setState(2);
						OrderService.updateOrder(order);
						//向request放入提示信息
						request.setAttribute("msg", "支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
						//转发到/jsp/info.jsp
						return "/jsp/info.jsp";
						
						
					} else if (r9_BType.equals("2")) {
						// 修改订单状态:
						// 服务器点对点，来自于易宝的通知
						System.out.println("收到易宝通知，修改订单状态！");//
						// 回复给易宝success，如果不回复，易宝会一直通知
						resp.getWriter().print("success");
					}

				} else {
					throw new RuntimeException("数据被篡改！");
				}
				return null;
	}
}
