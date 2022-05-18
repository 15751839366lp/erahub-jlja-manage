import service from "../../../utils/request";

export function getDepreciationMethodListApi(data) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/getDepreciationMethodList',
        method: 'post',
        data
    });
}

export function exportDepreciationMethodExcelApi() {
    return service.request({
        url: '/asset/metadata/depreciationmethod/exportDepreciationMethodExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeDepreciationMethodStatusApi(depreciationMethodId,status) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/changeDepreciationMethodStatus/' + depreciationMethodId + "/" + status,
        method: 'put',
    });
}

export function addDepreciationMethodApi(data) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/addDepreciationMethod',
        method: 'post',
        data
    });
}

export function updateDepreciationMethodApi(data) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/updateDepreciationMethod',
        method: 'put',
        data
    });
}

export function deleteDepreciationMethodApi(depreciationMethodId) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/deleteDepreciationMethod/' + depreciationMethodId,
        method: 'delete',
    });
}

export function deleteDepreciationMethodByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/deleteDepreciationMethodByBatchId',
        method: 'delete',
        data
    });
}

export function importDepreciationMethodApi(data) {
    return service.request({
        url: '/asset/metadata/depreciationmethod/importDepreciationMethod',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
