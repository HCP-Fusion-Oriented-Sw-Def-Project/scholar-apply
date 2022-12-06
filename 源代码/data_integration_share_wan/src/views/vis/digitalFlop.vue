<template>
  <div id="digital-flop">
    <div
      v-for="item in digitalFlopData"
      :key="item.title"
      class="digital-flop-item"
    >
      <div class="digital-flop-title">{{ item.title }}</div>
      <div class="digital-flop">
        <dv-digital-flop
          :key="timeKey"
          :config="item.number"
          style="width: 100px; height: 50px"
        />
        <div class="unit">{{ item.unit }}</div>
      </div>
    </div>

    <dv-decoration-10 />
  </div>
</template>

<script>
export default {
  name: 'DigitalFlop',
  // props: {
  //   passTopTotalNum: Array
  // },
  data() {
    return {
      digitalFlopData: [],
      timeKey: new Date().getTime()
    }
  },

  mounted() {
    // this.digitalFlopData = this.passTopTotalNum
  },
  methods: {
    updataValue(passTopTotalNum) {
      this.digitalFlopData = passTopTotalNum
      console.log('pass::', passTopTotalNum)
      this.timeKey = new Date().getTime()
    },
    createData() {
      const { randomExtend } = this
      this.digitalFlopData = [
        {
          title: '风险总数',
          number: {
            number: [randomExtend(20000, 30000)],
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
          title: '确认数目',
          number: {
            number: [randomExtend(20, 30)],
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
            number: [randomExtend(20, 30)],
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
            number: [randomExtend(10, 20)],
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
            number: [randomExtend(5, 10)],
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
            number: [randomExtend(5, 10)],
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
            number: [randomExtend(5, 10)],
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
            number: [randomExtend(5, 10)],
            content: '{nt}',
            textAlign: 'right',
            style: {
              fill: '#f46827',
              fontWeight: 'bold'
            }
          },
          unit: '小时'
        }
        // ,
        // {
        //   title: 'XXX',
        //   number: {
        //     number: [randomExtend(5, 10)],
        //     content: '{nt}',
        //     textAlign: 'right',
        //     style: {
        //       fill: '#40faee',
        //       fontWeight: 'bold'
        //     }
        //   },
        //   unit: '个'
        // }
      ]
    },
    randomExtend(minNum, maxNum) {
      if (arguments.length === 1) {
        return parseInt(Math.random() * minNum + 1, 10)
      } else {
        return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
      }
    }
  }
}
</script>

<style lang="less">
#digital-flop {
  position: relative;
  height: 15%;
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(6, 30, 93, 0.5);

  .dv-decoration-10 {
    position: absolute;
    width: 95%;
    left: 2.5%;
    height: 5px;
    bottom: 0px;
  }

  .digital-flop-item {
    width: 11%;
    height: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-left: 3px solid rgb(6, 30, 93);
    border-right: 3px solid rgb(6, 30, 93);
  }

  .digital-flop-title {
    font-size: 20px;
    margin-bottom: 20px;
  }

  .digital-flop {
    display: flex;
  }

  .unit {
    margin-left: 10px;
    display: flex;
    align-items: flex-end;
    box-sizing: border-box;
    padding-bottom: 13px;
  }
}
</style>
