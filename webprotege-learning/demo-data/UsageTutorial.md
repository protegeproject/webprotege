# Demo 实例数据使用教程

本教程说明如何将 `demo-data/` 中的汽车行业设备故障数据导入 WebProtégé，并完成浏览、查询、演示与扩展。

> **前置文档**
> - [README.md](README.md) — 数据项清单与文件说明
> - [UserManual.md](../UserManual.md) — WebProtégé 完整用户手册

---

## 0. 环境准备

### 0.1 启动 WebProtégé

```bash
cd /home/guofengxu/00.workspace/webprotege
docker compose up -d
```

浏览器访问：**http://localhost:5000**

### 0.2 登录与系统设置

| 字段 | 本地默认值 |
|------|------------|
| 用户名 | `admin` |
| 密码 | `admin123` |

首次部署还需：

1. 创建管理员（若尚未创建）：
   ```bash
   docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
   ```
2. 进入 **Settings**（http://localhost:5000/#application/settings）
3. 填写 **System notification email** 与 **Application host URL**（`http://localhost:5000`）
4. 开启 **Project import**（允许上传项目）

---

## 1. 导入实例数据（第一步，必做）

Demo 的**全部实例数据**已内嵌在主本体文件中，导入一次即可使用。

### 1.1 上传项目

1. 登录后进入 **Home**
2. 点击 **Upload Project...**
3. 填写：
   - **Project name：** `汽车设备故障与工单Demo`
   - **Description：** `设备故障触发生产异常影响工单执行`
4. 选择文件：
   ```
   demo-data/automotive-equipment-fault-demo.ttl
   ```
5. 点击确认上传，等待导入完成
6. 在项目列表中点击项目名进入编辑界面

### 1.2 验证导入成功

导入后应能看到以下内容：

| 检查项 | 操作 | 预期结果 |
|--------|------|----------|
| 类层次 | 左侧 **Class hierarchy** 展开 `Equipment` | 可见 `WeldingRobot`、`CNCMachine` 等子类 |
| 故障类 | 展开 `FaultEvent` | 可见 `SensorFault`、`MechanicalFault` 等 |
| 个体数量 | **Add Content** → 添加 **Individuals** Portlet → 选择 `FaultEvent` | 列出 4 条故障实例 |
| 搜索 | 顶部搜索框输入 `WR-001` | 找到焊接机器人#1 |

> **说明：** `csv/` 目录下的 CSV 文件是**可选扩展数据**，不是导入 Demo 的必需步骤（见第 7 节）。

---


> **关于关系图功能：** WebProtégé 界面中**没有名为 "Visualization" 的面板**。关系可视化功能的正式名称是 **Entity Graph**（实体关系图），可通过下文两种方式打开。

### 打开 Entity Graph 的两种方式

**方式 1（推荐，无需添加面板）：编辑器内标签页**

1. 切换到 **Individuals** 视角（顶部 Tab 栏）
2. 在左侧 **Individuals by Class** 中选择一个个体（如 `FAULT-20250711-001`）
3. 右侧 **Individual** 编辑器顶部有 3 个图标标签页：
   - 第 1 个：**Details**（详情，默认）
   - 第 2 个：**Entity Graph**（关系图图标，鼠标悬停显示提示）
   - 第 3 个：**Changes**（变更历史）
4. 点击中间的 **Entity Graph** 图标，即可看到该个体的关系网络图

**方式 2：添加独立面板**

1. 点击 **Add Content**（或 Perspective 菜单中的添加视图）
2. 在面板列表中选择 **Entity Graph**（不是 Visualization）
3. 选中任意实体后，图中显示其关联关系

## 2. 配置演示工作区（推荐）

为便于演示「故障 → 异常 → 工单」链路，建议新建专用 Perspective。

1. 进入项目后，点击 Tab 栏旁的 **Perspective** 菜单
2. 选择 **Create perspective**，命名为 `故障工单演示`
3. 点击 **Add Content**，依次添加以下 Portlet：

