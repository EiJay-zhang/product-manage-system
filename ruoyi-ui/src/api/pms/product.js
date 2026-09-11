import request from '@/utils/request'

export function listProduct(query) {
  return request({ url: '/pms/product/list', method: 'get', params: query })
}
export function optionProduct(query) {
  return request({ url: '/pms/product/optionselect', method: 'get', params: query })
}
export function getProduct(productId) {
  return request({ url: '/pms/product/' + productId, method: 'get' })
}
export function addProduct(data) {
  return request({ url: '/pms/product', method: 'post', data })
}
export function updateProduct(data) {
  return request({ url: '/pms/product', method: 'put', data })
}
export function delProduct(productIds) {
  return request({ url: '/pms/product/' + productIds, method: 'delete' })
}
export function importProduct(data) {
  return request({ url: '/pms/product/importData', method: 'post', data })
}
