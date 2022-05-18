import service from "../../../utils/request";

export function getProvinces(data) {
    return service.request({
        url: '/json/provinces.json',
        method: 'get'
    });
}