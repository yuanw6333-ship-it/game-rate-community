# GameRate 个人开发流程与项目文件夹框架规划

## 1. 文档目的

本文档用于指导 GameRate 游戏评分社区项目的个人开发流程，并作为提交给 AI 编程工具（如 Codex）的项目初始化说明。

目标是让 Codex 根据本文档生成一个规范的项目文件夹框架，包括：

- 后端项目目录
- Web 用户端目录
- Web 管理后台目录
- 微信小程序端目录
- docs 文档目录
- sql 数据库脚本目录
- README、CHANGELOG、.gitignore 等基础文件

本项目采用 **个人开发模式**，不使用复杂 Git 分支流程，只使用主分支进行版本记录，但保留规范的文档、提交、测试和部署记录。

---

## 2. 项目基本信息

### 2.1 项目名称

```text
GameRate 游戏评分社区
```

### 2.2 项目类型

```text
Java 后端项目 + Web 用户端 + Web 管理后台 + 微信小程序端
```

### 2.3 项目目标

GameRate 是一个多端游戏评分社区，用户可以浏览游戏、搜索游戏、查看游戏详情、评分、评论、收藏；管理员可以维护游戏数据、分类、平台、用户和评论；后续支持微信小程序端访问并部署到云服务器上线。

### 2.4 项目定位

```text
类似“豆瓣评分 + 游戏资料库 + 轻量评论社区”的游戏评分网站。
```

---

## 3. 技术栈规划

### 3.1 后端技术栈

```text
Java 17
Spring Boot
Spring MVC
MyBatis-Plus
MySQL
Redis
JWT
Spring Validation
Lombok
Swagger / Knife4j
Maven
```

### 3.2 Web 前端技术栈

```text
Vue 3
Element Plus
Axios
Pinia
Vue Router
Vite
```

### 3.3 微信小程序端技术栈

```text
uni-app
Vue 3
uView Plus
uni.request
微信开发者工具
```

### 3.4 部署技术栈

```text
Linux
Docker
Nginx
MySQL
Redis
HTTPS
域名
```

---

## 4. 个人开发模式说明

本项目为个人开发项目，不使用复杂的企业多人协作流程。

### 4.1 不使用的流程

```text
不使用 main / develop / feature 多分支开发
不使用 Pull Request
不使用多人 Code Review
不使用复杂 release 分支
```

### 4.2 使用的流程

```text
只使用 main 主分支
使用 Git 记录每个开发阶段
使用 docs 管理项目文档
使用 sql 管理数据库脚本
使用 CHANGELOG 记录版本变化
使用 README 展示项目说明
使用 Git Tag 标记阶段版本
```

### 4.3 核心原则

```text
个人开发不需要复杂分支，但需要规范文档。
个人开发不需要 Pull Request，但需要清晰 Commit。
个人开发不需要多人评审，但需要自测记录。
个人开发不需要复杂发布流程，但需要版本记录和部署文档。
```

---

## 5. 推荐项目根目录结构

Codex 需要根据以下结构生成项目文件夹框架：

```text
gamerate
├── gamerate-backend              # Spring Boot 后端项目
├── gamerate-web                  # Web 用户端项目
├── gamerate-admin                # Web 管理后台项目
├── gamerate-mini                 # 微信小程序端项目
├── docs                          # 项目文档目录
│   ├── 01-项目计划.md
│   ├── 02-需求文档.md
│   ├── 03-数据库设计.md
│   ├── 04-接口设计.md
│   ├── 05-开发流程.md
│   ├── 06-测试记录.md
│   ├── 07-部署文档.md
│   └── 08-简历描述.md
├── sql                           # 数据库脚本目录
│   ├── 01-init-schema.sql
│   ├── 02-init-data.sql
│   └── 03-test-data.sql
├── README.md                     # 项目总说明
├── CHANGELOG.md                  # 版本更新记录
└── .gitignore                    # Git 忽略文件
```

---

## 6. 后端项目目录规划

后端目录名：

