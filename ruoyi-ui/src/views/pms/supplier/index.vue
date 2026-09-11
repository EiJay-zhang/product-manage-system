<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="厂家"><el-input v-model="queryParams.supplierName" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['pms:supplier:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:supplier:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="厂家" prop="supplierName" />
      <el-table-column label="联系人" prop="contactName" />
      <el-table-column label="电话" prop="phone" />
      <el-table-column label="状态" prop="status"><template slot-scope="scope">{{ scope.row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-hasPermi="['pms:supplier:edit']">修改</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-hasPermi="['pms:supplier:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="supplierName"><el-input v-model="form.supplierName" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contactName" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio label="0">正常</el-radio><el-radio label="1">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listSupplier, getSupplier, addSupplier, updateSupplier, delSupplier } from "@/api/pms/supplier"
export default {
  name: "PmsSupplier",
  data() { return { loading: false, total: 0, list: [], title: "", open: false, queryParams: { pageNum: 1, pageSize: 10, supplierName: null }, form: {}, rules: { supplierName: [{ required: true, message: "厂家名称不能为空", trigger: "blur" }] } } },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listSupplier(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdd() { this.form = { status: "0" }; this.open = true; this.title = "新增厂家" },
    handleUpdate(row) { getSupplier(row.supplierId).then(r => { this.form = r.data; this.open = true; this.title = "修改厂家" }) },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; (this.form.supplierId ? updateSupplier : addSupplier)(this.form).then(() => { this.$modal.msgSuccess("操作成功"); this.open = false; this.getList() }) }) },
    handleDelete(row) { this.$modal.confirm("确认删除？").then(() => delSupplier(row.supplierId)).then(() => { this.getList(); this.$modal.msgSuccess("删除成功") }).catch(() => {}) },
    handleExport() { this.download("pms/supplier/export", { ...this.queryParams }, `supplier.xlsx`) }
  }
}
</script>
