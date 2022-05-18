<template>
    <div class="health">
        <!--       面包屑导航-->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>健康报备</el-breadcrumb-item>
            <el-breadcrumb-item>每日打卡</el-breadcrumb-item>
        </el-breadcrumb>
        <!--       打卡部分 style="height: 590px;"-->
        <el-card style="height: 590px;">
            <el-alert
                    :closable="false"
                    style="margin-bottom: 20px;"
                    v-if="reported"
                    show-icon
                    :title="'今日已成功打卡,勤洗手,戴口罩,不群聚,打卡时间:['+ruleForm.createTime+']'"
                    type="success">
            </el-alert>
            <h5 style="margin: 0px;padding: 0px;">基本信息
                <div style="float: right;margin-bottom: 10px;vertical-align: center;">
                    <el-button @click="OpenHistory" size="small" type="warning">签到记录</el-button>
                    <el-button style="margin-top: 5px;" type="primary" size="small" @click="onSubmit" v-if="!reported">
                        立即打卡
                    </el-button>
                    <el-button @click="reset" size="small" v-if="!reported">重置</el-button>
                </div>
            </h5>

            <!--            抽屉-->
            <el-drawer
                    :with-header="false"
                    v-model="drawer"
                    :direction="direction"
            >
                <span>
                    <el-card class="box-card" v-for="item in this.historyTable" :key="item.id"
                             style="margin-bottom: 10px;">
                      <div class="text item">
                         <ul>
                             <li>打卡人:{{userInfo.username}}</li>
                             <li>打卡位置:{{item.address}}</li>
                             <li>打卡时间:{{item.createTime}}</li>
                             <li>健康状况:
                                 <div style="float: right">
                                       <el-tag v-if="item.situation===0" size="small" effect="dark"
                                               type="success">健康</el-tag>
                                <el-tag v-else-if="item.situation===1" size="small" effect="dark"
                                        type="danger">发热</el-tag>
                                <el-tag v-else-if="item.situation===2" size="small" effect="dark"
                                        type="warning">其他情况</el-tag>
                                 </div>
                             </li>
                         </ul>
                      </div>
                        <!--                        分页-->

                    </el-card>
                     <el-pagination
                             @current-change="handleCurrentChange"
                             :current-page="queryMap.pageNo"
                             :page-size="queryMap.pageSize"
                             style="margin-top: 20px;"
                             background
                             small
                             layout="prev, pager, next"
                             :total="historyTotal">
                        </el-pagination>
                </span>
            </el-drawer>

            <el-divider></el-divider>
            <el-form size="small" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="打卡人">
                                <el-input v-model="userInfo.username" disabled></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <div class="grid-content bg-purple">
                                <el-form-item label="打卡时间">
                                    <el-date-picker
                                            disabled
                                            v-model="nowTime"
                                            type="datetime"
                                            placeholder="选择日期时间">
                                    </el-date-picker>

                                </el-form-item>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <div class="grid-content bg-purple">
                                <el-form-item label="所属部门">
                                    <el-input v-model="userInfo.department" disabled></el-input>
                                </el-form-item>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </el-form>

            <h5 style="margin: 0px;padding: 0px;">健康打卡</h5>

            <el-divider></el-divider>
            <el-form size="small" :inline="true" :label-position="'top'" :model="ruleForm" :rules="rules"
                     ref="ruleFormRef" label-width="100px" class="demo-ruleForm">
                <el-row :gutter="20">
                    <el-col :span="6">
                        <div class="grid-content bg-purple">
                            <el-form-item label="省份" prop="valueProvince">
                                <el-select
                                        :disabled="reported"
                                        v-model="ruleForm.valueProvince"
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
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="grid-content bg-purple">
                            <el-form-item label="城市" prop="valueCity">
                                <el-select
                                        :disabled="reported"
                                        v-model="ruleForm.valueCity"
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
                    <el-col :span="6">
                        <div class="grid-content bg-purple">
                            <el-form-item label="区县" prop="valueOrigin">
                                <el-select
                                        :disabled="reported"
                                        v-model="ruleForm.valueOrigin"
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
                    <el-col :span="6">
                        <div class="grid-content bg-purple">
                            <el-form-item v-if="reported" label="打卡位置" prop="valueOrigin">
                                <el-tag size="small">
                                    {{ruleForm.address}}
                                </el-tag>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <div class="grid-content bg-purple">
                            <el-form-item label="目前健康状况" prop="situation">
                                <el-radio-group :disabled="reported" v-model="ruleForm.situation">
                                    <el-radio :label="0">健康</el-radio>
                                    <el-radio :label="1">有咳嗽发热症状</el-radio>
                                    <el-radio :label="2">其他情况</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div class="grid-content bg-purple">
                            <el-form-item label="接触的人是否有疑似症状?  (冠状病毒检测结果为阳性或尚在等待检测结构)" prop="touch">
                                <el-radio-group :disabled="reported" v-model="ruleForm.touch">
                                    <el-radio :label="1">是</el-radio>
                                    <el-radio :label="0">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <div class="grid-content bg-purple">
                            <el-form-item label="自2020年1月1日起,是否在湖北省停留过 (保存转车路过)" prop="passby">
                                <el-radio-group :disabled="reported" v-model="ruleForm.passby">
                                    <el-radio :label="1">是</el-radio>
                                    <el-radio :label="0">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div class="grid-content bg-purple">
                            <el-form-item label="自2020年1月1日起,是否有接待过来自湖北的客户,亲戚或朋友)" prop="reception">
                                <el-radio-group :disabled="reported" v-model="ruleForm.reception">
                                    <el-radio :label="1">是</el-radio>
                                    <el-radio :label="0">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>

            </el-form>

        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, onMounted, onBeforeUnmount, getCurrentInstance} from "vue";
    import {ElMessage, ElNotification} from "element-plus";
    import {useStore} from "vuex";
    import {history, isReport, report} from '../../../../api/business/material/health'
    import {getProvinces} from '../../../../api/business/material/businessUtils'

    export default {

        setup() {
            const store = useStore();
            const { ctx } = getCurrentInstance();

            const drawer = ref(false)
            const direction = ref('rtl')
            const queryMap = reactive({pageSize: 4, pageNum: 1})
            const reported = ref(false)
            const historyTable = ref([])
            const historyTotal = ref(0)
            const provinceList = ref([])  // 省列表
            let cityList = []  // 市列表
            let originList = []  // 区列表
            const cityOptions = ref([]) // 市下拉框数据
            const originOptions = ref([]) // 区下拉框数据
            const ruleForm = ref({})
            const nowTime = ref(new Date())
            const userInfo = ref({})
            const rules = ref({
                situation: [
                    {required: true, message: "请选择当前情况", trigger: "blur"},
                ],
                touch: [
                    {required: true, message: "请选择是否接触", trigger: "blur"},
                ],
                passby: [
                    {required: true, message: "请选择是否路过", trigger: "blur"},
                ],
                reception: [
                    {required: true, message: "请选择是否招待", trigger: "blur"},
                ],
                valueProvince: [
                    {required: true, message: "请输入省份", trigger: "blur"}
                ],
                valueCity: [{required: true, message: "请输入城市", trigger: "blur"}],
                valueOrigin: [
                    {required: true, message: "请输入区县", trigger: "blur"}
                ],
            })

            const ruleFormRef = ref(null)


            const OpenHistory = () => {
                drawer.value = true;
                reportHistory();
            }

            /**
             * 翻页
             */
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                reportHistory();
            }

            //打卡历史记录
            const reportHistory = () => {
                history(queryMap).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("历史记录获取失败:" + res.data.msg);
                    } else {
                        historyTable.value = res.data.data.rows;
                        historyTotal.value = res.data.data.total;
                    }
                }).catch((res) => {
                    ElMessage.error("历史记录获取失败:" + res);
                });
            }
            //今日是否已签到
            const isReportToday = () => {
                isReport().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("今日健康报备检查错误:" + res.data.msg);
                    } else {
                        if (res.data.data !== null ) {
                            reported.value = true;
                            ruleForm.value = res.data.data;
                        } else {
                            reported.value = false;
                            ElNotification.warning({
                                title: '温馨提示',
                                message: '您今日还未打卡,别忘记了哟~'
                            });
                        }
                    }
                }).catch((res) => {
                    ElMessage.error("今日健康报备检查错误:" + res);
                });
            }
            //打卡
            const onSubmit = () => {
                ruleFormRef.value.validate( valid => {
                    if (!valid) {
                        return;
                    } else {
                        ruleForm.value.address =
                            ruleForm.value.province +
                            "/" +
                            ruleForm.value.city +
                            "/" +
                            ruleForm.value.origin;
                        report(ruleForm.value).then((res) => {
                            if (res.data.success) {
                                reported.value = true;
                                ruleForm.value.createTime = nowTime.value;
                                ElMessage.success("健康报备成功");
                            } else {
                                return ElMessage.error("健康报备失败:" + res.data.msg);
                            }
                        }).catch((res) => {
                            ElMessage.error("健康报备失败:" + res);
                        });
                    }
                });
            }

            //重置打卡表单
            const reset = () => {
                ruleFormRef.value.clearValidate();
                ruleForm.value = {};
            }
            const currentTime = () => {
                setInterval(getDate, 500);
            }
            const getDate = () => {
                nowTime.value = new Date();

            }
            // 选择省
            const changeProvince = (val) => {
                provinceList.value.forEach((province, index) => {
                    if (val.toString() === provinceList.value[index].value) {
                        cityOptions.value = provinceList.value[index].children;
                        ruleForm.value.valueCity = provinceList.value[index].children[0].value; //设置市的值
                        ruleForm.value.city = provinceList.value[index].children[0].label;

                        ruleForm.value.valueOrigin = provinceList.value[index].children[0].children[0].value; //设置县的值
                        ruleForm.value.origin = provinceList.value[index].children[0].children[0].label;

                        originOptions.value = provinceList.value[index].children[0].children;
                        //set value
                        ruleForm.value.province = provinceList.value[index].label;
                    }
                });
            }
            // 选择市
            const changeCity = (val) => {
                cityList.forEach((city, index) => {
                    if (val.toString() === cityList[index].value) {
                        originOptions.value = cityList[index].children;
                        ruleForm.value.valueOrigin = cityList[index].children[0].value; //设置县的值;
                        //set value
                        ruleForm.value.city = cityList[index].label;
                    }
                });
            }
            // 选择区
            const changeOrigin = (val) => {
                ruleForm.value.valueOrigin = val;

                originList.forEach((origin, index) => {
                    if (val.toString() === originList[index].value) {
                        //set value
                        ruleForm.value.origin = originList[index].label;
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

            onMounted(() => {
                currentTime();
            })

            // 销毁定时器
            onBeforeUnmount(() => {
                if (getDate) {
                    clearInterval(getDate); // 在Vue实例销毁前，清除时间定时器
                }
            })

            isReportToday();
            userInfo.value = store.state.login.userInfo;
            getJsonData();

            return {
                drawer,
                direction,
                queryMap,
                reported,
                historyTable,
                historyTotal,
                provinceList,
                cityList,
                originList,
                cityOptions,
                originOptions,
                ruleForm,
                nowTime,
                userInfo,
                rules,
                ruleFormRef,
                OpenHistory,
                handleCurrentChange,
                reportHistory,
                isReportToday,
                onSubmit,
                reset,
                currentTime,
                getDate,
                changeProvince,
                changeOrigin,
                changeCity,
                getJsonData
            }
        },
    }
</script>