```text
gamerate-backend
```

后端推荐包结构：

```text
gamerate-backend
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── gamerate
│   │   │           ├── GameRateApplication.java
│   │   │           ├── common
│   │   │           │   ├── result
│   │   │           │   ├── exception
│   │   │           │   └── constant
│   │   │           ├── config
│   │   │           ├── controller
│   │   │           │   ├── user
│   │   │           │   └── admin
│   │   │           ├── service
│   │   │           │   └── impl
│   │   │           ├── mapper
│   │   │           ├── entity
│   │   │           ├── dto
│   │   │           ├── vo
│   │   │           ├── interceptor
│   │   │           ├── annotation
│   │   │           └── utils
│   │   └── resources
│   │       ├── application.yml
│   │       ├── application-local.yml
│   │       ├── application-prod.yml
│   │       └── mapper
│   └── test
│       └── java
└── .gitignore
```

### 6.1 后端各目录作用

| 目录 | 作用 |
|---|---|
| common/result | 统一返回结果 |
| common/exception | 全局异常处理、自定义异常 |
| common/constant | 系统常量 |
| config | 配置类 |
| controller/user | 用户端接口 |
| controller/admin | 管理后台接口 |
| service | 业务接口 |
| service/impl | 业务实现 |
| mapper | MyBatis-Plus Mapper |
| entity | 数据库实体类 |
| dto | 接收前端请求参数 |
| vo | 返回前端视图数据 |
| interceptor | 登录拦截器 |
| annotation | 自定义注解 |
| utils | 工具类 |
| resources/mapper | XML SQL 文件 |

---

## 7. Web 用户端目录规划

Web 用户端目录名：

```text
gamerate-web
```

推荐目录结构：

```text
gamerate-web
├── package.json
├── vite.config.js
├── index.html
├── README.md
├── src
│   ├── api
│   ├── assets
│   ├── components
│   ├── router
│   ├── stores
│   ├── utils
│   ├── views
│   │   ├── Home
│   │   ├── GameList
│   │   ├── GameDetail
│   │   ├── Ranking
│   │   ├── Login
│   │   ├── Register
│   │   ├── Profile
│   │   ├── MyFavorites
│   │   └── MyRatings
│   ├── App.vue
│   └── main.js
└── .gitignore
```

### 7.1 Web 用户端核心页面

```text
首页
游戏列表页
游戏详情页
排行榜页
搜索结果页
登录页
注册页
个人中心页
我的收藏页
我的评分页
```

---

## 8. Web 管理后台目录规划

Web 管理后台目录名：

```text
gamerate-admin
```

推荐目录结构：

```text
gamerate-admin
├── package.json
├── vite.config.js
├── index.html
├── README.md
├── src
│   ├── api
│   ├── assets
│   ├── components
│   ├── layout
│   ├── router
│   ├── stores
│   ├── utils
│   ├── views
│   │   ├── Login
│   │   ├── Dashboard
│   │   ├── GameManage
│   │   ├── RawgImport
│   │   ├── CategoryManage
│   │   ├── PlatformManage
│   │   ├── UserManage
│   │   └── CommentManage
│   ├── App.vue
│   └── main.js
└── .gitignore
```

### 8.1 管理后台核心页面

```text
管理员登录页
后台首页
游戏管理页
RAWG 游戏导入页
分类管理页
平台管理页
用户管理页
评论管理页
```

---

## 9. 微信小程序端目录规划

微信小程序端目录名：

```text
gamerate-mini
```

推荐使用 uni-app。

推荐目录结构：

```text
gamerate-mini
├── package.json
├── pages.json
├── manifest.json
├── README.md
├── src
│   ├── api
│   ├── components
│   ├── stores
│   ├── utils
│   ├── static
│   └── pages
│       ├── index
│       ├── game-list
│       ├── game-detail
│       ├── ranking
│       ├── search
│       ├── login
│       ├── mine
│       ├── my-favorites
│       └── my-ratings
└── .gitignore
```

