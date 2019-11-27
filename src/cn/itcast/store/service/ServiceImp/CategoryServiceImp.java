package cn.itcast.store.service.ServiceImp;

import java.util.List;

import cn.itcast.store.dao.CategoryDao;
import cn.itcast.store.dao.daoImp.CategoryDaoImp;
import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;

public class CategoryServiceImp implements CategoryService {

	public List<Category> getAllCats()throws Exception{
		CategoryDao categoryDao=new CategoryDaoImp();
		return categoryDao.getAllCats();
		 
	}
}
