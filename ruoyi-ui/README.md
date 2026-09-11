# PMS 前端页面（若依 Vue 接入包）

本目录不是完整的 Vue 工程，而是可拷贝进标准 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 的 PMS 页面与 API。

后端菜单 `component` 已指向 `pms/*/index`（见 `sql/pms.sql`），登录后动态路由会加载这些页面，无需再手写 `router`。

## 拷贝

把下列文件复制到若依前端工程对应位置：

```
ruoyi-ui/src/api/pms/*.js      → src/api/pms/
ruoyi-ui/src/views/pms/**      → src/views/pms/
```

依赖若依已有能力：`@/utils/request`、`@/utils/auth`、`v-hasPermi`、`this.download`、`pagination`、`right-toolbar`。

## 开发代理

`.env.development`：

```
VUE_APP_BASE_API = '/dev-api'
```

`vue.config.js` 把 `/dev-api` 代理到 `http://localhost:8080`。

## 页面清单（12）

建议联调顺序：商品 → 分类/供应商 → 进货/库存/销售 → 物流商/物流对账 → 看板/报表 → 设备/设置。

| 菜单 | 组件 | 说明 |
| --- | --- | --- |
| 经营看板 | `views/pms/dashboard/index` | 库存预警、零库存、离线设备、待对账、今日营业额 |
| 商品信息 | `views/pms/product/index` | CRUD、导出、Excel 导入（厂家/分类按名称匹配） |
| 商品分类 | `views/pms/category/index` | CRUD、导出 |
| 供应商 | `views/pms/supplier/index` | CRUD、关联商品/进货 |
| 进货入库 | `views/pms/purchase/index` | 只增不改不删 |
| 库存流水 | `views/pms/stock/index` | 流水 + 盘点/损耗/调拨（改数量，无仓位） |
| 销售出库 | `views/pms/sale/index` | 只增；超卖由后端拒绝 |
| 物流对账 | `views/pms/logistics/index` | 录入/编辑/对账/异常；无删除 |
| 物流商 | `views/pms/carrier/index` | 有账单的物流商不能删 |
| 墨水屏设备 | `views/pms/device/index` | 绑定/解绑/刷新/重启（设备轮询后生效） |
| 报表统计 | `views/pms/report/index` | 总览/进货/库存/成本/供应商/营业额 |
| 业务设置 | `views/pms/setting/index` | 预警阈值、墨水屏轮询间隔 |

接口约定见仓库 `docs/api.md`，权限码见 `docs/permissions.md`。
