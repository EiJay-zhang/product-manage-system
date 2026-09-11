<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :span="6" v-for="item in cards" :key="item.key">
        <el-card shadow="hover" class="mb16">
          <div class="card-label">{{ item.label }}</div>
          <div class="card-value">{{ summary[item.key] ?? '-' }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-alert v-if="summary.turnoverWarn" :title="'营收预警：' + summary.turnoverWarn" type="warning" show-icon />
  </div>
</template>
<script>
import { getDashboard } from "@/api/pms/dashboard"
export default {
  name: "PmsDashboard",
  data() {
    return {
      summary: {},
      cards: [
        { key: "productCount", label: "商品数" },
        { key: "warnCount", label: "库存预警" },
        { key: "emptyCount", label: "零库存" },
        { key: "offlineDeviceCount", label: "离线设备" },
        { key: "uncheckedCount", label: "待对账" },
        { key: "unpaidCount", label: "未结清" },
        { key: "todayTurnover", label: "今日营业额" }
      ]
    }
  },
  created() { this.load() },
  methods: {
    load() {
      getDashboard().then(res => { this.summary = res.data || {} })
    }
  }
}
</script>
<style scoped>
.mb16 { margin-bottom: 16px; }
.card-label { color: #909399; font-size: 13px; }
.card-value { font-size: 24px; font-weight: 600; margin-top: 8px; }
</style>
