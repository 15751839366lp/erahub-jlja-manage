import service from "../../../utils/request";

export function getAssetEconomicUseListApi(data) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/getAssetEconomicUseList',
        method: 'post',
        data
    });
}

export function exportAssetEconomicUseExcelApi() {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/exportAssetEconomicUseExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeAssetEconomicUseStatusApi(assetEconomicUseId,status) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/changeAssetEconomicUseStatus/' + assetEconomicUseId + "/" + status,
        method: 'put',
    });
}

export function addAssetEconomicUseApi(data) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/addAssetEconomicUse',
        method: 'post',
        data
    });
}

export function updateAssetEconomicUseApi(data) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/updateAssetEconomicUse',
        method: 'put',
        data
    });
}

export function deleteAssetEconomicUseApi(assetEconomicUseId) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/deleteAssetEconomicUse/' + assetEconomicUseId,
        method: 'delete',
    });
}

export function deleteAssetEconomicUseByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/deleteAssetEconomicUseByBatchId',
        method: 'delete',
        data
    });
}

export function importAssetEconomicUseApi(data) {
    return service.request({
        url: '/asset/metadata/asseteconomicuse/importAssetEconomicUse',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
