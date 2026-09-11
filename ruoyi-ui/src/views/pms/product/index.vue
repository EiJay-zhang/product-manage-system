<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="商品名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" clearable placeholder="分类">
          <el-option v-for="c in categoryOptions" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="厂家" prop="supplierId">
        <el-select v-model="queryParams.supplierId" clearable filterable placeholder="厂家">
          <el-option v-for="s in supplierOptions" :key="s.supplierId" :label="s.supplierName" :value="s.supplierId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['pms:product:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['pms:product:export']">导出</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['pms:product:import']">导入</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="list" @selection-change="rows => ids = rows.map(i => i.productId)">
      <el-table-column type="selection" width="55" />
      <el-table-column label="编号" prop="productCode" width="130" />
      <el-table-column label="名称" prop="productName" />
      <el-table-column label="规格" prop="spec" />
      <el-table-column label="分类" prop="categoryName" />
      <el-table-column label="厂家" prop="supplierName" />
      <el-table-column label="进价" prop="purchasePrice" />
      <el-table-column label="售价" prop="salePrice" />
      <el-table-column label="库存" prop="stockQty" />
      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-hasPermi="['pms:product:edit']">修改</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-hasPermi="['pms:product:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="名称" prop="productName"><el-input v-model="form.productName" /></el-form-item>
        <el-form-item label="规格" prop="spec"><el-input v-model="form.spec" /></el-form-item>
        <el-form-item label="分类"><el-select v-model="form.categoryId" clearable><el-option v-for="c in categoryOptions" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" /></el-select></el-form-item>
        <el-form-item label="厂家" prop="supplierId"><el-select v-model="form.supplierId" filterable><el-option v-for="s in supplierOptions" :key="s.supplierId" :label="s.supplierName" :value="s.supplierId" /></el-select></el-form-item>
        <el-form-item label="进价" prop="purchasePrice"><el-input-number v-model="form.purchasePrice" :min="0.01" :precision="2" /></el-form-item>
        <el-form-item label="售价" prop="salePrice"><el-input-number v-model="form.salePrice" :min="0.01" :precision="2" /></el-form-item>
        <el-form-item label="库存" prop="stockQty"><el-input-number v-model="form.stockQty" :min="0" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.intro" type="textarea" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <el-dialog title="导入商品" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :action="upload.url" :headers="upload.headers" :disabled="upload.isUploading" :on-progress="() => upload.isUploading=true" :on-success="handleImportSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </el-upload>
      <div class="el-upload__tip"><el-link type="primary" :underline="false" @click="importTemplate">下载模板</el-link></div>
      <div slot="footer"><el-button @click="upload.open=false">取消</el-button><el-button type="primary" @click="$refs.upload.submit()">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listProduct, getProduct, addProduct, updateProduct, delProduct } from "@/api/pms/product"
import { optionCategory } from "@/api/pms/category"
import { optionSupplier } from "@/api/pms/supplier"
import { getToken } from "@/utils/auth"
export default {
  name: "PmsProduct",
  data() {
    return {
      loading: false, ids: [], showSearch: true, total: 0, list: [], title: "", open: false,
      categoryOptions: [], supplierOptions: [],
      queryParams: { pageNum: 1, pageSize: 10, productName: null, categoryId: null, supplierId: null },
      form: {},
      rules: {
        productName: [{ required: true, message: "商品名称不能为空", trigger: "blur" }],
        spec: [{ required: true, message: "规格不能为空", trigger: "blur" }],
        supplierId: [{ required: true, message: "请选择厂家", trigger: "change" }],
        purchasePrice: [{ required: true, message: "进价不能为空", trigger: "blur" }],
        salePrice: [{ required: true, message: "售价不能为空", trigger: "blur" }],
        stockQty: [{ required: true, message: "库存不能为空", trigger: "blur" }]
      },
      upload: { open: false, isUploading: false, headers: { Authorization: "Bearer " + getToken() }, url: process.env.VUE_APP_BASE_API + "/pms/product/importData" }
    }
  },
  created() {
    this.getList()
    optionCategory().then(r => this.categoryOptions = r.data || [])
    optionSupplier().then(r => this.supplierOptions = r.data || [])
  },
  methods: {
    getList() {
      this.loading = true
      listProduct(this.queryParams).then(res => { this.list = res.rows; this.total = res.total; this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    reset() { this.form = { stockQty: 0 } },
    handleAdd() { this.reset(); this.open = true; this.title = "新增商品" },
    handleUpdate(row) {
      this.reset()
      getProduct(row.productId).then(res => { this.form = res.data; this.open = true; this.title = "修改商品" })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const req = this.form.productId ? updateProduct : addProduct
        req(this.form).then(() => { this.$modal.msgSuccess("操作成功"); this.open = false; this.getList() })
      })
    },
    handleDelete(row) {
      const ids = row.productId || this.ids
      this.$modal.confirm('是否确认删除？').then(() => delProduct(ids)).then(() => { this.getList(); this.$modal.msgSuccess("删除成功") }).catch(() => {})
    },
    handleExport() { this.download('pms/product/export', { ...this.queryParams }, `product_${new Date().getTime()}.xlsx`) },
    handleImport() { this.upload.open = true },
    importTemplate() { this.download('pms/product/importTemplate', {}, `product_template.xlsx`) },
    handleImportSuccess(res) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$alert(res.msg, "导入结果", { dangerouslyUseHTMLString: true })
      this.getList()
    }
  }
}
</script>
