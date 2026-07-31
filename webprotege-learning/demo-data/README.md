# 汽车行业设备故障触发生产异常 Demo 数据

本目录提供 **设备故障 → 生产异常 → 工单执行影响** 演示场景所需的 OWL 本体与示例实例数据，可直接导入 WebProtégé。


> **快速上手：** 操作步骤请参阅 [UsageTutorial.md](UsageTutorial.md)
## 场景概述

某汽车整车厂 2025-07-11 早班生产过程中，多台关键设备发生故障，触发不同级别的生产异常，进而导致关联生产工单暂停、延迟或减产。数据覆盖四条完整因果链：

```
设备故障 ──触发──▶ 生产异常 ──影响──▶ 生产工单
                └──关联──▶ 维护工单（部分场景）
```

### Demo 因果链

| 故障事件 | 设备 | 生产异常 | 受影响工单 | 工单状态 |
|----------|------|----------|------------|----------|
| 焊接机器人位置传感器信号丢失 | WR-001 | 焊装A线停线 | WO-2025-0701 白车身组装 | Suspended |
| 数控机床主轴轴承过热 | CNC-003 | 副车架机加工尺寸超差 | WO-2025-0702 副车架 | Delayed |
| 焊接机器人伺服驱动器过流 | WR-002 | 焊装B线节拍延迟 | WO-2025-0703 车门总成 | Delayed |
| 冲压压机液压压力不足 | PR-005 | 冲压产能不足 | WO-2025-0704 前盖外板 | InProgress |

---

## 一、数据项清单

### 1.1 类（TBox — 概念模型）

| 分类 | 类名 | 中文标签 | 说明 |
|------|------|----------|------|
| 组织 | `Workshop` | 车间 | 冲压/焊装/总装等物理区域 |
| 组织 | `ProductionLine` | 产线 | 车间内的生产线 |
| 设备 | `Equipment` | 设备 | 顶层设备类 |
| 设备 | `ProductionEquipment` | 生产设备 | 焊接机器人、数控机床等 |
| 设备 | `WeldingRobot` | 焊接机器人 | |
| 设备 | `CNCMachine` | 数控机床 | |
| 设备 | `ConveyorSystem` | 输送系统 | |
| 设备 | `PressMachine` | 冲压设备 | |
| 设备 | `QualityInspectionEquipment` | 质量检测设备 | |
| 故障 | `FaultEvent` | 故障事件 | |
| 故障 | `MechanicalFault` | 机械故障 | |
| 故障 | `ElectricalFault` | 电气故障 | |
| 故障 | `SensorFault` | 传感器故障 | |
| 故障 | `HydraulicFault` | 液压故障 | |
| 异常 | `ProductionException` | 生产异常 | |
| 异常 | `LineStopException` | 停线异常 | |
| 异常 | `QualityDeviationException` | 质量偏差异常 | |
| 异常 | `ScheduleDelayException` | 计划延迟异常 | |
| 工单 | `WorkOrder` | 工单 | |
| 工单 | `ProductionWorkOrder` | 生产工单 | |
| 工单 | `MaintenanceWorkOrder` | 维护工单 | |
| 产品 | `Product` | 产品 | 白车身、副车架等 |
| 产品 | `VehicleModel` | 车型 | Model-X / Model-Y |
| 排班 | `Shift` | 班次 | 早班/中班 |

### 1.2 对象属性（关系）

| 属性 | 中文 | 定义域 → 值域 | Demo 用途 |
|------|------|---------------|-----------|
| `locatedInWorkshop` | 位于车间 | Equipment → Workshop | 设备归属 |
| `belongsToProductionLine` | 属于产线 | Equipment → ProductionLine | 设备产线绑定 |
| `lineLocatedInWorkshop` | 产线位于车间 | ProductionLine → Workshop | 产线归属 |
| `occursOnEquipment` | 发生在设备上 | FaultEvent → Equipment | 故障定位 |
| `triggersException` | 触发生产异常 | FaultEvent → ProductionException | **核心因果链 1** |
| `affectsWorkOrder` | 影响工单 | ProductionException → WorkOrder | **核心因果链 2** |
| `assignedToEquipment` | 分配至设备 | WorkOrder → Equipment | 工单执行资源 |
| `producesProduct` | 生产产品 | ProductionWorkOrder → Product | 产出物 |
| `forVehicleModel` | 对应车型 | WorkOrder → VehicleModel | 车型关联 |
| `handledByMaintenanceOrder` | 由维护工单处理 | ProductionException → MaintenanceWorkOrder | 异常处置 |
| `executedOnShift` | 执行班次 | WorkOrder → Shift | 排班信息 |
| `relatedFaultEvent` | 关联故障事件 | ProductionException → FaultEvent | 异常溯源 |

### 1.3 数据属性（字段）

