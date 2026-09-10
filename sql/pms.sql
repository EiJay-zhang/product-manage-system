-- ----------------------------
-- 商品管理系统业务表 + 菜单权限
-- 执行前请先导入 sql/ry_20260417.sql 与 sql/quartz.sql
-- ----------------------------
create database if not exists pms default character set utf8mb4 collate utf8mb4_general_ci;
use pms;

-- ----------------------------
-- 供应商
-- ----------------------------
drop table if exists pms_supplier;
create table pms_supplier (
  supplier_id     bigint(20)      not null auto_increment    comment '厂家ID',
  supplier_name   varchar(100)    not null                   comment '厂家名称',
  contact_name    varchar(50)     default ''                 comment '联系人',
  phone           varchar(20)     default ''                 comment '联系电话',
  status          char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag        char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (supplier_id),
  unique key uk_supplier_name (supplier_name)
) engine=innodb comment = '供应商（进货厂家）表';

-- ----------------------------
-- 商品分类
-- ----------------------------
drop table if exists pms_category;
create table pms_category (
  category_id     bigint(20)      not null auto_increment    comment '分类ID',
  category_name   varchar(50)     not null                   comment '分类名称',
  order_num       int(4)          default 0                  comment '显示顺序',
  status          char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag        char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (category_id),
  unique key uk_category_name (category_name)
) engine=innodb comment = '商品分类表';

-- ----------------------------
-- 商品档案（库存、最新进价等为当前快照；历史以进货/销售流水为准）
-- ----------------------------
drop table if exists pms_product;
create table pms_product (
  product_id      bigint(20)      not null auto_increment    comment '商品ID',
  product_code    varchar(32)     not null                   comment '商品编号（系统生成）',
  product_name    varchar(100)    not null                   comment '商品名称',
  spec            varchar(100)    not null                   comment '商品规格',
  category_id     bigint(20)      default null               comment '分类ID',
  supplier_id     bigint(20)      not null                   comment '当前供货厂家ID',
  purchase_price  decimal(10,2)   not null                   comment '当前进价（最近一次进货）',
  sale_price      decimal(10,2)   not null                   comment '售价',
  stock_qty       int(11)         not null default 0         comment '当前库存数量',
  purchase_time   date            default null               comment '最近进货日期',
  intro           varchar(500)    default ''                 comment '商品简介',
  del_flag        char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (product_id),
  unique key uk_product_code (product_code),
  key idx_product_supplier (supplier_id),
  key idx_product_category (category_id),
  key idx_product_name (product_name)
) engine=innodb comment = '商品信息表';

-- ----------------------------
-- 进货台账（只增不改不删）
-- ----------------------------
drop table if exists pms_purchase;
create table pms_purchase (
  purchase_id     bigint(20)      not null auto_increment    comment '进货单ID',
  purchase_no     varchar(32)     not null                   comment '进货单号',
  product_id      bigint(20)      not null                   comment '商品ID',
  product_name    varchar(100)    default ''                 comment '商品名称快照',
  spec            varchar(100)    default ''                 comment '规格快照',
  supplier_id     bigint(20)      not null                   comment '厂家ID',
  supplier_name   varchar(100)    default ''                 comment '厂家名称快照',
  qty             int(11)         not null                   comment '进货数量',
  purchase_price  decimal(10,2)   not null                   comment '本次进价',
  amount          decimal(12,2)   not null                   comment '本次进货金额',
  purchase_time   date            not null                   comment '进货日期',
  create_by       varchar(64)     default ''                 comment '操作人',
  create_time     datetime                                   comment '创建时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (purchase_id),
  unique key uk_purchase_no (purchase_no),
  key idx_purchase_product (product_id),
  key idx_purchase_supplier (supplier_id),
  key idx_purchase_time (purchase_time)
) engine=innodb comment = '进货台账表';

