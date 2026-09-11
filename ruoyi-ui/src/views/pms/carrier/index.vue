<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="物流商"><el-input v-model="queryParams.carrierName" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['pms:carrier:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:carrier:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="物流商" prop="carrierName" />
      <el-table-column label="联系人" prop="contactName" />
      <el-table-column label="电话" prop="phone" />
      <el-table-column label="状态"><template slot-scope="scope">{{ scope.row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-hasPermi="['pms:carrier:edit']">修改</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)" v-hasPermi="['pms:carrier:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="名称" prop="carrierName"><el-input v-model="form.carrierName" /></el-form-item>
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
import { listCarrier, getCarrier, addCarrier, updateCarrier, delCarrier } from "@/api/pms/carrier"
export default {
  name: "PmsCarrier",
  data() { return { loading: false, total: 0, list: [], title: "", open: false, queryParams: { pageNum: 1, pageSize: 10, carrierName: null }, form: {}, rules: { carrierName: [{ required: true, message: "物流商名称不能为空", trigger: "blur" }] } } },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listCarrier(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdd() { this.form = { status: "0" }; this.open = true; this.title = "新增物流商" },
    handleUpdate(row) { getCarrier(row.carrierId).then(r => { this.form = r.data; this.open = true; this.title = "修改物流商" }) },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; (this.form.carrierId ? updateCarrier : addCarrier)(this.form).then(() => { this.$modal.msgSuccess("操作成功"); this.open = false; this.getList() }) }) },
    handleDelete(row) { this.$modal.confirm("有物流单的物流商不能删除，确认删除？").then(() => delCarrier(row.carrierId)).then(() => { this.getList(); this.$modal.msgSuccess("删除成功") }).catch(() => {}) },
    handleExport() { this.download("pms/carrier/export", { ...this.queryParams }, `carrier.xlsx`) }
  }
}
</script>
