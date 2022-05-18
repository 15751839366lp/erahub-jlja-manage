import service from "../../../utils/request";

export function findProducts(params) {
    return service.request({
        url: '/business/material/product/findProducts',
        method: 'get',
        params: params
    });
}

export function publish(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function deleteProduct(url) {
    return service.request({
        url: url,
        method: 'delete'
    });
}

export function update(url,data) {
    return service.request({
        url: url,
        method: 'put',
        data
    });
}

export function edit(url) {
    return service.request({
        url: url,
        method: 'get'
    });
}

export function add(data) {
    return service.request({
        url: "/business/material/product/add",
        method: 'post',
        data
    });
}

export function remove(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function back(url) {
    return service.request({
        url: url,
        method: 'put'
    });
}

export function findProductList(params) {
    return service.request({
        url: "/business/material/product/findProductList",
        method: 'get',
        params: params
    });
}

export function findProductStocks(params) {
    return service.request({
        url: "/business/material/product/findProductStocks",
        method: 'get',
        params: params
    });
}

export function findAllStocks(params) {
    return service.request({
        url: "/business/material/product/findAllStocks",
        method: 'get',
        params: params
    });
}