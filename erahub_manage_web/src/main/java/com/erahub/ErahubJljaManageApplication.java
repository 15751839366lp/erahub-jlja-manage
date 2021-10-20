package com.erahub;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @Author lipeng
 * @Date 2020/12/15 13:14
 * @Version 1.0
 **/
@SpringBootApplication
@EnableTransactionManagement  //开启事务管理
@MapperScan("com.erahub.**.mapper") //扫描mapper
@Import(FdfsClientConfig.class)
public class ErahubJljaManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErahubJljaManageApplication.class,args);
    }
}
