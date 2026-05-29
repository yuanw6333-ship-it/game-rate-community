package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.entity.Category;
import com.gamerate.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<CategoryVO> listEnabledCategories();
}
