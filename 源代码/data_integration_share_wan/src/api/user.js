import request from '@/utils/request'
import Qs from 'qs'
// import store from '@/store'
export function login(data) {
  return request({
    url: '/vue-element-admin/user/login',
    method: 'post',
    data
  })
}

export function loginByUsername(username, password, imageCode, randomStr) {
  const data = {
    username,
    password,
    imageCode,
    randomStr
  }
  return request({
    // headers: {
    //      'Content-Type': 'application/x-www-form-urlencoded'
    // },
    // transformRequest: [data => Qs.stringify(data)],
    // url: '/login/login',
    // method: 'post',
    // data
    headers: {
      client_id: 'webapp',
      client_secret: 'webapp'
    },
    withCredentials: false,
    url: '/login',
    method: 'post',
    data: Qs.stringify(data)
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-element-admin/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}

// export function logout() {
//   return request({
//     url: '/login/logout',
//     method: 'post'
//   })
// }

//  查询用户列表
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

//  通过角色id查询所有用户
export function getUseByRoleId(data) {
  return request({
    datatype: 'json',
    url: '/uac/user/role/' + data.roleId,
    method: 'get',
    params: {
      loginName: data.loginName,
      name: data.name,
      phone: data.phone,
      pageNum: data.page,
      pageSize: data.size
    }

  })
}

//  根据id获取用户信息
export function getUserInfoById(data) {
  return request({
    contentType: 'application/json; charset=utf-8',
    transformRequest: [data => Qs.stringify(data)],
    url: '/uac/user/' + data,
    method: 'get'
  })
}

//  添加用户
export function createUser(data) {
  return request({
    // url: '/article/list',
    // method: 'get',
    // params: query
    // contentType:'application/json; charset=utf-8',
    // emulateJSON: false,
    // traditional: true,
    // transformRequest: [data => Qs.stringify(data)],
    datatype: 'json',
    url: '/uac/user',
    method: 'post',
    data: {
      'loginName': data.loginName,
      'loginPwd': data.loginPwd,
      'confirmPwd': data.confirmPwd,
      'userCode': data.userCode,
      'name': data.name,
      'phone': data.phone,
      'email': data.email,
      'userSource': data.userSource,
      'status': data.status,
      'photo': data.photo,
      'roleList': data.roleList,
      'officeList': data.officeList,
      'remarks': data.remarks
    }
  })
}

//  更新用户
export function updateUser(data) {
  return request({
    datatype: 'json',
    url: '/uac/user',
    method: 'put',
    headers: {
      client_id: 'webapp',
      client_secret: 'webapp'
    },
    data: {
      'id': data.id,
      'loginName': data.loginName,
      'loginPwd': data.loginPwd,
      'confirmPwd': data.confirmPwd,
      'userCode': data.userCode,
      'name': data.name,
      'phone': data.phone,
      'email': data.email,
      'userSource': data.userSource,
      'status': data.status,
      'photo': data.photo,
      'roleList': data.roleList,
      'officeList': data.officeList,
      'remarks': data.remarks
    }
  })
}

export function updateUserSelf(data) {
  return request({
    datatype: 'json',
    url: '/uac/user/self',
    method: 'put',
    headers: {
      client_id: 'webapp',
      client_secret: 'webapp'
    },
    data: {
      'id': data.id,
      'loginName': data.loginName,
      'loginPwd': data.loginPwd,
      'confirmPwd': data.confirmPwd,
      'userCode': data.userCode,
      'name': data.name,
      'phone': data.phone,
      'email': data.email,
      'userSource': data.userSource,
      'status': data.status,
      'photo': data.photo,
      'roleList': data.roleList,
      'officeList': data.officeList,
      'remarks': data.remarks
    }
  })
}

//  当前用户修改信息
export function updateUserInfo(data) {
  return request({
    datatype: 'json',
    url: '/SysUser/info',
    method: 'post',
    data
  })
}

//  当前用户修改密码
export function updatePassword(data) {
  return request({
    // datatype:'json',
    contentType: 'application/json; charset=utf-8',
    transformRequest: [data => Qs.stringify(data)],
    url: '/SysUser/modifyPwd',
    method: 'post',
    data
  })
}

//  删除用户
export function deleteLogicalUser(data) {
  return request({
    contentType: 'application/json; charset=utf-8',
    transformRequest: [data => Qs.stringify(data)],
    url: '/uac/user/' + data,
    method: 'delete'
  })
}

//  批量删除用户
export function deleteUserInBatches(data) {
  return request({
    datatype: 'json',
    url: '/uac/user',
    method: 'delete',
    data
  })
}

//  更改状态
export function userModifyStatus(data) {
  return request({
    datatype: 'json',
    url: '/SysUser/loginFlag',
    method: 'post',
    data: {
      'id': data.id,
      'loginFlag': data.loginFlag
    }
  })
}

// 添加用户到角色
export function addUserToRole(data) {
  return request({
    datatype: 'json',
    url: '/uac/role/saveUser/',
    method: 'post',
    data: {
      'roleId': data
    }
  })
}

export function getSelf() {
  return request({
    url: '/uac/user/self',
    method: 'get'
  })
}

//  更新用户
// export function updateUser(data) {
//   return request({
//     datatype: 'json',
//     url: '/userac/uac/user',
//     method: 'put',
//     data: {
//       'id': data.id,
//       'loginName': data.loginName,
//       'loginPwd': data.loginPwd,
//       'confirmPwd': data.confirmPwd,
//       'userCode': data.userCode,
//       'name': data.name,
//       'phone': data.phone,
//       'email': data.email,
//       'userSource': data.userSource,
//       'status': data.status,
//       'photo': data.photo,
//       'roleList': data.roleList,
//       'officeList': data.officeList,
//       'remarks': data.remarks
//     }
//   })
// }

// 修改当前用户密码
// export function updatePassword(data) {
//   return request({
//     datatype: 'json',
//     url: '/userac/uac/user/changePassword',
//     method: 'post',
//     data: {
//       'userId': data.id,
//       'forgetPassword': true,
//       'passwordOld': data.passwordOld,
//       'passwordNew': data.passwordNew
//       // 'resetPasswordDto': data
//     }
//   })
// }

export function getUserInfo() {
  return request({
    datatype: 'json',
    url: '/uac/user/self',
    method: 'get'
  })
}

