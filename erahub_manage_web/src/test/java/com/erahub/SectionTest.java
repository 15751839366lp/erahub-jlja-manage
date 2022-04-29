package com.erahub;

import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
import com.erahub.fixedasset.metadata.service.SectionService;
import com.erahub.fixedasset.metadata.utils.SectionTreeBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
}
