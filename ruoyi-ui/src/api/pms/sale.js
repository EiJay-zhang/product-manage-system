import request from '@/utils/request'

export function listSale(query) {
  return request({ url: '/pms/sale/list', method: 'get', params: query })
}
export function getSale(saleId) {
  return request({ url: '/pms/sale/' + saleId, method: 'get' })
}
export function addSale(data) {
  return request({ url: '/pms/sale', method: 'post', data })
}
