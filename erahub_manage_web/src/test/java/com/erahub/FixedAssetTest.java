package com.erahub;

import com.erahub.common.model.fixedasset.assetoperation.FixedAsset;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.fixedasset.assetoperation.service.FixedAssetService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
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

/**
 * @Author lipeng
 * @Date 2022/5/2 12:49
 * @Version 1.0
 **/
@SpringBootTest
public class FixedAssetTest {
    @Autowired
    private FixedAssetService fixedAssetService;

    @Test
    void importList() throws IOException {
        Workbook wookbook = null;
        wookbook = new HSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\fixed_asset_manage\\固定资产\\文档\\表\\ZJ202204.XLS"));//得到工作簿

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        ArrayList<FixedAsset> fixedAssets = new ArrayList<>();

        DataFormatter dataFormatter = new DataFormatter();
        DecimalFormat decimalFormat = new DecimalFormat("0");

        for (int i = 1; i <= lastRowNum; i++) {
            FixedAsset fixedAsset = new FixedAsset();
            Row row = sheet.getRow(i);

            if(row.getCell(9) != null && Long.valueOf(decimalFormat.format(row.getCell(9).getNumericCellValue())) == 1){
                String fixedAssetId = dataFormatter.formatCellValue(row.getCell(0)).trim();
                String sectionId = dataFormatter.formatCellValue(row.getCell(1)).trim();
                String categoryId = dataFormatter.formatCellValue(row.getCell(2)).trim();
                Long depreciationMethodId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(11)).trim());
                fixedAsset.setFixedAssetId(fixedAssetId);
                fixedAsset.setSectionId(sectionId);
                fixedAsset.setCategoryId(categoryId);
                fixedAsset.setDepreciationMethodId(depreciationMethodId);
                fixedAssets.add(fixedAsset);
            }
        }

        fixedAssetService.saveBatch(fixedAssets);
    }
}
