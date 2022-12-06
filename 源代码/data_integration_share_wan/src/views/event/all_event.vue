<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.eventName"
        class="filter-item"
        style="width: 250px"
        placeholder="事件名称"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.reportClass"
        class="filter-item"
        style="width: 250px"
        placeholder="上报单位"
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
      <el-table-column align="center" label="事件名称" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column width="180" align="center" label="上报单位">
        <template slot-scope="scope">
          <span>{{ scope.row.reportDeptName }}</span>
        </template>
      </el-table-column>

      <el-table-column width="200" align="center" label="上报时间">
        <template slot-scope="scope">
          <span>{{ scope.row.reportDate | formatTime }}</span>
        </template>
      </el-table-column>

      <el-table-column width="150" align="center" label="产品或服务名称">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="安全级别">
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
      </el-table-column>

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
            v-if="scope.row.status === 1"
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
            协同详情
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
    <DialogFormDeal
      :dialog-visible="dialogVisible"
      :event-detail="eventDetail"
      :dialog-status="dialogStatus"
    />
    <DialogTable
      :dialog-table-visible="dialogTableVisible"
      :coopera-list="cooperaList"
    />
  </div>
</template>

<script>
// import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import waves from '@/directive/waves'
import DialogFormDeal from './component/dialog_form_deal'
import DialogTable from './component/dialog_table'
import {
  getAllEventBeside,
  deleteEvent,
  updateEvent,
  getDeal,
  addSolve,
  addDeal,
  dealHelpEvent
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
    DialogFormDeal,
    DialogTable
  },
  data() {
    return {
      cooperaList: [],
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
        //   time: new Date(),
        //   service: '全套',
        //   level: 'p0',
        //   dealType: '0',
        //   distriType: '1',
        //   extInfo: '待处理'
        // }
      ],
      dialogVisible: false,
      dialogTableVisible: false,
      dialogStatus: 'read',
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
        reportClass: '',
        eventName: '',
        extInfo: '',
        username: ''
      },
      userType: ''
    }
  },
  created() {
    // const { username, userType } = JSON.parse(
    //   window.localStorage.getItem('userInfo')
    // )
    // this.listQuery.username = username
    // this.userType = userType
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getAllEventBeside(this.listQuery).then((res) => {
        console.log(res)
        this.listLoading = false
        this.list = res.data.list
      })
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
        extInfo: '',
        username: JSON.parse(window.localStorage.getItem('userInfo')).username
      }
      this.getList()
    },
    handleClose() {
      console.log(2222)
      this.dialogVisible = false
    },
    handleDeal(eventDetail) {
      console.log(eventDetail)
      updateEvent(eventDetail).then((res) => {
        this.getList()
      })
      // 创建
      // if (this.dialogStatus === 'create') {
      //   addEvent(eventDetail).then(res => {
      //     this.getAllEvent()
      //   })
      // } else {
      //   updateEvent(eventDetail).then(res => {
      //     this.getAllEvent()
      //   })
      // }
      this.dialogVisible = false
    },
    handleDealHelp(eventDetail) {
      console.log(eventDetail)
      const obj = Object.assign({
        deptIds: eventDetail.AllOfficeList,
        eventId: eventDetail.id,
        processType: eventDetail.processType
      })

      dealHelpEvent(obj).then((res) => {
        this.dialogVisible = false
        this.getList()
      })
    },
    handleSelectionChange() {},
    handleRead(row) {
      this.dialogVisible = true
      this.eventDetail = row
    },
    handleModify(row) {
      this.dialogVisible = true
      this.eventDetail = row
      this.dialogStatus = 'update'
    },
    handleCooperaDetail(row) {
      getDeal(row.id).then((res) => {
        console.log(res)
        this.cooperaList = res.data.list
        this.dialogTableVisible = true
      })
    },
    handleTableClose() {
      this.dialogTableVisible = false
    },
    handleCoopera(row) {
      const obj = Object.assign({}, row, {
        eventId: row.id,
        className: JSON.parse(window.localStorage.getItem('userInfo'))
          .classname
      })
      addDeal(obj).then((res) => {
        this.getList()
      })
      // updateDeal(obj).then(res => {
      //   this.getList()
      // })
    },
    // 处理
    handleParDeal(row) {
      console.log('111111:')
      this.dialogVisible = true
      this.eventDetail = row
    },
    handleDelete(row) {
      deleteEvent(row.id).then((res) => {
        this.getList()
      })
    },
    handleSubmit(row) {
      // const obj = Object.assign({}, row)
      addSolve(row.id).then((res) => {
        console.log(res)
      })
      // updateEvent(obj).then(res => {
      //   this.getAllEvent()
      // })
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
