# GameRate 游戏评分社区：功能板块式开发计划

## 1. 文档说明

本文档基于前面对 **GameRate 游戏评分社区** 的讨论整理而成，按照不同功能板块对项目进行拆分，方便后续逐步开发、测试、上线和写入简历。

项目最终目标：

> 开发一个支持 Web 网站端、后台管理端、微信小程序端的多端游戏评分社区。用户可以浏览游戏、查看游戏介绍、评分、评论、收藏；管理员可以维护游戏数据；系统可以基于站内数据生成排行榜，并最终部署到云服务器上线。

---

## 2. 项目整体架构板块

### 2.1 项目形态

GameRate 最终由以下几个部分组成：

```text
GameRate 游戏评分社区
├── Spring Boot 后端服务
├── Web 用户端
├── Web 管理后台
├── 微信小程序端
├── MySQL 数据库
├── Redis 缓存
└── 云服务器部署环境
```

### 2.2 多端关系

```text
Web 用户端        ┐
微信小程序端      ├── 调用同一套 Spring Boot 后端 API ── MySQL / Redis
Web 管理后台      ┘
```

核心原则：

- Web 网站端、小程序端、后台管理端共用一套后端接口。
- 游戏资料、评分、评论、收藏、排行榜都统一存储在自己的数据库中。
- 第三方 API 只负责辅助导入游戏基础资料，不作为项目核心数据来源。
- 用户评分、评论、收藏和排行榜应该由 GameRate 自己产生。

---

## 3. 技术栈板块

### 3.1 后端技术栈

| 技术 | 用途 |
|---|---|
| Java 17 | 后端主要开发语言 |
| Spring Boot | 快速搭建后端项目 |
| Spring MVC | 提供 RESTful API |
| MyBatis-Plus | 简化数据库 CRUD 和分页查询 |
| MySQL | 存储核心业务数据 |
| Redis | 缓存热门游戏、排行榜、Token 等数据 |
| JWT | 用户登录鉴权 |
| Spring Validation | 参数校验 |
| Lombok | 简化实体类代码 |
| Swagger / Knife4j | 接口文档和接口测试 |
| Maven | 项目依赖管理 |
| Git | 版本控制 |

### 3.2 Web 前端技术栈

| 技术 | 用途 |
|---|---|
| Vue 3 | 用户端和后台管理端开发 |
| Element Plus | Web UI 组件库 |
| Axios | 请求后端接口 |
| Pinia | 管理用户登录状态 |
| Vue Router | 页面路由 |
| Vite | 前端构建工具 |

### 3.3 微信小程序端技术栈

| 技术 | 用途 |
|---|---|
| uni-app | 开发微信小程序端 |
| Vue 3 | 小程序页面语法 |
| uView Plus | 小程序 UI 组件库 |
| uni.request | 请求后端接口 |
| 微信开发者工具 | 调试、预览、上传小程序 |

### 3.4 部署技术栈

| 技术 | 用途 |
|---|---|
| Linux | 云服务器运行环境 |
| Docker | 容器化部署 |
| Nginx | 前端部署和反向代理 |
| MySQL | 生产环境数据库 |
| Redis | 生产环境缓存 |
| HTTPS | 安全访问 |
| 域名 | 公网访问地址 |

---

## 4. 数据来源板块

### 4.1 数据来源总原则

GameRate 的数据来源应该分成三类：

```text
第三方 API：辅助导入游戏基础资料
管理员：编辑、修正和补充游戏资料
站内用户：产生评分、评论、收藏等社区数据
```

最终推荐方案：

```text
RAWG API：初始化游戏名称、封面、截图、发行日期、平台、类型等基础资料
管理员后台：编辑中文简介、修正分类、补充平台、维护游戏数据
GameRate 用户：产生评分、评论、收藏
GameRate 系统：根据站内数据计算排行榜
```

---

### 4.2 游戏介绍数据来源

V1 阶段推荐使用：

```text
RAWG API + 管理员编辑
```

具体流程：

```text
管理员输入游戏名
↓
后端调用 RAWG API 搜索游戏
↓
返回候选游戏列表
↓
管理员选择目标游戏
↓
系统拉取游戏详情
↓
管理员编辑中文简介
↓
保存到 GameRate 数据库
```

游戏简介处理建议：

