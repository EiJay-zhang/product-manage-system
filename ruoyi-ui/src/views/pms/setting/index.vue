<template>
  <div class="app-container">
    <el-form :model="form" label-width="160px" style="max-width: 560px">
      <el-form-item label="库存预警阈值"><el-input-number v-model="form.stockWarnThreshold" :min="0" /></el-form-item>
      <el-form-item label="营收下限"><el-input-number v-model="form.turnoverWarnMin" :precision="2" /></el-form-item>
      <el-form-item label="营收上限"><el-input-number v-model="form.turnoverWarnMax" :precision="2" /></el-form-item>
      <el-form-item label="墨水屏轮询间隔秒">
        <el-input-number v-model="form.einkSyncIntervalSec" :min="0" />
        <span class="tip">0 表示近实时（设备每 5 秒拉一次）；V1 不接 MQTT</span>
      </el-form-item>
      <el-form-item label="字体样式"><el-input v-model="form.einkFontStyle" /></el-form-item>
      <el-form-item><el-button type="primary" @click="submit" v-hasPermi="['pms:setting:edit']">保存</el-button></el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getSetting, saveSetting } from "@/api/pms/setting"
export default {
  name: "PmsSetting",
  data() { return { form: {} } },
  created() { getSetting().then(r => this.form = r.data || {}) },
  methods: {
    submit() { saveSetting(this.form).then(() => this.$modal.msgSuccess("保存成功")) }
  }
}
</script>
<style scoped>
.tip { margin-left: 8px; color: #909399; font-size: 12px; }
</style>
