<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="单号"><el-input v-model="queryParams.purchaseNo" clearable /></el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['pms:purchase:add']">进货入库</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:purchase:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="单号" prop="purchaseNo" width="160" />
      <el-table-column label="商品" prop="productName" />
      <el-table-column label="规格" prop="spec" />
      <el-table-column label="厂家" prop="supplierName" />
      <el-table-column label="数量" prop="qty" />
      <el-table-column label="进价" prop="purchasePrice" />
      <el-table-column label="金额" prop="amount" />
      <el-table-column label="进货日期" prop="purchaseTime" width="120" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog title="进货入库（只增不改不删）" :visible.sync="open" width="520px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="商品" prop="productId">
          <el-select v-model="form.productId" filterable @change="onProduct"><el-option v-for="p in products" :key="p.productId" :label="p.productName + ' / ' + p.spec" :value="p.productId" /></el-select>
        </el-form-item>
        <el-form-item label="厂家" prop="supplierId">
          <el-select v-model="form.supplierId"><el-option v-for="s in suppliers" :key="s.supplierId" :label="s.supplierName" :value="s.supplierId" /></el-select>
        </el-form-item>
        <el-form-item label="数量" prop="qty"><el-input-number v-model="form.qty" :min="1" /></el-form-item>
        <el-form-item label="进价" prop="purchasePrice"><el-input-number v-model="form.purchasePrice" :min="0.01" :precision="2" /></el-form-item>
        <el-form-item label="进货日期" prop="purchaseTime"><el-date-picker v-model="form.purchaseTime" type="date" value-format="yyyy-MM-dd" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listPurchase, addPurchase } from "@/api/pms/purchase"
import { optionProduct } from "@/api/pms/product"
import { optionSupplier } from "@/api/pms/supplier"
export default {
  name: "PmsPurchase",
  data() {
    return {
      loading: false, total: 0, list: [], open: false, products: [], suppliers: [],
      queryParams: { pageNum: 1, pageSize: 10, purchaseNo: null },
      form: {},
      rules: { productId: [{ required: true, message: "请选择商品", trigger: "change" }], qty: [{ required: true, message: "数量不能为空", trigger: "blur" }], purchasePrice: [{ required: true, message: "进价不能为空", trigger: "blur" }] }
    }
  },
  created() { this.getList(); optionProduct().then(r => this.products = r.data || []); optionSupplier().then(r => this.suppliers = r.data || []) },
  methods: {
    getList() { this.loading = true; listPurchase(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdd() { this.form = { qty: 1 }; this.open = true },
    onProduct(id) { const p = this.products.find(i => i.productId === id); if (p) { this.form.supplierId = p.supplierId; this.form.purchasePrice = p.purchasePrice } },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; addPurchase(this.form).then(() => { this.$modal.msgSuccess("入库成功"); this.open = false; this.getList() }) }) },
    handleExport() { this.download("pms/purchase/export", { ...this.queryParams }, `purchase.xlsx`) }
  }
}
</script>
