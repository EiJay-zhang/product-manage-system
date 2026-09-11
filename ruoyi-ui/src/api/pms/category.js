import request from '@/utils/request'

export function listCategory(query) {
  return request({ url: '/pms/category/list', method: 'get', params: query })
}
export function optionCategory() {
  return request({ url: '/pms/category/optionselect', method: 'get' })
}
export function getCategory(categoryId) {
  return request({ url: '/pms/category/' + categoryId, method: 'get' })
}
export function addCategory(data) {
  return request({ url: '/pms/category', method: 'post', data })
}
export function updateCategory(data) {
  return request({ url: '/pms/category', method: 'put', data })
}
export function delCategory(ids) {
  return request({ url: '/pms/category/' + ids, method: 'delete' })
}
