import service from "../../../utils/request";

export function getAssetOwnershipListApi(data) {
    return service.request({
        url: '/asset/metadata/assetownership/getAssetOwnershipList',
        method: 'post',
        data
    });
}

export function exportAssetOwnershipExcelApi() {
    return service.request({
        url: '/asset/metadata/assetownership/exportAssetOwnershipExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeAssetOwnershipStatusApi(assetOwnershipId,status) {
    return service.request({
        url: '/asset/metadata/assetownership/changeAssetOwnershipStatus/' + assetOwnershipId + "/" + status,
        method: 'put',
    });
}

export function addAssetOwnershipApi(data) {
    return service.request({
        url: '/asset/metadata/assetownership/addAssetOwnership',
        method: 'post',
        data
    });
}

export function updateAssetOwnershipApi(data) {
    return service.request({
        url: '/asset/metadata/assetownership/updateAssetOwnership',
        method: 'put',
        data
    });
}

export function deleteAssetOwnershipApi(assetOwnershipId) {
    return service.request({
        url: '/asset/metadata/assetownership/deleteAssetOwnership/' + assetOwnershipId,
        method: 'delete',
    });
}

export function deleteAssetOwnershipByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/assetownership/deleteAssetOwnershipByBatchId',
        method: 'delete',
        data
    });
}

export function importAssetOwnershipApi(data) {
    return service.request({
        url: '/asset/metadata/assetownership/importAssetOwnership',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
