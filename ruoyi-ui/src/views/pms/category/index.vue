<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="名称" prop="categoryName"><el-input v-model="queryParams.categoryName" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['pms:category:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:category:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="名称" prop="categoryName" />
      <el-table-column label="排序" prop="orderNum" />
      <el-table-column label="状态" prop="status"><template slot-scope="scope">{{ scope.row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-hasPermi="['pms:category:edit']">修改</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-hasPermi="['pms:category:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="categoryName"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.orderNum" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio label="0">正常</el-radio><el-radio label="1">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listCategory, getCategory, addCategory, updateCategory, delCategory } from "@/api/pms/category"
export default {
  name: "PmsCategory",
  data() {
    return { loading: false, total: 0, list: [], title: "", open: false, queryParams: { pageNum: 1, pageSize: 10, categoryName: null }, form: {}, rules: { categoryName: [{ required: true, message: "分类名称不能为空", trigger: "blur" }] } }
  },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listCategory(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdd() { this.form = { status: "0", orderNum: 0 }; this.open = true; this.title = "新增分类" },
    handleUpdate(row) { getCategory(row.categoryId).then(r => { this.form = r.data; this.open = true; this.title = "修改分类" }) },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; (this.form.categoryId ? updateCategory : addCategory)(this.form).then(() => { this.$modal.msgSuccess("操作成功"); this.open = false; this.getList() }) }) },
    handleDelete(row) { this.$modal.confirm("确认删除？").then(() => delCategory(row.categoryId)).then(() => { this.getList(); this.$modal.msgSuccess("删除成功") }).catch(() => {}) },
    handleExport() { this.download("pms/category/export", { ...this.queryParams }, `category.xlsx`) }
  }
}
</script>
