import service from "../../../utils/request";

export function findAll() {
    return service.request({
        url: '/business/material/supplier/findAll',
        method: 'get'
    });
}

export function deleteSupplier(url) {
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
        url: '/business/material/supplier/add',
        method: 'post',
        data
    });
}

export function findSupplierList(params) {
    return service.request({
        url: '/business/material/supplier/findSupplierList',
        method: 'get',
        params: params
    });
}