# PMS 角色与权限

超级管理员（若依 `admin`）默认全部权限。其他角色在「系统管理 → 角色管理」勾选「商品管理」菜单即可。

菜单 `component` 对应前端 `src/views/{component}.vue`。

## 目录与页面

| menu_id | 名称 | 路由 path | component | 权限 |
| --- | --- | --- | --- | --- |
| 2100 | 商品管理 | `pms` | — | — |
| 2101 | 经营看板 | `dashboard` | `pms/dashboard/index` | `pms:dashboard:query` |
| 2102 | 商品信息 | `product` | `pms/product/index` | `pms:product:list` |
| 2103 | 商品分类 | `category` | `pms/category/index` | `pms:category:list` |
| 2104 | 供应商 | `supplier` | `pms/supplier/index` | `pms:supplier:list` |
| 2105 | 进货入库 | `purchase` | `pms/purchase/index` | `pms:purchase:list` |
| 2106 | 库存流水 | `stock` | `pms/stock/index` | `pms:stock:log` |
| 2107 | 销售出库 | `sale` | `pms/sale/index` | `pms:sale:list` |
| 2108 | 物流对账 | `logistics` | `pms/logistics/index` | `pms:logistics:list` |
| 2112 | 物流商 | `carrier` | `pms/carrier/index` | `pms:carrier:list` |
| 2109 | 墨水屏设备 | `device` | `pms/device/index` | `pms:device:list` |
| 2110 | 报表统计 | `report` | `pms/report/index` | `pms:report:overview` |
| 2111 | 业务设置 | `setting` | `pms/setting/index` | `pms:setting:query` |

## 按钮

| 模块 | 权限码 |
| --- | --- |
| 商品 | `pms:product:query` / `add` / `edit` / `remove` / `export` / `import` |
| 分类 | `pms:category:query` / `add` / `edit` / `remove` / `export` |
| 供应商 | `pms:supplier:query` / `add` / `edit` / `remove` / `export` |
| 进货 | `pms:purchase:query` / `add` / `export` |
| 库存 | `pms:stock:adjust` / `export`（列表权限是页面上的 `pms:stock:log`） |
| 销售 | `pms:sale:query` / `add` / `export` |
| 物流 | `pms:logistics:query` / `add` / `edit` / `export` |
| 物流商 | `pms:carrier:query` / `add` / `edit` / `remove` / `export` |
| 设备 | `pms:device:query` / `bind` / `unbind` / `operate` / `log` / `export` / `edit` |
| 报表 | `pms:report:purchase` / `stock` / `cost` / `supplier` / `turnover` / `export` |
| 设置 | `pms:setting:edit` |

货品管理员建议：看板 + 商品/分类/供应商 + 进货/库存/销售 + 物流；设备与设置可另配。`/pms/eink/**` 无权限码（匿名）。
