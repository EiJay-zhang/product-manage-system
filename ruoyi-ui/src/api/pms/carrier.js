import request from '@/utils/request'

export function listCarrier(query) {
  return request({ url: '/pms/carrier/list', method: 'get', params: query })
}
export function optionCarrier() {
  return request({ url: '/pms/carrier/optionselect', method: 'get' })
}
export function getCarrier(carrierId) {
  return request({ url: '/pms/carrier/' + carrierId, method: 'get' })
}
export function addCarrier(data) {
  return request({ url: '/pms/carrier', method: 'post', data })
}
export function updateCarrier(data) {
  return request({ url: '/pms/carrier', method: 'put', data })
}
export function delCarrier(carrierIds) {
  return request({ url: '/pms/carrier/' + carrierIds, method: 'delete' })
}
export function listCarrierLogistics(carrierId) {
  return request({ url: '/pms/carrier/' + carrierId + '/logistics', method: 'get' })
}
