# WebProtégé 功能清单

WebProtégé 是一个免费的、开源的协作式 OWL 2 本体开发环境。本文档基于项目架构、Action 定义、客户端功能包及 REST/CLI 接口整理。


> **使用说明：** 面向终端用户的操作步骤请参阅 [UserManual.md](UserManual.md)。本文档为功能与技术对照参考。

---

> **使用说明：** 面向终端用户的操作步骤请参阅 [UserManual.md](UserManual.md)。本文档为功能与技术对照参考。

## 一、系统架构模块

| 模块 | 职责 | 关键技术 |
|------|------|----------|
| `webprotege-shared-core` | 基础类型、权限枚举 | Java 8 |
| `webprotege-shared` | 客户端/服务端共享 Action/Result DTO | Java 8（GWT 兼容） |
| `webprotege-server-api` | 服务端仓储与服务接口 | — |
| `webprotege-server-lucene` | 实体搜索、短名词典 | Apache Lucene |
| `webprotege-server-core` | 业务逻辑、Action 处理器、OWL API、MongoDB | OWL API 4.5.13 |
| `webprotege-server` | WAR 部署包、Servlet、Jersey REST | Tomcat 8/8.5 |
| `webprotege-client` | GWT 前端 UI（MVP 模式） | GWT 2.8.2 |
| `webprotege-cli` | 命令行管理工具 | — |

### 通信通道

| 路径 | 用途 |
|------|------|
| `/` → `WebProtege.jsp` | GWT 单页应用入口 |
| `/webprotege/dispatchservice` | GWT RPC 命令总线（主 UI 通道） |
| `/webprotege/submitfile` | 文件上传 |
| `/download` | 本体下载 |
| `/data/*` | Jersey REST 外部 API（API Key 认证） |

### 依赖方向

```
webprotege-shared-core ← webprotege-shared ← webprotege-server-api ← webprotege-server-lucene
                                                              ↓
                                                        webprotege-server-core ← webprotege-server (WAR)
                                                              ↑
                                                         webprotege-cli
webprotege-client → webprotege-shared only（不依赖服务端代码）
```

---

## 二、用户与认证

| 功能分类 | 功能项 | 说明 |
|----------|--------|------|
| 账户 | 用户注册 | 创建新用户账户 |
| 账户 | 用户登录/登出 | 会话认证 |
| 账户 | 修改密码 | 已登录用户修改密码 |
| 账户 | 重置密码 | 邮件重置密码 |
| 账户 | 邮箱设置 | 设置/获取通知邮箱 |
| 系统 | 应用级设置 | 全局配置（用户创建、项目创建开关等） |
| 系统 | 管理员创建账户 | CLI：`create-admin-account` |

---

## 三、项目管理

| 功能分类 | 功能项 | 说明 |
|----------|--------|------|
| 项目生命周期 | 创建空项目 | 新建空白 OWL 2 项目 |
| 项目生命周期 | 导入/上传项目 | 支持 RDF/XML、Turtle、OWL/XML、OBO 等格式 |
| 项目生命周期 | 加载项目 | 从 BinaryOWL 修订日志重建内存本体 |
| 项目生命周期 | 查看可用项目列表 | 含权限过滤 |
| 项目生命周期 | 移入/移出回收站 | 软删除与恢复 |
| 项目配置 | 项目详情与信息 | 名称、描述、所有者等 |
| 项目配置 | 前缀声明管理 | 编辑 OWL 命名空间前缀 |
| 项目配置 | 项目设置 | Webhook、字典语言等 |
| 项目配置 | 项目标签 | 为项目添加分类标签 |
| 项目配置 | 语言标签 | 管理多语言标签 |
| 导入导出 | 下载项目 | 导出本体文件 |
| 导入导出 | 上传并合并 | 将上传本体与现有项目合并 |
| 导入导出 | 上传并追加合并 | 向现有本体追加内容 |
| 导入导出 | CSV 导入 | 从 CSV 批量导入数据 |
| 权限 | 共享与权限设置 | 按用户/角色分配访问权限 |
| 权限 | 权限重建 | CLI：`rebuild-permissions` |

---

## 四、OWL 2 本体编辑

| 功能分类 | 功能项 | 对应实体类型 |
|----------|--------|--------------|
| 实体创建 | 创建类 | OWL Class |
| 实体创建 | 创建对象属性 | Object Property |
| 实体创建 | 创建数据属性 | Data Property |
| 实体创建 | 创建命名个体 | Named Individual |
| 实体创建 | 创建注释属性 | Annotation Property |
| 实体创建 | 在层次结构中创建实体 | 指定父节点创建 |
| 实体编辑 | 更新类框架 | 父类、等价类、不相交类等 |
| 实体编辑 | 更新对象/数据/注释属性框架 | 定义域、值域、特征等 |
| 实体编辑 | 更新命名个体框架 | 类型、属性断言等 |
| 实体编辑 | Manchester 语法编辑器 | 文本方式编辑 OWL 框架 |
| 实体编辑 | 修改实体 IRI | 重命名实体 |
| 实体操作 | 删除实体 | 批量删除类/属性/个体 |
| 实体操作 | 合并实体 | 将两个实体合并为一个 |
| 实体操作 | 查看已弃用实体 | Deprecated 实体列表 |
| 实体操作 | 实体查找/渲染 | 按 IRI 或短名查找并 HTML 渲染 |
| 本体级 | 本体注释编辑 | Ontology-level annotations |
| 本体级 | 根本体 ID | 获取根本体标识 |
| CRUD 配置 | 实体创建工具包 | 自定义 IRI 生成规则（UUID、前缀等） |

