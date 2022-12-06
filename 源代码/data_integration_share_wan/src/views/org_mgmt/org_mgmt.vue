<template>
  <div class="app-container">
    <div style="margin-bottom: 10px;margin-top: 0px">
      <el-button
        size="small"
        icon="el-icon-plus"
        type="primary"
        @click="handleCreate"
      >添加</el-button>
      <!-- <div style="float:right">
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          @click="handleFilter"
          size="small"
        >查找</el-button>
        <el-button
          class="filter-item"
          type="info"
          icon="el-icon-refresh"
          @click="resetListQuery"
          size="small"
        >重置</el-button>
      </div>-->
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      fit
      highlight-current-row
      :height="fullHeight-150"
      :indent="8"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column
        min-width="65px"
        align="left"
        label="机构名称"
        width="250"
      >
        <template slot-scope="scope">
          <span class="link-type">{{ scope.row.name }}</span>
        </template>
      </el-table-column>>
      <el-table-column
        label="排序号"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.sort }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="机构类型"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <span>{{ typeOptions[scope.row.type-1].label }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="更新时间" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.time | formatTimes }}</span>
        </template>
      </el-table-column>-->
      <!-- <el-table-column label="备注" align="center" >
        <template slot-scope="scope">
          <span>{{scope.row.remarks }}</span>
        </template>
      </el-table-column>-->
      <el-table-column
        label="操作"
        width="400"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            style="margin-left:10px"
            @click="handleUpdate(scope.row)"
          >编辑</el-button>
          <el-popover
            placement="top"
            width="160"
            trigger="click"
          >
            <p>确定删除吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button
                size="mini"
                type="text"
                @click="cancelPopover(scope.row, scope.$index)"
              >取消</el-button>
              <el-button
                type="primary"
                size="mini"
                @click="cancelPopover(scope.row, scope.$index),handleDelete(scope.row)"
              >确定</el-button>
            </div>
            <el-button
              :ref="'btn'+scope.$index"
              slot="reference"
              size="mini"
              type="danger"
              style="margin-left:10px"
            >删除</el-button>
          </el-popover>

          <el-button
            type="primary"
            size="mini"
            width="100px"
            style="margin-left:10px"
            @click="createSubOffice(scope.row)"
          >添加下级机构</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-dialogDrag
      :title="textMap[dialogStatus]"
      width="60%"
      :visible.sync="dialogFormVisible"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="right"
        label-width="100px"
        style=" margin-left:50px;margin-right:50px; height:350px; "
      >
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="上级机构 :"
              prop="parentOffice"
            >
              <el-input
                v-model="temp.parentOffice"
                class="filter-item"
                @focus="officeDialogFormVisible=true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="机构名称 :"
              prop="name"
            >
              <el-input v-model="temp.name" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-form-item label="机构代码 :" prop="id">
          <el-input v-model="temp.id" style ='width:400px' ></el-input>
        </el-form-item>-->
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="排序号 :"
              prop="sort"
            >
              <el-input
                v-model="temp.sort"
                maxlength="10"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="机构级别 :"
              prop="grade"
            >
              <el-input
                v-model="temp.grade"
                maxlength="10"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="机构类型 :"
              prop="type"
            >
              <el-select
                v-model="temp.type"
                class="filter-item"
                clearable
                placeholder="类型"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="邮政编码："
              prop="zipCode"
            >
              <el-input v-model.number="temp.zipCode" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="10">
            <el-form-item label="机构编码："
                          prop="zipCode">
              <el-input v-model="temp.zipCode"></el-input>
            </el-form-item>
          </el-col> -->
        </el-row>

        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="主负责人："
              prop="primaryPersonName"
            >
              <el-input
                v-model="temp.primaryPersonName"
                class="filter-item"
                @focus="getResUser();priOrdep=true"
              />
            </el-form-item>
          </el-col>

          <el-col :span="10">
            <el-form-item
              label="副负责人："
              prop="deputyPersonName"
            >
              <el-input
                v-model="temp.deputyPersonName"
                class="filter-item"
                @focus="getResUser();priOrdep=false"
              />
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="传真："
              prop="fax"
            >
              <el-input v-model="temp.fax" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="联系地址："
              prop="address"
            >
              <el-input v-model="temp.address" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="邮箱："
              prop="email"
            >
              <el-input v-model="temp.email" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="电话："
              prop="phone"
            >
              <el-input v-model="temp.phone" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="10" />
          <el-col :span="10" />
        </el-row>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button
          v-if="dialogStatus=='create'"
          :disabled="submitSuccess"
          type="primary"
          @click="createData"
        >确认</el-button>
        <el-button
          v-else
          type="primary"
          :disabled="submitSuccess"
          @click="updateData"
        >确认</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      title="选择机构"
      :visible.sync="officeDialogFormVisible"
      width="400px"
      top="7vh"
    >
      <p>组织机构</p>
      <hr>
      <el-tree
        ref="tree1"
        :data="list1"
        :props="defaultProps"
      />
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="officeDialogFormVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="getCurrentNodeOffice(),officeDialogFormVisible = false"
        >确认</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      title="选择负责人"
      :visible.sync="personDialogFormVisible"
      width="400px"
      top="7vh"
    >
      <p>负责人</p>
      <hr>
      <el-tree
        ref="tree2"
        :data="listUser"
        :props="defaultProps"
      />
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="personDialogFormVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="getCurrentNodePerson(),personDialogFormVisible = false"
        >确认</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      title="添加负责人"
      :visible.sync="dialogUserFormVisible"
      width="1200px"
      top="7vh"
      @close="dialogFormVisible = true;"
    >
      <div class="filter-container">
        <el-input
          v-model="userListQuery.loginName"
          style="width: 150px;"
          class="filter-item"
          placeholder="账号"
          @keyup.enter.native="handleFilterU"
        />
        <el-input
          v-model="userListQuery.name"
          style="width: 150px;"
          class="filter-item"
          placeholder="姓名"
          @keyup.enter.native="handleFilterU"
        />
        <el-input
          v-model="userListQuery.phone"
          style="width: 100px;"
          class="filter-item"
          placeholder="电话"
          @keyup.enter.native="handleFilterU"
        />
        <el-button
          v-waves
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          @click="handleFilterU"
        >查找</el-button>
        <el-button
          v-waves
          class="filter-item"
          type="info"
          icon="el-icon-refresh"
          @click="resetUserQuery"
        >重置</el-button>
      </div>
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="allUserList"
        element-loading-text="给我一点时间"
        border
        fit
        highlight-current-row
        style="width: 100%"
        @current-change="handleUserSelectionChange"
      >
        <el-table-column
          min-width="65px"
          label="用户账号"
        >
          <template slot-scope="scope">
            <span class="link-type">{{ scope.row.loginName }}</span>
          </template>
        </el-table-column>
        <el-table-column
          width="110px"
          align="center"
          label="用户姓名"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="邮箱"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="电话"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column
          width="150px"
          align="center"
          label="更新日期"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.updateDate | formatTimes }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          background
          :current-page="userListQuery.page"
          :page-sizes="[5,10,20,30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :page-size="userListQuery.size"
          :total="userTotal"
          :index="indexMethod2"
          @size-change="handleSizeChangeU"
          @current-change="handleCurrentChangeU"
        />
      </div>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogUserFormVisible = false;dialogFormVisible = true">取消</el-button>
        <el-button
          type="primary"
          :disabled="submitSuccess"
          @click="handleAuthorizeUsers"
        >确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import treeTable from '@/components/TreeTable'