-- ----------------------------
-- 库存变动流水
-- ----------------------------
drop table if exists pms_stock_log;
create table pms_stock_log (
  log_id          bigint(20)      not null auto_increment    comment '流水ID',
  product_id      bigint(20)      not null                   comment '商品ID',
  product_name    varchar(100)    default ''                 comment '商品名称快照',
  change_type     varchar(20)     not null                   comment '类型 IN入库 CHECK盘点 LOSS损耗 TRANSFER调拨 SALE销售 INIT建档',
  before_qty      int(11)         not null                   comment '变动前库存',
  change_qty      int(11)         not null                   comment '变动数量（可负）',
  after_qty       int(11)         not null                   comment '变动后库存',
  biz_type        varchar(20)     default ''                 comment '业务类型 purchase/sale/adjust/product',
  biz_id          bigint(20)      default null               comment '业务单据ID',
  create_by       varchar(64)     default ''                 comment '操作人',
  create_time     datetime                                   comment '变动时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (log_id),
  key idx_stock_product (product_id),
  key idx_stock_time (create_time),
  key idx_stock_type (change_type)
) engine=innodb comment = '库存变动流水表';

-- ----------------------------
-- 销售出库（只增不改不删，营业额数据源）
-- ----------------------------
drop table if exists pms_sale;
create table pms_sale (
  sale_id         bigint(20)      not null auto_increment    comment '销售单ID',
  sale_no         varchar(32)     not null                   comment '销售单号',
  product_id      bigint(20)      not null                   comment '商品ID',
  product_name    varchar(100)    default ''                 comment '商品名称快照',
  spec            varchar(100)    default ''                 comment '规格快照',
  category_id     bigint(20)      default null               comment '分类ID',
  qty             int(11)         not null                   comment '销售数量',
  sale_price      decimal(10,2)   not null                   comment '成交售价快照',
  amount          decimal(12,2)   not null                   comment '销售金额',
  sale_time       datetime        not null                   comment '销售时间',
  create_by       varchar(64)     default ''                 comment '操作人',
  create_time     datetime                                   comment '创建时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (sale_id),
  unique key uk_sale_no (sale_no),
  key idx_sale_product (product_id),
  key idx_sale_time (sale_time)
) engine=innodb comment = '销售出库表';

-- ----------------------------
-- 物流对账（禁止删除）
-- ----------------------------
drop table if exists pms_logistics;
create table pms_logistics (
  logistics_id    bigint(20)      not null auto_increment    comment '物流单ID',
  logistics_no    varchar(64)     not null                   comment '物流单号',
  purchase_id     bigint(20)      not null                   comment '关联进货批次',
  product_id      bigint(20)      default null               comment '对应商品',
  supplier_id     bigint(20)      default null               comment '供货厂家',
  carrier_id      bigint(20)      default null               comment '物流商ID',
  carrier         varchar(100)    default ''                 comment '物流服务商名称快照',
  ship_time       datetime                                   comment '发货时间',
  arrive_time     datetime                                   comment '到货时间',
  weight_volume   varchar(100)    default ''                 comment '重量/体积',
  freight         decimal(10,2)   default 0.00               comment '运费金额',
  insurance_fee   decimal(10,2)   default 0.00               comment '保价费',
  other_fee       decimal(10,2)   default 0.00               comment '其他杂费',
  total_fee       decimal(10,2)   default 0.00               comment '总物流费用',
  pay_status      char(1)         default '0'                comment '付款状态（0未对账 1已对账 2已结清）',
  reconcile_time  datetime                                   comment '对账时间',
  reconcile_by    varchar(64)     default ''                 comment '对账操作人',
  abnormal_flag   char(1)         default '0'                comment '异常标记（0否 1是）',
  abnormal_remark varchar(500)    default ''                 comment '异常备注',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (logistics_id),
  unique key uk_logistics_no (logistics_no),
  key idx_logistics_purchase (purchase_id),
  key idx_logistics_supplier (supplier_id),
  key idx_logistics_carrier (carrier_id),
  key idx_logistics_status (pay_status)
) engine=innodb comment = '物流对账表';

