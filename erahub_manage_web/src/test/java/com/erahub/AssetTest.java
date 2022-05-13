package com.erahub;

import com.erahub.asset.assetdetail.service.AssetService;
import com.erahub.common.model.asset.assetdetail.Asset;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.utils.SnowflakeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/5/2 12:49
 * @Version 1.0
 **/
@SpringBootTest
public class AssetTest {
    @Autowired
    private AssetService assetService;

    @Test
    void importList() throws IOException, ParseException {
        Workbook wookbook = null;
        wookbook = new HSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\asset\\固定资产\\文档\\ZJ202204.XLS"));//得到工作簿

        SnowflakeUtils snowflakeUtils = new SnowflakeUtils();

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        ArrayList<Asset> assets = new ArrayList<>();

        DataFormatter dataFormatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf8 = new SimpleDateFormat("yyyyMMdd");

        for (int i = 2; i <= lastRowNum; i++) {
            Asset asset = new Asset();
            Row row = sheet.getRow(i);

            if (!StringUtils.isEmpty(dataFormatter.formatCellValue(row.getCell(9)).trim())
                    && Long.valueOf(dataFormatter.formatCellValue(row.getCell(9)).trim()) == 1) {
                asset.setAssetUniqueId(snowflakeUtils.nextId());
                asset.setManufacturingNo(String.valueOf(snowflakeUtils.nextId()).substring(10));

                if (StringUtils.isEmpty(dataFormatter.formatCellValue(row.getCell(12)).trim())) {
                    asset.setRecordedDate(new Date());
                } else if (dataFormatter.formatCellValue(row.getCell(12)).trim().length() == 6) {
                    asset.setRecordedDate(sdf6.parse(dataFormatter.formatCellValue(row.getCell(12)).trim()));
                } else if (dataFormatter.formatCellValue(row.getCell(12)).trim().length() == 8) {
                    asset.setRecordedDate(sdf8.parse(dataFormatter.formatCellValue(row.getCell(12)).trim()));
                }

                asset.setAccountingVoucher(dataFormatter.formatCellValue(row.getCell(13)).trim());
                asset.setAssetId(dataFormatter.formatCellValue(row.getCell(0)).trim());
                asset.setAssetName(dataFormatter.formatCellValue(row.getCell(3)).trim());
                asset.setAssetCategoryId(dataFormatter.formatCellValue(row.getCell(2)).trim());
                asset.setSectionId(dataFormatter.formatCellValue(row.getCell(1)).trim());
                asset.setAssetQuantity(Long.valueOf(dataFormatter.formatCellValue(row.getCell(9)).trim()));
                asset.setMeasureUnit(dataFormatter.formatCellValue(row.getCell(8)).trim());

                if (StringUtils.isEmpty(dataFormatter.formatCellValue(row.getCell(6)).trim())) {
                    asset.setProductionDate(new Date());
                } else if (dataFormatter.formatCellValue(row.getCell(6)).trim().length() == 6) {
                    asset.setProductionDate(sdf6.parse(dataFormatter.formatCellValue(row.getCell(6)).trim()));
                } else if (dataFormatter.formatCellValue(row.getCell(6)).trim().length() == 8) {
                    asset.setProductionDate(sdf8.parse(dataFormatter.formatCellValue(row.getCell(6)).trim()));
                }

                if (StringUtils.isEmpty(dataFormatter.formatCellValue(row.getCell(7)).trim())) {
                    asset.setUsedDate(new Date());
                } else if (dataFormatter.formatCellValue(row.getCell(7)).trim().length() == 6) {
                    asset.setUsedDate(sdf6.parse(dataFormatter.formatCellValue(row.getCell(6)).trim()));
                } else if (dataFormatter.formatCellValue(row.getCell(7)).trim().length() == 8) {
                    asset.setUsedDate(sdf8.parse(dataFormatter.formatCellValue(row.getCell(7)).trim()));
                }

                asset.setAssetProjectId("PTLG");
                asset.setAssetSourceId(1l);
                asset.setAssetOwnershipId(1l);
                asset.setAssetEconomicUseId(1l);
                asset.setServiceConditionId(1l);
                asset.setTechnicalConditionId(1l);
                asset.setHistoricalCost(new BigDecimal(dataFormatter.formatCellValue(row.getCell(15)).trim().replace(",", "")));
                asset.setAccrualDepreciation(0);
                asset.setDepreciationMethodId(Long.valueOf(dataFormatter.formatCellValue(row.getCell(11)).trim()));
                asset.setDepreciationPeriod(new BigDecimal(dataFormatter.formatCellValue(row.getCell(5)).trim().replace(",", "")));
                asset.setEstimatedTotalWorkload(new BigDecimal(0));
                asset.setNetResidualValue(new BigDecimal(dataFormatter.formatCellValue(row.getCell(10)).trim().replace(",", "")));
                asset.setType(0);
                asset.setOperation(0);
                asset.setStatus(0);


                assets.add(asset);
            }
        }

        assetService.saveBatch(assets);
    }

    @Test
    void depreciation() {
//        BigDecimal bigDecimal = new BigDecimal(0);
//        List<Asset> list = assetService.list();
//        for (Asset asset : list) {
//            BigDecimal historicalCost = asset.getHistoricalCost();
//            BigDecimal netResidualValue = asset.getNetResidualValue();
//            BigDecimal depreciationPeriod = asset.getDepreciationPeriod();
//
//        }

    }

    public static void main(String[] args) {
        BigDecimal historicalCost = new BigDecimal("337091");
        BigDecimal depreciationPeriod = new BigDecimal("20");
        BigDecimal netResidualValue = new BigDecimal("0.05");
        BigDecimal residualValue = historicalCost.multiply(netResidualValue);
        BigDecimal AccumulatedDepreciationAmount = new BigDecimal("0");
        BigDecimal depreciationValue = historicalCost.subtract(residualValue);
        BigDecimal tempDepreciationValue = historicalCost.subtract(residualValue);

        for (int i = 0; i < depreciationPeriod.multiply(new BigDecimal(12)).intValue(); i++) {
            BigDecimal tempDepreciationAmount = tempDepreciationValue
                    .divide(new BigDecimal(depreciationPeriod.multiply(new BigDecimal(12)).intValue() - i),2);
            tempDepreciationValue = tempDepreciationValue.subtract(tempDepreciationAmount);
            AccumulatedDepreciationAmount = AccumulatedDepreciationAmount.add(tempDepreciationAmount);
            System.out.println("第" + (i + 1) + "次折旧，本期折旧额：" + tempDepreciationAmount
                    + "，累计折旧额：" + AccumulatedDepreciationAmount);
        }

        System.out.println("应提折旧额：" + depreciationValue);
        System.out.println("累计折旧额：" + AccumulatedDepreciationAmount);
    }
}
