<template>
    <div class="outStocks">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资管理</el-breadcrumb-item>
            <el-breadcrumb-item>出库记录</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 卡片部分-->
        <el-card>
            <!--搜索部分-->
            <el-form size="mini" :inline="true" :model="queryMap" class="demo-form-inline">
                <el-form-item label="发放单号">
                    <el-input v-model="queryMap.outNum" placeholder="发放单号"></el-input>
                </el-form-item>
                <el-form-item label="发放类型">
                    <el-select v-model="queryMap.type" placeholder="发放类型">
                        <el-option label="全部类型" value=""></el-option>
                        <el-option label="政府领取" value="0"></el-option>
                        <el-option label="医院领取" value="1"></el-option>
                        <el-option label="小区领取" value="2"></el-option>
                        <el-option label="个人领取" value="3"></el-option>
                        <el-option label="其他领取" value="4"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-select v-model="queryMap.status" placeholder="请选择状态">
                        <el-option label="已发放" :value="0"></el-option>
                        <el-option label="回收站" :value="1"></el-option>
                        <el-option label="待审核" :value="2"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="search">查询</el-button>
                    <el-button @click="reset">重置</el-button>
                </el-form-item>

                <el-form-item>
                    <router-link to="/business/material/product/publish">
                        <el-button type="success">
                            发放
                        </el-button>
                    </router-link>
                </el-form-item>
            </el-form>
            <!--            数据表格-->
            <el-table
                    size="mini"
                    border
                    :data="tableData"
                    style="width: 100%;" height="450px">
                <el-table-column label="#" prop="id" width="50"></el-table-column>
                <el-table-column prop="outNum" :show-overflow-tooltip='true' label="发放单号" width="180"></el-table-column>
                <el-table-column prop="type" label="发放类型" width="100">
                    <template #default="scope">
                        <el-tag effect="plain" size="mini" type="success" v-if="scope.row.type===0">政府领取</el-tag>
                        <el-tag effect="plain" size="mini" type="danger" v-else-if="scope.row.type===1">医院领取</el-tag>
                        <el-tag effect="plain" size="mini" type="warning" v-else-if="scope.row.type===2">小区领取</el-tag>
                        <el-tag effect="plain" size="mini" type="info" v-else-if="scope.row.type===3">个人领取</el-tag>
                        <el-tag effect="plain" size="mini" v-else-if="scope.row.type===4">其他领取</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="priority" label="紧急程度" width="180">
                    <template #default="scope">
                        <el-rate
                                :disabled="true"
                                v-model="scope.row.priority"
                                show-text
                                :texts="['不急','常规','紧急','特急','超急']"
                        >
                        </el-rate>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="发放地点" width="150"></el-table-column>
                <el-table-column prop="productNumber" label="总数" width="80"></el-table-column>
                <el-table-column prop="phone" label="联系方式" width="120"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                        <el-tag size="mini" type="danger" effect="plain" v-if="scope.row.status==1">回收</el-tag>
                        <el-tag size="mini" effect="plain" v-if="scope.row.status==0">已放</el-tag>
                        <el-tag size="mini" effect="plain" type="warning" v-if="scope.row.status==2">审核中</el-tag>
                    </template>
                </el-table-column>

                <el-table-column prop="operator" label="操作员" width="150"></el-table-column>

                <el-table-column prop="createTime" label="发放时间" width="200px;"></el-table-column>
                <el-table-column label="操作" fixed="right" width="200">
                    <template #default="scope">
                        <el-button icon="el-icon-view" type="text" size="small" @click="getDetail(scope.row.id)">
                            明细
                        </el-button>
                        <!--                        给操作员使用的按钮-->
                        <span v-if="scope.row.status==0">
                             <el-popconfirm
                                     @confirm="removeStock(scope.row.id)"
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
                        <span v-if="scope.row.status==1">
                             <el-popconfirm
                                     @confirm="backStock(scope.row.id)"
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
                        <span v-if="scope.row.status===2">
                          <el-popconfirm
                                  style="margin-left:20px;"
                                  @confirm="publishStock(scope.row.id)"
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
            <!--                表格分页-->
            <el-pagination
                    style="margin-top:20px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 发放明细 -->
            <el-dialog title="发放明细" v-model="dialogVisible" @close="closeDetail" width="60%">
                <!--                来源信息-->
                <el-card class="box-card" style="margin-bottom: 10px;">
                    <div class="text item">
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">省区市:</span> &nbsp;<el-tag size="mini">
                                    {{consumer.address}}
                                </el-tag>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">具体位置:</span> &nbsp;<el-tag
                                        size="mini">{{consumer.name}}
                                </el-tag>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">联系人 :</span> &nbsp;<el-tag
                                        size="mini">{{consumer.contact}}
                                </el-tag>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <span style="font-size: 11px;color: #303030;">电话 : </span>&nbsp;<el-tag size="mini">
                                    {{consumer.phone}}
                                </el-tag>
                                </div>
                            </el-col>

                        </el-row>
                    </div>
                </el-card>
                <!--                步骤条-->
                <el-steps simple v-if="pStatus==0" style="margin-left: 10px;margin-bottom: 5px;" :space="200"
                          :active="3" finish-status="success">
                    <el-step title="提交发放单"></el-step>
                    <el-step title="审核发放单"></el-step>
                    <el-step title="成功发放"></el-step>
                </el-steps>
                <el-steps simple v-if="pStatus==2" style="margin-left: 10px;margin-bottom: 5px;" :space="200"
                          :active="2" finish-status="success">
                    <el-step title="提交发放单"></el-step>
                    <el-step title="审核发放单"></el-step>
                    <el-step title="成功发放"></el-step>
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
                :current-page="pageNum"
                :page-size="3"
                layout="prev, pager, next,total"
                :total="detailTotal">
        </el-pagination>

        </span>
            </el-dialog>

        </el-card>
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {publish, detail, remove, deleteOutStock, back, findOutStockList} from '../../../../api/business/material/outStock'

    export default {

        setup() {

            const consumer = ref({})
            const detailTotal = ref(0)
            const detailTable = ref([])
            const dialogVisible = ref(false)
            const pageNum = ref(1)
            const total = ref(0)
            const queryMap = ref({pageNum: 1, pageSize: 10, status: 2,})
            const tableData = ref([])
            const pStatus = ref('')  //步骤flag
            const detailId = ref(0)  //步骤flag

            /**
             *物资发放审核
             */
            const publishStock = (id) => {

                publish("/business/material/outStock/publish/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("审核失败:" + res.data.data.errorMsg);
                    } else {
                        loadTableData();
                        return ElMessage.success("发放已审核通过");
                    }
                }).catch((res) => {
                    ElMessage.error("审核失败:" + res);
                });
            }

            /**
             * 关闭明细
             */
            const closeDetail = () => {
                pageNum.value = 1;
            }
            /**
             *  改变页码
             */
            const handleDetailSizeChange = (newSize) => {
                pageNum.value = newSize;
                getDetail(detailId.value);
            }

            /**
             * 查看发放单明细
             */
            const getDetail = (id) => {
                detailId.value = id;
                detail("/business/material/outStock/detail/" + id + "?pageNum=" + pageNum.value).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("获取明细失败:" + res.data.data.errorMsg);
                    } else {
                        detailTable.value = res.data.data.itemVOS;
                        detailTotal.value = res.data.data.total;
                        consumer.value = res.data.data.consumerVO;
                        pStatus.value = res.data.data.status;
                        dialogVisible.value = true;
                    }
                }).catch((res) => {
                    ElMessage.error("获取明细失败:" + res);
                });
            }

            /**
             * 从回收站恢复
             */
            const backStock = (id) => {
                back("/business/material/outStock/back/" + id).then((res) => {
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
            const removeStock = (id) => {
                remove("/business/material/outStock/remove/" + id).then((res) => {
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

            /**
             * 重置查询表单
             */
            const reset = () => {
                queryMap.value = {pageNum: 1, pageSize: 10, status: 0,};
            }
            /**
             * 加载表格数据
             */
            const loadTableData = () => {
                findOutStockList(queryMap.value).then((res) => {
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
             * 点击分页
             */
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                loadTableData();
            }
            //搜索
            const search = () => {
                queryMap.value.pageNum = 1;
                loadTableData();
            }

            /**删除明细
             */
            const del = (id) => {
                deleteOutStock("/business/material/outStock/delete/" + id).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("删除失败:" + res.data.data.errorMsg);
                    } else {
                        loadTableData();
                        return ElMessage.success("删除发放单记录成功");
                    }
                }).catch((res) => {
                    ElMessage.error("获取列表失败:" + res);
                });
            }

            loadTableData();

            return {
                consumer,
                detailTotal,
                detailTable,
                dialogVisible,
                pageNum,
                total,
                queryMap,
                tableData,
                pStatus,
                detailId,
                publishStock,
                handleDetailSizeChange,
                getDetail,
                closeDetail,
                backStock,
                removeStock,
                reset,
                loadTableData,
                handleSizeChange,
                handleCurrentChange,
                search,
                del,
            }
        },
    }
</script>
