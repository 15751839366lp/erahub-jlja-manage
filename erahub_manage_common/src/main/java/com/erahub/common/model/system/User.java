package com.erahub.common.model.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import java.util.Date;

@Data
@Excel("user")
@TableName("tb_user")
public class User {
    @ColumnWidth(20)
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "编号")
    private Long id;

    @ColumnWidth(40)
    @ExcelProperty(value = "用户名")
    private String username;

    @ColumnWidth(40)
    @ExcelProperty(value = "昵称")
    private String nickname;

    @ColumnWidth(40)
    @ExcelProperty(value = "邮箱")
    private String email;

    @ColumnWidth(40)
    @ExcelProperty(value = "电话号码")
    private String phoneNumber;

    private Integer status;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @ColumnWidth(60)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    @ExcelProperty(value = "修改时间")
    private Date modifiedTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "性别")
    private Integer sex;

    @ColumnWidth(40)
    @ExcelProperty(value = "密码盐值")
    private String salt;

    @ColumnWidth(20)
    @ExcelProperty(value = "用户类型")
    private Integer type;

    @ColumnWidth(40)
    @ExcelProperty(value = "用户密码")
    private String password;

    @ColumnWidth(40)
    @DateTimeFormat("yyyy/MM/dd")
    @ExcelProperty(value = "出生日期")
    private Date birth;

    private Long departmentId;

    @ColumnWidth(60)
    @ExcelProperty(value = "头像url")
    private String avatar;

}