---

## 五、层次结构浏览与编辑

| 功能项 | 说明 |
|--------|------|
| 类层次结构 | 树形浏览 OWL 类继承关系 |
| 属性层次结构 | 树形浏览对象/数据/注释属性层次 |
| 获取根节点 | 层次结构顶层节点 |
| 获取子节点 | 展开子类/子属性 |
| 获取兄弟节点 | 同级节点导航 |
| 路径到根 | 从当前节点追溯到根 |
| 移动节点 | 拖拽/操作改变父节点关系 |
| 个体列表 | 按类浏览命名个体 |

---

## 六、OBO 本体支持

| 功能项 | 说明 |
|--------|------|
| OBO 术语 ID 编辑 | 管理 OBO 格式术语标识 |
| OBO 定义编辑 | 术语定义（def） |
| OBO 同义词编辑 | exact/narrow/broad/related synonym |
| OBO 关系编辑 | is_a、part_of 等关系 |
| OBO 交叉引用 (XRef) | 外部数据库引用 |
| OBO 交叉产物 (Cross Product) | 等价类交叉产物定义 |
| OBO 命名空间 | 获取 OBO 命名空间列表 |

---

## 七、表单系统（Forms）

| 功能项 | 说明 |
|--------|------|
| 表单描述符管理 | 创建、编辑、删除自定义编辑表单 |
| 实体表单绑定 | 为特定实体类型/个体绑定表单 |
| 表单数据编辑 | 通过表单填写 OWL 断言 |
| 表单复制 | 从其他项目复制表单配置 |
| 网格视图 (Grid) | 表格化批量查看/编辑实体数据 |
| 集合项 (Collection) | 管理表单中的集合类型字段 |

---

## 八、协作与讨论

| 功能分类 | 功能项 | 说明 |
|----------|--------|------|
| 讨论 | 创建实体讨论线程 | 针对特定实体的 threaded 讨论 |
| 讨论 | 添加/编辑/删除评论 | 实体级评论 |
| 讨论 | 讨论线程状态 | 打开/关闭/解决 |
| 讨论 | 已评论实体列表 | 浏览所有有讨论的实体 |
| 议题 (Issues) | 项目级议题 | 独立议题跟踪（标题、正文、评论） |
| 议题 | 议题分配与关闭 | 指派负责人、关闭议题 |
| 关注 (Watches) | 添加/移除关注 | 关注实体变更 |
| 关注 | 关注变更通知 | 邮件通知被关注实体的修改 |
| 标签 | 实体标签 | 为实体添加自定义标签 |
| 标签 | 项目标签 | 项目级标签管理 |
| 动态 | 项目 Feed | 项目活动时间线 |

---

## 九、搜索与查询

| 功能项 | 说明 |
|--------|------|
| 实体搜索 | Lucene 驱动的全文/短名搜索 |
| 搜索设置 | 配置搜索范围与选项 |
| DL 查询 (Match) | 基于 OWL 描述逻辑的实体匹配查询 |
| 查询条件构建 | IRI、注释、日期、类选择等多种条件 |
| 用法查看 (Usage) | 查看实体在本体中的引用位置 |
| 已弃用实体索引 | Lucene 索引弃用实体 |

---

## 十、可视化

| 功能项 | 说明 |
|--------|------|
| 实体关系图 | D3 渲染的实体邻域图 |
| 边过滤条件 | 配置显示的关系类型 |
| 用户图设置 | 保存个人可视化偏好 |
| 默认边条件 | 项目级默认图过滤规则 |

---

## 十一、批量操作

| 功能项 | 说明 |
|--------|------|
| 批量编辑注释 | 同时修改多个实体的 annotation |
| 批量设置注释值 | 统一设置 annotation 值 |
| 批量删除注释 | 删除指定 annotation |
| 批量移动到父节点 | 将多个实体移到新父类下 |

---

## 十二、变更追踪与版本历史

| 功能项 | 说明 |
|--------|------|
| 项目变更历史 | 完整 revision 列表 |
| 实体变更历史 | 单个实体的修改记录 |
| 关注实体变更 | 被关注实体的变更列表 |
| 回滚修订 | 恢复到历史 revision |
| 修订摘要 | 每次变更的描述与时间 |
| 差异对比 (Diff) | 客户端 revision 差异视图 |
| BinaryOWL 持久化 | 每次编辑写入二进制修订日志 |

---

## 十三、界面定制（Perspective）

