-- GameRate V1 初始化基础数据脚本
-- 默认管理员账号：admin，初始密码：admin123456。
-- 初始密码仅用于本地初始化，正式环境上线前必须替换。

USE `gamerate`;

INSERT INTO `admin` (`username`, `password_hash`, `nickname`, `role_code`, `status`, `is_deleted`)
VALUES
  ('admin', '$2a$10$M3QR1QdjSCuiB8Z9Odow..6YHu3KF6gUL.4IbN4OVp1VvU0kVx2bK', '默认管理员', 'super_admin', 1, 0)
ON DUPLICATE KEY UPDATE
  `nickname` = VALUES(`nickname`),
  `role_code` = VALUES(`role_code`),
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);

INSERT INTO `category` (`name`, `code`, `description`, `sort_order`, `status`, `is_deleted`)
VALUES
  ('动作', 'action', '动作类游戏', 10, 1, 0),
  ('角色扮演', 'rpg', '角色扮演类游戏', 20, 1, 0),
  ('射击', 'shooter', '射击类游戏', 30, 1, 0),
  ('策略', 'strategy', '策略类游戏', 40, 1, 0),
  ('冒险', 'adventure', '冒险类游戏', 50, 1, 0),
  ('独立游戏', 'indie', '独立游戏', 60, 1, 0),
  ('体育', 'sports', '体育类游戏', 70, 1, 0),
  ('模拟经营', 'simulation', '模拟经营类游戏', 80, 1, 0),
  ('恐怖', 'horror', '恐怖类游戏', 90, 1, 0),
  ('竞速', 'racing', '竞速类游戏', 100, 1, 0)
ON DUPLICATE KEY UPDATE
  `description` = VALUES(`description`),
  `sort_order` = VALUES(`sort_order`),
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);

INSERT INTO `platform` (`name`, `code`, `description`, `sort_order`, `status`, `is_deleted`)
VALUES
  ('PC', 'pc', 'PC 平台', 10, 1, 0),
  ('Steam', 'steam', 'Steam 平台', 20, 1, 0),
  ('PlayStation', 'playstation', 'PlayStation 平台', 30, 1, 0),
  ('Xbox', 'xbox', 'Xbox 平台', 40, 1, 0),
  ('Nintendo Switch', 'nintendo_switch', 'Nintendo Switch 平台', 50, 1, 0),
  ('Android', 'android', 'Android 平台', 60, 1, 0),
  ('iOS', 'ios', 'iOS 平台', 70, 1, 0)
ON DUPLICATE KEY UPDATE
  `description` = VALUES(`description`),
  `sort_order` = VALUES(`sort_order`),
  `status` = VALUES(`status`),
  `is_deleted` = VALUES(`is_deleted`);
