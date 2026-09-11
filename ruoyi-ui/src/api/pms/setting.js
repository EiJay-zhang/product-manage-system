import request from '@/utils/request'

export function getSetting() {
  return request({ url: '/pms/setting', method: 'get' })
}
export function saveSetting(data) {
  return request({ url: '/pms/setting', method: 'put', data })
}
