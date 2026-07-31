# WebProtégé 第三方集成手册

本文档说明第三方应用如何与 WebProtégé 集成，涵盖业务数据写入本体实例、通过接口读取本体数据，以及推荐集成架构与能力边界。

> **相关文档：** [UserManual.md](UserManual.md)（用户操作）、[FunctionList.md](FunctionList.md)（功能清单）、[demo-data/README.md](demo-data/README.md)（汽车故障 Demo 数据）

---

## 1. 集成架构总览

WebProtégé 面向第三方集成的核心通道是 **REST API（`/data/*`）**，辅以 **本体下载（`/download`）** 和 **Webhook 变更通知**。

```
┌─────────────┐                    ┌──────────────────────────────┐
│  第三方系统  │                    │         WebProtégé           │
│  ERP/MES   │─── ETL/映射 ──────▶│  /data/* REST API (API Key)  │
│  业务应用   │                    │  /download (Session)         │
└─────────────┘                    │  Webhook 回调                │
       ▲                           │  OWL 本体 (TBox + ABox)      │
       │                           └──────────────────────────────┘
       │                                      │
       └──────── Webhook / revisions API ─────┘
```

**两条通信通道：**

| 通道 | 路径 | 认证方式 | 适用场景 |
|------|------|----------|----------|
| Jersey REST | `/data/*` | API Key 或 Session | 程序化读写、自动化集成 |
| 下载 Servlet | `/download` | Session（浏览器登录） | 全量本体导出 |
| GWT RPC | `/webprotege/dispatchservice` | Session | UI 内部通信，**不建议第三方直接调用** |

---

## 2. 前置准备

### 2.1 生成 API Key

在 Docker 容器中执行：

```bash
docker exec -it webprotege java -jar /webprotege-cli.jar generate-api-key
```

按提示输入用户名（须为已存在账户）和用途说明，系统将输出 API Key。**请妥善保管，丢失后无法恢复，只能重新生成。**

### 2.2 认证方式

所有 `/data/*` 请求须在 HTTP Header 中携带：

```
Authorization: ApiKey <your-api-key-here>
```

认证逻辑见服务端 `AuthenticationFilter`：优先解析 API Key，若无则回退到浏览器 Session。无法识别用户时返回 HTTP 401。

### 2.3 权限要求

| 操作 | 所需权限 | 典型角色 |
|------|----------|----------|
| 写入公理（添加/删除实例） | `EDIT_ONTOLOGY` | Editor 及以上 |
| 读取修订历史 | 项目访问权限 | Viewer 及以上 |
| 下载全量本体 | `DOWNLOAD_PROJECT` | 具备下载权限的角色 |
| 创建项目 | 有效 API Key + 创建权限 | Owner / 管理员 |

API Key 绑定到具体用户，该用户对目标项目的角色决定实际操作权限。

### 2.4 本体模型先行（TBox）

业务数据以 **ABox（实例断言）** 形式写入。写入前须确保目标项目中已存在：

- 相关 **类（Class）** 定义
- 相关 **对象属性 / 数据属性** 定义
- 必要的 **命名空间前缀**

可先通过 UI 上传完整本体（如 `demo-data/automotive-equipment-fault-demo.ttl`），或通过 REST API 分批写入 TBox 公理。

### 2.5 管理员配置

确保系统 Settings 中已开启：

- **Project creation**（若需 REST 创建项目）
- **Project import**（若需 UI 上传本体）

本地部署默认地址：`http://localhost:5000`（Docker 映射 `5000:8080`）。

---

## 3. 场景一：上传业务数据为本体实例

WebProtégé **不直接接收业务 JSON/CSV 行**。第三方系统须先将业务记录 **映射为 OWL 断言（axioms）**，再写入项目。

### 3.1 方式 A：REST API 增量写入（推荐）

**接口：** `POST /data/projects/{projectId}/axioms`

**支持 Content-Type：**

| Content-Type | 格式 |
|---|---|
| `text/ttl` | Turtle（推荐） |
| `application/rdf+xml` | RDF/XML |
| `text/owl-functional` | OWL Functional Syntax |

**可选查询参数：**

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `msg` | 本次变更的提交说明（revision 摘要） | `Added external axioms` |

**请求示例：新增一条故障事件及因果链**

