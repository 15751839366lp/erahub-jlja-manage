import service from "../../../utils/request";

export function getAssetCategoryListApi(data) {
    return service.request({
        url: '/asset/metadata/assetcategory/getAssetCategoryList',
        method: 'post',
        data
    });
}

export function exportAssetCategoryExcelApi() {
    return service.request({
        url: '/asset/metadata/assetcategory/exportAssetCategoryExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeAssetCategoryStatusApi(assetCategoryId,status) {
    return service.request({
        url: '/asset/metadata/assetcategory/changeAssetCategoryStatus/' + assetCategoryId + "/" + status,
        method: 'put',
    });
}

export function addAssetCategoryApi(data) {
    return service.request({
        url: '/asset/metadata/assetcategory/addAssetCategory',
        method: 'post',
        data
    });
}

export function updateAssetCategoryApi(data) {
    return service.request({
        url: '/asset/metadata/assetcategory/updateAssetCategory',
        method: 'put',
        data
    });
}

export function deleteAssetCategoryApi(id) {
    return service.request({
        url: '/asset/metadata/assetcategory/deleteAssetCategory/' + id,
        method: 'delete',
    });
}

export function deleteAssetCategoryByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/assetcategory/deleteAssetCategoryByBatchId',
        method: 'delete',
        data
    });
}

export function importAssetCategoryApi(data) {
    return service.request({
        url: '/asset/metadata/assetcategory/importAssetCategory',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}

export function getAllDepreciationMethodApi() {
    return service.request({
        url: '/asset/metadata/depreciationmethod/getAllDepreciationMethod',
        method: 'get'
    });
}