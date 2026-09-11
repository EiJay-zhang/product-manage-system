<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="类型">
        <el-select v-model="queryParams.changeType" clearable>
          <el-option label="入库" value="IN" /><el-option label="盘点" value="CHECK" /><el-option label="损耗" value="LOSS" />
          <el-option label="调拨" value="TRANSFER" /><el-option label="销售" value="SALE" /><el-option label="建档" value="INIT" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdjust" v-hasPermi="['pms:stock:adjust']">库存调整</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:stock:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="商品" prop="productName" />
      <el-table-column label="类型" prop="changeType" />
      <el-table-column label="变动前" prop="beforeQty" />
      <el-table-column label="变动" prop="changeQty" />
      <el-table-column label="结余" prop="afterQty" />
      <el-table-column label="时间" prop="createTime" width="160" />
      <el-table-column label="备注" prop="remark" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog title="库存调整（调拨仅改数量，无多仓）" :visible.sync="open" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商品" prop="productId">
          <el-select v-model="form.productId" filterable><el-option v-for="p in products" :key="p.productId" :label="p.productName + ' 当前' + p.stockQty" :value="p.productId" /></el-select>
        </el-form-item>
        <el-form-item label="类型" prop="changeType">
          <el-select v-model="form.changeType">
            <el-option label="盘点" value="CHECK" /><el-option label="损耗" value="LOSS" /><el-option label="调拨" value="TRANSFER" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整后库存" prop="afterQtyTarget"><el-input-number v-model="form.afterQtyTarget" :min="0" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listStockLog, adjustStock } from "@/api/pms/stock"
import { optionProduct } from "@/api/pms/product"
export default {
  name: "PmsStock",
  data() { return { loading: false, total: 0, list: [], open: false, products: [], queryParams: { pageNum: 1, pageSize: 10, changeType: null }, form: {}, rules: { productId: [{ required: true, message: "请选择商品", trigger: "change" }], afterQtyTarget: [{ required: true, message: "请填写调整后库存", trigger: "blur" }] } } },
  created() { this.getList(); optionProduct().then(r => this.products = r.data || []) },
  methods: {
    getList() { this.loading = true; listStockLog(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdjust() { this.form = { changeType: "CHECK" }; this.open = true },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; adjustStock(this.form).then(() => { this.$modal.msgSuccess("调整成功"); this.open = false; this.getList() }) }) },
    handleExport() { this.download("pms/stock/log/export", { ...this.queryParams }, `stock.xlsx`) }
  }
}
</script>
