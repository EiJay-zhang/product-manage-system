# 商品管理系统部署手册

本仓库只有后端。JAR 监听 `8080`，前端是独立的若依 Vue 工程，生产环境请求前缀为 `/prod-api`。Nginx 对外提供 80 端口：托管前端静态资源，并把 `/prod-api/` 反代到本机 8080。

相关文件：

| 文件 | 用途 |
| --- | --- |
| `deploy/nginx.conf` | Nginx 站点配置，复制到 `/etc/nginx/conf.d/pms.conf` |
| `deploy/ruoyi-pms.service` | systemd 服务，复制到 `/etc/systemd/system/ruoyi-pms.service` |
| `deploy/pms.sh` | 启停脚本，复制到 `/opt/pms/pms.sh` |

当前开发配置里 MySQL、Redis 指向 `8.133.190.119`。应用部署在这台机器上时，必须走 `127.0.0.1`（service / `pms.sh` 已覆盖）。安全组不要对公网开放 `3306` / `6379` / `8080`。

## 架构

```
浏览器 / 墨水屏
    │
    ▼
Nginx :80
    ├── /              → 前端 dist（/opt/pms/pms_web）
    ├── /prod-api/     → http://127.0.0.1:8080/   （登录、/pms/**、上传等）
    ├── /profile/      → http://127.0.0.1:8080/profile/  （头像、附件直链）
    └── /pms/eink/     → 设备心跳（可不带 /prod-api）
    │
    ▼
pms.jar :8080
    ├── MySQL 127.0.0.1:3306 / pms
    └── Redis 127.0.0.1:6379
```

服务器目录：

```
/opt/pms/
├── pms.jar                 # 后端（finalName 为 pms）
├── pms.jar.bak             # 更新时的备份
├── pms.sh                  # 手动启停脚本
├── pms.pid / console.log   # 仅脚本启动时使用
├── pms_web/                # 前端 dist
├── ruoyi/uploadPath/       # 上传文件（-Duser.home=/opt/pms）
└── logs/ruoyi/             # 应用日志
```

| 项 | 值 |
| --- | --- |
| JAR | `/opt/pms/pms.jar` |
| 前端 | `/opt/pms/pms_web` |
| 上传 | `/opt/pms/ruoyi/uploadPath` |
| 日志 | `/opt/pms/logs/ruoyi` |
| 库名 | `pms` |
| Redis / MySQL 密码 | 与 jar 内配置一致，当前为 `center` |

开机自启和日常启停：**systemd 与 `pms.sh` 二选一**，不要两套同时开。推荐 systemd。

---

## 一、服务器准备

以 CentOS / Alibaba Cloud Linux / Ubuntu 为例，SSH 登录后执行。

### 1. 安装 JDK 17

必须是 17。8 / 11 / 21 都可能起不来或行为异常。

```bash
# CentOS / Alibaba Cloud Linux
yum install -y java-17-openjdk java-17-openjdk-devel

# Ubuntu
# apt update && apt install -y openjdk-17-jdk

java -version    # 第一行应看到 17
which java       # systemd 默认用 /usr/bin/java，对不上就改 service / pms.sh
```

### 2. 安装 Nginx

```bash
# CentOS
yum install -y nginx

# Ubuntu
# apt install -y nginx

systemctl enable nginx
systemctl start nginx
```

### 3. MySQL、Redis

库名 `pms`。初始化顺序：

1. `sql/ry_20260417.sql` — 若依系统表
2. `sql/quartz.sql` — 定时任务表
3. `sql/pms.sql` — 业务表、菜单、字典

已有库补物流商时再执行 `sql/pms_carrier.sql`。

本机验证：

```bash
mysql -uroot -pcenter -h127.0.0.1 -e "SHOW DATABASES;"
redis-cli -a center ping    # 应返回 PONG
```

密码以 `application-druid.yml` / `application.yml` 为准。改过库密码时，要么改配置重新打包，要么在 service / `pms.sh` 里加启动参数覆盖。

### 4. 云安全组与本机防火墙

| 端口 | 是否开放公网 |
| --- | --- |
| 22 | 是（SSH） |
| 80 | 是（Nginx） |
| 443 | 有 HTTPS 再开 |
| 8080 / 3306 / 6379 | **不要对 `0.0.0.0/0` 开放** |

本机 firewalld 若开启，还要放行 80：

```bash
firewall-cmd --permanent --add-service=http
firewall-cmd --reload
```

---

