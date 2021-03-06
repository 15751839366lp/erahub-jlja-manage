package com.erahub.system.service;


import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Menu;
import com.erahub.common.vo.system.MenuNodeVO;
import com.erahub.common.vo.system.MenuVO;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/10 11:55
 * @Version 1.0
 **/
public interface MenuService {
    /**
     * 获取菜单树
     * @return
     */
    List<MenuNodeVO> findMenuTree(Boolean isAuthority);

    /**
     * 添加菜单
     * @param menuVO
     */
    Menu add(MenuVO menuVO) throws SystemException;

    /**
     * 删除节点
     * @param id
     */
    void delete(Long id) throws SystemException;

    /**
     * 编辑节点
     * @param id
     * @return
     */
    MenuVO edit(Long id) throws SystemException;

    /**
     * 更新节点
     * @param id
     */
    void update(Long id, MenuVO menuVO) throws SystemException;

    /**
     * 所有展开菜单的ID
     * @return
     */
    List<Long> findOpenIds();


    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> findAll();

}
