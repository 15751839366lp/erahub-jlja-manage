<template>
    <div id="decreaseMethod">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>资产管理</el-breadcrumb-item>
            <el-breadcrumb-item>元数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>减少方式</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-form :inline="true" ref="form" :model="queryMap" label-width="70px" size="small">
                <div style="display: inline-block">
                    <el-form-item label="ID" >
                        <el-input
                                @keyup.enter.native="searchDecreaseMethod"
                                @clear="searchDecreaseMethod"
                                clearable
                                v-model="queryMap.decreaseMethodId"
                                placeholder="请输入id查询"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="方式名称" style="margin-left: 50px">
                        <el-input
                                @keyup.enter.native="searchDecreaseMethod"
                                @clear="searchDecreaseMethod"
                                clearable
                                v-model="queryMap.decreaseMethodName"
                                placeholder="请输入方式名称查询"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="方式标记" style="margin-left: 50px">
                        <el-select
                                clearable
                                @clear="searchDecreaseMethod"
                                v-model="queryMap.methodMark"
                                placeholder="请选择方式标记"
                        >
                            <el-option
                                    v-for="methodMark in methodMarkList"
                                    :label="methodMark.methodMarkName"
                                    :key="methodMark.methodMarkId"
                                    :value="methodMark.methodMarkId"
                            >
                                <span style="float: left">{{ methodMark.methodMarkName }}</span>
                            </el-option>
                        </el-select>
                    </el-form-item>
                </div>
                <div style="display: inline-block">
                    <el-form-item label="减少方式">
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
                        <el-button type="primary" @click="searchDecreaseMethod" icon="el-icon-search"
                                   v-hasPermission="'asset:metadata:decreaseMethod:select'">查询
                        </el-button>
                        <el-button v-hasPermission="'asset:metadata:decreaseMethod:add'"
                                   type="success"
                                   icon="el-icon-plus"
                                   @click="openAddDialog"
                        >添加
                        </el-button>
                        <el-button @click="openUploadDialog"
                                   v-hasPermission="'asset:metadata:decreaseMethod:import'"
                                   icon="el-icon-upload">导入
                        </el-button>
                        <el-button @click="exportDecreaseMethod"
                                   v-hasPermission="'asset:metadata:decreaseMethod:export'"
                                   icon="el-icon-download">导出
                        </el-button>
                        <el-button @click="deleteDecreaseMethodByBatchId(selections)"
                                   icon="el-icon-delete"
                                   v-hasPermission="'asset:metadata:decreaseMethod:delete'"
                                   :disabled="selections.length === 0">批量
                        </el-button>
                    </el-form-item>
                </div>
            </el-form>
            <!-- 表格部分 -->
            <el-table
                    ref="table"
                    v-loading="loading"
                    row-key="decreaseMethodId"
                    style="width: 100%;"
                    height="490"
                    size="mini"
                    border
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    :data="decreaseMethods"
                    :row-style="{height: '30px'}"
                    @sort-change="sortChange"
                    @selection-change="selectChange"
            >
                <el-table-column type="selection" width="40px"></el-table-column>
                <el-table-column prop="decreaseMethodId" label="ID" width="100px" fixed sortable></el-table-column>
                <el-table-column prop="decreaseMethodName" label="方式名称" width="150px" fixed
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="assetQuantity" label="资产数量" width="100px" sortable>
                    <template #default="scope">
                        <el-tag type="success">{{scope.row.assetQuantity}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="methodMark" label="资源标记">
                    <template #default="scope">
                        <el-tag type="success" v-if="scope.row.methodMark==='0'">一般</el-tag>
                        <el-tag type="warning" v-if="scope.row.methodMark==='3'">盘亏</el-tag>
                        <el-tag type="danger" v-if="scope.row.methodMark==='5'">调出</el-tag>
                        <el-tag type="info" v-if="scope.row.methodMark==='9'">评估</el-tag>
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
                                   @change="changeDecreaseMethodStatus(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="150px">
                    <template #default="scope">
                        <el-button v-hasPermission="'asset:metadata:decreaseMethod:edit'"
                                   type="primary"
                                   icon="el-icon-edit"
                                   @click="openEditDialog(scope.row)"
                                   size="mini"
                        >
                        </el-button>
                        <el-button v-hasPermission="'asset:metadata:decreaseMethod:delete'"
                                   type="danger"
                                   icon="el-icon-delete"
                                   @click="deleteDecreaseMethod(scope.row)"
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
            <el-dialog title="添加减少方式" v-model="addDialogVisible" @close="addCloseDialog" width="50%">
                <span>
                  <el-form
                          :model="addDecreaseMethodForm"
                          :rules="formRules"
                          ref="addDecreaseMethodFormRef"
                          label-width="100px"
                  >
                    <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="方式ID" prop="decreaseMethodId">
                            <el-input v-model="addDecreaseMethodForm.decreaseMethodId"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="方式名称" prop="decreaseMethodName">
                            <el-input v-model="addDecreaseMethodForm.decreaseMethodName"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="资源标记" prop="methodMark">
                                 <el-select v-model="addDecreaseMethodForm.methodMark" placeholder="请选择资源标记">
                              <el-option
                                      v-for="methodMark in methodMarkList"
                                      :key="methodMark.methodMarkId"
                                      :label="methodMark.methodMarkName"
                                      :value="methodMark.methodMarkId"
                              ></el-option>
                            </el-select>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="状态" prop="status">
                                <el-radio v-model="addDecreaseMethodForm.status" :label="1">可用</el-radio>
                                <el-radio v-model="addDecreaseMethodForm.status" :label="0">禁用</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                          <el-col>
                          <el-form-item label="备注" prop="remark">
                              <el-input type="textarea" v-model="addDecreaseMethodForm.remark"
                                        style="width: 86%" :rows="3"></el-input>
                          </el-form-item>
                          </el-col>
                      </el-row>
                  </el-form>
                </span>
                <template #footer>
                    <span class="dialog-footer">
                      <el-button @click="addDialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="addDecreaseMethod" :disabled="btnDisabled"
                                 :loading="btnLoading">确 定</el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 编辑弹出框 -->
            <el-dialog title="编辑减少方式" v-model="editDialogVisible" @close="editCloseDialog" width="50%">
        <span>
          <el-form :model="editDecreaseMethodForm" :rules="formRules" ref="editDecreaseMethodFormRef"
                   label-width="100px">
            <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="方式ID" prop="decreaseMethodId">
                            <el-input v-model="editDecreaseMethodForm.decreaseMethodId" :disabled="true"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple">
                          <el-form-item label="方式名称" prop="decreaseMethodName">
                            <el-input v-model="editDecreaseMethodForm.decreaseMethodName"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="资源标记" prop="methodMark">
                                  <el-select v-model="editDecreaseMethodForm.methodMark" placeholder="请选择资源标记">
                              <el-option
                                      v-for="methodMark in methodMarkList"
                                      :key="methodMark.methodMarkId"
                                      :label="methodMark.methodMarkName"
                                      :value="methodMark.methodMarkId"
                              ></el-option>
                                  </el-select>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="10" style="margin-left: 30px">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="状态" prop="status">
                                <el-radio v-model="editDecreaseMethodForm.status" :label="1">可用</el-radio>
                                <el-radio v-model="editDecreaseMethodForm.status" :label="0">禁用</el-radio>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                      <el-row>
                          <el-col>
                          <el-form-item label="备注" prop="remark">
                              <el-input type="textarea" v-model="editDecreaseMethodForm.remark"
                                        style="width: 86%" :rows="3"></el-input>
                          </el-form-item>
                          </el-col>
                      </el-row>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateDecreaseMethod" :disabled="btnDisabled"
                     :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 上传弹出框 -->
            <el-dialog title="导入减少方式" v-model="uploadDialogVisible" @close="importCloseDialog" width="40%" center>
        <span style="display: inline-block;">
          <el-upload
                  accept=".xls,.xlsx"
                  class="upload-demo"
                  ref="upload"
                  :action="server + '/asset/metadata/decreasemethod/importDecreaseMethod'"
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
                           @click="importDecreaseMethod">导入文件</el-button>
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
        getDecreaseMethodListApi,
        changeDecreaseMethodStatusApi,
        exportDecreaseMethodExcelApi,
        addDecreaseMethodApi,
        updateDecreaseMethodApi,
        deleteDecreaseMethodApi,
        deleteDecreaseMethodByBatchIdApi,
        importDecreaseMethodApi,
    } from '../../../api/asset/metadata/decreaseMethod'

    export default {

        setup() {
            const selections = ref([])   //选中的值显示
            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const addDialogVisible = ref(false)
            const editDialogVisible = ref(false)
            const uploadDialogVisible = ref(false)
            const editDecreaseMethodForm = ref(null)
            const total = ref(0)
            const decreaseMethods = ref([])
            const addDecreaseMethodFormRef = ref(null)
            const editDecreaseMethodFormRef = ref(null)
            const timeRange = ref([])
            const fileList = ref([])
            const fileDatas = ref([])
            const server = import.meta.env.PROD ? import.meta.env.VITE_APP_BASE_API : "/api"

            const methodMarkList = ref([
                {
                    methodMarkId: '0',
                    methodMarkName: '一般',
                },
                {
                    methodMarkId: '3',
                    methodMarkName: '盘亏',
                },
                {
                    methodMarkId: '5',
                    methodMarkName: '调出',
                },
                {
                    methodMarkId: '9',
                    methodMarkName: '评估',
                },
            ])

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
                decreaseMethodId: [
                    {required: true, message: "请输入减少方式ID", trigger: "blur"},
                    {pattern: /^[+]?(0|([0-9]\d*))?$/, message: '请输入正确格式'}
                ],
                decreaseMethodName: [
                    {required: true, message: "请输入减少方式名称", trigger: "blur"}
                ],
                methodMark: [
                    {required: true, message: "请选择资源标记", trigger: "blur"}
                ],
                status: [
                    {required: true, message: "请选择状态", trigger: "blur"}
                ],
            })

            const addDecreaseMethodForm = ref({
                decreaseMethodId: null,
                decreaseMethodName: null,
                methodMark: null,
                status: 1,
                remark: null
            }) //添加表单

            const queryMap = reactive({
                decreaseMethodId: null,
                decreaseMethodName: null,
                methodMark: null,
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
                queryMap.decreaseMethodId = null;
                queryMap.decreaseMethodName = null;
                queryMap.methodMark = null;
                queryMap.status = null;
                queryMap.isAccurate = 0;
                queryMap.startCreateTime = null;
                queryMap.endCreateTime = null;
                timeRange.value = [];
                queryMap.isAsc = null;
                queryMap.sortColumn = null;
                queryMap.pageNum = 1;
                queryMap.pageSize = 10;

                getDecreaseMethodList()
            }

            //加载减少方式数据
            const getDecreaseMethodList = () => {

                if (timeRange.value != null && timeRange.value.length === 1) {
                    queryMap.startCreateTime = timeRange.value[0];
                } else if (timeRange.value != null && timeRange.value.length === 2) {
                    queryMap.startCreateTime = timeRange.value[0];
                    queryMap.endCreateTime = timeRange.value[1];
                }

                if (!utils.isEmpty(queryMap.decreaseMethodId) && !utils.isIneger(queryMap.decreaseMethodId)) {
                    ElMessage.error("请输入数值类型ID");
                    return;
                }
                loading.value = true;
                decreaseMethods.value = [];

                getDecreaseMethodListApi(queryMap).then((res) => {
                    if (!res.data.success) return ElMessage.error("查询失败: " + res.data.data.errorMsg);
                    decreaseMethods.value = res.data.data.rows;
                    total.value = res.data.data.total;
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("查询失败: " + res);
                });
            }

            /**
             * 导出
             */
            const exportDecreaseMethod = () => {
                exportDecreaseMethodExcelApi().then((res) => {
                    if (res.headers["content-type"] === "application/json") {
                        return ElMessage.error(
                            "Subject does not have permission [asset:metadata:decreaseMethod:export]"
                        );
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "减少方式列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                }).catch((res) => {
                    ElMessage.error("导出失败:" + res);
                });
            }

            /**
             * 禁用启用减少方式
             */
            const changeDecreaseMethodStatus = (row) => {
                changeDecreaseMethodStatusApi(row.decreaseMethodId, row.status).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("更新减少方式状态失败:" + res.data.data.errorMsg);
                        row.status = row.status == 1 ? 0 : 1;
                    } else {
                        const message = row.status == 1 ? '减少方式已启用' : '减少方式已禁用';
                        ElNotification({
                            type: 'success',
                            title: '操作成功',
                            message: message,
                        });
                    }
                }).catch((res) => {
                    ElMessage.error("更新减少方式状态失败:" + res);
                });
            }

            //添加减少方式
            const addDecreaseMethod = () => {
                addDecreaseMethodFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;

                        addDecreaseMethodApi(addDecreaseMethodForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("减少方式添加成功");
                                getDecreaseMethodList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                return ElMessage.error("减少方式添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            ElMessage.error("减少方式添加失败:" + res);
                        });
                    }
                });
            }

            //修改减少方式
            const updateDecreaseMethod = () => {
                editDecreaseMethodFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        updateDecreaseMethodApi(editDecreaseMethodForm.value).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "减少方式信息更新成功",
                                    type: "success"
                                });
                                getDecreaseMethodList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                ElMessage.error("减少方式信息更新失败:" + res.data.data.errorMsg);
                            }
                            editDialogVisible.value = false;
                        }).catch((res) => {
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            editDialogVisible.value = false;
                            ElMessage.error("减少方式信息更新失败:" + res);
                        });
                    }
                });
            }

            //删除减少方式
            const deleteDecreaseMethod = (row) => {
                if (row.assetQuantity > 0) {
                    ElMessage.error('该减少方式存在资产，无法删除');
                    return;
                }

                ElMessageBox.confirm(
                    "此操作将永久删除该减少方式, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteDecreaseMethodApi(row.decreaseMethodId).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("减少方式删除成功");
                                getDecreaseMethodList();
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

            //批量删除减少方式
            const deleteDecreaseMethodByBatchId = () => {
                let decreaseMethodIds = selections.value.map(item => item.decreaseMethodId);

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
                    "此操作将永久删除减少方式, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteDecreaseMethodByBatchIdApi(decreaseMethodIds).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("减少方式批量删除成功");
                                getDecreaseMethodList();
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

            //导入减少方式
            const importDecreaseMethod = () => {
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
                importDecreaseMethodApi(formData).then((res) => {
                    if (res.data.success) {
                        ElNotification({
                            title: "成功",
                            message: "导入减少方式成功",
                            type: "success"
                        });

                        getDecreaseMethodList();
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
                getDecreaseMethodList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getDecreaseMethodList();
            }

            /**
             * 搜索减少方式
             */
            const searchDecreaseMethod = () => {
                queryMap.pageNum = 1;
                getDecreaseMethodList();
            }

            //改变排序
            const sortChange = (column) => {
                if (column.prop == null || column.order == null) {
                    queryMap.isAsc = null;
                    queryMap.sortColumn = null;
                    getDecreaseMethodList();
                    return;
                }
                if (column.order == 'ascending') {
                    queryMap.isAsc = true;
                } else if (column.order == 'descending') {
                    queryMap.isAsc = false;
                }
                queryMap.sortColumn = utils.camelToSnakeCase(column.prop);
                getDecreaseMethodList();
            }

            //打开添加
            const openAddDialog = () => {
                addDialogVisible.value = true;
            }

            //打开修改
            const openEditDialog = (row) => {
                let newObj = {};
                editDecreaseMethodForm.value = utils.cloneObj(row, newObj);
                editDialogVisible.value = true;
            }

            //打开导入
            const openUploadDialog = (row) => {
                uploadDialogVisible.value = true;
            }

            //关闭弹出框
            const addCloseDialog = () => {
                addDecreaseMethodFormRef.value.clearValidate();
                addDecreaseMethodForm.value = {};
            }
            const editCloseDialog = () => {
                editDecreaseMethodFormRef.value.clearValidate();
                editDecreaseMethodForm.value = {};
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

            getDecreaseMethodList();

            return {
                selections,
                pickerOptions,
                btnLoading,
                btnDisabled,
                loading,
                addDialogVisible,
                editDialogVisible,
                uploadDialogVisible,
                editDecreaseMethodForm,
                addDecreaseMethodForm,
                formRules,
                total,
                queryMap,
                decreaseMethods,
                addDecreaseMethodFormRef,
                editDecreaseMethodFormRef,
                timeRange,
                fileList,
                fileDatas,
                server,
                methodMarkList,
                reset,
                getDecreaseMethodList,
                exportDecreaseMethod,
                changeDecreaseMethodStatus,
                addDecreaseMethod,
                updateDecreaseMethod,
                deleteDecreaseMethod,
                deleteDecreaseMethodByBatchId,
                importDecreaseMethod,
                handleSizeChange,
                handleCurrentChange,
                searchDecreaseMethod,
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
