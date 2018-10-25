/*
Navicat MySQL Data Transfer

Source Server         : localhost123456
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ouath

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-10-25 11:11:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `oauth_client_info`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_info`;
CREATE TABLE `oauth_client_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_name` varchar(50) DEFAULT NULL COMMENT '客户端名称',
  `client_id` varchar(10) DEFAULT NULL COMMENT '客户端id',
  `client_key` varchar(40) DEFAULT NULL COMMENT '客户端key',
  `client_sign_key` varchar(40) DEFAULT NULL COMMENT '客户端签名key',
  `client_status` int(1) DEFAULT NULL COMMENT '1有效，0无效',
  `client_server_ip_list` varchar(200) DEFAULT NULL COMMENT '客户端ip白名单',
  `client_manager_name` varchar(40) DEFAULT NULL COMMENT '客户端管理员名称',
  `client_manager_phone` varchar(11) DEFAULT NULL COMMENT '客户端管理员手机号',
  `client_manager_email` varchar(50) DEFAULT NULL COMMENT '客户端管理员邮箱',
  `index_url` varchar(100) DEFAULT NULL COMMENT '客户端首页地址',
  `url_prefix` varchar(100) DEFAULT NULL COMMENT '菜单URL前缀',
  `logout_url` varchar(100) DEFAULT NULL COMMENT '退出登录地址',
  `create_time` datetime DEFAULT NULL,
  `create_user_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_info
-- ----------------------------
INSERT INTO `oauth_client_info` VALUES ('1', '系统管理', '1001', 'sadsgsdfghsdff34254tg436367111', '123125345234', '1', '127.0.0.1', 'yanjun', '15710080901', 'yanjun@qq.com', 'http://127.0.0.1:11089/sysmanager/index', 'http://127.0.0.1:11089/sysmanager', 'http://127.0.0.1:11089/sysmanager/logout', '2018-01-19 20:07:35', '1');

-- ----------------------------
-- Table structure for `oauth_client_log`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_log`;
CREATE TABLE `oauth_client_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `req_str` longtext,
  `req_time` datetime DEFAULT NULL,
  `rep_str` longtext,
  `rep_time` datetime DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL,
  `in_time` datetime DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_oauth_client_log_req_time` (`req_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15251 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_log
-- ----------------------------

-- ----------------------------
-- Table structure for `oauth_menu`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_menu`;
CREATE TABLE `oauth_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(40) DEFAULT NULL,
  `menu_url` varchar(100) DEFAULT NULL,
  `menu_type` int(11) DEFAULT NULL COMMENT '1菜单，2页面中的url(如：页面中的增删改查链接)',
  `pid` int(11) DEFAULT NULL COMMENT '父节点id',
  `menu_level` int(11) DEFAULT NULL COMMENT '菜单级别 1，2 ，3',
  `has_son` int(11) DEFAULT NULL COMMENT '是否有子节点  1是 0否',
  `client_id` varchar(10) DEFAULT NULL COMMENT '所属客户端id',
  `order_by` int(11) DEFAULT NULL COMMENT '排序，升序排，数字越小越靠前',
  `status` int(11) DEFAULT NULL COMMENT '1有效，0无效',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_menu
-- ----------------------------
INSERT INTO `oauth_menu` VALUES ('41', '系统管理', '1', '1', '0', null, '1', '1001', '1', '1', '2018-01-30 09:20:08');
INSERT INTO `oauth_menu` VALUES ('42', '用户管理', '/page/user', '1', '41', null, '1', '1001', '1', '1', '2018-01-30 09:21:24');
INSERT INTO `oauth_menu` VALUES ('43', '角色管理', '/page/role', '1', '41', null, '1', '1001', '2', '1', '2018-01-30 09:22:01');
INSERT INTO `oauth_menu` VALUES ('44', '菜单管理', '/page/menu', '1', '41', null, '1', '1001', '3', '1', '2018-01-30 09:22:36');
INSERT INTO `oauth_menu` VALUES ('45', '系统首页', '', '2', '0', null, '1', '1001', '2', '1', '2018-01-30 09:23:07');
INSERT INTO `oauth_menu` VALUES ('46', '首页显示', '/index', '2', '45', null, null, '1001', '1', '1', '2018-01-30 09:23:50');
INSERT INTO `oauth_menu` VALUES ('47', '注销地址', '/logout', '2', '45', null, null, '1001', '2', '1', '2018-01-30 09:24:21');
INSERT INTO `oauth_menu` VALUES ('48', '客户端切换菜单查询', '/userClientMenu', '2', '45', null, null, '1001', '3', '1', '2018-01-30 09:25:08');
INSERT INTO `oauth_menu` VALUES ('49', '用户列表查询', '/user/listUser', '2', '42', null, null, '1001', '1', '1', '2018-01-30 09:27:31');
INSERT INTO `oauth_menu` VALUES ('50', '用户客户端角色查询', '/role/loadClientRole', '2', '42', null, null, '1001', '2', '1', '2018-01-30 09:28:07');
INSERT INTO `oauth_menu` VALUES ('51', '删除用户', '/user/delUser', '2', '42', null, null, '1001', '3', '1', '2018-01-30 09:28:42');
INSERT INTO `oauth_menu` VALUES ('52', '添加、编辑用户信息', '/user/addUser', '2', '42', null, null, '1001', '4', '1', '2018-01-30 09:30:10');
INSERT INTO `oauth_menu` VALUES ('53', '角色列表查询', '/role/list', '2', '43', null, null, '1001', '1', '1', '2018-01-30 09:31:15');
INSERT INTO `oauth_menu` VALUES ('54', '删除角色', '/role/delRole', '2', '43', null, null, '1001', '2', '1', '2018-01-30 09:31:36');
INSERT INTO `oauth_menu` VALUES ('55', '添加、编辑角色', '/role/addRole', '2', '43', null, null, '1001', '3', '1', '2018-01-30 09:32:31');
INSERT INTO `oauth_menu` VALUES ('56', '查询角色已有菜单', '/role/findRoleMenu', '2', '43', null, null, '1001', '4', '1', '2018-01-30 09:33:13');
INSERT INTO `oauth_menu` VALUES ('57', '查询菜单列表', '/role/findAllMenu', '2', '43', null, null, '1001', '5', '1', '2018-01-30 09:33:39');
INSERT INTO `oauth_menu` VALUES ('58', '加载子节点', '/menu/loadSonMenuList', '2', '44', null, null, '1001', '1', '1', '2018-01-30 09:44:35');
INSERT INTO `oauth_menu` VALUES ('59', '添加菜单', '/menu/add', '2', '44', null, null, '1001', '2', '1', '2018-01-30 09:45:01');
INSERT INTO `oauth_menu` VALUES ('60', '删除菜单', '/menu/del', '2', '44', null, null, '1001', '3', '1', '2018-01-30 09:45:22');
INSERT INTO `oauth_menu` VALUES ('61', '基本信息管理', '', '1', '0', null, '1', '1002', '1', '1', '2018-01-30 11:00:14');
INSERT INTO `oauth_menu` VALUES ('62', '业务管理', '', '1', '0', null, '1', '1002', '2', '1', '2018-01-30 11:00:24');
INSERT INTO `oauth_menu` VALUES ('80', '查询用户已经关联的客户端个角色', '/user/findUserClintRole', '2', '42', null, null, '1001', '5', '1', '2018-01-30 11:14:25');

-- ----------------------------
-- Table structure for `oauth_role`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_role`;
CREATE TABLE `oauth_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(40) DEFAULT NULL,
  `client_id` varchar(10) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1有效，0无效',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_role
-- ----------------------------
INSERT INTO `oauth_role` VALUES ('34', '系统管理员', '1001', '1', '2018-10-25 10:55:04');

-- ----------------------------
-- Table structure for `oauth_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_role_menu`;
CREATE TABLE `oauth_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1有效，0无效',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5617 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_role_menu
-- ----------------------------
INSERT INTO `oauth_role_menu` VALUES ('5597', '34', '43', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5598', '34', '41', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5599', '34', '44', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5600', '34', '45', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5601', '34', '46', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5602', '34', '47', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5603', '34', '48', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5604', '34', '49', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5605', '34', '42', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5606', '34', '50', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5607', '34', '51', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5608', '34', '52', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5609', '34', '53', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5610', '34', '54', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5611', '34', '55', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5612', '34', '56', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5613', '34', '57', '1', '2018-10-25 10:55:04');
INSERT INTO `oauth_role_menu` VALUES ('5614', '34', '58', '1', '2018-10-25 10:55:05');
INSERT INTO `oauth_role_menu` VALUES ('5615', '34', '59', '1', '2018-10-25 10:55:05');
INSERT INTO `oauth_role_menu` VALUES ('5616', '34', '60', '1', '2018-10-25 10:55:05');

-- ----------------------------
-- Table structure for `oauth_user_base_info`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user_base_info`;
CREATE TABLE `oauth_user_base_info` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) DEFAULT NULL COMMENT '用户登陆名称',
  `login_pwd` varchar(100) DEFAULT NULL COMMENT '登陆密码',
  `nick_name` varchar(40) DEFAULT NULL COMMENT '姓名',
  `phone_no` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(40) DEFAULT NULL COMMENT '邮箱地址',
  `user_status` int(1) DEFAULT NULL COMMENT '状态 1有效  0无效',
  `create_time` datetime DEFAULT NULL,
  `passwd_time` datetime DEFAULT NULL COMMENT '密码设置时间',
  `create_user_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `oauth_user_user_name_idx` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_user_base_info
-- ----------------------------
INSERT INTO `oauth_user_base_info` VALUES ('14', 'admin', 'eaa7ed0da7736e70548a0e129e1db21f42f7ca926d16e1207689e8f09f07d0e5', '管理员', '15710080901', 'yanjun@qq.com', '1', '2018-01-30 10:11:12', '2018-01-30 10:11:12', '14');

-- ----------------------------
-- Table structure for `oauth_user_client`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user_client`;
CREATE TABLE `oauth_user_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `client_id` varchar(10) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user_id` varchar(40) DEFAULT NULL,
  `is_manager` int(1) DEFAULT NULL COMMENT '是否是管理员，1是，0不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=506 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_user_client
-- ----------------------------
INSERT INTO `oauth_user_client` VALUES ('24', '14', '1001', '1', '2018-01-30 10:11:12', '14', '1');

-- ----------------------------
-- Table structure for `oauth_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user_role`;
CREATE TABLE `oauth_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(40) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '1有效  0无效',
  `create_time` datetime DEFAULT NULL,
  `create_user_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=368 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_user_role
-- ----------------------------
INSERT INTO `oauth_user_role` VALUES ('359', '14', '34', '1', '2018-06-14 13:54:19', '14');
INSERT INTO `oauth_user_role` VALUES ('360', '14', '45', '1', '2018-06-14 13:54:19', '14');
