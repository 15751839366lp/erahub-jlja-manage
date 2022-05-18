import service from "../../../utils/request";

export function getAssetSourceListApi(data) {
    return service.request({
        url: '/asset/metadata/assetsource/getAssetSourceList',
        method: 'post',
        data
    });
}

export function exportAssetSourceExcelApi() {
    return service.request({
        url: '/asset/metadata/assetsource/exportAssetSourceExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeAssetSourceStatusApi(assetSourceId,status) {
    return service.request({
        url: '/asset/metadata/assetsource/changeAssetSourceStatus/' + assetSourceId + "/" + status,
        method: 'put',
    });
}

export function addAssetSourceApi(data) {
    return service.request({
        url: '/asset/metadata/assetsource/addAssetSource',
        method: 'post',
        data
    });
}

export function updateAssetSourceApi(data) {
    return service.request({
        url: '/asset/metadata/assetsource/updateAssetSource',
        method: 'put',
        data
    });
}

export function deleteAssetSourceApi(assetSourceId) {
    return service.request({
        url: '/asset/metadata/assetsource/deleteAssetSource/' + assetSourceId,
        method: 'delete',
    });
}

export function deleteAssetSourceByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/assetsource/deleteAssetSourceByBatchId',
        method: 'delete',
        data
    });
}

export function importAssetSourceApi(data) {
    return service.request({
        url: '/asset/metadata/assetsource/importAssetSource',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
