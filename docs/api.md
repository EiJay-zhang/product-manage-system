# PMS 前端对接说明

基础地址：`http://localhost:8080`。Swagger：`/swagger-ui/index.html`（分组「商品管理」）。

## 通用约定

- 业务前缀：`/pms/**`
- 鉴权：请求头 `Authorization: Bearer <token>`（登录 `POST /login`，验证码 `GET /captchaImage`）
- 分页列表：`pageNum`、`pageSize`，响应 `{ rows, total }`（若依 `TableDataInfo`）
- 其余接口：`{ code: 200, msg, data }`（`AjaxResult`）
- 时间：JSON 多为 `yyyy-MM-dd` 或 `yyyy-MM-dd HH:mm:ss`
- 设备端 `/pms/eink/**` 免登录，见 [eink-protocol.md](eink-protocol.md)

## 单据规则

| 模块 | 规则 |
| --- | --- |
| 进货 `/pms/purchase` | 只增不改不删 |
| 销售 `/pms/sale` | 只增不改不删；库存不足拒绝 |
| 物流 `/pms/logistics` | 可改、可对账、禁止删除 |
| 库存调整 `/pms/stock/adjust` | 类型仅 `CHECK` / `LOSS` / `TRANSFER`，传目标库存 `afterQtyTarget` |
| 商品快照 | `pms_product` 上进价/库存/厂家是当前值；历史看进货/销售流水 |

## 枚举

| 用途 | 值 |
| --- | --- |
| 删除标志 | `0` 正常 / `2` 删除 |
| 库存变动 | `IN` 入库 / `CHECK` 盘点 / `LOSS` 损耗 / `TRANSFER` 调拨 / `SALE` 销售 / `INIT` 建档 |
| 物流付款 | `0` 未对账 / `1` 已对账 / `2` 已结清 |
| 设备在线 | `0` 离线 / `1` 在线（心跳超过 120 秒视为离线） |
| 同步状态 | `0` 失败 / `1` 成功 / `2` 待确认 |
| 报表区间 `range` | `today` / `week` / `month` / `year` |

`TRANSFER` 只改一个商品数量，没有调出仓/调入仓。库存成本 = 最新进价 × 当前库存，不是 FIFO。

## 接口一览

### 经营看板

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/dashboard` | `pms:dashboard:query` |

`data`：`productCount`、`warnCount`、`emptyCount`、`offlineDeviceCount`、`uncheckedCount`、`unpaidCount`、`todayTurnover`、`turnoverWarn`。

### 商品

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/product/list` | `pms:product:list` |
| GET | `/pms/product/optionselect` | `pms:product:query` |
| GET | `/pms/product/{productId}` | `pms:product:query` |
| POST | `/pms/product` | `pms:product:add` |
| PUT | `/pms/product` | `pms:product:edit` |
| DELETE | `/pms/product/{productIds}` | `pms:product:remove` |
| POST | `/pms/product/export` | `pms:product:export` |
| POST | `/pms/product/importData` | `pms:product:import` |
| POST | `/pms/product/importTemplate` | 登录即可（下载模板） |

导入 Excel 列：商品名称、规格、分类、进货厂家、进价、售价、库存、进货时间、简介。厂家/分类按**名称**匹配已有档案；编号由系统生成。库存 > 0 会自动记一笔首次进货。已绑定墨水屏的商品不能删除。

### 分类 / 供应商 / 物流商

CRUD + `optionselect` + `export`。供应商另有 `GET /{id}/products`、`GET /{id}/purchases`。物流商另有 `GET /{id}/logistics`。有物流单的物流商不能删除。

### 进货 / 销售

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/purchase/list` | `pms:purchase:list` |
| GET | `/pms/purchase/{purchaseId}` | `pms:purchase:query` |
| POST | `/pms/purchase` | `pms:purchase:add` |
| POST | `/pms/purchase/export` | `pms:purchase:export` |
| GET | `/pms/sale/list` | `pms:sale:list` |
| GET | `/pms/sale/{saleId}` | `pms:sale:query` |
| POST | `/pms/sale` | `pms:sale:add` |
| POST | `/pms/sale/export` | `pms:sale:export` |

进货会更新商品当前进价、厂家、库存。销售数量必须为正整数，且不超过当前库存。

### 库存

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/stock/log/list` | `pms:stock:log` |
| PUT | `/pms/stock/adjust` | `pms:stock:adjust` |
| POST | `/pms/stock/log/export` | `pms:stock:export` |

