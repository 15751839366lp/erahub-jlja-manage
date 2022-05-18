<template>
    <div id="assetCategory">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>资产管理</el-breadcrumb-item>
            <el-breadcrumb-item>元数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>资产类别</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-form :inline="true" ref="form" :model="queryMap" label-width="70px" size="small">

                <el-form-item label="ID">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            @clear="searchAssetCategory"
                            clearable
                            v-model="queryMap.assetCategoryId"
                            placeholder="请输入id查询"
                    ></el-input>
                </el-form-item>
                <el-form-item label="类别名称">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            @clear="searchAssetCategory"
                            clearable
                            v-model="queryMap.assetCategoryName"
                            placeholder="请输入类别名称查询"
                    ></el-input>
                </el-form-item>
                <el-form-item label="折旧方法">
                    <el-select
                            clearable
                            @clear="searchAssetCategory"
                            v-model="queryMap.depreciationMethodId"
                            placeholder="请选择折旧方法"
                    >
                        <el-option
                                v-for="depreciationMethod in depreciationMethodList"
                                :label="depreciationMethod.depreciationMethodName"
                                :key="depreciationMethod.depreciationMethodId"
                                :value="depreciationMethod.depreciationMethodId"
                        >
                            <span style="float: left">{{ depreciationMethod.depreciationMethodName }}</span>
                            <!--                            <span style="float: right; color: #8492a6; font-size: 13px">-->
                            <!--                                <el-tag size="small" effect="plain" type="success">-->
                            <!--                                  {{ department.total }}人-->
                            <!--                                </el-tag>-->
                            <!--                              </span>-->
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="计量单位">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            clearable
                            @clear="searchAssetCategory"
                            v-model="queryMap.measureUnit"
                            placeholder="请输入计量单位查询"
                    ></el-input>
                </el-form-item>
                <el-form-item label="能力单位">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            clearable
                            @clear="searchAssetCategory"
                            v-model="queryMap.capacityUnit"
                            placeholder="请输入能力单位查询">
                    </el-input>
                </el-form-item>
                <el-form-item label="折旧年限">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            clearable
                            @clear="searchAssetCategory"
                            v-model="queryMap.depreciationPeriod"
                            placeholder="请输入折旧年限查询">
                    </el-input>
                </el-form-item>
                <el-form-item label="工作量">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            clearable
                            @clear="searchAssetCategory"
                            v-model="queryMap.estimatedTotalWorkload"
                            placeholder="请输入最大工作量查询">
                    </el-input>
                </el-form-item>
                <el-form-item label="净残值率">
                    <el-input
                            @keyup.enter.native="searchAssetCategory"
                            clearable
                            @clear="searchAssetCategory"
                            v-model="queryMap.netResidualValue"
                            placeholder="请输入净残值率查询">
                    </el-input>
                </el-form-item>
                <div style="display: inline-block">

                    <el-form-item label="使用状态">
                        <el-radio v-model="queryMap.status" :label="1">可用</el-radio>
                        <el-radio v-model="queryMap.status" :label="0">禁用</el-radio>
                        <el-radio v-model="queryMap.status" :label="null">全部</el-radio>
                    </el-form-item>
                    <el-form-item label="查询类型" style="margin-left:110px;">
                        <el-radio v-model="queryMap.isAccurate" :label="0">模糊查询</el-radio>
                        <el-radio v-model="queryMap.isAccurate" :label="1">精确查询</el-radio>
                    </el-form-item>
                    <el-form-item label="明细类型" style="margin-left:110px;">
                        <el-radio v-model="queryMap.detailed" :label="1">是</el-radio>
                        <el-radio v-model="queryMap.detailed" :label="0">否</el-radio>
                        <el-radio v-model="queryMap.detailed" :label="null">全部</el-radio>
                    </el-form-item>
                </div>
                <el-form-item label="创建时间">
                    <el-date-picker
                            :clearable="false"
                            v-model="timeRange"
                            type="datetimerange"
                            :value-format="'YYYY-MM-DD HH:mm:ss'"
                            :shortcuts="pickerOptions.shortcuts"
                            unlink-panels
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item style="float: right;margin-right: 150px; ">
                    <el-button @click="reset" icon="el-icon-refresh">重置</el-button>
                    <el-button type="primary" @click="searchAssetCategory" icon="el-icon-search"
                               v-hasPermission="'asset:metadata:assetCategory:select'">查询</el-button>
                    <el-button v-hasPermission="'asset:metadata:assetCategory:add'"
                               type="success"
                               icon="el-icon-plus"
                               @click="openAddDialog"
                    >添加
                    </el-button>
                    <el-button @click="openUploadDialog"
                               v-hasPermission="'asset:metadata:assetCategory:import'"
                               icon="el-icon-upload">导入
                    </el-button>
                    <el-button @click="exportAssetCategory"
                               v-hasPermission="'asset:metadata:assetCategory:export'"
                               icon="el-icon-download">导出
                    </el-button>
                    <el-button @click="deleteAssetCategoryByBatchId(selections)"
                               icon="el-icon-delete"
                               v-hasPermission="'asset:metadata:assetCategory:delete'"
                               :disabled="selections.length === 0">批量
                    </el-button>
                </el-form-item>
            </el-form>
            <!-- 表格部分 -->
            <el-table
                    ref="table"
                    v-loading="loading"
                    row-key="assetCategoryId"
                    style="width: 100%;"
                    height="490"
                    size="mini"
                    border
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    :data="assetCategories"
                    :row-style="{height: '30px'}"
                    @sort-change="sortChange"
                    @selection-change="selectChange"
            >
                <el-table-column type="selection" width="40px"></el-table-column>
                <el-table-column prop="assetCategoryId" label="ID" width="100px" fixed sortable></el-table-column>
                <el-table-column prop="assetCategoryName" label="类别名称" width="150px" fixed
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="assetQuantity" label="资产数量" width="100px" sortable>
                    <template #default="scope">
                        <el-tag type="success">{{scope.row.assetQuantity}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="level" label="级数" width="100px">
                    <template #default="scope">
                        <el-tag v-if="scope.row.level===1">一级分类</el-tag>
                        <el-tag type="success" v-else-if="scope.row.level===2">二级分类</el-tag>
                        <el-tag type="info" v-else-if="scope.row.level===3">三级分类</el-tag>
                        <el-tag type="warning" v-else-if="scope.row.level===4">四级分类</el-tag>
                        <el-tag type="danger" v-else-if="scope.row.level===5">五级分类</el-tag>
                        <el-tag type="" v-else>六级分类</el-tag>
                    </template>
                </el-table-column>
                <!--                <el-table-column prop="depreciationMethodId" label="折旧方法id"></el-table-column>-->
                <el-table-column prop="depreciationMethodName" label="折旧方法" width="120px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="detailed" label="明细">
                    <template #default="scope">
                        <el-tag type="success" v-if="scope.row.detailed===1">是</el-tag>
                        <el-tag type="danger" v-else-if="scope.row.detailed===0">否</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="measureUnit" label="计量单位" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="capacityUnit" label="能力单位" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="depreciationPeriod" label="折旧年限"></el-table-column>
                <el-table-column prop="estimatedTotalWorkload" label="总工作量"></el-table-column>
                <el-table-column prop="netResidualValue" label="净残值率"></el-table-column>
                <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="true" width="150"
                                 sortable></el-table-column>
                <el-table-column prop="modifiedTime" label="修改时间" :show-overflow-tooltip="true"
                                 width="150"></el-table-column>
                <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true" width="150"></el-table-column>
                <el-table-column prop="status" label="状态" width="100" fixed="right">
                    <template #default="scope">
                            <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"
                                   @change="changeAssetCategoryStatus(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="150px">
                    <template #default="scope">
                        <el-button v-hasPermission="'asset:metadata:assetCategory:edit'"
                                   type="primary"
                                   icon="el-icon-edit"
                                   @click="openEditDialog(scope.row)"
                                   size="mini"
                        >
                        </el-button>
                        <el-button v-hasPermission="'asset:metadata:assetCategory:delete'"
                                   type="danger"
                                   icon="el-icon-delete"
                                   @click="deleteAssetCategory(scope.row)"
                                   size="mini"
                        >
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页 -->
            <el-pagination
                    style="margin-top:10px;"
                    background

                    :show-header="true"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[10,20, 30, 50, 100, 200]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>

            <!-- 添加弹出框 -->
            <el-dialog title="添加分类" v-model="addDialogVisible" @close="addCloseDialog" width="50%">
                <span>
                  <el-form
                          :model="addAssetCategoryForm"
                          :rules="formRules"
                          ref="addAssetCategoryFormRef"
                          label-width="100px"
                  >
                    <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="类别ID" prop="assetCategoryId">
                            <el-input v-model="addAssetCategoryForm.assetCategoryId"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="类别名称" prop="assetCategoryName">
                            <el-input v-model="addAssetCategoryForm.assetCategoryName"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="明细" prop="detailed">
                                <el-radio v-model="addAssetCategoryForm.detailed" label="1">是</el-radio>
                                <el-radio v-model="addAssetCategoryForm.detailed"
                                          label="0">否</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="状态" prop="status">
                                <el-radio v-model="addAssetCategoryForm.status" :label="1">可用</el-radio>
                                <el-radio v-model="addAssetCategoryForm.status" :label="0">禁用</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="净残值率" prop="netResidualValue">
                            <el-input v-model="addAssetCategoryForm.netResidualValue"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="折旧方法" prop="depreciationMethodId">
                            <el-select v-model="addAssetCategoryForm.depreciationMethodId" placeholder="请选择折旧方法">
                              <el-option
                                      v-for="depreciationMethod in depreciationMethodList"
                                      :key="depreciationMethod.depreciationMethodId"
                                      :label="depreciationMethod.depreciationMethodName"
                                      :value="depreciationMethod.depreciationMethodId"
                                      :disabled="depreciationMethod.status == 1 ? false : true"
                              ></el-option>
                            </el-select>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="计量单位" prop="measureUnit">
                            <el-input v-model="addAssetCategoryForm.measureUnit"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="能力单位" prop="capacityUnit">
                             <el-input v-model="addAssetCategoryForm.capacityUnit"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="折旧年限" prop="depreciationPeriod">
                            <el-input v-model="addAssetCategoryForm.depreciationPeriod"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="总工作量" prop="estimatedTotalWorkload">
                            <el-input v-model="addAssetCategoryForm.estimatedTotalWorkload"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                          <el-col>
                          <el-form-item label="备注" prop="remark">
                              <el-input type="textarea" v-model="addAssetCategoryForm.remark"
                                        style="width: 86%" :rows="3"></el-input>
                          </el-form-item>
                          </el-col>
                      </el-row>
                  </el-form>
                </span>
                <template #footer>
                    <span class="dialog-footer">
                      <el-button @click="addDialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="addAssetCategory" :disabled="btnDisabled"
                                 :loading="btnLoading">确 定</el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 编辑弹出框 -->
            <el-dialog title="编辑分类" v-model="editDialogVisible" @close="editCloseDialog" width="50%">
        <span>
          <el-form :model="editAssetCategoryForm" :rules="formRules" ref="editAssetCategoryFormRef"
                   label-width="100px">
            <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="类别ID" prop="assetCategoryId">
                            <el-input v-model="editAssetCategoryForm.assetCategoryId" :disabled="true"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="类别名称" prop="assetCategoryName">
                            <el-input v-model="editAssetCategoryForm.assetCategoryName"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="明细" prop="detailed">
                                <el-radio v-model="editAssetCategoryForm.detailed"
                                          :label="1">是</el-radio>
                                <el-radio v-model="editAssetCategoryForm.detailed"
                                          :label="0">否</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="状态" prop="status">
                                <el-radio v-model="editAssetCategoryForm.status" :label="1">可用</el-radio>
                                <el-radio v-model="editAssetCategoryForm.status" :label="0">禁用</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="净残值率" prop="netResidualValue">
                            <el-input v-model="editAssetCategoryForm.netResidualValue"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="折旧方法" prop="depreciationMethodId">
                            <el-select v-model="editAssetCategoryForm.depreciationMethodId" placeholder="请选择折旧方法">
                              <el-option
                                      v-for="depreciationMethod in depreciationMethodList"
                                      :key="depreciationMethod.depreciationMethodId"
                                      :label="depreciationMethod.depreciationMethodName"
                                      :value="depreciationMethod.depreciationMethodId"
                                      :disabled="depreciationMethod.status == 1 ? false : true"
                              ></el-option>
                            </el-select>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="计量单位" prop="measureUnit">
                            <el-input v-model="editAssetCategoryForm.measureUnit"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="能力单位" prop="capacityUnit">
                             <el-input v-model="editAssetCategoryForm.capacityUnit"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="折旧年限" prop="depreciationPeriod">
                            <el-input v-model="editAssetCategoryForm.depreciationPeriod"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="总工作量" prop="estimatedTotalWorkload">
                            <el-input v-model="editAssetCategoryForm.estimatedTotalWorkload"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                          <el-col>
                          <el-form-item label="备注" prop="remark">
                              <el-input type="textarea" v-model="editAssetCategoryForm.remark"
                                        style="width: 86%" :rows="3"></el-input>
                          </el-form-item>
                          </el-col>
                      </el-row>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateAssetCategory" :disabled="btnDisabled"
                     :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 上传弹出框 -->
            <el-dialog title="导入资产类别" v-model="uploadDialogVisible" @close="importCloseDialog" width="40%" center>
        <span style="display: inline-block;">
          <el-upload
                  accept=".xls,.xlsx"
                  class="upload-demo"
                  ref="upload"
                  :action="server + '/asset/metadata/assetcategory/importAssetCategory'"
                  :file-list="fileList"
                  :on-remove="handleRemove"
                  :on-change="handleChange"
                  :before-upload="beforeUpload"
                  :auto-upload="false"
          >
              <template #trigger>
                 <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              </template>
                <el-button style="margin-left: 10px;" size="small" type="success"
                           @click="importAssetCategory">导入文件</el-button>
          <div slot="tip" class="el-upload__tip">只能上传xls/xlsx文件，单个文件大小不能超过20MB，总文件大小不能超过100MB</div>
        </el-upload>
        </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import utils from '../../../api/common/utils';
    import {
        getAssetCategoryListApi,
        getAllDepreciationMethodApi,
        changeAssetCategoryStatusApi,
        exportAssetCategoryExcelApi,
        addAssetCategoryApi,
        updateAssetCategoryApi,
        deleteAssetCategoryApi,
        deleteAssetCategoryByBatchIdApi,
        importAssetCategoryApi,
    } from '../../../api/asset/metadata/assetCategory'

    export default {

        setup() {
            const selections = ref([])   //选中的值显示
            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const depreciationMethodList = ref([])
            const addDialogVisible = ref(false)
            const editDialogVisible = ref(false)
            const uploadDialogVisible = ref(false)
            const editAssetCategoryForm = ref(null)
            const total = ref(0)
            const assetCategories = ref([])
            const addAssetCategoryFormRef = ref(null)
            const editAssetCategoryFormRef = ref(null)
            const timeRange = ref([])
            const fileList = ref([])
            const fileDatas = ref([])
            const server = import.meta.env.PROD ? import.meta.env.VITE_APP_BASE_API : "/api"

            const pickerOptions = reactive({
                shortcuts: [
                    {
                        text: '今天(此刻)',
                        value: () => {
                            const end = new Date();
                            const start = new Date(new Date(new Date().toLocaleDateString()).getTime());
                            return [start, end];
                        }
                    },
                    {
                        text: '昨天',
                        value: () => {
                            const end = new Date(new Date(new Date().toLocaleDateString()).getTime());
                            const start = new Date(new Date(new Date().toLocaleDateString()).getTime());
                            start.setTime(start.getTime() - 3600 * 1000 * 24);
                            return [start, end];
                        }
                    },
                    {
                        text: '最近一周',
                        value: () => {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            return [start, end];
                        }
                    },
                    {
                        text: '最近一个月',
                        value: () => {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            return [start, end];
                        }
                    },
                    {
                        text: '最近两个月',
                        value: () => {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 60);
                            return [start, end];
                        }
                    },
                    {
                        text: '最近三个月',
                        value: () => {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            return [start, end];
                        }
                    }]
            })

            const addAssetCategoryForm = ref({
                assetCategoryId: null,
                assetCategoryName: null,
                detailed: null,
                status: null,
                depreciationMethodId: null,
                measureUnit: null,
                capacityUnit: null,
                depreciationPeriod: null,
                estimatedTotalWorkload: null,
                netResidualValue: null,
                remark: null,
            }) //添加表单
            const formRules = ref({
                assetCategoryId: [
                    {required: true, message: "请输入分类ID", trigger: "blur"},
                    {pattern: /^[+]?(0|([0-9]\d*))?$/, message: '请输入正确格式'}
                ],
                assetCategoryName: [
                    {required: true, message: "请输入分类名称", trigger: "blur"}
                ],
                detailed: [
                    {required: true, message: "请选择是否为明细", trigger: "blur"}
                ],
                status: [
                    {required: true, message: "请选择状态", trigger: "blur"}
                ],
                depreciationMethodId: [
                    {required: true, message: "请选择折旧方法", trigger: "blur"}
                ],
                depreciationPeriod: [
                    {required: true, message: "请输入折旧年限", trigger: "blur"},
                    {pattern: /^[+-]?(0|([1-9]\d*))(\.\d{1,2})?$/, message: '请输入正确数字'}
                ],
                estimatedTotalWorkload: [
                    {required: true, message: "请输入总工作量", trigger: "blur"},
                    {pattern: /^[+-]?(0|([1-9]\d*))(\.\d{1,2})?$/, message: '请输入正确数字'}
                ],
                netResidualValue: [
                    {required: true, message: "请输入净残值率", trigger: "blur"},
                    {pattern: /^(0(\.\d{1,2})?|1(\.0{1,2})?)$/, message: '请输入0-1之间两位小数'}
                ],
            })

            const queryMap = reactive({
                assetCategoryId: null,
                assetCategoryName: null,
                detailed: null,
                status: null,
                depreciationMethodId: null,
                depreciationMethodName: null,
                measureUnit: null,
                capacityUnit: null,
                depreciationPeriod: null,
                estimatedTotalWorkload: null,
                netResidualValue: null,
                isAccurate: 0,
                startCreateTime: null,
                endCreateTime: null,
                isAsc: null,
                sortColumn: null,
                pageNum: 1,
                pageSize: 10,
            })

            /**
             * 重置
             */
            const reset = () => {
                queryMap.assetCategoryId = null;
                queryMap.assetCategoryName = null;
                queryMap.detailed = null;
                queryMap.status = null;
                queryMap.depreciationMethodId = null;
                queryMap.depreciationMethodName = null;
                queryMap.measureUnit = null;
                queryMap.capacityUnit = null;
                queryMap.depreciationPeriod = null;
                queryMap.estimatedTotalWorkload = null;
                queryMap.netResidualValue = null;
                queryMap.isAccurate = 0;
                queryMap.startCreateTime = null;
                queryMap.endCreateTime = null;
                timeRange.value = [];
                queryMap.isAsc = null;
                queryMap.sortColumn = null;
                queryMap.pageNum = 1;
                queryMap.pageSize = 10;

                getAssetCategoryList()
            }

            //加载分类数据
            const getAssetCategoryList = () => {

                if (timeRange.value != null && timeRange.value.length === 1) {
                    queryMap.startCreateTime = timeRange.value[0];
                } else if (timeRange.value != null && timeRange.value.length === 2) {
                    queryMap.startCreateTime = timeRange.value[0];
                    queryMap.endCreateTime = timeRange.value[1];
                }

                if (!utils.isEmpty(queryMap.assetCategoryId) && !utils.isStringIneger(queryMap.assetCategoryId)) {
                    ElMessage.error("请输入数值类型ID");
                    return;
                } else if (!utils.isEmpty(queryMap.depreciationPeriod) && !utils.isNumberTwoScale(queryMap.depreciationPeriod, 2)) {
                    ElMessage.error("请输入数值类型折旧年限");
                    return;
                } else if (!utils.isEmpty(queryMap.estimatedTotalWorkload) && !utils.isNumberTwoScale(queryMap.estimatedTotalWorkload, 2)) {
                    ElMessage.error("请输入数值类型工作量");
                    return;
                } else if (!utils.isEmpty(queryMap.netResidualValue) && !utils.isNumberTwoScale(queryMap.netResidualValue, 2)) {
                    ElMessage.error("请输入数值类型净残值率");
                    return;
                }
                loading.value = true;
                assetCategories.value = [];

                getAssetCategoryListApi(queryMap).then((res) => {
                    if (!res.data.success) return ElMessage.error("查询失败: " + res.data.data.errorMsg);
                    assetCategories.value = res.data.data.rows;
                    total.value = res.data.data.total;
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("查询失败: " + res);
                });
            }

            /**
             * 加载所有折旧方法
             */
            const getAllDepreciationMethod = () => {
                getAllDepreciationMethodApi().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取折旧方法列表失败:" + res.data.data.errorMsg);
                    }
                    depreciationMethodList.value = res.data.data;
                }).catch((res) => {
                    ElMessage.error("获取折旧方法列表失败:" + res);
                });
            }

            /**
             * 导出
             */
            const exportAssetCategory = () => {
                exportAssetCategoryExcelApi().then((res) => {
                    if (res.headers["content-type"] === "application/json") {
                        return ElMessage.error(
                            "Subject does not have permission [asset:metadata:assetCategory:export]"
                        );
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "资产类别列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                }).catch((res) => {
                    ElMessage.error("导出失败:" + res);
                });
            }

            /**
             * 禁用启用类别
             */
            const changeAssetCategoryStatus = (row) => {
                changeAssetCategoryStatusApi(row.assetCategoryId, row.status).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("更新资产类别状态失败:" + res.data.data.errorMsg);
                        row.status = row.status == 1 ? 0 : 1;
                    } else {
                        const message = row.status == 1 ? '资产类别状态已启用' : '资产类别状态已禁用';
                        ElNotification({
                            type: 'success',
                            title: '操作成功',
                            message: message,
                        });
                    }
                }).catch((res) => {
                    ElMessage.error("更新资产类别状态失败:" + res);
                });
            }

            //添加分类
            const addAssetCategory = () => {
                addAssetCategoryFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;

                        addAssetCategoryApi(addAssetCategoryForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("分类添加成功");
                                getAssetCategoryList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                return ElMessage.error("分类添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            ElMessage.error("分类添加失败:" + res);
                        });
                    }
                });
            }

            //修改分类
            const updateAssetCategory = () => {
                editAssetCategoryFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        updateAssetCategoryApi(editAssetCategoryForm.value).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "分类信息更新成功",
                                    type: "success"
                                });
                                getAssetCategoryList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                ElMessage.error("分类信息更新失败:" + res.data.data.errorMsg);
                            }
                            editDialogVisible.value = false;
                        }).catch((res) => {
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            editDialogVisible.value = false;
                            ElMessage.error("分类信息更新失败:" + res);
                        });
                    }
                });
            }

            //删除分类
            const deleteAssetCategory = (row) => {
                if(row.detailed == 0){
                    ElMessage.error('该节点非明细节点，无法删除');
                    return;
                }
                if(row.assetQuantity > 0){
                    ElMessage.error('该类别存在资产，无法删除');
                    return;
                }

                ElMessageBox.confirm(
                    "此操作将永久删除该分类, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteAssetCategoryApi(row.assetCategoryId).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("分类删除成功");
                                getAssetCategoryList();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error(res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            }

            //批量删除分类
            const deleteAssetCategoryByBatchId = () => {
                let assetCategoryIds = selections.value.map(item => item.assetCategoryId);

                let flag = true;
                selections.value.forEach(item => {
                    if(item.detailed == 0){
                        flag = false;
                    }
                    if(item.assetQuantity > 0){
                        flag = false;
                    }
                })

                if(!flag){
                    ElMessage.error('请勿选择非明细节点,或存在资产的节点');
                    return;
                }
                ElMessageBox.confirm(
                    "此操作将永久删除分类, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteAssetCategoryByBatchIdApi(assetCategoryIds).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("分类批量删除成功");
                                getAssetCategoryList();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error(res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            }

            //导入分类
            const importAssetCategory = () => {
                let fullLoading = ElLoading.service({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                if (fileDatas.value == null || fileDatas.value.length == 0) {
                    ElMessage.error("请选择上传文件");
                    clearImportDialog(fullLoading);
                    return
                }
                let formData = new FormData();
                fileDatas.value.forEach((file, index) => {
                    //所有文件保存在formData中
                    formData.append(`file${index}`, file)
                })
                importAssetCategoryApi(formData).then((res) => {
                    if (res.data.success) {
                        ElNotification({
                            title: "成功",
                            message: "导入分类成功",
                            type: "success"
                        });

                        getAssetCategoryList();
                        uploadDialogVisible.value = false;
                        clearImportDialog(fullLoading);
                    } else {
                        clearImportDialog(fullLoading);
                        ElMessage.error(res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    clearImportDialog(fullLoading);
                    ElMessage.error(res);
                });
            }

            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.pageSize = newSize;
                queryMap.pageNum = 1;
                getAssetCategoryList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getAssetCategoryList();
            }

            /**
             * 搜索类别
             */
            const searchAssetCategory = () => {
                queryMap.pageNum = 1;
                getAssetCategoryList();
            }

            //改变排序
            const sortChange = (column) => {
                if (column.prop == null || column.order == null) {
                    queryMap.isAsc = null;
                    queryMap.sortColumn = null;
                    getAssetCategoryList();
                    return;
                }
                if (column.order == 'ascending') {
                    queryMap.isAsc = true;
                } else if (column.order == 'descending') {
                    queryMap.isAsc = false;
                }
                queryMap.sortColumn = utils.camelToSnakeCase(column.prop);
                getAssetCategoryList();
            }

            //打开添加
            const openAddDialog = () => {
                addDialogVisible.value = true;
            }

            //打开修改
            const openEditDialog = (row) => {
                let newObj = {};
                editAssetCategoryForm.value = utils.cloneObj(row, newObj);
                editDialogVisible.value = true;
            }

            //打开导入
            const openUploadDialog = (row) => {
                uploadDialogVisible.value = true;
            }

            //关闭弹出框
            const addCloseDialog = () => {
                addAssetCategoryFormRef.value.clearValidate();
                addAssetCategoryForm.value = {};
            }
            const editCloseDialog = () => {
                editAssetCategoryFormRef.value.clearValidate();
                editAssetCategoryForm.value = {};
            }
            const importCloseDialog = () => {
                fileList.value = [];
            }

            //清理导入框
            const clearImportDialog = (fullLoading) => {
                fileList.value = [];
                fileDatas.value = []
                fullLoading.close();
            }

            const selectChange = (sels) => {
                selections.value = sels;
            }

            //上传前校验
            const beforeUpload = (file) => {
                // const isLt2M = file.size / 1024 / 1024 < 10
                // if (!isLt2M) {
                //     ElMessage.error('上传图片大小不能超过 10MB！')
                //     return;
                // }
                // if(fileList.value.length>1){
                //     ElMessage.error('只支持上传一个文件')
                //     return;
                // }

                //这个设置上传多个文件，可根据自己业务情况决定，本文只是上传一个
                /*if(fileList.value.length>5){
                    ElMessage.error('只支持5个文件');
                    return;
                }*/

                let fd = new FormData()
                fd.append('file', file);

                return false;
            }

            const handleChange = (file, fileList) => {
                // let reader = new FileReader();
                // reader.readAsDataURL(file.raw);
                // reader.onload = (e) => {
                //     fileList.value.push({name: file.raw.name, url: e.target.result});
                // }
                fileDatas.value = [];
                fileList.forEach(item => {
                    fileDatas.value.push(item.raw)
                })
            }

            //文件移除配置方法
            const handleRemove = (file, fileList) => {
                fileDatas.value = [];
                fileList.forEach(item => {
                    fileDatas.value.push(item.raw)
                })
            }

            getAllDepreciationMethod();
            getAssetCategoryList();

            return {
                selections,
                pickerOptions,
                btnLoading,
                btnDisabled,
                loading,
                depreciationMethodList,
                addDialogVisible,
                editDialogVisible,
                uploadDialogVisible,
                editAssetCategoryForm,
                addAssetCategoryForm,
                formRules,
                total,
                queryMap,
                assetCategories,
                addAssetCategoryFormRef,
                editAssetCategoryFormRef,
                timeRange,
                fileList,
                fileDatas,
                server,
                reset,
                getAssetCategoryList,
                getAllDepreciationMethod,
                exportAssetCategory,
                changeAssetCategoryStatus,
                addAssetCategory,
                updateAssetCategory,
                deleteAssetCategory,
                deleteAssetCategoryByBatchId,
                importAssetCategory,
                handleSizeChange,
                handleCurrentChange,
                searchAssetCategory,
                sortChange,
                openAddDialog,
                openEditDialog,
                openUploadDialog,
                addCloseDialog,
                editCloseDialog,
                importCloseDialog,
                clearImportDialog,
                selectChange,
                beforeUpload,
                handleChange,
                handleRemove,
            };
        },
    };
</script>
