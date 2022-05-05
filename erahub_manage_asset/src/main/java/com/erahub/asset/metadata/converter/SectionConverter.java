package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.SectionDTO;
import com.erahub.common.model.asset.metadata.Section;
import com.erahub.common.vo.asset.metadata.SectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

                if(section.getAssetQuantity() == null){
                    sectionVO.setAssetQuantity(0l);
                }
//                if(section.getSectionDetailed() != null){
//                    sectionVO.setHasChildren(section.getSectionDetailed() == 1 ? false : true);
//                }

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
    public static void converterToSection(SectionDTO sectionDTO,Section section) {
        BeanUtils.copyProperties(sectionDTO, section);
        section.setLevel(Long.valueOf(sectionDTO.getSectionId().length() / 4));
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

                section.setLevel(Long.valueOf(sectionDTO.getSectionId().length() / 4));

                sections.add(section);
            }
        }
        return sections;
    }
}