```bash
curl -X POST "http://localhost:5000/data/projects/{projectId}/axioms?msg=新增故障事件" \
  -H "Authorization: ApiKey <your-api-key>" \
  -H "Content-Type: text/ttl" \
  --data-binary @- <<'EOF'
@prefix auto: <http://example.com/automotive/production#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> .

auto:FAULT-20250711-005 a auto:SensorFault ;
    rdfs:label "输送带编码器通信中断"@zh ;
    auto:faultCode "E-CV-SENSOR-015" ;
    auto:severityLevel "Minor" ;
    auto:occurrenceTime "2025-07-11T11:20:00"^^xsd:dateTime ;
    auto:occursOnEquipment auto:CV-004 ;
    auto:triggersException auto:EXC-20250711-005 .

auto:EXC-20250711-005 a auto:ScheduleDelayException ;
    rdfs:label "总装主线物料配送延迟"@zh ;
    auto:exceptionDescription "输送带编码器通信中断导致物料配送延迟"@zh ;
    auto:downtimeMinutes 30 ;
    auto:affectsWorkOrder auto:WO-2025-0705 .
EOF
```

**成功响应：** HTTP 201 Created，body 为 JSON，包含 `addedAxiomsCount`（本次添加的公理数量）。

**删除已有实例断言：**

```
POST /data/projects/{projectId}/delete-axioms
```

请求体格式与 `axioms` 相同（Turtle / RDF/XML / Functional Syntax），提交要删除的公理集合。WebProtégé 以公理增删为变更单元，**没有字段级 PATCH 接口**。更新实例时，通常先删除旧断言再写入新断言。

### 3.2 方式 B：全量/批量导入（适合初始化）

| 方式 | 操作 | 适用场景 |
|------|------|----------|
| UI 上传项目 | 首页 **Upload Project** → 选择 `.ttl` / `.owl` | 首次导入含 TBox + ABox 的完整本体 |
| UI 合并上传 | 项目内 **Project → Upload and append merge** | 向已有项目追加实例，不覆盖已有内容 |
| UI 合并（可能冲突） | **Project → Upload and merge** | 与现有本体合并，冲突需手动解决 |
| REST 创建空项目 | `POST /data/projects` | 程序化创建项目后再分批 POST axioms |

**REST 创建项目示例：**

```bash
curl -X POST "http://localhost:5000/data/projects" \
  -H "Authorization: ApiKey <your-api-key>" \
  -H "Content-Type: application/json" \
  -d '{
    "projectOwner": "your-username",
    "displayName": "汽车设备故障与工单Demo",
    "langTag": "zh",
    "description": "设备故障触发生产异常影响工单执行演示"
  }'
```

响应 HTTP 201，Location Header 指向新项目 URL，body 含 `projectId`（UUID）。

**带本地 import 的本体：** 打包为 zip，顶层本体重命名为 `root-ontology.owl`，其余 import 文件放在同一压缩包内，通过 UI 上传。

### 3.3 方式 C：CSV 批量导入（仅 UI）

适合业务人员操作，**目前无 REST 接口**：

1. 打开项目 → **Project → Import from CSV**
2. 选择导入类型（如 Named Individual）
3. 选择根类（如 `FaultEvent`）
4. 将 CSV 列映射到 OWL 数据/对象属性
5. 确认导入

`demo-data/csv/` 目录提供了设备、故障、工单的 CSV 样例。程序化集成时，建议由 ETL 将 CSV 转为 Turtle 后走方式 A。

### 3.4 业务数据 → OWL 映射参考

以 `demo-data` 汽车故障场景为例：

| 业务概念 | OWL 表达 |
|----------|----------|
| 故障事件 | `NamedIndividual`，类型为 `auto:FaultEvent` 的子类（如 `SensorFault`） |
| 设备编号 | 数据属性断言：`auto:equipmentId "WR-001"` |
| 故障严重等级 | 数据属性：`auto:severityLevel "Critical"` |
| 发生时间 | 数据属性：`auto:occurrenceTime "2025-07-11T08:15:00"^^xsd:dateTime` |
| 故障发生在设备上 | 对象属性：`auto:occursOnEquipment auto:WR-001` |
| 故障触发生产异常 | 对象属性：`auto:triggersException auto:EXC-20250711-001` |
| 异常影响工单 | 对象属性：`auto:affectsWorkOrder auto:WO-2025-0701` |
| 工单状态 | 数据属性：`auto:workOrderStatus "Suspended"` |

