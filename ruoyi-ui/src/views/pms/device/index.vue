<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="SN"><el-input v-model="queryParams.sn" clearable /></el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleBind" v-hasPermi="['pms:device:bind']">绑定</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain size="mini" @click="openTpl = true" v-hasPermi="['pms:device:edit']">展示模板</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="SN" prop="sn" />
      <el-table-column label="编号" prop="deviceCode" />
      <el-table-column label="商品" prop="productName" />
      <el-table-column label="货架" prop="shelfNo" />
      <el-table-column label="在线"><template slot-scope="scope">{{ scope.row.onlineStatus === '1' ? '在线' : '离线' }}</template></el-table-column>
      <el-table-column label="心跳" prop="lastHeartbeat" width="160" />
      <el-table-column label="操作" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleRefresh(scope.row.deviceId)" v-hasPermi="['pms:device:operate']">刷新</el-button>
          <el-button size="mini" type="text" @click="handleRestart(scope.row.deviceId)" v-hasPermi="['pms:device:operate']">重启</el-button>
          <el-button size="mini" type="text" @click="handleUnbind(scope.row.deviceId)" v-hasPermi="['pms:device:unbind']">解绑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog title="绑定设备" :visible.sync="open" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="SN" prop="sn"><el-input v-model="form.sn" /></el-form-item>
        <el-form-item label="编号"><el-input v-model="form.deviceCode" /></el-form-item>
        <el-form-item label="商品"><el-select v-model="form.productId" filterable clearable><el-option v-for="p in products" :key="p.productId" :label="p.productName" :value="p.productId" /></el-select></el-form-item>
        <el-form-item label="货架"><el-input v-model="form.shelfNo" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitBind">确定</el-button></div>
    </el-dialog>
    <el-dialog title="展示模板（轮询间隔 0=近实时 5 秒）" :visible.sync="openTpl" width="480px">
      <el-form :model="tpl" label-width="120px">
        <el-form-item label="轮询间隔秒"><el-input-number v-model="tpl.einkSyncIntervalSec" :min="0" /></el-form-item>
        <el-form-item label="字体"><el-input v-model="tpl.einkFontStyle" /></el-form-item>
        <el-form-item label="展示名称"><el-switch v-model="tpl.showName" active-value="Y" inactive-value="N" /></el-form-item>
        <el-form-item label="展示规格"><el-switch v-model="tpl.showSpec" active-value="Y" inactive-value="N" /></el-form-item>
        <el-form-item label="展示售价"><el-switch v-model="tpl.showSalePrice" active-value="Y" inactive-value="N" /></el-form-item>
        <el-form-item label="展示简介"><el-switch v-model="tpl.showIntro" active-value="Y" inactive-value="N" /></el-form-item>
        <el-form-item label="展示库存"><el-switch v-model="tpl.showStock" active-value="Y" inactive-value="N" /></el-form-item>
        <el-form-item label="展示厂家"><el-switch v-model="tpl.showSupplier" active-value="Y" inactive-value="N" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="openTpl=false">取消</el-button><el-button type="primary" @click="submitTpl">保存</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listDevice, bindDevice, unbindDevice, refreshDevice, restartDevice, getTemplate, saveTemplate } from "@/api/pms/device"
import { optionProduct } from "@/api/pms/product"
export default {
  name: "PmsDevice",
  data() {
    return { loading: false, total: 0, list: [], open: false, openTpl: false, products: [], queryParams: { pageNum: 1, pageSize: 10, sn: null }, form: {}, tpl: {}, rules: { sn: [{ required: true, message: "SN不能为空", trigger: "blur" }] } }
  },
  created() { this.getList(); optionProduct().then(r => this.products = r.data || []); getTemplate().then(r => this.tpl = r.data || {}) },
  methods: {
    getList() { this.loading = true; listDevice(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleBind() { this.form = {}; this.open = true },
    submitBind() { this.$refs.form.validate(v => { if (!v) return; bindDevice(this.form).then(() => { this.$modal.msgSuccess("绑定成功"); this.open = false; this.getList() }) }) },
    handleRefresh(id) { refreshDevice(id).then(() => this.$modal.msgSuccess("已下发刷新（设备轮询后生效）")) },
    handleRestart(id) { restartDevice(id).then(() => this.$modal.msgSuccess("已下发重启")) },
    handleUnbind(id) { this.$modal.confirm("确认解绑？").then(() => unbindDevice(id)).then(() => { this.getList(); this.$modal.msgSuccess("已解绑") }).catch(() => {}) },
    submitTpl() { saveTemplate(this.tpl).then(() => { this.$modal.msgSuccess("已保存"); this.openTpl = false }) }
  }
}
</script>
