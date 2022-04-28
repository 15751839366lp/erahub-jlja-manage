package com.erahub.fixedasset.metadata.converter;

import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:02
 * @Version 1.0
 **/
@Component
public class SectionConverter {
    /**
     * pojo转vo list
     *
     * @param sections
     * @return
     */
    public static List<SectionVO> converterToSectionVOList(List<Section> sections) {
        List<SectionVO> sectionVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sections)) {
            for (Section section : sections) {
                SectionVO sectionVO = new SectionVO();
                BeanUtils.copyProperties(section, sectionVO);

                sectionVOS.add(sectionVO);
            }
        }
        return sectionVOS;
    }

    /**
     * dto转pojo
     *
     * @param sectionDTO
     * @return
     */
    public static Section converterToSection(SectionDTO sectionDTO) {

        Section section = new Section();
        BeanUtils.copyProperties(sectionDTO, section);

        section.setSectionLevel(Long.valueOf(sectionDTO.getSectionId().length() / 4));

        return section;
    }

    /**
     * dto转pojo list
     *
     * @param sectionDTOS
     * @return
     */
    public static List<Section> converterToSectionList(List<SectionDTO> sectionDTOS) {
        List<Section> sections = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sectionDTOS)) {
            for (SectionDTO sectionDTO : sectionDTOS) {
                Section section = new Section();
                BeanUtils.copyProperties(sectionDTO, section);

                section.setSectionLevel(Long.valueOf(sectionDTO.getSectionId().length() / 4));

                sections.add(section);
            }
        }
        return sections;
    }
}
