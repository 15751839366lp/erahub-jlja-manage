<template>
    <div id="serviceCondition">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>资产管理</el-breadcrumb-item>
            <el-breadcrumb-item>元数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>使用状态</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-form :inline="true" ref="form" :model="queryMap" label-width="70px" size="small">
                <div style="display: inline-block">
                    <el-form-item label="ID" >
                        <el-input
                                @keyup.enter.native="searchServiceCondition"
                                @clear="searchServiceCondition"
                                clearable
                                v-model="queryMap.serviceConditionId"
                                placeholder="请输入id查询"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="状态名称" style="margin-left: 50px">
                        <el-input
                                @keyup.enter.native="searchServiceCondition"
                                @clear="searchServiceCondition"
                                clearable
                                v-model="queryMap.serviceConditionName"
                                placeholder="请输入状态名称查询"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="计提折旧" style="margin-left: 50px">
                        <el-radio v-model="queryMap.accrualDepreciation" :label="1">是</el-radio>
                        <el-radio v-model="queryMap.accrualDepreciation" :label="0">否</el-radio>
                        <el-radio v-model="queryMap.accrualDepreciation" :label="null">全部</el-radio>
                    </el-form-item>
                </div>
                <div style="display: inline-block">
                    <el-form-item label="使用状态">
                        <el-radio v-model="queryMap.status" :label="1">可用</el-radio>
                        <el-radio v-model="queryMap.status" :label="0">禁用</el-radio>
                        <el-radio v-model="queryMap.status" :label="null">全部</el-radio>
                    </el-form-item>
                    <el-form-item label="查询类型" style="margin-left: 300px">
                        <el-radio v-model="queryMap.isAccurate" :label="0">模糊查询</el-radio>
                        <el-radio v-model="queryMap.isAccurate" :label="1">精确查询</el-radio>
                    </el-form-item>
                </div>
                <div style="display: inline-block">
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
                    <el-form-item style="float: right;margin-left: 30px; ">
                        <el-button @click="reset" icon="el-icon-refresh">重置</el-button>
                        <el-button type="primary" @click="searchServiceCondition" icon="el-icon-search"
                                   v-hasPermission="'asset:metadata:serviceCondition:select'">查询
                        </el-button>
                        <el-button v-hasPermission="'asset:metadata:serviceCondition:add'"
                                   type="success"
                                   icon="el-icon-plus"
                                   @click="openAddDialog"
                        >添加
                        </el-button>
                        <el-button @click="openUploadDialog"
                                   v-hasPermission="'asset:metadata:serviceCondition:import'"
                                   icon="el-icon-upload">导入
                        </el-button>
                        <el-button @click="exportServiceCondition"
                                   v-hasPermission="'asset:metadata:serviceCondition:export'"
                                   icon="el-icon-download">导出
                        </el-button>
                        <el-button @click="deleteServiceConditionByBatchId(selections)"
                                   icon="el-icon-delete"
                                   v-hasPermission="'asset:metadata:serviceCondition:delete'"
                                   :disabled="selections.length === 0">批量
                        </el-button>
                    </el-form-item>
                </div>
            </el-form>
            <!-- 表格部分 -->
            <el-table
                    ref="table"
                    v-loading="loading"
                    row-key="serviceConditionId"
                    style="width: 100%;"
                    height="490"
                    size="mini"
                    border
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    :data="serviceConditions"
                    :row-style="{height: '30px'}"
                    @sort-change="sortChange"
                    @selection-change="selectChange"
            >
                <el-table-column type="selection" width="40px"></el-table-column>
                <el-table-column prop="serviceConditionId" label="ID" width="100px" fixed sortable></el-table-column>
                <el-table-column prop="serviceConditionName" label="状态名称" width="150px" fixed
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="assetQuantity" label="资产数量" width="100px" sortable>
                    <template #default="scope">
                        <el-tag type="success">{{scope.row.assetQuantity}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="accrualDepreciation" label="计提折旧">
                    <template #default="scope">
                        <el-tag type="success" v-if="scope.row.accrualDepreciation===1">是</el-tag>
                        <el-tag type="danger" v-else-if="scope.row.accrualDepreciation===0">否</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="true" width="150"
                                 sortable></el-table-column>
                <el-table-column prop="modifiedTime" label="修改时间" :show-overflow-tooltip="true"
                                 width="150"></el-table-column>
                <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true" width="150"></el-table-column>
                <el-table-column prop="status" label="状态" width="100" fixed="right">
                    <template #default="scope">
                        <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"
                                   @change="changeServiceConditionStatus(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="150px">
                    <template #default="scope">
                        <el-button v-hasPermission="'asset:metadata:serviceCondition:edit'"
                                   type="primary"
                                   icon="el-icon-edit"
                                   @click="openEditDialog(scope.row)"
                                   size="mini"
                        >
                        </el-button>
                        <el-button v-hasPermission="'asset:metadata:serviceCondition:delete'"
                                   type="danger"
                                   icon="el-icon-delete"
                                   @click="deleteServiceCondition(scope.row)"
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
            <el-dialog title="添加使用状态" v-model="addDialogVisible" @close="addCloseDialog" width="50%">
                <span>
                  <el-form
                          :model="addServiceConditionForm"
                          :rules="formRules"
                          ref="addServiceConditionFormRef"
                          label-width="100px"
                  >
                    <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="状态ID" prop="serviceConditionId">
                            <el-input v-model="addServiceConditionForm.serviceConditionId"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="状态名称" prop="serviceConditionName">
                            <el-input v-model="addServiceConditionForm.serviceConditionName"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="计提折旧" prop="accrualDepreciation">
                                <el-radio v-model="addServiceConditionForm.accrualDepreciation" label="1">是</el-radio>
                                <el-radio v-model="addServiceConditionForm.accrualDepreciation"
                                          label="0">否</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="状态" prop="status">
                                <el-radio v-model="addServiceConditionForm.status" :label="1">可用</el-radio>
                                <el-radio v-model="addServiceConditionForm.status" :label="0">禁用</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                          <el-col>
                          <el-form-item label="备注" prop="remark">
                              <el-input type="textarea" v-model="addServiceConditionForm.remark"
                                        style="width: 86%" :rows="3"></el-input>
                          </el-form-item>
                          </el-col>
                      </el-row>
                  </el-form>
                </span>
                <template #footer>
                    <span class="dialog-footer">
                      <el-button @click="addDialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="addServiceCondition" :disabled="btnDisabled"
                                 :loading="btnLoading">确 定</el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 编辑弹出框 -->
            <el-dialog title="编辑使用状态" v-model="editDialogVisible" @close="editCloseDialog" width="50%">
        <span>
          <el-form :model="editServiceConditionForm" :rules="formRules" ref="editServiceConditionFormRef"
                   label-width="100px">
            <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="状态ID" prop="serviceConditionId">
                            <el-input v-model="editServiceConditionForm.serviceConditionId" :disabled="true"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="状态名称" prop="serviceConditionName">
                            <el-input v-model="editServiceConditionForm.serviceConditionName"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="计提折旧" prop="accrualDepreciation">
                                <el-radio v-model="editServiceConditionForm.accrualDepreciation"
                                          :label="1">是</el-radio>
                                <el-radio v-model="editServiceConditionForm.accrualDepreciation"
                                          :label="0">否</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="状态" prop="status">
                                <el-radio v-model="editServiceConditionForm.status" :label="1">可用</el-radio>
                                <el-radio v-model="editServiceConditionForm.status" :label="0">禁用</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                          <el-col>
                          <el-form-item label="备注" prop="remark">
                              <el-input type="textarea" v-model="editServiceConditionForm.remark"
                                        style="width: 86%" :rows="3"></el-input>
                          </el-form-item>
                          </el-col>
                      </el-row>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateServiceCondition" :disabled="btnDisabled"
                     :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 上传弹出框 -->
            <el-dialog title="导入使用状态" v-model="uploadDialogVisible" @close="importCloseDialog" width="40%" center>
        <span style="display: inline-block;">
          <el-upload
                  accept=".xls,.xlsx"
                  class="upload-demo"
                  ref="upload"
                  :action="server + '/asset/metadata/servicecondition/importServiceCondition'"
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
                           @click="importServiceCondition">导入文件</el-button>
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
        getServiceConditionListApi,
        changeServiceConditionStatusApi,
        exportServiceConditionExcelApi,
        addServiceConditionApi,
        updateServiceConditionApi,
        deleteServiceConditionApi,
        deleteServiceConditionByBatchIdApi,
        importServiceConditionApi,
    } from '../../../api/asset/metadata/serviceCondition'

    export default {

        setup() {
            const selections = ref([])   //选中的值显示
            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const addDialogVisible = ref(false)
            const editDialogVisible = ref(false)
            const uploadDialogVisible = ref(false)
            const editServiceConditionForm = ref(null)
            const total = ref(0)
            const serviceConditions = ref([])
            const addServiceConditionFormRef = ref(null)
            const editServiceConditionFormRef = ref(null)
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

            const formRules = ref({
                serviceConditionId: [
                    {required: true, message: "请输入使用状态ID", trigger: "blur"},
                    {pattern: /^[+]?(0|([0-9]\d*))?$/, message: '请输入正确格式'}
                ],
                serviceConditionName: [
                    {required: true, message: "请输入使用状态名称", trigger: "blur"}
                ],
                accrualDepreciation: [
                    {required: true, message: "请选择是否计提折旧", trigger: "blur"}
                ],
                status: [
                    {required: true, message: "请选择状态", trigger: "blur"}
                ],
            })

            const addServiceConditionForm = ref({
                serviceConditionId: null,
                serviceConditionName: null,
                accrualDepreciation: 1,
                status: 1,
                remark: null
            }) //添加表单

            const queryMap = reactive({
                serviceConditionId: null,
                serviceConditionName: null,
                accrualDepreciation: null,
                status: null,
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
                queryMap.serviceConditionId = null;
                queryMap.serviceConditionName = null;
                queryMap.accrualDepreciation = null;
                queryMap.status = null;
                queryMap.netResidualValue = null;
                queryMap.isAccurate = 0;
                queryMap.startCreateTime = null;
                queryMap.endCreateTime = null;
                timeRange.value = [];
                queryMap.isAsc = null;
                queryMap.sortColumn = null;
                queryMap.pageNum = 1;
                queryMap.pageSize = 10;

                getServiceConditionList()
            }

            //加载使用状态数据
            const getServiceConditionList = () => {

                if (timeRange.value != null && timeRange.value.length === 1) {
                    queryMap.startCreateTime = timeRange.value[0];
                } else if (timeRange.value != null && timeRange.value.length === 2) {
                    queryMap.startCreateTime = timeRange.value[0];
                    queryMap.endCreateTime = timeRange.value[1];
                }

                if (!utils.isEmpty(queryMap.serviceConditionId) && !utils.isIneger(queryMap.serviceConditionId)) {
                    ElMessage.error("请输入数值类型ID");
                    return;
                }
                loading.value = true;
                serviceConditions.value = [];

                getServiceConditionListApi(queryMap).then((res) => {
                    if (!res.data.success) return ElMessage.error("查询失败：" + res.data.data.errorMsg);
                    serviceConditions.value = res.data.data.rows;
                    total.value = res.data.data.total;
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("查询失败：" + res);
                });
            }

            /**
             * 导出
             */
            const exportServiceCondition = () => {
                exportServiceConditionExcelApi().then((res) => {
                    if (res.headers["content-type"] === "application/json") {
                        return ElMessage.error(
                            "Subject does not have permission [asset:metadata:serviceCondition:export]"
                        );
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "使用状态列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                }).catch((res) => {
                    ElMessage.error("导出失败:" + res);
                });
            }

            /**
             * 禁用启用状态
             */
            const changeServiceConditionStatus = (row) => {
                changeServiceConditionStatusApi(row.serviceConditionId, row.status).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("更新使用状态状态失败:" + res.data.data.errorMsg);
                        row.status = row.status == 1 ? 0 : 1;
                    } else {
                        const message = row.status == 1 ? '使用状态状态已启用' : '使用状态状态已禁用';
                        ElNotification({
                            type: 'success',
                            title: '操作成功',
                            message: message,
                        });
                    }
                }).catch((res) => {
                    ElMessage.error("更新使用状态状态失败:" + res);
                });
            }

            //添加使用状态
            const addServiceCondition = () => {
                addServiceConditionFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;

                        addServiceConditionApi(addServiceConditionForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("使用状态添加成功");
                                getServiceConditionList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                return ElMessage.error("使用状态添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            ElMessage.error("使用状态添加失败:" + res);
                        });
                    }
                });
            }

            //修改使用状态
            const updateServiceCondition = () => {
                editServiceConditionFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        updateServiceConditionApi(editServiceConditionForm.value).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "使用状态信息更新成功",
                                    type: "success"
                                });
                                getServiceConditionList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                ElMessage.error("使用状态信息更新失败:" + res.data.data.errorMsg);
                            }
                            editDialogVisible.value = false;
                        }).catch((res) => {
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            editDialogVisible.value = false;
                            ElMessage.error("使用状态信息更新失败:" + res);
                        });
                    }
                });
            }

            //删除使用状态
            const deleteServiceCondition = (row) => {
                if (row.assetQuantity > 0) {
                    ElMessage.error('该状态存在资产，无法删除');
                    return;
                }

                ElMessageBox.confirm(
                    "此操作将永久删除该使用状态, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteServiceConditionApi(row.serviceConditionId).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("使用状态删除成功");
                                getServiceConditionList();
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

            //批量删除使用状态
            const deleteServiceConditionByBatchId = () => {
                let serviceConditionIds = selections.value.map(item => item.serviceConditionId);

                let flag = true;
                selections.value.forEach(item => {
                    if (item.assetQuantity > 0) {
                        flag = false;
                    }
                })

                if (!flag) {
                    ElMessage.error('请勿选择存在资产的节点');
                    return;
                }
                ElMessageBox.confirm(
                    "此操作将永久删除使用状态, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteServiceConditionByBatchIdApi(serviceConditionIds).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("使用状态批量删除成功");
                                getServiceConditionList();
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

            //导入使用状态
            const importServiceCondition = () => {
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
                importServiceConditionApi(formData).then((res) => {
                    if (res.data.success) {
                        ElNotification({
                            title: "成功",
                            message: "导入使用状态成功",
                            type: "success"
                        });

                        getServiceConditionList();
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
                getServiceConditionList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getServiceConditionList();
            }

            /**
             * 搜索状态
             */
            const searchServiceCondition = () => {
                queryMap.pageNum = 1;
                getServiceConditionList();
            }

            //改变排序
            const sortChange = (column) => {
                if (column.prop == null || column.order == null) {
                    queryMap.isAsc = null;
                    queryMap.sortColumn = null;
                    getServiceConditionList();
                    return;
                }
                if (column.order == 'ascending') {
                    queryMap.isAsc = true;
                } else if (column.order == 'descending') {
                    queryMap.isAsc = false;
                }
                queryMap.sortColumn = utils.camelToSnakeCase(column.prop);
                getServiceConditionList();
            }

            //打开添加
            const openAddDialog = () => {
                addDialogVisible.value = true;
            }

            //打开修改
            const openEditDialog = (row) => {
                let newObj = {};
                editServiceConditionForm.value = utils.cloneObj(row, newObj);
                editDialogVisible.value = true;
            }

            //打开导入
            const openUploadDialog = (row) => {
                uploadDialogVisible.value = true;
            }

            //关闭弹出框
            const addCloseDialog = () => {
                addServiceConditionFormRef.value.clearValidate();
                addServiceConditionForm.value = {};
            }
            const editCloseDialog = () => {
                editServiceConditionFormRef.value.clearValidate();
                editServiceConditionForm.value = {};
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

            getServiceConditionList();

            return {
                selections,
                pickerOptions,
                btnLoading,
                btnDisabled,
                loading,
                addDialogVisible,
                editDialogVisible,
                uploadDialogVisible,
                editServiceConditionForm,
                addServiceConditionForm,
                formRules,
                total,
                queryMap,
                serviceConditions,
                addServiceConditionFormRef,
                editServiceConditionFormRef,
                timeRange,
                fileList,
                fileDatas,
                server,
                reset,
                getServiceConditionList,
                exportServiceCondition,
                changeServiceConditionStatus,
                addServiceCondition,
                updateServiceCondition,
                deleteServiceCondition,
                deleteServiceConditionByBatchId,
                importServiceCondition,
                handleSizeChange,
                handleCurrentChange,
                searchServiceCondition,
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
