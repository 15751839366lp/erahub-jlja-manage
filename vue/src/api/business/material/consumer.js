import service from "../../../utils/request";

export function deleteConsumer(url) {
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
        url: '/business/material/consumer/add',
        method: 'post',
        data
    });
}

export function findConsumerList(params) {
    return service.request({
        url: '/business/material/consumer/findConsumerList',
        method: 'get',
        params: params
    });
}

export function findAll() {
    return service.request({
        url: '/business/material/consumer/findAll',
        method: 'get'
    });
}