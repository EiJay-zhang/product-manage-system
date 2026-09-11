import request from '@/utils/request'

export function listPurchase(query) {
  return request({ url: '/pms/purchase/list', method: 'get', params: query })
}
export function getPurchase(purchaseId) {
  return request({ url: '/pms/purchase/' + purchaseId, method: 'get' })
}
export function addPurchase(data) {
  return request({ url: '/pms/purchase', method: 'post', data })
}
