<template>
    <div id="inStocks">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资管理</el-breadcrumb-item>
            <el-breadcrumb-item>物资入库</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 卡片区域 -->
        <el-card>
            <!-- 搜索部分 -->
            <el-form size="small" :inline="true" :model="queryMap" class="demo-form-inline">
                <el-form-item label="类型">
                    <el-select clearable v-model="queryMap.type" placeholder="请选择入库类型">
                        <el-option label="捐赠" value="1"></el-option>
                        <el-option label="下拨" value="2"></el-option>
                        <el-option label="采购" value="3"></el-option>
                        <el-option label="借用" value="4"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="单号">

                    <el-input
                            clearable
                            @clear="search"
                            v-model="queryMap.inNum"
                            placeholder="请输入入库单查询"
                            @keyup.enter.native="search"
                            class="input-with-select"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="queryMap.status" placeholder="请选择状态">
                        <el-option label="已入库" :value="0"></el-option>
                        <el-option label="回收站" :value="1"></el-option>
                        <el-option label="待审核" :value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-date-picker
                            :clearable="false"
                            v-model="range"
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
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button icon="el-icon-refresh" type="warning" @click="clearTime">重置</el-button>
                </el-form-item>
                <el-form-item>
                    <router-link to="/business/material/product/add-stocks">
                        <el-button type="success" icon="el-icon-plus">入库</el-button>
                    </router-link>
                </el-form-item>

                <el-form-item>
                    <el-button icon="el-icon-download">导出</el-button>
                </el-form-item>

            </el-form>
            <!-- 表格区域 -->
            <el-table size="mini" v-loading="loading" :data="tableData" border style="width: 100%;" height="420">
                <el-table-column label="#" prop="id" width="50"></el-table-column>
                <el-table-column prop="inNum" :show-overflow-tooltip='true' label="入库单号" width="180"></el-table-column>
                <el-table-column prop="type" label="物资类型" width="100">
                    <template #default="scope">
                        <el-tag type="success" v-if="scope.row.type===1">捐赠</el-tag>
                        <el-tag v-else-if="scope.row.type===2">下拨</el-tag>
                        <el-tag type="danger" v-else-if="scope.row.type===3">采购</el-tag>
                        <el-tag type="warning" v-else>借用</el-tag>
                    </template>
                </el-table-column>

                <el-table-column prop="productNumber" label="数量" sortable width="100"></el-table-column>
                <el-table-column prop="phone" label="联系方式" width="150"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                        <el-tag size="mini" type="danger" effect="plain" v-if="scope.row.status==1">回收</el-tag>
                        <el-tag size="mini" effect="plain" v-if="scope.row.status==0">已入库</el-tag>
                        <el-tag size="mini" effect="plain" type="warning" v-if="scope.row.status==2">审核中</el-tag>
                    </template>
                </el-table-column>

                <el-table-column prop="operator" label="操作员" width="180"></el-table-column>
                <el-table-column prop="supplierName" label="物资提供方" width="180"></el-table-column>
                <el-table-column prop="createTime" label="入库时间" sortable width="180"></el-table-column>
                <el-table-column label="操作" fixed="right" width="200">
                    <template #default="scope">
                        <el-button icon="el-icon-view" @click="getDetail(scope.row.id)"
                                   type="text" size="small">明细

                        </el-button>

                        <!--                        给操作员使用的按钮-->
                        <span v-if="scope.row.status==0">
                             <el-popconfirm
                                     @confirm="removeData(scope.row.id)"
                                     style="margin-left: 20px;"
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
                        <!--   给操作员使用的按钮(回收站)-->
                        <span v-if="scope.row.status===1">
                             <el-popconfirm
                                     @confirm="backData(scope.row.id)"
                                     style="margin-left:10px;"
                                     confirmButtonText='好的'
                                     cancelButtonText='不用了'
                                     icon="el-icon-info"
                                     iconColor="green"
                                     title="这是一段内容确定恢复数据吗？"
                             >
                                 <template #reference>
                            <el-button type="text" size="mini"
                                       icon="el-icon-s-operation">还原</el-button>
                                 </template>
                        </el-popconfirm>
                                <el-popconfirm
                                        style="margin-left:20px;"
                                        @confirm="del(scope.row.id)"
                                        title="这是一段内容确定删除吗？"
                                >
                                    <template #reference>
                            <el-button type="text" size="small" icon="el-icon-delete">删除</el-button>
                                    </template>
                        </el-popconfirm>
                        </span>

                        <!--                        给审核员使用的按钮-->
                        <span v-if="scope.row.status==2">
                          <el-popconfirm
                                  style="margin-left:20px;"
                                  @confirm="publishData(scope.row.id)"
                                  title="审核通过后库存将更新,是否通过"
                          >
                              <template #reference>
                        <el-button type="text" size="small" icon="el-icon-refresh">通过</el-button>
                                  </template>
                    </el-popconfirm>
                        <el-popconfirm
                                style="margin-left:20px;"
                                @confirm="del(scope.row.id)"
                                title="这是一段内容确定删除吗？"
                        >
                            <template #reference>
                            <el-button type="text" size="small" icon="el-icon-delete">删除</el-button>
                            </template>
                        </el-popconfirm>
                        </span>

                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页部分 -->
            <el-pagination
                    style="margin-top:10px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 入库明细 -->
            <el-dialog title="入库明细" v-model="dialogVisible" @close="closeDetail" width="60%">
                <!--                来源信息-->
                <el-card class="box-card" style="margin-bottom: 10px;">
                    <div class="text item">
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">省区市:</span> &nbsp;<el-tag size="mini">
                                    {{supplier.address}}
                                </el-tag>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">具体位置:</span> &nbsp;<el-tag
                                        size="mini">{{supplier.name}}
                                </el-tag>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">联系人 :</span> &nbsp;<el-tag
                                        size="mini">{{supplier.contact}}
                                </el-tag>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">电话 : </span>&nbsp;<el-tag size="mini">
                                    {{supplier.phone}}
                                </el-tag>
                                </div>
                            </el-col>

                        </el-row>
                    </div>
                </el-card>

                <!--                步骤条-->
                <el-steps simple v-if="pStatus==0" style="margin-left: 10px;margin-bottom: 5px;" :space="200"
                          :active="3" finish-status="success">
                    <el-step title="提交入库单"></el-step>
                    <el-step title="审核入库单"></el-step>
                    <el-step title="进入库存"></el-step>
                </el-steps>
                <el-steps simple v-if="pStatus==2" style="margin-left: 10px;margin-bottom: 5px;" :space="200"
                          :active="2" finish-status="success">
                    <el-step title="提交入库单"></el-step>
                    <el-step title="审核入库单"></el-step>
                    <el-step title="进入库存"></el-step>
                </el-steps>
                <span>
            <el-table height="260" max-height="350" border :data="detailTable" style="width: 100%">
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="pnum" label="商品编号"></el-table-column>
               <el-table-column prop="model" label="规格"></el-table-column>
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
                          style="width: 30px;height:30px"
                  />
                </template>
              </el-table-column>
               <el-table-column prop="count" label="数量"></el-table-column>
                <el-table-column prop="unit" label="单位"></el-table-column>
            </el-table>
              <!--              明细分页-->
        <el-pagination
                style="margin-top:20px;"
                background
                @current-change="handleDetailSizeChange"
                :current-page="this.pageNum"
                :page-size="3"
                layout="prev, pager, next,total"
                :total="this.detailTotal">
        </el-pagination>

        </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {publish, back, remove, deleteInStock, detail, findInStockList} from '../../../../api/business/material/inStock'

    export default {

        setup() {

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
            const supplier = ref({})
            const detailTotal = ref(0)
            const pageNum = ref(1)
            const detailId = ref(undefined)
            const loading = ref(true)
            const detailTable = ref([])
            const dialogVisible = ref(false)
            const total = ref(0)
            const queryMap = ref({pageNum: 1, pageSize: 10, status: 0})
            const tableData = ref([])
            const range = ref([])
            const pStatus = ref('') //步骤flag

            /**
             * 关闭明细
             */
            const closeDetail = () => {
                pageNum.value = 1;
            }
            const clearTime = () => {
                queryMap.value = {pageNum: 1, pageSize: 10, status: 0};
                queryMap.value.startTime = null;
                queryMap.value.endTime = null;
                range.value = [];
            }
            /**
             *  改变页码
             */
            const handleDetailSizeChange = (newSize) => {
                pageNum.value = newSize;
                getDetail(detailId.value);
            }

            /**
             *物资入库审核
             */
            const publishData = (id) => {
                publish("/business/material/inStock/publish/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("审核失败:" + res.data.data.errorMsg);
                    } else {
                        loadTableData();
                        return ElMessage.success("入库已审核通过");
                    }
                }).catch((res) => {
                    ElMessage.error("审核失败:" + res);
                });
            }
            /**
             * 从回收站恢复
             */
            const backData = (id) => {
                back("/business/material/inStock/back/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("从回收站恢复失败:" + res.data.data.errorMsg);
                    } else {
                        loadTableData();
                        return ElMessage.success("从回收站中已恢复");
                    }
                }).catch((res) => {
                    ElMessage.error("从回收站恢复失败:" + res);
                });
            }
            /**
             * 移除回收站
             */
            const removeData = (id) => {
                remove("/business/material/inStock/remove/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("移入回收站失败:" + res.data.data.errorMsg);
                    } else {
                        loadTableData();
                        return ElMessage.success("移入回收站成功");
                    }
                }).catch((res) => {
                    ElMessage.error("移入回收站失败:" + res);
                });
            }
            /**删除明细
             */
            const del = (id) => {
                deleteInStock("/business/material/inStock/delete/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("删除失败:" + res.data.data.errorMsg);
                    } else {
                        loadTableData();
                        return ElMessage.success("删除入库单记录成功");
                    }
                }).catch((res) => {
                    ElMessage.error("删除失败:" + res);
                });
            }
            /**
             * 查看入库单明细
             */
            const getDetail = (id) => {
                detailId.value = id;
                detail("/business/material/inStock/detail/" + id + "?pageNum=" + pageNum.value).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("获取明细失败:" + res.data.data.errorMsg);
                    } else {
                        detailTable.value = res.data.data.itemVOS;
                        detailTotal.value = res.data.data.total;
                        supplier.value = res.data.data.supplierVO;
                        pStatus.value = res.data.data.status;
                        dialogVisible.value = true;
                    }
                }).catch((res) => {
                    ElMessage.error("获取明细失败:" + res);
                });
            }
            /**
             * 加载表格数据
             */
            const loadTableData = () => {
                if (range.value != null && range.value.length === 1) {
                    queryMap.value.startTime = range.value[0];
                } else if (range.value != null && range.value.length === 2) {
                    queryMap.value.startTime = range.value[0];
                    queryMap.value.endTime = range.value[1];
                }

                findInStockList(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取列表失败:" + res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        tableData.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取列表失败:" + res);
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
             * 查询入库单
             */
            const search = () => {
                queryMap.value.pageNum = 1;
                loadTableData();
            }
            /**
             * 点击分页
             */
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                loadTableData();
            }


            loadTableData();
            setTimeout(() => {
                loading.value = false;
            }, 500);

            return {
                pickerOptions,
                supplier,
                detailTotal,
                pageNum,
                detailId,
                loading,
                detailTable,
                dialogVisible,
                total,
                queryMap,
                tableData,
                range,
                pStatus,
                closeDetail,
                clearTime,
                handleDetailSizeChange,
                publishData,
                backData,
                removeData,
                del,
                getDetail,
                loadTableData,
                handleSizeChange,
                handleCurrentChange,
                search,
            };
        },
    };
</script>


