<template>
    <div id="publishStocks">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/business/material/product/out-stocks' }">出库记录</el-breadcrumb-item>
            <el-breadcrumb-item>发放物资</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 卡片区域 -->

        <el-row :gutter="20">
            <!--右边出库表单-->
            <el-col :span="11">
                <el-card class="box-card">
                    <div class="grid-content bg-purple">
<!--                        出库步骤条-->
                        <el-steps style="margin-bottom: 20px;" :active="1" >
                            <el-step title="填写" icon="el-icon-edit"></el-step>
                            <el-step title="审核" icon="el-icon-postcard"></el-step>
                            <el-step title="发放" icon="el-icon-set-up"></el-step>
                        </el-steps>
<!--                        发放表单-->

                        <el-form
                                size="mini"
                                ref="addRuleFormRef"
                                :rules="addRules"
                                :model="addRuleForm"
                                label-width="80px"
                        >
                            <el-form-item label="添加方式" >
                                <el-col :span="24"><div class="grid-content bg-purple">
                                    <el-radio border size="mini"  @change="existenceChange(existence)" v-model="existence"  :label="0">新增去向</el-radio>
                                    <el-radio border size="mini" @change="existenceChange(existence)"  v-model="existence" :label="1">已知去向</el-radio>
                                </div></el-col>
                            </el-form-item>

                            <el-form-item v-if="existence==1" label="物资去向" prop="consumerId">

                                <el-col :span="12">
                                    <div class="grid-content bg-purple">
                                        <el-select
                                                v-if="existence==1"
                                                style="width:100%;"
                                                filterable
                                                @change="consumerSelectChange(addRuleForm.consumerId)"
                                                v-model="addRuleForm.consumerId"
                                                placeholder="选择已存在去向"
                                        >
                                            <el-option
                                                    v-for="item in consumers"
                                                    :key="item.id"
                                                    :label="item.name"
                                                    :value="item.id"
                                            ></el-option>
                                        </el-select>
                                    </div>
                                </el-col>
                            </el-form-item>
                            <el-form-item label="优先级" prop="type">
                                <el-radio-group v-model="addRuleForm.priority">
                                    <el-radio  :size="'mini'" :label="1">不急</el-radio>
                                    <el-radio  :size="'mini'" :label="2">常规</el-radio>
                                    <el-radio  :size="'mini'" :label="3">紧急</el-radio>
                                    <el-radio  :size="'mini'" :label="4">特急</el-radio>
                                    <el-radio  :size="'mini'" :label="5">超急</el-radio>
                                </el-radio-group>
                            </el-form-item>

                            <el-form-item label="领取类型" prop="type">
                                <el-radio-group v-model="addRuleForm.type">
                                    <el-radio  :size="'mini'" :label="0">政府</el-radio>
                                    <el-radio  :size="'mini'" :label="1">医院</el-radio>
                                    <el-radio  :size="'mini'" :label="2">小区</el-radio>
                                    <el-radio  :size="'mini'" :label="3">个人</el-radio>
                                    <el-radio  :size="'mini'" :label="4">其他</el-radio>
                                </el-radio-group>
                            </el-form-item>

                            <div v-if="existence==0">
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
                                <el-form-item label="具体去向" prop="name">
                                    <el-input placeholder="请输入具体去向" v-model="addRuleForm.name"></el-input>
                                </el-form-item>
                                <el-form-item label="联系人" prop="contact">
                                    <el-input placeholder="请输入联系人名称" v-model="addRuleForm.contact"></el-input>
                                </el-form-item>

                                <el-form-item label="电话" prop="phone">
                                    <el-input placeholder="请输入电话号码" v-model="addRuleForm.phone"></el-input>
                                </el-form-item>
                                <el-form-item label="排序" prop="sort">
                                    <el-input-number v-model="addRuleForm.sort" :min="1" :max="10" label="排序"></el-input-number>
                                </el-form-item>
                            </div>

                            <div v-if="existence==1">
                                <el-card class="box-card" style="margin-bottom: 30px;">
                                    <div  class="text item" style="font-size: 12px;padding: 5px;">
                                        物资名称: <span style="margin-left: 200px;color: #999">
                    <el-tag size="mini" type="info">{{consumerInfo.name}}</el-tag>
                  </span>
                                    </div>
                                    <div  class="text item" style="font-size: 12px;padding: 5px;">
                                        物资地址: <span style="margin-left: 200px;color: #999">
                    <el-tag size="mini" type="info">{{consumerInfo.address}}
                    </el-tag></span>
                                    </div>
                                    <div  class="text item" style="font-size: 12px;padding: 5px;">
                                        联系方式: <span style="margin-left: 200px;color: #999">
                    <el-tag size="mini" type="info">{{consumerInfo.phone}}
                    </el-tag></span>
                                    </div>

                                    <div  class="text item" style="font-size: 12px;padding: 5px;">
                                        联系人员:  <span style="margin-left: 200px;color: #999">
                    <el-tag size="mini" type="info">{{consumerInfo.contact}}
                    </el-tag></span>
                                    </div>
                                </el-card>
                            </div>
                            <el-row>
                                <el-col :span="12">
                                    <div class="grid-content bg-purple">
                                        <el-form-item label="物资明细">
                                            <el-button size="mini"  icon="el-icon-search" @click="drawer = true">发放明细</el-button>
                                        </el-form-item>
                                    </div>
                                </el-col>
                                <el-col :span="12">
                                    <div class="grid-content bg-purple"><el-form-item label="物资总类">
                                        <el-input-number v-model="itemNum" :disabled="true"></el-input-number>
                                    </el-form-item>
                                    </div>
                                </el-col>
                            </el-row>
                            <el-form-item label="描述信息" prop="remark">
                                <el-input type="textarea" :rows="2" v-model="addRuleForm.remark"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="add">立即创建</el-button>

                                <el-button >重置</el-button>
                            </el-form-item>
                        </el-form>



                    </div>
                </el-card>
            </el-col>
            <!--                物资库存表格-->

            <el-col :span="13">
                <div class="grid-content bg-purple-light">
                    <el-card>
                        <!-- 查询搜索 -->
                        <el-form  :inline="true" :model="queryMap" class="demo-form-inline">
                            <el-form-item label="分类">
                                <el-cascader
                                        @change="selectChange"
                                        v-model="categorykeys"
                                        :props="selectProps"
                                        :options="catetorys"
                                        clearable
                                ></el-cascader>
                            </el-form-item>
                            <el-form-item>
                                <el-form-item label="名称">
                                    <el-input clearable v-model="queryMap.name" placeholder="名称"
                                              @clear="search"></el-input>
                                </el-form-item>
                                <el-button type="primary" @click="search">查询</el-button>
                            </el-form-item>
                        </el-form>
                        <el-alert
                                title="勾选先下方物资后,在左侧表单的明细中添加其发放数量"
                                type="warning"
                                show-icon style="margin-bottom:20px;">
                        </el-alert>
                        <!-- 发放预选表格 -->
                        <el-table
                                style="height:420px;"
                                :data="tableData"
                                border
                                :row-key="getRowKey"
                                ref="dataTable"
                                @selection-change="handleSelectionChange"
                        >
                            <el-table-column type="selection" :reserve-selection="true"></el-table-column>
                            <el-table-column prop="imageUrl" label="图片" align="center" width="100px" padding="0px">
                                <!--            <template #default="scope">-->
                                <!--              <img-->
                                <!--                slot="error"-->
                                <!--                :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"-->
                                <!--                alt-->
                                <!--                style="width: 55px;height:55px"-->
                                <!--              />-->
                                <!--            </template>-->
                                <template #default="scope">
                                    <el-popover placement="right" trigger="hover">
                                        <img :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"
                                             style="height: 200px;width: 200px"/>
                                        <template #reference>
                                        <img :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"
                                             :alt="scope.row.imgUrl" style="height: 32px;width: 32px;cursor: pointer">
                                        </template>
                                    </el-popover>
                                </template>
                            </el-table-column>
                            <el-table-column prop="name" :show-overflow-tooltip='true' label="名称"></el-table-column>
                            <el-table-column prop="pnum" label="商品编号" :show-overflow-tooltip='true'></el-table-column>

                            <el-table-column prop="model" label="型号"></el-table-column>
                            <el-table-column prop="stock" label="库存">
                                <template #default="scope">
                                <el-tag type="primary" effect="plain"  v-text="scope.row.stock">
                                </el-tag>
                                </template>
                            </el-table-column>
                            <el-table-column prop="unit" label="单位">
                            </el-table-column>

                        </el-table>
                        <!-- 表格分页 -->
                        <el-pagination
                                background
                                style="margin-top:10px;"
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="queryMap.pageNum"
                                :page-sizes="[6, 8, 16, 20]"
                                :page-size="queryMap.pageSize"
                                layout="total, sizes, prev, pager, next"
                                :total="total"
                        ></el-pagination>

                        <!-- 抽屉 -->
                        <el-drawer size="49%" title="发放明细" v-model="drawer" :with-header="false">
              <span>
     <el-button size="mini" style="float: right;margin: 10px;"  type="primary" icon="el-icon-remove" :disabled="products.length==0" @click="removeAllItem" >
                      清空
                  </el-button>
                <el-table height="650" :data="products" border>
                  <el-table-column prop="name" label="名称" width="120px;"></el-table-column>
                  <el-table-column
                          prop="imageUrl"
                          label="图片"
                          align="center"
                          width="150px"
                          padding="0px"
                  >
                    <template #default="scope">
                      <img
                              :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"
                              alt
                              style="width: 50px;height:30px"
                      />
                    </template>
                  </el-table-column>
                  <el-table-column prop="model" label="型号" width="100px;"></el-table-column>

                  <el-table-column label="数量" width="160">
                    <template #default="scope">
                      <el-input-number
                              @change="outStockNumberChange(scope.row.number,scope.row.stock)"
                              size="mini"
                              v-model="scope.row.number"
                              :min="1"
                              :max="scope.row.stock"
                              label="描述文字"
                      ></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column prop="unit" label="单位" width="80px;"></el-table-column>
                  <el-table-column label="操作" width="100px;" fixed="right">
                    <template #default="scope">
                      <el-button
                              type="danger"
                              size="mini"
                              plain
                              icon="el-icon-remove"
                              @click="removeItem(scope.row.pnum)"
                      >移除</el-button>
                    </template>
                  </el-table-column>

                </el-table>

              </span>
                        </el-drawer>
                    </el-card>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {useRouter, useRoute} from "vue-router";
    import {getProvinces} from '../../../../api/business/material/businessUtils'
    import {categoryTree} from '../../../../api/business/material/productCategory'
    import {findProductStocks} from '../../../../api/business/material/product'
    import {addOutStock} from '../../../../api/business/material/outStock'
    import {findAll} from '../../../../api/business/material/consumer'

    export default {

        setup(){
            const router = useRouter();
            const {ctx} = getCurrentInstance();

            const products = ref([])    //product of car
            const consumers = ref([])
            const itemNum = ref(0)
            const consumerInfo = ref({name:"选择后显示具体信息",address:"选择后显示地址信息",contact:"选择后显示联系人信息", phone:"选择后显示联系方式"})  //卡片展示
            const provinceList = ref([]) // 省列表
            let cityList = [] // 市列表
            let originList = [] // 区列表
            const cityOptions = ref([]) // 市下拉框数据
            const originOptions = ref([]) // 区下拉框数据,
            const existence = ref(0)
            const total = ref(0)
            const drawer = ref(false)
            const tableData = ref([])
            const catetorys = ref([])
            const categorykeys = ref([])
            const queryMap = ref({
                pageNum: 1,
                    pageSize: 6,
            })
            const selectProps = reactive({
                expandTrigger: "hover",
                    value: "id",
                    label: "name",
                    children: "children"
            }) //级联选择器配置项
            const addRuleForm = ref({priority:1})  //出库表单
            const addRules = reactive({
                consumerId: [
                    { required: true, message: "请选中物资去向", trigger: "blur" }
                ],
                    type: [
                    { required: true, message: "请选中领取类型", trigger: "blur" }
                ],
                    priority: [{ required: true, message: "请选中发放类型", trigger: "blur" }],
                    remark: [
                    { required: true, message: "请输入商品说明备注", trigger: "blur" },
                    { min: 5, max: 20, message: "长度在 5 到 20 个字符", trigger: "blur" }
                ],
                    name: [
                    { required: true, message: "请输入去向名称", trigger: "blur" },
                    { min: 2, max: 50, message: "长度在 2 到 50 个字符", trigger: "blur" }
                ],
                    address: [
                    { required: true, message: "请输入地址信息", trigger: "blur" },
                    { min: 2, max: 12, message: "长度在 2 到 12 个字符", trigger: "blur" }
                ],

                    valueProvince: [
                    { required: true, message: "请输入省份", trigger: "blur" }
                ],
                    valueCity: [{ required: true, message: "请输入城市", trigger: "blur" }],
                    valueOrigin: [
                    { required: true, message: "请输入区县", trigger: "blur" }
                ],
                    sort: [
                    { required: true, message: "请输入排序号", trigger: "blur" }
                ],
                    contact: [{ required: true, message: "请输入联系人", trigger: "blur" }],
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

            const addRuleFormRef = ref(null)
            const addDialogVisible = ref(null)

            const dataTable = ref(null)

            /**
             * 分类搜索改变时
             */
            const selectChange = () => {
                let str = "";
                if(categorykeys.value != null){
                    for (let i = 0; i < categorykeys.value.length; i++) {
                        str += categorykeys.value[i] + ",";
                    }
                    str = str.substring(0, str.length - 1);
                }
                queryMap.value.categorys = str;
            }
            /**
             * 加载商品类别
             */
            const getCatetorys = () => {
                categoryTree().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取商品类别失败:"+res.data.data.errorMsg);
                    } else {
                        catetorys.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取商品类别失败:"+res);
                });
            }
            /**
             * 加载商品列表(可出库)
             */
            const loadTableData = () => {
                findProductStocks(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取商品列表失败:"+res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        tableData.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取商品列表失败:"+res);
                });
            }
            /**
             * 改变页码
             */
            const handleSizeChange = (newSize) => {
                queryMap.value.pageSize = newSize;
                loadTableData();
            }
            /**
             * 点击分页
             */
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                loadTableData();
            }
            /**
             * 搜索
             */
            const search = () => {
                queryMap.value.pageNum = 1;
                loadTableData();
            }

            // 指定一个key标识这一行的数据
            const getRowKey = (row) => {
                return row.pnum
            }

            // val表示选中的表格行数据
            const handleSelectionChange = (val) => {
                products.value = val;
                itemNum.value = val.length;
            }

            /**
             * 改变去向
             */
            const existenceChange = (existence) => {
                //选择已经存在的去向
                if(existence===1){
                }else if(existence===0){
                    addRuleForm.value.consumerId='';
                }
                addRuleFormRef.value.clearValidate();
                consumerInfo.value={name:"选择后显示具体信息",address:"选择后显示地址信息",contact:"选择后显示联系人信息", phone:"选择后显示联系方式"};
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

            //改变来源选择
            const consumerSelectChange = (consumerId) => {
                let obj=consumers.value.find((x) => {
                    return x.id === consumerId;
                });
                consumerInfo.value=obj;
            }

            //加载去向数据
            const getConsumers = () => {
                findAll().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取去向数据失败:"+res.data.data.errorMsg);
                    } else {
                        consumers.value = res.data.data;
                    }
                }).catch((res) => {
                    ElMessage.error("获取去向数据失败:"+res);
                });
            }
            /**
             * 移除item从抽屉中
             */
            const removeItem = (val) => {
                products.value.forEach(row => {
                    if (row.pnum === val) {
                        dataTable.value.toggleRowSelection(row, false);
                    }
                });
                products.value = products.value.filter(item => item.pnum !== val);
            }
            /**
             * 清空所有
             */
            const removeAllItem = () => {
                products.value.forEach(row => {
                    dataTable.value.toggleRowSelection(row, false);
                });
                products.value=[];
            }
            const outStockNumberChange = (currentValue, stock) => {
                if(currentValue===stock){
                    ElMessage.warning("已达到可申请发放该物资数量的阈值");
                }
            }
            /**
             * 创建发放单
             */
            //添加
            const add = () => {
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
                        let car = [];
                        products.value.forEach(row => {
                            if (row.number !== undefined) {
                                let item = { productId: row.id, productNumber: row.number };
                                car.push(item);
                            }
                        });
                        if (car.length > 0) {
                            addRuleForm.value.products = car;
                        } else {
                            return ElMessage.warning(
                                "发放商品的数量不能为空，请勾选物资后在明细中添加数量"
                            );
                        }
                        ElMessageBox.confirm("此操作将提交发放单, 是否继续?", "提示", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then((res) => {
                            if (res === "confirm") {
                                addOutStock(addRuleForm.value).then((res) => {
                                    if (res.data.success) {
                                        ElMessage.warning("物资发放进入审核状态");
                                        router.push("/business/material/product/out-stocks");
                                    } else {
                                        return ElMessage.error("商品发放失败:" + res.data.data.errorMsg);
                                    }
                                }).catch((res) => {
                                    ElMessage.warning("物资发放进入审核状态");
                                });
                            }
                            addDialogVisible.value = false;
                        }).catch(() => {
                            if(addDialogVisible.value != null && addDialogVisible.value != undefined){
                                addDialogVisible.value = false;
                            }
                            ElMessage({
                                type: "info",
                                message: "已取消删除"
                            });
                        });
                    }
                });
            }

            getConsumers()
            getCatetorys();
            loadTableData();
            getJsonData();

            return {

                products,
                consumers,
                itemNum,
                consumerInfo,
                provinceList,
                cityList,
                originList,
                cityOptions,
                originOptions,
                existence,
                total,
                drawer,
                tableData,
                catetorys,
                categorykeys,
                queryMap,
                selectProps,
                addRuleForm,
                addRules,
                addRuleFormRef,
                addDialogVisible,
                dataTable,
                selectChange,
                getCatetorys,
                loadTableData,
                handleSizeChange,
                handleCurrentChange,
                search,
                getRowKey,
                handleSelectionChange,
                existenceChange,
                changeProvince,
                changeCity,
                changeOrigin,
                getJsonData,
                consumerSelectChange,
                getConsumers,
                removeItem,
                removeAllItem,
                outStockNumberChange,
                add,
            }
        },
    }
</script>


