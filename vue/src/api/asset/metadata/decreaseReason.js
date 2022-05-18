import service from "../../../utils/request";

export function getDecreaseReasonListApi(data) {
    return service.request({
        url: '/asset/metadata/decreasereason/getDecreaseReasonList',
        method: 'post',
        data
    });
}

export function exportDecreaseReasonExcelApi() {
    return service.request({
        url: '/asset/metadata/decreasereason/exportDecreaseReasonExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeDecreaseReasonStatusApi(decreaseReasonId,status) {
    return service.request({
        url: '/asset/metadata/decreasereason/changeDecreaseReasonStatus/' + decreaseReasonId + "/" + status,
        method: 'put',
    });
}

export function addDecreaseReasonApi(data) {
    return service.request({
        url: '/asset/metadata/decreasereason/addDecreaseReason',
        method: 'post',
        data
    });
}

export function updateDecreaseReasonApi(data) {
    return service.request({
        url: '/asset/metadata/decreasereason/updateDecreaseReason',
        method: 'put',
        data
    });
}

export function deleteDecreaseReasonApi(decreaseReasonId) {
    return service.request({
        url: '/asset/metadata/decreasereason/deleteDecreaseReason/' + decreaseReasonId,
        method: 'delete',
    });
}

export function deleteDecreaseReasonByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/decreasereason/deleteDecreaseReasonByBatchId',
        method: 'delete',
        data
    });
}

export function importDecreaseReasonApi(data) {
    return service.request({
        url: '/asset/metadata/decreasereason/importDecreaseReason',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
