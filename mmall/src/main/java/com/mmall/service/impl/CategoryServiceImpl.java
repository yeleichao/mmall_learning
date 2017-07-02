package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by yeleichao on 2017-7-2.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加商品
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName, Integer parentId){
        if(StringUtils.isBlank(categoryName) || parentId == null){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int resultCount = categoryMapper.insert(category);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }


    /**
     * 更新商品名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponse setCategoryName(Integer categoryId, String categoryName){
        if(StringUtils.isBlank(categoryName) || categoryId == null){
            return ServerResponse.createByErrorMessage("更新商品名称参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("更新商品名称成功");
        }
        return ServerResponse.createBySuccessMessage("更新商品名称失败");
    }

    public ServerResponse<List<Category>> getChildrenParallerCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点的id和孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null) {
            for(Category categoryItem : categorySet)
                categoryIdList.add(categoryItem.getId());
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }


    //递归算法，算出字节点
    public Set<Category> findChildrenCategory(Set<Category> categorySet , Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点，递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem : categoryList){
            findChildrenCategory(categorySet, categoryItem.getId());
        }

        return categorySet;
    }
}
