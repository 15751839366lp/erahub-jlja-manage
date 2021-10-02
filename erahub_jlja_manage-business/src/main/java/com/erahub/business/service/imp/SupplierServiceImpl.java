package com.erahub.business.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.business.mapper.SupplierMapper;
import com.erahub.business.service.SupplierService;
import com.erahub.business.converter.SupplierConverter;
import com.erahub.common.model.business.InStock;
import com.erahub.common.model.business.Supplier;
import com.erahub.common.vo.business.SupplierVO;
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
public class SupplierServiceImpl implements SupplierService {


    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 供应商列表
     * @param pageNum
     * @param pageSize
     * @param supplierVO
     * @return
     */
    @Override
    public PageVO<SupplierVO> findSupplierList(Integer pageNum, Integer pageSize, SupplierVO supplierVO) {
        IPage<Supplier> supplierIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Supplier> supplierQueryWrapper = new QueryWrapper<>();
        supplierQueryWrapper.orderByAsc("sort");

        if (supplierVO.getName() != null && !"".equals(supplierVO.getName())) {
            supplierQueryWrapper.like("name", supplierVO.getName());
        }
        if (supplierVO.getContact() != null && !"".equals(supplierVO.getContact())) {
            supplierQueryWrapper.like("contact", supplierVO.getContact());
        }
        if (supplierVO.getAddress() != null && !"".equals(supplierVO.getAddress())) {
            supplierQueryWrapper.like("address", supplierVO.getAddress());
        }
        supplierIPage = supplierMapper.selectPage(supplierIPage, supplierQueryWrapper);
        List<Supplier> suppliers = supplierIPage.getRecords();
        List<SupplierVO> categoryVOS= SupplierConverter.converterToVOList(suppliers);

        return new PageVO<>(supplierIPage.getTotal(), categoryVOS);
    }



    /**
     * 添加供应商
     * @param SupplierVO
     */
    @Override
    public Supplier add(SupplierVO SupplierVO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(SupplierVO,supplier);
        supplier.setCreateTime(new Date());
        supplier.setModifiedTime(new Date());
        supplierMapper.insert(supplier);
        return supplier;
    }

    /**
     * 编辑供应商
     * @param id
     * @return
     */
    @Override
    public SupplierVO edit(Long id) {
        Supplier supplier = supplierMapper.selectById(id);
        return SupplierConverter.converterToSupplierVO(supplier);
    }

    /**
     * 更新供应商
     * @param id
     * @param SupplierVO
     */
    @Override
    public void update(Long id, SupplierVO SupplierVO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(SupplierVO,supplier);
        supplier.setModifiedTime(new Date());
        supplierMapper.updateById(supplier);
    }

    /**
     * 删除供应商
     * @param id
     */
    @Override
    public void delete(Long id) {
        supplierMapper.deleteById(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<SupplierVO> findAll() {
        List<Supplier> suppliers = supplierMapper.selectList(null);
        return SupplierConverter.converterToVOList(suppliers);
    }

}
