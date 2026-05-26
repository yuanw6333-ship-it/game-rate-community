-- GameRate V1 初始化建表脚本
-- 目标数据库：MySQL 8.x

CREATE DATABASE IF NOT EXISTS `gamerate`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `gamerate`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
  `password_hash` VARCHAR(100) NOT NULL COMMENT '密码哈希',
  `nickname` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别：0未知，1男，2女',
  `bio` VARCHAR(255) DEFAULT NULL COMMENT '个人简介',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1正常',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`),
  UNIQUE KEY `uk_user_email` (`email`),
  KEY `idx_user_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `admin` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL COMMENT '管理员登录名',
  `password_hash` VARCHAR(100) NOT NULL COMMENT '密码哈希',
  `nickname` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '管理员昵称',
  `role_code` VARCHAR(50) NOT NULL DEFAULT 'super_admin' COMMENT '角色编码',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1正常',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_username` (`username`),
  KEY `idx_admin_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `code` VARCHAR(50) NOT NULL COMMENT '分类编码',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '分类说明',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`code`),
  UNIQUE KEY `uk_category_name` (`name`),
  KEY `idx_category_status_sort` (`status`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏分类表';

CREATE TABLE IF NOT EXISTS `platform` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '平台ID',
  `name` VARCHAR(50) NOT NULL COMMENT '平台名称',
  `code` VARCHAR(50) NOT NULL COMMENT '平台编码',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '平台说明',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_platform_code` (`code`),
  UNIQUE KEY `uk_platform_name` (`name`),
  KEY `idx_platform_status_sort` (`status`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏平台表';

CREATE TABLE IF NOT EXISTS `game` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '游戏ID',
  `name` VARCHAR(100) NOT NULL COMMENT '游戏名称',
  `original_name` VARCHAR(100) DEFAULT NULL COMMENT '游戏原名',
  `cover_url` VARCHAR(500) DEFAULT NULL COMMENT '封面图URL',
  `description` TEXT COMMENT '游戏简介',
  `developer` VARCHAR(100) DEFAULT NULL COMMENT '开发商',
  `publisher` VARCHAR(100) DEFAULT NULL COMMENT '发行商',
  `release_date` DATE DEFAULT NULL COMMENT '发售日期',
  `category_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '主分类ID',
  `platform_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '主平台ID',
  `rawg_id` BIGINT UNSIGNED DEFAULT NULL COMMENT 'RAWG 外部游戏ID',
  `source` VARCHAR(30) NOT NULL DEFAULT 'manual' COMMENT '数据来源：manual手动，rawg导入',
  `source_url` VARCHAR(500) DEFAULT NULL COMMENT '外部来源URL',
  `avg_score` DECIMAL(3,1) NOT NULL DEFAULT 0.0 COMMENT '平均评分，0-10',
  `rating_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评分人数',
  `comment_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数量',
  `favorite_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数量',
  `hot_score` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '热度分',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0下架，1上架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_game_rawg_id` (`rawg_id`),
  KEY `idx_game_name` (`name`),
  KEY `idx_game_category` (`category_id`),
  KEY `idx_game_platform` (`platform_id`),
  KEY `idx_game_status` (`status`),
  KEY `idx_game_avg_score` (`avg_score`),
  KEY `idx_game_hot_score` (`hot_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏表';

CREATE TABLE IF NOT EXISTS `game_rating` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `game_id` BIGINT UNSIGNED NOT NULL COMMENT '游戏ID',
  `score` DECIMAL(3,1) NOT NULL COMMENT '评分，0-10',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_game_rating_user_game` (`user_id`, `game_id`),
  KEY `idx_game_rating_game` (`game_id`),
  KEY `idx_game_rating_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏评分表';

CREATE TABLE IF NOT EXISTS `game_comment` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `game_id` BIGINT UNSIGNED NOT NULL COMMENT '游戏ID',
  `content` VARCHAR(1000) NOT NULL COMMENT '评论内容',
  `like_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0隐藏，1显示',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_game_comment_game` (`game_id`),
  KEY `idx_game_comment_user` (`user_id`),
  KEY `idx_game_comment_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏评论表';

CREATE TABLE IF NOT EXISTS `game_favorite` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `game_id` BIGINT UNSIGNED NOT NULL COMMENT '游戏ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_game_favorite_user_game` (`user_id`, `game_id`),
  KEY `idx_game_favorite_game` (`game_id`),
  KEY `idx_game_favorite_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏收藏表';