- 不建议直接复制 Steam、TapTap、百科等网站的大段中文介绍。
- 可以使用 RAWG 英文简介作为参考。
- 可以自己编辑中文简介。
- 也可以使用 AI 辅助生成 100 到 200 字中文简介。
- 最终展示到网站上的简介应该是自己整理后的内容。

---

### 4.3 游戏图片数据来源

V1 阶段推荐：

```text
直接保存 RAWG API 返回的图片 URL
```

数据库中保存：

```text
cover_url
background_url
screenshot_url
```

V1 不建议一开始就下载图片到自己服务器。

原因：

- 开发更快。
- 不需要对象存储。
- 适合项目演示。
- 后续上线后可以再接入 OSS / COS / R2。

后期可扩展：

```text
阿里云 OSS
腾讯云 COS
七牛云
Cloudflare R2
```

---

### 4.4 游戏初始数量建议

V1 初始导入游戏数量不需要太多。

建议：

```text
热门游戏：10 个
高分游戏：10 个
经典游戏：10 个
新游戏：10 个
国产/独立游戏：10 个
```

总数控制在：

```text
30 到 50 个游戏
```

示例游戏：

```text
Elden Ring
Black Myth: Wukong
Cyberpunk 2077
The Witcher 3
Baldur's Gate 3
Red Dead Redemption 2
God of War
Hollow Knight
Stardew Valley
Hades
```

---

### 4.5 不建议的数据获取方式

V1 不建议：

```text
直接爬取 Steam 页面
直接爬取 TapTap 页面
直接爬取 Metacritic 页面
直接复制别的网站游戏简介
直接复制别的平台用户评论
批量下载第三方图片
```

原因：

- 可能违反平台规则。
- 页面结构容易变化。
- 数据稳定性差。
- 版权风险更高。
- 对初级项目来说复杂度过高。

---

## 5. 游戏资料导入板块

### 5.1 功能定位

后台管理端增加一个功能：

```text
从 RAWG 导入游戏
```

这个功能可以作为项目亮点，因为它不是普通 CRUD，而是包含：

- 第三方 API 调用
- 数据清洗
- 数据映射
- 数据落库
- 后台管理
- 数据来源记录

---

### 5.2 导入流程

```text
管理员登录后台
↓
进入游戏管理
↓
点击“从 RAWG 导入”
↓
输入游戏关键词
↓
后端调用 RAWG 搜索接口
↓
展示候选游戏列表
↓
管理员选择目标游戏
↓
后端调用 RAWG 详情接口
↓
展示导入预览
↓
管理员确认保存
↓
写入 game、category、platform 等表
```

---

### 5.3 推荐接口

```http
GET  /api/admin/external/rawg/search?keyword=elden ring
GET  /api/admin/external/rawg/detail/{rawgId}
POST /api/admin/external/rawg/import
```

---

### 5.4 导入字段建议

从 RAWG 导入时，可以保存以下字段：

```text
source_type
source_id
source_url
name
cover_url
background_url
description
release_date
developer
publisher
category
platform
rawg_rating
metacritic_score
last_sync_time
```

注意：

```text
rawg_rating 和 metacritic_score 是外部评分。
average_score 是 GameRate 站内用户评分。
```

不要把外部评分直接当作自己网站的用户评分。

---

## 6. 用户端功能板块

### 6.1 用户角色

用户端分为两类用户：

```text
游客
普通用户
```

### 6.2 游客功能

游客可以：

- 浏览首页
- 查看游戏列表
- 查看游戏详情
- 搜索游戏
- 查看游戏评分
- 查看评论列表
- 查看排行榜

游客不能：

- 给游戏评分
- 发表评论
- 收藏游戏
- 查看个人中心
- 修改个人资料

---

### 6.3 普通用户功能

普通用户登录后可以：

- 注册
- 登录
- 退出登录
- 查看个人信息
- 修改昵称
- 修改头像
- 修改密码
- 浏览游戏
- 搜索游戏
- 给游戏评分
- 修改自己的评分
- 发表评论
- 删除自己的评论
- 收藏游戏
- 取消收藏
- 查看我的收藏
- 查看我的评分记录

---

### 6.4 用户端页面规划

V1 用户端建议包含：

```text
首页
游戏列表页
游戏详情页
搜索结果页
排行榜页
登录页
注册页
个人中心页
我的收藏页
我的评分页
```

---

## 7. 游戏展示板块

### 7.1 游戏列表页功能

游戏列表页需要支持：

- 分页展示游戏
- 根据关键词搜索
- 根据分类筛选
- 根据平台筛选
- 根据评分排序
- 根据热度排序
- 根据发行时间排序

