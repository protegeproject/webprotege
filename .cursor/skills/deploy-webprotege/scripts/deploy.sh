#!/usr/bin/env bash
# Local-machine deploy: full GWT Maven package → Docker image → recreate containers.
# 本机打包编译部署（不用于远程服务器）。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../../.." && pwd)"
VERSION="${WEBPROTEGE_VERSION:-5.0.0-SNAPSHOT}"
WAR="$ROOT/webprotege-server/target/webprotege-server-${VERSION}.war"
CLI_JAR="$ROOT/webprotege-cli/target/webprotege-cli-${VERSION}.jar"
PORT="${WEBPROTEGE_PORT:-5000}"

RUN_MAVEN=1
RUN_DOCKER=1
MVN_ARGS=(-DskipTests)

usage() {
  cat <<EOF
Usage: $(basename "$0") [options]

  --skip-tests    Pass -DskipTests to Maven (default)
  --run-tests     Do not skip tests
  --maven-only    Only run mvn clean package
  --docker-only   Only rebuild image and recreate containers (requires existing WAR)
  -h, --help      Show this help
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --skip-tests) MVN_ARGS=(-DskipTests); shift ;;
    --run-tests) MVN_ARGS=(); shift ;;
    --maven-only) RUN_DOCKER=0; shift ;;
    --docker-only) RUN_MAVEN=0; shift ;;
    -h|--help) usage; exit 0 ;;
    *) echo "Unknown option: $1" >&2; usage >&2; exit 1 ;;
  esac
done

cd "$ROOT"

if [[ "$RUN_MAVEN" -eq 1 ]]; then
  echo "==> [1/3] mvn clean package (with GWT; do NOT use -P skip-gwt-compilation)"
  mvn clean package "${MVN_ARGS[@]}"
fi

if [[ ! -f "$WAR" ]]; then
  echo "ERROR: WAR not found: $WAR" >&2
  exit 1
fi
if ! unzip -l "$WAR" | grep -q 'webprotege/webprotege.nocache.js'; then
  echo "ERROR: WAR missing webprotege/webprotege.nocache.js (built with skip-gwt?)" >&2
  exit 1
fi
if [[ ! -f "$CLI_JAR" ]]; then
  echo "ERROR: CLI jar not found: $CLI_JAR" >&2
  exit 1
fi

if [[ "$RUN_DOCKER" -eq 0 ]]; then
  echo "==> Maven-only done. WAR OK: $WAR"
  exit 0
fi

echo "==> [2/3] Rebuild protegeproject/webprotege from frontend-inclusive WAR"
BUILD_CTX="$(mktemp -d)"
cleanup() { rm -rf "$BUILD_CTX"; }
trap cleanup EXIT

cp "$WAR" "$BUILD_CTX/webprotege-server-${VERSION}.war"
cp "$CLI_JAR" "$BUILD_CTX/webprotege-cli-${VERSION}.jar"
cat > "$BUILD_CTX/Dockerfile" <<EOF
FROM tomcat:8-jre11-slim
RUN rm -rf /usr/local/tomcat/webapps/* \\
    && mkdir -p /srv/webprotege \\
    && mkdir -p /usr/local/tomcat/webapps/ROOT
WORKDIR /usr/local/tomcat/webapps/ROOT
COPY webprotege-cli-${VERSION}.jar /webprotege-cli.jar
COPY webprotege-server-${VERSION}.war ./webprotege.war
RUN unzip -q webprotege.war && rm webprotege.war
EOF

docker build -t protegeproject/webprotege:latest -t protegeproject/webprotege:local "$BUILD_CTX"
docker run --rm protegeproject/webprotege:latest \
  test -f /usr/local/tomcat/webapps/ROOT/webprotege/webprotege.nocache.js

echo "==> [3/3] Recreate and start containers"
docker compose up -d --force-recreate webprotege

echo "==> Waiting for startup..."
for i in $(seq 1 60); do
  if docker logs webprotege 2>&1 | grep -q 'WebProtege initialization complete'; then
    break
  fi
  sleep 2
done

echo "==> Verify"
curl -sI "http://127.0.0.1:${PORT}/webprotege/webprotege.nocache.js" | head -5
docker ps --filter name=webprotege --format 'table {{.Names}}\t{{.Status}}\t{{.Ports}}'
echo "Deploy complete. Hard-refresh http://localhost:${PORT}"