| 属性 | 中文 | 类型 | 适用类 |
|------|------|------|--------|
| `equipmentId` | 设备编号 | string | Equipment |
| `equipmentStatus` | 设备状态 | string | Equipment（Running/Fault/Maintenance/Idle） |
| `faultCode` | 故障代码 | string | FaultEvent |
| `severityLevel` | 严重等级 | string | FaultEvent（Critical/Major/Minor） |
| `occurrenceTime` | 发生时间 | dateTime | FaultEvent |
| `resolutionTime` | 恢复时间 | dateTime | FaultEvent |
| `downtimeMinutes` | 停机时长(分钟) | integer | ProductionException |
| `exceptionDescription` | 异常描述 | string | ProductionException |
| `defectRate` | 不良率 | double | ProductionException |
| `workOrderId` | 工单编号 | string | WorkOrder |
| `workOrderStatus` | 工单状态 | string | WorkOrder（Planned/InProgress/Suspended/Delayed/Completed） |
| `plannedQuantity` | 计划产量 | integer | ProductionWorkOrder |
| `actualQuantity` | 实际产量 | integer | ProductionWorkOrder |
| `plannedStartTime` | 计划开始时间 | dateTime | WorkOrder |
| `plannedEndTime` | 计划结束时间 | dateTime | WorkOrder |
| `lineCapacity` | 产线产能 | integer | ProductionLine |

**日期时间格式：** `YYYY-MM-DDTHH:mm:ss`（ISO 8601，如 `2025-07-11T08:15:00`）

### 1.4 实例数据（ABox — 已包含在本体文件中）

| 实体类型 | 数量 | 示例 ID |
|----------|------|---------|
| 车间 | 3 | WS-Stamping, WS-Welding, WS-Assembly |
| 产线 | 4 | LINE-Weld-A, LINE-Weld-B, LINE-Assembly-Main, LINE-Stamping-1 |
| 设备 | 6 | WR-001, WR-002, CNC-003, CV-004, PR-005, QI-006 |
| 故障事件 | 4 | FAULT-20250711-001 ~ 004 |
| 生产异常 | 4 | EXC-20250711-001 ~ 004 |
| 生产工单 | 4 | WO-2025-0701 ~ 0704 |
| 维护工单 | 1 | WO-M-001 |
| 产品 | 4 | Product-BIW, Product-Subframe, ... |
| 车型 | 2 | Model-X-EV, Model-Y-Hybrid |
| 班次 | 2 | Shift-Morning, Shift-Afternoon |

---

## 二、文件说明

| 文件 | 格式 | 用途 |
|------|------|------|
| `automotive-equipment-fault-demo.ttl` | Turtle (OWL 2) | **主数据文件**：含完整 TBox + ABox + 因果链，推荐首选导入 |
| `csv/equipment.csv` | CSV | 设备主数据（可选，用于 CSV 批量追加） |
| `csv/fault-events.csv` | CSV | 故障事件（可选，含 1 条额外故障供扩展） |
| `csv/work-orders.csv` | CSV | 工单（可选，与本体中实例一致） |

**命名空间：** `http://example.com/automotive/production#`  
**前缀建议：** `auto:`

---

## 三、导入 WebProtégé 步骤

### 方式 A：上传本体（推荐）

1. 登录 WebProtégé（本地：`http://localhost:5000`）
2. 确认 **Settings** 中已开启 **Project import**
3. 首页点击 **Upload Project...**
4. 填写项目信息：
   - **Project name:** `汽车设备故障与工单Demo`
   - **Description:** `设备故障触发生产异常影响工单执行演示`
5. 选择 `automotive-equipment-fault-demo.ttl` 上传
6. 进入项目后，在 **Class hierarchy** 浏览类层次，在 **Individuals by class** 查看实例

### 方式 B：创建空项目后合并

1. **Create Project...** 创建空白项目
2. 项目内 **Project → Upload and append merge**
3. 上传 `automotive-equipment-fault-demo.ttl`

### 方式 C：CSV 追加（需先导入本体）

本体导入完成后，可通过 **Project → Import from CSV** 追加新个体：

1. 选择导入类型：**Named Individual**
2. 选择根类（如 `FaultEvent`）
3. 将 CSV 列映射到对应数据/对象属性
4. 确认导入

> CSV 导入前须确保目标属性已在本体中定义（方式 A/B 导入后已满足）。

---

## 四、Demo 演示建议

### 4.1 浏览因果链

在 **OWL Graph** 或 **Class hierarchy** 中选中 `FAULT-20250711-001`，查看其对象属性断言：

- `occursOnEquipment` → `WR-001`
- `triggersException` → `EXC-20250711-001`

再选中 `EXC-20250711-001`：

- `affectsWorkOrder` → `WO-2025-0701`
- `handledByMaintenanceOrder` → `WO-M-001`

### 4.2 查询受影响工单

添加 **SPARQL Query** Portlet，示例查询：

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

### 4.3 讨论与协作演示

- 在 `WO-2025-0701`（停线工单）上添加 **Comment**，模拟生产/设备/计划部门协同
- 使用 **Watch** 订阅 `FaultEvent` 类变更通知

---

## 五、扩展数据

如需追加更多故障或工单：

1. 编辑 `automotive-equipment-fault-demo.ttl` 添加新个体与断言，再 **Upload and append merge**
2. 或使用 `csv/*.csv` 通过 CSV 导入向导批量添加
3. 新增实例须保持因果链完整：`FaultEvent` → `triggersException` → `ProductionException` → `affectsWorkOrder` → `WorkOrder`

---

## 六、数据字典 — 枚举值参考

| 字段 | 允许值 |
|------|--------|
| equipmentStatus | Running, Fault, Maintenance, Idle |
| severityLevel | Critical, Major, Minor |
| workOrderStatus | Planned, InProgress, Suspended, Delayed, Completed |
