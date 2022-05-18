import service from "../../../utils/request";

export function getAssetProjectListApi(data) {
    return service.request({
        url: '/asset/metadata/assetproject/getAssetProjectList',
        method: 'post',
        data
    });
}

export function exportAssetProjectExcelApi() {
    return service.request({
        url: '/asset/metadata/assetproject/exportAssetProjectExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeAssetProjectStatusApi(assetProjectId,status) {
    return service.request({
        url: '/asset/metadata/assetproject/changeAssetProjectStatus/' + assetProjectId + "/" + status,
        method: 'put',
    });
}

export function addAssetProjectApi(data) {
    return service.request({
        url: '/asset/metadata/assetproject/addAssetProject',
        method: 'post',
        data
    });
}

export function updateAssetProjectApi(data) {
    return service.request({
        url: '/asset/metadata/assetproject/updateAssetProject',
        method: 'put',
        data
    });
}

export function deleteAssetProjectApi(assetProjectId) {
    return service.request({
        url: '/asset/metadata/assetproject/deleteAssetProject/' + assetProjectId,
        method: 'delete',
    });
}

export function deleteAssetProjectByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/assetproject/deleteAssetProjectByBatchId',
        method: 'delete',
        data
    });
}

export function importAssetProjectApi(data) {
    return service.request({
        url: '/asset/metadata/assetproject/importAssetProject',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
