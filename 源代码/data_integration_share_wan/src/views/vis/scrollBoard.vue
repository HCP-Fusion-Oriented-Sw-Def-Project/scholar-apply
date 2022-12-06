<template>
  <div id="scroll-board">
    <dv-scroll-board :key="timeKey" :config="config" />
  </div>
</template>

<script>
export default {
  name: 'ScrollBoard',
  data() {
    return {
      timeKey: new Date().getTime(),
      config: {
        header: ['时间', '编号', '名称', '上报机构'],
        data: [
          ['2019-07-01 19:25:00', 'xx风险-xx', '5', 'xxxxxxx'],
          ['2019-07-02 17:25:00', 'xx风险-xx', '13', 'xxxxxxx'],
          ['2019-07-03 16:25:00', 'xx风险-xx', '6', 'xxxxxxx'],
          ['2019-07-04 15:25:00', 'xx风险-xx', '2', 'xxxxxxx'],
          ['2019-07-05 14:25:00', 'xx风险-xx', '1', 'xxxxxxx'],
          ['2019-07-06 13:25:00', 'xx风险-xx', '3', 'xxxxxxx'],
          ['2019-07-07 12:25:00', 'xx风险-xx', '4', 'xxxxxxx'],
          ['2019-07-08 11:25:00', 'xx风险-xx', '2', 'xxxxxxx'],
          ['2019-07-09 10:25:00', 'xx风险-xx', '5', 'xxxxxxx'],
          ['2019-07-10 09:25:00', 'xx风险-xx', '3', 'xxxxxxx']
        ],
        index: true,
        columnWidth: [50, 150, 150],
        align: ['center'],
        rowNum: 7,
        headerBGC: '#1981f6',
        headerHeight: 45,
        oddRowBGC: 'rgba(0, 44, 81, 0.8)',
        evenRowBGC: 'rgba(10, 29, 50, 0.8)'
      }
    }
  },
  methods: {
    updataValue(val) {
      this.config.data = val
      this.timeKey = new Date().getTime()
    }
  }
}
</script>

<style lang="less">
#scroll-board {
  width: 51%;
  box-sizing: border-box;
  height: 100%;
  overflow: hidden;
}
</style>