| Portlet 名称 | 用途 |
|--------------|------|
| **Class hierarchy** | 浏览类层次，选择实体 |
| **Search** | 按名称快速定位实例 |
| **Individual editor** | 查看/编辑个体属性断言 |
| **OWL Entity Description Browser** | 以 Manchester 语法查看完整描述 |
| **Query** | 按条件查询实体 |
| **Entity Graph** | 图形化展示实体关系 |
| **Discussion** | 添加评论，演示协作 |

4. 拖拽 Portlet 标题栏，建议布局：

```
┌─────────────────┬──────────────────────────────┐
│ Class hierarchy │  Individual editor            │
│ Search          │  OWL Entity Description Browser│
├─────────────────┼──────────────────────────────┤
│ Query           │  Entity Graph               │
└─────────────────┴──────────────────────────────┘
```

布局会自动保存，下次打开项目时恢复。

---

## 3. 浏览实例数据

### 3.1 按类查看个体列表

1. 添加 **Individuals** Portlet（若尚未添加）
2. 在 Portlet 顶部的类选择器中选择目标类
3. 查看该类下的所有命名个体

**推荐浏览顺序：**

| 选择类 | 可见实例 | 说明 |
|--------|----------|------|
| `Equipment` | WR-001, WR-002, CNC-003 ... | 6 台设备 |
| `FaultEvent` | FAULT-20250711-001 ~ 004 | 4 条故障 |
| `ProductionException` | EXC-20250711-001 ~ 004 | 4 条异常 |
| `ProductionWorkOrder` | WO-2025-0701 ~ 0704 | 4 条生产工单 |
| `MaintenanceWorkOrder` | WO-M-001 | 维护工单 |

### 3.2 用搜索快速定位

在 **Search** Portlet 或顶部搜索框中输入：

| 搜索词 | 定位到 |
|--------|--------|
| `WR-001` | 焊接机器人#1 |
| `FAULT-20250711-001` | 传感器故障事件 |
| `WO-2025-0701` | 白车身组装工单 |
| `停线` | 焊装A线停线异常 |
| `Suspended` | 含该状态断言的工单 |

点击搜索结果中的实体，**Individual editor** 会同步显示其详情。

### 3.3 查看个体属性（Individual editor）

选中任意个体后，在 **Individual editor** 中可看到：

**示例：选中 `FAULT-20250711-001`（传感器故障）**

| 区域 | 内容 |
|------|------|
| Types（类型） | `SensorFault` |
| Object property assertions | `occursOnEquipment` → `WR-001`；`triggersException` → `EXC-20250711-001` |
| Data property assertions | `faultCode` = E-WR-SENSOR-042；`severityLevel` = Critical；`occurrenceTime` = 2025-07-11T08:15:00 |
| Annotations | `rdfs:label` = 焊接机器人位置传感器信号丢失 |

**示例：选中 `WO-2025-0701`（受影响工单）**

| 属性 | 值 |
|------|-----|
| workOrderStatus | Suspended |
| plannedQuantity | 48 |
| actualQuantity | 12 |
| assignedToEquipment | WR-001 |
| producesProduct | 白车身总成 |
| forVehicleModel | Model-X 纯电SUV |

---

## 4. 追踪因果链（核心演示）

Demo 的核心价值是展示：**设备故障如何一路影响到工单执行**。

### 4.1 链路 1：停线 → 工单暂停（最严重）

按以下顺序逐个点选实体，观察 **Individual editor** 中的关系：

```
步骤 1  FAULT-20250711-001  焊接机器人位置传感器信号丢失
         │ occursOnEquipment
         ▼
步骤 2  WR-001              焊接机器人#1（equipmentStatus = Fault）
         ▲ assignedToEquipment
步骤 3  EXC-20250711-001      焊装A线停线（downtimeMinutes = 95）
         │ affectsWorkOrder
         ▼
步骤 4  WO-2025-0701          焊装A线-白车身组装（status = Suspended，产量 12/48）
         ▲ handledByMaintenanceOrder
步骤 5  WO-M-001              WR-001传感器更换维护（维护工单）
```

**演示话术参考：**
> 早班 08:15，焊装 A 线焊接机器人 WR-001 发生传感器故障，触发停线异常，导致白车身组装工单暂停，设备部门已创建维护工单 WO-M-001 进行抢修。

### 4.2 链路 2：质量偏差 → 工单延迟

