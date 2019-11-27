package cn.itcast.store.dao.daoImp;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.store.dao.CategoryDao;
import cn.itcast.store.domain.Category;
import cn.itcast.store.utils.JDBCUtils;

public class CategoryDaoImp implements CategoryDao {

	@Override
	public List<Category> getAllCats() throws Exception{
	String sql="select* from category";
	QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
	return qr.query(sql, new BeanListHandler<Category>(Category.class));
	
	}

}
