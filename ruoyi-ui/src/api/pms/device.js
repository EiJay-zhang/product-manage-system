import request from '@/utils/request'

export function listDevice(query) {
  return request({ url: '/pms/device/list', method: 'get', params: query })
}
export function getDevice(deviceId) {
  return request({ url: '/pms/device/' + deviceId, method: 'get' })
}
export function bindDevice(data) {
  return request({ url: '/pms/device/bind', method: 'post', data })
}
export function unbindDevice(deviceId) {
  return request({ url: '/pms/device/unbind/' + deviceId, method: 'put' })
}
export function refreshDevice(deviceId) {
  return request({ url: '/pms/device/refresh/' + deviceId, method: 'put' })
}
export function restartDevice(deviceId) {
  return request({ url: '/pms/device/restart/' + deviceId, method: 'put' })
}
export function markDevice(data) {
  return request({ url: '/pms/device/mark/' + data.deviceId, method: 'put', data })
}
export function batchRefreshDevice(data) {
  return request({ url: '/pms/device/batchRefresh', method: 'post', data })
}
export function listSyncLog(query) {
  return request({ url: '/pms/device/syncLog/list', method: 'get', params: query })
}
export function getTemplate() {
  return request({ url: '/pms/device/template', method: 'get' })
}
export function saveTemplate(data) {
  return request({ url: '/pms/device/template', method: 'put', data })
}
