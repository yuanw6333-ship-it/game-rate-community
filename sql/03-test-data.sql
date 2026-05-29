-- GameRate V1 开发测试数据脚本
-- 仅用于本地开发或演示环境，正式环境不要执行。

USE `gamerate`;

INSERT INTO `user` (`username`, `password_hash`, `nickname`, `avatar_url`, `email`, `bio`, `status`, `is_deleted`)
VALUES
  ('test_user', '$2a$10$M3QR1QdjSCuiB8Z9Odow..6YHu3KF6gUL.4IbN4OVp1VvU0kVx2bK', '测试玩家', NULL, 'test_user@example.com', '用于本地开发测试的普通用户', 1, 0)
ON DUPLICATE KEY UPDATE
  `nickname` = VALUES(`nickname`),
  `bio` = VALUES(`bio`),
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);

INSERT INTO `game` (
  `name`,
  `original_name`,
  `description`,
  `developer`,
  `publisher`,
  `release_date`,
  `category_id`,
  `platform_id`,
  `source_type`,
  `source_id`,
  `source_url`,
  `steam_app_id`,
  `cover_url`,
  `background_url`,
  `rawg_rating`,
  `metacritic_score`,
  `average_score`,
  `rating_count`,
  `view_count`,
  `comment_count`,
  `favorite_count`,
  `hot_score`,
  `status`,
  `is_deleted`
)
VALUES
  (
    '艾尔登法环',
    'ELDEN RING',
    '开放世界动作角色扮演游戏测试数据。',
    'FromSoftware',
    'Bandai Namco Entertainment',
    '2022-02-25',
    (SELECT `id` FROM `category` WHERE `code` = 'rpg' LIMIT 1),
    (SELECT `id` FROM `platform` WHERE `code` = 'steam' LIMIT 1),
    'manual',
    'test-elden-ring',
    'https://example.com/games/elden-ring',
    1245620,
    'https://example.com/images/elden-ring-cover.jpg',
    'https://example.com/images/elden-ring-bg.jpg',
    4.50,
    96,
    9.5,
    1,
    0,
    1,
    1,
    95.00,
    1,
    0
  )
ON DUPLICATE KEY UPDATE
  `description` = VALUES(`description`),
  `developer` = VALUES(`developer`),
  `publisher` = VALUES(`publisher`),
  `release_date` = VALUES(`release_date`),
  `category_id` = VALUES(`category_id`),
  `platform_id` = VALUES(`platform_id`),
  `cover_url` = VALUES(`cover_url`),
  `background_url` = VALUES(`background_url`),
  `rawg_rating` = VALUES(`rawg_rating`),
  `metacritic_score` = VALUES(`metacritic_score`),
  `average_score` = VALUES(`average_score`),
  `rating_count` = VALUES(`rating_count`),
  `view_count` = VALUES(`view_count`),
  `comment_count` = VALUES(`comment_count`),
  `favorite_count` = VALUES(`favorite_count`),
  `hot_score` = VALUES(`hot_score`),
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);

SET @test_user_id = (SELECT `id` FROM `user` WHERE `username` = 'test_user' LIMIT 1);
SET @test_game_id = (SELECT `id` FROM `game` WHERE `source_type` = 'manual' AND `source_id` = 'test-elden-ring' LIMIT 1);

INSERT INTO `game_rating` (`user_id`, `game_id`, `score`, `content`, `status`, `is_deleted`)
VALUES
  (@test_user_id, @test_game_id, 9.5, '地图探索和战斗体验都很出色。', 1, 0)
ON DUPLICATE KEY UPDATE
  `score` = VALUES(`score`),
  `content` = VALUES(`content`),
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);

INSERT INTO `game_comment` (`user_id`, `game_id`, `content`, `like_count`, `status`, `is_deleted`)
SELECT @test_user_id, @test_game_id, '这是一条用于开发联调的测试评论。', 0, 1, 0
WHERE NOT EXISTS (
  SELECT 1
  FROM `game_comment`
  WHERE `user_id` = @test_user_id
    AND `game_id` = @test_game_id
    AND `content` = '这是一条用于开发联调的测试评论。'
);

INSERT INTO `game_favorite` (`user_id`, `game_id`, `status`, `is_deleted`)
VALUES
  (@test_user_id, @test_game_id, 1, 0)
ON DUPLICATE KEY UPDATE
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);