展示字段：

```text
游戏封面
游戏名称
游戏简介
游戏分类
游戏平台
平均评分
评分人数
收藏数
评论数
```

---

### 7.2 游戏详情页功能

游戏详情页需要展示：

```text
游戏封面
游戏背景图
游戏名称
游戏简介
开发商
发行商
发行日期
分类
平台
标签
平均评分
评分人数
浏览量
收藏数
评论数
我的评分
收藏按钮
评论输入框
评论列表
来源链接
```

---

### 7.3 游戏来源标记

如果游戏资料来自 RAWG，详情页可以展示：

```text
部分游戏资料和图片来源：RAWG
```

并保留来源链接。

---

## 8. 评分功能板块

### 8.1 功能列表

评分模块是 GameRate 的核心业务。

V1 需要支持：

- 用户给游戏评分
- 用户修改评分
- 查询当前用户对某游戏的评分
- 查询游戏平均分
- 查询游戏评分人数
- 查看我的评分记录

---

### 8.2 评分规则

```text
用户必须登录后才能评分
一个用户对一个游戏只能有一条评分记录
评分范围为 1 到 10 分
用户可以修改自己的评分
用户评分后需要重新计算游戏平均分
用户评分后需要更新评分人数
```

---

### 8.3 推荐接口

```http
POST /api/ratings
PUT  /api/ratings/{gameId}
GET  /api/ratings/game/{gameId}/me
GET  /api/ratings/me
```

提交评分示例：

```json
{
  "gameId": 1,
  "score": 9
}
```

---

### 8.4 评分业务流程

```text
用户登录
↓
进入游戏详情页
↓
选择 1 到 10 分
↓
提交评分
↓
后端判断是否已评分
↓
未评分则新增评分记录
已评分则更新评分记录
↓
重新计算游戏平均分
↓
更新 game 表 average_score 和 rating_count
↓
返回最新评分信息
```

---

## 9. 评论功能板块

### 9.1 功能列表

V1 评论模块支持：

- 用户发表评论
- 查看某游戏评论列表
- 删除自己的评论
- 管理员删除违规评论
- 评论分页查询

V1 暂不做：

- 评论回复
- 评论点赞
- 敏感词审核
- 举报评论

这些可以放到 V2 或 V3 阶段。

---

### 9.2 评论规则

```text
用户必须登录后才能发表评论
评论内容不能为空
用户只能删除自己的评论
管理员可以删除任意评论
游戏详情页展示评论列表
```

---

### 9.3 推荐接口

```http
POST   /api/comments
GET    /api/comments/game/{gameId}
DELETE /api/comments/{id}
DELETE /api/admin/comments/{id}
```

发布评论示例：

```json
{
  "gameId": 1,
  "content": "这款游戏的战斗系统很优秀，剧情也很有代入感。"
}
```

---

## 10. 收藏功能板块

### 10.1 功能列表

V1 收藏模块支持：

- 收藏游戏
- 取消收藏
- 判断当前用户是否已收藏
- 查看我的收藏列表
- 更新游戏收藏数

---

### 10.2 收藏规则

```text
用户必须登录后才能收藏
一个用户不能重复收藏同一个游戏
已收藏状态下再次点击可以取消收藏
游戏详情页展示当前收藏状态
个人中心可以查看我的收藏
```

---

### 10.3 推荐接口

```http
POST   /api/favorites/{gameId}
DELETE /api/favorites/{gameId}
GET    /api/favorites/{gameId}/status
GET    /api/favorites/me
```

---

## 11. 排行榜功能板块

### 11.1 排行榜数据来源

排行榜推荐使用站内数据计算。

不要完全依赖外部平台排行榜。

站内排行榜数据来自：

```text
game_rating
game_comment
game_favorite
game.view_count
game.average_score
game.rating_count
```

---

### 11.2 V1 排行榜类型

V1 可以先做：

```text
高分榜
热门榜
新游榜
收藏榜
评论榜
```

---

### 11.3 排行规则建议

#### 高分榜

```text
按 average_score 降序排列
评分人数 rating_count 需要大于一定数量
```

示例：

```text
rating_count >= 5
```

#### 热门榜

可以设计一个 hot_score：

```text
hot_score = 浏览量 * 0.2 + 评论数 * 3 + 收藏数 * 5 + 评分人数 * 2
```

#### 新游榜

