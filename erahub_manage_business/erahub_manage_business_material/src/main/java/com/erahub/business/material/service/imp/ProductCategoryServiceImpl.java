package com.erahub.business.material.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.business.material.mapper.ProductCategoryMapper;
import com.erahub.business.material.mapper.ProductMapper;
import com.erahub.business.material.service.ProductCategoryService;
import com.erahub.business.material.converter.ProductCategoryConverter;
import com.erahub.common.error.business.BusinessCodeEnum;
import com.erahub.common.error.business.BusinessException;
import com.erahub.common.model.business.Product;
import com.erahub.common.model.business.ProductCategory;
import com.erahub.business.material.utils.CategoryTreeBuilder;
import com.erahub.common.utils.ListPageUtils;
import com.erahub.common.vo.business.ProductCategoryTreeNodeVO;
import com.erahub.common.vo.business.ProductCategoryVO;
import com.erahub.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/16 17:19
 * @Version 1.0
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {


    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 商品类别列表
     *
     * @param pageNum
     * @param pageSize
     * @param ProductCategoryVO
     * @return
     */
    @Override
    public PageVO<ProductCategoryVO> findProductCategoryList(Integer pageNum, Integer pageSize, ProductCategoryVO ProductCategoryVO) {
        IPage<ProductCategory> productCategoryIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductCategory> productCategoryQueryWrapper = new QueryWrapper<>();
        productCategoryQueryWrapper.orderByAsc("sort");

        if (ProductCategoryVO.getName() != null && !"".equals(ProductCategoryVO.getName())) {
            productCategoryQueryWrapper.like("name", ProductCategoryVO.getName());
        }
        productCategoryIPage = productCategoryMapper.selectPage(productCategoryIPage, productCategoryQueryWrapper);
        List<ProductCategory> productCategories = productCategoryIPage.getRecords();
        List<ProductCategoryVO> categoryVOS = ProductCategoryConverter.converterToVOList(productCategories);

        return new PageVO<>(productCategoryIPage.getTotal(), categoryVOS);
    }


    /**
     * 添加商品类别
     *
     * @param ProductCategoryVO
     */
    @Override
    public void add(ProductCategoryVO ProductCategoryVO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(ProductCategoryVO, productCategory);
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.insert(productCategory);
    }

    /**
     * 编辑商品类别
     *
     * @param id
     * @return
     */
    @Override
    public ProductCategoryVO edit(Long id) {
        ProductCategory productCategory = productCategoryMapper.selectById(id);
        return ProductCategoryConverter.converterToProductCategoryVO(productCategory);
    }

    /**
     * 更新商品类别
     *
     * @param id
     * @param ProductCategoryVO
     */
    @Override
    public void update(Long id, ProductCategoryVO ProductCategoryVO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(ProductCategoryVO, productCategory);
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.updateById(productCategory);
    }

    /**
     * 删除商品类别
     *
     * @param id
     */
    @Override
    public void delete(Long id) throws BusinessException {
        ProductCategory category = productCategoryMapper.selectById(id);
        if (null == category) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR, "该分类不存在");
        } else {
            //检查是否存在子分类
            QueryWrapper<ProductCategory> productCategoryQueryWrapper = new QueryWrapper<>();
            productCategoryQueryWrapper.eq("pid", id);

            int childCount = productCategoryMapper.selectCount(productCategoryQueryWrapper);
            if (childCount != 0) {
                throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR, "存在子节点,无法直接删除");
            }
            //检查该分类是否有物资引用
            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.eq("one_category_id", id)
                    .or().eq("two_category_id", id)
                    .or().eq("three_category_id", id);

            if (productMapper.selectCount(productQueryWrapper) != 0) {
                throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR, "该分类存在物资引用,无法直接删除");
            }
            productCategoryMapper.deleteById(id);
        }
    }

    /**
     * 所有商品类别
     *
     * @return
     */
    @Override
    public List<ProductCategoryVO> findAll() {
        List<ProductCategory> productCategories = productCategoryMapper.selectList(null);
        return ProductCategoryConverter.converterToVOList(productCategories);
    }

    /**
     * 分类树形结构
     *
     * @return
     */
    @Override
    public PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize) {
        List<ProductCategoryVO> productCategoryVOList = findAll();
        List<ProductCategoryTreeNodeVO> nodeVOS = ProductCategoryConverter.converterToTreeNodeVO(productCategoryVOList);
        List<ProductCategoryTreeNodeVO> tree = CategoryTreeBuilder.build(nodeVOS);
        List<ProductCategoryTreeNodeVO> page;
        if (pageSize != null && pageNum != null) {
            page = ListPageUtils.page(tree, pageSize, pageNum);
            return new PageVO<>(tree.size(), page);
        } else {
            return new PageVO<>(tree.size(), tree);
        }
    }

    /**
     * 获取父级分类（2级树）
     *
     * @return
     */
    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        List<ProductCategoryVO> productCategoryVOList = findAll();
        List<ProductCategoryTreeNodeVO> nodeVOS = ProductCategoryConverter.converterToTreeNodeVO(productCategoryVOList);
        return CategoryTreeBuilder.buildParent(nodeVOS);
    }

}
