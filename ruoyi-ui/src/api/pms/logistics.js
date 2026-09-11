import request from '@/utils/request'

export function listLogistics(query) {
  return request({ url: '/pms/logistics/list', method: 'get', params: query })
}
export function getLogistics(logisticsId) {
  return request({ url: '/pms/logistics/' + logisticsId, method: 'get' })
}
export function addLogistics(data) {
  return request({ url: '/pms/logistics', method: 'post', data })
}
export function updateLogistics(data) {
  return request({ url: '/pms/logistics', method: 'put', data })
}
export function updateLogisticsStatus(data) {
  return request({ url: '/pms/logistics/status', method: 'put', data })
}
export function markLogistics(data) {
  return request({ url: '/pms/logistics/mark', method: 'put', data })
}
export function batchReconcile(data) {
  return request({ url: '/pms/logistics/batchReconcile', method: 'post', data })
}
export function statsMonthly(query) {
  return request({ url: '/pms/logistics/stats/monthly', method: 'get', params: query })
}
export function statsCarrier(query) {
  return request({ url: '/pms/logistics/stats/carrier', method: 'get', params: query })
}
export function statsSupplier(query) {
  return request({ url: '/pms/logistics/stats/supplier', method: 'get', params: query })
}
