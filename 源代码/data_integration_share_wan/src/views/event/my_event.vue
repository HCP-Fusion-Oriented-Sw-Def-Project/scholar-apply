<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.eventName"
        class="filter-item"
        style="width: 250px"
        placeholder="申请项目名称"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.reportClass"
        class="filter-item"
        style="width: 250px"
        placeholder="上报学院"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.extInfo"
        placeholder="当前状态"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      >
        <el-option
          v-for="item in statusOption"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <!-- 中部，添加、批量删除等操作-->
    <div class="edit-container" style="overflow: hidden">
      <div class="filter-button" style="float: left">
        <el-button
          v-waves
          class="filter-item"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleCreate"
        >添加</el-button>
        <!-- <el-button
          v-waves
          class="filter-item"
          type="danger"
          icon="el-icon-delete"
          @click="resetListQuery"
          size="mini"
        >批量删除</el-button> -->
      </div>
      <div class="filter-button" style="float: right">
        <el-button
          v-waves
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleFilter"
        >查找</el-button>
        <el-button
          v-waves
          class="filter-item"
          type="info"
          icon="el-icon-refresh"
          size="mini"
          @click="resetListQuery"
        >重置</el-button>
      </div>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" />
      <el-table-column label="#" type="index" align="center" />
      <el-table-column align="center" label="申请项目名称" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column width="180" align="center" label="上报学院">
        <template slot-scope="scope">
          <span>{{ scope.row.reportDeptName }}</span>
        </template>
      </el-table-column>

      <el-table-column width="200" align="center" label="上报时间">
        <template slot-scope="scope">
          <span>{{ scope.row.reportDate | formatTime }}</span>
        </template>
      </el-table-column>

      <!-- <el-table-column width="150" align="center" label="产品或服务名称">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceName }}</span>
        </template>
      </el-table-column>

      <el-table-column width="120" align="center" label="安全级别">
        <template slot-scope="scope">
          <span>{{ scope.row.securityLevel }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="处理类型">
        <template slot-scope="scope">
          <span>{{ scope.row.processType }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="分发类型">
        <template slot-scope="scope">
          <span>{{ scope.row.distributionType }}</span>
        </template>
      </el-table-column> -->

      <el-table-column label="当前状态" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.status | statusFilter">
            {{ row.status | statusFilterName }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- <el-table-column min-width="300px" label="Title">
        <template slot-scope="{row}">
          <router-link :to="'/example/edit/'+row.id" class="link-type">
            <span>{{ row.title }}</span>
          </router-link>
        </template>
      </el-table-column> -->

      <el-table-column align="center" label="操作" width="260">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleRead(scope.row)">
            查看
          </el-button>
          <el-button
            v-if="scope.row.status === 0 && checkPermission(['admin', 'submit'])"
            type="warning"
            size="mini"
            @click="handleModify(scope.row)"
          >
            修改
          </el-button>
          <el-button
            v-if="scope.row.status === 0 && checkPermission(['admin', 'submit'])"
            type="danger"
            size="mini"
            class="btnTop"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
          <el-button
            v-if="scope.row.status === 0 && checkPermission(['admin', 'submit'])"
            type="success"
            size="mini"
            class="btnTop"
            @click="handleSubmit(scope.row)"
          >
            提交
          </el-button>
          <el-button
            v-if="(scope.row.status === 2 || scope.row.status === 3) && checkPermission(['admin', 'deal'])"
            type="warning"
            size="mini"
            @click="handleParDeal(scope.row)"
          >
            处理
          </el-button>

          <el-button
            type="info"
            size="mini"
            class="btnTop"
            @click="handleCooperaDetail(scope.row)"
          >
            处理详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[5, 10, 15, 20, 25]"
        :page-size="listQuery.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div> -->
    <DialogForm
      :dialog-visible="dialogVisible"
      :event-detail="eventDetail"
      :dialog-status="dialogStatus"
    />
    <DialogTable
      :dialog-table-visible="dialogTableVisible"
      :coopera-list="cooperaList"
    />
    <DialogFormDealHelp
      :dialog-visible="dialogVisibleDealHelp"
      :event-detail="eventDetail"
    />
  </div>
</template>

<script>
// import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import waves from '@/directive/waves'
import DialogForm from './component/dialog_form.vue'
import DialogTable from './component/dialog_table'
import DialogFormDealHelp from './component/dialog_form_deal_help'
import checkPermission from '@/utils/permission' // 权限判断函数
import {
  getAllMyEvent,
  addEvent,
  deleteMyEvent,
  updateEvent,
  submitMyEvent,
  getDeal,
  coordinationEvent
} from '.././../api/event'
import { parseTime } from '@/utils'
export default {
  directives: {
    waves
  },
  // components: { Pagination },
  filters: {
    statusFilter(extInfo) {
      extInfo = '' + extInfo
      console.log('test:::', extInfo)
      const statusMap = {
        '1': 'warning',
        '2': 'danger',
        '3': 'info',
        '-1': 'success',
        '0': 'success'
      }
      return statusMap[extInfo]
    },
    statusFilterName(extInfo) {
      const statusMap = {
        1: '待处理',
        2: '待协同',
        3: '协同中',
        '-1': '已结束',
        0: '待提交'
      }
      return statusMap[extInfo]
    },
    formatTime(time) {
      return parseTime(time)
    }
  },
  components: {
    DialogForm,
    DialogFormDealHelp,
    DialogTable
  },
  data() {
    return {
      statusOption: [
        {
          label: '待处理',
          value: '待处理'
        },
        {
          label: '待协同',
          value: '待协同'
        },
        {
          label: '协同中',
          value: '协同中'
        },
        {
          label: '已结束',
          value: '已结束'
        },
        {
          label: '待提交',
          value: '待提交'
        }
      ],
      list: [
        // {
        //   name: '恐怖袭击',
        //   company: 'usa',
        //   createDate: new Date(),
        //   service: '全套',
        //   level: 'p0',
        //   dealType: '0',
        //   distriType: '1',
        //   extInfo: '待处理'
        // }
      ],
      dialogVisible: false,
      dialogTableVisible: false,
      dialogStatus: 'create',
      eventDetail: {
        // name: 'test',
        // date: new Date()
      },
      total: 0,
      listLoading: true,
      currentPage: 1,
      listQuery: {
        page: 1,
        size: 10,
        createBy: ''
        // reportClass: '',
        // name: ''
        // extInfo: ''
      },
      userType: '',
      cooperaList: [],
      dialogVisibleDealHelp: false
    }
  },
  created() {
    // const { username, userType } = JSON.parse(
    //   window.localStorage.getItem('userInfo')
    // )
    // this.listQuery.createBy = username
    // this.userType = userType
    this.getList()
  },
  methods: {
    checkPermission,
    getList() {
      this.listLoading = true
      getAllMyEvent(this.listQuery).then((res) => {
        // console.log(res)
        this.listLoading = false
        this.list = res.data.list
      })
      // this.listLoading = true
      // fetchList(this.listQuery).then(response => {
      //   this.list = response.data.items
      //   this.total = response.data.total
      // })
    },
    handleSizeChange() {},
    handleCurrentChange() {},
    handleFilter() {
      this.getList()
    },
    resetListQuery() {
      this.listQuery = {
        page: 1,
        size: 10,
        reportClass: '',
        eventName: '',
        extInfo: ''
      }
      this.getList()
    },
    handleClose() {
      console.log(2222)
      this.dialogVisible = false
    },
    handleTableClose() {
      this.dialogTableVisible = false
    },
    handleDeal(eventDetail) {
      console.log(eventDetail)
      // 创建
      if (this.dialogStatus === 'create') {
        addEvent(eventDetail).then((res) => {
          this.getList()
        })
      } else {
        updateEvent(eventDetail).then((res) => {
          this.getList()
        })
      }
      this.dialogVisible = false
    },
    handleSelectionChange() {},
    handleCreate() {
      this.dialogVisible = true
      this.dialogStatus = 'create'
    },
    handleRead(row) {
      this.dialogVisible = true
      this.eventDetail = row
    },
    handleCoopera(row) {
      const obj = Object.assign({}, row)
      updateEvent(obj).then((res) => {
        this.getList()
      })
    },
    // 处理
    handleParDeal(row) {
      this.dialogVisibleDealHelp = true
      this.eventDetail = row
    },
    handleDelete(row) {
      deleteMyEvent(row.id).then((res) => {
        this.getList()
      })
    },
    // 提交
    handleSubmit(row) {
      submitMyEvent(row.id).then((res) => {
        console.log(res)
        this.getList()
      })
    },
    handleModify(row) {
      this.dialogVisible = true
      this.eventDetail = row
    },
    handleCooperaDetail(row) {
      getDeal(row.id).then((res) => {
        console.log(res)
        this.cooperaList = res.data.list
        this.dialogTableVisible = true
      })
    },
    // 系统的回调函数
    handleDealHelp(eventDetail) {
      console.log(eventDetail)
      const obj = Object.assign({
        eventId: eventDetail.id,
        processStatus: eventDetail.processStatus,
        remark: eventDetail.newRemark,
        result: eventDetail.result
      })

      coordinationEvent(obj).then((res) => {
        this.dialogVisibleDealHelp = false
        this.getList()
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
.btnTop {
  margin-top: 7px;
}
</style>
