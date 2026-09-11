<template>
  <div class="app-container">
    <el-form :inline="true" size="small">
      <el-form-item label="区间">
        <el-select v-model="query.range">
          <el-option label="今日" value="today" /><el-option label="本周" value="week" /><el-option label="本月" value="month" /><el-option label="本年" value="year" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      <el-form-item><el-button @click="exportReport">导出当前页</el-button></el-form-item>
    </el-form>
    <el-tabs v-model="tab" @tab-click="load">
      <el-tab-pane label="总览" name="overview"><el-descriptions :column="3" border>
        <el-descriptions-item label="商品数">{{ overview.productCount }}</el-descriptions-item>
        <el-descriptions-item label="进货量">{{ overview.purchaseQty }}</el-descriptions-item>
        <el-descriptions-item label="库存量">{{ overview.stockQty }}</el-descriptions-item>
        <el-descriptions-item label="进货成本">{{ overview.purchaseCost }}</el-descriptions-item>
        <el-descriptions-item label="库存预警">{{ overview.warnCount }}</el-descriptions-item>
        <el-descriptions-item label="零库存">{{ overview.emptyCount }}</el-descriptions-item>
      </el-descriptions></el-tab-pane>
      <el-tab-pane label="进货" name="purchase"><el-table :data="purchaseRows"><el-table-column prop="purchaseNo" label="单号" /><el-table-column prop="productName" label="商品" /><el-table-column prop="qty" label="数量" /><el-table-column prop="amount" label="金额" /></el-table></el-tab-pane>
      <el-tab-pane label="库存" name="stock"><el-table :data="stockRows"><el-table-column prop="productName" label="商品" /><el-table-column prop="changeType" label="类型" /><el-table-column prop="afterQty" label="结余" /></el-table></el-tab-pane>
      <el-tab-pane label="成本" name="cost"><el-table :data="costRows"><el-table-column prop="productName" label="商品" /><el-table-column prop="stockQty" label="库存" /><el-table-column prop="stockCost" label="成本" /></el-table></el-tab-pane>
      <el-tab-pane label="供应商" name="supplier"><el-table :data="supplierRows"><el-table-column prop="supplierName" label="厂家" /><el-table-column prop="totalAmount" label="进货额" /><el-table-column prop="totalQty" label="数量" /></el-table></el-tab-pane>
      <el-tab-pane label="营业额" name="turnover">
        <p>汇总：{{ turnover.turnover }} / 件数 {{ turnover.qty }}</p>
        <el-table :data="detailRows"><el-table-column prop="saleNo" label="单号" /><el-table-column prop="productName" label="商品" /><el-table-column prop="amount" label="金额" /></el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { reportOverview, reportPurchase, reportStock, reportCost, reportSupplier, reportTurnover, reportDetail } from "@/api/pms/report"
export default {
  name: "PmsReport",
  data() {
    return { tab: "overview", query: { range: "month" }, overview: {}, purchaseRows: [], stockRows: [], costRows: [], supplierRows: [], turnover: {}, detailRows: [] }
  },
  created() { this.load() },
  methods: {
    load() {
      const q = this.query
      if (this.tab === "overview") reportOverview(q).then(r => this.overview = r.data || {})
      if (this.tab === "purchase") reportPurchase(q).then(r => this.purchaseRows = (r.data && r.data.rows) || r.data || [])
      if (this.tab === "stock") reportStock(q).then(r => this.stockRows = (r.data && r.data.rows) || r.data || [])
      if (this.tab === "cost") reportCost(q).then(r => this.costRows = r.data || [])
      if (this.tab === "supplier") reportSupplier(q).then(r => this.supplierRows = r.data || [])
      if (this.tab === "turnover") { reportTurnover(q).then(r => this.turnover = r.data || {}); reportDetail(q).then(r => this.detailRows = r.data || []) }
    },
    exportReport() { this.download("pms/report/export/" + this.tab, { ...this.query }, `report_${this.tab}.xlsx`) }
  }
}
</script>
