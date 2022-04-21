package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.fixedasset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.fixedasset.metadata.service.DepreciationMethodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author lipeng
 * @Date 2022/4/21 18:20
 * @Version 1.0
 **/
@Service
@Transactional
public class DepreciationMethodServiceImpl extends ServiceImpl<DepreciationMethodMapper, DepreciationMethod> implements DepreciationMethodService {

}