-- ----------------------------
-- 物流商
-- ----------------------------
drop table if exists pms_carrier;
create table pms_carrier (
  carrier_id      bigint(20)      not null auto_increment    comment '物流商ID',
  carrier_name    varchar(100)    not null                   comment '物流商名称',
  contact_name    varchar(50)     default ''                 comment '联系人',
  phone           varchar(20)     default ''                 comment '联系电话',
  status          char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag        char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (carrier_id),
  unique key uk_carrier_name (carrier_name)
) engine=innodb comment = '物流商表';



-- ----------------------------
-- 墨水屏设备
-- ----------------------------
drop table if exists pms_device;
create table pms_device (
  device_id         bigint(20)      not null auto_increment  comment '设备ID',
  sn                varchar(64)     not null                 comment '设备SN',
  device_code       varchar(64)     default ''               comment '设备编号',
  product_id        bigint(20)      default null             comment '绑定商品ID',
  shelf_no          varchar(64)     default ''               comment '货架编号',
  online_status     char(1)         default '0'              comment '在线状态（0离线 1在线）',
  last_sync_time    datetime                                 comment '最后同步时间',
  last_heartbeat    datetime                                 comment '最后心跳时间',
  battery           int(4)          default null             comment '电量',
  firmware          varchar(64)     default ''               comment '固件版本',
  abnormal_flag     char(1)         default '0'              comment '异常标记（0否 1是）',
  abnormal_remark   varchar(500)    default ''               comment '异常备注',
  bind_time         datetime                                 comment '绑定时间',
  create_by         varchar(64)     default ''               comment '创建者',
  create_time       datetime                                 comment '创建时间',
  update_by         varchar(64)     default ''               comment '更新者',
  update_time       datetime                                 comment '更新时间',
  remark            varchar(500)    default null             comment '备注',
  primary key (device_id),
  unique key uk_device_sn (sn),
  key idx_device_product (product_id)
) engine=innodb comment = '墨水屏设备表';

-- ----------------------------
-- 墨水屏同步日志
-- ----------------------------
drop table if exists pms_device_sync_log;
create table pms_device_sync_log (
  log_id          bigint(20)      not null auto_increment    comment '日志ID',
  device_id       bigint(20)      not null                   comment '设备ID',
  sn              varchar(64)     default ''                 comment '设备SN',
  product_id      bigint(20)      default null               comment '商品ID',
  sync_content    varchar(2000)   default ''                 comment '同步内容',
  sync_status     char(1)         default '0'                comment '同步状态（0失败 1成功 2待确认）',
  command_type    varchar(20)     default 'refresh'          comment '指令 refresh/restart/unbind',
  create_time     datetime                                   comment '同步时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (log_id),
  key idx_sync_device (device_id),
  key idx_sync_time (create_time)
) engine=innodb comment = '墨水屏同步日志表';

-- ----------------------------
-- 业务配置（单行）
-- ----------------------------
drop table if exists pms_setting;
create table pms_setting (
  setting_id              bigint(20)      not null           comment '配置ID',
  stock_warn_threshold    int(11)         default 5          comment '库存预警阈值',
  turnover_warn_min       decimal(12,2)   default null       comment '营收下限',
  turnover_warn_max       decimal(12,2)   default null       comment '营收上限',
  eink_sync_interval_sec  int(11)         default 0          comment '墨水屏同步频率秒（0表示实时推送）',
  eink_font_style         varchar(50)     default 'default'  comment '字体样式',
  show_name               char(1)         default 'Y'        comment '展示名称',
  show_spec               char(1)         default 'Y'        comment '展示规格',
  show_sale_price         char(1)         default 'Y'        comment '展示售价',
  show_intro              char(1)         default 'Y'        comment '展示简介',
  show_stock              char(1)         default 'N'        comment '展示库存',
  show_supplier           char(1)         default 'N'        comment '展示厂家',
  update_by               varchar(64)     default ''         comment '更新者',
  update_time             datetime                           comment '更新时间',
  remark                  varchar(500)    default null       comment '备注',
  primary key (setting_id)
) engine=innodb comment = '商品系统业务配置表';

