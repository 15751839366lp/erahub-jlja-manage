<template>
    <div id="productCategroys">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资流向</el-breadcrumb-item>
            <el-breadcrumb-item>物资来源</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 右侧卡片区域 -->
        <!-- 用户列表卡片区 -->
        <el-card class="box-card">
            <el-form size="mini" :inline="true" :model="queryMap" class="demo-form-inline">
                <el-form-item label="省市区县">
                    <el-input v-model="queryMap.address" clearable @clear="search" placeholder="省市区县"></el-input>
                </el-form-item>
                <el-form-item label="联系人">
                    <el-input v-model="queryMap.contact" clearable @clear="search" placeholder="联系人"></el-input>
                </el-form-item>
                <el-form-item label="具体地点">
                    <el-input
                            clearable
                            v-model="queryMap.name"
                            placeholder="请具体地点查询"
                            @clear="search"
                            class="input-with-el-select"
                    ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button icon="el-icon-search" @click="search" type="primary"> 查询</el-button>
                    <el-button
                            v-hasPermission="'supplier:add'"
                            type="success"
                            icon="el-icon-circle-plus-outline"
                            @click="addDialogVisible=true"
                    >添加
                    </el-button>
                </el-form-item>
            </el-form>


            <!-- 表格区域 -->
                <el-table
                        border
                        size="mini"
                        v-loading="loading"
                        stripe
                        :data="supplierData"
                        style="width: 100%;"
                        height="460"
                >
                    <el-table-column prop="id" type="index" label="ID" width="50"></el-table-column>

                    <el-table-column label="物资提供方地址">
                        <el-table-column
                                prop="address"
                                label="省份"
                                width="130">
                            <template #default="scope">
                                <span v-text="scope.row.address.split('/')[0]"></span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="address"
                                label="市"
                                width="100">
                            <template #default="scope">
                                <span v-text="scope.row.address.split('/')[1]"></span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="address"
                                label="区县"
                                width="100">
                            <template #default="scope">
                                <span v-text="scope.row.address.split('/')[2]"></span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="地址"
                                width="180">
                        </el-table-column>
                    </el-table-column>

                    <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
                    <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
                    <el-table-column prop="contact" label="联系人" width="120"></el-table-column>
                    <el-table-column prop="phone" label="电话" width="120"></el-table-column>
                    <el-table-column prop="sort" label="排序" width="80"></el-table-column>
                    <el-table-column label="操作" fixed="right" width="180">
                        <template #default="scope">
                            <el-button
                                    v-hasPermission="'supplier:edit'"
                                    type="text"
                                    size="mini"
                                    icon="el-icon-edit"
                                    @click="editSupplier(scope.row.id)"
                            >编辑
                            </el-button>

                            <el-button
                                    v-hasPermission="'supplier:delete'"
                                    type="text"
                                    size="mini"
                                    icon="el-icon-delete"
                                    @click="del(scope.row.id)"
                            >删除
                            </el-button>
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
                    :page-sizes="[ 10, 15, 20]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 来源添加弹出框 -->
            <el-dialog title="添加来源" v-model="addDialogVisible" width="50%" @close="closeAddDialog">
        <span>
          <el-form
                  :model="addRuleForm"
                  :rules="addRules"
                  ref="addRuleFormRef"
                  label-width="100px"
                  class="demo-ruleForm"
          >
           <el-row>
              <el-col :span="8">
                <div class="grid-content bg-purple"></div>
                <el-form-item label="省份" prop="valueProvince">
                  <el-select
                          v-model="addRuleForm.valueProvince"
                          placeholder="请选择省"
                          @change="changeProvince"
                  >
                    <el-option
                            v-for="item in provinceList"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-form-item label="城市" prop="valueCity">
                    <el-select
                            v-model="addRuleForm.valueCity"
                            placeholder="请选择市"
                            @change="changeCity"
                    >
                      <el-option
                              v-for="item in cityOptions"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple">
                  <el-form-item label="区县" prop="valueOrigin">
                    <el-select
                            v-model="addRuleForm.valueOrigin"
                            placeholder="请选择区"
                            @change="changeOrigin"
                    >
                      <el-option
                              v-for="item in originOptions"
                              :key="item.label"
                              :label="item.label"
                              :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <el-form-item label="来源 名称" prop="name">
              <el-input v-model="addRuleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="联系人" prop="contact">
              <el-input v-model="addRuleForm.contact"></el-input>
            </el-form-item>


            <el-form-item label="邮箱" prop="email">
              <el-input v-model="addRuleForm.email"></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model="addRuleForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="addRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addSupplier">确 定</el-button>
        </span>
                </template>
            </el-dialog>

            <!-- 系别编辑弹出框 -->
            <el-dialog
                    title="更新来源"
                    v-model="editDialogVisible"
                    width="50%"
                    @close="closeEditDialog"
            >
        <span>
          <el-form
                  :model="editRuleForm"
                  :rules="addRules"
                  ref="editRuleFormRef"
                  label-width="100px"
                  class="demo-ruleForm"
          >
            <el-form-item label="省市区县" prop="address">
              <el-input disabled v-model="editRuleForm.address"></el-input>
            </el-form-item>

            <el-form-item label="来源详细地" prop="name">
              <el-input type="textarea" v-model="editRuleForm.name"></el-input>
            </el-form-item>
             <el-form-item label="联系人" prop="contact">
              <el-input v-model="editRuleForm.contact"></el-input>
            </el-form-item>


            <el-form-item label="邮箱" prop="email">
              <el-input v-model="editRuleForm.email"></el-input>
            </el-form-item>

            <el-form-item label="电话" prop="phone">
              <el-input v-model="editRuleForm.phone"></el-input>
            </el-form-item>

            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="editRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateSupplier">确 定</el-button>
        </span>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {getProvinces} from '../../../../api/business/material/businessUtils'
    import {deleteSupplier, update, edit, add, findSupplierList} from '../../../../api/business/material/supplier'

    export default {

        setup() {
            const {ctx} = getCurrentInstance();

            const loading = ref(true)
            const editDialogVisible = ref(false)
            const addDialogVisible = ref(false) //添加弹框是否显示
            const total = ref(0) //总共多少条数据
            const supplierData = ref([]) //表格数据
            const queryMap = ref({pageNum: 1, pageSize: 10, name: ""}) //查询对象
            const addRuleForm = ref({}) //添加表单数据
            const editRuleForm = ref({}) //修改表单数据
            const deans = ref([]) //所有系主任
            const addRules = reactive({
                name: [
                    {required: true, message: "请输入来源名称", trigger: "blur"},
                    {min: 2, max: 50, message: "长度在 2 到 50 个字符", trigger: "blur"}
                ],
                address: [
                    {required: true, message: "请输入地址信息", trigger: "blur"},
                    {min: 2, max: 12, message: "长度在 2 到 12 个字符", trigger: "blur"}
                ],
                email: [
                            {
                                required: true,
                                validator: (rule, value, callback) => {
                                    const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                                    if (!value) {
                                        return callback(new Error("邮箱不能为空"));
                                    }
                                    setTimeout(() => {
                                        if (mailReg.test(value)) {
                                            callback();
                                        } else {
                                            callback(new Error("请输入正确的邮箱格式"));
                                        }
                                    }, 100);
                                },
                                trigger: "blur"
                            }
                        ],
                valueProvince: [
                    {required: true, message: "请输入省份", trigger: "blur"}
                ],
                valueCity: [{required: true, message: "请输入城市", trigger: "blur"}],
                valueOrigin: [
                    {required: true, message: "请输入区县", trigger: "blur"}
                ],
                sort: [
                    {required: true, message: "请输入排序号", trigger: "blur"}
                ],
                contact: [{required: true, message: "请输入联系人", trigger: "blur"}],
                phone: [
                    {
                        required: true,
                        message: "请输入联系方式",
                        validator: (rule, value, callback) => {
                            const phoneReg = /^1[34578]\d{9}$$/;
                            if (!value) {
                                return callback(new Error("电话号码不能为空"));
                            }
                            setTimeout(() => {
                                // Number.isInteger是es6验证数字是否为整数的方法,实际输入的数字总是识别成字符串
                                // 所以在前面加了一个+实现隐式转换

                                if (!Number.isInteger(+value)) {
                                    callback(new Error("请输入数字值"));
                                } else {
                                    if (phoneReg.test(value)) {
                                        callback();
                                    } else {
                                        callback(new Error("电话号码格式不正确"));
                                    }
                                }
                            }, 100);
                        },
                        trigger: "blur"
                    }
                ]
            }) //添加验证
            const provinceList = ref([]) // 省列表
            let cityList = [] // 市列表
            let originList = [] // 区列表
            const cityOptions = ref([]) // 市下拉框数据
            const originOptions = ref([]) // 区下拉框数据
            const addRuleFormRef = ref(null)
            const editRuleFormRef = ref(null)

            //搜索
            const search = () => {
                queryMap.value.pageNum = 1;
                getSupplierList();
            }
            //删除来源
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该用户, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteSupplier("/business/material/supplier/delete/" + id).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("来源删除成功");
                                getSupplierList();
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
            //更新用户
            const updateSupplier = () => {
                editRuleFormRef.value.validate( valid => {
                    if (!valid) {
                        return;
                    } else {
                        update(
                            "business/material/supplier/update/" + editRuleForm.value.id,
                            editRuleForm.value
                        ).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "来源信息更新",
                                    type: "success"
                                });
                                editRuleForm.value = {};
                                getSupplierList();
                            } else {
                                ElMessage.error("来源信息更新失败:" + res.data.data.errorMsg);
                            }

                            editDialogVisible.value = false;
                        }).catch((res) => {
                            editDialogVisible.value = false;
                            ElMessage.error("来源信息更新失败:" + res);
                        });
                    }
                });
            }
            //编辑
            const editSupplier = (id) => {
                edit("business/material/supplier/edit/" + id).then((res) => {
                    if (res.data.success) {
                        editRuleForm.value = res.data.data;
                    } else {
                        return ElMessage.error("来源信息编辑失败" + res.data.data.errorMsg);
                    }
                    editDialogVisible.value = true;
                }).catch((res) => {
                    ElMessage.error("来源信息编辑失败" + res);
                });
            }
            //添加
            const addSupplier = () => {
                addRuleFormRef.value.validate( valid => {
                    if (!valid) {
                        return;
                    } else {
                        addRuleForm.value.address =
                            addRuleForm.value.province +
                            "/" +
                            addRuleForm.value.city +
                            "/" +
                            addRuleForm.value.origin;
                        add(addRuleForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("来源添加成功");
                                addRuleForm.value = {};
                                getSupplierList();
                            } else {
                                return ElMessage.error("来源添加失败:" + res.data.data.errorMsg);
                            }
                            addDialogVisible.value = false;
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            ElMessage.error("来源添加失败:" + res);
                        });
                    }
                });
            }
            //加载系别列表
            const getSupplierList = () => {
                findSupplierList(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        loading.value = false;
                        return ElMessage.error("获取用户列表失败:" + res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        supplierData.value = res.data.data.rows;
                        loading.value = false;
                    }
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("获取用户列表失败:" + res);
                });
            }

            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.value.pageSize = newSize;
                getSupplierList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                getSupplierList();
            }
            //关闭弹出框
            const closeAddDialog = () => {
                addRuleFormRef.value.clearValidate();
                addRuleForm.value = {};
            }
            //关闭弹出框
            const closeEditDialog = () => {
                editRuleFormRef.value.clearValidate();
                editRuleForm.value = {};
            }
            // 选择省
            const changeProvince = (val) => {
                provinceList.value.forEach((province, index) => {
                    if (val.toString() === provinceList.value[index].value) {
                        cityOptions.value = provinceList.value[index].children;
                        addRuleForm.value.valueCity = provinceList.value[index].children[0].value; //设置市的值
                        addRuleForm.value.city = provinceList.value[index].children[0].label;

                        addRuleForm.value.valueOrigin = provinceList.value[index].children[0].children[0].value; //设置县的值
                        addRuleForm.value.origin = provinceList.value[index].children[0].children[0].label;

                        originOptions.value = provinceList.value[index].children[0].children;
                        //set value
                        addRuleForm.value.province = provinceList.value[index].label;
                    }
                });
            }
            // 选择市
            const changeCity = (val) => {
                cityList.forEach((city, index) => {
                    if (val.toString() === cityList[index].value) {
                        originOptions.value = cityList[index].children;
                        addRuleForm.value.valueOrigin = cityList[index].children[0].value; //设置县的值;
                        //set value
                        addRuleForm.value.city = cityList[index].label;
                    }
                });
            }
            // 选择区
            const changeOrigin = (val) => {
                addRuleForm.value.valueOrigin = val;

                originList.forEach((origin, index) => {
                    if (val.toString() === originList[index].value) {
                        //set value
                        addRuleForm.value.origin = originList[index].label;
                    }
                });
                //添加this.$forceUpdate();进行强制渲染，效果实现。搜索资料得出结果：因为数据层次太多，render函数没有自动更新，需手动强制刷新。
                ctx.$forceUpdate();
            }
            const getJsonData = () => {
                getProvinces().then(res => {
                    provinceList.value = [];
                    cityList = [];
                    originList = [];
                    res.data.forEach(item => {
                        if (item.value.match(/0000$/)) {
                            provinceList.value.push({
                                value: item.value,
                                label: item.name,
                                children: []
                            });
                        } else if (item.value.match(/00$/)) {
                            cityList.push({
                                value: item.value,
                                label: item.name,
                                children: []
                            });
                        } else {
                            originList.push({
                                value: item.value,
                                label: item.name
                            });
                        }
                    });
                    for (let index in provinceList.value) {
                        for (let index1 in cityList) {
                            if (
                                provinceList.value[index].value.slice(0, 2) ===
                                cityList[index1].value.slice(0, 2)
                            ) {
                                provinceList.value[index].children.push(cityList[index1]);
                            }
                        }
                    }
                    for (let item1 in cityList) {
                        for (let item2 in originList) {
                            if (
                                originList[item2].value.slice(0, 4) ===
                                cityList[item1].value.slice(0, 4)
                            ) {
                                cityList[item1].children.push(originList[item2]);
                            }
                        }
                    }
                });
            }

            getSupplierList();
            getJsonData();

            return {
                loading,
                editDialogVisible,
                addDialogVisible,
                total,
                supplierData,
                queryMap,
                addRuleForm,
                editRuleForm,
                deans,
                addRules,
                provinceList,
                cityList,
                originList,
                cityOptions,
                originOptions,
                addRuleFormRef,
                editRuleFormRef,
                search,
                del,
                updateSupplier,
                editSupplier,
                addSupplier,
                getSupplierList,
                handleSizeChange,
                handleCurrentChange,
                closeAddDialog,
                closeEditDialog,
                changeProvince,
                changeCity,
                changeOrigin,
                getJsonData,
            };
        },
    };
</script>