```
FAULT-20250711-002 → CNC-003 → EXC-20250711-002 → WO-2025-0702
（机械故障）          （数控机床）  （尺寸超差）       （Delayed，38/60）
```

### 4.3 链路 3：节拍延迟

```
FAULT-20250711-003 → WR-002 → EXC-20250711-003 → WO-2025-0703
（电气故障）          （焊接机器人#2） （节拍延迟）    （Delayed，22/40）
```

### 4.4 链路 4：产能不足 → 减产进行

```
FAULT-20250711-004 → PR-005 → EXC-20250711-004 → WO-2025-0704
（液压故障）          （冲压压机）  （产能不足）       （InProgress，95/200）
```

### 4.5 用关系图可视化

1. 在 **Entity Graph** Portlet 中，选中 `FAULT-20250711-001`
2. 图形将展示该实体的一阶邻域关系
3. 在可视化设置中勾选以下关系类型以获得完整链路：
   - `occursOnEquipment`
   - `triggersException`
   - `affectsWorkOrder`
   - `handledByMaintenanceOrder`
   - `assignedToEquipment`

---

## 5. 查询实例数据

WebProtégé 提供 **Query** Portlet（界面中可能显示为 "Query" 或 "DL Query"），可按类型和属性条件筛选实体。

### 5.1 查询所有故障事件

1. 添加 **Query** Portlet
2. 添加条件：**Type** = `FaultEvent`（或 `SensorFault` 等子类）
3. 点击 **Execute**
4. 结果列表应返回 4 条故障实例

### 5.2 查询状态为 Suspended 的工单

1. 在 Query Portlet 中添加条件：
   - **Type** = `ProductionWorkOrder`
   - **Data property value** = `workOrderStatus` 等于 `Suspended`
2. 执行查询
3. 预期结果：`WO-2025-0701`

### 5.3 查询严重等级为 Critical 的故障

1. 条件：
   - **Type** = `FaultEvent`
   - **Data property value** = `severityLevel` 等于 `Critical`
2. 预期结果：`FAULT-20250711-001`、`FAULT-20250711-004`

### 5.4 查询特定设备上的所有断言

1. 选中 `WR-001`
2. 打开 **Usage** Portlet
3. 查看所有引用 `WR-001` 的公理，包括：
   - 故障事件的 `occursOnEquipment`
   - 工单的 `assignedToEquipment`
   - 维护工单的 `assignedToEquipment`

---

## 6. 模拟业务场景（编辑演示）

以下操作演示如何在系统中**响应新故障**，需 **Editor** 权限。

### 场景 A：故障恢复，工单恢复执行

模拟 WR-001 传感器更换完成：

1. 搜索并选中 `WR-001`
2. 在 **Individual editor** 中，将 `equipmentStatus` 从 `Fault` 改为 `Running`
3. 选中 `FAULT-20250711-001`，添加 `resolutionTime` = `2025-07-11T10:00:00`
4. 选中 `WO-2025-0701`，将 `workOrderStatus` 从 `Suspended` 改为 `InProgress`
5. 填写变更说明（commit message），如：`WR-001维护完成，恢复白车身工单`
6. 在 **Revisions** Portlet 中确认产生了新修订

### 场景 B：新增一条故障链

模拟输送带故障（CSV 中预留的扩展数据）：

1. 在 **Individuals** Portlet 中，选择 `SensorFault` 类 → **Create individual**
2. 命名为 `FAULT-20250711-005`，设置：
   - `faultCode` = E-CV-SENSOR-015
   - `severityLevel` = Minor
   - `occurrenceTime` = 2025-07-11T11:20:00
   - `occursOnEquipment` = CV-004
3. 创建 `ScheduleDelayException` 个体 `EXC-20250711-005`
4. 添加断言：
   - `FAULT-20250711-005` → `triggersException` → `EXC-20250711-005`
   - `EXC-20250711-005` → `affectsWorkOrder` → （新建或已有工单）
5. 保存并在 **Entity Graph** 中验证新链路

### 场景 C：协作处理

1. 选中 `WO-2025-0701`
2. 在 **Discussion** Portlet 中添加评论：
   > 设备部反馈：传感器模块已更换，预计 10:00 恢复生产，请计划部调整后续排程。
