package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.enums.fixedasset.metadata.FixedAssetCategoryStatusEnum;
import com.erahub.common.enums.system.UserStatusEnum;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.system.User;
import com.erahub.common.response.ActiveUser;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.fixedasset.metadata.converter.FixedAssetCategoryConverter;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/
@Service
@Transactional
public class FixedAssetCategoryServiceImpl extends ServiceImpl<FixedAssetCategoryMapper, FixedAssetCategory> implements FixedAssetCategoryService {

    @Autowired
    private FixedAssetCategoryMapper fixedAssetCategoryMapper;

    @Autowired
    private FixedAssetCategoryConverter fixedAssetCategoryConverter;

    /**
     * 查询资产类别列表
     *
     * @return
     */
    @Override
    public PageVO<FixedAssetCategoryVO> findFixedAssetCategoryList(FixedAssetCategoryDTO fixedAssetCategoryDTO) {

        IPage<FixedAssetCategory> fixedAssetCategoryIPage = new Page<>(fixedAssetCategoryDTO.getPageNum(), fixedAssetCategoryDTO.getPageSize());

        BigDecimal depreciationPeriod = fixedAssetCategoryDTO.getDepreciationPeriod();
        BigDecimal estimatedTotalWorkload = fixedAssetCategoryDTO.getEstimatedTotalWorkload();
        BigDecimal netResidualValue = fixedAssetCategoryDTO.getNetResidualValue();
        if (depreciationPeriod != null) {
            fixedAssetCategoryDTO.setDepreciationPeriod(ArithmeticUtils.mul(depreciationPeriod.toString(), "100"));
        }
        if (estimatedTotalWorkload != null) {
            fixedAssetCategoryDTO.setEstimatedTotalWorkload(ArithmeticUtils.mul(estimatedTotalWorkload.toString(), "100"));
        }
        if (netResidualValue != null) {
            fixedAssetCategoryDTO.setNetResidualValue(ArithmeticUtils.mul(netResidualValue.toString(), "100"));
        }


        fixedAssetCategoryIPage = fixedAssetCategoryMapper.selectPageList(fixedAssetCategoryIPage, fixedAssetCategoryDTO);
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryIPage.getRecords();
        List<FixedAssetCategoryVO> fixedAssetCategoryVOS = fixedAssetCategoryConverter.converterToFixedAssetCategoryVOList(fixedAssetCategoryList);

        return new PageVO<>(fixedAssetCategoryIPage.getTotal(), fixedAssetCategoryVOS);
    }

    /**
     * 更新资产类别状态
     *
     * @param fixedAssetCategoryDTO
     */
    @Override
    public void changeFixedAssetCategoryStatus(FixedAssetCategoryDTO fixedAssetCategoryDTO) {
        FixedAssetCategory dbFixedAssetCategory = fixedAssetCategoryMapper.selectById(fixedAssetCategoryDTO.getCategoryId());
        if (dbFixedAssetCategory == null) {
//            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR, "要更新状态的资产类别不存在");
        }

        FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
        fixedAssetCategory.setCategoryId(fixedAssetCategoryDTO.getCategoryId());
        fixedAssetCategory.setStatus(fixedAssetCategoryDTO.getStatus() > 0l ? FixedAssetCategoryStatusEnum.DISABLE.getStatusCode() :
                FixedAssetCategoryStatusEnum.AVAILABLE.getStatusCode());
        fixedAssetCategoryMapper.updateById(fixedAssetCategory);

    }
}
