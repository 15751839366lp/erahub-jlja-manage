import service from "../../../utils/request";

export function getServiceConditionListApi(data) {
    return service.request({
        url: '/asset/metadata/servicecondition/getServiceConditionList',
        method: 'post',
        data
    });
}

export function exportServiceConditionExcelApi() {
    return service.request({
        url: '/asset/metadata/servicecondition/exportServiceConditionExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeServiceConditionStatusApi(serviceConditionId,status) {
    return service.request({
        url: '/asset/metadata/servicecondition/changeServiceConditionStatus/' + serviceConditionId + "/" + status,
        method: 'put',
    });
}

export function addServiceConditionApi(data) {
    return service.request({
        url: '/asset/metadata/servicecondition/addServiceCondition',
        method: 'post',
        data
    });
}

export function updateServiceConditionApi(data) {
    return service.request({
        url: '/asset/metadata/servicecondition/updateServiceCondition',
        method: 'put',
        data
    });
}

export function deleteServiceConditionApi(serviceConditionId) {
    return service.request({
        url: '/asset/metadata/servicecondition/deleteServiceCondition/' + serviceConditionId,
        method: 'delete',
    });
}

export function deleteServiceConditionByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/servicecondition/deleteServiceConditionByBatchId',
        method: 'delete',
        data
    });
}

export function importServiceConditionApi(data) {
    return service.request({
        url: '/asset/metadata/servicecondition/importServiceCondition',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