3. 点击 **Watch** 关注该工单
4. 在 **Project Feed** 中查看活动记录

---

## 7. 使用 CSV 扩展数据（可选）

`csv/` 目录提供结构化表格，适合批量追加新实例。**须先完成第 1 节的本体导入。**

### 7.1 导入步骤

1. 打开已导入的 Demo 项目
2. **Project** → **Import from CSV**
3. 选择文件（如 `demo-data/csv/fault-events.csv`）
4. 配置导入选项：
   - **Import type：** Named Individual
   - **Root class：** `FaultEvent`
   - **Display name column：** `故障事件名称`
5. 映射列与属性：

| CSV 列 | 映射到属性 | 列类型 |
|--------|------------|--------|
| 故障代码 | `faultCode` | Text |
| 严重等级 | `severityLevel` | Text |
| 发生时间 | `occurrenceTime` | Text |
| 设备名称 | `occursOnEquipment` | Named Individual |
| 异常名称 | （暂不映射，需手动补关系） | — |

6. 确认导入

> **注意：** CSV 导入会创建个体和属性断言，但**不会自动建立** `triggersException`、`affectsWorkOrder` 等因果链关系，需在 **Individual editor** 中手动补充，或在本体文件中预先定义后再合并导入。

### 7.2 CSV 文件用途

| 文件 | 记录数 | 用途 |
|------|--------|------|
| `equipment.csv` | 6 | 设备主数据（与本体重复，可用于练习 CSV 导入） |
| `fault-events.csv` | 5 | 含 1 条额外故障（输送带编码器通信中断） |
| `work-orders.csv` | 5 | 工单数据（与本体重复，可用于练习） |

---

## 8. 15 分钟演示脚本

适合向业务或技术团队做 Demo 汇报：

| 时间 | 环节 | 操作 |
|------|------|------|
| 0-2 min | 背景介绍 | 说明场景：设备故障 → 生产异常 → 工单影响 |
| 2-4 min | 打开项目 | 展示 Class hierarchy 中的设备/故障/工单类体系 |
| 4-7 min | 因果链演示 | 按 4.1 节逐步点选 FAULT-001 → WR-001 → EXC-001 → WO-0701 |
| 7-9 min | 可视化 | 在 Entity Graph 中展示关系图 |
| 9-11 min | 查询 | 用 Query 查出所有 Critical 故障和 Suspended 工单 |
| 11-13 min | 编辑 | 模拟故障恢复，修改工单状态，展示 Revision 历史 |
| 13-15 min | 协作 | 在 Discussion 中添加跨部门评论，展示 Watch 机制 |

---

## 9. 常见问题

### Q1：上传后看不到中文标签？

**原因：** 词典语言未配置。

**解决：** **Project** → **Settings** → **Dictionary languages** → 添加 `zh`（中文）→ 保存。

### Q2：搜索不到实例？

**原因：** 项目索引尚未建立完成。

**解决：** 等待数秒后刷新页面；或直接在 **Individuals** Portlet 中按类浏览。

### Q3：CSV 导入后没有关系链？

**原因：** CSV 导入仅创建个体级断言（类型 + 属性值），对象属性关系需手动补充。

**解决：** 在 Individual editor 中为新建个体添加 `triggersException`、`affectsWorkOrder` 等对象属性断言。

### Q4：如何导出当前数据？

**操作：** 回到 **Home** → 找到项目 → 点击 **Download** 图标 → 选择 Turtle 或 RDF/XML 格式保存。

### Q5：如何重置为初始 Demo 数据？

**操作：** 删除当前项目 → 重新执行第 1 节上传 `automotive-equipment-fault-demo.ttl`。

---

## 10. 下一步

| 目标 | 建议操作 |
|------|----------|
| 了解全部数据字段 | 阅读 [README.md](README.md) 第一、二节 |
| 学习 WebProtégé 全部功能 | 阅读 [UserManual.md](../UserManual.md) |
| 对接外部系统 | 参考 UserManual 第 15 节 REST API |
| 定制编辑表单 | 为 `FaultEvent`、`WorkOrder` 创建 Forms（UserManual 第 8 节） |
