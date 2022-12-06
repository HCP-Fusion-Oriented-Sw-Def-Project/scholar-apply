<template>
  <div id="cards">
    <div v-for="(card, i) in cards" :key="card.title" class="card-item">
      <div class="card-header">
        <div class="card-header-left">{{ card.title }}</div>
        <div class="card-header-right">{{ "0" + (i + 1) }}</div>
      </div>
      <dv-charts v-if="i===0" :key="timeKey0" class="ring-charts" :option="card.ring" />
      <dv-charts v-if="i===1" :key="timeKey1" class="ring-charts" :option="card.ring" />
      <dv-charts v-if="i===2" :key="timeKey2" class="ring-charts" :option="card.ring" />
      <!-- <div class="card-footer">
        <div class="card-footer-item">
          <div class="footer-title">累计数量</div>
          <div class="footer-detail">
            <dv-digital-flop
              :config="card.total"
              style="width: 70%; height: 35px"
            />元
          </div>
        </div>
        <div class="card-footer-item">
          <div class="footer-title">风险信息</div>
          <div class="footer-detail">
            <dv-digital-flop
              :config="card.num"
              style="width: 70%; height: 35px"
            />处
          </div>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'Cards',
  data() {
    return {
      cards: [],
      timeKey0: new Date().getTime(),
      timeKey1: new Date().getTime(),
      timeKey2: new Date().getTime()
    }
  },
  mounted() {
    const { createData } = this

    createData()

    // setInterval(this.createData, 30000)
  },
  methods: {
    createData() {
      // const { randomExtend } = this

      this.cards = [
        {
          title: '总趋势',
          ring: {
            title: {
              text: '总趋势'
            },
            xAxis: {
              name: '时间',
              data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
              axisLabel: {
                style: {
                  fill: '#FFFFFF'
                }
              }
            },
            yAxis: {
              name: '数量',
              data: 'value',
              axisLabel: {
                style: {
                  fill: '#FFFFFF'
                }
              },
              min: 0,
              splitLine: {
                show: false
              }
            },
            grid: {
              top: '3%'
            },
            series: [
              {
                data: [],
                type: 'bar',
                gradient: {
                  color: ['#37a2da', '#67e0e3']
                }
              }
            ],
            color: ['#FFFFFF']
          }
        },
        {
          title: '近一天趋势',
          ring: {
            title: {
              text: '周销售额趋势'
            },
            xAxis: {
              name: '时间',
              // show: false,
              data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
              axisLabel: {
                style: {
                  fill: '#FFFFFF'
                }
              }
            },
            yAxis: {
              name: '销售额',
              data: 'value',
              axisLabel: {
                style: {
                  fill: '#FFFFFF'
                }
              },
              min: 0,
              splitLine: {
                show: false
              }
            },
            grid: {
              top: '3%'
            },
            series: [
              {
                data: [],
                type: 'bar',
                gradient: {
                  color: ['#37a2da', '#67e0e3']
                }
              }
            ],
            color: ['#FFFFFF']
          }
        },
        {
          title: '近1小时趋势',
          ring: {
            title: {
              text: '周销售额趋势'
            },
            xAxis: {
              name: '时间',
              data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
              axisLabel: {
                style: {
                  fill: '#FFFFFF'
                }
              }
            },
            yAxis: {
              name: '销售额',
              data: 'value',
              axisLabel: {
                style: {
                  fill: '#FFFFFF'
                }
              },
              min: 0,
              splitLine: {
                show: false
              }
            },
            grid: {
              top: '3%'
            },
            series: [
              {
                data: [],
                type: 'bar',
                gradient: {
                  color: ['#37a2da', '#67e0e3']
                }
              }
            ],
            color: ['#FFFFFF']
          }
        }
      ]
    },
    updataValue(valX, valY, num) {
      this.cards[num].ring.xAxis.data = valX
      this.cards[num].ring.series[0].data = valY
      console.log(num, 'aaaa:::', this.cards[num])
      switch (num) {
        case 0:
          this.timeKey0 = new Date().getTime()
          break
        case 1:
          this.timeKey1 = new Date().getTime()
          break
        case 2:
          this.timeKey2 = new Date().getTime()
          break
      }
    }
  }
}
</script>

<style lang="less">
#cards {
  display: flex;
  justify-content: space-between;
  height: 45%;

  .card-item {
    background-color: rgba(6, 30, 93, 0.5);
    border-top: 2px solid rgba(1, 153, 209, 0.5);
    width: 30%;
    display: flex;
    flex-direction: column;
  }

  .card-header {
    display: flex;
    height: 20%;
    align-items: center;
    justify-content: space-between;

    .card-header-left {
      font-size: 18px;
      font-weight: bold;
      padding-left: 20px;
    }

    .card-header-right {
      padding-right: 20px;
      font-size: 40px;
      color: #03d3ec;
    }
  }

  .ring-charts {
    height: 90%;
    bottom: 1%;
  }

  .card-footer {
    height: 25%;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }

  .card-footer-item {
    padding: 5px 10px 0px 10px;
    box-sizing: border-box;
    width: 40%;
    background-color: rgba(6, 30, 93, 0.7);
    border-radius: 3px;

    .footer-title {
      font-size: 15px;
      margin-bottom: 5px;
    }

    .footer-detail {
      font-size: 20px;
      color: #1294fb;
      display: flex;
      font-size: 18px;
      align-items: center;

      .dv-digital-flop {
        margin-right: 5px;
      }
    }
  }
}
</style>
