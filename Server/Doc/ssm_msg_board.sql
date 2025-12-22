/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : ssm_msg_board

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 20/12/2025 21:17:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for like_record
-- ----------------------------
DROP TABLE IF EXISTS `like_record`;
CREATE TABLE `like_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '点赞记录ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '点赞用户ID（关联user.id）',
  `message_id` bigint UNSIGNED NOT NULL COMMENT '点赞的主留言ID（关联message.id）',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '点赞时间戳（毫秒），默认1970-01-01 00:00:00',
  `is_canceled` tinyint NOT NULL DEFAULT 0 COMMENT '取消状态：0-未取消 1-已取消',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_message`(`user_id` ASC, `message_id` ASC) USING BTREE COMMENT '防止同一用户重复点赞同一留言',
  INDEX `fk_like_message`(`message_id` ASC) USING BTREE,
  CONSTRAINT `fk_like_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储用户对主留言的点赞行为，防止重复点赞，支持取消点赞' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like_record
-- ----------------------------
INSERT INTO `like_record` VALUES (1, 1, 2, 1766236487000, 0);
INSERT INTO `like_record` VALUES (2, 2, 1, 1766236497000, 0);
INSERT INTO `like_record` VALUES (3, 3, 5, 1766236507000, 0);
INSERT INTO `like_record` VALUES (4, 4, 9, 1766236517000, 1);
INSERT INTO `like_record` VALUES (5, 5, 1, 1766236527000, 0);
INSERT INTO `like_record` VALUES (6, 6, 9, 1766236537000, 0);
INSERT INTO `like_record` VALUES (7, 7, 6, 1766236547000, 0);
INSERT INTO `like_record` VALUES (8, 8, 5, 1766236557000, 1);
INSERT INTO `like_record` VALUES (9, 1, 6, 1766236567000, 0);
INSERT INTO `like_record` VALUES (10, 2, 5, 1766236577000, 0);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '留言唯一ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言内容（支持富文本）',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '获赞数',
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建者ID（关联user.id）',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间戳（毫秒），默认1970-01-01 00:00:00',
  `update_time` bigint NOT NULL DEFAULT 0 COMMENT '更新时间戳（毫秒），默认1970-01-01 00:00:00',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：-2-违规 -1-待审核/停用 0-正常',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE COMMENT '优化按创建者查询留言的性能',
  CONSTRAINT `fk_message_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储用户发布的主留言信息，支持编辑、点赞、举报、搜索、排序' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, '第一条留言', '这是普通用户1发布的第一条测试留言，内容简单~', 12, 1, 1766236587000, 1766236587000, 0, 0);
INSERT INTO `message` VALUES (2, '第二条留言', '普通用户2的测试留言，包含一些测试内容：123456，abcdef', 8, 2, 1766232987000, 1766232987000, 0, 0);
INSERT INTO `message` VALUES (3, '违规测试留言', '这条留言包含违规内容，状态设为违规', 0, 3, 1766229387000, 1766229387000, -2, 0);
INSERT INTO `message` VALUES (4, '待审核留言', '这条留言需要管理员审核后才能显示', 0, 4, 1766225787000, 1766225787000, -1, 0);
INSERT INTO `message` VALUES (5, '管理员留言', '管理员1发布的公告类留言，大家请注意查看', 25, 6, 1766222187000, 1766224587000, 0, 0);
INSERT INTO `message` VALUES (6, '第五条普通留言', '普通用户5的日常分享，记录生活点滴', 5, 5, 1766218587000, 1766218587000, 0, 0);
INSERT INTO `message` VALUES (7, '第六条测试留言', '普通用户6的留言，内容较长~ 这里是补充的测试内容，用于验证富文本存储是否正常。', 18, 7, 1766214987000, 1766216587000, 0, 0);
INSERT INTO `message` VALUES (8, '已删除留言', '这条留言被软删除了，前端不显示', 0, 1, 1766211387000, 1766211387000, 0, 1);
INSERT INTO `message` VALUES (9, '高赞留言', '这条留言获得了很多点赞，测试点赞数字段', 99, 2, 1766207787000, 1766207787000, 0, 0);
INSERT INTO `message` VALUES (10, '管理员2留言', '管理员2发布的管理通知', 5, 6, 1766204187000, 1766204587000, 0, 0);

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '回复唯一ID',
  `message_id` bigint UNSIGNED NOT NULL COMMENT '所属主留言ID（关联message.id）',
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '回复者ID（关联user.id）',
  `parent_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '回复父ID：NULL-回复主留言 非NULL-多级回复',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间戳（毫秒），默认1970-01-01 00:00:00',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_message_id`(`message_id` ASC) USING BTREE COMMENT '优化按主留言查询回复的性能',
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE COMMENT '优化按父回复查询子回复的性能',
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE COMMENT '优化按回复者查询回复的性能',
  CONSTRAINT `fk_reply_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reply_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reply_parent` FOREIGN KEY (`parent_id`) REFERENCES `reply` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储对主留言的回复，支持多级回复，仅允许创建/逻辑删除，无更新操作' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES (1, 1, 2, NULL, '回复普通用户1的第一条留言，我是普通用户2~', 1766236577000, 0);