## 二、上传并启动后端

```bash
mkdir -p /opt/pms
```

本机打包：

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"
mvn -pl ruoyi-admin -am package -DskipTests
```

产物：`ruoyi-admin/target/pms.jar`。

本机上传：

```bash
scp ruoyi-admin/target/pms.jar root@8.133.190.119:/opt/pms/pms.jar
scp deploy/ruoyi-pms.service root@8.133.190.119:/etc/systemd/system/ruoyi-pms.service
scp deploy/pms.sh root@8.133.190.119:/opt/pms/pms.sh
```

服务器上：

```bash
chmod +x /opt/pms/pms.sh
```

`which java` 不是 `/usr/bin/java` 时，改 service 的 `ExecStart` 和 `pms.sh` 的 `JAVA_BIN`。

### 方式 A：systemd（推荐）

服务器重启后会自动拉起 JAR。

```bash
systemctl daemon-reload
systemctl enable ruoyi-pms
systemctl start ruoyi-pms
systemctl status ruoyi-pms
```

`active (running)` 才算起来。改过 `.service` 文件后必须再执行一次 `daemon-reload`。

### 方式 B：脚本 `pms.sh`

适合手动启停。需要开机自启时用 crontab，**不要**再 `systemctl enable ruoyi-pms`。

```bash
/opt/pms/pms.sh start
/opt/pms/pms.sh status
/opt/pms/pms.sh stop
/opt/pms/pms.sh restart
```

crontab 开机自启：

```bash
crontab -e
# 加入一行
@reboot sleep 15 && /opt/pms/pms.sh start
```

看日志：

```bash
tail -f /opt/pms/logs/ruoyi/sys-info.log
tail -f /opt/pms/logs/ruoyi/sys-error.log
journalctl -u ruoyi-pms -f          # systemd 方式
tail -f /opt/pms/console.log        # 脚本方式
```

看到 `Started RuoYiApplication` 后测本机接口：

```bash
curl -s http://127.0.0.1:8080/captchaImage
ss -lntp | grep 8080
```

应返回 JSON（含验证码图片）。公网不要直接访问 8080。

service / `pms.sh` 已把 Redis、MySQL 主机改成 `127.0.0.1`。数据库不在本机时，改成内网 IP。密码仍用 jar 内配置，不会被这两处覆盖。

---

## 三、配置 Nginx

核心规则：`/prod-api/xxx` 转发为 `http://127.0.0.1:8080/xxx`。`proxy_pass` **末尾斜杠必须保留**，用来去掉 `/prod-api` 前缀，与若依前端默认一致。

本机上传：

```bash
scp deploy/nginx.conf root@8.133.190.119:/etc/nginx/conf.d/pms.conf
```

服务器上：

```bash
# 有域名就改 server_name；没有就保持 IP
vi /etc/nginx/conf.d/pms.conf

nginx -t
systemctl reload nginx
```

主配置里如果还有 `server { listen 80; ... }` 的默认站点，会和 `pms.conf` 抢 80 端口。处理办法：注释掉默认 server，或把前端 root 直接写进默认站点。

完整配置见 `deploy/nginx.conf`，要点：

- 前端目录：`/opt/pms/pms_web`
- `client_max_body_size 20m`：与 `application.yml` 中 `max-request-size: 20MB` 对齐
- `/`：Vue history 模式，`try_files` 回退到 `index.html`
- `/index.html` 不缓存，避免发版后用户仍用旧入口
- `/prod-api/`：业务接口（`^~`，避免静态资源正则抢走请求）
- `/profile/`：上传文件直链
- `/pms/eink/`：墨水屏可不带 `/prod-api`

CentOS / Alibaba Cloud Linux 上 Nginx 读 `/opt/pms/pms_web` 可能被 SELinux 拦住，首次部署建议执行：

```bash
chcon -Rt httpd_sys_content_t /opt/pms/pms_web
# 若没有 chcon：yum install -y policycoreutils-python-utils
```

---

## 四、部署前端

本仓库没有前端。独立若依 Vue 工程生产环境必须：

```bash
# .env.production
VUE_APP_BASE_API = '/prod-api'
```

改完后重新 `npm run build:prod`，改 env 不重新打包等于没改。

```bash
npm run build:prod
# 产物一般在 dist/

mkdir -p /opt/pms/pms_web
scp -r dist/* root@8.133.190.119:/opt/pms/pms_web/
```

暂时只有后端、没有前端时，可以先用：

