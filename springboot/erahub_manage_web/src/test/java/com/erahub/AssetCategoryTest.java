package com.erahub;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erahub.common.model.asset.metadata.AssetCategory;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import com.erahub.asset.metadata.mapper.AssetCategoryMapper;
import com.erahub.asset.metadata.service.AssetCategoryService;
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
public class AssetCategoryTest {

    @Autowired
    private AssetCategoryService assetCategoryService;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;


    @Test
    void assetCategoryImport() throws IOException {
        Workbook wookbook = null;
        wookbook = new XSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\asset_manage\\固定资产\\文档\\表\\资产类别.xlsx"));//得到工作簿

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        ArrayList<AssetCategory> assetCategories = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("0");


        for (int i = 1; i <= lastRowNum; i++) {
            AssetCategory assetCategory = new AssetCategory();
            Row row = sheet.getRow(i);
            Date date = new Date();

            String categoryId = row.getCell(0).getStringCellValue();
            if(categoryId != null && !"0".equals(categoryId.substring(0,1))){
                categoryId = "0" + categoryId;
            }
            assetCategory.setAssetCategoryId(categoryId);
            assetCategory.setAssetCategoryName(row.getCell(1).getStringCellValue());
            assetCategory.setLevel(Integer.valueOf(decimalFormat.format(row.getCell(2).getNumericCellValue())));
            assetCategory.setDetailed(Integer.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
            assetCategory.setStatus(Integer.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
            assetCategory.setDepreciationMethodId(Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
            if (!StringUtils.isEmpty(row.getCell(6))) {
                assetCategory.setMeasureUnit(row.getCell(6).getStringCellValue());
            }

            if (!StringUtils.isEmpty(row.getCell(7))) {
                assetCategory.setCapacityUnit(row.getCell(7).getStringCellValue());
            }

            assetCategory.setRemark(row.getCell(11).getStringCellValue());


            assetCategories.add(assetCategory);
        }

        assetCategoryService.saveBatch(assetCategories);
    }

    @Test
    void getAssetCategoryList() {

        List<AssetCategory> list = assetCategoryMapper.selectList(new QueryWrapper<AssetCategory>()
                .orderByAsc("category_id"));

        LinkedHashMap<Long, LinkedHashMap<Long, AssetCategoryVO>> tmpMap = new LinkedHashMap<>();

        list.forEach(item -> {
            long length = item.getAssetCategoryId().toString().length();

            if (tmpMap.containsKey(length)) {
                AssetCategoryVO assetCategoryVo = new AssetCategoryVO();
                BeanUtils.copyProperties(item, assetCategoryVo);
                tmpMap.get(length).put(Long.valueOf(item.getAssetCategoryId()), assetCategoryVo);
            } else {
                LinkedHashMap<Long, AssetCategoryVO> tmpList = new LinkedHashMap<>();
                AssetCategoryVO assetCategoryVo = new AssetCategoryVO();
                BeanUtils.copyProperties(item, assetCategoryVo);
                tmpList.put(Long.valueOf(item.getAssetCategoryId()), assetCategoryVo);
                tmpMap.put(length, tmpList);
            }
        });


        ListIterator<Map.Entry> listIterator = new ArrayList<Map.Entry>(tmpMap.entrySet()).listIterator(tmpMap.size());
        LinkedHashMap<Long, AssetCategoryVO> nextMap = new LinkedHashMap<>();
        LinkedHashMap<Long, AssetCategoryVO> previousMap = new LinkedHashMap<>();
        while (listIterator.hasPrevious()) {
            Map.Entry tmpEntry = listIterator.previous();
            previousMap = (LinkedHashMap<Long, AssetCategoryVO>) tmpEntry.getValue();
            if (nextMap.size() != 0) {
                for(Map.Entry<Long, AssetCategoryVO> entrySet : nextMap.entrySet()) {
                    Long prevNode = Long.valueOf(entrySet.getKey().toString().substring(0, entrySet.getKey().toString().length() - 2));
                    List<AssetCategoryVO> children = previousMap.get(prevNode).getChildren();
                    if(children != null){
                        children.add(entrySet.getValue());
                    }else {
                        List<AssetCategoryVO> child = new ArrayList<>();
                        child.add(entrySet.getValue());
                        previousMap.get(prevNode).setChildren(child);
                    }
                }
            }
            nextMap = previousMap;
        }
        LinkedHashMap<Long, AssetCategoryVO> rs = (LinkedHashMap<Long, AssetCategoryVO>) tmpMap.values().toArray()[0];
        List result = new ArrayList(rs.values());

    }

}
