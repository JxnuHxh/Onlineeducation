package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.UserService;
import cn.itcast.store.service.ServiceImp.CategoryServiceImp;
import cn.itcast.store.service.ServiceImp.UserServiceImp;
import cn.itcast.store.utils.MailUtils;
import cn.itcast.store.utils.MyBeanUtils;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;


@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
	
public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	       return "jsp/register.jsp";
	}
public String loginUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
	CategoryService categoryService=new  CategoryServiceImp();
	List<Category> list=categoryService.getAllCats();
	//将返回的集合放入request
	request.setAttribute("allCats",list);
    return "jsp/login.jsp";
}
public String userRegist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	//接受表单参数
	Map<String,String[]> map= request.getParameterMap();
	User user=new User();
	MyBeanUtils.populate(user, map);
	user.setUid(UUIDUtils.getId());
	user.setState(0);
	user.setCode(UUIDUtils.getCode());
	UserService UserService =new UserServiceImp();
	try {
	UserService.userRegist(user);
	MailUtils.sendMail(user.getEmail(), user.getCode());
	request.setAttribute("msg", "user ");
	request.setAttribute("msg", "successful!");
	return "/jsp/info.jsp";
	}catch(Exception e) {
		request.setAttribute("msg", "not successful!");
		
	}
	
	return "/jsp/info.jsp";
}

public String active(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
    //获取激活码
	String code=request.getParameter("code");
	
	//调用业务层激活码
	UserService userService=new UserServiceImp();
	//进行激活的信息提示
	boolean flag=userService.userActive(code);
	
	System.out.println(flag);
	
	if(flag==true) {
		request.setAttribute("usg","jihuochenggong");
		return "/jsp/login.jsp";
	}
	else {
		request.setAttribute("usg","userjihuoshibai");
		return "/jsp/info.jsp";
	}
	
}
//userLogin
public String userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 获取用户数据
	User user =new User();
	MyBeanUtils.populate(user,request.getParameterMap());
	//调用业务层登录功能
	UserService UserService=new UserServiceImp();
	User user02=null;
	try {
	user02=UserService.userLogin(user);
	request.getSession().setAttribute("loginUser",user02);
	response.sendRedirect("/store_v5/index.jsp");
	return null;
	}catch(Exception e)
	{
	String msg=e.getMessage();
	System.out.println(msg);
	request.setAttribute("msg",msg);
	return "/jsp/login.jsp";
	}
	
 }
public String logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.getSession().invalidate();
	response.sendRedirect("/store_v5/index.jsp");
	
  return null;
}

}
