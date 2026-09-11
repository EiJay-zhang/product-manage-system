<template>
  <div class="app-container">
    <el-form :model="queryParams" size="small" :inline="true">
      <el-form-item label="物流单号"><el-input v-model="queryParams.logisticsNo" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.payStatus" clearable>
          <el-option label="未对账" value="0" /><el-option label="已对账" value="1" /><el-option label="已结清" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['pms:logistics:add']">录入</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain size="mini" :disabled="!ids.length" @click="handleReconcile" v-hasPermi="['pms:logistics:edit']">批量对账</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain size="mini" @click="handleExport" v-hasPermi="['pms:logistics:export']">导出</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="list" @selection-change="rows => ids = rows.map(i => i.logisticsId)">
      <el-table-column type="selection" width="50" />
      <el-table-column label="物流单号" prop="logisticsNo" width="140" />
      <el-table-column label="进货单" prop="purchaseNo" />
      <el-table-column label="商品" prop="productName" />
      <el-table-column label="物流商" prop="carrier" />
      <el-table-column label="总费用" prop="totalFee" />
      <el-table-column label="状态" prop="payStatus">
        <template slot-scope="scope">{{ { '0': '未对账', '1': '已对账', '2': '已结清' }[scope.row.payStatus] }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-hasPermi="['pms:logistics:edit']">编辑</el-button>
          <el-button size="mini" type="text" @click="handleMark(scope.row)" v-hasPermi="['pms:logistics:edit']">异常</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="560px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="物流单号" prop="logisticsNo"><el-input v-model="form.logisticsNo" :disabled="!!form.logisticsId" /></el-form-item>
        <el-form-item label="进货批次" prop="purchaseId" v-if="!form.logisticsId"><el-input-number v-model="form.purchaseId" :min="1" /></el-form-item>
        <el-form-item label="物流商">
          <el-select v-model="form.carrierId" clearable filterable>
            <el-option v-for="c in carriers" :key="c.carrierId" :label="c.carrierName" :value="c.carrierId" />
          </el-select>
        </el-form-item>
        <el-form-item label="运费"><el-input-number v-model="form.freight" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="保价费"><el-input-number v-model="form.insuranceFee" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="其他杂费"><el-input-number v-model="form.otherFee" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="发货时间"><el-date-picker v-model="form.shipTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" /></el-form-item>
        <el-form-item label="到货时间"><el-date-picker v-model="form.arriveTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" /></el-form-item>
        <el-form-item label="重量/体积"><el-input v-model="form.weightVolume" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="open=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listLogistics, getLogistics, addLogistics, updateLogistics, markLogistics, batchReconcile } from "@/api/pms/logistics"
import { optionCarrier } from "@/api/pms/carrier"
export default {
  name: "PmsLogistics",
  data() {
    return {
      loading: false, total: 0, list: [], ids: [], open: false, title: "", carriers: [],
      queryParams: { pageNum: 1, pageSize: 10, logisticsNo: null, payStatus: null },
      form: {},
      rules: { logisticsNo: [{ required: true, message: "物流单号不能为空", trigger: "blur" }], purchaseId: [{ required: true, message: "请关联进货批次", trigger: "blur" }] }
    }
  },
  created() { this.getList(); optionCarrier().then(r => this.carriers = r.data || []) },
  methods: {
    getList() { this.loading = true; listLogistics(this.queryParams).then(r => { this.list = r.rows; this.total = r.total; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleAdd() { this.form = { freight: 0, insuranceFee: 0, otherFee: 0 }; this.open = true; this.title = "录入物流单" },
    handleUpdate(row) { getLogistics(row.logisticsId).then(r => { this.form = r.data; this.open = true; this.title = "编辑物流单" }) },
    submitForm() { this.$refs.form.validate(v => { if (!v) return; (this.form.logisticsId ? updateLogistics : addLogistics)(this.form).then(() => { this.$modal.msgSuccess("操作成功"); this.open = false; this.getList() }) }) },
    handleMark(row) { this.$prompt("异常备注", "标记异常").then(({ value }) => markLogistics({ logisticsId: row.logisticsId, abnormalRemark: value })).then(() => { this.$modal.msgSuccess("已标记"); this.getList() }).catch(() => {}) },
    handleReconcile() { batchReconcile({ ids: this.ids }).then(() => { this.$modal.msgSuccess("对账成功"); this.getList() }) },
    handleExport() { this.download("pms/logistics/export", { ...this.queryParams }, `logistics.xlsx`) }
  }
}
</script>
