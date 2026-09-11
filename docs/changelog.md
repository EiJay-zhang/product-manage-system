# 接口变更记录

面向前端与固件，避免漏接后补模块。

## 物流商（后补模块）

- 前缀 `/pms/carrier`：列表、下拉、详情、CRUD、导出、`/{id}/logistics`
- 物流单字段 `carrierId`（名称快照 `carrier`）
- 菜单 2112，按钮 2292–2296
- 已有库执行 `sql/pms_carrier.sql`

## 商品导入 / 分类导出

- `POST /pms/product/importData` 权限 `pms:product:import`（菜单 2206）
- `POST /pms/product/importTemplate` 下载模板
- `POST /pms/category/export` 权限 `pms:category:export`（菜单 2215）
- 已有库执行 `sql/pms_import_perm.sql`

## 墨水屏轮询（V1 不定 MQTT）

- 心跳响应根字段：`pollIntervalSec`、`pendingCommand`、`pendingLogId`
- 内容接口 VO 同样包含上述字段
- `eink_sync_interval_sec`：`<=0` 时下发 5 秒；`>0` 按下发秒数轮询
- 刷新/重启仍写 `pms_device_sync_log`，设备 ack 后关闭待确认

## 口径（产品已确认维持）

- 库存成本 = 最新进价 × 库存（非 FIFO）
- `TRANSFER` 只改数量，无多仓库
