package com.erahub;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class ErahubJljaManageApplicationTest {

    @Test
    void test01() throws IOException {
        Workbook wookbook = null;
        wookbook = new XSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\fixed_asset_manage\\固定资产\\文档\\表\\资产类别.xlsx"));//得到工作簿

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        for(int i = 1 ; i <= lastRowNum ; i++){

        }

        System.out.println(lastRowNum);
    }
}