```text
按 release_date 降序排列
```

#### 收藏榜

```text
按 favorite_count 降序排列
```

#### 评论榜

```text
按 comment_count 降序排列
```

---

### 11.4 推荐接口

```http
GET /api/rankings/high-score
GET /api/rankings/hot
GET /api/rankings/new
GET /api/rankings/favorite
GET /api/rankings/comment
```

---

### 11.5 后期优化

V2 或 V3 可以加入：

```text
Redis 缓存排行榜
定时任务刷新排行榜
ranking 表保存每日榜单
本周热门榜
本月热门榜
```

---

## 12. 后台管理功能板块

### 12.1 管理员功能

管理员后台 V1 需要支持：

- 管理员登录
- 游戏管理
- 分类管理
- 平台管理
- 用户管理
- 评论管理
- RAWG 游戏导入

---

### 12.2 游戏管理

功能：

```text
新增游戏
编辑游戏
删除游戏
上架游戏
下架游戏
查看游戏列表
从 RAWG 导入游戏
```

---

### 12.3 分类管理

功能：

```text
新增分类
编辑分类
删除分类
启用分类
禁用分类
```

---

### 12.4 平台管理

功能：

```text
新增平台
编辑平台
删除平台
启用平台
禁用平台
```

---

### 12.5 用户管理

功能：

```text
查看用户列表
查看用户详情
启用用户
禁用用户
```

---

### 12.6 评论管理

功能：

```text
查看评论列表
按游戏筛选评论
按用户筛选评论
删除违规评论
```

---

### 12.7 后台页面规划

V1 后台页面：

```text
管理员登录页
后台首页
游戏管理页
从 RAWG 导入游戏页
新增游戏页
编辑游戏页
分类管理页
平台管理页
用户管理页
评论管理页
```

---

## 13. 微信小程序端功能板块

### 13.1 小程序定位

微信小程序端主要做轻量用户端，不做复杂后台。

小程序端重点功能：

```text
浏览游戏
搜索游戏
查看详情
评分
评论
收藏
查看个人中心
```

---

### 13.2 小程序 V1 后续页面规划

```text
小程序首页
游戏列表页
游戏详情页
搜索页
排行榜页
登录页
我的页面
我的收藏页
我的评分页
我的评论页
```

---

### 13.3 小程序登录方案

建议分两阶段：

#### 第一阶段：账号密码登录

小程序端使用和 Web 端相同的登录接口：

```http
POST /api/user/login
```

登录成功后保存 Token：

```js
uni.setStorageSync('token', res.data.token)
```

请求接口时携带 Token：

```js
header: {
  Authorization: 'Bearer ' + uni.getStorageSync('token')
}
```

#### 第二阶段：微信授权登录

后期再接入：

```text
wx.login 获取 code
↓
小程序把 code 发给后端
↓
后端用 code 换 openid
↓
根据 openid 判断用户是否存在
↓
不存在则自动注册
↓
存在则直接登录
↓
后端返回 JWT Token
```

---

### 13.4 小程序端接口复用原则

小程序端不单独开发后端。

它直接复用 Web 端接口：

```text
/api/games
/api/games/{id}
/api/ratings
/api/comments
/api/favorites
/api/rankings
/api/user/login
```

---

## 14. 数据库设计板块

### 14.1 V1 核心数据表

V1 建议先设计以下表：

```text
user 用户表
admin 管理员表
game 游戏表
category 游戏分类表
platform 游戏平台表
game_rating 游戏评分表
game_comment 游戏评论表
game_favorite 游戏收藏表
```

---

### 14.2 game 表建议字段

```text
id
source_type
source_id
source_url
steam_app_id
name
cover_url
background_url
description
developer
publisher
release_date
category_id
platform_id
rawg_rating
metacritic_score
average_score
rating_count
view_count
favorite_count
comment_count
status
last_sync_time
create_time
update_time
```

---

### 14.3 user 表建议字段

```text
id
username
password
nickname
avatar
email
status
role
create_time
update_time
```

---

### 14.4 game_rating 表建议字段

```text
id
user_id
game_id
score
create_time
update_time
```

约束建议：

```text
user_id + game_id 设置唯一索引
```

防止同一用户重复评分同一个游戏。

---

### 14.5 game_comment 表建议字段

```text
id
user_id
game_id
content
status
create_time
update_time
```

---

### 14.6 game_favorite 表建议字段

```text
id
user_id
game_id
create_time
```

约束建议：

