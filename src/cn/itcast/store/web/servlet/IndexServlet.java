package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.ServiceImp.CategoryServiceImp;
import cn.itcast.store.service.ServiceImp.ProductServiceImp;
import cn.itcast.store.web.base.BaseServlet;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/IndexServlet")
public class IndexServlet extends BaseServlet {
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//调用业务层功能：获取全部分类信息，返回集合
		CategoryService categoryService=new  CategoryServiceImp();
		List<Category> list=categoryService.getAllCats();
		//将返回的集合放入request
		request.setAttribute("allCats",list);
		
		ProductService productService=new ProductServiceImp();
		
		List<Product> list02=productService.findNews();
		List<Product> list01=productService.findHots();
		request.setAttribute("hots", list01);
		request.setAttribute("news", list02);
		
		
		return "/jsp/index.jsp";
	}
	
}
