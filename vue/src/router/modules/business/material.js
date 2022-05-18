const material = [
    {
        path: '/business/material/product/list',
        name: 'ProductList',
        meta:{
            title: "物资资料"
        },
        component: () => import( '../../../views/business/material/product/list.vue'), //物资资料
    },
    {
        path: '/business/material/product/in-stocks',
        name: 'ProductInStocks',
        meta:{
            title: "入库记录"
        },
        component: () => import( '../../../views/business/material/product/in-stock.vue'), //入库记录
    },
    {
        path: '/business/material/product/out-stocks',
        name: 'ProductOutStocks',
        meta:{
            title: "发放记录"
        },
        component: () => import( '../../../views/business/material/product/out-stock.vue'), //发放记录
    },
    {
        path: '/business/material/product/categories',
        name: 'ProductCategories',
        meta:{
            title: "物资类别"
        },
        component: () => import( '../../../views/business/material/product/category.vue'), //物资类别
    },
    {
        path: '/business/material/product/stocks',
        name: 'ProductStocks',
        meta:{
            title: "物资库存"
        },
        component: () => import( '../../../views/business/material/product/stock.vue'),  //物资库存
    },
    {
        path: '/business/material/product/suppliers',
        name: 'ProductSuppliers',
        meta:{
            title: "物资来源"
        },
        component: () => import( '../../../views/business/material/product/supplier.vue'), //物资来源
    },
    {
        path: '/business/material/product/consumers',
        name: 'ProductConsumers',
        meta:{
            title: "物资去处"
        },
        component: () => import( '../../../views/business/material/product/consumer.vue'), //物资去处
    },
     {
        path: '/business/material/product/add-stocks',
        name: 'ProductAddStocks',
        meta:{
            title: "物资入库"
        },
        component: () => import( '../../../views/business/material/product/add-stock.vue'), //物资入库
    },
    {
        path: '/business/material/product/publish',
        name: 'ProductPublish',
        meta:{
            title: "物资发放"
        },
        component: () => import( '../../../views/business/material/product/publish.vue'),   //物资发放
    },
    {
        path: '/business/material/covid19/map',
        name: 'Covid19Map',
        meta:{
            title: "全国疫情"
        },
        component: () => import( '../../../views/business/material/covid19/map.vue'), //全国疫情
    },
    {
        path: '/business/material/covid19/health',
        name: 'Health',
        meta:{
            title: "健康打卡"
        },
        component: () => import( '../../../views/business/material/covid19/health.vue'), //健康打卡
    }
]

export default material