调整体：`{ productId, afterQtyTarget, changeType, remark }`。

### 物流对账

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/logistics/list` | `pms:logistics:list` |
| GET | `/pms/logistics/{id}` | `pms:logistics:query` |
| POST | `/pms/logistics` | `pms:logistics:add` |
| PUT | `/pms/logistics` | `pms:logistics:edit` |
| PUT | `/pms/logistics/status` | `pms:logistics:edit` |
| PUT | `/pms/logistics/mark` | `pms:logistics:edit` |
| POST | `/pms/logistics/batchReconcile` | `pms:logistics:edit` |
| GET | `/pms/logistics/stats/monthly` | `pms:logistics:query` |
| GET | `/pms/logistics/stats/carrier` | `pms:logistics:query` |
| GET | `/pms/logistics/stats/supplier` | `pms:logistics:query` |
| POST | `/pms/logistics/export` | `pms:logistics:export` |

录入可传 `carrierId`，名称从物流商档案快照到 `carrier`。一笔物流绑一个进货批次，运费不按 SKU 分摊。

### 墨水屏管理（后台）

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/device/list` | `pms:device:list` |
| GET | `/pms/device/{deviceId}` | `pms:device:query` |
| GET | `/pms/device/template` | `pms:device:query` |
| PUT | `/pms/device/template` | `pms:device:edit` |
| POST | `/pms/device/bind` | `pms:device:bind` |
| PUT | `/pms/device/unbind/{deviceId}` | `pms:device:unbind` |
| PUT | `/pms/device/refresh/{deviceId}` | `pms:device:operate` |
| PUT | `/pms/device/restart/{deviceId}` | `pms:device:operate` |
| PUT | `/pms/device/mark/{deviceId}` | `pms:device:operate` |
| POST | `/pms/device/batchRefresh` | `pms:device:operate` |
| GET | `/pms/device/syncLog/list` | `pms:device:log` |
| POST | `/pms/device/syncLog/export` | `pms:device:export` |

刷新/重启写入 `pms_device_sync_log`（待确认），设备按 [eink-protocol.md](eink-protocol.md) 轮询拉取，**V1 不接 MQTT**。

### 报表

查询参数：`range`、`beginTime`、`endTime`、`productName`、`categoryId`、`supplierId`。

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/report/overview` | `pms:report:overview` |
| GET | `/pms/report/purchase` | `pms:report:purchase` |
| GET | `/pms/report/stock` | `pms:report:stock` |
| GET | `/pms/report/cost` | `pms:report:cost` |
| GET | `/pms/report/supplier` | `pms:report:supplier` |
| GET | `/pms/report/turnover` | `pms:report:turnover` |
| GET | `/pms/report/turnover/trend` | `pms:report:turnover` |
| GET | `/pms/report/turnover/rank` | `pms:report:turnover` |
| GET | `/pms/report/turnover/compare` | `pms:report:turnover` |
| GET | `/pms/report/turnover/detail` | `pms:report:turnover` |
| POST | `/pms/report/export/{reportType}` | `pms:report:export` |

`reportType`：`overview` / `purchase` / `stock` / `cost` / `supplier` / `turnover`。进货/库存接口的 `data` 为 `{ rows, total }`。

### 业务设置

| 方法 | 路径 | 权限 |
| --- | --- | --- |
| GET | `/pms/setting` | `pms:setting:query` |
| PUT | `/pms/setting` | `pms:setting:edit` |

`einkSyncIntervalSec`：设备轮询间隔（秒）。`null` 或 `<=0` 时后端下发 **5 秒**近实时轮询。

操作日志复用若依 `/monitor/operlog`，业务写接口已打 `@Log`。
