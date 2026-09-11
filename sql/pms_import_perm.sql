-- 商品导入、分类导出按钮（已有库增量）
use pms;

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2206', '商品导入', '2102', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:import', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2206);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '2215', '分类导出', '2103', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'pms:category:export', '#', 'admin', sysdate(), ''
from dual where not exists (select 1 from sys_menu where menu_id = 2215);

alter table pms_setting
  modify eink_sync_interval_sec int(11) default 0 comment '墨水屏轮询间隔秒（0表示近实时5秒，V1不接MQTT）';
