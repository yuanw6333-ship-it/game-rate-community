package com.gamerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.entity.Category;
import com.gamerate.mapper.CategoryMapper;
import com.gamerate.service.CategoryService;
import com.gamerate.vo.CategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private static final int STATUS_ENABLED = 1;

    @Override
    public List<CategoryVO> listEnabledCategories() {
        return lambdaQuery()
                .eq(Category::getStatus, STATUS_ENABLED)
                .orderByAsc(Category::getSortOrder)
                .orderByAsc(Category::getId)
                .list()
                .stream()
                .map(this::toVO)
                .toList();
    }

    private CategoryVO toVO(Category category) {
        return CategoryVO.builder()
                .id(category.getId())
                .name(category.getName())
                .code(category.getCode())
                .description(category.getDescription())
                .sortOrder(category.getSortOrder())
                .build();
    }
}
