<template>
    <div id="productCategroys">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资管理</el-breadcrumb-item>
            <el-breadcrumb-item>物资资料</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 右侧卡片区域 -->
        <el-card class="box-card">
            <el-container style="margin-bottom:20px;">
                <el-alert
                        show-icon
                        title="友情提示:  商品的分类只支持三级分类"
                        type="warning">
                </el-alert>
            </el-container>
            <el-row :gutter="6">
                <el-col :span="5">
                    <el-cascader
                            size="small"
                            placeholder="请选择分类查询"
                            @change="selectChange"
                            v-model="categorykeys"
                            :props="searchSelectProps"
                            :options="cateories"
                            clearable
                    ></el-cascader>
                </el-col>
                <el-col :span="6">
                    <el-input
                            clearable
                            size="small"
                            v-model="queryMap.name"
                            placeholder="请输入物资名称查询"
                            @clear="search"
                            class="input-with-select"
                    ></el-input>
                </el-col>
                <el-col :span="5">
                    <template>
                        <el-select size="small" v-model="queryMap.status" @click="search" placeholder="请选择状态">
                            <el-option label="正常" :value="0"></el-option>
                            <el-option label="回收站" :value="1"></el-option>
                            <el-option label="待审核" :value="2"></el-option>
                        </el-select>
                    </template>
                </el-col>
                <el-col :span="8">
                    <el-button size="small" type="primary" icon="el-icon-search" @click="search">查找</el-button>
                    <el-button size="small" icon="el-icon-refresh-right" type="warning" @click="resetForm">重置
                    </el-button>
                    <el-button size="small" type="success" icon="el-icon-circle-plus-outline" @click="openAdd"
                               v-hasPermission="'product:add'">添加
                    </el-button>
                    <el-button size="small" icon="el-icon-refresh" @click="getproductList">刷新</el-button>
                </el-col>
            </el-row>

            <!-- 表格区域 -->

            <el-table
                    size="mini"
                    v-loading="loading"
                    border
                    stripe
                    :data="productData"
                    style="width: 100%;margin-top:20px;"
                    height="400"
            >
                <el-table-column prop="id" type="index" label="ID"></el-table-column>

                <el-table-column prop="imageUrl" label="图片" align="center" width="150px" padding="0px">
                    <!--            <template slot-scope="scope">-->
                    <!--              <img-->
                    <!--                slot="error"-->
                    <!--                :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"-->
                    <!--                alt-->
                    <!--                style="width: 55px;height:55px"-->
                    <!--              />-->
                    <!--            </template>-->
                    <template #default="scope">
                        <el-popover placement="right" trigger="hover">
                            <img :src="scope.row.imageUrlShow"
                                 style="height: 200px;width: 200px"/>
                            <template #reference>
                                <img :src="'http://127.0.0.1:8989/'+scope.row.imageUrlShow"
                                     :alt="scope.row.imgUrl" style="height: 38px;width: 38px;cursor: pointer">
                            </template>
                        </el-popover>
                    </template>
                </el-table-column>

                <el-table-column prop="name" label="物资名称"></el-table-column>


                <el-table-column prop="pnum" :show-overflow-tooltip='true' label="物资编号"></el-table-column>
                <el-table-column label="物资规格" width="100">
                    <template #default="scope">
                        <el-tag type="success" size="mini" closable v-text="scope.row.model"></el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="80"></el-table-column>
                <el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                        <el-tag size="mini" type="danger" effect="plain" v-if="scope.row.status==1">回收</el-tag>
                        <el-tag size="mini" effect="plain" v-if="scope.row.status==0">正常</el-tag>
                        <el-tag size="mini" effect="plain" type="warning" v-if="scope.row.status==2">审核中</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间"></el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <!--              给管理员提供的恢复和删除-->

                        <span v-if="scope.row.status==1">
                 <el-popconfirm

                         @confirm="backStock(scope.row.id)"
                         style="margin-left:10px;"
                         confirmButtonText='好的'
                         cancelButtonText='不用了'
                         icon="el-icon-info"
                         iconColor="green"
                         title="这是一段内容确定恢复吗？"
                 >
                     <template #reference>
                <el-button type="text" size="mini" icon="el-icon-back">恢复</el-button>
                         </template>
                 </el-popconfirm>
              <el-button style="margin-left:10px;" type="text" size="mini" icon="el-icon-delete"
                         @click="del(scope.row.id)">删除</el-button>
              </span>

                        <!--              给操作员提供的编辑和回收-->
                        <span v-if="scope.row.status==0">
               <el-button type="text" size="mini" icon="el-icon-edit" @click="editStock(scope.row.id)">编辑</el-button>
              <el-popconfirm
                      @confirm="removeStock(scope.row.id)"
                      style="margin-left:10px;"
                      confirmButtonText='好的'
                      cancelButtonText='不用了'
                      icon="el-icon-info"
                      iconColor="red"
                      title="这是一段内容确定移入回收站吗？"
              >
                  <template #reference>
              <el-button type="text" size="mini" icon="el-icon-s-operation">回收站</el-button>
                      </template>
            </el-popconfirm>
              </span>

                        <!--          给管理员提供的审核和不通过-->
                        <span v-if="scope.row.status==2">
              <el-popconfirm
                      @confirm="publishStock(scope.row.id)"
                      style="margin-left:10px;"
                      confirmButtonText='是的'
                      cancelButtonText='不用了'
                      icon="el-icon-info"
                      iconColor="blue"
                      title="确定审核通过吗？"
              >
                  <template #reference>
              <el-button type="text" size="mini" icon="el-icon-s-operation">通过</el-button>
                  </template>
            </el-popconfirm>
              <el-button style="margin-left:10px;" type="text" size="mini" icon="el-icon-delete"
                         @click="del(scope.row.id)">删除</el-button>
                </span>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页 -->
            <el-pagination
                    style="margin-top:10px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[6, 10, 15, 20]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 物资添加弹出框 -->
            <el-dialog title="添加物资" v-model="addDialogVisible" width="50%" @close="closeAddDialog">
        <span>
          <el-form
                  size="mini"
                  :model="addRuleForm"
                  :rules="addRules"
                  ref="addRuleFormRef"
                  label-width="100px"
                  class="demo-ruleForm"
          >
            <el-form-item label="物资名称" prop="name">
              <el-input v-model="addRuleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="物资图片" prop="name">
              <!-- 图片上传部分 -->
              <el-upload
                      :action="uploadApi"
                      :class="{ disabled: uploadDisabled }"
                      list-type="picture-card"
                      :limit="limitCount"
                      :on-change="handleChange"
                      :on-remove="handleRemove"
                      accept="image/*"
                      :on-success="handleSuccess"
                      :headers="headerObject"
                      ref="upload1"
                      :on-preview="handlePictureCardPreview"
              >
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <div class="grid-content bg-purple-light">
                  <el-form-item label="物资规格" prop="model">
                    <el-input v-model="addRuleForm.model"></el-input>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-form-item label="分类" prop="categoryKeys">
                    <el-cascader
                            :options="cateories"
                            clearable
                            filterable
                            :props="selectProps"
                            v-model="addRuleForm.categoryKeys"
                    ></el-cascader>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-form-item label="单位" prop="unit">
                    <el-input v-model="addRuleForm.unit"></el-input>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="grid-content bg-purple-light">
                  <el-form-item label="排序" prop="sort">
                    <el-input-number v-model="addRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>

            <el-form-item label="备注信息" prop="remark">
              <el-input type="textarea" v-model="addRuleForm.remark"></el-input>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addStock" :disabled="btnDisabled" :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>

            <!-- 物资编辑弹出框 -->
            <el-dialog
                    title="更新物资"
                    v-model="editDialogVisible"
                    width="50%"
                    @close="closeEditDialog"
            >
        <span>
          <el-form
                  size="mini"
                  :model="editRuleForm"
                  :rules="addRules"
                  ref="editRuleFormRef"
                  label-width="100px"
                  class="demo-ruleForm"
          >
            <el-form-item label="物资名称" prop="name">
              <el-input v-model="editRuleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="物资图片" prop="name">
              <!-- 图片上传部分 -->
              <el-upload
                      :action="uploadApi"
                      :class="{ disabled: uploadDisabled }"
                      list-type="picture-card"
                      :limit="limitCount"
                      accept="image/*"
                      :on-success="editHandleSuccess"
                      :headers="headerObject"
                      ref="upload2"
                      :on-preview="handlePictureCardPreview"
                      :file-list="imgFilesList"
              >
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item label="物资规格" prop="model">
                  <el-input v-model="editRuleForm.model"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-form-item label="分类" prop="categoryKeys">
                    <el-cascader
                            :options="cateories"
                            clearable
                            filterable
                            :props="selectProps"
                            v-model="editRuleForm.categoryKeys"
                    ></el-cascader>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-form-item label="单位" prop="unit">
                    <el-input v-model="editRuleForm.unit"></el-input>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="grid-content bg-purple-light">
                  <el-form-item label="排序" prop="sort">
                    <el-input-number v-model="editRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button
                  type="primary"
                  @click="updateStock"
                  :disabled="btnDisabled"
                  :loading="btnLoading"
          >确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 图片预览 -->
            <el-dialog v-model="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt/>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {getToken} from '../../../../utils/auth'
    import {
        publish,
        deleteProduct,
        update,
        edit,
        add,
        remove,
        back,
        findProductList
    } from '../../../../api/business/material/product'
    import {categoryTree} from '../../../../api/business/material/productCategory'

    export default {

        setup() {

            const uploadApi = ref(import.meta.env.DEV ? '/api/system/upload/image' : import.meta.env.VITE_APP_BASE_API + 'system/upload/image')
            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const headerObject = reactive({
                Authorization: getToken()
            }) //图片上传请求头
            const cateories = ref([]) //类别选择
            const selectProps = reactive({
                expandTrigger: "hover",
                value: "id",
                label: "name",
                children: "children",
                checkStrictly: false
            }) //级联选择器配置项
            const searchSelectProps = reactive({
                expandTrigger: "hover",
                value: "id",
                label: "name",
                children: "children",
                checkStrictly: true
            }) //级联搜索选择器配置项


            const editDialogVisible = ref(false)
            const addDialogVisible = ref(false) //添加弹框是否显示
            const total = ref(0) //总共多少条数据
            const productData = ref([]) //表格数据
            const queryMap = ref({
                pageNum: 1,
                pageSize: 6,
                name: "",
                categoryId: "",
                supplier: "",
                status: 0,
            }) //查询对象
            const addRuleForm = ref({}) //添加表单数据
            const editRuleForm = ref({}) //修改表单数据
            const uploadDisabled = ref(false)
            const limitCount = ref(1)
            const dialogImageUrl = ref('')
            const dialogVisible = ref(false)
            const addRules = ref({
                name: [
                    {required: true, message: "请输入物资名称", trigger: "blur"},
                    {min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur"}
                ],
                unit: [
                    {required: true, message: "请输入物资单位", trigger: "blur"},
                    {min: 1, max: 10, message: "长度在 1 到 10 个字符", trigger: "blur"}
                ],
                model: [
                    {required: true, message: "请输入物资规格", trigger: "blur"},
                    {min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur"}
                ],
                remark: [
                    {required: true, message: "请输入物资说明备注", trigger: "blur"},
                    {min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur"}
                ],
                categorys: [
                    {required: true, message: "请输入物资分类", trigger: "blur"}
                ],
                sort: [{required: true, message: "请输入地址信息", trigger: "blur"}],
                categoryKeys: [
                    {required: true, message: "请选择物资分类", trigger: "blur"}
                ]
            }) //添加验证
            const imgFilesList = ref([])
            const categorykeys = ref([]) //搜索类别

            const addRuleFormRef = ref(null)
            const editRuleFormRef = ref(null)
            const upload1 = ref(null)
            const upload2 = ref(null)


            //重置查询表单
            const resetForm = () => {
                queryMap.value = {
                    pageNum: 1,
                    pageSize: 5,
                    name: "",
                    categoryId: "",
                    supplier: "",
                    status: 0,
                };
            }
            /**
             * 打开添加物资弹框
             */
            const openAdd = () => {
                getCatetorys();
                addDialogVisible.value = true;
            }
            /**
             * 搜索物资
             */
            const search = () => {
                queryMap.value.pageNum = 1;
                getproductList();
            }

            /**
             * 物资添加审核
             */
            const publishStock = (id) => {
                publish("/business/material/product/publish/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("审核失败:" + res.data.data.errorMsg);
                    } else {
                        getproductList();
                        return ElMessage.success("物资已审核通过");
                    }
                }).catch((res) => {
                    ElMessage.error("审核失败:" + res);
                });
            }
            /**
             * 删除物资
             */
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该物资, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteProduct("/business/material/product/delete/" + id).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("物资删除成功");
                                getproductList();
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
            /**
             * 更新物资
             */
            const updateStock = () => {
                editRuleFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnDisabled.value = true;
                        btnLoading.value = true;
                        update(
                            "/business/material/product/update/" + editRuleForm.value.id,
                            editRuleForm.value
                        ).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "物资信息更新",
                                    type: "success"
                                });
                                editRuleForm.value = {};
                                getproductList();
                            } else {
                                ElMessage.error("物资信息更新失败:" + res.data.data.errorMsg);
                            }
                            editDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                        }).catch((res) => {
                            editDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                            ElMessage.error("物资信息更新失败:" + res);
                        });
                    }
                });
            }
            /**
             * 编辑物资
             */
            const editStock = (id) => {
                edit("/business/material/product/edit/" + id).then((res) => {
                    if (res.data.success) {
                        editRuleForm.value = res.data.data;
                        const item = res.data.data;
                        imgFilesList.value.push({
                            url: import.meta.env.VITE_APP_BASE_API + item.imageUrl,
                            name: item.name,
                            id: item.id
                        });
                    } else {
                        return ElMessage.error("物资信息编辑失败" + res.data.data.errorMsg);
                    }
                    const array = [];
                    array.push(res.data.data.oneCategoryId);
                    array.push(res.data.data.twoCategoryId);
                    array.push(res.data.data.threeCategoryId);
                    editRuleForm.value.categoryKeys = array;
                    editDialogVisible.value = true;
                }).catch((res) => {
                    editDialogVisible.value = true;
                    ElMessage.error("物资信息编辑失败" + res);
                });
            }
            /**
             * 添加物资
             */
            const addStock = () => {
                addRuleFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnDisabled.value = true;
                        btnLoading.value = true;
                        add(addRuleForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("物资添加成功");
                                addRuleForm.value = {};
                                getproductList();
                            } else {
                                return ElMessage.error("物资添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                            ElMessage.error("物资添加失败" + res);
                        });
                    }
                });
            }
            /**
             * 移除回收站
             */
            const removeStock = (id) => {
                remove("/business/material/product/remove/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("移入回收站失败:" + res.data.data.errorMsg);
                    } else {
                        getproductList();
                        return ElMessage.success("移入回收站成功");
                    }
                }).catch((res) => {
                    ElMessage.error("移入回收站失败:" + res);
                });
            }
            /**
             * 从回收站恢复
             */
            const backStock = (id) => {
                back("/business/material/product/back/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("从回收站恢复失败:" + res.data.data.errorMsg);
                    } else {
                        getproductList();
                        return ElMessage.success("从回收站中已恢复");
                    }
                }).catch((res) => {
                    ElMessage.error("从回收站恢复失败:" + res);
                });
            }
            /**
             * 加载物资列表
             */
            const getproductList = () => {
                findProductList(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取物资列表失败");
                    } else {
                        res.data.data.rows.forEach((val) => {
                            val.imageUrlShow = import.meta.env.DEV ? '/api/' + val.imageUrl : import.meta.env.VITE_APP_BASE_API + val.imageUrl
                        })
                        total.value = res.data.data.total;
                        productData.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取物资列表失败");
                });
            }
            /**
             * 加载物资类别
             */
            const getCatetorys = () => {
                categoryTree().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取物资类别失败");
                    } else {
                        cateories.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取物资类别失败");
                });
            }
            const handleChange = (file, fileList) => {
                uploadDisabled.value = fileList.length >= limitCount.value;
            }
            const handleRemove = (file, fileList) => {
                uploadDisabled.value = fileList.length >= limitCount.value;
                addRuleForm.value.imageUrl = "";
            }
            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.value.pageSize = newSize;
                getproductList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                getproductList();
            }
            //关闭弹出框
            const closeAddDialog = () => {
                addRuleFormRef.value.clearValidate();
                addRuleForm.value = {};
                upload1.value.clearFiles();
            }
            //关闭弹出框
            const closeEditDialog = () => {
                editRuleFormRef.value.clearValidate();
                editRuleForm.value = {};
                upload2.value.clearFiles();
                imgFilesList.value = [];
            }
            const handleSuccess = (response, file, fileList) => {
                addRuleForm.value.imageUrl = response.msg;
            }
            const handlePictureCardPreview = (file) => {
                dialogImageUrl.value = file.url;
                dialogVisible.value = true;
            }
            //编辑
            const editHandleSuccess = (response, file, fileList) => {
                editRuleForm.value.imageUrl = response.msg;
            }
            /**
             * 分类搜索改变时
             */
            const selectChange = () => {
                let str = "";
                for (let i = 0; i < categorykeys.value.length; i++) {
                    str += categorykeys.value[i] + ",";
                }
                str = str.substring(0, str.length - 1);
                queryMap.value.categorys = str;
            }

            getproductList();
            getCatetorys();
            setTimeout(() => {
                loading.value = false;
            }, 500);

            return {
                uploadApi,
                btnLoading,
                btnDisabled,
                loading,
                headerObject,
                cateories,
                selectProps,
                searchSelectProps,
                editDialogVisible,
                addDialogVisible,
                total,
                productData,
                queryMap,
                addRuleForm,
                editRuleForm,
                uploadDisabled,
                limitCount,
                dialogImageUrl,
                dialogVisible,
                addRules,
                imgFilesList,
                categorykeys,
                addRuleFormRef,
                editRuleFormRef,
                upload1,
                upload2,
                resetForm,
                openAdd,
                search,
                publishStock,
                del,
                updateStock,
                editStock,
                addStock,
                removeStock,
                backStock,
                getCatetorys,
                getproductList,
                handleCurrentChange,
                handleSizeChange,
                handleChange,
                handleRemove,
                closeAddDialog,
                closeEditDialog,
                handleSuccess,
                handlePictureCardPreview,
                editHandleSuccess,
                selectChange,
            };
        },
    };
</script>