- `http://8.133.190.119/prod-api/captchaImage`
- `http://8.133.190.119/prod-api/swagger-ui/index.html`

登录页要等 `dist` 放到 Nginx 目录后才能打开。

---

## 五、验收

| 地址 | 预期 |
| --- | --- |
| `http://8.133.190.119/` | 登录页 |
| `http://8.133.190.119/prod-api/captchaImage` | JSON，含验证码 |
| `http://8.133.190.119/prod-api/login` | POST 可登录 |
| `http://8.133.190.119/pms/eink/content/测试SN` | 设备接口（免登录） |

默认账号 `admin` / `admin123`，登录后立刻改密。

---

## 六、日常运维

```bash
# 重启后端（systemd）
systemctl restart ruoyi-pms
# 或脚本
/opt/pms/pms.sh restart

# 更新 jar：先停再备份再覆盖，然后启动
systemctl stop ruoyi-pms          # 或 /opt/pms/pms.sh stop
cp /opt/pms/pms.jar /opt/pms/pms.jar.bak
# 再 scp 新 jar 到 /opt/pms/pms.jar
systemctl start ruoyi-pms         # 或 /opt/pms/pms.sh start

# 更新前端：覆盖 dist 即可，一般不用重启 nginx
# scp -r dist/* root@8.133.190.119:/opt/pms/pms_web/
```

| 项 | 路径 |
| --- | --- |
| 上传文件 | `/opt/pms/ruoyi/uploadPath` |
| 应用日志 | `/opt/pms/logs/ruoyi` |
| systemd 日志 | `journalctl -u ruoyi-pms` |
| 脚本控制台日志 | `/opt/pms/console.log` |

---

## 七、常见问题

先按层排查，不要一上来改代码：

1. JAR 是否在听 8080：`ss -lntp | grep 8080`、`curl -s http://127.0.0.1:8080/captchaImage`
2. Nginx 是否在听 80：`ss -lntp | grep ':80'`、`nginx -t`
3. 浏览器实际请求的 URL 是不是 `/prod-api/...`

### 1. 页面能开，接口 404 / 登录失败

现象：登录页出来了，点登录或验证码报 404、网络错误。

原因和解决：

- 前端生产环境 `VUE_APP_BASE_API` 必须是 `/prod-api`，改完必须重新 `npm run build:prod`。
- Nginx `location /prod-api/` 里 `proxy_pass http://127.0.0.1:8080/;` **末尾必须有 `/`**。少了斜杠会变成 `/prod-api/login` 原样打到后端，后端没有这个路径。
- 浏览器开发者工具看请求 URL：正确是 `http://IP/prod-api/login`，响应应是 JSON。若请求打到 `http://IP/login` 或 `http://IP:8080/login`，是前端 baseAPI 或代理配错。
- 改完 Nginx 后执行 `nginx -t && systemctl reload nginx`。

### 2. 502 Bad Gateway

现象：Nginx 能访问，接口或整站 502。

```bash
systemctl status ruoyi-pms
/opt/pms/pms.sh status
ss -lntp | grep 8080
tail -n 100 /opt/pms/logs/ruoyi/sys-error.log
journalctl -u ruoyi-pms -n 100 --no-pager
```

常见原因：

- JAR 没起来：按下面「JAR 启动失败」处理。
- 8080 被别的进程占用，当前 JAR 没绑上。
- Nginx 反代地址写错（必须是 `127.0.0.1:8080`，不要写公网 IP）。
- 刚重启，JAR 还在连库，等 `Started RuoYiApplication` 再试。

### 3. JAR 启动失败 / 启动后立刻退出

```bash
journalctl -u ruoyi-pms -n 150 --no-pager
tail -n 150 /opt/pms/console.log
tail -n 150 /opt/pms/logs/ruoyi/sys-error.log
java -version
ls -l /opt/pms/pms.jar
```

对照处理：

| 日志关键词 | 处理 |
| --- | --- |
| `UnsupportedClassVersion` / `class file version` | JDK 不是 17，按第一节重装 |
| `Unable to access jarfile` / `jar not found` | 文件必须是 `/opt/pms/pms.jar`。旧名 `ruoyi-admin.jar` 已废弃 |
| `Address already in use` / `端口 8080` | 见第 4 条，先杀掉旧进程 |
| `Access denied for user` | MySQL 账号密码与 jar 不一致，或用户不允许从 `127.0.0.1` 登录 |
| `Communications link failure` / `Connection refused` 且带 `3306` | MySQL 没启动，或还在连公网 IP。确认 service / `pms.sh` 里是 `127.0.0.1` |
| `Unable to connect to Redis` / `NOAUTH` / `WRONGPASS` | Redis 没启动，或密码不是 jar 里的 `center` |
| `Table 'pms.xxx' doesn't exist` | 初始化 SQL 没跑完，按顺序重跑 |
| `Failed to execute goal` 出现在本机打包 | 本机 JDK 17 + Maven，不要拿源码到服务器编译 |

