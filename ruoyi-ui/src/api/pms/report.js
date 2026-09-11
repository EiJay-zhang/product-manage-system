import request from '@/utils/request'

export function reportOverview(query) {
  return request({ url: '/pms/report/overview', method: 'get', params: query })
}
export function reportPurchase(query) {
  return request({ url: '/pms/report/purchase', method: 'get', params: query })
}
export function reportStock(query) {
  return request({ url: '/pms/report/stock', method: 'get', params: query })
}
export function reportCost(query) {
  return request({ url: '/pms/report/cost', method: 'get', params: query })
}
export function reportSupplier(query) {
  return request({ url: '/pms/report/supplier', method: 'get', params: query })
}
export function reportTurnover(query) {
  return request({ url: '/pms/report/turnover', method: 'get', params: query })
}
export function reportTrend(query) {
  return request({ url: '/pms/report/turnover/trend', method: 'get', params: query })
}
export function reportRank(query) {
  return request({ url: '/pms/report/turnover/rank', method: 'get', params: query })
}
export function reportCompare(query) {
  return request({ url: '/pms/report/turnover/compare', method: 'get', params: query })
}
export function reportDetail(query) {
  return request({ url: '/pms/report/turnover/detail', method: 'get', params: query })
}
