-- 创建数据库
CREATE DATABASE IF NOT EXISTS ssm_msg_board DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE ssm_msg_board;

-- 1. 创建用户表(user)
CREATE TABLE `user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱（登录账号）',
  `nickname` VARCHAR(32) NOT NULL COMMENT '用户昵称',
  `password` VARCHAR(64) NOT NULL COMMENT '密码（建议MD5/BCrypt加密存储）',
  `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户 1-管理员',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：-1-停用 0-正常',
  `create_time` BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_role_status` (`role`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储用户基础信息，区分普通用户/管理员，管控用户状态';

-- 2. 创建主留言表(message)
CREATE TABLE `message` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '留言唯一ID',
  `title` VARCHAR(128) NOT NULL COMMENT '留言标题',
  `content` TEXT NOT NULL COMMENT '留言内容（支持富文本）',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '获赞数',
  `creator_id` BIGINT UNSIGNED NOT NULL COMMENT '创建者ID（关联user.id）',
  `create_time` BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
  `update_time` BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：-2-违规 -1-待审核/停用 0-正常',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_status_create_time` (`status`, `create_time`),
  KEY `idx_title_content` (`title`, `content`(100)),
  CONSTRAINT `fk_message_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储用户发布的主留言信息，支持编辑、点赞、举报、搜索、排序';

-- 3. 创建回复表(reply)
CREATE TABLE `reply` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '回复唯一ID',
  `message_id` BIGINT UNSIGNED NOT NULL COMMENT '所属主留言ID（关联message.id）',
  `creator_id` BIGINT UNSIGNED NOT NULL COMMENT '回复者ID（关联user.id）',
  `parent_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '回复父ID：NULL-回复主留言 非NULL-多级回复',
  `content` TEXT NOT NULL COMMENT '回复内容',
  `create_time` BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_parent_id` (`parent_id`),
  CONSTRAINT `fk_reply_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reply_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reply_parent` FOREIGN KEY (`parent_id`) REFERENCES `reply` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储对主留言的回复，支持多级回复，仅允许创建/逻辑删除，无更新操作';

-- 4. 创建点赞记录表(like_record)
CREATE TABLE `like_record` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '点赞记录ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '点赞用户ID（关联user.id）',
  `message_id` BIGINT UNSIGNED NOT NULL COMMENT '点赞的主留言ID（关联message.id）',
  `create_time` BIGINT NOT NULL COMMENT '点赞时间戳（毫秒）',
  `is_canceled` TINYINT NOT NULL DEFAULT 0 COMMENT '取消状态：0-未取消 1-已取消',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_message` (`user_id`, `message_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_message_id` (`message_id`),
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_like_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储用户对主留言的点赞行为，防止重复点赞，支持取消点赞';

-- 5. 创建举报记录表(report)
CREATE TABLE `report` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '举报记录ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '举报用户ID（关联user.id）',
  `message_id` BIGINT UNSIGNED NOT NULL COMMENT '举报的主留言ID（关联message.id）',
  `reason` VARCHAR(256) NOT NULL COMMENT '举报原因',
  `create_time` BIGINT NOT NULL COMMENT '举报时间戳（毫秒）',
  `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核 1-审核通过 2-审核驳回',
  `audit_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核管理员ID（关联user.id）',
  `audit_time` BIGINT DEFAULT NULL COMMENT '审核时间戳（毫秒）',
  PRIMARY KEY (`id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_message_id` (`message_id`),
  CONSTRAINT `fk_report_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_report_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_report_audit_user` FOREIGN KEY (`audit_user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储用户对主留言的举报信息，支持管理员审核流程';