**命名空间：** `http://example.com/automotive/production#`（前缀建议 `auto:`）

**日期时间格式：** ISO 8601，`YYYY-MM-DDTHH:mm:ss`（如 `2025-07-11T08:15:00`）

**枚举值参考：**

| 字段 | 允许值 |
|------|--------|
| equipmentStatus | Running, Fault, Maintenance, Idle |
| severityLevel | Critical, Major, Minor |
| workOrderStatus | Planned, InProgress, Suspended, Delayed, Completed |

---

## 4. 场景二：通过接口获取本体实例数据

### 4.1 方式 A：修订历史 API（REST，支持 API Key）

适合 **增量同步**，可获取每次变更的完整公理内容。

| 接口 | 方法 | 返回内容 |
|------|------|----------|
| `/data/projects/{id}/revisions` | GET | 修订列表（作者、时间、摘要） |
| `/data/projects/{id}/revisions/{n}` | GET | 指定修订的完整公理变更（ADD / REMOVE） |

**查询参数（列表接口）：**

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `from` | 起始修订号 | `1` |
| `to` | 结束修订号 | `HEAD` |
| `userId` | 按作者过滤 | 无 |

**请求示例：**

```bash
# 列出项目所有修订
curl -H "Authorization: ApiKey <key>" \
  "http://localhost:5000/data/projects/{projectId}/revisions"

# 获取自 revision 10 以来的变更
curl -H "Authorization: ApiKey <key>" \
  "http://localhost:5000/data/projects/{projectId}/revisions?from=10&to=HEAD"

# 获取某次修订的详细公理变更
curl -H "Authorization: ApiKey <key>" \
  "http://localhost:5000/data/projects/{projectId}/revisions/15"
```

**修订详情 JSON 结构：**

```json
{
  "rev": 15,
  "timestamp": 1720684800000,
  "userId": "editor1",
  "msg": "新增故障事件",
  "subjects": ["http://example.com/automotive/production#FAULT-20250711-005"],
  "changes": [
    {
      "operation": "ADD",
      "axiom": "ClassAssertion(...)"
    }
  ]
}
```

**增量同步模式：** 第三方系统维护本地 RDF 镜像，轮询或订阅 Webhook 后拉取新修订；对 `ADD` 操作合并公理，对 `REMOVE` 操作删除对应公理。

### 4.2 方式 B：全量导出（`/download`，仅 Session 认证）

```
GET /download?project={projectId}&format=ttl&revision=HEAD
```

**查询参数：**

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `project` | 项目 UUID | 必填 |
| `format` | 导出格式 | `owl`（RDF/XML） |
| `revision` | 修订号 | `HEAD`（最新） |

**支持格式：**

| format 值 | 说明 | 扩展名 |
|-----------|------|--------|
| `owl` | RDF/XML | `.owl` |
| `ttl` | Turtle | `.ttl` |
| `owx` | OWL/XML | `.owx` |
| `omn` | Manchester Syntax | `.omn` |
| `ofn` | OWL Functional Syntax | `.ofn` |

**重要限制：** `/download` 使用浏览器 Session 认证，**不支持 API Key**。纯服务端集成可选方案：

1. **中间层定时导出：** 通过已登录 Session Cookie 调用 `/download`，将 `.ttl` 存入数据湖供下游消费
2. **二次开发：** 在 `ProjectDownloadServlet` 中增加 API Key 校验（需修改源码）

### 4.3 方式 C：Webhook 推送（事件驱动）

在项目设置（**Project → Settings → Webhooks**）中配置回调 URL，订阅 `PROJECT_CHANGED` 事件。

本体发生变更时，WebProtégé 向第三方 URL POST JSON：

```json
{
  "projectId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "userId": "editor1",
  "revisionNumber": 15,
  "timestamp": 1720684800000
}
```

收到通知后，第三方调用 `GET /data/projects/{id}/revisions/{revisionNumber}` 拉取本次变更公理。适合 MES、告警系统等需要实时感知的场景。

### 4.4 方式 D：SPARQL 查询（仅 UI，无 REST）

