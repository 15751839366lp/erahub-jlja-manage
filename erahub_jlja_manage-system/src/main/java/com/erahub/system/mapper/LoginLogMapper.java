package com.erahub.system.mapper;


import com.erahub.common.model.system.LoginLog;
import com.erahub.common.vo.system.UserVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2020/3/20 19:09
 * @Version 1.0
 **/
public interface LoginLogMapper extends Mapper<LoginLog> {

    /**
     * 用户登入报表
     * @param userVO
     * @return
     */
    List<Map<String,Object>> userLoginReport(UserVO userVO);

}