INSERT INTO `reply` VALUES (2, 1, 3, 1, '回复普通用户2的评论，我是普通用户3（二级回复）', 1766236578000, 0);
INSERT INTO `reply` VALUES (3, 2, 1, NULL, '回复普通用户2的第二条留言，互相交流一下~', 1766236579000, 0);
INSERT INTO `reply` VALUES (4, 5, 5, NULL, '管理员的公告很有用，感谢分享！', 1766236580000, 0);
INSERT INTO `reply` VALUES (5, 5, 6, 4, '不客气，这是管理员应该做的~', 1766236581000, 0);
INSERT INTO `reply` VALUES (6, 9, 3, NULL, '这条留言确实不错，给你点赞！', 1766236582000, 0);
INSERT INTO `reply` VALUES (7, 9, 7, 6, '我也觉得不错，支持一下', 1766236583000, 0);
INSERT INTO `reply` VALUES (8, 9, 2, 7, '感谢大家的认可~', 1766236584000, 0);
INSERT INTO `reply` VALUES (9, 10, 1, NULL, '收到管理员2的通知，会遵守相关规则', 1766236585000, 0);
INSERT INTO `reply` VALUES (10, 1, 8, NULL, '这条回复的创建者被软删除了，测试关联数据', 1766236586000, 1);

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '举报记录ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '举报用户ID（关联user.id）',
  `message_id` bigint UNSIGNED NOT NULL COMMENT '举报的主留言ID（关联message.id）',
  `reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报原因',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '举报时间戳（毫秒），默认1970-01-01 00:00:00',
  `audit_status` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核 1-审核通过 2-审核驳回',
  `audit_user_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '审核管理员ID（关联user.id）',
  `audit_time` bigint NULL DEFAULT 0 COMMENT '审核时间戳（毫秒），默认1970-01-01 00:00:00',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE COMMENT '优化按审核状态查询举报记录的性能',
  INDEX `idx_message_id`(`message_id` ASC) USING BTREE COMMENT '优化按被举报留言查询举报记录的性能',
  INDEX `fk_report_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_report_audit_user`(`audit_user_id` ASC) USING BTREE,
  CONSTRAINT `fk_report_audit_user` FOREIGN KEY (`audit_user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_report_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_report_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储用户对主留言的举报信息，支持管理员审核流程' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (1, 1, 3, '这条留言包含违规信息，不符合社区规范', 1766236487000, 1, 6, 1766236497000);
INSERT INTO `report` VALUES (2, 2, 4, '留言内容涉嫌不当，请求审核', 1766236497000, 2, 6, 1766236507000);
INSERT INTO `report` VALUES (3, 3, 1, '恶意灌水，无意义内容', 1766236507000, 0, NULL, 0);
INSERT INTO `report` VALUES (4, 5, 2, '留言包含广告信息，影响体验', 1766236517000, 1, 7, 1766236527000);
INSERT INTO `report` VALUES (5, 7, 6, '内容低俗，请求处理', 1766236527000, 0, NULL, 0);
INSERT INTO `report` VALUES (6, 1, 9, '涉嫌抄袭他人内容', 1766236537000, 2, 6, 1766236547000);
INSERT INTO `report` VALUES (7, 2, 10, '管理员留言不符合规范', 1766236547000, 0, NULL, 0);
INSERT INTO `report` VALUES (8, 4, 3, '重复举报违规留言', 1766236557000, 1, 7, 1766236567000);
INSERT INTO `report` VALUES (9, 6, 8, '已删除留言仍可举报？测试边界情况', 1766236567000, 2, 6, 1766236577000);
INSERT INTO `report` VALUES (10, 8, 5, '软删除用户举报管理员留言', 1766236577000, 0, NULL, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱（登录账号）唯一',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（建议MD5/BCrypt加密存储）',
  `role` tinyint NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户 1-管理员',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：-1-停用 0-正常',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间戳（毫秒），默认1970-01-01 00:00:00',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE COMMENT '邮箱唯一索引，避免重复注册'
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储用户基础信息，区分普通用户/管理员，管控用户状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'user1@test.com', '普通用户1', '123456', 0, 0, 1766236587000, 0);
INSERT INTO `user` VALUES (2, 'user2@test.com', '普通用户2', '123456', 0, 0, 1766232987000, 0);
INSERT INTO `user` VALUES (3, 'user3@test.com', '普通用户3', '123456', 0, 0, 1766229387000, 0);
INSERT INTO `user` VALUES (4, 'user4@test.com', '普通用户4', '123456', 0, -1, 1766225787000, 0);
INSERT INTO `user` VALUES (5, 'user5@test.com', '普通用户5', '123456', 0, 0, 1766222187000, 0);
INSERT INTO `user` VALUES (6, 'admin1@test.com', '管理员1', '123456', 1, 0, 1766218587000, 0);
INSERT INTO `user` VALUES (7, 'admin2@test.com', '管理员2', '123456', 1, 0, 1766214987000, 0);
INSERT INTO `user` VALUES (8, 'user6@test.com', '普通用户6', '123456', 0, 0, 1766211387000, 0);
INSERT INTO `user` VALUES (9, 'user7@test.com', '普通用户7', '123456', 0, 0, 1766207787000, 0);
INSERT INTO `user` VALUES (10, 'user8@test.com', '普通用户8', '123456', 0, 0, 1766204187000, 1);

SET FOREIGN_KEY_CHECKS = 1;
