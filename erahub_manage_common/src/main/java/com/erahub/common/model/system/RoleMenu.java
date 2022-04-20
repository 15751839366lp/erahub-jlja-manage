package com.erahub.common.model.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_role_menu")
public class RoleMenu {

    @TableId
    private Long id;

    private Long roleId;

    private Long menuId;

}
