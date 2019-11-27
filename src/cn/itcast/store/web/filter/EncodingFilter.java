package cn.itcast.store.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
/**
 * 缁熶竴缂栫爜
 * @author Administrator
 *
 */
@WebServlet("/EncodingFilter")
public class EncodingFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		//1.寮鸿浆
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) resp;
		//System.out.println("@@@@@@@@@@@@@@@@@@");		
		//2.鏀捐
		chain.doFilter(new MyRequest(request), response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}

//涔嬪墠鐨凪yRequest澧炲己浜唕equest.getParameter("name");鏂规硶
//澧炲己浜嗘墍鏈夌殑鑾峰彇鍙傛暟鐨勬柟娉時equest.getParameterValues("name");
//澧炲己浜嗘墍鏈夌殑鑾峰彇鍙傛暟鐨勬柟娉時equest.getParameterMap();
class MyRequest extends HttpServletRequestWrapper{
	private HttpServletRequest request;
	private boolean flag=true;
	
	
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.request=request;
	}
	
	@Override
	public String getParameter(String name) {  
		if(name==null || name.trim().length()==0){
			return null;
		}
		String[] values = getParameterValues(name);
		if(values==null || values.length==0){
			return null;
		}
		
		return values[0];
	}
	
	@Override
	/**
	 * hobby=[eat,drink]
	 */
	public String[] getParameterValues(String name) {
		if(name==null || name.trim().length()==0){
			return null;
		}
		Map<String, String[]> map = getParameterMap();
		if(map==null || map.size()==0){
			return null;
		}
		
		return map.get(name);
	}
	
	@Override
	/**
	 * map{ username=[tom],password=[123],hobby=[eat,drink]}
	 */
	public Map<String,String[]> getParameterMap() {  
		
		/**
		 * 棣栧厛鍒ゆ柇璇锋眰鏂瑰紡
		 * 鑻ヤ负post  request.setchar...(utf-8)
		 * 鑻ヤ负get 灏唌ap涓殑鍊奸亶鍘嗙紪鐮佸氨鍙互浜�
		 */
		String method = request.getMethod();
		if("post".equalsIgnoreCase(method)){
			try {
				request.setCharacterEncoding("utf-8");
				return request.getParameterMap();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("get".equalsIgnoreCase(method)){
			Map<String,String[]> map = request.getParameterMap();
			if(flag){
				for (String key:map.keySet()) {
					String[] arr = map.get(key);
					//缁х画閬嶅巻鏁扮粍
					for(int i=0;i<arr.length;i++){
						//缂栫爜
						try {
							arr[i]=new String(arr[i].getBytes("iso-8859-1"),"utf-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
				flag=false;
			}
			//闇�瑕侀亶鍘唌ap 淇敼value鐨勬瘡涓�涓暟鎹殑缂栫爜
			
			return map;
		}
		
		return super.getParameterMap();
	}
	
}