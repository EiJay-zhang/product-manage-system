-- 第五期：进货作废、销售退货、商品导入（已有库增量）
-- 请整份执行。若某句提示 Duplicate column name 'status'，说明列已存在，跳过该句继续跑后面即可。
USE pms;

-- 加单据状态列（不要用 PREPARE，客户端会把 '' 拆坏导致语法错误）
ALTER TABLE pms_purchase
  ADD COLUMN status char(1) DEFAULT '0' COMMENT '状态（0正常 1作废）' AFTER remark;

ALTER TABLE pms_sale
  ADD COLUMN status char(1) DEFAULT '0' COMMENT '状态（0正常 1已退货）' AFTER remark;

INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, remark)
SELECT 103, '单据状态', 'pms_bill_status', '0', 'admin', sysdate(), '进货/销售单据状态'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_id = 103 OR dict_type = 'pms_bill_status');

INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 1011, 7, '作废', 'VOID', 'pms_stock_change_type', '', 'danger', 'N', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 1011);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 1012, 8, '退货', 'RETURN', 'pms_stock_change_type', '', 'warning', 'N', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 1012);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 1013, 1, '正常', '0', 'pms_bill_status', '', 'success', 'Y', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 1013);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 1014, 2, '作废', '1', 'pms_bill_status', '', 'info', 'N', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 1014);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 2206, '商品导入', 2102, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'pms:product:import', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2206);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 2234, '进货作废', 2105, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'pms:purchase:void', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2234);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 2254, '销售退货', 2107, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'pms:sale:void', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2254);
