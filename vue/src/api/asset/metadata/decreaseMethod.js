import service from "../../../utils/request";

export function getDecreaseMethodListApi(data) {
    return service.request({
        url: '/asset/metadata/decreasemethod/getDecreaseMethodList',
        method: 'post',
        data
    });
}

export function exportDecreaseMethodExcelApi() {
    return service.request({
        url: '/asset/metadata/decreasemethod/exportDecreaseMethodExcel',
        method: 'get',
        responseType: "blob"
    });
}

export function changeDecreaseMethodStatusApi(decreaseMethodId,status) {
    return service.request({
        url: '/asset/metadata/decreasemethod/changeDecreaseMethodStatus/' + decreaseMethodId + "/" + status,
        method: 'put',
    });
}

export function addDecreaseMethodApi(data) {
    return service.request({
        url: '/asset/metadata/decreasemethod/addDecreaseMethod',
        method: 'post',
        data
    });
}

export function updateDecreaseMethodApi(data) {
    return service.request({
        url: '/asset/metadata/decreasemethod/updateDecreaseMethod',
        method: 'put',
        data
    });
}

export function deleteDecreaseMethodApi(decreaseMethodId) {
    return service.request({
        url: '/asset/metadata/decreasemethod/deleteDecreaseMethod/' + decreaseMethodId,
        method: 'delete',
    });
}

export function deleteDecreaseMethodByBatchIdApi(data) {
    return service.request({
        url: '/asset/metadata/decreasemethod/deleteDecreaseMethodByBatchId',
        method: 'delete',
        data
    });
}

export function importDecreaseMethodApi(data) {
    return service.request({
        url: '/asset/metadata/decreasemethod/importDecreaseMethod',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data;boundary=<calculated when request is sent>'
        },
        contentType: 'application/json',
        processData: true,
        data
    });
}
