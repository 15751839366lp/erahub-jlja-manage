package com.erahub.business.material.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.business.material.mapper.ProductMapper;
import com.erahub.business.material.mapper.ProductStockMapper;
import com.erahub.business.material.service.ProductService;
import com.erahub.business.material.converter.ProductConverter;
import com.erahub.common.error.business.material.BusinessCodeEnum;
import com.erahub.common.error.business.material.BusinessException;
import com.erahub.common.model.business.material.Product;
import com.erahub.common.vo.business.material.ProductStockVO;
import com.erahub.common.vo.business.material.ProductVO;
import com.erahub.common.vo.common.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author lipeng
 * @Date 2020/3/16 17:19
 * @Version 1.0
 **/
@Service
@Transactional
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductMapper productMapper;


    @Autowired
    private ProductStockMapper productStockMapper;



    /**
     * ๅๅๅ่กจ
     * @param pageNum
     * @param pageSize
     * @param productVO
     * @return
     */
    @Override
    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO) {
        List<Product> products;

        IPage<Product> productIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();

        if (productVO.getStatus() != null) {
            productQueryWrapper.eq("status", productVO.getStatus());
        }
        if(productVO.getThreeCategoryId()!=null){
            productQueryWrapper.eq("one_category_id", productVO.getOneCategoryId());
            productQueryWrapper.eq("two_category_id", productVO.getTwoCategoryId());
            productQueryWrapper.eq("three_category_id", productVO.getThreeCategoryId());

            productIPage = productMapper.selectPage(productIPage, productQueryWrapper);
            products = productIPage.getRecords();
            List<ProductVO> categoryVOS= ProductConverter.converterToVOList(products);

            return new PageVO<>(productIPage.getTotal(), categoryVOS);
        }
        if(productVO.getTwoCategoryId()!=null){
            productQueryWrapper.eq("one_category_id", productVO.getOneCategoryId());
            productQueryWrapper.eq("two_category_id", productVO.getTwoCategoryId());

            productIPage = productMapper.selectPage(productIPage, productQueryWrapper);
            products = productIPage.getRecords();
            List<ProductVO> categoryVOS=ProductConverter.converterToVOList(products);
            return new PageVO<>(productIPage.getTotal(), categoryVOS);
        }
        if(productVO.getOneCategoryId()!=null) {
            productQueryWrapper.eq("one_category_id", productVO.getOneCategoryId());

            productIPage = productMapper.selectPage(productIPage, productQueryWrapper);
            products = productIPage.getRecords();
            List<ProductVO> categoryVOS = ProductConverter.converterToVOList(products);
            return new PageVO<>(productIPage.getTotal(), categoryVOS);
        }
        productQueryWrapper.orderByAsc("sort");
        if (productVO.getName() != null && !"".equals(productVO.getName())) {
            productQueryWrapper.like("name", productVO.getName());
        }
        productIPage = productMapper.selectPage(productIPage, productQueryWrapper);
        products = productIPage.getRecords();
        List<ProductVO> categoryVOS=ProductConverter.converterToVOList(products);

        return new PageVO<>(productIPage.getTotal(), categoryVOS);
    }



    /**
     * ๆทปๅ?ๅๅ
     * @param ProductVO
     */
    @Override
    public void add(ProductVO ProductVO) {
        Product product = new Product();
        BeanUtils.copyProperties(ProductVO,product);
        product.setCreateTime(new Date());
        product.setModifiedTime(new Date());
        @NotNull(message = "ๅ็ฑปไธ่ฝไธบ็ฉบ") Long[] categoryKeys = ProductVO.getCategoryKeys();
        if(categoryKeys.length==3){
            product.setOneCategoryId(categoryKeys[0]);
            product.setTwoCategoryId(categoryKeys[1]);
            product.setThreeCategoryId(categoryKeys[2]);
        }
        product.setStatus(2);//ๆชๅฎกๆ?ธ
        product.setPNum(UUID.randomUUID().toString().substring(0,32));
        productMapper.insert(product);
    }

    /**
     * ็ผ่พๅๅ
     * @param id
     * @return
     */
    @Override
    public ProductVO edit(Long id) {
        Product product = productMapper.selectById(id);
        return ProductConverter.converterToProductVO(product);
    }

    /**
     * ๆดๆฐๅๅ
     * @param id
     * @param ProductVO
     */
    @Override
    public void update(Long id, ProductVO ProductVO) {
        Product product = new Product();
        BeanUtils.copyProperties(ProductVO,product);
        product.setModifiedTime(new Date());
        @NotNull(message = "ๅ็ฑปไธ่ฝไธบ็ฉบ") Long[] categoryKeys = ProductVO.getCategoryKeys();
        if(categoryKeys.length==3){
            product.setOneCategoryId(categoryKeys[0]);
            product.setTwoCategoryId(categoryKeys[1]);
            product.setThreeCategoryId(categoryKeys[2]);
        }
        productMapper.updateById(product);
    }

    /**
     * ๅ?้คๅๅ
     * @param id
     */
    @Override
    public void delete(Long id) throws BusinessException {
        Product t = new Product();
        t.setId(id);
        Product product = productMapper.selectById(id);
        //ๅชๆ็ฉ่ตๅคไบๅๆถ็ซ,ๆ่ๅพๅฎกๆ?ธ็ๆๅตไธๅฏๅ?้ค
        if(product.getStatus()!=1&&product.getStatus()!=2){
            throw new BusinessException(BusinessCodeEnum.PRODUCT_STATUS_ERROR);
        }else {
            productMapper.deleteById(id);
        }
    }

    /**
     * ็ฉ่ตๅบๅญๅ่กจ
     * @param pageNum
     * @param pageSize
     * @param productVO
     * @return
     */
    @Override
    public PageVO<ProductStockVO> findProductStocks(Integer pageNum, Integer pageSize, ProductVO productVO) {
        productVO.setPageNum(pageNum);
        productVO.setPageSize(pageSize);

        List<ProductStockVO> productStockVOList=productStockMapper.findProductStocks(productVO);
        Integer count =productStockMapper.findProductStocksCount(productVO);
        return new PageVO<>(count , productStockVOList);
    }

    /**
     * ๆๆๅบๅญไฟกๆฏ
     * @return
     */
    @Override
    public List<ProductStockVO> findAllStocks(Integer pageNum, Integer pageSize, ProductVO productVO) {
        productVO.setPageNum(pageNum);
        productVO.setPageSize(pageSize);
        return productStockMapper.findAllStocks(productVO);
    }

    /**
     * ็งปๅฅๅๆถ็ซ
     * @param id
     */
    @Override
    public void remove(Long id) throws BusinessException {
        Product t = new Product();
        t.setId(id);
        Product product = productMapper.selectById(id);
        if(product.getStatus()!=0){
            throw new BusinessException(BusinessCodeEnum.PRODUCT_STATUS_ERROR);
        }else {
            t.setStatus(1);
            productMapper.updateById(t);
        }
    }

    /**
     * ไปๅๆถ็ซๆขๅคๆฐๆฎ
     * @param id
     */
    @Override
    public void back(Long id) throws BusinessException {
        Product t = new Product();
        t.setId(id);
        Product product = productMapper.selectById(id);
        if(product.getStatus()!=1){
            throw new BusinessException(BusinessCodeEnum.PRODUCT_STATUS_ERROR);
        }else {
            t.setStatus(0);
            productMapper.updateById(t);
        }
    }

    /**
     * ็ฉ่ตๅฎกๆ?ธ
     * @param id
     */
    @Override
    public void publish(Long id) throws BusinessException {
        Product t = new Product();
        t.setId(id);
        Product product = productMapper.selectById(id);
        if(product.getStatus()!=2){
            throw new BusinessException(BusinessCodeEnum.PRODUCT_STATUS_ERROR);
        }else {
            t.setStatus(0);
            productMapper.updateById(t);
        }
    }



}