insert into pms_setting (setting_id, stock_warn_threshold, eink_sync_interval_sec, eink_font_style, show_name, show_spec, show_sale_price, show_intro, show_stock, show_supplier)
values (1, 5, 0, 'default', 'Y', 'Y', 'Y', 'Y', 'N', 'N');

-- ----------------------------
-- 字典
-- ----------------------------
insert into sys_dict_type values(100, '物流付款状态', 'pms_pay_status', '0', 'admin', sysdate(), '', null, '物流对账付款状态');
insert into sys_dict_type values(101, '库存变动类型', 'pms_stock_change_type', '0', 'admin', sysdate(), '', null, '库存流水类型');
insert into sys_dict_type values(102, '设备在线状态', 'pms_online_status', '0', 'admin', sysdate(), '', null, '墨水屏在线状态');

insert into sys_dict_data values(1000, 1, '未对账', '0', 'pms_pay_status', '', 'info',    'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1001, 2, '已对账', '1', 'pms_pay_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1002, 3, '已结清', '2', 'pms_pay_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1003, 1, '入库', 'IN', 'pms_stock_change_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1004, 2, '盘点', 'CHECK', 'pms_stock_change_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1005, 3, '损耗', 'LOSS', 'pms_stock_change_type', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1006, 4, '调拨', 'TRANSFER', 'pms_stock_change_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1007, 5, '销售', 'SALE', 'pms_stock_change_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1008, 6, '建档', 'INIT', 'pms_stock_change_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1009, 1, '离线', '0', 'pms_online_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1010, 2, '在线', '1', 'pms_online_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 菜单 2100 起，按钮 2200 起
-- ----------------------------
insert into sys_menu values('2100', '商品管理', '0', '0', 'pms', null, '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', null, '商品管理系统目录');
insert into sys_menu values('2101', '经营看板', '2100', '1', 'dashboard', 'pms/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'pms:dashboard:query', 'dashboard', 'admin', sysdate(), '', null, '经营看板');
insert into sys_menu values('2102', '商品信息', '2100', '2', 'product', 'pms/product/index', '', '', 1, 0, 'C', '0', '0', 'pms:product:list', 'list', 'admin', sysdate(), '', null, '商品信息菜单');
insert into sys_menu values('2103', '商品分类', '2100', '3', 'category', 'pms/category/index', '', '', 1, 0, 'C', '0', '0', 'pms:category:list', 'tree', 'admin', sysdate(), '', null, '商品分类菜单');
insert into sys_menu values('2104', '供应商', '2100', '4', 'supplier', 'pms/supplier/index', '', '', 1, 0, 'C', '0', '0', 'pms:supplier:list', 'peoples', 'admin', sysdate(), '', null, '供应商菜单');
insert into sys_menu values('2105', '进货入库', '2100', '5', 'purchase', 'pms/purchase/index', '', '', 1, 0, 'C', '0', '0', 'pms:purchase:list', 'download', 'admin', sysdate(), '', null, '进货入库菜单');
insert into sys_menu values('2106', '库存流水', '2100', '6', 'stock', 'pms/stock/index', '', '', 1, 0, 'C', '0', '0', 'pms:stock:log', 'redis-list', 'admin', sysdate(), '', null, '库存流水菜单');
insert into sys_menu values('2107', '销售出库', '2100', '7', 'sale', 'pms/sale/index', '', '', 1, 0, 'C', '0', '0', 'pms:sale:list', 'upload', 'admin', sysdate(), '', null, '销售出库菜单');
insert into sys_menu values('2108', '物流对账', '2100', '8', 'logistics', 'pms/logistics/index', '', '', 1, 0, 'C', '0', '0', 'pms:logistics:list', 'money', 'admin', sysdate(), '', null, '物流对账菜单');
insert into sys_menu values('2112', '物流商', '2100', '8', 'carrier', 'pms/carrier/index', '', '', 1, 0, 'C', '0', '0', 'pms:carrier:list', 'guide', 'admin', sysdate(), '', null, '物流商菜单');
insert into sys_menu values('2109', '墨水屏设备', '2100', '9', 'device', 'pms/device/index', '', '', 1, 0, 'C', '0', '0', 'pms:device:list', 'server', 'admin', sysdate(), '', null, '墨水屏设备菜单');
insert into sys_menu values('2110', '报表统计', '2100', '10', 'report', 'pms/report/index', '', '', 1, 0, 'C', '0', '0', 'pms:report:overview', 'chart', 'admin', sysdate(), '', null, '报表统计菜单');
insert into sys_menu values('2111', '业务设置', '2100', '11', 'setting', 'pms/setting/index', '', '', 1, 0, 'C', '0', '0', 'pms:setting:query', 'edit', 'admin', sysdate(), '', null, '业务设置菜单');

-- 商品按钮
insert into sys_menu values('2201', '商品查询', '2102', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2202', '商品新增', '2102', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2203', '商品修改', '2102', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2204', '商品删除', '2102', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2205', '商品导出', '2102', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:export', '#', 'admin', sysdate(), '', null, '');
-- 分类按钮
insert into sys_menu values('2211', '分类查询', '2103', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:category:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2212', '分类新增', '2103', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:category:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2213', '分类修改', '2103', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:category:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2214', '分类删除', '2103', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:category:remove', '#', 'admin', sysdate(), '', null, '');
-- 供应商按钮
insert into sys_menu values('2221', '厂家查询', '2104', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:supplier:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2222', '厂家新增', '2104', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:supplier:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2223', '厂家修改', '2104', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:supplier:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2224', '厂家删除', '2104', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:supplier:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2225', '厂家导出', '2104', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:supplier:export', '#', 'admin', sysdate(), '', null, '');
-- 进货按钮
insert into sys_menu values('2231', '进货查询', '2105', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:purchase:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2232', '进货入库', '2105', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:purchase:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2233', '进货导出', '2105', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:purchase:export', '#', 'admin', sysdate(), '', null, '');
-- 库存按钮
insert into sys_menu values('2241', '库存调整', '2106', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:stock:adjust', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2242', '流水导出', '2106', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:stock:export', '#', 'admin', sysdate(), '', null, '');
-- 销售按钮
insert into sys_menu values('2251', '销售查询', '2107', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:sale:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2252', '销售出库', '2107', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:sale:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2253', '销售导出', '2107', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:sale:export', '#', 'admin', sysdate(), '', null, '');
-- 物流按钮
insert into sys_menu values('2261', '物流查询', '2108', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:logistics:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2262', '物流新增', '2108', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:logistics:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2263', '物流修改', '2108', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:logistics:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2264', '物流导出', '2108', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:logistics:export', '#', 'admin', sysdate(), '', null, '');
-- 物流商按钮
insert into sys_menu values('2292', '物流商查询', '2112', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2293', '物流商新增', '2112', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2294', '物流商修改', '2112', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2295', '物流商删除', '2112', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2296', '物流商导出', '2112', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:export', '#', 'admin', sysdate(), '', null, '');
-- 设备按钮
insert into sys_menu values('2271', '设备查询', '2109', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2272', '设备绑定', '2109', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:bind', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2273', '设备解绑', '2109', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:unbind', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2274', '设备操作', '2109', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:operate', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2275', '同步日志', '2109', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:log', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2276', '设备导出', '2109', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2277', '模板修改', '2109', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:device:edit', '#', 'admin', sysdate(), '', null, '');
-- 报表按钮
insert into sys_menu values('2281', '进货报表', '2110', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:report:purchase', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2282', '库存报表', '2110', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:report:stock', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2283', '成本报表', '2110', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:report:cost', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2284', '供应商报表', '2110', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:report:supplier', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2285', '营业额报表', '2110', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:report:turnover', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2286', '报表导出', '2110', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:report:export', '#', 'admin', sysdate(), '', null, '');
-- 设置按钮
insert into sys_menu values('2291', '设置修改', '2111', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:setting:edit', '#', 'admin', sysdate(), '', null, '');
