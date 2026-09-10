-- 物流商维护（已有库增量脚本）
use pms;

create table if not exists pms_carrier (
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

set @col_exists := (
  select count(1) from information_schema.columns
  where table_schema = 'pms' and table_name = 'pms_logistics' and column_name = 'carrier_id'
);
set @sql := if(@col_exists = 0,
  'alter table pms_logistics add column carrier_id bigint(20) default null comment ''物流商ID'' after supplier_id, add key idx_logistics_carrier (carrier_id)',
  'select 1');
prepare stmt from @sql;
execute stmt;
deallocate prepare stmt;

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2112', '物流商', '2100', '8', 'carrier', 'pms/carrier/index', '', '', 1, 0, 'C', '0', '0', 'pms:carrier:list', 'guide', 'admin', sysdate(), '物流商菜单'
from dual where not exists (select 1 from sys_menu where menu_id = 2112);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2292', '物流商查询', '2112', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:query', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2292);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2293', '物流商新增', '2112', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:add', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2293);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2294', '物流商修改', '2112', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:edit', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2294);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2295', '物流商删除', '2112', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:remove', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2295);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2296', '物流商导出', '2112', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:carrier:export', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2296);
