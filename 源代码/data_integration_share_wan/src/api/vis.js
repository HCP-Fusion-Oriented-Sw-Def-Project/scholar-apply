import request from '@/utils/request'

// 一次拿所有的数据 /riskAllInfo
export function getRiskAllInfo() {
  return request({
    url: '/static/riskAllInfo',
    method: 'get'
  })
}

// 风险总数 GET /static/riskTotal
export function getRiskTotal() {
  return request({
    url: '/static/riskTotal',
    method: 'get'
  })
}
// 确认数目/static/confirmed
export function getConfirmed() {
  return request({
    url: '/static/confirmed',
    method: 'get'
  })
}
// 待确认数目
// export function getRiskTotal() {
//   return request({
//     url: '/static/riskTotal',
//     method: 'get',
//   })
// }
// 无需处理 /static/noRequired
export function getNoRequired() {
  return request({
    url: '/static/noRequired',
    method: 'get'
  })
}
// 协同处理 /static/requiredCoordinationTotal
export function getRequiredCoordinationTotal() {
  return request({
    url: '/static/requiredCoordinationTotal',
    method: 'get'
  })
}
// 正在协同 /static/coordinating
export function getCoordinating() {
  return request({
    url: '/static/coordinating',
    method: 'get'
  })
}
// 完成协同 /static/coordinated
export function getCoordinated() {
  return request({
    url: '/static/coordinated',
    method: 'get'
  })
}
// 平均处理时间 /static/averageProcessTime
export function getAverageProcessTime() {
  return request({
    url: '/static/averageProcessTime',
    method: 'get'
  })
}

// 最多的10个单位
export function getMostSubmitDept() {
  return request({
    url: '/static/mostSubmitDept',
    method: 'get'
  })
}

// 最新的10条数据
export function getAllEventBeside() {
  return request({
    url: '/eventMessage/all',
    method: 'get'
  })
}

// 总趋势   /static/allTimeSubmit
export function getAllTimeSubmit() {
  return request({
    url: '/static/allTimeSubmit',
    method: 'get'
  })
}

// 近一天趋势 /static/oneDaySubmit
export function getAneDaySubmit() {
  return request({
    url: '/static/oneDaySubmit',
    method: 'get'
  })
}

// 近一日趋势 /static/oneHourSubmit
export function getAneHourSubmit() {
  return request({
    url: '/static/oneHourSubmit',
    method: 'get'
  })
}

// 安全级别 /static/securityLevelProportion
export function getSecurityLevelProportion() {
  return request({
    url: '/static/securityLevelProportion',
    method: 'get'
  })
}

// 影响范围 /static/influenceScopeProportion
export function getInfluenceScopeProportion() {
  return request({
    url: '/static/influenceScopeProportion',
    method: 'get'
  })
}

export function getAllEvent(data) {
  return request({
    url: '/cooperationEvent',
    method: 'get',
    params: data
  })
}
