package cn.itcast.store.service;

import java.util.List;

import cn.itcast.store.domain.Category;

public interface CategoryService {

	List<Category> getAllCats()throws Exception;
}
