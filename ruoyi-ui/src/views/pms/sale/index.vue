<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="单号"><el-input v-model="queryParams.saleNo" clearable /></el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['pms:sale:add']">销售出库</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:sale:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="单号" prop="saleNo" width="160" />
      <el-table-column label="商品" prop="productName" />
      <el-table-column label="数量" prop="qty" />
      <el-table-column label="售价" prop="salePrice" />
      <el-table-column label="金额" prop="amount" />
      <el-table-column label="时间" prop="saleTime" width="160" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog title="销售出库（库存不足会失败）" :visible.sync="open" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品" prop="productId">
          <el-select v-model="form.productId" filterable @change="onProduct"><el-option v-for="p in products" :key="p.productId" :label="p.productName + ' 库存' + p.stockQty" :value="p.productId" /></el-select>
        </el-form-item>
        <el-form-item label="数量" prop="qty"><el-input-number v-model="form.qty" :min="1" /></el-form-item>
        <el-form-item label="售价" prop="salePrice"><el-input-number v-model="form.salePrice" :min="0.01" :precision="2" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listSale, addSale } from "@/api/pms/sale"
import { optionProduct } from "@/api/pms/product"
export default {
  name: "PmsSale",
  data() { return { loading: false, total: 0, list: [], open: false, products: [], queryParams: { pageNum: 1, pageSize: 10, saleNo: null }, form: {}, rules: { productId: [{ required: true, message: "请选择商品", trigger: "change" }], qty: [{ required: true, message: "数量不能为空", trigger: "blur" }] } } },
  created() { this.getList(); optionProduct().then(r => this.products = r.data || []) },
  methods: {
    getList() { this.loading = true; listSale(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdd() { this.form = { qty: 1 }; this.open = true },
    onProduct(id) { const p = this.products.find(i => i.productId === id); if (p) this.form.salePrice = p.salePrice },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; addSale(this.form).then(() => { this.$modal.msgSuccess("出库成功"); this.open = false; this.getList() }) }) },
    handleExport() { this.download("pms/sale/export", { ...this.queryParams }, `sale.xlsx`) }
  }
}
</script>