`ExecStart` 里的 Java 路径必须真实存在：

```bash
ls -l /usr/bin/java
readlink -f /usr/bin/java
```

对不上就把 service / `pms.sh` 改成 `which java` 的结果，然后 `systemctl daemon-reload && systemctl restart ruoyi-pms`。

### 4. 8080 被占用 / 两套启动方式冲突

现象：启动报端口占用，或改代码不生效、停不干净。

```bash
ss -lntp | grep 8080
ps -ef | grep pms.jar | grep -v grep
systemctl is-enabled ruoyi-pms
systemctl is-active ruoyi-pms
```

不要同时使用 systemd 和 `pms.sh`。

- 用 systemd：`crontab -l` 里删掉 `@reboot ... pms.sh`，只用 `systemctl enable --now ruoyi-pms`。
- 用脚本：`systemctl disable --now ruoyi-pms`，再 `/opt/pms/pms.sh start`。

残留进程：

```bash
kill $(ps -ef | grep '/opt/pms/pms.jar' | grep -v grep | awk '{print $2}')
# 仍占着就 kill -9
systemctl start ruoyi-pms    # 或 /opt/pms/pms.sh start
```

### 5. 连不上数据库 / Redis

本机部署必须用 `127.0.0.1`。jar 内默认是公网 IP `8.133.190.119`，生产靠 service / `pms.sh` 覆盖主机，**不覆盖密码**。

```bash
systemctl cat ruoyi-pms | grep -E 'redis|mysql|jdbc'
grep -n redis /opt/pms/pms.sh

mysql -uroot -pcenter -h127.0.0.1 -e "SELECT 1;"
redis-cli -h 127.0.0.1 -a center ping
systemctl status mysqld mariadb redis redis-server
```

处理：

- MySQL/Redis 没起来：先 `systemctl start mysqld`（或 `mariadb`）和 `systemctl start redis`。
- 密码改过：同步改 `application.yml` / `application-druid.yml` 后重新打包，或在 `ExecStart` / `SPRING_OPTS` 增加：
  - `--spring.data.redis.password=新密码`
  - `--spring.datasource.druid.master.username=用户`
  - `--spring.datasource.druid.master.password=新密码`
- Redis 有密码但客户端没带：日志会出现 `NOAUTH Authentication required`。
- 走公网 IP：安全组还要放行本机访问，延迟更大，生产不要这么做。
- JDBC `useSSL=true` 在部分环境会握手失败。service / `pms.sh` 已用 `useSSL=false`。

### 6. 验证码出不来 / 登录提示验证码错误

验证码图片和校验都走 Redis。

```bash
curl -s http://127.0.0.1:8080/captchaImage
redis-cli -a center ping
```

- `captchaImage` 失败：先保证 Redis 通。
- 页面能出图但提交说验证码错误：Redis 连的不是同一台；或重复提交、验证码已消费。刷新再登。
- 连续输错 5 次会锁 10 分钟（`maxRetryCount` / `lockTime`）。等过期或清 Redis 登录缓存：

```bash
redis-cli -a center keys '*login*'
# 确认 key 后再 del，不要盲删整个库
```

### 7. 登录成功马上掉线 / 过一会儿 401

Token 默认 30 分钟。属于配置，不是故障。

- 浏览器是否带了请求头 `Authorization: Bearer <token>`。
- 前端有没有把 token 存进 Cookie/LocalStorage。
- 服务器时间是否对：`date`。差太多会导致 JWT 异常。
- 多台 Redis / 重启 Redis 会让已登录态失效，重新登录即可。

### 8. 上传失败 / 头像不显示

