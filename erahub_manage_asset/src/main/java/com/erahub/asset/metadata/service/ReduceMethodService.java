package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.ReduceMethodDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ReduceMethodExcel;
import com.erahub.common.model.asset.metadata.ReduceMethod;
import com.erahub.common.vo.asset.metadata.ReduceMethodVO;
import com.erahub.common.vo.common.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lipeng
 * @since 2022-05-08
 */
public interface ReduceMethodService extends IService<ReduceMethod> {

    /**
     * 查询减少方式列表
     * @param reduceMethodDTO
     * @return
     */
    PageVO<ReduceMethodVO> getReduceMethodList(ReduceMethodDTO reduceMethodDTO);

    /**
     * 更新减少方式状态
     * @param reduceMethodId
     * @param status
     */
    void changeReduceMethodStatus(Long reduceMethodId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<ReduceMethodExcel> exportReduceMethodExcel();

    /**
     * 添加减少方式
     * @param reduceMethodDTO
     */
    void addReduceMethod(ReduceMethodDTO reduceMethodDTO) throws AssetException;

    /**
     * 修改减少方式
     * @param reduceMethodDTO
     */
    void updateReduceMethod(ReduceMethodDTO reduceMethodDTO) throws AssetException;

    /**
     * 删除减少方式
     * @param reduceMethodId
     */
    void deleteReduceMethodById(Long reduceMethodId) throws AssetException;

    /**
     * 批量删除减少方式
     * @param reduceMethodIds
     */
    void deleteReduceMethodByBatchId(List<Long> reduceMethodIds) throws AssetException;

    /**
     * 上传导入减少方式
     * @param fileMap
     * @return
     */
    void importReduceMethod(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
