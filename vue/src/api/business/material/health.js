import service from "../../../utils/request";

export function history(params) {
    return service.request({
        url: '/business/material/health/history',
        method: 'get',
        params: params
    });
}

export function isReport() {
    return service.request({
        url: '/business/material/health/isReport',
        method: 'get'
    });
}

export function report(data) {
    return service.request({
        url: '/business/material/health/report',
        method: 'post',
        data
    });
}

