# CHANGELOG

## v0.5.0 - 游戏评分模块

### Added

- 新增游戏评分实体、Mapper、Service、DTO、VO 和 Controller。
- 新增提交或修改评分接口 `POST /api/ratings`。
- 新增查询当前用户指定游戏评分接口 `GET /api/ratings/game/{gameId}/me`。
- 新增分页查询我的评分记录接口 `GET /api/ratings/me`。
- 新增评分模块接口文档和测试记录。

### Changed

- 评分接口接入登录拦截，用户 ID 统一从 JWT 登录上下文获取。
- 新增或修改评分后，从 `game_rating` 表重新聚合计算 `average_score` 和 `rating_count`，并同步更新 `game` 表。

### Fixed

- 通过业务判断和数据库唯一索引共同防止同一用户重复评分同一个游戏。

## v0.4.0 - RAWG 数据导入模块

### Added

- 新增 RAWG 配置读取 `RawgProperties` 和 `RestTemplate` Bean。
- 新增 `RawgClient`，封装 RAWG 搜索和详情请求逻辑。
- 新增后台 RAWG 搜索接口 `GET /api/admin/rawg/search`。
- 新增后台 RAWG 详情预览接口 `GET /api/admin/rawg/games/{rawgId}`。
- 新增后台 RAWG 导入接口 `POST /api/admin/rawg/import`。
- 新增 RAWG DTO、VO、Service 和后台 Controller。
- 更新接口设计文档和测试记录。

### Changed

- `application-local.yml` 和 `application-prod.yml` 增加 RAWG 配置占位，真实 API Key 通过环境变量 `RAWG_API_KEY` 提供。
- 保留 RAWG 数据导入模块。由于本地网络无法连接 `api.rawg.io:443`，暂缓联调。
- 新增 10 条手动游戏测试数据，支持后续评分、评论、收藏模块开发。

### Fixed

- 移除本地配置文件中的真实 RAWG API Key，避免密钥写入项目文件。
- 增加 RAWG 请求 URL 脱敏日志，仅显示 API Key 是否存在。
- 细化 RAWG 网络超时、客户端错误、服务端错误和通用请求异常提示。
- 将 RAWG 请求连接超时和读取超时统一设置为 10 秒。

## v0.3.0 - 游戏基础模块

### Added

- 新增分类、平台、游戏实体、Mapper、Service、VO、DTO 和 Controller。
- 新增用户端分类列表接口 `GET /api/categories`。
- 新增用户端平台列表接口 `GET /api/platforms`。
- 新增用户端游戏分页列表接口 `GET /api/games`。
- 新增用户端游戏详情接口 `GET /api/games/{id}`，查询详情时浏览量加 1。
- 新增后台游戏分页、新增、修改、删除或下架接口。
- 新增游戏基础模块接口文档和测试记录。

### Changed

- 后台游戏接口接入登录拦截。
- 游戏热度排序按浏览数、收藏数、评论数综合排序。

### Fixed

- 暂无

## v0.1.0 - 项目初始化

### Added

- 创建 GameRate 基础项目目录框架
- 创建 docs 文档占位文件
- 创建 sql 脚本占位文件
- 创建后端推荐包结构
- 创建 Web、管理后台、小程序基础 src 目录结构

### Changed

- 暂无

### Fixed

- 暂无
