#!/usr/bin/env bash
# Soak test: run two backend jars side by side against local targets and sample
# heap, threads and check-cycle time. Used to compare the pre-fix and current code.
#   ops/soak/soak.sh OLD_JAR NEW_JAR MINUTES OUT_DIR
# Needs Docker, a JDK (jcmd/jstat on PATH) and python.
set -euo pipefail
OLD_JAR=$1; NEW_JAR=$2; MINUTES=$3; OUT=$4
HERE=$(cd "$(dirname "$0")" && pwd)
mkdir -p "$OUT"

docker rm -f ss-soak-pg >/dev/null 2>&1 || true
docker run -d --name ss-soak-pg -p 5433:5432 -e POSTGRES_PASSWORD=pg postgres:16-alpine >/dev/null
until docker exec ss-soak-pg pg_isready -U postgres >/dev/null 2>&1; do sleep 1; done
sleep 2

python "$HERE/targets.py" 9100 > "$OUT/targets.log" 2>&1 &
TARGETS=$!

start_app() { # name jar port
  java -Xmx128m -Dsoak.name="$1" -jar "$2" \
    --server.port="$3" --spring.profiles.active=soak \
    --spring.datasource.url=jdbc:postgresql://localhost:5433/postgres \
    --spring.datasource.username=postgres --spring.datasource.password=pg \
    --spring.flyway.schemas="$1" --spring.flyway.default-schema="$1" \
    --spring.datasource.hikari.schema="$1" --spring.flyway.baseline-on-migrate=true \
    --logging.level.com.atinder.service_status_backend=INFO \
    --logging.level.org.springframework.jdbc.core=WARN \
    --security.jwt.secret=soak-test-secret-at-least-32-bytes-long \
    > "$OUT/$1.log" 2>&1 &
}
start_app old "$OLD_JAR" 8091
start_app new "$NEW_JAR" 8092
until curl -sf localhost:8091/actuator/health >/dev/null && curl -sf localhost:8092/actuator/health >/dev/null; do sleep 2; done

# 60 services each: 30 fast, 15 that hang for 30 s, 15 to a blackhole IP that never answers SYN
for schema in old new; do
  sql="INSERT INTO $schema.services (name, url) VALUES "
  for i in $(seq 1 30); do sql+="('fast$i','http://127.0.0.1:9100/ok?$i'),"; done
  for i in $(seq 1 15); do sql+="('slow$i','http://127.0.0.1:9100/hang?$i'),"; done
  for i in $(seq 1 15); do sql+="('hole$i','http://10.255.255.1:81/$i'),"; done
  docker exec ss-soak-pg psql -q -U postgres -c "${sql%,};"
done

pid_of() { jps -v | grep -- "-Dsoak.name=$1" | cut -d' ' -f1; }
OLD_PID=$(pid_of old); NEW_PID=$(pid_of new)
echo "t_min,app,heap_used_mb,live_threads,rss_mb" > "$OUT/samples.csv"
END=$(( $(date +%s) + MINUTES * 60 )); T0=$(date +%s)
while [ "$(date +%s)" -lt "$END" ]; do
  for app in old new; do
    pid=$([ $app = old ] && echo "$OLD_PID" || echo "$NEW_PID")
    heap=$(jstat -gc "$pid" | tail -1 | awk '{printf "%.1f", ($3+$4+$6+$8)/1024}')
    threads=$(jcmd "$pid" Thread.print 2>/dev/null | grep -c '^"' || true)
    rss=$(powershell -NoProfile -Command "[math]::Round((Get-Process -Id $pid).WorkingSet64/1MB,1)")
    echo "$(( ($(date +%s) - T0) / 60 )),$app,$heap,$threads,$rss" >> "$OUT/samples.csv"
  done
  sleep 30
done

# jps gives Windows PIDs there, which bash's kill can't reach
if command -v taskkill >/dev/null; then taskkill //F //PID "$OLD_PID" //PID "$NEW_PID" >/dev/null; else kill "$OLD_PID" "$NEW_PID"; fi
kill "$TARGETS" 2>/dev/null || true
docker rm -f ss-soak-pg >/dev/null
python "$HERE/summarize.py" "$OUT"