### 9.1 小程序核心页面

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
```

---

## 10. docs 文档目录规划

docs 是本项目的重要部分。

目录结构：

```text
docs
├── 01-项目计划.md
├── 02-需求文档.md
├── 03-数据库设计.md
├── 04-接口设计.md
├── 05-开发流程.md
├── 06-测试记录.md
├── 07-部署文档.md
└── 08-简历描述.md
```

---

### 10.1 01-项目计划.md

内容包括：

```text
项目介绍
项目定位
技术栈
功能模块
开发阶段
小程序规划
部署目标
```

---

### 10.2 02-需求文档.md

内容包括：

```text
用户角色
游客功能
普通用户功能
管理员功能
Web 用户端功能
管理后台功能
微信小程序端功能
数据来源
排行榜规则
```

---

### 10.3 03-数据库设计.md

内容包括：

```text
数据库名称
数据表列表
每张表字段
字段类型
主键
唯一索引
表关系
初始数据说明
```

V1 核心表：

```text
user
admin
game
category
platform
game_rating
game_comment
game_favorite
```

---

### 10.4 04-接口设计.md

内容包括：

```text
接口名称
请求方式
请求路径
请求参数
返回结果
是否需要登录
异常情况
```

核心接口分类：

```text
用户接口
游戏接口
分类接口
平台接口
评分接口
评论接口
收藏接口
排行榜接口
RAWG 数据导入接口
后台管理接口
```

---

### 10.5 05-开发流程.md

内容包括：

```text
个人开发模式
Git 使用方式
Commit 规范
开发阶段
测试流程
版本记录方式
Tag 规则
```

---

### 10.6 06-测试记录.md

内容包括：

```text
接口测试记录
业务流程测试记录
Bug 记录
修复记录
部署测试记录
```

---

### 10.7 07-部署文档.md

内容包括：

```text
服务器环境
JDK 安装
MySQL 安装
Redis 安装
Nginx 安装
后端 jar 部署
前端 dist 部署
Nginx 反向代理
域名配置
HTTPS 配置
小程序后端接口配置
```

---

### 10.8 08-简历描述.md

内容包括：

```text
项目名称
项目描述
技术栈
负责内容
核心功能
项目亮点
难点和解决方案
GitHub 地址
线上访问地址
```

---

## 11. sql 数据库脚本目录规划

sql 目录用于保存数据库结构和测试数据。

目录结构：

```text
sql
├── 01-init-schema.sql
├── 02-init-data.sql
└── 03-test-data.sql
```

---

### 11.1 01-init-schema.sql

用于创建数据库和核心业务表。

需要包含：

```text
创建 gamerate 数据库
创建 user 表
创建 admin 表
创建 game 表
创建 category 表
创建 platform 表
创建 game_rating 表
创建 game_comment 表
创建 game_favorite 表
```

---

### 11.2 02-init-data.sql

用于初始化基础数据。

需要包含：

```text
默认管理员账号
基础游戏分类
基础游戏平台
```

基础分类示例：

```text
动作
角色扮演
射击
策略
冒险
独立游戏
体育
模拟经营
恐怖
竞速
```

基础平台示例：

```text
PC
Steam
PlayStation
Xbox
Nintendo Switch
Android
iOS
```

---

### 11.3 03-test-data.sql

用于开发和演示时插入测试数据。

需要包含：

```text
测试用户
测试游戏
测试评分
测试评论
测试收藏
```

---

## 12. Git 使用方式

### 12.1 分支策略

个人开发只使用：

```text
main
```

不需要创建：

```text
develop
feature/*
fix/*
release/*
```

---

### 12.2 提交频率

建议：

```text
每完成一个小功能提交一次
每完成一个接口提交一次
每完成一个文档更新提交一次
每天开发结束前至少提交一次
```

---

### 12.3 Commit 提交规范

格式：

```text
类型(模块): 做了什么
```

常用类型：

| 类型 | 含义 |
|---|---|
| feat | 新功能 |
| fix | 修复问题 |
| docs | 文档修改 |
| style | 代码格式调整 |
| refactor | 代码重构 |
| test | 测试相关 |
| chore | 配置、依赖、构建相关 |
| sql | 数据库脚本修改 |

示例：

```bash
git commit -m "docs(project): add GameRate project plan"
git commit -m "chore(backend): init Spring Boot backend project"
git commit -m "sql(database): add initial table schema"
git commit -m "feat(user): add user register api"
git commit -m "feat(user): add user login api"
git commit -m "feat(game): add game list and detail api"
git commit -m "feat(rawg): add RAWG game import api"
git commit -m "feat(rating): add game rating feature"
git commit -m "fix(rating): fix average score calculation"
git commit -m "docs(api): update rating api document"
```

---

## 13. 版本记录方式

虽然不使用复杂分支，但建议使用 Git Tag 记录阶段版本。

推荐版本规划：

```text
v0.1.0 项目初始化完成
v0.2.0 用户登录注册完成
v0.3.0 游戏模块完成
v0.4.0 RAWG 导入完成
v0.5.0 评分评论收藏完成
v0.6.0 排行榜完成
v0.7.0 Web 用户端完成
v0.8.0 后台管理端完成
v0.9.0 微信小程序端完成
v1.0.0 部署上线版本
```

打 Tag 示例：

```bash
git tag v0.1.0
git push origin v0.1.0
```

---

## 14. CHANGELOG 记录规范

根目录需要创建：

```text
CHANGELOG.md
```

示例内容：

```md
# CHANGELOG

## v0.1.0 - 项目初始化

### Added
- 初始化项目目录结构
- 新增 docs 文档目录
- 新增 sql 数据库脚本目录
- 新增 README.md

### Changed
- 暂无

### Fixed
- 暂无
```

---

## 15. 开发阶段规划

### 15.1 第 0 阶段：项目准备

任务：

```text
创建 gamerate 根目录
创建 GitHub 仓库
初始化 Git
创建 README.md
创建 CHANGELOG.md
创建 docs 目录
创建 sql 目录
提交第一次 commit
```

提交示例：

```bash
git add .
git commit -m "docs(project): init GameRate project documents"
git push
```

---

### 15.2 第 1 阶段：需求和数据库设计

任务：

```text
完善项目计划文档
完善需求文档
设计数据库表
编写 init-schema.sql
编写 init-data.sql
准备测试数据
```

提交示例：

```bash
git commit -m "docs(requirement): add GameRate requirement document"
git commit -m "sql(database): add initial database schema"
```

---

### 15.3 第 2 阶段：后端项目初始化

任务：

```text
创建 Spring Boot 项目
配置 Maven 依赖
配置 MySQL
配置 MyBatis-Plus
配置统一返回 Result
配置全局异常处理
配置 Knife4j
配置基础包结构
```

提交示例：

```bash
git commit -m "chore(backend): init Spring Boot backend project"
git commit -m "feat(common): add result and global exception handler"
```

---

### 15.4 第 3 阶段：用户登录模块

任务：

```text
用户注册
用户登录
密码加密
JWT Token 生成
JWT 拦截器
获取当前用户信息
修改个人资料
```

提交示例：

```bash
git commit -m "feat(user): add user register api"
git commit -m "feat(user): add user login and jwt auth"
git commit -m "docs(api): update user auth api document"
```

---

### 15.5 第 4 阶段：游戏基础模块

任务：

```text
游戏列表
游戏详情
游戏搜索
分类筛选
平台筛选
分页查询
后台新增游戏
后台编辑游戏
后台删除游戏
```

提交示例：

```bash
git commit -m "feat(game): add game list and detail api"
git commit -m "feat(game): add game search and filter api"
git commit -m "feat(admin): add game management api"
```

---

### 15.6 第 5 阶段：RAWG 数据导入模块

任务：

```text
申请 RAWG API Key
配置 RAWG API 参数
封装 RawgClient
搜索外部游戏
查看外部游戏详情
导入游戏资料
保存封面 URL
保存来源信息
```

提交示例：

```bash
git commit -m "feat(rawg): add rawg game search api"
git commit -m "feat(rawg): add rawg game import api"
git commit -m "docs(api): update rawg import api document"
```

---

### 15.7 第 6 阶段：评分模块

任务：

```text
用户提交评分
用户修改评分
查询我的评分
重新计算平均分
更新评分人数
```

提交示例：

```bash
git commit -m "feat(rating): add submit rating api"
git commit -m "feat(rating): recalculate game average score"
```

---

### 15.8 第 7 阶段：评论模块

任务：

```text
发表评论
查看评论列表
删除自己的评论
管理员删除评论
评论分页
```

提交示例：

```bash
git commit -m "feat(comment): add game comment api"
git commit -m "feat(comment): add delete comment api"
```

---

### 15.9 第 8 阶段：收藏模块

任务：

```text
收藏游戏
取消收藏
查询收藏状态
查看我的收藏
更新收藏数
```

提交示例：

```bash
git commit -m "feat(favorite): add favorite game api"
git commit -m "feat(favorite): add my favorite list api"
```

---

### 15.10 第 9 阶段：排行榜模块

任务：

```text
高分榜
热门榜
新游榜
收藏榜
评论榜
hot_score 计算
```

提交示例：

```bash
git commit -m "feat(ranking): add game ranking api"
```

---

### 15.11 第 10 阶段：后台管理端

任务：

```text
管理员登录
游戏管理页面
RAWG 导入页面
分类管理页面
平台管理页面
用户管理页面
评论管理页面
```

提交示例：

```bash
git commit -m "feat(admin): add admin game management page"
git commit -m "feat(admin): add rawg import page"
```

---

### 15.12 第 11 阶段：Web 用户端

任务：

```text
首页
游戏列表页
游戏详情页
登录页
注册页
个人中心
我的收藏
我的评分
排行榜页
```

提交示例：

```bash
git commit -m "feat(web): add game home page"
git commit -m "feat(web): add game detail page"
```

---

### 15.13 第 12 阶段：微信小程序端

任务：

```text
uni-app 项目初始化
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

提交示例：

```bash
git commit -m "chore(mini): init uni-app mini program"
git commit -m "feat(mini): add game detail page"
```

---

### 15.14 第 13 阶段：部署上线

任务：

```text
准备服务器
安装 JDK
安装 MySQL
安装 Redis
安装 Nginx
后端打包 jar
前端打包 dist
部署后端服务
部署前端页面
配置 Nginx 反向代理
配置域名
配置 HTTPS
测试线上接口
提交微信小程序审核
```

提交示例：

```bash
git commit -m "docs(deploy): add server deployment guide"
git commit -m "chore(deploy): add production config template"
```

---

## 16. 每个功能的标准开发流程

每开发一个功能，建议按照以下顺序：

```text
1. 先更新 docs/04-接口设计.md
2. 再设计或修改 sql 脚本
3. 再写 Entity / DTO / VO
4. 再写 Mapper
5. 再写 Service
6. 再写 Controller
7. 用 Knife4j / Apifox 测试接口
8. 更新 docs/06-测试记录.md
9. 更新 CHANGELOG.md
10. Git commit 提交
```

---

## 17. 测试流程

### 17.1 接口测试

工具：

```text
Knife4j
Apifox
Postman
```

每个接口至少测试：

```text
正常情况
参数为空
参数错误
未登录访问
数据不存在
重复提交
权限不足
```

---

### 17.2 业务流程测试

评分流程：

```text
用户注册
用户登录
查看游戏详情
提交评分
修改评分
查看平均分是否变化
查看我的评分记录
```

收藏流程：

```text
用户登录
收藏游戏
再次点击取消收藏
查看我的收藏列表
检查游戏收藏数是否正确
```

评论流程：

```text
用户登录
发表评论
查看评论列表
删除自己的评论
管理员删除评论
```

---

### 17.3 部署测试

上线后测试：

```text
网站是否能访问
接口是否能访问
数据库是否连接正常
图片是否能显示
登录是否正常
小程序是否能请求接口
HTTPS 是否正常
Nginx 代理是否正常
```

---

## 18. .gitignore 建议内容

根目录 `.gitignore` 建议包含：

```gitignore
# Java
target/
*.class
*.log

# IDE
.idea/
*.iml
.vscode/

# OS
.DS_Store
Thumbs.db

# Node
node_modules/
dist/

# Env
.env
.env.local
application-local.yml
application-prod.yml

# Build
*.jar
```

---

## 19. README.md 建议结构

根目录 README.md 建议包含：

```md
# GameRate 游戏评分社区

## 项目介绍

## 技术栈

## 功能模块

## 项目结构

## 本地启动方式

## 数据库初始化

## 接口文档

## 测试账号

## 部署说明

## 项目截图

## 版本记录
```

---

## 20. 给 Codex 的生成要求

请 Codex 根据本文档完成以下任务：

### 20.1 生成项目根目录结构

生成：

```text
gamerate
├── gamerate-backend
├── gamerate-web
├── gamerate-admin
├── gamerate-mini
├── docs
├── sql
├── README.md
├── CHANGELOG.md
└── .gitignore
```

---

### 20.2 生成 docs 初始文档

在 docs 目录下生成：

```text
01-项目计划.md
02-需求文档.md
03-数据库设计.md
04-接口设计.md
05-开发流程.md
06-测试记录.md
07-部署文档.md
08-简历描述.md
```

每个文档先生成基础标题和目录，不需要一次性写满所有内容。

---

### 20.3 生成 sql 初始脚本

在 sql 目录下生成：

```text
01-init-schema.sql
02-init-data.sql
03-test-data.sql
```

要求：

- `01-init-schema.sql` 先写出数据库和核心表的基础结构。
- `02-init-data.sql` 写入基础分类、平台和管理员账号。
- `03-test-data.sql` 预留测试用户、测试游戏、测试评分、测试评论、测试收藏。

---

### 20.4 生成后端基础包结构

在 `gamerate-backend` 中生成 Spring Boot 标准目录结构：

```text
src/main/java/com/gamerate
src/main/resources
src/test/java
```

并生成以下包：

```text
common/result
common/exception
common/constant
config
controller/user
controller/admin
service/impl
mapper
entity
dto
vo
interceptor
annotation
utils
```

---

### 20.5 生成前端基础目录

在 `gamerate-web` 中生成：

```text
src/api
src/assets
src/components
src/router
src/stores
src/utils
src/views
```

在 `gamerate-admin` 中生成：

```text
src/api
src/assets
src/components
src/layout
src/router
src/stores
src/utils
src/views
```

在 `gamerate-mini` 中生成：

```text
src/api
src/components
src/stores
src/utils
src/static
src/pages
```

---

### 20.6 不需要 Codex 立即实现完整功能

本次只要求 Codex 生成：

```text
项目文件夹框架
基础 README
基础 CHANGELOG
docs 文档占位
sql 脚本占位
后端包结构
前端目录结构
小程序目录结构
.gitignore
```

暂时不要一次性生成完整业务代码。

---

## 21. 当前最优执行顺序

当前应该让 Codex 先做：

```text
1. 创建项目根目录 gamerate
2. 创建 backend / web / admin / mini 四个项目目录
3. 创建 docs 目录和 8 个文档
4. 创建 sql 目录和 3 个 SQL 文件
5. 创建 README.md
6. 创建 CHANGELOG.md
7. 创建 .gitignore
8. 创建后端基础包结构
9. 创建前端基础目录结构
10. 创建小程序基础目录结构
```

完成后，再进入下一步：

```text
数据库设计
↓
后端项目初始化
↓
用户注册登录
↓
游戏模块
↓
RAWG 数据导入
↓
评分评论收藏
↓
排行榜
↓
Web 前端
↓
后台管理
↓
微信小程序
↓
部署上线
```