| 现象 | 处理 |
| --- | --- |
| Nginx 413 / `Request Entity Too Large` | `client_max_body_size` 保持 20m，改完 `nginx -t && systemctl reload nginx` |
| 接口 200 但文件找不到 | 上传目录是 `/opt/pms/ruoyi/uploadPath`。确认启动带了 `-Duser.home=/opt/pms`，且目录可写：`mkdir -p /opt/pms/ruoyi/uploadPath && ls -ld /opt/pms/ruoyi/uploadPath` |
| 头像 URL 404 | 正确路径是 `/prod-api/profile/...` 或 `/profile/...`。确认 Nginx 有这两段反代，磁盘上文件存在 |
| 权限 denied | `chown` 给运行 Java 的用户（当前 service 是 root） |

### 9. 打开网站空白 / 刷新子路由 404

- `/opt/pms/pms_web/index.html` 是否存在：`ls /opt/pms/pms_web`
- `location /` 必须有 `try_files $uri $uri/ /index.html;`
- 发版后仍是旧页面：强刷，或确认 `/index.html` 的 `Cache-Control` 已生效
- CentOS 上 Nginx 报 403：SELinux，执行 `chcon -Rt httpd_sys_content_t /opt/pms/pms_web`，或临时 `getenforce` 看是否 Enforcing
- 目录权限：`chmod -R 755 /opt/pms/pms_web`

### 10. 80 端口被默认站点抢走

现象：打开 IP 看到 Nginx 欢迎页，不是登录页。

```bash
nginx -T 2>/dev/null | grep -n 'listen 80'
ls /etc/nginx/conf.d/
```

注释掉主配置里自带的 `server { listen 80; ... }`，只保留 `/etc/nginx/conf.d/pms.conf`，然后 `nginx -t && systemctl reload nginx`。

### 11. 菜单里没有商品管理模块

`pms.sql` 没导入，或导到了别的库。

```bash
mysql -uroot -pcenter -h127.0.0.1 pms -e "SHOW TABLES LIKE 'pms_%';"
mysql -uroot -pcenter -h127.0.0.1 pms -e "SELECT menu_id, menu_name FROM sys_menu WHERE menu_name LIKE '%商品%' OR path LIKE 'pms%';"
```

表或菜单缺失时，对 `pms` 库再执行一次 `sql/pms.sql`。超级管理员重新登录；其他角色要在「系统管理 → 角色」里勾选新菜单。

### 12. 墨水屏调不通

设备可以不带 `/prod-api`：

```bash
curl -s http://127.0.0.1:8080/pms/eink/content/测试SN
curl -s http://127.0.0.1/pms/eink/content/测试SN
```

- 公网应打 `http://IP/pms/eink/...`，由 Nginx 转到 8080。
- 接口免登录。若 401，说明请求没打到 `/pms/eink/`（路径写错或被 `/` 的前端 try_files 吃掉）。
- 确认 `pms.conf` 里有 `location /pms/eink/`。
- 设备 SN 必须先在后台建档。

### 13. systemd 改了配置不生效

`.service` 文件改完必须：

```bash
systemctl daemon-reload
systemctl restart ruoyi-pms
systemctl cat ruoyi-pms    # 确认看到的是新内容
```

只 `restart` 不 `daemon-reload`，仍会用旧的 ExecStart。

### 14. 内存不足 / 被系统杀掉

小规格云主机把 JVM 调低（service 和 `pms.sh` 当前是 `-Xms256m -Xmx512m`）。

```bash
dmesg -T | grep -i 'killed process'
free -h
```

出现 OOM Killer 就再降 `-Xmx`，或升级内存。不要在 1G 内存机器上把 `-Xmx` 开到 1G 以上。

### 15. 时区 / 报表日期差一天

启动参数已设 `TZ=Asia/Shanghai`。JDBC URL 使用 `serverTimezone=GMT%2B8`。

```bash
date
timedatectl
```

系统时区不对：`timedatectl set-timezone Asia/Shanghai` 后重启 JAR。

### 16. Swagger 打不开

地址是 `http://IP/prod-api/swagger-ui/index.html`（经 Nginx），或本机 `http://127.0.0.1:8080/swagger-ui/index.html`。

不要用公网 IP:8080。安全组未放行 8080 时，公网直接访问 8080 会超时，这是预期行为。

---

改完配置后的最小验证：

```bash
curl -s -o /dev/null -w '%{http_code}\n' http://127.0.0.1:8080/captchaImage
curl -s -o /dev/null -w '%{http_code}\n' http://127.0.0.1/prod-api/captchaImage
curl -s -o /dev/null -w '%{http_code}\n' http://127.0.0.1/
```

前两个应是 `200`，第三个在前端部署后应是 `200`。
