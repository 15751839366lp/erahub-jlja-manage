<template>
    <div id="stocks">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资管理</el-breadcrumb-item>
            <el-breadcrumb-item>库存维护</el-breadcrumb-item>
        </el-breadcrumb>
        <el-row :gutter="20">
            <el-col :span="13">
                <div class="grid-content bg-purple-dark">
                    <el-card class="box-card">
                        <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
                        <div id="tianxing" style="width: 650px;height:350px;"></div>
                    </el-card>
                    <el-card class="box-card" style="margin-top:10px;">
                        <!-- 库存饼图 -->
                        <div id="bingtu" style="width: 590px;height:225px;"></div>
                    </el-card>
                </div>
            </el-col>
            <el-col :span="11">
                <div class="grid-content bg-purple-dark">
                    <el-card class="box-card">
                        <el-form size="mini" :inline="true" :model="queryMap" class="demo-form-inline">
                            <el-form-item>
                                <el-cascader
                                        placeholder="请选择分类查询"
                                        @change="selectChange"
                                        v-model="categorykeys"
                                        :props="searchSelectProps"
                                        :options="catetorys"
                                        clearable
                                ></el-cascader>
                            </el-form-item>

                            <el-form-item>
                                <el-input clearable @clear="search" v-model="queryMap.name"
                                          placeholder="物资名称"></el-input>
                            </el-form-item>

                            <el-form-item>
                                <el-button size="mini" type="primary" @click="search" icon="el-icon-search">查询
                                </el-button>
                            </el-form-item>
                        </el-form>
                        <el-table height="530" border :data="tableData" style="width: 100%">
                            <el-table-column prop="imageUrl" label="图片" align="center" width="80" padding="0px">
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
                                        <img :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"
                                             style="height: 200px;width: 200px"/>
                                        <template #reference>
                                        <img :src="'http://127.0.0.1:8989/'+scope.row.imageUrl"
                                             :alt="scope.row.imgUrl" style="height: 21px;width: 21px;cursor: pointer">
                                        </template>
                                    </el-popover>
                                </template>
                            </el-table-column>
                            <el-table-column prop="name" label="名称" width="140"></el-table-column>
                            <el-table-column prop="model" label="规格" width="120"></el-table-column>
                            <el-table-column prop="stock" label="库存">
                                <template #default="scope">
                                    <el-tag size="mini" closable>{{scope.row.stock}}</el-tag>
                                </template>
                            </el-table-column>
                            <el-table-column prop="unit" label="单位"></el-table-column>
                        </el-table>
                        <!-- 分页 -->
                        <el-pagination
                                style="margin-top:20px;"
                                background
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="queryMap.pageNum"
                                :page-sizes="[9, 10, 15, 20]"
                                layout="total, prev, pager, next, jumper,sizes"
                                :total="total"
                        ></el-pagination>
                    </el-card>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance, onMounted} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import echarts from "echarts";
    import {findProductStocks, findAllStocks} from '../../../../api/business/material/product';
    import {categoryTree} from '../../../../api/business/material/productCategory'

    export default {

        setup() {

            const catetorys = ref([]) //类别选择
            const searchSelectProps = reactive({
                expandTrigger: "hover",
                value: "id",
                label: "name",
                children: "children",
                checkStrictly: true
            }) //级联搜索选择器配置项
            const categorykeys = ref([])
            const total = ref(0)
            const tableData = ref([])
            const queryMap = ref({pageSize: 9, pageNum: 1})
            let xData = []
            let yData = []
            let legendData = [] //饼图存放物资名称
            let selected = {} //存放选择的数据
            let seriesData = [{name: "", value: ""}] //饼图数据

            /**
             * 搜索
             */
            const search = () => {
                queryMap.value.pageNum = 1;
                getStockList();
            }
            /**
             * 加载库存信息
             */
            const getStockList = () => {
                findProductStocks(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取物资库存列表失败:" + res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        tableData.value = res.data.data.rows;
                        xData = [];
                        yData = [];
                        selected = {};
                        //构建表格条形统计图的数据
                        tableData.value.forEach(function (e) {
                            xData.push(e.name);
                            yData.push(e.stock);
                        });
                        //重新绘制表格
                        draw();
                        //饼图
                        findAllProductStocks();
                    }
                }).catch((res) => {
                    ElMessage.error("获取物资库存列表失败:" + res);
                });
            }

            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.value.pageSize = newSize;
                getStockList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                getStockList();
            }
            /**
             * 绘制条形统计图
             */
            const draw = () => {
                let myChart = echarts.init(document.getElementById("tianxing"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        text: "库存条形图"
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            dataZoom: {
                                yAxisIndex: "none"
                            },
                            dataView: {readOnly: false}, //  缩放
                            magicType: {type: ["bar"]}, ///　　折线  直方图切换
                            restore: {}, // 重置
                            saveAsImage: {} // 导出图片
                        }
                    },
                    tooltip: {},
                    legend: {
                        data: ["库存量"]
                    },
                    xAxis: {
                        data: xData
                    },
                    yAxis: {},
                    series: [
                        {
                            name: "库存量",
                            type: "bar",
                            showBackground: true,
                            data: yData,
                            itemStyle: {
                                normal: {
                                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                                    color: function (params) {
                                        // build a color map as your need.
                                        let colorList = [
                                            '#0780cf ', '#fa6d1d ', '#ac2026 ', '#701866 ', '#d22e8d ',
                                            '#FE8463', '#a195c5 ', '#FAD860', '#F3A43B', '#60C0DD',
                                            '#D7504B', '#a195c5  ', '#F4E001', '#F0805A', '#63b2ee'
                                        ];
                                        return colorList[params.dataIndex]
                                    },
                                    //以下为是否显示，显示位置和显示格式的设置了
                                    label: {
                                        show: true,
                                        position: 'top',
//                             formatter: '{c}'
                                        formatter: '{b}\n{c}'
                                    }
                                }
                            },
                        },

                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
            /**
             * 绘制饼图
             */
            const drawRound = () => {
                let myChart = echarts.init(document.getElementById("bingtu"));
                let option = {
                    title: {
                        text: "库存占比图",

                        left: "left"
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {} // 导出图片
                        }
                    },
                    tooltip: {
                        trigger: "item",
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        type: "scroll",
                        orient: "vertical",
                        right: 10,
                        top: 20,
                        bottom: 20,
                        data: legendData,
                        selected: selected
                    },
                    series: [
                        {
                            name: "物资名称",
                            type: "pie",
                            radius: "55%",
                            center: ["40%", "50%"],
                            data: seriesData,
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: "rgba(0, 0, 0, 0.5)"
                                }
                            }
                        }
                    ]
                };
                myChart.setOption(option);
            }

            /**
             * 物资所有的库存信息
             */
            const findAllProductStocks = () => {
                findAllStocks(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取物资所有库存失败:" + res.data.data.errorMsg);
                    } else {
                        legendData = [];
                        selected = {};
                        seriesData = [{}];

                        //构建饼图的数据对象
                        res.data.data.forEach(function (e) {
                            legendData.push(e.name);
                            seriesData.push({name: e.name, value: e.stock});
                        });

                        //重新绘制表格
                        drawRound();
                    }
                }).catch((res) => {
                    ElMessage.error("获取物资所有库存失败:" + res);
                });

            }
            /**
             * 分类搜索改变时
             */
            const selectChange = () => {
                let str = "";
                if(categorykeys.value != null && categorykeys.value != undefined){
                    for (let i = 0; i < categorykeys.value.length; i++) {
                        str += categorykeys.value[i] + ",";
                    }
                    str = str.substring(0, str.length - 1);
                }
                queryMap.value.categorys = str;
            }
            /**
             * 加载物资类别
             */
            const getCatetorys = () => {
                categoryTree().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取物资类别失败:" + res.data.data.errorMsg);
                    } else {
                        catetorys.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取物资类别失败:" + res);
                });
            }

            onMounted(() => {
                draw();
                drawRound();
            })

            getStockList();
            getCatetorys();
            findAllProductStocks();

            return {
                catetorys,
                searchSelectProps,
                categorykeys,
                total,
                tableData,
                queryMap,
                xData,
                yData,
                legendData,
                selected,
                seriesData,
                search,
                getStockList,
                handleCurrentChange,
                handleSizeChange,
                draw,
                drawRound,
                findAllProductStocks,
                selectChange,
                getCatetorys,
            };
        },
    };
</script>
