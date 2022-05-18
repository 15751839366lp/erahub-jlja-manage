import service from "../../../utils/request";

export function publish(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function detail(url) {
    return service.request({
        url: url,
        method: 'get'
    });
}

export function remove(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function deleteOutStock(url) {
    return service.request({
        url: url,
        method: 'get'
    });
}

export function back(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function findOutStockList(params) {
    return service.request({
        url: "/business/material/outStock/findOutStockList",
        method: 'get',
        params: params
    });
}

export function addOutStock(data) {
    return service.request({
        url: "/business/material/outStock/addOutStock",
        method: 'post',
        data
    });
}