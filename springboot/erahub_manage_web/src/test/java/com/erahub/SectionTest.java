package com.erahub;

import com.erahub.common.dto.asset.metadata.SectionDTO;
import com.erahub.common.model.asset.metadata.Section;
import com.erahub.common.vo.asset.metadata.SectionVO;
import com.erahub.asset.metadata.service.SectionService;
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
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:44
 * @Version 1.0
 **/
@SpringBootTest
public class SectionTest {
    @Autowired
    private SectionService sectionService;

    @Test
    void treeBuild() {
        List<SectionVO> tree = sectionService.getSectionList(new SectionDTO()).getRows();
        System.out.println(tree);
    }

    @Test
    void importList() throws IOException {
        Workbook wookbook = null;
        wookbook = new XSSFWorkbook(new FileInputStream("E:\\Program\\document\\project\\jlja\\fixed_asset_manage\\固定资产\\文档\\表\\使用单位.xlsx"));//得到工作簿

        Sheet sheet = wookbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        ArrayList<Section> sections = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("0");


        for (int i = 1; i <= lastRowNum; i++) {
            Section section = new Section();
            Row row = sheet.getRow(i);
            Date date = new Date();

            String id = row.getCell(0).getStringCellValue();

            section.setSectionId(id);
            section.setSectionName(row.getCell(1).getStringCellValue());
            section.setSectionAbbreviation(row.getCell(2).getStringCellValue());
            section.setLevel(Integer.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
            section.setDetailed(Integer.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
//            section.setStatus(Integer.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
            section.setStatus(1);
            if(row.getCell(7) != null && !StringUtils.isEmpty(row.getCell(7).getStringCellValue())){
                section.setRemark(row.getCell(7).getStringCellValue());
            }

            sections.add(section);
        }

        sectionService.saveBatch(sections);
    }
}
