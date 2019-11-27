package cn.itcast.store.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.ServiceImp.ProductServiceImp;
import cn.itcast.store.web.base.BaseServlet;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends BaseServlet {
	
	public String findProductByPid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取id
		String pid=request.getParameter("pid");
		
		//根据商品pid查询商品信息
		ProductService productService=new ProductServiceImp();
		Product product=productService.findProductByPid(pid);
		//将获得商品放入request
		request.setAttribute("product",product);
		//转发到
		return "/jsp/product_info.jsp";
	}
	//
	public String findProductsByCidWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		String cid=request.getParameter("cid");
		int curNum=Integer.parseInt(request.getParameter("num"));
		
		ProductService productService=new ProductServiceImp();
		PageModel pm= productService.findProductsByCidWithPage(cid,curNum);
		request.setAttribute("page", pm);
		return "/jsp/product_list.jsp";
	}
}