// import treeToArray from './customEval'
// 可拖拽布局
// import VueDragResize from 'vue-drag-resize'
import waves from '@/directive/waves' // 水波纹指令
import {
  fetchOfficeList,
  fetchOfficeTree,
  updateOffice,
  createOffice,
  deleteOffice,
  fetchUserList
} from '@/api/office'
import { parseTime, arrayToTree2 } from '@/utils/index'

export default {
  name: 'TreeTableDemo',
  directives: {
    waves
  },
  filters: {
    formatTimes(updateDate) {
      const y = new Date(updateDate).getFullYear()
      const m = new Date(updateDate).getMonth() + 1
      const d = new Date(updateDate).getDate()
      const h = new Date(updateDate).getHours()
      const i = new Date(updateDate).getMinutes()
      const s = new Date(updateDate).getSeconds()

      var t = y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s
      // var t = Y + '-' + m + '-' + d;
      return t
    }
  },
  // components: { treeTable },
  // computed: {
  //   getParentOfficeOptions() {
  //     let parentOfficeOptions = this.list.map((v)=>{ return {
  //         label
  //     };
  //   });
  //     for(const v of this.list){
  //       v.label = v.name;
  //       this.parentOfficeOptions.push(v);
  //     }
  //     return  arrayToTree2(this.parentOfficeOptions)
  //   }
  // }
  data() {
    var validateSort = (rule, value, callback) => {
      if (!/^\d{0,10}$/.test(value)) {
        callback(new Error(' 请输入十位数字'))
      } else {
        callback()
      }
    }
    var validatePri = (rule, value, callback) => {
      if (!this.temp.primaryPersonName) {
        callback(new Error('请选择主要负责人'))
      } else {
        callback()
      }
    }
    var validateDep = (rule, value, callback) => {
      if (!this.temp.deputyPersonName) {
        callback(new Error('请选择副负责人'))
      } else {
        callback()
      }
    }
    return {
      delVisibles: [],
      userTotal: 0,
      expandAll: false,
      tableKey: 0,
      args: [null, null, 'timeLine'],
      allUserList: [],
      // func: treeToArray,
      list: [],
      // 上级机构
      list1: [],
      // 角色列表
      listUser: [],
      // 主负责人or副负责人
      priOrdep: true,
      tempList: [],
      columns: [
        {
          text: '机构名称',
          value: 'name',
          width: '300'
        }
      ],
      listQuery: {
        id: '',
        name: '',
        type: ''
      },
      typeOptions: [
        {
          label: '省级公司',
          key: '1'
        },
        {
          label: '市级公司',
          key: '2'
        },
        {
          label: '部门',
          key: '3'
        }
      ],
      defaultProps: {
        label: function(a) {
          return a.name
        }
      },
      parentOfficeOptions: [],
      textMap: {
        update: '编辑',
        create: '添加'
      },
      temp: {
        upper: '',
        name: '',
        id: '',
        number: '',
        type: '',
        parentOffice: '',
        parentId: '',
        grade: ''
      },
      userListQuery: {
        page: 1,
        size: 10,
        loginName: '',
        name: '',
        phone: '',
        uacOffice: {
          id: '',
          name: ''
        }
      },
      multipleSelection: null,
      dialogFormVisible: false,
      delConfirmvisible: false,
      officeDialogFormVisible: false,
      personDialogFormVisible: false,
      dialogUserFormVisible: false,
      dialogStatus: '',
      //  避免重复提交
      submitSuccess: false,
      rules: {
        type: [
          { required: true, message: '请输入机构类型', trigger: 'change' }
        ],
        // timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        name: [{ required: true, message: '请输入机构名称', trigger: ['blur', 'change'] }],
        parentOffice: [{ required: true, message: '请输入上级机构', trigger: 'change' }],
        sort: [
          { required: true, message: '请输入排序号', trigger: 'blur' },
          { validator: validateSort, trigger: ['blur', 'change'] }
        ],
        grade: [{ required: true, message: '请输入组织等级', trigger: ['blur', 'change'] }],
        primaryPersonName: [
          { required: true, validator: validatePri, trigger: 'change' }
        ],
        deputyPersonName: [
          { required: true, validator: validateDep, trigger: 'change' }
        ]
        // type: [{ required: true, message: 'type is required', trigger: 'blur' }]
      },
      fullHeight: document.documentElement.clientHeight
    }
  },
  watch: {
    fullHeight(val) {
      // if(!this.timer) {
      this.fullHeight = val
      //   this.timer = true
      //   let that = this
      //   setTimeout(function (){
      //     that.timer = false
      //   },400)
      // }
    }
  },
  created() {
    this.getList()
    // 获取结构列表
    // 获取用户列表
    fetchUserList().then(res => {
      this.listUser = res.data.list
      console.log('负责人')
      console.log(this.listUser)
    })
  },
  mouted() {
    const that = this
    window.onresize = () => {
      return (() => {
        window.clientHeight = document.body.clientHeight
        that.fullHeight = window.clientHeight
      })()
    }
  },
  methods: {
    loadUsers(node, resolve) {
      resolve(node)
    },
    handleFilterU() {
      this.userListQuery.page = 1
      if (this.userListQuery.loginName === '' && this.userListQuery.name === '' && this.userListQuery.phone === '') {
        this.$message({
          message: '请输入查找的相关信息!',
          type: 'warning'
        })
      } else {
        // var _list = []
        // for (const v of this.allUserList) {
        //   if (v.loginName.indexOf(this.userListQuery.loginName) >= 0 && v.name.indexOf(this.userListQuery.name) >= 0 && v.phone.indexOf(this.userListQuery.phone) >= 0) { _list.push(v) }
        // }
        // if (!_list.length) {
        //   this.$message({
        //     message: '无该人员!',
        //     type: 'warning'
        //   })
        // } else {
        //   this.allUserList = _list
        // }
        this.getUserList()
      }
    },
    getUserList() {
      fetchUserList(this.userListQuery).then(response => {
        this.allUserList = response.data.list
        console.log(this.allUserList)
        // 获取 userList 所有元素的id
        this.userTotal = response.data.totalSize
      })
    },
    getResUser() {
      this.resetUserQuery()
      this.getUserList()

      this.multipleSelection = null
      this.dialogFormVisible = false
      this.dialogUserFormVisible = true
    },
    indexMethod2(index) {
      if (this.userListQuery.page === 1) {
        return index + 1
      } else {
        return index + (this.userListQuery.page - 1) * this.userListQuery.size + 1
      }
    },
    handleSizeChangeU(val) {
      console.log(val)
      this.userListQuery.size = val
      this.getUserList()
    },
    handleCurrentChangeU(val) {
      this.userListQuery.page = val
      this.getUserList()
    },
    // 获取机构列表
    getList() {
      this.listLoading = true
      fetchOfficeTree().then(response => {
        // this.parentOfficeOptions = this.list

        // element树形表格数据必须含有children字段,不能含有haschildren字段
        var _list1 = JSON.parse(
          JSON.stringify(response.data).replace(/child/g, 'children')
        )
        this.list1 = JSON.parse(
          JSON.stringify(_list1).replace(/hasChildren/g, 'hasChild')
        )
        this.list = this.list1[0].children
        // this.list = response.data.data[0]
        this.listLoading = false
      })

      fetchOfficeList().then(response => {
        this.tempList = response.data
        console.log(this.tempList)
      })
    },
    handleAuthorizeUsers() {
      if (this.multipleSelection === null) {
        this.$message({
          message: '请勾选用户!',
          type: 'warning'
        })
        return
      }

      const data = this.multipleSelection
      if (this.priOrdep) {
        this.temp.primaryPerson = data
        this.temp.primaryPersonName = data.name
      } else {
        this.temp.deputyPerson = data
        this.temp.deputyPersonName = data.name
      }
      this.dialogUserFormVisible = false
      this.dialogFormVisible = true
    },
    getCurrentNodeOffice() {
      const data = this.$refs.tree1.getCurrentNode()
      this.temp.parentOffice = data.name
      this.temp.parentId = data.id
    },
    getCurrentNodePerson() {
      const data = this.$refs.tree2.getCurrentNode()
      if (this.priOrdep) {
        console.log(data)
        this.temp.primaryPerson = data
        this.temp.primaryPersonName = data.name
      } else {
        this.temp.deputyPerson = data
        this.temp.deputyPersonName = data.name
      }
    },
    handleFilter() {
      // this.listQuery.page = 1
      this.getList()
    },
    // handleSizeChange(val) {
    //   this.listQuery.limit = val
    //   this.getList()
    // },
    // handleCurrentChange(val) {
    //   this.listQuery.page = val
    //   this.getList()
    // },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.temp = {
        parentOffice: '',
        parentId: '',
        sort: 0,
        name: '',
        id: '',
        number: '',
        type: '',
        delFlag: '0',
        grade: '',
        zipCode: '',
        useAble: '',
        primaryPerson: {},
        deputyPerson: {},
        address: '',
        phone: '',
        fax: '',
        email: '',
        primaryPersonName: '',
        deputyPersonName: ''
      }
    },
    resetListQuery() {
      this.listQuery = {
        id: '',
        name: '',
        type: ''
      }
      this.handleFilter()
    },
    resetUserListQuery() {
      this.userListQuery = {
        page: 1,
        size: 10,
        loginName: '',
        name: '',
        phone: '',
        uacOffice: {
          id: '',
          name: ''
        }
      }
    },
    resetUserQuery() {
      this.resetUserListQuery()
      this.getUserList()
    },
    cancelPopover(row, index) {
      // this.delVisibles.splice(index, 1, false)
      this.$refs['btn' + index].$el.click()
    },
    handleCreate() {
      this.resetTemp()
      // fetchOfficeList().then(response => {
      //   this.parentOfficeOptions = []
      //   for (const v of response.data.data.list) {
      //     v.label = v.name
      //     this.parentOfficeOptions.push(v)
      //   }
      //   this.parentOfficeOptions = arrayToTree2(this.parentOfficeOptions)
      // })
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.submitSuccess = false
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createSubOffice(row) {
      this.resetTemp()
      fetchOfficeList().then(response => {
        this.parentOfficeOptions = []
        console.log(response.data.list, 'sub')
        for (const v of response.data.list) {
          v.label = v.name
          this.parentOfficeOptions.push(v)
        }
        this.parentOfficeOptions = arrayToTree2(this.parentOfficeOptions)
      })
      const temps = Object.assign({}, row)
      this.temp.parentOffice = temps.name
      this.temp.parentId = temps.id
      this.dialogStatus = 'create'
      this.submitSuccess = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.submitSuccess = true
          const temp1 = {
            address: this.temp.address,
            deputyPersonId: this.temp.deputyPerson.id,
            email: this.temp.email,
            fax: this.temp.fax,
            name: this.temp.name,
            parentId: this.temp.parentId,
            phone: this.temp.phone,
            primaryPersonId: this.temp.primaryPerson.id,
            sort: this.temp.sort,
            type: this.temp.type,
            useAble: this.temp.useAble,
            zipCode: this.temp.zipCode
          }
          console.log(temp1, 'tianjia')
          createOffice(temp1).then(response => {
            // if(response.data.data){
            this.dialogFormVisible = false
            this.handleFilter()
            this.$notify({
              title: '成功',
              message: response.msg,
              type: 'success',
              duration: 2000
            })
            this.getList()
            // }
            // else{
            //   this.$message('添加失败')
            // }
          }).catch(error => {
            console.log(error)
            this.submitSuccess = false
          }
          )
        }
      })
    },
    handleUpdate(row) {
      console.log(row)
      this.temp = Object.assign({}, row) // copy obj
      // this.getUserList()
      console.log(this.temp, this.tempList, '编辑')
      // this.temp.parentOffice="该机构为最高级机构"
      for (const v of this.tempList) {
        if (v.id === this.temp.parentId) {
          this.temp.parentOffice = v.name
          break
        }
      }
      this.temp.primaryPersonName = this.temp.primaryPerson.name
      this.temp.deputyPersonName = this.temp.deputyPerson.name
      this.dialogStatus = 'update'
      this.submitSuccess = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          for (const v of this.tempList) {
            if (this.temp.parentId === v.id) {
              if (v.parentIds.indexOf(this.temp.id) !== -1) {
                this.$notify({
                  title: '失败',
                  message: '自身子节点不可为自身上级',
                  type: 'error',
                  duration: 2000
                })
                return
              }
            }
          }

          this.submitSuccess = true
          const temp1 = {
            address: this.temp.address,
            deputyPersonId: this.temp.deputyPerson.id,
            email: this.temp.email,
            fax: this.temp.fax,
            name: this.temp.name,
            parentId: this.temp.parentId,
            phone: this.temp.phone,
            primaryPersonId: this.temp.primaryPerson.id,
            sort: this.temp.sort,
            type: this.temp.type,
            useAble: this.temp.useAble,
            zipCode: this.temp.zipCode,
            id: this.temp.id
          }
          console.log(temp1, 'gengxin')
          if (temp1.id === temp1.parentId) {
            this.$notify({
              title: '失败',
              message: '自身不可为自身上级',
              type: 'error',
              duration: 2000
            })
            this.submitSuccess = false
            return
          }
          updateOffice(temp1).then(response => {
            // updateTree(this.list, this.temp)
            // for (const v of this.list) {
            //   if (v.id === this.temp.id) {
            //     const index = this.list.indexOf(v)
            //     this.list.splice(index, 1, this.temp)
            //     break
            //   }
            // }
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: response.msg,
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(error => {
            console.log(error)
            this.submitSuccess = false
          }
          )
        }
      })
    },
    handleDelete(row) {
      this.temp = Object.assign({}, row)
      // var id = []
      // getTreeNode(this.temp, id)
      console.log(row.id)
      deleteOffice(row.id).then(response => {
        this.$notify({
          title: '成功',
          message: response.msg,
          type: 'success',
          duration: 2000
        })
        console.log(response)
        this.handleFilter()
        const index = this.list.indexOf(row)
        console.log(index)
        this.list.splice(index, 1)
      })
    },
    handleUserSelectionChange(val) {
      // const arr = val
      // this.multipleSelection = []
      // for (const v of arr) {
      //   this.multipleSelection.push(v.id)
      // }
      this.multipleSelection = val
      console.log(val)
    },
    // handleDownload() {
    //   this.downloadLoading = true
    //   import('@/vendor/Export2Excel').then(excel => {
    //     const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
    //     const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
    //     const data = this.formatJson(filterVal, this.list)
    //     excel.export_json_to_excel(tHeader, data, 'table-list')
    //     this.downloadLoading = false
    //   })
    // },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === 'timestamp') {
            return parseTime(v[j])
          } else {
            return v[j]
          }
        })
      )
    }
  }
}
</script>
<style >
</style>
