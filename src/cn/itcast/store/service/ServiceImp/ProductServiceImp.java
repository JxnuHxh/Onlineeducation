package cn.itcast.store.service.ServiceImp;

import java.util.List;

import cn.itcast.store.dao.ProductDao;
import cn.itcast.store.dao.daoImp.ProductDaoImp;
import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;

public class ProductServiceImp implements ProductService {

	ProductDao productDao=new 	ProductDaoImp();
	@Override
	public List<Product> findNews() throws Exception {

		return productDao.findNews();
	}

	@Override
	public List<Product> findHots() throws Exception {
		
		return productDao.findHots();
	}

	@Override
	public Product findProductByPid(String pid) throws Exception {
		// TODO Auto-generated method stub
		return productDao.findProductByPid(pid) ;
	}

	@Override
	public PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception {
		//创建pagemodel对象 目的：计算分页参数
		int totalRecords=productDao.findTotalRecords(cid);
		PageModel pm=new PageModel(curNum,totalRecords,12);
		//关联集合
		List list = productDao.findProductsByCidWithPage(cid,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		pm.setUrl("ProductServlet?method=findProductsByCidWithPage&cid="+cid);
		return pm;
	}

}
