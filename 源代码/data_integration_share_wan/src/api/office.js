import request from '@/utils/request'
// import Qs from 'qs'
// import store from '@/store'
// 查询树形机构
export function fetchOfficeTree() {
  return request({
    url: '/uac/office/tree',
    method: 'get'
  })
}
// 查询机构列表
export function fetchOfficeList() {
  return request({
    url: '/uac/office/all',
    method: 'get'
  })
}
// 查询机构列表
export function getOfficeInfoById(data) {
  return request({
    url: '/uac/office/' + data,
    method: 'get'
  })
}

// 添加机构
export function createOffice(data) {
  return request({
    // transformRequest: [data => Qs.stringify(data)],
    datatype: 'json',
    url: '/uac/office',
    method: 'post',
    data
  })
}

// 更新机构
export function updateOffice(data) {
  return request({
    // transformRequest: [data => Qs.stringify(data)],
    datatype: 'json',
    url: '/uac/office',
    method: 'put',
    data
  })
}

// 删除机构
export function deleteOffice(data) {
  return request({
    url: '/uac/office/' + data,
    method: 'delete'
  })
}
// 获取用户信息列表
export function fetchUserList(query) {
  return request({
    datatype: 'json',
    url: '/uac/user',
    method: 'get',
    params: {
      pageSize: query.size,
      pageNum: query.page,
      loginName: query.loginName,
      name: query.name,
      phone: query.phone,
      'uacOffice.id': query.uacOffice.id
    }
  })
}

// 通过角色id查询其机构
export function fetchOfficeById(data) {
  return request({
    url: '/uac/office/role/' + data,
    method: 'get'
  })
}
