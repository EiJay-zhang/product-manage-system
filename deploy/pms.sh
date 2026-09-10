#!/bin/bash
# PMS backend start/stop
# Usage: ./pms.sh {start|stop|restart|status}
# Deploy: cp deploy/pms.sh /opt/pms/ && chmod +x /opt/pms/pms.sh
#
# Boot autostart: pick ONE
#   1) systemd  (recommended): systemctl enable ruoyi-pms
#   2) crontab:  crontab -e  ->  @reboot sleep 15 && /opt/pms/pms.sh start
# Do not enable both.

set -euo pipefail

APP_NAME=pms
APP_DIR=/opt/pms
JAR_FILE="${APP_DIR}/${APP_NAME}.jar"
PID_FILE="${APP_DIR}/${APP_NAME}.pid"
CONSOLE_LOG="${APP_DIR}/console.log"
JAVA_BIN="${JAVA_BIN:-/usr/bin/java}"

JAVA_OPTS="-Xms256m -Xmx512m -Dfile.encoding=UTF-8 -Duser.home=${APP_DIR}"
SPRING_OPTS=(
  --spring.data.redis.host=127.0.0.1
  --spring.datasource.druid.master.url="jdbc:mysql://127.0.0.1:3306/pms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8"
)

cd "$APP_DIR" 2>/dev/null || {
  echo "dir not found: $APP_DIR"
  exit 1
}

running_pid() {
  if [[ -f "$PID_FILE" ]]; then
    local pid
    pid="$(cat "$PID_FILE" 2>/dev/null || true)"
    if [[ -n "${pid}" ]] && kill -0 "$pid" 2>/dev/null; then
      echo "$pid"
      return 0
    fi
  fi
  pgrep -f "java .*-jar ${JAR_FILE}" || true
}

start() {
  local pid
  pid="$(running_pid)"
  if [[ -n "$pid" ]]; then
    echo "${APP_NAME} already running, pid ${pid}"
    return 0
  fi
  if [[ ! -x "$JAVA_BIN" ]]; then
    JAVA_BIN="$(command -v java || true)"
  fi
  if [[ -z "$JAVA_BIN" ]]; then
    echo "java not found"
    exit 1
  fi
  if [[ ! -f "$JAR_FILE" ]]; then
    echo "jar not found: $JAR_FILE"
    exit 1
  fi
  nohup "$JAVA_BIN" $JAVA_OPTS -jar "$JAR_FILE" "${SPRING_OPTS[@]}" >>"$CONSOLE_LOG" 2>&1 &
  echo $! >"$PID_FILE"
  echo "${APP_NAME} started, pid $(cat "$PID_FILE")"
  echo "console log: $CONSOLE_LOG"
  echo "app log: ${APP_DIR}/logs/ruoyi/sys-info.log"
}

stop() {
  local pid
  pid="$(running_pid)"
  if [[ -z "$pid" ]]; then
    echo "${APP_NAME} is not running"
    rm -f "$PID_FILE"
    return 0
  fi
  echo "stopping ${APP_NAME} pid ${pid} ..."
  kill "$pid" 2>/dev/null || true
  local i
  for i in $(seq 1 30); do
    if ! kill -0 "$pid" 2>/dev/null; then
      break
    fi
    sleep 1
  done
  if kill -0 "$pid" 2>/dev/null; then
    echo "force kill ${pid}"
    kill -9 "$pid" 2>/dev/null || true
  fi
  rm -f "$PID_FILE"
  echo "${APP_NAME} stopped"
}

status() {
  local pid
  pid="$(running_pid)"
  if [[ -n "$pid" ]]; then
    echo "${APP_NAME} is running, pid ${pid}"
    return 0
  fi
  echo "${APP_NAME} is not running"
  return 1
}

case "${1:-}" in
  start) start ;;
  stop) stop ;;
  restart) stop; start ;;
  status) status ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
    ;;
esac
