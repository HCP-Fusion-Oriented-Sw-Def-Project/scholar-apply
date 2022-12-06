<template>
  <div>
    <el-dialog title="协同" :visible.sync="dialogTableVisible" width="60%" :show-close="false">
      <el-table :data="cooperaList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" />
        <el-table-column label="#" type="index" />
        <el-table-column label="协同单位">
          <template slot-scope="scope">{{ scope.row.coordinationDept }}</template>
        </el-table-column>
        <el-table-column label="下发时间">
          <template slot-scope="scope">{{ scope.row.issueTime | formatTime }}</template>
        </el-table-column>
        <el-table-column label="处理状态">
          <template slot-scope="scope">{{ scope.row.processStatus | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="处理时间">
          <template slot-scope="scope">{{ scope.row.updateDate | formatTime }}</template>
        </el-table-column>
        <el-table-column label="处理人">
          <template slot-scope="scope">{{ scope.row.processPerson }}</template>
        </el-table-column>
        <!-- <el-table-column label="操作">
           <template slot-scope="scope">{
             <el-button type="danger" @click="handleDelete(scope.row)">删除</el-button>
           </template>
        </el-table-column> -->
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleClose">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils'

export default {
  name: 'DialogTable',
  filters: {
    formatTime(time) {
      return parseTime(time)
    },
    statusFilter(extInfo) {
      const statusMap = {
        0: '开始处理',
        1: '处理结束'
      }
      return statusMap[extInfo]
    }
  },
  props: {
    dialogTableVisible: {
      type: Boolean,
      default: false
    },
    cooperaList: {
      type: Array,
      default: null
    }
  },

  data() {
    return {

    }
  },
  methods: {
    handleSelectionChange() {

    },
    handleDelete() {

    },
    handleClose() {
      this.$parent.handleTableClose()
    },
    handleDeal() {

    }
  }
}
</script>

<style>

</style>
