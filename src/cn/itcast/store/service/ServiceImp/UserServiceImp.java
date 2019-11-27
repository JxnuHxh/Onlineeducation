package cn.itcast.store.service.ServiceImp;

import java.sql.SQLException;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.dao.daoImp.UserDaoImp;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;

public class UserServiceImp implements UserService {

	@Override
	public void userRegist(User user) throws SQLException {
		UserDao userDao=new UserDaoImp();
		userDao.userRegist(user);
	}

	@Override
	public boolean userActive(String code) throws SQLException {
		UserDao userDao=new UserDaoImp();
	User user= userDao.userActive(code);
	if(user!=null)
	{
		System.out.println("cg");

		
		user.setState(1);
		user.setCode(null);
		userDao.updateUser(user);
			return true;
	}else {
		System.out.println("yunxing1shibai1");
		return false;
	}
	}

	@Override
	public User userLogin(User user) throws SQLException {
		UserDao userDao=new UserDaoImp();
		User uu=userDao.userLogin(user);
		if(null==uu)
		{
			throw new RuntimeException("mimacuowu!");
		}else if(uu.getState()==0){
			throw new RuntimeException("yonghuweijihuo!");
		}else {
			return uu;
		}
	
	}
	

}
