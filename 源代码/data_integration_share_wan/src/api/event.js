import request from '@/utils/request'

// event 事件
// solve 提交
// deal 协同详情  **处理 协同**
// 任务创建人 任务处理人
// 刚开始创建完就是待处理、当处理人点击人数小于总的人数是待协同、处理人总的人数时为待提交、任务的提交者

// 用户刚新建的事件，存在一个管理员要进行事件审核（处理）
export function easyLogin(data) {
  return request({
    url: '/cooperationUserinfo',
    method: 'get',
    params: data
  })
}

export function getAllEvent(data) {
  return request({
    url: '/cooperationEvent',
    method: 'get',
    params: data
  })
}

// 查看我的事件
export function getAllMyEvent() {
  return request({
    url: '/eventMessage/my',
    method: 'get'
  })
}

// 提交
export function submitMyEvent(id) {
  return request({
    url: '/eventMessage/submit/' + id,
    method: 'put'
  })
}

// 删除
export function deleteMyEvent(id) {
  return request({
    url: '/eventMessage/' + id,
    method: 'delete'
  })
}

// 全部事件
// 列表查询
export function getAllEventBeside(data) {
  return request({
    url: '/eventMessage/all',
    method: 'get',
    params: data
  })
}

// 单位协同处置
export function dealHelpEvent(data) {
  return request({
    url: '/eventMessage/coordinationProcess',
    method: 'post',
    data
  })
}

// 协同反馈
export function coordinationEvent(data) {
  return request({
    url: '/eventMessage/coordination',
    method: 'post',
    data
  })
}

export function addEvent(data) {
  return request({
    url: '/eventMessage',
    method: 'post',
    data
  })
}

export function updateEvent(data) {
  return request({
    url: '/eventMessage',
    method: 'put',
    data
  })
}

export function deleteEvent(id) {
  return request({
    url: '/cooperationEvent',
    method: 'delete',
    params: {
      id
    }
  })
}

// 查看协同详情/eventMessage/coordination/{id}
export function getDeal(id) {
  return request({
    url: '/eventMessage/coordination/' + id,
    method: 'get'
  })
}

export function addDeal(data) {
  return request({
    url: '/cooperationDeal/deal',
    method: 'get',
    params: data
  })
}

export function updateDeal(data) {
  return request({
    url: '/cooperationDeal',
    method: 'put',
    data
  })
}

export function deleteDeal(id) {
  return request({
    url: '/cooperationDeal',
    method: 'delete',
    params: {
      id
    }
  })
}

export function getDealById(id) {
  return request({
    url: '/cooperationDeal/' + id,
    method: 'get'
  })
}

export function getSolve() {
  return request({
    url: '/cooperationInnerSolve',
    method: 'get'
  })
}

export function addSolve(data) {
  return request({
    url: '/cooperationInnerSolve',
    method: 'post',
    data
  })
}

export function updateSolve(data) {
  return request({
    url: '/cooperationInnerSolve',
    method: 'put',
    data
  })
}

export function deleteSolve(id) {
  return request({
    url: '/cooperationInnerSolve',
    method: 'delete',
    params: {
      id
    }
  })
}
