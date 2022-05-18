import service from "../../../utils/request";

export function getTechnicalConditionListApi(data) {
    return service.request({
        url: '/asset/metadata/technicalcondition/getTechnicalConditionList',
        method: 'post',
        data
    });
}

export function exportTechnicalConditionExcelApi() {
    return service.request({
        url: '/asset/metadata/technicalcondition/exportTechnicalConditionExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeTechnicalConditionStatusApi(technicalConditionId,status) {
    return service.request({
        url: '/asset/metadata/technicalcondition/changeTechnicalConditionStatus/' + technicalConditionId + "/" + status,
        method: 'put',
    });
}

export function addTechnicalConditionApi(data) {
    return service.request({
        url: '/asset/metadata/technicalcondition/addTechnicalCondition',
        method: 'post',
        data
    });
}

export function updateTechnicalConditionApi(data) {
    return service.request({
        url: '/asset/metadata/technicalcondition/updateTechnicalCondition',
        method: 'put',
        data
    });
}

export function deleteTechnicalConditionApi(technicalConditionId) {
    return service.request({
        url: '/asset/metadata/technicalcondition/deleteTechnicalCondition/' + technicalConditionId,
        method: 'delete',
    });
}

export function deleteTechnicalConditionByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/technicalcondition/deleteTechnicalConditionByBatchId',
        method: 'delete',
        data
    });
}

export function importTechnicalConditionApi(data) {
    return service.request({
        url: '/asset/metadata/technicalcondition/importTechnicalCondition',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
