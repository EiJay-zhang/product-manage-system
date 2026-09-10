# 商品管理系统（后端）

基于 [RuoYi-Vue 3.9.2](https://gitee.com/y_project/RuoYi-Vue) 的商品进销存后端，业务模块为 `ruoyi-pms`。本仓库**只有后端**，前端使用独立的若依 Vue 工程对接。

## 技术栈

| 项 | 版本 |
| --- | --- |
| JDK | 17+ |
| Spring Boot | 4.1.0 |
| 权限 | Spring Security + JWT |
| 持久化 | MyBatis + MySQL 8 |
| 缓存 | Redis |
| 接口文档 | SpringDoc / Swagger |

## 模块

```
product-manage-system
├── ruoyi-admin       # 启动入口、配置、Swagger
├── ruoyi-framework   # Security、Redis、数据源
├── ruoyi-system      # 用户、角色、菜单等系统能力
├── ruoyi-common      # 公共工具
├── ruoyi-quartz      # 定时任务
├── ruoyi-generator   # 代码生成
├── ruoyi-pms         # 商品管理业务
└── sql               # 初始化脚本
```

## 业务能力

- 经营看板
- 商品档案、分类
- 供应商、进货入库（进货台账只增不改不删）
- 库存流水（入库 / 盘点 / 损耗 / 调拨 / 销售）
- 销售出库（只增不改不删，营业额数据源）
- 物流商维护、物流对账（物流单禁止删除）
- 墨水屏设备绑定与同步（设备端心跳接口免登录）
- 报表统计、业务设置

## 环境要求

- JDK 17（本机可用 Homebrew：`/opt/homebrew/opt/openjdk@17`）
- Maven 3.9+
- MySQL 8，库名 `pms`
- Redis 6+

数据源与 Redis 写在：

- `ruoyi-admin/src/main/resources/application-druid.yml`（MySQL）
- `ruoyi-admin/src/main/resources/application.yml`（Redis、端口、Token）

本机开发请把 Redis 配成**公网地址**；应用部署在同一台云主机内网时，再改成私网地址。日志目录默认 `${user.home}/logs/ruoyi`，上传目录默认 `${user.home}/ruoyi/uploadPath`。

## 初始化数据库

按顺序在 `pms` 库执行：

1. `sql/ry_20260417.sql` — 若依系统表
2. `sql/quartz.sql` — 定时任务表
3. `sql/pms.sql` — 业务表、菜单、字典

已有库补物流商时，再执行 `sql/pms_carrier.sql`。

默认账号：`admin` / `admin123`。首次登录后请改密。

## 启动

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"

# 编译
mvn -pl ruoyi-pms,ruoyi-admin -am install -DskipTests

# 启动（必须在 ruoyi-admin 目录，不要对父工程执行 spring-boot:run）
cd ruoyi-admin
mvn spring-boot:run
```

启动后：

- 服务：`http://localhost:8080`
- Swagger：`http://localhost:8080/swagger-ui/index.html`（分组「商品管理」）

## 打包

产物文件名是 `pms.jar`（`ruoyi-admin/pom.xml` 的 `finalName`）。

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"

mvn -pl ruoyi-admin -am package -DskipTests
```

产物路径：`ruoyi-admin/target/pms.jar`。生产部署说明见 `deploy/README.md`。

## 前端对接

若依前端开发环境建议：

```
# .env.development
VUE_APP_BASE_API = '/dev-api'
```

`vue.config.js` 代理目标：`http://localhost:8080`。

请求头：`Authorization: Bearer <token>`。登录接口为 `POST /login`，验证码为 `GET /captchaImage`。

业务接口前缀均为 `/pms/**`：

| 模块 | 前缀 | 前端路由（约定） |
| --- | --- | --- |
| 经营看板 | `/pms/dashboard` | `pms/dashboard/index` |
| 商品信息 | `/pms/product` | `pms/product/index` |
| 商品分类 | `/pms/category` | `pms/category/index` |
| 供应商 | `/pms/supplier` | `pms/supplier/index` |
| 进货入库 | `/pms/purchase` | `pms/purchase/index` |
| 库存流水 | `/pms/stock` | `pms/stock/index` |
| 销售出库 | `/pms/sale` | `pms/sale/index` |
| 物流对账 | `/pms/logistics` | `pms/logistics/index` |
| 物流商 | `/pms/carrier` | `pms/carrier/index` |
| 墨水屏设备 | `/pms/device` | `pms/device/index` |
| 设备端（免登录） | `/pms/eink` | — |
| 报表统计 | `/pms/report` | `pms/report/index` |
| 业务设置 | `/pms/setting` | `pms/setting/index` |

权限标识形如 `pms:product:list`，与菜单按钮一致。超级管理员角色默认拥有全部权限；其他角色需在系统管理里分配菜单。

## 业务约定

- 商品上的进价、库存、厂家是**当前快照**；历史以进货、销售流水为准。
- 新建商品且库存大于 0 时，会自动记一笔首次进货。
- 库存成本按「最新进价 × 当前库存」估算，不是 FIFO。
- 物流费用与进货批次 1:1，未按 SKU 分摊。
- 物流单录入可传 `carrierId`，名称从物流商档案带出；已有关联账单的物流商不能删除。
- 墨水屏刷新目前是 HTTP 同步日志，不是真实 MQTT 推送。

## 许可证

沿用若依 MIT License。
