package com.erahub;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.fixedasset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.DepreciationMethodService;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@SpringBootTest
public class FixedAssetCategoryTest {

    @Autowired
    private FixedAssetCategoryService fixedAssetCategoryService;

    @Autowired
    private FixedAssetCategoryMapper fixedAssetCategoryMapper;


    @Test
    void fixedAssetCategoryImport() throws IOException {
        Workbook wookbook = null;
        wookbook = new XSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\fixed_asset_manage\\固定资产\\文档\\表\\资产类别.xlsx"));//得到工作簿

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        ArrayList<FixedAssetCategory> fixedAssetCategories = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("0");


        for (int i = 1; i <= lastRowNum; i++) {
            FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
            Row row = sheet.getRow(i);
            Date date = new Date();

            fixedAssetCategory.setCategoryId(Long.valueOf(decimalFormat.format(row.getCell(0).getNumericCellValue())));
            fixedAssetCategory.setCategoryName(row.getCell(1).getStringCellValue());
            fixedAssetCategory.setCategoryLevel(Long.valueOf(decimalFormat.format(row.getCell(2).getNumericCellValue())));
            fixedAssetCategory.setCategoryDetailed(Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
            fixedAssetCategory.setStatus(Long.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
            fixedAssetCategory.setDepreciationMethodId(Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
            if (!StringUtils.isEmpty(row.getCell(6))) {
                fixedAssetCategory.setMeasureUnit(row.getCell(6).getStringCellValue());
            }

            if (!StringUtils.isEmpty(row.getCell(7))) {
                fixedAssetCategory.setCapacityUnit(row.getCell(7).getStringCellValue());
            }

            fixedAssetCategory.setDepreciationPeriod(Long.valueOf(decimalFormat.format(row.getCell(8).getNumericCellValue() * 100)));
            fixedAssetCategory.setEstimatedTotalWorkload(Long.valueOf(decimalFormat.format(row.getCell(9).getNumericCellValue() * 100)));
            fixedAssetCategory.setNetResidualValue(Long.valueOf(decimalFormat.format(row.getCell(10).getNumericCellValue() * 100)));
            fixedAssetCategory.setRemark(row.getCell(11).getStringCellValue());


            fixedAssetCategories.add(fixedAssetCategory);
        }

        fixedAssetCategoryService.saveBatch(fixedAssetCategories);
    }

    @Test
    void getfixedAssetCategoryList() {

        List<FixedAssetCategory> list = fixedAssetCategoryMapper.selectList(new QueryWrapper<FixedAssetCategory>()
                .orderByAsc("category_id"));

        LinkedHashMap<Long, LinkedHashMap<Long, FixedAssetCategoryVO>> tmpMap = new LinkedHashMap<>();

        list.forEach(item -> {
            long length = item.getCategoryId().toString().length();

            if (tmpMap.containsKey(length)) {
                FixedAssetCategoryVO fixedAssetCategoryVo = new FixedAssetCategoryVO();
                BeanUtils.copyProperties(item, fixedAssetCategoryVo);
                tmpMap.get(length).put(item.getCategoryId(), fixedAssetCategoryVo);
            } else {
                LinkedHashMap<Long, FixedAssetCategoryVO> tmpList = new LinkedHashMap<>();
                FixedAssetCategoryVO fixedAssetCategoryVo = new FixedAssetCategoryVO();
                BeanUtils.copyProperties(item, fixedAssetCategoryVo);
                tmpList.put(item.getCategoryId(), fixedAssetCategoryVo);
                tmpMap.put(length, tmpList);
            }
        });


        ListIterator<Map.Entry> listIterator = new ArrayList<Map.Entry>(tmpMap.entrySet()).listIterator(tmpMap.size());
        LinkedHashMap<Long, FixedAssetCategoryVO> nextMap = new LinkedHashMap<>();
        LinkedHashMap<Long, FixedAssetCategoryVO> previousMap = new LinkedHashMap<>();
        while (listIterator.hasPrevious()) {
            Map.Entry tmpEntry = listIterator.previous();
            previousMap = (LinkedHashMap<Long, FixedAssetCategoryVO>) tmpEntry.getValue();
            if (nextMap.size() != 0) {
                for(Map.Entry<Long, FixedAssetCategoryVO> entrySet : nextMap.entrySet()) {
                    Long prevNode = Long.valueOf(entrySet.getKey().toString().substring(0, entrySet.getKey().toString().length() - 2));
                    List<FixedAssetCategoryVO> children = previousMap.get(prevNode).getChildren();
                    if(children != null){
                        children.add(entrySet.getValue());
                    }else {
                        List<FixedAssetCategoryVO> child = new ArrayList<>();
                        child.add(entrySet.getValue());
                        previousMap.get(prevNode).setChildren(child);
                    }
                }
            }
            nextMap = previousMap;
        }
        LinkedHashMap<Long, FixedAssetCategoryVO> rs = (LinkedHashMap<Long, FixedAssetCategoryVO>) tmpMap.values().toArray()[0];
        List result = new ArrayList(rs.values());

    }

}