WebProtégé UI 提供 **SPARQL Query** Portlet，可在项目内执行 SPARQL 查询实例关系。

示例（查询故障→异常→工单因果链）：

```sparql
PREFIX auto: <http://example.com/automotive/production#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT ?faultLabel ?equipLabel ?excLabel ?woLabel ?status ?planned ?actual
WHERE {
  ?fault a auto:FaultEvent ;
         rdfs:label ?faultLabel ;
         auto:occursOnEquipment ?equip ;
         auto:triggersException ?exc .
  ?equip rdfs:label ?equipLabel .
  ?exc rdfs:label ?excLabel ;
       auto:affectsWorkOrder ?wo .
  ?wo rdfs:label ?woLabel ;
      auto:workOrderStatus ?status ;
      auto:plannedQuantity ?planned ;
      auto:actualQuantity ?actual .
}
ORDER BY ?faultLabel
```

**目前没有对外 SPARQL Endpoint REST 接口**，第三方无法远程执行 SPARQL。如需 SPARQL 查询能力，建议通过方式 B 导出 TTL 后在外部三元组存储（如 Apache Jena Fuseki、GraphDB）上执行。

---

## 5. REST API 完整参考

基础路径：`http://<host>:<port>/data/`

| 资源路径 | HTTP 方法 | 功能 | 认证 |
|----------|-----------|------|------|
| `/data/projects` | GET | 列出当前用户可访问的项目 | API Key / Session |
| `/data/projects` | POST | 创建新项目 | **必须** API Key |
| `/data/projects/{projectId}` | GET | 获取项目详情 | API Key / Session |
| `/data/projects/{projectId}/axioms` | POST | 添加公理（RDF/XML、Turtle、Functional Syntax） | API Key / Session |
| `/data/projects/{projectId}/delete-axioms` | POST | 删除公理 | API Key / Session |
| `/data/projects/{projectId}/revisions` | GET | 列出修订历史 | API Key / Session |
| `/data/projects/{projectId}/revisions/{n}` | GET | 获取指定修订详情 | API Key / Session |
| `/data/projects/{projectId}/forms` | GET | 列出表单配置 | API Key / Session |
| `/data/projects/{projectId}/forms` | POST | 批量设置表单配置 | API Key / Session |
| `/data/projects/{projectId}/forms/{formId}` | GET | 获取单个表单详情 | API Key / Session |
| `/data/projects/{projectId}/settings` | GET | 读取项目设置 | API Key / Session |
| `/data/projects/{projectId}/settings` | POST | 更新项目设置 | API Key / Session |

**HTTP 状态码：**

| 状态码 | 含义 |
|--------|------|
| 200 | 成功（GET） |
| 201 | 创建成功（POST axioms / 创建项目） |
| 401 | 未认证（缺少或无效 API Key / Session） |
| 403 | 无权限（用户无权编辑该项目） |
| 404 | 资源不存在（如修订号无效） |

---

## 6. 推荐集成模式

以汽车设备故障 Demo 场景为例的端到端集成：

```
┌─────────────┐     故障告警      ┌──────────────┐
│  MES/设备系统 │ ──────────────▶ │  集成中间层   │
└─────────────┘                 │  (ETL/映射)  │
                                └──────┬───────┘
                                       │
                    ┌──────────────────┼──────────────────┐
                    ▼                  ▼                  ▼
            POST /axioms        Webhook 通知      GET /revisions
            (写入实例)          (变更触发)         (拉取变更)
                    │                  │                  │
                    └──────────────────┼──────────────────┘
                                       ▼
                              ┌─────────────────┐
                              │   WebProtégé    │
                              │  汽车故障本体    │
                              │  (TBox + ABox)  │
                              └─────────────────┘
                                       │
                                       ▼
                              RAG / 推理 / 报表系统
                              (消费 TTL 或增量公理)
```

### 6.1 典型流程

| 阶段 | 操作 | 接口/方式 |
|------|------|-----------|
| 1. 初始化 | 上传 `automotive-equipment-fault-demo.ttl` 建立 TBox + 基础 ABox | UI Upload Project 或 REST POST axioms |
| 2. 实时写入 | MES 产生故障事件 → 中间层映射为 Turtle → 写入本体 | `POST /axioms` |
| 3. 变更感知 | 配置 Webhook → 收到 `revisionNumber` → 拉取变更公理 | Webhook + `GET /revisions/{n}` |
| 4. 全量消费 | 定时导出完整本体供 RAG / 知识图谱使用 | `/download?format=ttl`（需 Session） |
| 5. 状态更新 | 工单状态变更 → 删除旧断言 + 写入新断言 | `POST /delete-axioms` + `POST /axioms` |

