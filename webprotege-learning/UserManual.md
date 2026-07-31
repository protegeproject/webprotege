# WebProtégé 用户手册

WebProtégé 是一个免费的、开源的协作式 OWL 2 本体编辑环境。本手册说明如何在浏览器中使用各项功能。

> **相关文档**
> - [FunctionList.md](FunctionList.md) — 功能与技术对照清单（开发者参考）
> - [readme.md](readme.md) — 构建、部署与架构说明
> - [官方用户指南（英文）](http://protegewiki.stanford.edu/wiki/WebProtegeUsersGuide)

---

## 1. 快速开始

### 1.1 访问应用

| 环境 | 地址 |
|------|------|
| 本地 Docker 部署 | http://localhost:5000 |
| Stanford 公共实例 | https://webprotege.stanford.edu |

在浏览器中打开地址即可使用，**无需安装客户端**。

### 1.2 首次使用（本地 Docker）

1. 启动服务：`docker compose up -d`
2. 创建管理员（首次部署）：
   ```bash
   docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
   ```
3. 登录后进入 **Settings**：http://localhost:5000/#application/settings
4. 填写必填项：
   - **System notification email address**（系统通知邮箱）
   - **Application host URL**（如 `http://localhost:5000`）
5. 按需开启：**User creation**、**Project creation**、**Project import**

**本地默认管理员账号（当前部署）：**

| 字段 | 值 |
|------|-----|
| 用户名 | `admin` |
| 密码 | `admin123` |
| 邮箱 | `admin@localhost` |

登录地址：http://localhost:5000 → 右上角 **Sign In**。

> 若首页显示 *"WebProtégé is not configured properly"*，说明上述设置尚未完成。

### 1.3 界面概览

```
┌─────────────────────────────────────────────────────────────┐
│  顶部栏：项目名 · Share · Help · 用户菜单                      │
├──────────┬──────────────────────────────────────────────────┤
│  左侧栏   │  主工作区（Perspective / Tab）                     │
│  · Home  │  ┌─────────────┬─────────────┐                   │
│  · Owned │  │  Portlet 1  │  Portlet 2  │  ...              │
│  · Trash │  │ (层次结构)   │ (编辑器)     │                   │
│          │  └─────────────┴─────────────┘                   │
└──────────┴──────────────────────────────────────────────────┘
```

- **Home**：查看有权限访问的全部项目
- **Owned By Me**：仅显示您拥有的项目
- **Trash**：已移入回收站的项目（可恢复，不会立即删除）

---

## 2. 账户与登录

### 2.1 注册与登录

**本地 Docker 部署默认账号：**

| 字段 | 值 |
|------|-----|
| 用户名 | `admin` |
| 密码 | `admin123` |
| 邮箱 | `admin@localhost` |

| 操作 | 步骤 |
|------|------|
| 注册 | 首页点击 **Sign Up for Account** → 填写用户名、邮箱、密码 |
| 登录 | 右上角 **Sign In** → 输入用户名和密码 |
| 登出 | 用户菜单 → **Sign Out** |
| 修改密码 | 用户菜单 → **Change Password** |

> 注册功能需管理员在 **Settings** 中开启 **User creation**。

### 2.2 忘记密码

1. 登录页选择 **Reset Password**
2. 输入注册邮箱
3. 查收邮件并按链接重置

> 本地部署需正确配置邮件服务（`mail.properties`），否则无法发送重置邮件。

---

## 3. 项目管理

### 3.1 创建空项目

1. 登录后进入 **Home**
2. 点击 **Create Project...**
3. 填写 **Project name** 和 **Description**
4. 确认创建
5. 在项目列表中点击项目名称进入编辑界面

### 3.2 上传已有本体

1. 点击 **Upload Project...**
2. 填写项目名称与描述
3. 选择本体文件并上传

**支持格式：** RDF/XML、Turtle、OWL/XML、OBO 等。

**带本地 import 的本体：** 将文件打包为 zip，其中：
- 顶层本体重命名为 `root-ontology.owl`
- 其余 import 文件放在同一压缩包内

### 3.3 下载项目

1. 在项目列表中找到目标项目
2. 点击该行 **Download** 图标
3. 选择导出格式并保存

### 3.4 回收站

| 操作 | 步骤 |
|------|------|
| 移入回收站 | 项目列表 → 项目操作 → **Move to trash** |
| 查看回收站 | 左侧栏 **Trash** |
| 恢复项目 | 在 Trash 中选择项目 → **Restore** |

### 3.5 项目设置

进入项目后，通过 **Project** 菜单或 **Settings** 可配置：

- 项目名称与描述
- **Prefix declarations**（OWL 命名空间前缀）
- **Dictionary languages**（词典/搜索语言）
- **Webhooks**（变更通知回调）
- **Project tags**（项目标签）

### 3.6 合并上传

向已有项目追加内容：

1. 打开项目
2. **Project** → **Upload and merge** 或 **Upload and append merge**
3. 选择文件并确认

| 方式 | 说明 |
|------|------|
| Upload and merge | 与现有本体合并，可能产生冲突需手动解决 |
| Upload and append merge | 向现有本体追加新内容 |

### 3.7 CSV 导入

1. 打开项目
2. **Project** → **Import from CSV**
3. 按向导映射列与 OWL 属性
4. 确认导入

---

## 4. 共享与权限

### 4.1 邀请协作者

1. 打开项目
2. 点击右上角 **Share**
3. 在共享对话框中添加用户
4. 为每位用户选择角色

### 4.2 内置角色

| 角色 | 典型权限 |
|------|----------|
| **Owner** | 完全控制，含删除项目、管理权限 |
| **Manager** | 管理共享与项目设置 |
| **Editor** | 编辑本体内容 |
| **Commenter** | 查看 + 发表评论/讨论 |
| **Viewer** | 只读访问 |

角色权限为继承式：Owner 包含 Manager 的全部权限，以此类推。

### 4.3 常见权限场景

| 需求 | 建议角色 |
|------|----------|
| 只读浏览 | Viewer |
| 参与讨论但不改本体 | Commenter |
| 日常编辑 | Editor |
| 管理成员与设置 | Manager / Owner |

---

## 5. OWL 2 本体编辑

### 5.1 打开项目后的默认视图

默认提供**简化编辑界面**，覆盖常用 OWL 构造。可通过 **Add Content** 添加更多 OWL 2 专用面板（Portlet）。

### 5.2 创建实体

在 **Class hierarchy**（类层次）或对应 Portlet 中：

| 实体类型 | 操作 |
|----------|------|
| OWL Class（类） | 选中父类 → 右键或工具栏 **Create subclass** |
| Object Property | 属性层次 → **Create sub property** |
| Data Property | 同上 |
| Named Individual | 个体列表 → **Create individual** |
| Annotation Property | 注释属性层次 → 创建 |

也可在层次结构中指定父节点后创建子实体。

### 5.3 编辑实体框架（Frame）

选中实体后，在 **Class editor** / **Property editor** / **Individual editor** 中编辑：

| 实体 | 可编辑内容 |
|------|------------|
| Class | 父类、等价类、不相交类、注释等 |
| Object/Data/Annotation Property | 父属性、定义域、值域、特征（Functional、Transitive 等） |
| Named Individual | 类型（Classes）、属性断言、注释 |

修改后点击 **Save** 或等效确认按钮提交。

### 5.4 Manchester 语法编辑器

1. 添加 **OWL Entity Description Editor** Portlet
2. 选中要编辑的实体
3. 在文本框中用 Manchester Syntax 编写公理
4. 编辑器提供语法高亮、错误检查与自动补全
5. 可一次编辑多条公理，填写变更说明后 **Commit**

**示例（类定义）：**
```
Class: Person
    SubClassOf: Animal
    EquivalentTo: Human
```

### 5.5 浏览实体描述

添加 **OWL Entity Description Browser** Portlet，选中实体即可查看 Manchester Syntax 渲染的完整描述，含跨本体 import 的公理。

### 5.6 修改实体 IRI

1. 选中实体
2. **Edit** → **Change IRI**（或右键菜单）
3. 输入新 IRI 并确认

> 会影响所有引用该 IRI 的公理，操作前请确认影响范围。

### 5.7 删除与合并实体

| 操作 | 步骤 |
|------|------|
| 删除 | 选中实体 → **Delete** → 确认（支持批量选择） |
| 合并 | 选中源实体与目标实体 → **Merge** → 确认 |

### 5.8 查看已弃用实体

添加 **Deprecated entities** 相关 Portlet，或在搜索中过滤 deprecated 实体。

### 5.9 本体级注释

通过 **Ontology annotations** Portlet 编辑本体级别的 metadata（如 `owl:versionInfo`、`rdfs:comment`）。

---

## 6. 层次结构浏览

### 6.1 类层次结构（Class Hierarchy）

- **展开/折叠**：点击节点前的箭头
- **选择实体**：单击节点，其他 Portlet 同步更新
- **移动节点**：拖拽到新的父类下（需 Editor 权限）
- **路径到根**：查看从当前类到 `owl:Thing` 的路径

### 6.2 属性层次结构

与类层次操作类似，分别适用于 Object Property、Data Property、Annotation Property。

### 6.3 个体列表

1. 添加 **Individuals** Portlet
2. 选择类，查看该类的命名个体
3. 可在此创建、编辑、删除个体

---

## 7. OBO 本体编辑

适用于 OBO 格式本体或 OBO 风格项目：

| 功能 | 操作位置 |
|------|----------|
| 术语 ID | **OBO Term Id Editor** |
| 定义 (def) | **OBO Definition Editor** |
| 同义词 | **OBO Synonym Editor**（exact / narrow / broad / related） |
| 关系 (is_a, part_of 等) | **OBO Relations Editor** |
| 交叉引用 (XRef) | **OBO XRef Editor** |
| Cross Product | **OBO Cross Product Editor** |

上传 OBO 文件时，使用 **Upload Project** 并选择 `.obo` 格式。

---

## 8. 表单系统（Forms）

表单系统允许为特定实体类型定制结构化编辑界面。

### 8.1 管理表单

1. **Project** → **Forms** 或添加 **Forms** Portlet
2. **Create form** → 定义表单名称与字段
3. 将表单 **绑定** 到特定类或个体
4. 可从其他项目 **Copy forms**

### 8.2 使用表单编辑

1. 选中已绑定表单的实体
2. 在 **Form editor** Portlet 中填写字段
3. 保存后自动生成对应 OWL 断言

### 8.3 网格视图（Grid）

- 以表格形式批量查看/编辑多个实体的表单数据
- 适合维护大量结构相似的术语（如 OBO 本体）

---

## 9. 搜索与查询

### 9.1 实体搜索

1. 添加 **Search** Portlet，或使用顶部搜索框
2. 输入实体短名、标签或 IRI 片段
3. 从结果列表中选择实体

搜索由 Lucene 驱动，支持多语言词典（可在项目设置中配置语言）。

### 9.2 DL 查询（Match）

1. 添加 **DL Query** Portlet
2. 构建描述逻辑查询条件（类表达式、属性约束等）
3. 执行查询，返回匹配的类或个体

### 9.3 用法查看（Usage）

1. 选中实体
2. 打开 **Usage** Portlet
3. 查看该实体在本体中被引用的所有位置

---

## 10. 协作功能

### 10.1 实体讨论（Threaded Notes）

1. 选中实体
2. 打开 **Discussion** / **Comments** Portlet
3. **Add comment** 发起或回复讨论
4. 可将线程标记为 **Resolved** / **Closed**

### 10.2 项目议题（Issues）

1. 打开 **Issues** Portlet
2. **Create issue** → 填写标题与正文
3. 可 **Assign** 负责人、**Close** 议题
4. 在议题下添加评论

### 10.3 关注（Watches）

| 操作 | 步骤 |
|------|------|
| 添加关注 | 选中实体 → **Watch** 或 Watches Portlet → **Add watch** |
| 查看关注列表 | **Watches** Portlet |
| 接收通知 | 配置邮箱后，被关注实体的变更会发送邮件通知 |

### 10.4 实体与项目标签

- **Entity tags**：为实体添加自定义标签，便于分类检索
- **Project tags**：在项目设置中管理项目级标签

### 10.5 项目动态（Feed）

**Project Feed** Portlet 显示项目活动时间线：编辑、评论、议题等。

---

## 11. 变更历史与回滚

### 11.1 查看变更

| 范围 | Portlet |
|------|---------|
| 整个项目 | **Change history** / **Revisions** |
| 单个实体 | **Entity changes** |
| 被关注实体 | **Watched changes** |

每次编辑产生一个 **revision**，包含时间、作者与变更摘要。

### 11.2 回滚

1. 打开 **Revisions** Portlet
2. 选择目标 revision
3. 点击 **Revert to this revision**（需相应权限）
4. 确认回滚

> 回滚会创建新的 revision，不会删除历史记录。

### 11.3 下载历史快照

在 Revisions 列表中，可下载任意 revision 对应的本体快照。

---

## 12. 实体关系图（Entity Graph）

> 界面中**没有**名为 "Visualization" 的功能，关系可视化正式名称为 **Entity Graph**。

### 12.1 方式一：编辑器内标签页（推荐）

适用于 **Individual**、**Class**、**Entity Editor** 等编辑器面板：

1. 切换到 **Individuals** 视角（演示个体数据时推荐）
2. 选中一个命名个体（如故障事件 `FAULT-20250711-001`）
3. 在右侧编辑器顶部，点击中间的 **Entity Graph** 图标标签页（位于 Details 与 Changes 之间）
4. 图形展示该实体的一阶邻域关系

### 12.2 方式二：添加独立面板

1. 点击 **Add Content** 添加面板
2. 在列表中选择 **Entity Graph**
3. 选中起始实体，图形自动更新

### 12.3 过滤边类型

- 在 Entity Graph 设置中选择要显示的关系类型（如 `triggersException`、`affectsWorkOrder`）
- 可保存个人偏好或设置项目级默认过滤规则

---

## 13. 批量操作

适用于需同时修改多个实体的场景：

| 操作 | 说明 |
|------|------|
| 批量编辑注释 | 同时修改多个实体的 annotation |
| 批量设置注释值 | 统一设置 annotation 值 |
| 批量删除注释 | 删除指定 annotation |
| 批量移动到父节点 | 将多个类移到新的父类下 |

入口通常在 **Bulk operations** 菜单或选中多个实体后的右键菜单。

---

## 14. 界面定制（Perspective）

### 14.1 管理工作区布局

1. 点击 Tab 栏旁的 **Perspective** 菜单
2. **Create perspective** 新建工作区
3. 通过 **Add Content** 向当前 Tab 添加 Portlet
4. 拖拽 Portlet 标题栏调整布局
5. 布局自动保存，下次打开项目时恢复

### 14.2 常用 Portlet 组合

| 工作场景 | 推荐 Portlet |
|----------|--------------|
| 日常 OWL 编辑 | Class hierarchy + Class editor + Search |
| 深度 OWL 编辑 | Class hierarchy + OWL Entity Description Editor |
| OBO 策展 | Class hierarchy + OBO editors + Grid |
| 协作审查 | Class hierarchy + Discussion + Change history |
| 质量检查 | DL Query + Usage + Deprecated entities |

### 14.3 内置 Portlet 速查

| Portlet | 功能 |
|---------|------|
| Class / Property hierarchy | 树形导航 |
| Class / Property / Individual editor | 框架编辑 |
| OWL Entity Description Editor | Manchester 语法编辑 |
| OWL Entity Description Browser | 描述浏览 |
| Form editor | 自定义表单 |
| OBO Term Editor | OBO 专用字段 |
| Search | 实体搜索 |
| DL Query | 描述逻辑查询 |
| Usage | 引用位置 |
| Entity Graph | 实体关系图（非 Visualization） |
| Change history / Revisions | 变更历史 |
| Discussion | 实体评论 |
| Watches | 关注列表 |
| Project Feed | 活动流 |
| Ontology annotations | 本体注释 |
| Individuals | 命名个体列表 |

---

## 15. REST API（程序集成）

面向开发者或自动化脚本，基础路径为 `/data/*`，需 **API Key** 认证。

### 15.1 获取 API Key

```bash
docker exec -it webprotege java -jar /webprotege-cli.jar generate-api-key
```

### 15.2 常用接口

| 接口 | 方法 | 用途 |
|------|------|------|
| `/data/projects` | GET | 列出项目 |
| `/data/projects` | POST | 创建项目 |
| `/data/projects/{id}` | GET | 项目详情 |
| `/data/projects/{id}/axioms` | POST | 添加公理 |
| `/data/projects/{id}/delete-axioms` | POST | 删除公理 |
| `/data/projects/{id}/revisions` | GET | 修订历史 |
| `/data/projects/{id}/forms` | GET/POST | 表单管理 |
| `/data/projects/{id}/settings` | GET/POST | 项目设置 |

请求时在 Header 中携带 API Key（详见服务端 `AuthenticationFilter` 配置）。

---

## 16. CLI 管理命令

在 Docker 容器中执行：

```bash
docker exec -it webprotege java -jar /webprotege-cli.jar <command>
```

| 命令 | 用途 |
|------|------|
| `create-admin-account` | 创建系统管理员 |
| `generate-api-key` | 生成 REST API 密钥 |
| `rebuild-permissions` | 重建全库权限索引 |
| `set-permissions` | 设置用户/项目权限 |

---

## 17. 常见问题

### Q: 首页提示 "WebProtégé is not configured properly"

**原因：** 系统通知邮箱或 Application host URL 未设置。

**解决：** 管理员登录 → Settings → 填写 **System notification email** 和 **Application host URL**（本地示例：`http://localhost:5000`）。

### Q: 无法注册新用户

**原因：** 管理员未开启 User creation。

**解决：** Settings → 启用 **User creation**。

### Q: 无法创建或上传项目

**原因：** 未开启 Project creation / Project import。

**解决：** Settings → 启用对应选项。

### Q: 上传带 import 的本体失败

**解决：** 使用 zip 打包，顶层本体命名为 `root-ontology.owl`，其余 import 文件放在同一包内。

### Q: 修改后其他用户看不到

**原因：** 项目未共享或协作者权限不足。

**解决：** Share → 添加用户并分配 Editor 或更高角色。

### Q: 邮件通知不工作

**原因：** 本地部署未配置 SMTP。

**解决：** 配置 `mail.properties` 中的邮件服务器参数。

---

## 18. 典型工作流示例

### 18.1 从零开始构建 OWL 本体

1. Create Project → 输入名称
2. 在 Class hierarchy 中创建根类
3. 逐层添加子类
4. 在 Class editor 中编辑父类、等价类、注释
5. 创建 Object/Data Property 并设置 domain/range
6. 创建 Named Individual 并断言属性
7. Share 邀请协作者

### 18.2 策展 OBO 本体

1. Upload Project → 选择 `.obo` 文件
2. 添加 OBO 系列 Portlet
3. 使用 Grid 批量编辑术语字段
4. 用 Discussion 记录策展决策
5. Download 导出更新后的本体

### 18.3 协作审查变更

1. Editor 完成编辑
2. Reviewer 打开 Change history 查看 revision
3. 在 Discussion 中就具体实体发表评论
4. 如需撤销，Manager 在 Revisions 中回滚

---

## 19. 获取帮助

- 应用内：右上角 **Help** 图标 → 打开[官方用户指南](http://protegewiki.stanford.edu/wiki/WebProtegeUsersGuide)
- 功能对照：[FunctionList.md](FunctionList.md)
- 部署问题：[readme.md](readme.md) 中的 Docker / Maven 章节
