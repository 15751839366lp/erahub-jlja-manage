<template>
    <div id="section">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>资产管理</el-breadcrumb-item>
            <el-breadcrumb-item>元数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>单位管理</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card">
            <!-- 节点过滤 -->
            <el-form :inline="true" ref="form" :model="queryMap" label-width="70px" size="small">

                <el-form-item label="ID">
                    <el-input
                            @keyup.enter.native="getSectionList"
                            @clear="getSectionList"
                            clearable
                            v-model="queryMap.sectionId"
                            placeholder="请输入id查询"
                    ></el-input>
                </el-form-item>
                <el-form-item label="单位名称">
                    <el-input
                            @keyup.enter.native="getSectionList"
                            @clear="getSectionList"
                            clearable
                            v-model="queryMap.sectionName"
                            placeholder="请输入单位名称查询"
                    ></el-input>
                </el-form-item>
                <el-form-item label="单位简称">
                    <el-input
                            @keyup.enter.native="getSectionList"
                            clearable
                            @clear="getSectionList"
                            v-model="queryMap.sectionAbbreviation"
                            placeholder="请输入单位简称查询"
                    ></el-input>
                </el-form-item>
                <el-form-item label="最深级数">
                    <el-input
                            @keyup.enter.native="getSectionList"
                            clearable
                            @clear="getSectionList"
                            v-model="queryMap.level"
                            placeholder="请输入最深级数查询"
                    ></el-input>
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
                <el-form-item style="margin-left: 80px; ">
                    <!--                    <el-button-->
                    <!--                            type="primary"-->
                    <!--                            plain-->
                    <!--                            :icon="openFlag ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"-->
                    <!--                            size="mini"-->
                    <!--                            @click="toggleRowExpansion(openFlag)"-->
                    <!--                    >{{openFlag ? '收起' : '展开'}}</el-button>-->
                    <el-button @click="reset" icon="el-icon-refresh">重置</el-button>
                    <el-button type="primary" @click="getSectionList" icon="el-icon-search"
                               v-hasPermission="'asset:metadata:section:select'">查询
                    </el-button>
                    <el-button v-hasPermission="'asset:metadata:section:add'"
                               type="success"
                               icon="el-icon-plus"
                               @click="openAddDialog"
                    >添加
                    </el-button>
                    <el-button @click="openUploadDialog"
                               v-hasPermission="'asset:metadata:section:import'"
                               icon="el-icon-upload">导入
                    </el-button>
                    <el-button @click="exportSection"
                               v-hasPermission="'asset:metadata:section:export'"
                               icon="el-icon-download">导出
                    </el-button>
                    <el-button @click="deleteSectionByBatchId(selections)"
                               icon="el-icon-delete"
                               v-hasPermission="'asset:metadata:section:delete'"
                               :disabled="selections.length === 0">批量
                    </el-button>
                </el-form-item>
            </el-form>

            <!-- 表格部分 -->
            <el-table
                    ref="sectionTable"
                    v-loading="loading"
                    row-key="sectionId"
                    default-expand-all
                    style="width: 100%;"
                    height="490"
                    size="mini"
                    border
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    :data="sections"
                    :row-style="{height: '30px'}"
                    @selection-change="selectChange"
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                >
                <el-table-column type="selection" width="40px"></el-table-column>
                <el-table-column prop="sectionId" label="ID" width="200px" fixed
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="sectionName" label="单位名称" width="150px" fixed
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="sectionAbbreviation" label="单位简称" width="150px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="assetQuantity" label="资产数量">
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
                <el-table-column prop="detailed" label="明细">
                    <template #default="scope">
                        <el-tag type="success" v-if="scope.row.detailed===1">是</el-tag>
                        <el-tag type="danger" v-else-if="scope.row.detailed===0">否</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="true"
                                 width="150"></el-table-column>
                <el-table-column prop="modifiedTime" label="修改时间" :show-overflow-tooltip="true"
                                 width="150"></el-table-column>
                <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true" width="150"></el-table-column>
                <el-table-column prop="status" label="状态" width="100" fixed="right">
                    <template #default="scope">
                        <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"
                                   @change="changeSectionStatus(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="150px">
                    <template #default="scope">
                        <el-button v-hasPermission="'asset:metadata:section:edit'"
                                   type="primary"
                                   icon="el-icon-edit"
                                   @click="openEditDialog(scope.row)"
                                   size="mini"
                        >
                        </el-button>
                        <el-button v-hasPermission="'asset:metadata:section:delete'"
                                   type="danger"
                                   icon="el-icon-delete"
                                   @click="deleteSection(scope.row)"
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
                    layout="total"
                    :total="total"
            ></el-pagination>

            <!-- 添加弹出框 -->
            <el-dialog title="添加单位" v-model="addDialogVisible" @close="addCloseDialog" width="50%">
                            <span>
                              <el-form
                                      :model="addSectionForm"
                                      :rules="formRules"
                                      ref="addSectionFormRef"
                                      label-width="100px"
                              >
                                <el-row>
                                  <el-col :span="10">
                                    <div class="grid-content bg-purple">
                                      <el-form-item label="单位ID" prop="sectionId">
                                        <el-input v-model="addSectionForm.sectionId"></el-input>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                </el-row>
                                  <el-row>
                                      <el-col :span="10">
                                    <div class="grid-content bg-purple">
                                      <el-form-item label="单位名称" prop="sectionName">
                                        <el-input v-model="addSectionForm.sectionName"></el-input>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                    <el-col :span="10" style="margin-left: 30px">
                                    <div class="grid-content bg-purple">
                                      <el-form-item label="单位简称" prop="sectionName">
                                        <el-input v-model="addSectionForm.sectionAbbreviation"></el-input>
                                      </el-form-item>
                                    </div>
                                  </el-col>

                                </el-row>
                                  <el-row>
                                       <el-col :span="10">
                                   <div class="grid-content bg-purple">
                                      <el-form-item label="明细" prop="detailed">
                                            <el-radio v-model="addSectionForm.detailed" :label="1">是</el-radio>
                                            <el-radio v-model="addSectionForm.detailed" :label="0">否</el-radio>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                       <el-col :span="10" style="margin-left: 30px">
                                    <div class="grid-content bg-purple-light">
                                      <el-form-item label="状态" prop="status">
                                            <el-radio v-model="addSectionForm.status" :label="1">可用</el-radio>
                                            <el-radio v-model="addSectionForm.status" :label="0">禁用</el-radio>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                  </el-row>
                                  <el-row>
                                      <el-col>
                                      <el-form-item label="备注" prop="remark">
                                          <el-input type="textarea" v-model="addSectionForm.remark"
                                                    style="width: 86%" :rows="3"></el-input>
                                      </el-form-item>
                                      </el-col>
                                  </el-row>
                              </el-form>
                            </span>
                <template #footer>
                                <span class="dialog-footer">
                                  <el-button @click="addDialogVisible = false">取 消</el-button>
                                  <el-button type="primary" @click="addSection" :disabled="btnDisabled"
                                             :loading="btnLoading">确 定</el-button>
                                </span>
                </template>
            </el-dialog>

                        <!-- 编辑弹出框 -->
                        <el-dialog title="编辑单位" v-model="editDialogVisible" @close="editCloseDialog" width="50%">
                    <span>
                      <el-form :model="editSectionForm" :rules="formRules" ref="editSectionFormRef"
                               label-width="100px">
                        <el-row>
                                  <el-col :span="10">
                                    <div class="grid-content bg-purple">
                                      <el-form-item label="单位ID" prop="sectionId">
                                        <el-input v-model="editSectionForm.sectionId"></el-input>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                </el-row>
                                  <el-row>
                                      <el-col :span="10">
                                    <div class="grid-content bg-purple">
                                      <el-form-item label="单位名称" prop="sectionName">
                                        <el-input v-model="editSectionForm.sectionName"></el-input>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                    <el-col :span="10" style="margin-left: 30px">
                                    <div class="grid-content bg-purple">
                                      <el-form-item label="单位简称" prop="sectionName">
                                        <el-input v-model="editSectionForm.sectionAbbreviation"></el-input>
                                      </el-form-item>
                                    </div>
                                  </el-col>

                                </el-row>
                                  <el-row>
                                       <el-col :span="10">
                                   <div class="grid-content bg-purple">
                                      <el-form-item label="明细" prop="detailed">
                                            <el-radio v-model="editSectionForm.detailed" :label="1">是</el-radio>
                                            <el-radio v-model="editSectionForm.detailed" :label="0">否</el-radio>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                       <el-col :span="10" style="margin-left: 30px">
                                    <div class="grid-content bg-purple-light">
                                      <el-form-item label="状态" prop="status">
                                            <el-radio v-model="editSectionForm.status" :label="1">可用</el-radio>
                                            <el-radio v-model="editSectionForm.status" :label="0">禁用</el-radio>
                                      </el-form-item>
                                    </div>
                                  </el-col>
                                  </el-row>
                                  <el-row>
                                      <el-col>
                                      <el-form-item label="备注" prop="remark">
                                          <el-input type="textarea" v-model="editSectionForm.remark"
                                                    style="width: 86%" :rows="3"></el-input>
                                      </el-form-item>
                                      </el-col>
                                  </el-row>
                              </el-form>
                    </span>
                            <template #footer>
                            <span class="dialog-footer">
                      <el-button @click="editDialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="updateSection" :disabled="btnDisabled"
                                 :loading="btnLoading">确 定</el-button>
                    </span>
                            </template>
                        </el-dialog>
                        <!-- 上传弹出框 -->
                        <el-dialog title="导入单位" v-model="uploadDialogVisible" @close="importCloseDialog" width="40%" center>
                    <span style="display: inline-block;">
                      <el-upload
                              accept=".xls,.xlsx"
                              class="upload-demo"
                              ref="upload"
                              :action="server + '/asset/metadata/section/importSection'"
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
                                       @click="importSection">导入文件</el-button>
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
        getSectionListApi,
        exportSectionExcelApi,
        changeSectionStatusApi,
        addSectionApi,
        updateSectionApi,
        deleteSectionApi,
        deleteSectionByBatchIdApi,
        importSectionApi,
    } from '../../../api/asset/metadata/section'


    export default {

        setup() {
            const selections = ref([])   //选中的值显示
            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const addDialogVisible = ref(false)
            const editDialogVisible = ref(false)
            const uploadDialogVisible = ref(false)
            const editSectionForm = ref(null)
            const total = ref(0)
            const sections = ref([])
            const addSectionFormRef = ref(null)
            const editSectionFormRef = ref(null)
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

            const addSectionForm = ref({
                sectionId: null,
                sectionName: null,
                sectionAbbreviation: null,
                detailed: null,
                status: null,
                remark: null,
            }) //添加表单
            const formRules = ref({
                sectionId: [
                    {required: true, message: "请输入单位ID", trigger: "blur"},
                    {pattern: /^[+]?(0|([0-9]\d*))?$/, message: '请输入正确格式'}
                ],
                sectionName: [
                    {required: true, message: "请输入单位名称", trigger: "blur"}
                ],
                sectionAbbreviation: [
                    {required: true, message: "请输入单位简称", trigger: "blur"}
                ],
                detailed: [
                    {required: true, message: "请选择明细", trigger: "blur"}
                ],
                status: [
                    {required: true, message: "请选择状态", trigger: "blur"}
                ],
            })

            const queryMap = reactive({
                sectionId: null,
                sectionName: null,
                sectionAbbreviation: null,
                level: 2,
                detailed: null,
                status: null,
                remark: null,
                isAccurate: 0,
                startCreateTime: null,
                endCreateTime: null,
            })

            /**
             * 重置
             */
            const reset = () => {
                queryMap.sectionId = null;
                queryMap.sectionName = null;
                queryMap.sectionAbbreviation = null;
                queryMap.level = 5;
                queryMap.detailed = null;
                queryMap.status = null;
                queryMap.remark = null;
                queryMap.isAccurate = 0;
                queryMap.startCreateTime = null;
                queryMap.endCreateTime = null;
                timeRange.value = [];

                getSectionList()
            }

            //加载单位数据
            const getSectionList = () => {

                if (timeRange.value != null && timeRange.value.length === 1) {
                    queryMap.startCreateTime = timeRange.value[0];
                } else if (timeRange.value != null && timeRange.value.length === 2) {
                    queryMap.startCreateTime = timeRange.value[0];
                    queryMap.endCreateTime = timeRange.value[1];
                }

                if (!utils.isEmpty(queryMap.sectionId) && !utils.isStringIneger(queryMap.sectionId)) {
                    ElMessage.error("请输入数值类型ID");
                    return;
                }
                if (!utils.isEmpty(queryMap.level) && !utils.isIneger(queryMap.level)) {
                    ElMessage.error("请输入数值类型");
                    return;
                }
                loading.value = true;
                sections.value = [];

                getSectionListApi(queryMap).then((res) => {
                    if (!res.data.success) return ElMessage.error("查询失败：" + res.data.data.errorMsg);
                    sections.value = res.data.data.rows;
                    total.value = res.data.data.total;
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("查询失败：" + res);
                });
            }

            // //懒加载
            // const getChildrenList = (tree, treeNode, resolve) => {
            //     if (tree.children == []) {
            //         return
            //     }
            //     getChildrenListApi(tree.sectionId).then((res) => {
            //         if (!res.data.success) return ElMessage.error("查询失败子节点：" + res.data.data.errorMsg);
            //         resolve(res.data.data.rows);
            //         total.value = total.value + res.data.data.total;
            //     }).catch((res) => {
            //         ElMessage.error("查询失败子节点：" + res);
            //     });
            // }

            /**
             * 导出
             */
            const exportSection = () => {
                exportSectionExcelApi().then((res) => {
                    if (res.headers["content-type"] === "application/json") {
                        return ElMessage.error(
                            "Subject does not have permission [asset:metadata:section:export]"
                        );
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "使用单位列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                }).catch((res) => {
                    ElMessage.error("导出失败:" + res);
                });
            }

            /**
             * 禁用启用单位
             */
            const changeSectionStatus = (row) => {
                changeSectionStatusApi(row.sectionId, row.status).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("更新单位状态失败:" + res.data.data.errorMsg);
                        row.status = row.status == 1 ? 0 : 1;
                    } else {
                        const message = row.status == 1 ? '单位状态已启用' : '单位状态已禁用';
                        ElNotification({
                            type: 'success',
                            title: '操作成功',
                            message: message,
                        });
                    }
                }).catch((res) => {
                    ElMessage.error("更新单位状态失败:" + res);
                });
            }

            //添加单位
            const addSection = () => {
                addSectionFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;

                        addSectionApi(addSectionForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("添加成功");
                                getSectionList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                return ElMessage.error("添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            ElMessage.error("添加失败:" + res);
                        });
                    }
                });
            }

            //修改单位
            const updateSection = () => {
                editSectionFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        updateSectionApi(editSectionForm.value).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "更新成功",
                                    type: "success"
                                });
                                getSectionList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                ElMessage.error("更新失败:" + res.data.data.errorMsg);
                            }
                            editDialogVisible.value = false;
                        }).catch((res) => {
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            editDialogVisible.value = false;
                            ElMessage.error("更新失败:" + res);
                        });
                    }
                });
            }

            //删除单位
            const deleteSection = (row) => {
                if(row.detailed == 0){
                    ElMessage.error('该节点非明细节点，无法删除');
                    return;
                }
                if(row.assetQuantity > 0){
                    ElMessage.error('该单位存在资产，无法删除');
                    return;
                }
                ElMessageBox.confirm(
                    "此操作将永久删除该单位, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteSectionApi(row.sectionId).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("单位删除成功");
                                getSectionList();
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

            //批量删除单位
            const deleteSectionByBatchId = () => {
                let sectionIds = selections.value.map(item => item.sectionId);
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
                    ElMessage.error('请勿选择非明细节点，或存在资产的节点');
                    return;
                }

                ElMessageBox.confirm(
                    "此操作将永久删除该单位, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteSectionByBatchIdApi(sectionIds).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("单位批量删除成功");
                                getSectionList();
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

            //导入单位
            const importSection = () => {
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
                importSectionApi(formData).then((res) => {
                    if (res.data.success) {
                        ElNotification({
                            title: "成功",
                            message: "导入单位成功",
                            type: "success"
                        });

                        getSectionList();
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


            //打开添加
            const openAddDialog = () => {
                addDialogVisible.value = true;
            }

            //打开修改
            const openEditDialog = (row) => {
                let newObj = {};
                editSectionForm.value = utils.cloneObj(row, newObj);
                editDialogVisible.value = true;
            }

            //打开导入
            const openUploadDialog = (row) => {
                uploadDialogVisible.value = true;
            }

            //关闭弹出框
            const addCloseDialog = () => {
                addSectionFormRef.value.clearValidate();
                addSectionForm.value = {};
            }
            const editCloseDialog = () => {
                editSectionFormRef.value.clearValidate();
                editSectionForm.value = {};
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

            getSectionList();
            queryMap.level = 5;

            return {
                selections,
                pickerOptions,
                btnLoading,
                btnDisabled,
                loading,
                addDialogVisible,
                editDialogVisible,
                uploadDialogVisible,
                editSectionForm,
                addSectionForm,
                formRules,
                total,
                queryMap,
                sections,
                addSectionFormRef,
                editSectionFormRef,
                timeRange,
                fileList,
                fileDatas,
                server,
                reset,
                getSectionList,
                exportSection,
                changeSectionStatus,
                addSection,
                updateSection,
                deleteSection,
                deleteSectionByBatchId,
                importSection,
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
