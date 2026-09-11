import request from '@/utils/request'

export function listStockLog(query) {
  return request({ url: '/pms/stock/log/list', method: 'get', params: query })
}
export function adjustStock(data) {
  return request({ url: '/pms/stock/adjust', method: 'put', data })
}
