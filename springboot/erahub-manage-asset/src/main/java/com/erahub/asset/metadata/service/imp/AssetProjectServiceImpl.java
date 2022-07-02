package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.AssetProjectConverter;
import com.erahub.asset.metadata.mapper.AssetProjectMapper;
import com.erahub.asset.metadata.mapper.AssetProjectMapper;
import com.erahub.asset.metadata.service.AssetProjectService;
import com.erahub.common.dto.asset.metadata.AssetProjectDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetProjectExcel;
import com.erahub.common.model.asset.metadata.AssetProject;
import com.erahub.common.model.asset.metadata.AssetProject;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.AssetProjectVO;
import com.erahub.common.vo.common.PageVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lipeng
 * @since 2022-05-09
 */
@Service
public class AssetProjectServiceImpl extends ServiceImpl<AssetProjectMapper, AssetProject> implements AssetProjectService {

    private int batchSize = 1000;

    @Autowired
    private AssetProjectMapper assetProjectMapper;

    @Autowired
    private AssetProjectConverter assetProjectConverter;

    /**
     * 查询工程项目列表
     * @param assetProjectDTO
     * @return
     */
    @Override
    public PageVO<AssetProjectVO> getAssetProjectList(AssetProjectDTO assetProjectDTO) {

        IPage<AssetProject> assetProjectIPage = new Page<>(assetProjectDTO.getPageNum(), assetProjectDTO.getPageSize());

        assetProjectIPage = assetProjectMapper.selectAssetProjectPageList(assetProjectIPage, assetProjectDTO);
        List<AssetProject> assetProjectList = assetProjectIPage.getRecords();
        List<AssetProjectVO> assetProjectVOS = assetProjectConverter.converterToAssetProjectVOList(assetProjectList);

        return new PageVO<>(assetProjectIPage.getTotal(), assetProjectVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<AssetProjectExcel> exportAssetProjectExcel() {
        List<AssetProjectExcel> assetProjectExcels = new ArrayList<>();

        IPage<AssetProject> assetProjectIPage = new Page<>();
        //临时不分页
        assetProjectIPage.setSize(-1l);

        assetProjectIPage = assetProjectMapper.selectAssetProjectPageList(assetProjectIPage, new AssetProjectDTO());
        List<AssetProject> assetProjectList = assetProjectIPage.getRecords();
        List<AssetProjectVO> assetProjectVOS = assetProjectConverter.converterToAssetProjectVOList(assetProjectList);
        ListMapUtils.copyList(assetProjectVOS, assetProjectExcels, AssetProjectExcel.class);

        return assetProjectExcels;
    }

    /**
     * 更新工程项目状态
     *
     * @param assetProjectId
     * @param status
     */
    @Override
    public void changeAssetProjectStatus(String assetProjectId, Integer status) throws AssetException {
        AssetProject dbAssetProject = assetProjectMapper.selectById(assetProjectId);
        if (dbAssetProject == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的工程项目不存在");
        }

        dbAssetProject.setStatus(status);
        assetProjectMapper.updateById(dbAssetProject);

    }

    /**
     * 添加工程项目
     *
     * @param assetProjectDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addAssetProject(AssetProjectDTO assetProjectDTO) throws AssetException {
        @NotNull(message = "工程项目id不能为空") String assetProjectId = assetProjectDTO.getAssetProjectId();
        @NotBlank(message = "工程项目名称不能为空") String assetProjectName = assetProjectDTO.getAssetProjectName();

        QueryWrapper<AssetProject> assetProjectQueryWrapper = new QueryWrapper<>();
        assetProjectQueryWrapper.eq("asset_project_id", assetProjectId)
                .or()
                .eq("asset_project_name", assetProjectName);

        List<AssetProject> assetProjectList = assetProjectMapper.selectList(assetProjectQueryWrapper);

        if (!assetProjectList.isEmpty() && assetProjectList.get(0).getAssetProjectId().equals(assetProjectId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该工程项目id已被占用");
        }
        if (!assetProjectList.isEmpty() && assetProjectList.get(0).getAssetProjectName().equals(assetProjectName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该工程项目名称已被占用");
        }

        AssetProject assetProject = new AssetProject();
        assetProjectConverter.converterToAssetProject(assetProjectDTO, assetProject);

        assetProjectMapper.insert(assetProject);
    }

    /**
     * 修改工程项目
     *
     * @param assetProjectDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateAssetProject(AssetProjectDTO assetProjectDTO) throws AssetException {
        @NotNull(message = "工程项目id不能为空") String assetProjectId = assetProjectDTO.getAssetProjectId();

        List<AssetProject> updateAssetProjectList = assetProjectMapper
                .selectList(new QueryWrapper<AssetProject>().like("asset_project_id", assetProjectId));

        if (updateAssetProjectList == null || updateAssetProjectList.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_PROJECT_NOT_FOUND);
        }

        AssetProject updateAssetProject = updateAssetProjectList.get(0);
        assetProjectConverter.converterToAssetProject(assetProjectDTO, updateAssetProject);

        assetProjectMapper.updateById(updateAssetProject);
    }

    /**
     * 删除工程项目
     *
     * @param assetProjectId
     */
    @Transactional
    @Override
    public void deleteAssetProjectById(String assetProjectId) throws AssetException {
        List<AssetProject> deleteAssetProjects = assetProjectMapper.selectAssetProjectListByIds(Arrays.asList(assetProjectId));

        if (deleteAssetProjects == null || deleteAssetProjects.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_PROJECT_NOT_FOUND, "要删除的工程项目不存在");
        }

        if (deleteAssetProjects.get(0).getAssetQuantity() != null
                && deleteAssetProjects.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的工程项目还存在资产");
        }


        assetProjectMapper.deleteById(assetProjectId);
    }

    /**
     * 批量删除工程项目
     *
     * @param assetProjectIds
     */
    @Transactional
    @Override
    public void deleteAssetProjectByBatchId(List<String> assetProjectIds) throws AssetException {
        List<AssetProject> assetProjectList = assetProjectMapper.selectAssetProjectListByIds(assetProjectIds);

        if (assetProjectList != null && assetProjectList.size() > 0) {
            for (AssetProject assetProject : assetProjectList) {

                if (assetProject.getAssetQuantity() != null && assetProject.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的工程项目存在资产");
                }
            }
        }

        assetProjectMapper.deleteBatchIds(assetProjectIds);
    }

    /**
     * 上传导入工程项目
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importAssetProject(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<AssetProject> assetProjectList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        try {
            for (MultipartFile file : fileMap.values()) {
                //判断文件是否存在
                if (file == null || file.getName() == null) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "文件有误");
                }
                String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                //判断文件类型
                if (".xls".equals(fileType)) {
                    workbook = new HSSFWorkbook(file.getInputStream());
                    sheet = workbook.getSheetAt(0);
                } else if (".xlsx".equals(fileType)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                    sheet = workbook.getSheetAt(0);
                } else {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "文件类型错误");
                }

                int lastRowNum = sheet.getLastRowNum();

                for (int i = 1; i <= lastRowNum; i++) {
                    AssetProject assetProject = new AssetProject();
                    Row row = sheet.getRow(i);

                    String assetProjectId = dataFormatter.formatCellValue(row.getCell(0)).trim();
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(assetProjectId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(assetProjectId);
                    }

                    //数据复值
                    assetProject.setAssetProjectId(assetProjectId);
                    assetProject.setAssetProjectName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    assetProject.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    assetProject.setRemark(dataFormatter.formatCellValue(row.getCell(6)).trim());

                    assetProjectList.add(assetProject);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<AssetProject> assetProjects = assetProjectMapper.selectBatchIds(idList);
            if (assetProjects.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(assetProjectList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = assetProjectList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, assetProjectList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容有误，添加失败");
        }
    }
}
