<template>
    <div id="category">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资管理</el-breadcrumb-item>
            <el-breadcrumb-item>物资分类</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-button type="warning" icon="el-icon-circle-plus-outline" @click="openAdd"
                               v-hasPermission="'productCategory:add'">添加
                    </el-button>
                </el-col>
            </el-row>
            <!-- 表格部分 -->
            <el-table
                    ref="table"
                    v-loading="loading"
                    style="width: 100%; margin-top:10px;"
                    row-key="id"
                    border
                    :data="categorys"
                    lazy
                    :load="load"
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            >
                <el-table-column prop="id" label="ID" width="100px"></el-table-column>
                <el-table-column prop="name" label="分类名称" width="150px"></el-table-column>
                <el-table-column prop="sort" label="排序" width="100px"></el-table-column>
                <el-table-column prop="createTime" label="创建时间" ></el-table-column>
                <el-table-column prop="modifiedTime" label="修改时间" ></el-table-column>
                <el-table-column prop="remark" label="备注" ></el-table-column>
                <el-table-column prop="lev" label="层级">
                    <template #default="scope">
                        <el-tag v-if="scope.row.lev===1">一级分类</el-tag>
                        <el-tag type="success" v-else-if="scope.row.lev===2">二级分类</el-tag>
                        <el-tag type="danger" v-else>三级分类</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button v-hasPermission="'productCategory:edit'" type="primary" size="mini" icon="el-icon-edit"
                                   @click="editCategory(scope.row.id)"></el-button>
                        <el-button v-hasPermission="'productCategory:delete'" type="danger" size="mini"
                                   icon="el-icon-delete" @click="del(scope.row.id)"></el-button>
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
                    :page-sizes="[5,10, 15, 20, 25,30]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 添加弹出框 -->
            <el-dialog title="添加分类" v-model="addDialogVisible" @close="addCloseDialog" width="50%">
        <span>
          <el-form :model="addRuleForm" :rules="addRules" ref="addRuleFormRef" label-width="100px">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="addRuleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="父分类" prop="pid">
              <el-cascader
                      @change="selectParentChange"
                      :options="parentCategorys"
                      clearable
                      filterable
                      style="width:100%"
                      :props="selectProps"
                      v-model="pKeys"
              ></el-cascader>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="addRuleForm.remark"></el-input>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="addRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addCategory" :disabled="btnDisabled" :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 编辑弹出框 -->
            <el-dialog title="编辑分类" v-model="editDialogVisible" @close="editCloseDialog" width="50%">
        <span>
          <el-form :model="editRuleForm" :rules="addRules" ref="editRuleFormRef" label-width="100px">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="editRuleForm.name"></el-input>
            </el-form-item>

            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="editRuleForm.remark"></el-input>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="editRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateCategory" :disabled="btnDisabled" :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {
        update,
        edit,
        deleteProductCategory,
        categoryTree,
        getParentCategoryTree,
        add
    } from '../../../../api/business/material/productCategory'

    export default {

        setup() {

            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const pKeys = ref([])
            const addDialogVisible = ref(false)
            const editDialogVisible = ref(false)
            const editRuleForm = ref(null)
            const addRuleForm = ref({pid: "0", name: "", remark: "", sort: 0}) //添加表单
            const addRules = ref({
                name: [
                    {required: true, message: "请输入分类名", trigger: "blur"},
                    {min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur"}
                ],
                remark: [
                    {required: true, message: "请输入备注信息", trigger: "blur"}
                ],
                sort: [{required: true, message: "请输入排序号", trigger: "blur"}]
            })
            const total = ref(0)
            const queryMap = reactive({pageNum: 1, pageSize: 10})
            const categorys = ref([])
            const parentCategorys = ref([])
            const selectProps = reactive({
                expandTrigger: "hover",
                value: "id",
                label: "name",
                children: "children",
                checkStrictly: true
            }) //级联选择器配置项
            const columns = reactive([
                {
                    label: "分类名称",
                    prop: "name"
                },
                {
                    label: "排序",
                    prop: "sort"
                },
                {
                    label: "创建时间",
                    prop: "createTime"
                },
                {
                    label: "修改时间",
                    prop: "modifiedTime"
                },
                {
                    label: "备注",
                    prop: "remark"
                },
                {
                    label: "层级",
                    prop: "lev",
                    type: "template",
                    template: "lev"
                },
                {
                    label: "操作",
                    type: "template",
                    template: "caozuo"
                }
            ])

            const addRuleFormRef = ref(null)
            const editRuleFormRef = ref(null)


            const updateCategory = () => {
                editRuleFormRef.value.validate( valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        update(
                            "/business/material/productCategory/update/" + editRuleForm.value.id,
                            editRuleForm.value
                        ).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "分类信息更新",
                                    type: "success"
                                });
                                getCategoryList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                ElMessage.error("分类信息更新失败:" + res.data.data.errorMsg);
                            }
                            btnLoading.value = false;
                            btnDisabled.value = false;
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
            //修改
            const editCategory = (id) => {
                edit("/business/material/productCategory/edit/" + id).then((res) => {
                    if (res.data.success) {
                        editRuleForm.value = res.data.data;
                    } else {
                        return ElMessage.error("分类信息编辑失败" + res.data.data.errorMsg);
                    }
                    editDialogVisible.value = true;
                }).catch((res) => {
                    editDialogVisible.value = true;
                    ElMessage.error("分类信息编辑失败:" + res);
                });
            }
            //删除分类
            const del = (id) => {
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
                        deleteProductCategory(
                            "/business/material/productCategory/delete/" + id
                        ).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("分类删除成功");
                                getCategoryList();
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

            //父级分类中改变
            const selectParentChange = () => {
                const len = pKeys.value == null ? 0 : pKeys.value.length;
                if (len > 0) {
                    addRuleForm.value.pid = pKeys.value[len - 1];
                } else {
                    addRuleForm.value.pid = 0;
                }
            }
            //加载分类数据
            const getCategoryList = () => {
                categorys.value = [];
                categoryTree(queryMap).then((res) => {
                    if (!res.data.success) return ElMessage.error("分类列表失败");
                    res.data.data.rows.forEach((value, key, iterable) => {
                        if(value["children"] != null && value["children"].length > 0){
                            value["hasChildren"] = true
                            value["children"].forEach((value1, key, iterable) => {
                                if(value1["children"] != null && value1["children"].length > 0){
                                    value1["hasChildren"] = true
                                    value1["children"].forEach((value2, key, iterable) => {
                                        if(value2["children"] != null && value2["children"].length > 0){
                                            value2["hasChildren"] = true
                                        }else{
                                            value2["hasChildren"] = false
                                        }
                                    });
                                }else{
                                    value1["hasChildren"] = false
                                }
                            });
                        }else{
                            value["hasChildren"] = false
                        }
                    });
                    categorys.value = res.data.data.rows;
                    total.value = res.data.data.total;
                }).catch((res) => {
                    ElMessage.error("分类列表失败");
                });
            }
            //加载父级分类数据
            const getParentCategoryList = () => {
                getParentCategoryTree().then((res) => {
                    if (!res.data.success) return ElMessage.error("父级分类列表失败:" + res.data.data.errorMsg);
                    parentCategorys.value = res.data.data;
                }).catch((res) => {
                    ElMessage.error("父级分类列表失败:" + res);
                });
            }
            //添加分类
            const addCategory = () => {
                addRuleFormRef.value.validate( valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        if (addRuleForm.value.pid == null) {
                            addRuleForm.value.pid = 0;
                        }
                        add(addRuleForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("分类添加成功");
                                getCategoryList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                return ElMessage.error("分类添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                            btnLoading.value = false;
                            btnDisabled.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            ElMessage.error("分类添加失败:" + res);
                        });
                    }
                });
            }
            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.pageSize = newSize;
                getCategoryList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getCategoryList();
            }
            //打开添加
            const openAdd = () => {
                addDialogVisible.value = true;
                getParentCategoryList();
            }
            //关闭弹出框
            const addCloseDialog = () => {
                addRuleFormRef.value.clearValidate();
                addRuleForm.value = {};
                pKeys.value = [];
            }
            const editCloseDialog = () => {
                editRuleFormRef.value.clearValidate();
                editRuleForm.value = {};

            }
            const clearParent = () => {
                addRuleForm.value.pid = 0;
            }

            const load = (tree, treeNode, resolve) => {
                console.log(tree)
                resolve(tree.children)
            }

            getCategoryList();
            setTimeout(() => {
                loading.value = false;
            }, 500);

            return {
                btnLoading,
                btnDisabled,
                loading,
                pKeys,
                addDialogVisible,
                editDialogVisible,
                editRuleForm,
                addRuleForm,
                addRules,
                total,
                queryMap,
                categorys,
                parentCategorys,
                selectProps,
                columns,
                addRuleFormRef,
                editRuleFormRef,
                updateCategory,
                editCategory,
                del,
                selectParentChange,
                getCategoryList,
                getParentCategoryList,
                addCategory,
                handleSizeChange,
                handleCurrentChange,
                openAdd,
                addCloseDialog,
                editCloseDialog,
                clearParent,
                load
            };
        },
    };
</script>
