package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.AssetProjectDTO;
import com.erahub.common.model.asset.metadata.AssetProject;
import com.erahub.common.vo.asset.metadata.AssetProjectVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/5/10 17:42
 * @Version 1.0
 **/
@Component
public class AssetProjectConverter {
    /**
     * pojo转vo list
     *
     * @param assetProjects
     * @return
     */
    public static List<AssetProjectVO> converterToAssetProjectVOList(List<AssetProject> assetProjects) {
        List<AssetProjectVO> assetProjectVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetProjects)) {
            for (AssetProject assetProject : assetProjects) {
                AssetProjectVO assetProjectVO = new AssetProjectVO();
                BeanUtils.copyProperties(assetProject, assetProjectVO);

                if(assetProject.getAssetQuantity() == null){
                    assetProjectVO.setAssetQuantity(0l);
                }

                assetProjectVOS.add(assetProjectVO);
            }
        }
        return assetProjectVOS;
    }

    /**
     * dto转pojo
     *
     * @param assetProjectDTO
     * @param assetProject
     * @return
     */
    public static void converterToAssetProject(AssetProjectDTO assetProjectDTO, AssetProject assetProject) {
        BeanUtils.copyProperties(assetProjectDTO, assetProject);
    }

    /**
     * dto转pojo list
     *
     * @param assetProjectDTOS
     * @return
     */
    public static List<AssetProject> converterToAssetProjectList(List<AssetProjectDTO> assetProjectDTOS) {
        List<AssetProject> assetProjects = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetProjectDTOS)) {
            for (AssetProjectDTO assetProjectDTO : assetProjectDTOS) {
                AssetProject assetProject = new AssetProject();
                BeanUtils.copyProperties(assetProjectDTO, assetProject);

                assetProjects.add(assetProject);
            }
        }
        return assetProjects;
    }
}
