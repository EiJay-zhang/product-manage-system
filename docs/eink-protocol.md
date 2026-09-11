# 墨水屏设备协议（V1）

V1 **只使用 HTTP 轮询**，不接 MQTT。后台「刷新 / 重启」写入同步日志，设备下次心跳或拉内容时带上待执行指令。

## 鉴权

`/pms/eink/**` 使用 `@Anonymous`，**免登录**。请仅在内网或受控链路上使用；后续迭代再加设备密钥。

基础路径：`http://<host>:8080/pms/eink`

## 在线判定

- 心跳成功会把 `last_heartbeat` 更新为当前时间，`online_status = 1`
- 距上次心跳超过 **120 秒**（`PmsConstants.HEARTBEAT_TIMEOUT_SEC`）列表/详情展示为离线
- 从未心跳的设备视为离线

## 轮询间隔

配置项：`pms_setting.eink_sync_interval_sec`（业务设置页「墨水屏轮询间隔秒」）。

| 配置值 | 下发给设备的 `pollIntervalSec` |
| --- | --- |
| `null`、`0`、负数 | `5`（近实时） |
| `> 0` | 原值（秒） |

设备应按返回的 `pollIntervalSec` sleep 后再请求。不要在设备侧写死间隔。

## 1. 心跳 `POST /pms/eink/heartbeat`

请求：

```json
{
  "sn": "EINK-001",
  "battery": 86,
  "firmware": "1.0.0"
}
```

`sn` 必填。未知 SN 会自动注册一台未绑定商品的设备。

成功响应（节选）：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "deviceId": 1,
    "sn": "EINK-001",
    "productId": 12,
    "onlineStatus": "1",
    "battery": 86,
    "firmware": "1.0.0"
  },
  "pollIntervalSec": 5,
  "pendingCommand": "refresh",
  "pendingLogId": 88
}
```

`pollIntervalSec` / `pendingCommand` / `pendingLogId` 在 **AjaxResult 根上**（与 `data` 同级）。无待执行指令时后两者为 `null`。

`pendingCommand`：`refresh` | `restart` | `unbind`。

## 2. 拉内容 `GET /pms/eink/content/{sn}`

设备未注册返回业务错误「设备未注册」。

`data`（`PmsEinkContentVo`）：

```json
{
  "sn": "EINK-001",
  "productName": "示例商品",
  "spec": "500g",
  "salePrice": 19.90,
  "intro": "简介",
  "stockQty": null,
  "supplierName": null,
  "fontStyle": "default",
  "empty": "N",
  "pollIntervalSec": 5,
  "pendingCommand": "refresh",
  "pendingLogId": 88
}
```

- `empty = Y`：未绑定商品或商品已删，屏幕应显示空态
- 名称/规格/售价/简介/库存/厂家是否下发，由业务设置 `show_*` 控制（库存、厂家默认不展示）

## 3. 回执 `POST /pms/eink/ack`

执行完 `pendingCommand` 后调用，把对应日志从「待确认」改为成功或失败。

推荐（有 `pendingLogId` 时）：

```json
{
  "logId": 88,
  "syncStatus": "1",
  "remark": "ok"
}
```

无 `logId` 时可按 SN 更新最新一条待确认日志：

```json
{
  "sn": "EINK-001",
  "syncStatus": "1"
}
```

`syncStatus`：`1` 成功 / `0` 失败。

## 建议设备流程

1. `POST /heartbeat`（上报电量、固件）
2. 若 `pendingCommand` 非空：执行指令，再 `POST /ack`
3. `GET /content/{sn}` 刷新屏幕
4. 等待 `pollIntervalSec` 秒后回到步骤 1

库存或价签变更时，后台会插入 `refresh` 待确认日志；设备不必依赖 MQTT 即可在下一轮看到。
