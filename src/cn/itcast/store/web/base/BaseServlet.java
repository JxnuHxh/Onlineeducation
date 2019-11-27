package cn.itcast.store.web.base;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// localhost:8080/store/productServlet?method=addProduct
		String method = req.getParameter("method");

		if (null == method || "".equals(method) || method.trim().equals("")) {
			method = "execute";
		}

		// 娉ㄦ剰:姝ゅ鐨則his浠ｈ〃鐨勬槸瀛愮被鐨勫璞�
		// System.out.println(this);
		// 瀛愮被瀵硅薄瀛楄妭鐮佸璞�
		Class clazz = this.getClass();//反射

		try {
			// 鏌ユ壘瀛愮被瀵硅薄瀵瑰簲鐨勫瓧鑺傜爜涓殑鍚嶇О涓簃ethod鐨勬柟娉�.杩欎釜鏂规硶鐨勫弬鏁扮被鍨嬫槸:HttpServletRequest.class,HttpServletResponse.class
			Method md = clazz.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
			if(null!=md){
				String jspPath = (String) md.invoke(this, req, resp);
				if (null != jspPath) {
					req.getRequestDispatcher(jspPath).forward(req, resp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 榛樿鏂规硶
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return null;
	}

}