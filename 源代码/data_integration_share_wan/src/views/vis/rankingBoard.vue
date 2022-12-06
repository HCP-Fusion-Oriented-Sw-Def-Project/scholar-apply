<template>
  <div id="ranking-board">
    <div class="ranking-board-title">单位风险总量统计</div>
    <dv-scroll-ranking-board :key="timeKey" :config="config" />
  </div>
</template>

<script>
export default {
  name: 'RankingBoard',
  data() {
    return {
      timeKey: new Date().getTime(),
      config: {
        data: [
          {
            name: 'xxx',
            value: 55
          },
          {
            name: 'xxx',
            value: 120
          },
          {
            name: 'xxx',
            value: 78
          },
          {
            name: 'xxx',
            value: 66
          },
          {
            name: 'xxx',
            value: 80
          },
          {
            name: 'xxx',
            value: 45
          },
          {
            name: 'xxx',
            value: 29
          },
          {
            name: 'xxx',
            value: 29
          },
          {
            name: 'xxx',
            value: 29
          },
          {
            name: 'xxx',
            value: 29
          }
        ],
        rowNum: 10
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
#ranking-board {
  width: 20%;
  box-shadow: 0 0 3px blue;
  display: flex;
  flex-direction: column;
  background-color: rgba(6, 30, 93, 0.5);
  border-top: 2px solid rgba(1, 153, 209, .5);
  box-sizing: border-box;
  padding: 0px 30px;

  .ranking-board-title {
    font-weight: bold;
    height: 50px;
    display: flex;
    align-items: center;
    font-size: 20px;
  }

  .dv-scroll-ranking-board {
    flex: 1;
  }
}
</style>