| 功能项 | 说明 |
|--------|------|
| 视角 (Perspective) 管理 | 创建/删除/重命名工作区布局 |
| 视角布局编辑 | 拖拽排列 Portlet 面板 |
| 视角详情设置 | 名称、描述等元数据 |
| 重置布局 | 恢复默认视角布局 |
| Portlet 面板 | 可自由组合的 UI 组件（层次、编辑器、搜索等） |

### 内置 Portlet 面板

| Portlet | 功能 |
|---------|------|
| 类/属性层次结构 | 树形导航 |
| 类/属性/个体编辑器 | OWL 框架编辑 |
| Manchester 语法编辑器 | 文本编辑 |
| 表单编辑器 | 自定义表单 |
| OBO 术语编辑器 | OBO 专用字段 |
| 实体描述浏览器 | 渲染 OWL 描述 |
| 搜索 | 实体搜索 |
| DL 查询 | 描述逻辑查询 |
| 用法 | 引用位置 |
| 可视化 | 关系图 |
| 变更历史 | 项目/实体 revision |
| 讨论线程 | 实体评论 |
| 已评论实体 | 讨论概览 |
| 关注实体 | Watch 列表 |
| 本体注释 | Ontology annotations |
| 项目 Feed | 活动流 |
| 个体列表 | Named individuals |

---

## 十四、REST API（`/data/*`）

| 资源路径 | HTTP 方法 | 功能 |
|----------|-----------|------|
| `/data/projects` | GET | 列出项目 |
| `/data/projects` | POST | 创建新项目（需 API Key） |
| `/data/projects/{projectId}` | GET | 获取项目详情 |
| `/data/projects/{projectId}/axioms` | POST | 添加公理（RDF/XML、Turtle、Functional Syntax） |
| `/data/projects/{projectId}/delete-axioms` | POST | 删除公理 |
| `/data/projects/{projectId}/revisions` | GET | 列出修订历史 |
| `/data/projects/{projectId}/revisions/{n}` | GET | 获取指定修订 |
| `/data/projects/{projectId}/forms` | GET/POST | 列出/创建表单 |
| `/data/projects/{projectId}/forms/{formId}` | GET | 获取表单详情 |
| `/data/projects/{projectId}/settings` | GET/POST | 读取/更新项目设置 |

---

## 十五、CLI 命令行工具

| 命令 | 功能 |
|------|------|
| `create-admin-account` | 创建系统管理员账户 |
| `generate-api-key` | 生成 REST API 访问密钥 |
| `rebuild-permissions` | 重建全库权限索引 |
| `set-permissions` | 设置用户/项目权限 |

---

## 十六、权限体系（BuiltInAction 摘要）

| 权限域 | 典型权限 |
|--------|----------|
| 系统级 | 创建账户、编辑应用设置、重建权限、代登录 |
| 项目级 | 查看项目、编辑本体、下载、回滚变更 |
| 实体级 | 创建/删除类、属性、个体、数据类型 |
| 协作级 | 创建/关闭议题、评论、编辑他人评论 |
| 配置级 | 编辑共享设置、表单、前缀、标签、可视化默认值 |
| 导入级 | 上传项目、上传合并、上传追加合并 |

**内置角色层级：** Owner → Manager → Editor → Commenter → Viewer（继承式权限）

---

## 十七、技术栈与存储

| 层面 | 技术 | 存储内容 |
|------|------|----------|
| 前端 | GWT 2.8.2 → JavaScript | — |
| 服务端 | Java 11 + Dagger 2 | — |
| OWL 处理 | OWL API 4.5.13 + BinaryOWL | 本体修订日志（文件系统） |
| 元数据 | MongoDB 4.x + Morphia | 用户、权限、讨论、标签、表单配置 |
| 搜索 | Apache Lucene | 实体短名、多语言词典 |
| 内存索引 | 50+ Index 类 | 本体公理高速读取（不查 MongoDB） |
| 缓存 | Caffeine | 项目组件、查询结果 |
| 邮件 | Mustache 模板 | 变更通知、密码重置 |

### 持久化分层

| 存储 | 技术 | 内容 |
|------|------|------|
| MongoDB | Morphia 1.3 ODM | 用户、权限、项目元数据、讨论、标签、表单配置 |
| 文件系统 | BinaryOWL 格式 | 本体修订日志（`data.directory`，默认 `/srv/webprotege`） |
| Lucene | `webprotege-server-lucene` | 实体短名搜索、多语言词典 |

---

## 十八、核心架构机制

| 机制 | 说明 |
|------|------|
| Action/Result 命令总线 | 所有客户端-服务端通信通过 `Action<R>` / `Result` 模式 |
| 变更流程 | 生成 OWL 变更 → 应用 → 持久化 revision → 广播事件 → 更新内存索引 |
| 依赖注入 | Dagger 2：`ServerComponent`（应用级）+ `ProjectComponent`（项目级） |
| 项目缓存 | `ProjectCache` 管理已加载项目，空闲超时后自动卸载（默认 180 秒） |
| 客户端架构 | GWT MVP：Presenter + View（UiBinder）+ `PlaceHistoryHandler` URL 导航 |
