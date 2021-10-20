/*
 Navicat Premium Data Transfer

 Source Server         : mysql_lp
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : erahub_jlja_manage

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 20/10/2021 15:12:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '上级菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '权限标识',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` bigint(0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `available` int(0) NULL DEFAULT 1 COMMENT '0：不可用，1：可用',
  `open` int(0) NULL DEFAULT 1 COMMENT '0:不展开，1：展开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 350 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, 0, '系统管理', '', NULL, 'el-icon-setting', '0', 1, '2020-03-07 17:41:30', '2021-10-20 15:10:49', 1, 1);
INSERT INTO `tb_menu` VALUES (4, 1, '菜单权限', '/system/menus', 'menus', 'el-icon-help', '0', 3, '2020-03-07 18:57:42', '2020-12-15 17:25:02', 1, 0);
INSERT INTO `tb_menu` VALUES (5, 0, '监控中心', '', NULL, 'el-icon-camera', '0', 4, '2020-03-07 18:58:18', '2021-10-20 15:11:03', 1, 1);
INSERT INTO `tb_menu` VALUES (226, 1, '用户管理', '/system/users', 'users', 'el-icon-user', '0', 2, '2020-03-10 05:27:54', '2020-12-15 17:24:22', 1, 0);
INSERT INTO `tb_menu` VALUES (230, 351, '入库记录', '/business/material/product/in-stocks', 'el-icon-date', 'el-icon-date', '0', 1, '2020-03-10 05:34:28', '2021-10-20 14:30:28', 1, 0);
INSERT INTO `tb_menu` VALUES (234, 226, '用户添加', '', 'user:add', 'el-icon-plus', '1', 1, '2020-03-10 05:50:44', '2020-03-10 07:51:56', 1, 0);
INSERT INTO `tb_menu` VALUES (235, 1, '角色管理', '/system/roles', 'roles', 'el-icon-postcard', '0', 3, '2020-03-10 05:51:28', '2020-12-15 17:24:41', 1, 0);
INSERT INTO `tb_menu` VALUES (239, 226, '用户删除', '', 'user:delete', 'el-icon-picture', '1', 1, '2020-03-10 06:05:30', '2020-03-10 08:10:19', 1, 0);
INSERT INTO `tb_menu` VALUES (240, 226, '用户编辑', '', 'user:edit', 'el-icon-video-camera-solid', '1', 1, '2020-03-10 06:06:30', '2020-03-10 07:52:28', 1, 0);
INSERT INTO `tb_menu` VALUES (241, 235, '角色编辑', '', 'role:edit', 'el-icon-s-promotion', '1', 2, '2020-03-10 06:11:03', '2020-03-11 11:40:19', 1, 0);
INSERT INTO `tb_menu` VALUES (242, 235, '角色删除', '', 'role:delete', 'el-icon-s-marketing', '1', 3, '2020-03-10 06:15:29', '2020-03-11 11:43:36', 1, 0);
INSERT INTO `tb_menu` VALUES (247, 4, '添加菜单', '', 'menu:add', 'el-icon-s-opportunity', '1', 1, '2020-03-10 07:55:10', '2020-04-27 09:59:43', 1, 0);
INSERT INTO `tb_menu` VALUES (249, 4, '修改菜单', '', 'menu:update', 'el-icon-share', '1', 2, '2020-03-10 07:56:55', '2020-03-15 13:29:29', 1, 0);
INSERT INTO `tb_menu` VALUES (250, 4, '删除菜单', '', 'menu:delete', 'el-icon-folder-opened', '1', 3, '2020-03-10 07:57:38', '2020-03-15 13:29:41', 1, 0);
INSERT INTO `tb_menu` VALUES (251, 235, '分配权限', '', 'role:authority', 'el-icon-document-add', '1', 1, '2020-03-10 08:13:22', '2020-03-11 11:39:30', 1, 0);
INSERT INTO `tb_menu` VALUES (253, 1, '控制面板', '/system/welcome', 'welcome:view', 'el-icon-star-off', '0', 1, '2020-03-10 08:46:44', '2020-12-15 19:22:46', 1, 0);
INSERT INTO `tb_menu` VALUES (254, 226, '分配角色', '', 'user:assign', 'el-icon-s-tools', '1', 3, '2020-03-11 01:32:29', '2020-04-27 10:58:30', 1, 0);
INSERT INTO `tb_menu` VALUES (255, 235, '添加角色', '', 'role:add', 'el-icon-help', '1', 1, '2020-03-11 01:34:18', '2020-03-11 01:34:18', 1, 0);
INSERT INTO `tb_menu` VALUES (256, 226, '禁用用户', '', 'user:status', 'el-icon-circle-close', '1', 1, '2020-03-11 06:50:04', '2020-03-14 05:05:49', 1, 0);
INSERT INTO `tb_menu` VALUES (258, 226, '用户更新', '', 'user:update', 'el-icon-refresh', '1', 1, '2020-03-11 08:26:54', '2020-03-11 08:26:54', 1, 0);
INSERT INTO `tb_menu` VALUES (259, 235, '角色更新', '', 'role:update', 'el-icon-refresh-left', '1', 1, '2020-03-11 11:45:20', '2020-03-11 11:45:20', 1, 0);
INSERT INTO `tb_menu` VALUES (260, 235, '状态更新', '', 'role:status', 'el-icon-refresh', '1', 1, '2020-03-14 05:07:02', '2020-03-14 05:07:24', 1, 0);
INSERT INTO `tb_menu` VALUES (261, 1, '部门管理', '/system/departments', '', 'el-icon-s-home', '0', 3, '2020-03-15 06:05:48', '2021-10-15 16:34:23', 1, 0);
INSERT INTO `tb_menu` VALUES (262, 261, '添加部门', '', 'department:add', 'el-icon-plus', '1', 1, '2020-03-15 11:57:42', '2020-03-21 12:37:21', 1, 0);
INSERT INTO `tb_menu` VALUES (263, 261, '编辑院系', '', 'department:edit', 'el-icon-edit', '1', 1, '2020-03-15 11:59:52', '2020-03-15 12:16:36', 1, 0);
INSERT INTO `tb_menu` VALUES (264, 261, '更新院系', '', 'department:update', 'el-icon-refresh', '1', 1, '2020-03-15 12:02:34', '2020-03-15 12:16:32', 1, 0);
INSERT INTO `tb_menu` VALUES (265, 261, '删除院系', NULL, 'department:delete', 'el-icon-delete', '1', 1, '2020-03-15 12:03:21', '2020-03-15 12:03:21', 1, 0);
INSERT INTO `tb_menu` VALUES (267, 351, '物资资料', '/business/material/product/list', '', 'el-icon-goods', '0', 2, '2020-03-16 09:01:02', '2021-10-20 14:30:41', 1, 0);
INSERT INTO `tb_menu` VALUES (268, 351, '物资类别', '/business/material/product/categories', '', 'el-icon-star-off', '0', 2, '2020-03-16 09:01:48', '2021-10-20 14:30:45', 1, 0);
INSERT INTO `tb_menu` VALUES (269, 351, '物资来源', '/business/material/product/suppliers', '', 'el-icon-coordinate', '0', 5, '2020-03-16 12:35:10', '2021-10-20 14:31:03', 1, 0);
INSERT INTO `tb_menu` VALUES (270, 351, '发放记录', '/business/material/product/out-stocks', '', 'el-icon-goods', '0', 5, '2020-03-16 13:55:49', '2021-10-20 14:31:09', 1, 1);
INSERT INTO `tb_menu` VALUES (271, 5, '登入日志', '/monitor/login-log', 'login:log', 'el-icon-date', '0', 1, '2020-03-20 10:31:12', '2020-12-15 18:28:47', 1, 0);
INSERT INTO `tb_menu` VALUES (273, 303, '全国疫情', '/business/material/covid19/map', 'map:view', 'el-icon-s-opportunity', '0', 1, '2020-03-20 11:32:02', '2021-10-20 14:30:07', 1, 1);
INSERT INTO `tb_menu` VALUES (274, 267, '添加物资', '', 'product:add', 'el-icon-s-opportunity', '1', 1, '2020-03-21 02:04:24', '2020-03-21 02:04:24', 1, 0);
INSERT INTO `tb_menu` VALUES (276, 267, '上传图片', NULL, 'upload:image', 'el-icon-finished', '1', 2, '2020-03-21 02:05:21', '2020-03-21 02:05:21', 1, 0);
INSERT INTO `tb_menu` VALUES (277, 267, '更新物资', NULL, 'product:update', 'el-icon-folder', '1', 3, '2020-03-21 02:05:56', '2020-03-21 02:05:56', 1, 0);
INSERT INTO `tb_menu` VALUES (278, 267, '编辑物资', NULL, 'product:edit', 'el-icon-edit', '1', 1, '2020-03-21 02:06:23', '2020-03-21 02:06:23', 1, 0);
INSERT INTO `tb_menu` VALUES (279, 269, '删除来源', '', 'supplier:delete', 'el-icon-document-delete', '1', 1, '2020-03-21 02:17:29', '2020-03-21 12:32:22', 1, 0);
INSERT INTO `tb_menu` VALUES (280, 269, '更新来源', '', 'supplier:update', 'el-icon-paperclip', '1', 1, '2020-03-21 02:18:34', '2020-03-21 12:36:41', 1, 0);
INSERT INTO `tb_menu` VALUES (281, 269, '添加来源', NULL, 'supplier:add', 'el-icon-document-add', '1', 1, '2020-03-21 02:19:02', '2020-03-21 02:19:02', 1, 1);
INSERT INTO `tb_menu` VALUES (282, 269, '编辑来源', NULL, 'supplier:edit', 'el-icon-scissors', '1', 2, '2020-03-21 02:19:36', '2020-03-21 02:19:36', 1, 1);
INSERT INTO `tb_menu` VALUES (283, 268, '添加类别', '', 'productCategory:add', ' el-icon-folder-add', '1', 1, '2020-03-21 02:26:12', '2020-03-21 02:44:22', 1, 0);
INSERT INTO `tb_menu` VALUES (284, 268, '删除类别', NULL, 'productCategory:delete', 'el-icon-delete', '1', 1, '2020-03-21 02:27:05', '2020-03-21 02:28:49', 1, 0);
INSERT INTO `tb_menu` VALUES (285, 268, '编辑类别', NULL, 'productCategory:edit', 'el-icon-scissors', '1', 2, '2020-03-21 02:27:42', '2020-03-21 02:27:42', 1, 0);
INSERT INTO `tb_menu` VALUES (286, 268, '更新类别', NULL, 'productCategory:update', ' el-icon-coordinate', '1', 1, '2020-03-21 02:28:17', '2020-03-21 02:28:17', 1, 0);
INSERT INTO `tb_menu` VALUES (296, 295, 'swagger文档', '/monitor/swagger-ui', NULL, 'el-icon-document', '0', 2, '2020-03-22 01:22:48', '2020-12-15 18:32:54', 1, 0);
INSERT INTO `tb_menu` VALUES (298, 5, 'SQL监控', '/monitor/druid', NULL, 'el-icon-view', '0', 3, '2020-03-22 02:48:05', '2021-10-20 15:11:17', 1, 0);
INSERT INTO `tb_menu` VALUES (299, 271, '删除日志', '', 'loginLog:delete', 'el-icon-delete', '1', 1, '2020-03-22 21:55:44', '2020-03-22 21:55:44', 1, 0);
INSERT INTO `tb_menu` VALUES (300, 271, '批量删除', '', 'loginLog:batchDelete', 'el-icon-delete-solid', '1', 1, '2020-03-22 22:19:26', '2020-03-22 22:19:26', 1, 0);
INSERT INTO `tb_menu` VALUES (301, 4, '编辑菜单', '', 'menu:edit', 'el-icon-edit', '1', 1, '2020-03-22 23:12:25', '2020-03-22 23:12:25', 1, 0);
INSERT INTO `tb_menu` VALUES (303, 0, '健康报备', '', '', 'el-icon-platform-eleme', '0', 3, '2020-03-24 10:11:53', '2020-12-15 20:15:30', 1, 1);
INSERT INTO `tb_menu` VALUES (304, 303, '健康打卡', '/business/material/covid19/health', '', 'el-icon-s-cooperation', '0', 1, '2020-03-24 10:12:57', '2021-10-20 14:29:52', 1, 0);
INSERT INTO `tb_menu` VALUES (307, 5, '操作日志', '/monitor/logs', '', 'el-icon-edit', '0', 2, '2020-04-04 19:04:53', '2021-10-20 15:11:11', 1, 0);
INSERT INTO `tb_menu` VALUES (308, 307, '删除日志', '', 'log:delete', 'el-icon-circle-close', '1', 1, '2020-04-04 19:59:20', '2020-04-04 19:59:20', 1, 1);
INSERT INTO `tb_menu` VALUES (309, 307, '批量删除', NULL, 'log:batchDelete', 'el-icon-document-delete', '1', 2, '2020-04-04 19:59:59', '2020-04-04 19:59:59', 1, 0);
INSERT INTO `tb_menu` VALUES (310, 351, '物资去处', '/business/material/product/consumers', '', 'el-icon-edit', '0', 1, '2020-04-05 10:08:21', '2021-10-20 14:30:35', 1, 0);
INSERT INTO `tb_menu` VALUES (312, 0, '业务管理', NULL, NULL, 'el-icon-s-goods', '0', 2, '2020-04-05 10:19:07', '2020-08-19 17:57:27', 1, 1);
INSERT INTO `tb_menu` VALUES (316, 351, '物资库存', '/business/material/product/stocks', '', 'el-icon-tickets', '0', 5, '2020-04-16 08:45:08', '2021-10-20 14:31:13', 1, 0);
INSERT INTO `tb_menu` VALUES (317, 226, '导出表格', '', 'user:export', 'el-icon-edit', '1', 1, '2020-04-17 18:02:05', '2020-04-17 18:02:05', 1, 0);
INSERT INTO `tb_menu` VALUES (318, 1, '图标管理', '/system/icon', '', 'el-icon-star-off', '0', 7, '2020-04-21 12:06:33', '2020-12-17 21:47:49', 1, 1);
INSERT INTO `tb_menu` VALUES (321, 1, '文件管理', '/system/files', '', 'el-icon-picture-outline', '0', 7, '2020-04-25 10:52:17', '2021-10-09 09:30:59', 1, 1);
INSERT INTO `tb_menu` VALUES (322, 310, '添加去处', '', 'consumer:add', 'el-icon-plus', '1', 2, '2020-04-27 16:57:04', '2020-04-27 16:58:21', 1, 1);
INSERT INTO `tb_menu` VALUES (323, 310, '删除去处', NULL, 'consumer:delete', 'el-icon-delete', '1', 1, '2020-04-27 16:57:42', '2020-04-27 16:57:42', 1, 0);
INSERT INTO `tb_menu` VALUES (324, 310, '编辑去处', '', 'consumer:edit', 'el-icon-edit', '1', 1, '2020-04-27 16:59:17', '2020-04-27 16:59:17', 1, 0);
INSERT INTO `tb_menu` VALUES (325, 310, '更新去处', NULL, 'consumer:update', 'el-icon-star-off', '1', 1, '2020-04-27 17:00:18', '2020-04-27 17:00:18', 1, 1);
INSERT INTO `tb_menu` VALUES (326, 230, '添加入库', '', 'inStock:in', 'el-icon-plus', '1', 3, '2020-04-27 17:07:04', '2020-08-19 17:57:15', 1, 1);
INSERT INTO `tb_menu` VALUES (328, 230, '入库明细', NULL, 'inStock:detail', 'el-icon-zoom-in', '1', 2, '2020-04-27 17:08:25', '2021-09-27 11:07:53', 1, 0);
INSERT INTO `tb_menu` VALUES (329, 4, '导出菜单', NULL, 'menu:export', 'el-icon-edit', '1', 1, '2020-04-27 17:26:40', '2021-10-15 16:34:02', 1, 0);
INSERT INTO `tb_menu` VALUES (331, 267, '删除物资', NULL, 'product:delete', 'el-icon-delete', '1', 1, '2020-04-30 18:27:02', '2020-04-30 19:05:31', 1, 0);
INSERT INTO `tb_menu` VALUES (332, 267, '回收物资', '', 'product:remove', 'el-icon-remove', '1', 1, '2020-04-30 18:56:48', '2020-04-30 18:56:48', 1, 1);
INSERT INTO `tb_menu` VALUES (333, 267, '物资审核', NULL, 'product:publish', 'el-icon-edit', '1', 1, '2020-04-30 18:58:38', '2020-04-30 19:05:42', 1, 0);
INSERT INTO `tb_menu` VALUES (336, 267, '物资还原', NULL, 'product:back', 'el-icon-top-left', '1', 1, '2020-04-30 19:06:22', '2020-04-30 19:06:22', 1, 0);
INSERT INTO `tb_menu` VALUES (337, 230, '入库回收', '', 'inStock:remove', 'el-icon-remove', '1', 3, '2020-04-30 19:12:56', '2020-08-19 17:57:55', 1, 1);
INSERT INTO `tb_menu` VALUES (338, 230, '入库审核', NULL, 'inStock:publish', 'el-icon-edit', '1', 2, '2020-04-30 19:13:32', '2020-08-19 17:57:32', 1, 0);
INSERT INTO `tb_menu` VALUES (339, 230, '删除记录', NULL, 'inStock:delete', 'el-icon-delete', '1', 4, '2020-04-30 19:14:03', '2020-08-19 17:57:42', 1, 0);
INSERT INTO `tb_menu` VALUES (340, 230, '入库还原', '', 'inStock:back', 'el-icon-d-arrow-left', '1', 3, '2020-04-30 19:17:27', '2020-08-19 17:57:49', 1, 0);
INSERT INTO `tb_menu` VALUES (341, 295, '个人博客', '/blog', '', 'el-icon-view', '0', 1, '2020-05-07 19:34:31', '2020-05-07 19:34:31', 1, 0);
INSERT INTO `tb_menu` VALUES (343, 304, '健康上报', '', 'health:report', 'el-icon-edit', '1', 1, '2020-05-14 20:21:09', '2020-05-14 20:21:09', 1, 0);
INSERT INTO `tb_menu` VALUES (344, 5, '项目接口', '/monitor/swagger-ui', '', 'el-icon-edit', '0', 4, '2020-12-15 18:35:18', '2021-10-20 15:11:21', 1, 1);
INSERT INTO `tb_menu` VALUES (351, 312, '物资管理', NULL, NULL, 'el-icon-star-off', '0', 2, '2021-10-20 14:37:29', '2021-10-20 14:37:41', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
