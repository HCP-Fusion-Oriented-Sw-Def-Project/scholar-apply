<template>
  <div id="data-view">
    <dv-full-screen-container>
      <top-header />

      <div class="main-content">
        <digital-flop ref="digitalFlopRef" :pass-top-total-num="topTotalNum" />

        <div class="block-left-right-content">
          <ranking-board ref="rankingBoardRef" />

          <div class="block-top-bottom-content">
            <div class="block-top-content">
              <rose-chart ref="roseChartRefSecurity" title-name="安全等级" />
              <rose-chart ref="roseChartRefInfluence" title-name="影响范围" />
              <!-- <water-level-chart /> -->

              <scroll-board ref="scrollBoardRef" />
            </div>

            <cards ref="cardsRef" />
          </div>
        </div>
      </div>
    </dv-full-screen-container>
  </div>
</template>

<script>
import topHeader from './topHeader'
import digitalFlop from './digitalFlop'
import rankingBoard from './rankingBoard'
import roseChart from './roseChart'
// import waterLevelChart from './waterLevelChart'
import scrollBoard from './scrollBoard'
import cards from './cards'
import {
  getRiskAllInfo,
  // getRiskTotal,
  // getConfirmed,
  // getNoRequired,
  // getRequiredCoordinationTotal,
  // getCoordinating,
  // getCoordinated,
  getMostSubmitDept,
  getAllEventBeside,
  getAllTimeSubmit,
  // getAverageProcessTime,
  getAneDaySubmit,
  getAneHourSubmit,
  getSecurityLevelProportion,
  getInfluenceScopeProportion
} from '@/api/vis'

