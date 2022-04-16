package com.erahub;

import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import com.erahub.fixedasset.metadata.service.imp.FixedAssetCategoryServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
public class ErahubJljaManageApplicationTest {

    @Autowired
    private FixedAssetCategoryService fixedAssetCategoryService;

    @Test
    void fixedAssetCategoryImport() throws IOException {
        Workbook wookbook = null;
        wookbook = new XSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\fixed_asset_manage\\固定资产\\文档\\表\\资产类别.xlsx"));//得到工作簿

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        ArrayList<FixedAssetCategory> fixedAssetCategories = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("0");


        for(int i = 1 ; i <= lastRowNum ; i++){
            FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
            Row row = sheet.getRow(i);
            Date date = new Date();

            fixedAssetCategory.setCategoryId(Long.valueOf(decimalFormat.format(row.getCell(0).getNumericCellValue())));
            fixedAssetCategory.setCategoryName(row.getCell(1).getStringCellValue());
            fixedAssetCategory.setCategoryLevel(Long.valueOf(decimalFormat.format(row.getCell(2).getNumericCellValue())));
            fixedAssetCategory.setCategoryDetailed(Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
            fixedAssetCategory.setStatus(Long.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
            fixedAssetCategory.setDepreciationMethodId(Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
            if(!StringUtils.isEmpty(row.getCell(6))){
                fixedAssetCategory.setMeasureUnit(row.getCell(6).getStringCellValue());
            }

            if(!StringUtils.isEmpty(row.getCell(7))){
                fixedAssetCategory.setCapacityUnit(row.getCell(7).getStringCellValue());
            }

            fixedAssetCategory.setDepreciationPeriod(Long.valueOf(decimalFormat.format(row.getCell(8).getNumericCellValue()*100)));
            fixedAssetCategory.setEstimatedTotalWorkload(Long.valueOf(decimalFormat.format(row.getCell(9).getNumericCellValue()*100)));
            fixedAssetCategory.setNetResidualValue(Long.valueOf(decimalFormat.format(row.getCell(10).getNumericCellValue()*100)));
            fixedAssetCategory.setRemark(row.getCell(11).getStringCellValue());


            fixedAssetCategories.add(fixedAssetCategory);
        }

        fixedAssetCategoryService.saveBatch(fixedAssetCategories);
    }
}
