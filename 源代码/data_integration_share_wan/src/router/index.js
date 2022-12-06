import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'profile', icon: 'user', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // {
  //   path: '/permission',
  //   component: Layout,
  //   redirect: '/permission/page',
  //   alwaysShow: true, // will always show the root menu
  //   name: 'Permission',
  //   meta: {
  //     title: 'permission',
  //     icon: 'lock',
  //     roles: ['admin', 'editor'] // you can set roles in root nav
  //   },
  //   children: [
  //     {
  //       path: 'page',
  //       component: () => import('@/views/permission/page'),
  //       name: 'PagePermission',
  //       meta: {
  //         title: 'pagePermission',
  //         roles: ['admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: 'directive',
  //       component: () => import('@/views/permission/directive'),
  //       name: 'DirectivePermission',
  //       meta: {
  //         title: 'directivePermission'
  //         // if do not set roles, means: this page does not require permission
  //       }
  //     },
  //     {
  //       path: 'role',
  //       component: () => import('@/views/permission/role'),
  //       name: 'RolePermission',
  //       meta: {
  //         title: 'rolePermission',
  //         roles: ['admin']
  //       }
  //     }
  //   ]
  // },
  {
    path: '/org_mgmt',
    component: Layout,
    redirect: 'noredirect',
    name: 'org_mgmt',
    meta: {
      title: '系统管理',
      icon: 'menu',
      noCache: true,
      roles: ['admin']
    },
    children: [
      // {
      //   path: 'menu_mgmt',
      //   component: () => import('@/views/org_mgmt/menu_mgmt'),
      //   name: 'menu_mgmt',
      //   meta: {
      //     title: '菜单管理',
      //     icon: 'menu',
      //     noCache: true
      //   }
      // },
      {
        path: 'user_mgmt',
        component: () => import('@/views/org_mgmt/user_mgmt'),
        name: 'user_mgmt',
        meta: {
          title: '用户管理',
          icon: 'user',
          noCache: true
        }
      },
      {
        path: 'org_mgmt',
        component: () => import('@/views/org_mgmt/org_mgmt'),
        name: 'org_mgmt',
        meta: {
          title: '机构管理',
          icon: 'people',
          noCache: true
        }
      },
      {
        path: 'permission',
        component: () => import('@/views/org_mgmt/permission'),
        name: 'permission',
        meta: {
          title: '角色管理',
          icon: 'peoples',
          noCache: true
        }
      }
    ]
  },

  /** when your routing map is too long, you can split it into small modules **/
  // {
  //   path: '/example',
  //   component: Layout,
  //   redirect: '/example/list',
  //   name: 'Example',
  //   meta: {
  //     title: 'example',
  //     icon: 'example'
  //   },
  //   children: [
  //     {
  //       path: 'create',
  //       component: () => import('@/views/example/create'),
  //       name: 'CreateArticle',
  //       meta: { title: 'createArticle', icon: 'edit' }
  //     },
  //     {
  //       path: 'edit/:id(\\d+)',
  //       component: () => import('@/views/example/edit'),
  //       name: 'EditArticle',
  //       meta: { title: 'editArticle', noCache: true, activeMenu: '/example/list' },
  //       hidden: true
  //     },
  //     {
  //       path: 'list',
  //       component: () => import('@/views/example/list'),
  //       name: 'ArticleList',
  //       meta: { title: 'articleList', icon: 'list' }
  //     }
  //   ]
  // },

  // {
  //   path: '/excel',
  //   component: Layout,
  //   redirect: '/excel/export-excel',
  //   name: 'Excel',
  //   meta: {
  //     title: 'excel',
  //     icon: 'excel'
  //   },
  //   children: [
  //     {
  //       path: 'export-excel',
  //       component: () => import('@/views/excel/export-excel'),
  //       name: 'ExportExcel',
  //       meta: { title: 'exportExcel' }
  //     },
  //     {
  //       path: 'export-selected-excel',
  //       component: () => import('@/views/excel/select-excel'),
  //       name: 'SelectExcel',
  //       meta: { title: 'selectExcel' }
  //     },
  //     {
  //       path: 'export-merge-header',
  //       component: () => import('@/views/excel/merge-header'),
  //       name: 'MergeHeader',
  //       meta: { title: 'mergeHeader' }
  //     },
  //     {
  //       path: 'upload-excel',
  //       component: () => import('@/views/excel/upload-excel'),
  //       name: 'UploadExcel',
  //       meta: { title: 'uploadExcel' }
  //     }
  //   ]
  // },
  // {
  //   path: '/tem',
  //   component: Layout,
  //   redirect: 'noredirect',
  //   meta: {
  //     title: '示例',
  //     icon: 'documentation'
  //   },
  //   children: [{
  //     path: 'list',
  //     name: 'list',
  //     component: () => import('@/views/tem/list'),
  //     meta: {
  //       title: '列表',
  //       icon: 'documentation'
  //     }
  //   },
  //   {
  //     path: 'form',
  //     name: 'form',
  //     component: () => import('@/views/tem/form'),
  //     meta: {
  //       title: '添加',
  //       noCache: true
  //     }
  //   }
  //   ]
  // },
  {
    path: '/event',
    component: Layout,
    redirect: 'noRedirect',
    meta: {
      title: '事件管理',
      icon: 'event'
    },
    children: [
      {
        path: 'all_event',
        name: 'all_event',
        component: () => import('@/views/event/all_event'),
        meta: {
          title: '全部事件',
          icon: 'AllEvent',
          roles: ['admin', 'audit']
        }
      },
      {
        path: 'my_event',
        name: 'my_event',
        component: () => import('@/views/event/my_event'),
        meta: {
          title: '我的事件',
          icon: 'MyEvent',
          roles: ['admin', 'submit', 'deal']
        }
      }
    ]
  },

  // {
  //   path: '/visualization',
  //   component: Layout,
  //   redirect: 'noRedirect',
  //   meta: {
  //     title: '可视化大屏',
  //     icon: 'documentation',
  //     roles: ['admin', 'audit']
  //   },
  //   children: [
  //     {
  //       path: 'vis',
  //       name: 'vis',
  //       component: () => import('@/views/visualization/informationVis'),
  //       meta: {
  //         title: '可视化大屏',
  //         icon: 'documentation'
  //       }
  //     }
  //   ]
  // },

  {
    path: '/vis',
    component: Layout,
    redirect: 'noRedirect',
    meta: {
      title: '可视化大屏',
      icon: 'documentation',
      roles: ['admin', 'audit','submit','deal']
    },
    children: [
      {
        path: 'vis',
        name: 'vis',
        component: () => import('@/views/vis/index'),
        meta: {
          title: '可视化大屏',
          icon: 'documentation'
        }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