### 6.2 与 RAG 服务联动

若下游有 RAG 推理服务（如 `industrialAIAgent`），推荐数据流：

1. WebProtégé 作为 **本体权威源（Source of Truth）**，维护结构化 OWL 知识
2. 通过 Webhook + revisions API 或定时 TTL 导出，将本体快照/增量同步到 RAG 向量库
3. RAG 服务基于同步的本体数据进行语义检索与推理，WebProtégé 负责知识的协作编辑与版本管理

---

## 7. 能力边界

| 能力 | 状态 | 说明 |
|------|------|------|
| REST 写入实例（axioms） | ✅ 支持 | 推荐的主通道 |
| REST 删除实例（delete-axioms） | ✅ 支持 | 以公理为粒度删除 |
| REST 读取全量本体 | ❌ 不支持 | 无 `GET /axioms`；需 `/download` 或自建导出 |
| REST 增量读取 | ✅ 支持 | 通过 revisions API |
| REST SPARQL 查询 | ❌ 不支持 | 仅 UI 内 Portlet |
| REST CSV 导入 | ❌ 不支持 | 仅 UI 向导 |
| API Key 认证 | ✅ 支持 | `/data/*` 全接口 |
| 字段级 CRUD | ❌ 不支持 | 以 OWL 公理为变更粒度 |
| Webhook 变更通知 | ✅ 支持 | 项目级配置 |
| 版本回滚 | ✅ 支持 | UI Revisions Portlet（无 REST 回滚接口） |

---

## 8. CLI 管理命令

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

## 9. 常见问题

### Q: POST axioms 返回 403 Forbidden

**原因：** API Key 对应用户对目标项目无编辑权限。

**解决：** 通过 UI Share 为该用户分配 Editor 或更高角色，或使用 `set-permissions` CLI 命令。

### Q: POST axioms 返回 401 Unauthorized

**原因：** API Key 无效、格式错误或已失效。

**解决：** 检查 Header 格式为 `Authorization: ApiKey <key>`（注意 `ApiKey` 与 key 之间有空格）。必要时重新生成。

### Q: 如何通过 API 获取完整本体而非增量？

**现状：** REST API 不提供全量读取接口。`/download` 仅支持 Session 认证。

**方案：**
- 维护本地镜像：初始化时通过 UI 下载一次 TTL，之后通过 revisions API 增量同步
- 二次开发：为 `ProjectDownloadServlet` 增加 API Key 支持
- 中间层：用 Session Cookie 定时调用 `/download`

### Q: 业务系统只有 JSON，如何对接？

在集成中间层实现 **JSON → Turtle 映射器**，将业务字段映射为 OWL 数据/对象属性断言，再通过 `POST /axioms` 写入。参考本文档 3.4 节的映射表。

### Q: 如何确保因果链完整性？

新增实例时，同一 POST 请求中应包含完整的关系断言。例如新增故障事件时，同时断言 `triggersException` 和 `affectsWorkOrder` 关系，避免孤立节点。

---

## 10. 相关源码索引

| 模块 | 关键类 | 说明 |
|------|--------|------|
| `webprotege-server` | `AuthenticationFilter` | API Key / Session 认证 |
| `webprotege-server` | `AxiomsResource` | POST 添加公理 |
| `webprotege-server` | `DeleteAxiomsResource` | POST 删除公理 |
| `webprotege-server` | `RevisionsResource` | GET 修订历史 |
| `webprotege-server` | `ProjectsResource` | GET/POST 项目管理 |
| `webprotege-server` | `ProjectDownloadServlet` | GET 全量下载 |
| `webprotege-server-core` | `PostedAxiomsActionExecutor` | 公理加载与执行 |
| `webprotege-server-core` | `ProjectChangedWebhookPayload` | Webhook 变更载荷 |
| `webprotege-cli` | `GenerateApiKeyCmd` | API Key 生成 |
