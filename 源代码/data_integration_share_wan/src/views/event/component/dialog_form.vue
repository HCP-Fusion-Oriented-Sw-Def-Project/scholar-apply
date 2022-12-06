<template>
  <div>
    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="dialogVisible"
      width="60%"
    >
      <el-form
        ref="form"
        :model="eventDetail"
        label-width="120"
        label-position="left"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请项目名称">
              <el-input
                v-model="eventDetail.name"
                style="width: 70%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上报时间">
              <el-date-picker
                v-model="eventDetail.reportDate"
                type="datetime"
                placeholder="选择日期时间"
                align="right"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="上报人">
               <el-input
                v-model="eventDetail.reportClass"
                style="width: 55%"
              /> 
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="事件影响产品/服务的名称">
              <el-input
                v-model="eventDetail.serviceName"
                style="width: 50%"
              />
            </el-form-item>
          </el-col> -->
        </el-row>
        <!-- <el-row>
          <el-col :span="12">
            <el-form-item label="事件影响产品/服务的版本号">
              <el-input
                v-model="eventDetail.serviceVersion"
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件影响产品/服务的供应商">
              <el-input
                v-model="eventDetail.serviceSupplier"
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="事件影响产品/服务的设备位置">
              <el-input
                v-model="eventDetail.servicePosition"
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件影响产品/服务的设备ip">
              <el-input
                v-model="eventDetail.serviceIp"
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="信息泄露风险">
              <el-select v-model="eventDetail.disclosureRisk">
                <el-option
                  v-for="item in condition1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据完整性风险">
              <el-select v-model="eventDetail.dataIntegrityRisk">
                <el-option
                  v-for="item in condition1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统可用性风险">
              <el-select v-model="eventDetail.systemAvailabilityRisk">
                <el-option
                  v-for="item in condition1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="影响范围">
              <el-select v-model="eventDetail.influenceScope">
                <el-option
                  v-for="item in condition2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="修复难度">
              <el-select v-model="eventDetail.repairDifficulty">
                <el-option
                  v-for="item in condition2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全级别">
              <el-select v-model="eventDetail.securityLevel">
                <el-option
                  v-for="item in condition3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="处理类型">
              <el-select v-model="eventDetail.processType">
                <el-option
                  v-for="item in condition4"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求协同单位">
              <el-input
                v-model="eventDetail.requestDeptVOS"
                style="width: 70%"
              />
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="分发类型">
              <el-select v-model="eventDetail.distributionType">
                <el-option
                  v-for="item in condition5"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注">
              <el-input
                v-model="eventDetail.remarks"
                style="width: 70%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleDeal">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DialogForm',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    eventDetail: {
      type: Object,
      default: null
    },
    dialogStatus: {
      type: String,
      default: 'create'
    }
  },
  data() {
    return {
      textMap: {
        create: '添加',
        update: '编辑',
        read: '查看'
      },
      // 信息、数据、系统
      condition1: [
        {
          value: '0',
          label: '严重'
        },
        {
          value: '1 ',
          label: '一般'
        },
        {
          value: '2',
          label: '无'
        }
      ],
      // 影响、修复
      condition2: [
        {
          value: '0',
          label: '非常严重'
        },
        {
          value: '1',
          label: '严重'
        },
        {
          value: '2',
          label: '一般'
        },
        {
          value: '3',
          label: '无'
        }
      ],
      // 安全级别
      condition3: [
        {
          value: '0',
          label: '超危'
        },
        {
          value: '1',
          label: '高危'
        },
        {
          value: '2',
          label: '中危'
        },
        {
          value: '3',
          label: '低危'
        },
        {
          value: '4',
          label: '无危险'
        }
      ],
      // 处理类型
      condition4: [
        {
          value: '0',
          label: '超危'
        },
        {
          value: '1',
          label: '自动处理'
        },
        {
          value: '2',
          label: '人工处理'
        }
      ],
      // 分发类型
      condition5: [
        {
          value: '0 ',
          label: '自处理'
        },
        {
          value: '1',
          label: '分发协同'
        },
        {
          value: '2',
          label: '超危'
        }
      ],
      // 请求协同
      condition6: []
    }
  },
  methods: {
    handleClose() {
      this.$parent.handleClose()
    },
    handleDeal() {
      if (this.dialogStatus === 'update') {
        this.$parent.handleDeal(this.eventDetail)
      } else {
        // this.eventDetail.createBy = JSON.parse(
        //   window.localStorage.getItem('userInfo')
        // ).username
        // this.eventDetail.extInfo = '待处理'
        this.$parent.handleDeal(this.eventDetail)
      }
    }
  }
}
</script>

<style>
</style>
