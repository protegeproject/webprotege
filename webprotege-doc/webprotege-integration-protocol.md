# WebProtégé Integration 技术协议

本文档描述 `webprotege-integration` 模块向第三方应用开放的 HTTP REST 接口。模块分两族资源：

| 资源族 | 动词 | 用途 |
|---|---|---|
| 个体运行时数据 | GET / PUT / PATCH / DELETE | 读写命名个体的已断言属性值（传感器状态、设备运行数据等） |
| 类 / 属性模型对象 | **仅 GET** | 读 OWL Class / Object·Data·Annotation Property 的帧（父类、domain / range、特征等） |

写操作进入 WebProtégé 正式变更/修订流，而不是旁路存储。类与属性在本协议 **第一期只读**，不提供创建、改层次、改 domain/range。

实现入口：

- 个体资源：`webprotege-integration/.../api/IndividualRuntimeDataResource`
- 个体服务：`webprotege-integration/.../service/IndividualRuntimeDataService`
- 属性映射：`webprotege-integration/.../service/IndividualFramePropertyMapper`
- 类 / 属性资源与服务：见 [第 15 节 实现范围](#15-实现范围与分期)
- Jersey 挂载：`webprotege-server/.../api/ApiModule`（`/data/*`）

文档结构：第 1–10 节为个体运行时数据（已实现）；第 11–14 节为类 / 属性只读协议；第 15 节为实现范围。

---

## 1. 协议总览

| 项 | 约定 |
|---|---|
| 传输 | HTTP/1.1（生产环境建议 HTTPS） |
| 风格 | REST |
| 媒体类型 | `application/json`（请求与响应） |
| Servlet 入口 | Jersey 映射 `/data/*` |
| 个体基路径 | `/data/integration/projects/{projectId}/individuals/runtime-data` |
| 类基路径 | `/data/integration/projects/{projectId}/classes` |
| 属性基路径 | `/data/integration/projects/{projectId}/properties` |
| 鉴权 | `Authorization: ApiKey <key>`，或浏览器 Session |
| 授权 | 读：`VIEW_PROJECT`；写：`EDIT_ONTOLOGY`（仅个体写路径） |
| 数据落点 | 项目内存本体 + BinaryOWL 修订，不是独立 Mongo 表 |

本地 Docker 默认基址：`http://localhost:5000`（`docker-compose.yml` 将宿主机 `5000` 映射到容器 `8080`）。

请求路径：

```
第三方应用
  → HTTP + ApiKey
  → AuthenticationFilter
  → IndividualRuntimeDataResource
  → IndividualRuntimeDataService
  → ActionDispatch（Get/Update NamedIndividualFrame）
  → 本体索引 / 修订日志
```

---

## 2. 鉴权

第三方必须携带 API Key。`AuthenticationFilter` 解析 `Authorization` 头，scheme 不区分大小写：

```http
Authorization: ApiKey <plaintext-key>
```

等价写法：`apikey <key>`。解析逻辑见 `ApiKeyParser`（匹配 `apikey\\s+(.+)`）。

未带 Key 且无登录 Session、Key 无效、或解析为 Guest 时，过滤器直接返回 **401**（空 body）。资源层对 Guest 再拦一次，body 为：

```json
{ "message": "Authentication required" }
```

### 2.1 生成 API Key

容器内 CLI：

```bash
docker exec -it webprotege java -jar /webprotege-cli.jar generate-api-key
```

交互提示：

1. user name → 对目标项目有权限的用户（如 `admin`）
2. purpose → 例如 `integration`

明文 Key 只显示一次，之后按哈希存入 MongoDB。该用户必须对目标项目具备读/写权限。

仓库内也可使用：

```bash
.cursor/skills/generate-apikey/scripts/generate-apikey.sh
```

---

## 3. 资源与动词

`projectId` 必须是 UUID：

```
[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}
```

`individualIri` 是本体个体 IRI（建议 URL 编码）。PUT/PATCH 可放在 query 或 JSON body；两边都有时 **trim 后必须相等**，否则 400。

| 方法 | 路径 | 含义 |
|---|---|---|
| `GET` | `.../runtime-data?individualIri={iri}` | 读单个个体的已断言属性 |
| `GET` | `.../runtime-data` | 列出 `owl:Thing` 下全部命名个体（服务端分页拉全） |
| `PUT` | `.../runtime-data` | **整表替换** 已断言属性（类型 / sameAs 保留） |
| `PATCH` | `.../runtime-data` | **按 key 合并**；已有 key 覆盖，新 key 追加 |
| `DELETE` | `.../runtime-data?individualIri={iri}` | 清空已断言属性（类型 / sameAs 保留） |

完整 URL 模板：

```
{origin}/data/integration/projects/{projectId}/individuals/runtime-data
```

示例：

```
http://localhost:5000/data/integration/projects/00000000-0000-0000-0000-000000000001/individuals/runtime-data
```

---

## 4. JSON 契约

### 4.1 请求（PUT / PATCH）

对应 `IndividualRuntimeDataRequest`。

```json
{
  "individualIri": "http://example.org/Sensor1",
  "properties": {
    "http://example.org/hasStatus": "RUNNING",
    "http://example.org/hasTemp": "23.5"
  }
}
```

| 字段 | 类型 | 说明 |
|---|---|---|
| `individualIri` | string，可选 | 与 query 二选一；都有则 trim 后必须一致 |
| `properties` | `map<IRI, string>` | key = 属性 IRI；`null` 视为空 map |

约束：

- PATCH **必须**带 JSON body，否则 400：`Request body is required`
- PUT 允许空 body：此时使用 query 中的 IRI，`properties` 视为空 map（等价于清空断言属性）
- DELETE **必须**带 query `individualIri`，不接受 body

### 4.2 单条响应（GET 单条 / PUT / PATCH）

对应 `IndividualRuntimeData`。

```json
{
  "projectId": "00000000-0000-0000-0000-000000000001",
  "individualIri": "http://example.org/Sensor1",
  "properties": {
    "http://example.org/hasStatus": "RUNNING",
    "http://example.org/hasTemp": "23.5"
  },
  "updatedAt": 1700000000000,
  "updatedBy": "admin"
}
```

| 字段 | 类型 | 说明 |
|---|---|---|
| `projectId` | string | 项目 UUID |
| `individualIri` | string | 个体 IRI |
| `types` | `string[]` | 已断言类型（命名类 IRI）。第一期兼容增强，见 [11.4](#114-个体响应的-types-字段兼容增强)；写路径不改 type |
| `properties` | `map<IRI, string>` | 已断言属性；派生值不出现 |
| `updatedAt` | number (int64) | 读路径为 `0`；写成功后为服务端毫秒时间戳 |
| `updatedBy` | string / null | 读路径为 `null`；写成功后为 API 用户名 |

读路径没有独立的本体时间戳字段，因此 `updatedAt=0`、`updatedBy=null` 是协议行为，不是错误。

### 4.3 列表响应（GET 无 `individualIri`）

对应 `IndividualRuntimeDataListResponse`。

```json
{
  "items": [
    {
      "projectId": "00000000-0000-0000-0000-000000000001",
      "individualIri": "http://example.org/Sensor1",
      "properties": {
        "http://example.org/hasStatus": "RUNNING"
      },
      "updatedAt": 0,
      "updatedBy": null
    }
  ],
  "count": 1
}
```

`count` 由 `items.size()` 派生，客户端不要单独依赖服务端另存的计数。

列表实现会按页调用 `GetIndividualsAction`（默认每页 100），直到走完 `pageCount`，再对每个命名个体取帧。个体很多时延迟较高，生产侧优先按 IRI 点查。

### 4.4 错误 body

资源层（400 / 401 / 404）：

```json
{ "message": "Query parameter 'individualIri' is required" }
```

权限拒绝走全局 `PermissionDeniedExceptionMapper`（403），形态接近：

```json
{
  "code": 403,
  "reason": "Forbidden",
  "message": "..."
}
```

未知项目走 `UnknownProjectExceptionMapper`，HTTP **404**，可能无 JSON body。

过滤器层 401（无效/缺失 Key）通常为空 body。

---

## 5. 属性映射语义

`properties` 是 **字符串扁平图**，不是 OWL 公理 JSON。映射由 `IndividualFramePropertyMapper` 完成。

| 规则 | 行为 |
|---|---|
| 只暴露 `State.ASSERTED` | 推理/派生值不返回、不写入 |
| 已有属性 | 保留原种类：对象属性仍指向个体 IRI，数据属性仍是字面量，注解属性仍是注解 |
| **新属性** | 默认写成 **数据属性 + `OWLLiteral`** |
| PUT | 请求 map **整体替换** 已断言属性；未出现的 key 被删掉 |
| PATCH | 在现有 map 上 `putAll`；只改给出的 key |
| DELETE | 属性集清空；`rdf:type` / `owl:sameAs` 不动 |
| 对象属性值 | 字符串必须是个体 IRI，例如 `http://example.org/Other` |
| 字面量 | 取 `OWLLiteral.getLiteral()`，不带语言标签/数据类型后缀 |

PUT 传空 `properties` 等于清空断言属性。与 DELETE 的差别：DELETE 在本来就没有断言属性时返回 **404**；PUT 空 map 仍返回 **200** 及空 `properties`。

写操作通过 `UpdateNamedIndividualFrameAction` → `HasApplyChanges` 进入修订日志，并广播 `ProjectEvent`。每次成功写入都会在 History 中留下一条变更。

---

## 6. HTTP 状态码

| 码 | 场景 |
|---|---|
| 200 | GET / PUT / PATCH 成功 |
| 204 | DELETE 成功（无 body） |
| 400 | IRI 缺失/冲突、blank、非法参数；body `{ "message": "..." }` |
| 401 | 无鉴权 / Guest / 无效 Key |
| 403 | 已认证但无 `VIEW_PROJECT`（读）或 `EDIT_ONTOLOGY`（写） |
| 404 | 项目不存在；或 DELETE 时该个体没有可清的断言属性 |

常见 400 文案：

| 条件 | `message` |
|---|---|
| PUT/PATCH 的 query IRI 与 body IRI 不一致 | `Query parameter 'individualIri' must match request body 'individualIri'` |
| PUT/PATCH 两边都未提供 IRI | `individualIri must be provided as a query parameter or in the request body` |
| PATCH 无 body | `Request body is required` |
| DELETE 缺少 `individualIri` | `Query parameter 'individualIri' is required` |
| Guest | `Authentication required` |

---

## 7. 调用示例

以下占位符：

| 变量 | 示例值 |
|---|---|
| `BASE` | `http://localhost:5000` |
| `API_KEY` | CLI 生成的明文 Key |
| `PROJECT` | `00000000-0000-0000-0000-000000000001` |
| `IRI` | `http://example.org/Sensor1` |

### 7.1 curl

**读单个个体**

```bash
curl -sS \
  -H "Authorization: ApiKey ${API_KEY}" \
  -H "Accept: application/json" \
  --get \
  --data-urlencode "individualIri=${IRI}" \
  "${BASE}/data/integration/projects/${PROJECT}/individuals/runtime-data"
```

成功响应示例：

```json
{
  "projectId" : "00000000-0000-0000-0000-000000000001",
  "individualIri" : "http://example.org/Sensor1",
  "properties" : {
    "http://example.org/hasStatus" : "RUNNING"
  },
  "updatedAt" : 0,
  "updatedBy" : null
}
```

**列出项目内全部个体运行时数据**

```bash
curl -sS \
  -H "Authorization: ApiKey ${API_KEY}" \
  "${BASE}/data/integration/projects/${PROJECT}/individuals/runtime-data"
```

**PUT：整表替换（传感器上报全量快照）**

```bash
curl -sS -X PUT \
  -H "Authorization: ApiKey ${API_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "individualIri": "http://example.org/Sensor1",
    "properties": {
      "http://example.org/hasStatus": "RUNNING",
      "http://example.org/hasTemp": "23.5"
    }
  }' \
  "${BASE}/data/integration/projects/${PROJECT}/individuals/runtime-data"
```

也可把 IRI 只放 query，body 只带 `properties`。同时传 query 与 JSON 时，请把 IRI 写在 URL 上，避免 curl 把请求变成 `application/x-www-form-urlencoded`：

```bash
curl -sS -X PUT \
  -H "Authorization: ApiKey ${API_KEY}" \
  -H "Content-Type: application/json" \
  -d '{"properties":{"http://example.org/hasStatus":"IDLE"}}' \
  "${BASE}/data/integration/projects/${PROJECT}/individuals/runtime-data?individualIri=http%3A%2F%2Fexample.org%2FSensor1"
```

**PATCH：只更新状态（SCADA / MES 增量）**

```bash
curl -sS -X PATCH \
  -H "Authorization: ApiKey ${API_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "individualIri": "http://example.org/Sensor1",
    "properties": {
      "http://example.org/hasStatus": "FAULT"
    }
  }' \
  "${BASE}/data/integration/projects/${PROJECT}/individuals/runtime-data?individualIri=http%3A%2F%2Fexample.org%2FSensor1"
```

query 与 body 的 IRI 必须一致。PATCH 后 `hasTemp` 等未出现的 key 会保留。

**DELETE：清空运行时属性**

```bash
curl -sS -X DELETE \
  -H "Authorization: ApiKey ${API_KEY}" \
  --get \
  --data-urlencode "individualIri=${IRI}" \
  "${BASE}/data/integration/projects/${PROJECT}/individuals/runtime-data"
```

成功：HTTP 204，无 body。若该个体没有已断言属性：HTTP 404，

```json
{ "message": "No asserted property values to clear for individual: http://example.org/Sensor1" }
```

### 7.2 Python

```python
import os
import requests

BASE = os.environ.get("WP_BASE", "http://localhost:5000")
KEY = os.environ["WP_API_KEY"]
PROJECT = "00000000-0000-0000-0000-000000000001"
IRI = "http://example.org/Sensor1"
URL = f"{BASE}/data/integration/projects/{PROJECT}/individuals/runtime-data"
HEADERS = {
    "Authorization": f"ApiKey {KEY}",
    "Content-Type": "application/json",
    "Accept": "application/json",
}

# 读单个个体
r = requests.get(URL, headers=HEADERS, params={"individualIri": IRI}, timeout=30)
r.raise_for_status()
print(r.json()["properties"])

# 列出全部
r = requests.get(URL, headers=HEADERS, timeout=60)
r.raise_for_status()
print(r.json()["count"])

# 增量写
r = requests.patch(
    URL,
    headers=HEADERS,
    params={"individualIri": IRI},
    json={
        "individualIri": IRI,
        "properties": {"http://example.org/hasTemp": "24.1"},
    },
    timeout=30,
)
r.raise_for_status()
print(r.json()["updatedBy"], r.json()["updatedAt"])

# 全量替换
r = requests.put(
    URL,
    headers=HEADERS,
    json={
        "individualIri": IRI,
        "properties": {
            "http://example.org/hasStatus": "RUNNING",
            "http://example.org/hasTemp": "24.1",
        },
    },
    timeout=30,
)
r.raise_for_status()

# 清空
r = requests.delete(URL, headers=HEADERS, params={"individualIri": IRI}, timeout=30)
if r.status_code not in (204, 404):
    r.raise_for_status()
```

### 7.3 Java（JAX-RS Client）

```java
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

Client client = ClientBuilder.newClient();
WebTarget target = client.target("http://localhost:5000")
        .path("data/integration/projects/{projectId}/individuals/runtime-data")
        .resolveTemplate("projectId", "00000000-0000-0000-0000-000000000001")
        .queryParam("individualIri", "http://example.org/Sensor1");

Response get = target.request(MediaType.APPLICATION_JSON)
        .header("Authorization", "ApiKey " + apiKey)
        .get();

Map<String, Object> patch = new LinkedHashMap<>();
patch.put("individualIri", "http://example.org/Sensor1");
patch.put("properties", Collections.singletonMap(
        "http://example.org/hasStatus", "RUNNING"));

Response updated = target.request(MediaType.APPLICATION_JSON)
        .header("Authorization", "ApiKey " + apiKey)
        .method("PATCH", Entity.json(patch));
```

---

## 8. 第三方对接约定

1. **先建本体再写数据。** 个体与属性必须已在项目中存在。本 API **不会创建个体**；对未知 IRI 走 `GetNamedIndividualFrame`，不会自动声明 `owl:NamedIndividual`。
2. **属性 IRI 使用全称**，不要用 `hasStatus` 这类短名。
3. **全量快照用 PUT，遥测增量用 PATCH。** PUT 漏 key 会把该断言从本体删除。
4. **新测点第一次写入** 会建成数据属性字面量。若本体中该属性实际是对象属性，应先在 WebProtégé UI 上断言一次正确类型，后续才会按对象属性写入。
5. **写即修订。** 每次 PUT/PATCH/DELETE 都会进入 History 并广播事件。适合秒～分钟级上报，不适合毫秒级采样。
6. **列表 GET 成本高。** 会分页拉全量个体再逐个取帧；生产侧优先按 IRI 点查。
7. **Key 绑定用户。** 该用户必须是项目成员且具备 View / Edit。403 是权限问题，不是 JSON 字段错误。
8. **不要与公理 REST 混用同一批运行时属性。** 同仓库还有 `/data/projects/{id}/axioms` 等 OWL 公理接口；运行时属性请只用 `/data/integration/...`。

---

## 9. 最小联调清单

1. 容器已启动（`docker compose up -d`），`admin` 账户已创建。
2. `generate-api-key` 拿到明文 Key。
3. 在 UI 打开目标项目，从 URL 记下项目 UUID。
4. 创建个体 `http://example.org/Sensor1` 和数据属性 `http://example.org/hasStatus`。
5. `GET` 不带 `individualIri`，确认列表中能看到该个体。
6. `PATCH` 写入 `RUNNING`，再 `GET` 单条核对 `properties`。
7. 在 Individuals 视图确认帧上出现该断言。
8. 用错误 Key 调用，确认 401；用无编辑权限用户调用 PUT，确认 403。

---

## 10. 与现有 REST 的关系

| 接口族 | 基路径 | 用途 |
|---|---|---|
| Integration · 个体 | `/data/integration/projects/{id}/individuals/runtime-data` | 个体运行时属性的 JSON 读写 |
| Integration · 类 | `/data/integration/projects/{id}/classes` | OWL Class 帧只读（第 11 节） |
| Integration · 属性 | `/data/integration/projects/{id}/properties` | OWL Property 帧只读（第 12 节） |
| Projects | `/data/projects` | 创建/定位项目 |
| Axioms | `/data/projects/{id}/axioms` | 以 RDF/XML、Turtle 等格式增删公理（无 GET） |
| Forms / Revisions / Settings | `/data/projects/{id}/...` | 表单、修订、项目设置 |

Integration 模块通过 `ActionDispatch` 复用与 GWT 客户端相同的 Frame Action，因此 UI 与第三方 API 看到的是同一份本体帧。`/data/projects/{id}/axioms` **不能**替代类 / 属性读取：它只支持 POST 增公理与 delete-axioms，没有按实体帧查询。

---

## 11. 类（OWL Class）只读接口

第一期只读。路径风格与个体接口对称：query 传 IRI，不把 IRI 放进 path。

完整 URL 模板：

```
{origin}/data/integration/projects/{projectId}/classes
```

### 11.1 动词

| 方法 | 路径 | 含义 |
|---|---|---|
| `GET` | `.../classes?classIri={iri}` | 读单个类的帧（父类 + 已断言属性/注解） |
| `GET` | `.../classes` | 列出项目签名中的全部 OWL Class（服务端分页拉全后再逐个取帧） |

鉴权与个体读路径相同：`VIEW_PROJECT`；Guest / 无效 Key → 401；无权限 → 403；未知项目 → 404。

未知 `classIri` **不返回 404**。与 `GetClassFrameAction` 一致：返回空帧（`parents` / `properties` 为空数组/空 map），HTTP 200。这与个体点查行为对齐。

`classIri` 建议 URL 编码。空白 IRI 视为未提供，走列表。

### 11.2 单条响应

对应 `OntologyClassData`。

```json
{
  "projectId": "00000000-0000-0000-0000-000000000001",
  "classIri": "http://example.org/Sensor",
  "parents": [
    "http://www.w3.org/2002/07/owl#Thing"
  ],
  "properties": {
    "http://www.w3.org/2000/01/rdf-schema#label": "Sensor",
    "http://example.org/hasStatus": "http://example.org/StatusValue"
  }
}
```

| 字段 | 类型 | 说明 |
|---|---|---|
| `projectId` | string | 项目 UUID |
| `classIri` | string | 类 IRI |
| `parents` | `string[]` | 直接父类 IRI，来自 `PlainClassFrame.getParents()`（`rdfs:subClassOf` 命名类）。不含匿名类表达式 |
| `properties` | `map<IRI, string>` | 已断言属性值与注解；`State.DERIVED` 不出现 |

映射规则与个体 `properties` 相同，复用 `IndividualFramePropertyMapper` 的 visitor（或抽成共享 mapper）：

- 只暴露 `State.ASSERTED`
- 对象属性值 / 类值 → 目标 IRI 字符串
- 字面量 → `OWLLiteral.getLiteral()`，不带语言标签 / 数据类型后缀
- 同一属性多个值时 **后写覆盖**（扁平 map 限制，与个体接口一致）
- 不区分注解属性与逻辑限制；`rdfs:label` 等会出现在同一 map 里

不含 `updatedAt` / `updatedBy`：类不是运行时遥测，读路径没有独立修订戳。

### 11.3 列表响应

对应 `OntologyClassListResponse`。

```json
{
  "items": [ { "projectId": "...", "classIri": "...", "parents": [], "properties": {} } ],
  "count": 1
}
```

`count` 由 `items.size()` 派生。

列表实现：`GetMatchingEntitiesAction` + `EntityTypeIsOneOfCriteria(CLASS)`，默认每页 100，直到走完 `pageCount`；再对每个类调用 `GetClassFrameAction`。内置类（如 `owl:Thing`）若在项目签名中会出现在列表里。类很多时延迟较高，生产侧优先按 IRI 点查。

### 11.4 个体响应的 `types` 字段（兼容增强）

当前个体 GET **不返回** `rdf:type`。`PlainNamedIndividualFrame.getParents()` 在服务端已经读到，只是 `toRuntimeData()` 丢掉了。

第一期在 `IndividualRuntimeData` 上 **追加** 可选字段，已有客户端忽略未知 JSON 字段即可：

```json
{
  "projectId": "...",
  "individualIri": "http://example.org/Sensor1",
  "types": ["http://example.org/Sensor"],
  "properties": { "http://example.org/hasStatus": "RUNNING" },
  "updatedAt": 0,
  "updatedBy": null
}
```

| 字段 | 类型 | 说明 |
|---|---|---|
| `types` | `string[]` | 个体的已断言类型（命名类 IRI）。写路径仍不改 type / sameAs |

不在本期暴露 `sameAs`。

---

## 12. 属性（OWL Property）只读接口

第一期只读。三种 OWL 属性共用一个资源，用 `kind` 区分，避免三套 URL。JSON 形状统一，用不到的字段给空数组。

完整 URL 模板：

```
{origin}/data/integration/projects/{projectId}/properties
```

### 12.1 动词

| 方法 | 路径 | 含义 |
|---|---|---|
| `GET` | `.../properties?propertyIri={iri}&kind={kind}` | 读单个属性帧 |
| `GET` | `.../properties?kind={kind}` | 列出该种类的全部属性 |
| `GET` | `.../properties` | 列出全部三种属性 |

`kind` 取值（大小写不敏感）：

| `kind` | OWL 实体 | Frame Action |
|---|---|---|
| `OBJECT` | `OWLObjectProperty` | `GetObjectPropertyFrameAction` |
| `DATA` | `OWLDataProperty` | `GetDataPropertyFrameAction` |
| `ANNOTATION` | `OWLAnnotationProperty` | `GetAnnotationPropertyFrameAction` |

约束：

- 点查 **必须**带 `kind`。同一 IRI 在 OWL 2 下可能 punning，服务端不能从空帧判断种类。缺 `kind` 且带了 `propertyIri` → 400：`Query parameter 'kind' is required when 'propertyIri' is provided`
- `kind` 非法 → 400：`Query parameter 'kind' must be OBJECT, DATA, or ANNOTATION`
- 未知 `propertyIri` 仍返回 200 空帧（domain / range / characteristics / inverses / annotations 皆空），与类 / 个体点查一致

鉴权同第 11 节。

### 12.2 单条响应

对应 `OntologyPropertyData`。

```json
{
  "projectId": "00000000-0000-0000-0000-000000000001",
  "propertyIri": "http://example.org/hasStatus",
  "kind": "OBJECT",
  "domains": ["http://example.org/Sensor"],
  "ranges": ["http://example.org/StatusValue"],
  "characteristics": ["Functional"],
  "inverses": ["http://example.org/isStatusOf"],
  "annotations": {
    "http://www.w3.org/2000/01/rdf-schema#label": "has status"
  }
}
```

| 字段 | 类型 | OBJECT | DATA | ANNOTATION |
|---|---|---|---|---|
| `propertyIri` | string | 属性 IRI | 同左 | 同左 |
| `kind` | string | `OBJECT` | `DATA` | `ANNOTATION` |
| `domains` | `string[]` | 类 IRI | 类 IRI | 任意 IRI |
| `ranges` | `string[]` | 类 IRI | 数据类型 IRI（如 `xsd:string`） | 任意 IRI |
| `characteristics` | `string[]` | 见下表 | 若 `functional` 则为 `["Functional"]`，否则 `[]` | 恒为 `[]` |
| `inverses` | `string[]` | 逆属性 IRI | 恒为 `[]` | 恒为 `[]` |
| `annotations` | `map<IRI, string>` | 属性上的注解 | 同左 | 同左 |

对象属性 `characteristics` 取值与 `ObjectPropertyCharacteristic` 的 `@JsonProperty` 对齐：

`Functional` · `InverseFunctional` · `Transitive` · `Symmetric` · `Asymmetric` · `Reflexive` · `Irreflexive`

第一期 **不返回** 属性层次（super-property）。`ObjectPropertyFrame` 等帧对象本身不含 parents；层次在 `GetHierarchyChildrenAction` 上，放到第二期。

### 12.3 列表响应

对应 `OntologyPropertyListResponse`。

```json
{
  "items": [ { "projectId": "...", "propertyIri": "...", "kind": "DATA", "domains": [], "ranges": [], "characteristics": [], "inverses": [], "annotations": {} } ],
  "count": 1
}
```

列表实现：`GetMatchingEntitiesAction` + `EntityTypeIsOneOfCriteria`（按 `kind` 过滤，缺省则三种都查），分页拉全后再逐个取对应 Frame。内置属性（`owl:topObjectProperty`、`rdfs:label` 等）若在签名中会出现。属性很多时优先点查。

---

## 13. HTTP 状态码（类 / 属性）

沿用第 6 节，仅 GET：

| 码 | 场景 |
|---|---|
| 200 | 点查或列表成功（未知 IRI 也是 200 空帧） |
| 400 | `kind` 缺失/非法；body `{ "message": "..." }` |
| 401 | 无鉴权 / Guest / 无效 Key |
| 403 | 已认证但无 `VIEW_PROJECT` |
| 404 | 项目不存在 |

类 / 属性第一期无 PUT / PATCH / DELETE，对这些路径返回容器默认 405。

---

## 14. 类 / 属性调用示例

占位符同第 7 节。

**读单个类**

```bash
curl -sS \
  -H "Authorization: ApiKey ${API_KEY}" \
  -H "Accept: application/json" \
  --get \
  --data-urlencode "classIri=http://example.org/Sensor" \
  "${BASE}/data/integration/projects/${PROJECT}/classes"
```

**列出全部类**

```bash
curl -sS \
  -H "Authorization: ApiKey ${API_KEY}" \
  "${BASE}/data/integration/projects/${PROJECT}/classes"
```

**读单个对象属性**

```bash
curl -sS \
  -H "Authorization: ApiKey ${API_KEY}" \
  -H "Accept: application/json" \
  --get \
  --data-urlencode "propertyIri=http://example.org/hasStatus" \
  --data-urlencode "kind=OBJECT" \
  "${BASE}/data/integration/projects/${PROJECT}/properties"
```

**列出数据属性**

```bash
curl -sS \
  -H "Authorization: ApiKey ${API_KEY}" \
  --get \
  --data-urlencode "kind=DATA" \
  "${BASE}/data/integration/projects/${PROJECT}/properties"
```

Python：

```python
import os
import requests

BASE = os.environ.get("WP_BASE", "http://localhost:5000")
KEY = os.environ["WP_API_KEY"]
PROJECT = "00000000-0000-0000-0000-000000000001"
HEADERS = {"Authorization": f"ApiKey {KEY}", "Accept": "application/json"}

classes_url = f"{BASE}/data/integration/projects/{PROJECT}/classes"
props_url = f"{BASE}/data/integration/projects/{PROJECT}/properties"

r = requests.get(classes_url, headers=HEADERS, params={"classIri": "http://example.org/Sensor"}, timeout=30)
r.raise_for_status()
print(r.json()["parents"], r.json()["properties"])

r = requests.get(props_url, headers=HEADERS, params={"kind": "DATA"}, timeout=60)
r.raise_for_status()
print(r.json()["count"])
```

第三方对接补充（相对第 8 节）：

1. **先读 schema 再写个体。** 用 `/classes`、`/properties` 发现类型与测点 IRI，再用 `/individuals/runtime-data` 写值。
2. **属性 IRI 仍用全称。** `kind` 告诉服务端走哪一种 Frame Action，不能省略点查时的 `kind`。
3. **列表成本高。** 与个体列表相同：匹配引擎分页 + 逐个取帧。
4. **不要用公理 REST 拼 schema。** `/data/projects/{id}/axioms` 无 GET。
5. **空帧不是“不存在”。** 点查未知 IRI 为 200。若需要确认实体是否在签名中，看列表或 UI。

最小联调（接第 9 节）：

1. UI 中确认存在类 `http://example.org/Sensor` 与数据属性 `http://example.org/hasStatus`。
2. `GET .../classes?classIri=...` 看到 `parents` / `properties`。
3. `GET .../properties?propertyIri=...&kind=DATA` 看到 `domains` / `ranges`。
4. `GET` 个体时 `types` 含该 Sensor 类。
5. 错误 Key → 401；无 View 权限 → 403。

---

## 15. 实现范围与分期

### 15.1 第一期（本协议，建议一次 PR）

只读 + 个体 `types` 兼容字段。不改个体写语义。

| 项 | 做法 |
|---|---|
| 类点查 / 列表 | `GetClassFrameAction` + `GetMatchingEntitiesAction(EntityType.CLASS)` |
| 属性点查 / 列表 | `GetObject/Data/AnnotationPropertyFrameAction` + `GetMatchingEntitiesAction` 按 `kind` 过滤 |
| 个体 `types` | `IndividualRuntimeData` 增加 `types`；`toRuntimeData()` 写入 `frame.getParents()` 的 IRI |
| 属性值扁平化 | 类帧的 `propertyValues` 复用现有 mapper visitor；可抽 `FramePropertyMapper` 供个体与类共用 |
| 鉴权 / 错误 body | 复制 `IndividualRuntimeDataResource` 的 Guest 检查与 `{ "message" }` |
| Jersey | `ApiModule.provideResourceConfig` 再 `register` 两个资源 |

建议新增文件（均在 `webprotege-integration`，测试对称现有 `*_TestCase`）：

```
api/OntologyClassResource.java
api/OntologyPropertyResource.java
service/OntologyClassService.java
service/OntologyPropertyService.java
dto/OntologyClassData.java
dto/OntologyClassListResponse.java
dto/OntologyPropertyData.java
dto/OntologyPropertyListResponse.java
```

改动的已有文件：

- `IndividualRuntimeData.java` / `IndividualRuntimeDataService.toRuntimeData()` — 增加 `types`
- `IndividualFramePropertyMapper.java` — 可选：抽出对 `ImmutableSet<PlainPropertyValue>` 的重载
- `ApiModule.java` — 注册新资源
- `webprotege-integration/pom.xml` `<description>` — 范围从“个体运行时”扩到 schema 只读
- 本协议文档

测试最低集：

- Resource：Guest → 401；缺 `kind` 点查属性 → 400；合法 GET 把 query 交给 service
- Service：class 点查走 `GetClassFrameAction`；列表走 `GetMatchingEntitiesAction` 并翻页；property 按 `kind` 选对 Frame Action
- Mapper / DTO：Jackson 往返；`types` 序列化；OBJECT 的 `characteristics` / `inverses` 不被 DATA 路径填错
- 个体回归：`types` 出现后 PUT/PATCH 仍不改 parents / sameAs

### 15.2 明确不做（第一期）

| 不做 | 原因 |
|---|---|
| 创建 / 修改 / 删除类或属性 | 第三方应在 UI 建 TBox；写路径会进修订历史，schema 变更应人工审 |
| 属性层次（super-property） | 帧对象无 parents，需另接 `GetHierarchyChildrenAction` |
| `GET .../classes/children` 树接口 | 可用列表 + `parents` 在客户端拼树；树接口留第二期 |
| 个体 `sameAs` | 运行时集成用不到 |
| 匿名类表达式、Manchester 语法 | 扁平 JSON 表达不了；需要时用公理 REST 写、UI 读 |
| 分页 query（`page` / `pageSize`） | 与现有个体列表一致，服务端拉全后一次返回 |
| 依赖 `webprotege-server-core` 的 Index | integration 只依赖 `webprotege-shared` + `ActionDispatch` |

### 15.3 第二期（有明确需求再做）

- `GET .../classes/children?classIri=` / `GET .../properties/children?kind=`（`GetHierarchyRoots` + `GetHierarchyChildren`）
- 列表 `summary=true`：只返回 IRI + label，不取帧
- 对外分页
- 类 / 属性写接口（若产品确认 TBox 也可由第三方改）

### 15.4 建议实现顺序

1. 个体 `types`（改动面最小，立刻补上“个体属于哪个类”）
2. `OntologyClassResource` + Service + DTO + 测试
3. `OntologyPropertyResource` + Service + DTO + 测试
4. `ApiModule` 注册
5. 对照本协议补 curl / 联调清单