export default {
  name: 'DataView',
  components: {
    topHeader,
    digitalFlop,
    rankingBoard,
    roseChart,
    // waterLevelChart,
    scrollBoard,
    cards
  },
  data() {
    return {
      timerIn: '',
      topTotalNum: [
        {
          title: '风险总数',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#4d99fc',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '已确认数目',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#f46827',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '待确认数目',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#40faee',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '无需处理',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#4d99fc',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '协同处理',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#f46827',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '正在协同',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#40faee',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '完成协同',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#4d99fc',
              fontWeight: 'bold'
            }
          },
          unit: '条'
        },
        {
          title: '平均处理时间',
          number: {
            number: [0],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#f46827',
              fontWeight: 'bold'
            }
          },
          unit: '小时'
        }
      ],
      dataTotalByOrg: [],
      timer: ''
    }
  },
  mounted() {
    this.getData()
    this.timerIn = setInterval(this.getData, 10000)
  },
  beforeDestroy() {
    clearInterval(this.timerIn)
  },

  methods: {
    getData() {
      // this.getRiskTotal()
      // this.getConfirmed()
      // this.getNoRequired()
      // this.getRequiredCoordinationTotal()
      // this.getCoordinating()
      // this.getCoordinated()
      // this.getAverageProcessTime()
      this.getMostSubmitDept()
      this.getAllEventBeside()
      this.getAllTimeSubmit()
      this.getAneDaySubmit()
      this.getAneHourSubmit()
      this.getSecurityLevelProportion()
      this.getInfluenceScopeProportion()
      this.getRiskAllInfo()
    },
    getDataTotalByOrg() {},

    // 上部数据
    // getRiskTotal() {
    //   getRiskTotal().then((response) => {
    //     var temp = new Array(1)
    //     temp[0] = response.data
    //     this.topTotalNum[0].number.number = temp
    //     this.passdigitalFlopRefValues()
    //   })
    // },
    // getConfirmed() {
    //   getConfirmed().then((response) => {
    //     // console.log('确认2', response)
    //     var temp = new Array(1)
    //     temp[0] = response.data
    //     this.topTotalNum[1].number.number = temp
    //     this.passdigitalFlopRefValues()
    //   })
    // },
    // getNoRequired() {
    //   getNoRequired().then((response) => {
    //     var temp = new Array(1)
    //     temp[0] = response.data
    //     this.topTotalNum[3].number.number = temp
    //     this.passdigitalFlopRefValues()
    //   })
    // },
    // getRequiredCoordinationTotal() {
    //   getRequiredCoordinationTotal().then((response) => {
    //     var temp = new Array(1)
    //     temp[0] = response.data
    //     this.topTotalNum[4].number.number = temp
    //     this.passdigitalFlopRefValues()
    //   })
    // },
    // getCoordinating() {
    //   getCoordinating().then((response) => {
    //     var temp = new Array(1)
    //     temp[0] = response.data
    //     this.topTotalNum[5].number.number = temp
    //     this.passdigitalFlopRefValues()
    //   })
    // },
    // getCoordinated() {
    //   getCoordinated().then((response) => {
    //     var temp = new Array(1)
    //     temp[0] = response.data
    //     this.topTotalNum[6].number.number = temp
    //     this.passdigitalFlopRefValues()
    //   })
    // },
    getRiskAllInfo() {
      getRiskAllInfo().then((response) => {
        console.log('所有的数据:::', response)
        var temp0 = new Array(1)
        temp0[0] = response.data.风险总数
        this.topTotalNum[0].number.number = temp0

        var temp1 = new Array(1)
        temp1[0] = response.data.已确认数
        this.topTotalNum[1].number.number = temp1

        var temp2 = new Array(1)
        temp2[0] = response.data.待确认数
        this.topTotalNum[2].number.number = temp2

        var temp3 = new Array(1)
        temp3[0] = response.data.无需处理数
        this.topTotalNum[3].number.number = temp3

        var temp4 = new Array(1)
        temp4[0] = response.data.需要协同数
        this.topTotalNum[4].number.number = temp4

        var temp5 = new Array(1)
        temp5[0] = response.data.正在协同数
        this.topTotalNum[5].number.number = temp5

        var temp6 = new Array(1)
        temp6[0] = response.data.协同完成数
        this.topTotalNum[6].number.number = temp6

        var temp7 = new Array(1)
        temp7[0] = response.data.平均处理时间
        this.topTotalNum[7].number.number = temp7

        this.passdigitalFlopRefValues()
      })
    },
    // 左侧数据（最多的10个）
    getMostSubmitDept() {
      getMostSubmitDept().then((response) => {
        var temp = []
        for (const key in response.data) {
          var innTemp = {
            name: response.data[key].officeName,
            value: response.data[key].num
          }
          temp.push(innTemp)
        }
        this.passRankingBoardRefValues(temp)
      })
    },
    // 最新的10条数据
    getAllEventBeside() {
      getAllEventBeside().then((response) => {
        var temp = []
        for (const key in response.data.list) {
          var innTemp = []
          innTemp[0] = response.data.list[key].reportDate
          innTemp[1] = response.data.list[key].no
          innTemp[2] = response.data.list[key].name
          innTemp[3] = response.data.list[key].reportDeptName
          temp.push(innTemp)
        }
        this.passScrollBoardRefValues(temp)
      })
    },
    getAllTimeSubmit() {
      getAllTimeSubmit().then((response) => {
        var xAxisTemp = []
        var yAxisTemp = []
        for (let i = 0; i < response.data.length; i++) {
          xAxisTemp.push(response.data[i].time)
          yAxisTemp.push(response.data[i].num)
        }
        this.passCardsRefValues(xAxisTemp, yAxisTemp, 0)
      })
    },
    getAneDaySubmit() {
      getAneDaySubmit().then((response) => {
        var xAxisTemp = []
        var yAxisTemp = []
        for (let i = response.data.length - 1; i > -1; i--) {
          // xAxisTemp.push(response.data[i].time)
          xAxisTemp.push(i + 1)
          yAxisTemp.push(response.data[i].num)
        }
        console.log('确认1', yAxisTemp)
        this.passCardsRefValues(xAxisTemp, yAxisTemp, 1)
      })
    },
    getAneHourSubmit() {
      getAneHourSubmit().then((response) => {
        var xAxisTemp = []
        var yAxisTemp = []
        for (let i = response.data.length - 1; i > -1; i--) {
          // xAxisTemp.push(response.data[i].time)
          xAxisTemp.push((i + 1) * 10)
          yAxisTemp.push(response.data[i].num)
        }
        this.passCardsRefValues(xAxisTemp, yAxisTemp, 2)
      })
    },
    getSecurityLevelProportion() {
      getSecurityLevelProportion().then((response) => {
        var temp = []
        console.log('安全等级', response)
        for (const key in response.data) {
          var innTemp = {
            name: this.securityOpation(response.data[key].type),
            value: response.data[key].percent * 100
          }
          temp.push(innTemp)
        }
        this.passRoseChartRefSecurityValues(temp)
      })
    },
    securityOpation(num) {
      if (num === null || num === '') {
        return '其它'
      }
      const statusMap = {
        0: '超危',
        1: '高危',
        2: '中危',
        3: '低危',
        4: '无危险'
      }
      return statusMap[num]
    },
    getInfluenceScopeProportion() {
      getInfluenceScopeProportion().then((response) => {
        var temp = []
        for (const key in response.data) {
          var innTemp = {
            name: this.influenceOpation(response.data[key].type),
            value: response.data[key].percent * 100
          }
          temp.push(innTemp)
        }
        this.passRoseChartRefInfluenceValues(temp)
      })
    },
    influenceOpation(num) {
      if (num === null || num === '') {
        return '其它'
      }
      const statusMap = {
        0: '非常严重',
        1: '严重',
        2: '一般',
        3: '无'
      }
      return statusMap[num]
    },
    // getAverageProcessTime() {
    //   getAverageProcessTime().then((response) => {
    //     var temp = new Array(response.data)
    //     this.topTotalNum[7].number.number = temp
    //   })
    // },
    // 定时刷新

    /** 子页面传值 */
    // 上部值
    passdigitalFlopRefValues() {
      this.$refs.digitalFlopRef.updataValue(this.topTotalNum)
    },
    // 最多10条数据
    passRankingBoardRefValues(val) {
      this.$refs.rankingBoardRef.updataValue(val)
    },
    // 最新10条数据
    passScrollBoardRefValues(val) {
      this.$refs.scrollBoardRef.updataValue(val)
    },
    // 柱状图的
    passCardsRefValues(valX, valY, num) {
      this.$refs.cardsRef.updataValue(valX, valY, num)
    },
    // 安全
    passRoseChartRefSecurityValues(val) {
      this.$refs.roseChartRefSecurity.updataValue(val)
    },
    // 影响范围
    passRoseChartRefInfluenceValues(val) {
      this.$refs.roseChartRefInfluence.updataValue(val)
    }
  }
}
</script>

<style lang="less">
#data-view {
  width: 100%;
  height: 100%;
  background-color: #030409;
  color: #fff;

  #dv-full-screen-container {
    background-image: url("./img/bg.png");
    background-size: 100% 100%;
    box-shadow: 0 0 3px blue;
    display: flex;
    flex-direction: column;
    z-index: 99999;
  }

  .main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .block-left-right-content {
    flex: 1;
    display: flex;
    margin-top: 20px;
  }

  .block-top-bottom-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    padding-left: 20px;
  }

  .block-top-content {
    height: 40%;
    display: flex;
    flex-grow: 0;
    box-sizing: border-box;
    padding-bottom: 20px;
  }
}
</style>
