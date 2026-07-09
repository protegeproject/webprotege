#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

ACTION="all"
VERSION="$(date +%Y%m%d-%H%M%S)"
VERSION_EXPLICIT="false"

validate_version() {
  if [[ ! "${VERSION}" =~ ^[A-Za-z0-9_][A-Za-z0-9_.-]{0,127}$ ]]; then
    echo "错误: version 仅允许字母、数字、下划线、点和连字符，且不能以连字符开头"
    exit 1
  fi
}

if [[ $# -ge 1 ]]; then
  case "$1" in
    all|build|upload|deploy|-h|--help|help)
      ACTION="$1"
      ;;
    *)
      VERSION="$1"
      VERSION_EXPLICIT="true"
      ;;
  esac
fi

if [[ $# -ge 2 ]]; then
  ACTION="$2"
fi

validate_version

APP_NAME="$(basename "${PROJECT_ROOT}")"
PKG_DIR="${SCRIPT_DIR}/${APP_NAME}-${VERSION}"
OUT_TGZ="${SCRIPT_DIR}/${APP_NAME}-${VERSION}.tar.gz"

# 构建产物
BUILD_IMAGE="${BUILD_IMAGE:-webprotege:${VERSION}}"
TAR_NAME="${APP_NAME}-${VERSION}.image.tar"
BUILD_TAR_PATH="${SCRIPT_DIR}/${TAR_NAME}"

# 远程信息
REMOTE_USER="${REMOTE_USER:-guofengxu}"
REMOTE_HOST="${REMOTE_HOST:-157.0.19.155}"
REMOTE_BASE_DIR="${REMOTE_BASE_DIR:-/home/guofengxu/00.workspace}"
REMOTE_DEPLOY_DIR="${REMOTE_DEPLOY_DIR:-${REMOTE_BASE_DIR}/${APP_NAME}-${VERSION}}"

# 运行参数
WEBPROTEGE_PORT="${WEBPROTEGE_PORT:-5000}"
WEBPROTEGE_CONTAINER_NAME="${WEBPROTEGE_CONTAINER_NAME:-webprotege}"
MONGO_CONTAINER_NAME="${MONGO_CONTAINER_NAME:-webprotege-mongodb}"

usage() {
  cat <<EOF
用法:
  $0 [version] [action]
  $0 [action]

action:
  all      执行全流程 (默认): build + upload + deploy
  build    仅构建镜像并打包
  upload   仅上传已生成的包和镜像
  deploy   仅在远端执行部署

可用环境变量:
  BUILD_IMAGE, REMOTE_USER, REMOTE_HOST, REMOTE_BASE_DIR, REMOTE_DEPLOY_DIR,
  WEBPROTEGE_PORT, WEBPROTEGE_CONTAINER_NAME, MONGO_CONTAINER_NAME
EOF
}

log() {
  echo "==> $*"
}

prepare_package_dir() {
  log "准备目录: ${PKG_DIR}"
  rm -rf "${PKG_DIR}" "${OUT_TGZ}"
  mkdir -p "${PKG_DIR}/scripts" "${PKG_DIR}/.protegedata/mongodb" "${PKG_DIR}/.protegedata/protege"
}

write_compose_file() {
  log "生成 docker-compose.yml"
  cat > "${PKG_DIR}/docker-compose.yml" <<EOF
version: "3"

services:
  wpmongo:
    container_name: ${MONGO_CONTAINER_NAME}
    image: mongo:4.1-bionic
    restart: unless-stopped
    volumes:
      - ./.protegedata/mongodb:/data/db

  webprotege:
    container_name: ${WEBPROTEGE_CONTAINER_NAME}
    image: ${BUILD_IMAGE}
    depends_on:
      - wpmongo
    restart: unless-stopped
    environment:
      - webprotege.mongodb.host=wpmongo
    volumes:
      - ./.protegedata/protege:/srv/webprotege
    ports:
      - "${WEBPROTEGE_PORT}:8080"
EOF
}

write_scripts() {
  log "生成脚本"

  cat > "${PKG_DIR}/scripts/deploy.sh" <<EOF
#!/usr/bin/env bash
set -euo pipefail
ROOT="\$(cd "\$(dirname "\$0")/.." && pwd)"
cd "\$ROOT"

WEBPROTEGE_PORT="${WEBPROTEGE_PORT}"
TAR_NAME="${TAR_NAME}"
BUILD_IMAGE="${BUILD_IMAGE}"

sudo ufw allow "\${WEBPROTEGE_PORT}/tcp" || true
mkdir -p .protegedata/protege .protegedata/mongodb

docker load -i "\${TAR_NAME}"
docker compose up -d
docker compose ps
EOF

  cat > "${PKG_DIR}/scripts/start.sh" <<'EOF'
#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
docker compose up -d
EOF

  cat > "${PKG_DIR}/scripts/stop.sh" <<'EOF'
#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
docker compose down
EOF

  cat > "${PKG_DIR}/scripts/status.sh" <<'EOF'
#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
docker compose ps
docker compose logs --tail=100 webprotege
EOF

  chmod +x "${PKG_DIR}/scripts/"*.sh
}

write_checksums() {
  log "生成校验文件"
  (
    cd "${PKG_DIR}"
    sha256sum docker-compose.yml scripts/*.sh "${TAR_NAME}" > SHA256SUMS
  )
}

pack_release() {
  log "打包发布目录"
  tar -czf "${OUT_TGZ}" -C "${SCRIPT_DIR}" "${PKG_DIR##*/}"
}

build_stage() {
  log "[build] Maven 打包"
  mvn -f "${PROJECT_ROOT}/pom.xml" clean package -DskipTests

  log "[build] Docker 构建镜像: ${BUILD_IMAGE}"
  docker build -f "${PROJECT_ROOT}/Dockerfile" -t "${BUILD_IMAGE}" "${PROJECT_ROOT}"

  log "[build] 导出镜像: ${BUILD_TAR_PATH}"
  docker save -o "${BUILD_TAR_PATH}" "${BUILD_IMAGE}"

  prepare_package_dir

  log "复制镜像tar到发布目录"
  cp "${BUILD_TAR_PATH}" "${PKG_DIR}/${TAR_NAME}"

  write_compose_file
  write_scripts
  write_checksums
  pack_release

  log "[build] 完成: ${OUT_TGZ}"
}

upload_stage() {
  if [[ ! -f "${OUT_TGZ}" ]]; then
    echo "错误: 未找到发布包 ${OUT_TGZ}，请先执行 build/all"
    exit 1
  fi

  log "[upload] 远端创建目录: ${REMOTE_BASE_DIR}"
  ssh "${REMOTE_USER}@${REMOTE_HOST}" bash -s -- "${REMOTE_BASE_DIR}" <<'EOF'
set -euo pipefail
mkdir -p "$1"
EOF

  log "[upload] 上传包到远端"
  scp "${OUT_TGZ}" "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_BASE_DIR}/"

  log "[upload] 完成"
}

deploy_stage() {
  local remote_tgz
  local remote_folder
  remote_tgz="${REMOTE_BASE_DIR}/${OUT_TGZ##*/}"
  remote_folder="${PKG_DIR##*/}"

  log "[deploy] 远端解压并部署"
  ssh "${REMOTE_USER}@${REMOTE_HOST}" bash -s -- "${REMOTE_BASE_DIR}" "${REMOTE_DEPLOY_DIR}" "${remote_tgz}" "${remote_folder}" <<'EOF'
set -euo pipefail
remote_base_dir="$1"
remote_deploy_dir="$2"
remote_tgz="$3"
remote_folder="$4"

cd "$remote_base_dir"
if [[ ! -f "$remote_tgz" ]]; then
  echo "错误: 远端发布包不存在: $remote_tgz"
  exit 1
fi

tar -xzf "$remote_tgz"
staged_dir="$remote_base_dir/$remote_folder"
if [[ ! -d "$staged_dir" ]]; then
  echo "错误: 解压后未找到目录: $staged_dir"
  exit 1
fi

if [[ "$remote_deploy_dir" != "$staged_dir" ]]; then
  mkdir -p "$(dirname "$remote_deploy_dir")"
  rm -rf "$remote_deploy_dir"
  mv "$staged_dir" "$remote_deploy_dir"
fi
cd "$remote_deploy_dir"
./scripts/deploy.sh
EOF

  log "[deploy] 完成"
}

validate_remote_path() {
  local value="$1"
  local label="$2"
  if [[ "$value" != /* || "$value" == *".."* || ! "$value" =~ ^[A-Za-z0-9._/@-]+$ ]]; then
    echo "错误: ${label} 路径不安全: ${value}"
    exit 1
  fi
}

validate_deploy_path() {
  local base_dir="$1"
  local deploy_dir="$2"

  base_dir="${base_dir%/}"
  deploy_dir="${deploy_dir%/}"

  if [[ "$deploy_dir" == "$base_dir" || "$deploy_dir" != "$base_dir/"* ]]; then
    echo "错误: REMOTE_DEPLOY_DIR 必须位于 REMOTE_BASE_DIR 下"
    exit 1
  fi
}

require_explicit_version_for_remote_actions() {
  case "${ACTION}" in
    upload|deploy)
      if [[ "${VERSION_EXPLICIT}" != "true" ]]; then
        echo "错误: 执行 ${ACTION} 时必须显式指定 version，例如: $0 20260524-120000 ${ACTION}"
        exit 1
      fi
      ;;
  esac
}

main() {
  validate_remote_path "${REMOTE_BASE_DIR}" "REMOTE_BASE_DIR"
  validate_remote_path "${REMOTE_DEPLOY_DIR}" "REMOTE_DEPLOY_DIR"
  validate_deploy_path "${REMOTE_BASE_DIR}" "${REMOTE_DEPLOY_DIR}"
  require_explicit_version_for_remote_actions

  case "${ACTION}" in
    all)
      build_stage
      upload_stage
      deploy_stage
      ;;
    build)
      build_stage
      ;;
    upload)
      upload_stage
      ;;
    deploy)
      deploy_stage
      ;;
    -h|--help|help)
      usage
      ;;
    *)
      echo "未知 action: ${ACTION}"
      usage
      exit 1
      ;;
  esac
}

main "$@"
