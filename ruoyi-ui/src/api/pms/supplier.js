import request from '@/utils/request'

export function listSupplier(query) {
  return request({ url: '/pms/supplier/list', method: 'get', params: query })
}
export function optionSupplier() {
  return request({ url: '/pms/supplier/optionselect', method: 'get' })
}
export function getSupplier(supplierId) {
  return request({ url: '/pms/supplier/' + supplierId, method: 'get' })
}
export function addSupplier(data) {
  return request({ url: '/pms/supplier', method: 'post', data })
}
export function updateSupplier(data) {
  return request({ url: '/pms/supplier', method: 'put', data })
}
export function delSupplier(supplierIds) {
  return request({ url: '/pms/supplier/' + supplierIds, method: 'delete' })
}
export function listSupplierProducts(supplierId) {
  return request({ url: '/pms/supplier/' + supplierId + '/products', method: 'get' })
}
export function listSupplierPurchases(supplierId) {
  return request({ url: '/pms/supplier/' + supplierId + '/purchases', method: 'get' })
}