```text
user_id + game_id 设置唯一索引
```

防止同一用户重复收藏同一个游戏。

---

## 15. 后端接口板块

### 15.1 用户接口

```http
POST /api/user/register
POST /api/user/login
GET  /api/user/profile
PUT  /api/user/profile
PUT  /api/user/password
```

### 15.2 游戏接口

```http
GET    /api/games
GET    /api/games/{id}
GET    /api/games/search
POST   /api/admin/games
PUT    /api/admin/games/{id}
DELETE /api/admin/games/{id}
```

### 15.3 分类接口

```http
GET    /api/categories
POST   /api/admin/categories
PUT    /api/admin/categories/{id}
DELETE /api/admin/categories/{id}
```

### 15.4 平台接口

```http
GET    /api/platforms
POST   /api/admin/platforms
PUT    /api/admin/platforms/{id}
DELETE /api/admin/platforms/{id}
```

### 15.5 评分接口

```http
POST /api/ratings
PUT  /api/ratings/{gameId}
GET  /api/ratings/game/{gameId}/me
GET  /api/ratings/me
```

### 15.6 评论接口

```http
POST   /api/comments
GET    /api/comments/game/{gameId}
DELETE /api/comments/{id}
DELETE /api/admin/comments/{id}
```

### 15.7 收藏接口

```http
POST   /api/favorites/{gameId}
DELETE /api/favorites/{gameId}
GET    /api/favorites/{gameId}/status
GET    /api/favorites/me
```

### 15.8 排行榜接口

```http
GET /api/rankings/high-score
GET /api/rankings/hot
GET /api/rankings/new
GET /api/rankings/favorite
GET /api/rankings/comment
```

### 15.9 外部数据导入接口

```http
GET  /api/admin/external/rawg/search?keyword=elden ring
GET  /api/admin/external/rawg/detail/{rawgId}
POST /api/admin/external/rawg/import
```

---

## 16. 开发阶段板块

### 16.1 第一阶段：后端基础搭建

目标：

```text
Spring Boot 项目可以正常启动
MySQL 可以连接
接口返回格式统一
异常处理统一
JWT 鉴权可用
```

任务：

```text
创建 Spring Boot 项目
配置 MyBatis-Plus
配置 MySQL
创建统一 Result
创建全局异常处理
创建 JWT 工具类
创建登录拦截器
接入 Knife4j
```

---

### 16.2 第二阶段：基础数据和游戏模块

目标：

```text
完成游戏基础数据展示和后台管理
```

任务：

```text
创建数据库表
准备分类和平台数据
完成游戏列表接口
完成游戏详情接口
完成游戏搜索接口
完成后台新增游戏
完成后台编辑游戏
完成后台删除游戏
```

---

### 16.3 第三阶段：RAWG 数据导入

目标：

```text
让管理员可以通过 RAWG API 辅助导入游戏资料
```

任务：

```text
申请 RAWG API Key
配置 rawg.api-key
封装 RawgClient
完成 RAWG 搜索接口
完成 RAWG 详情接口
完成游戏导入接口
完成导入预览
保存来源信息
```

---

### 16.4 第四阶段：用户和登录模块

目标：

```text
完成用户注册登录和鉴权
```

任务：

```text
用户注册
用户登录
密码加密
Token 生成
Token 校验
获取当前用户信息
修改个人资料
```

---

### 16.5 第五阶段：评分、评论、收藏模块

目标：

```text
形成用户互动闭环
```

任务：

```text
提交评分
修改评分
重新计算平均分
发表评论
查看评论列表
删除评论
收藏游戏
取消收藏
查看我的收藏
```

---

### 16.6 第六阶段：排行榜模块

目标：

```text
基于站内数据生成排行榜
```

任务：

```text
高分榜
热门榜
新游榜
收藏榜
评论榜
hot_score 计算规则
排行榜接口
```

---

### 16.7 第七阶段：Web 前端和后台管理

目标：

```text
完成可演示的 Web 页面和后台页面
```

任务：

```text
Web 首页
游戏列表页
游戏详情页
登录注册页
个人中心页
我的收藏页
我的评分页
后台登录页
后台游戏管理
后台 RAWG 导入
后台分类管理
后台平台管理
后台用户管理
后台评论管理
```

---

### 16.8 第八阶段：微信小程序端

目标：

```text
复用后端接口，开发小程序用户端
```

任务：

