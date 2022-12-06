<template>
  <div id="rose-chart">
    <div class="rose-chart-title">{{ titleName }}分布</div>
    <dv-charts :key="timeKey" :option="option" />
  </div>
</template>

<script>
export default {
  name: 'RoseChart',
  props: {
    titleName: String
  },
  data() {
    return {
      option: {},
      timeKey: new Date().getTime()
    }
  },
  mounted() {
    const { createData } = this

    createData()

    // setInterval(createData, 30000)
  },
  methods: {
    createData() {
      const { randomExtend } = this

      this.option = {
        series: [
          {
            type: 'pie',
            radius: '50%',
            roseSort: false,
            data: [
              { name: 'xxx11', value: randomExtend(40, 70) },
              { name: 'xxx', value: randomExtend(20, 30) },
              { name: 'xxx', value: randomExtend(10, 50) },
              { name: 'xxx', value: randomExtend(5, 20) },
              { name: 'xxx', value: randomExtend(40, 50) },
              { name: 'xxx', value: randomExtend(20, 30) },
              { name: 'xxx', value: randomExtend(5, 10) },
              { name: 'xxx', value: randomExtend(20, 35) },
              { name: 'xxx', value: randomExtend(5, 10) }
            ],
            insideLabel: {
              show: false
            },
            outsideLabel: {
              formatter: '{name} {percent}%',
              labelLineEndLength: 20,
              style: {
                fill: '#fff'
              },
              labelLineStyle: {
                stroke: '#fff'
              }
            },
            roseType: true
          }
        ],
        color: [
          '#da2f00',
          '#fa3600',
          '#ff4411',
          '#ff724c',
          '#541200',
          '#801b00',
          '#a02200',
          '#5d1400',
          '#b72700'
        ]
      }
    },
    randomExtend(minNum, maxNum) {
      if (arguments.length === 1) {
        return parseInt(Math.random() * minNum + 1, 10)
      } else {
        return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
      }
    },

    updataValue(val) {
      console.log('222::22:', this.option.series[0])
      console.log('222:::', this.option.series[0].data)
      this.option.series[0].data = val
      console.log('2223:::', this.option.series[0].data)
      this.timeKey = new Date().getTime()
    }
  }
}
</script>

<style lang="less">
#rose-chart {
  width: 23%;
  height: 100%;
  margin-right: 20px;
  background-color: rgba(6, 30, 93, 0.5);
  border-top: 2px solid rgba(1, 153, 209, 0.5);
  box-sizing: border-box;

  .rose-chart-title {
    height: 50px;
    font-weight: bold;
    text-indent: 20px;
    font-size: 20px;
    display: flex;
    align-items: center;
  }

  .dv-charts-container {
    height: calc(~"100% - 50px");
  }
}
</style>