```text
uni-app 项目创建
小程序首页
游戏列表页
游戏详情页
搜索页
排行榜页
登录页
我的页面
评分功能
评论功能
收藏功能
```

---

### 16.9 第九阶段：部署上线

目标：

```text
让项目可以通过公网访问
```

任务：

```text
购买云服务器
安装 JDK
安装 MySQL
安装 Redis
安装 Nginx
后端打包 jar
前端打包 dist
配置 Nginx 反向代理
配置域名
配置 HTTPS
部署小程序后端接口
提交微信小程序审核
```

---

## 17. V1 优先级板块

### 17.1 必做功能

```text
用户注册登录
JWT 鉴权
游戏列表
游戏详情
游戏搜索
分类筛选
平台筛选
RAWG 游戏导入
游戏评分
平均分计算
评论
收藏
排行榜
后台游戏管理
后台分类管理
后台平台管理
后台用户管理
后台评论管理
```

---

### 17.2 可选功能

```text
头像上传
评论点赞
评论回复
游戏清单
Redis 排行榜缓存
AOP 操作日志
接口限流
微信授权登录
AI 生成游戏中文简介
对象存储保存图片
```

---

### 17.3 暂不建议做的功能

```text
即时聊天
复杂论坛
私信系统
支付系统
游戏购买
复杂推荐算法
爬虫批量抓取第三方网站
复制其他平台用户评论
```

---

## 18. 简历亮点板块

项目完成后，简历可以这样描述：

### 项目名称

GameRate 游戏评分社区

### 项目描述

GameRate 是一个基于 Spring Boot + Vue3 + uni-app 的多端游戏评分社区，支持 Web 用户端、后台管理端和微信小程序端。系统提供游戏浏览、搜索筛选、评分、评论、收藏、排行榜和后台游戏数据管理等功能。后端使用 Spring Boot 提供统一 RESTful API，使用 MyBatis-Plus 完成数据持久化，使用 JWT 实现登录鉴权，并通过 RAWG API 辅助导入游戏基础资料。项目后续可部署至云服务器并支持微信小程序端访问。

### 技术栈

```text
Java 17、Spring Boot、Spring MVC、MyBatis-Plus、MySQL、Redis、JWT、Swagger / Knife4j、Vue3、Element Plus、uni-app、Nginx、Docker、Linux
```

### 项目亮点

1. 使用 Spring Boot 构建统一后端接口，同时支持 Web 用户端、后台管理端和微信小程序端。
2. 使用 JWT + 拦截器实现用户登录鉴权，限制未登录用户访问评分、评论、收藏等接口。
3. 接入 RAWG API 实现游戏资料辅助导入，支持第三方数据搜索、预览、清洗和落库。
4. 设计用户评分业务规则，限制同一用户对同一游戏重复评分，并支持修改评分后重新计算平均分。
5. 基于站内评分、浏览量、评论数、收藏数计算高分榜、热门榜、新游榜等排行榜。
6. 使用统一 Result 返回结构、全局异常处理和参数校验，提高接口规范性。
7. 后台管理端支持游戏、分类、平台、用户、评论和外部数据导入管理，形成完整业务闭环。
8. 项目接口兼容微信小程序端，后续可扩展微信授权登录、分享和小程序审核上线。

---

## 19. 推荐执行路线

建议按照以下顺序推进：

```text
1. 明确需求和功能边界
2. 设计数据库表
3. 搭建 Spring Boot 后端项目
4. 完成用户注册登录和 JWT 鉴权
5. 完成游戏、分类、平台模块
6. 接入 RAWG API 导入初始游戏资料
7. 完成评分、评论、收藏模块
8. 完成排行榜模块
9. 完成 Web 用户端页面
10. 完成后台管理端页面
11. 完成微信小程序端页面
12. 接入 Redis 和缓存优化
13. 部署到云服务器
14. 绑定域名和配置 HTTPS
15. 整理 README、接口文档、部署文档和简历描述
```

---

## 20. 当前最建议先做的事情

当前最应该先完成：

```text
数据库设计
↓
后端项目初始化
↓
游戏表、分类表、平台表设计
↓
RAWG API Key 申请
↓
游戏数据导入接口
↓
导入 30 到 50 个游戏作为初始数据
```

原因：

- 游戏数据是网站展示的基础。
- 没有游戏数据，前端页面无法演示。
- RAWG 导入功能可以作为简历亮点。
- 初始数据准备好后，评分、评论、收藏和排行榜才有业务意义。
