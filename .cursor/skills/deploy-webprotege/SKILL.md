---
name: deploy-webprotege
description: >-
  Local-machine packaging, compilation, and deployment of WebProtégé: full Maven
  package (with GWT), rebuild protegeproject/webprotege Docker image from the
  frontend-inclusive WAR, then recreate and start containers on this host. Use
  when the user asks to deploy WebProtégé locally, 本机打包/编译/部署, rebuild
  the Docker image, restart localhost:5000, fix a blank homepage after skip-gwt
  builds, or mentions 本机部署 / docker compose / mvn clean package with GWT.
  Not for remote servers or CI/CD.
---

# Deploy WebProtégé（本机打包编译部署）

在本机进行打包、编译、部署：于当前仓库所在机器执行 Maven（含 GWT）、重建本地
Docker 镜像，并用 `docker compose` 启动本机容器。不部署到远程主机或云环境。

App URL: `http://localhost:5000`（`docker-compose.yml` 映射 `5000:8080`）。
Version: `5.0.0-SNAPSHOT`。

## Required operations (do in order)

1. 完整执行 mvn clean package（含 GWT）
2. 用含前端的 WAR 重建 protegeproject/webprotege 镜像
3. 重建并启动容器

## Workflow checklist

```
Progress:
- [ ] 1. mvn clean package (with GWT)
- [ ] 2. Rebuild protegeproject/webprotege image from WAR
- [ ] 3. Recreate and start containers
- [ ] 4. Verify homepage assets
```

Prefer the script when possible:

```bash
.cursor/skills/deploy-webprotege/scripts/deploy.sh
```

Optional flags: `--skip-tests` (default), `--run-tests`, `--maven-only`, `--docker-only`.

Otherwise run the steps below manually. Shell needs unrestricted permissions (`all`) so Maven can write `~/.m2` and Docker can access the daemon.

### 1. 完整执行 mvn clean package（含 GWT）

From repo root:

```bash
mvn clean package -DskipTests
```

**Hard rules:**

- Do **not** use `-P skip-gwt-compilation`. That profile omits
  `webprotege/webprotege.nocache.js` and makes `http://localhost:5000` a blank page.
- GWT compile takes ~10–15 minutes; wait for `BUILD SUCCESS`.
- Confirm frontend is in the WAR:

```bash
unzip -l webprotege-server/target/webprotege-server-5.0.0-SNAPSHOT.war | grep webprotege.nocache.js
```

Expected: an entry like `webprotege/webprotege.nocache.js`. Client WAR should be tens of MB (not ~2 KB).

### 2. 用含前端的 WAR 重建 protegeproject/webprotege 镜像

Do **not** `docker build` with the repo root as context: `.protegedata/mongodb` can
block the build (`no permission to read ... WiredTiger`).

Build from a clean temp context that only contains the artifacts:

```bash
BUILD_CTX=$(mktemp -d)
cp webprotege-server/target/webprotege-server-5.0.0-SNAPSHOT.war "$BUILD_CTX/"
cp webprotege-cli/target/webprotege-cli-5.0.0-SNAPSHOT.jar "$BUILD_CTX/"
cat > "$BUILD_CTX/Dockerfile" <<'EOF'
FROM tomcat:8-jre11-slim
RUN rm -rf /usr/local/tomcat/webapps/* \
    && mkdir -p /srv/webprotege \
    && mkdir -p /usr/local/tomcat/webapps/ROOT
WORKDIR /usr/local/tomcat/webapps/ROOT
COPY webprotege-cli-5.0.0-SNAPSHOT.jar /webprotege-cli.jar
COPY webprotege-server-5.0.0-SNAPSHOT.war ./webprotege.war
RUN unzip -q webprotege.war && rm webprotege.war
EOF
docker build -t protegeproject/webprotege:latest -t protegeproject/webprotege:local "$BUILD_CTX"
rm -rf "$BUILD_CTX"
```

Verify the image includes GWT:

```bash
docker run --rm protegeproject/webprotege:latest \
  test -f /usr/local/tomcat/webapps/ROOT/webprotege/webprotege.nocache.js
```

### 3. 重建并启动容器

```bash
docker compose up -d --force-recreate webprotege
```

Mongo (`webprotege-mongodb`) stays up unless the user asks to recreate everything:
`docker compose up -d --force-recreate`.

Wait until logs show `WebProtege initialization complete`.

### 4. Verify

```bash
curl -sI http://127.0.0.1:5000/webprotege/webprotege.nocache.js | head -5
docker ps --filter name=webprotege --format 'table {{.Names}}\t{{.Status}}\t{{.Ports}}'
```

`nocache.js` must be **HTTP 200**. If 404, the image/WAR still lacks GWT — redo steps 1–3 without `skip-gwt`.

Tell the user to hard-refresh the browser (Ctrl+Shift+R).

## Common failure modes

| Symptom | Cause | Fix |
|---------|--------|-----|
| Blank homepage | Built with `-P skip-gwt-compilation` | Full GWT package + image rebuild |
| `nocache.js` 404 | WAR/image missing GWT output | Same as above |
| Maven `LocalRepositoryNotAccessibleException` / `/root/.m2` | Sandboxed shell | Re-run with `required_permissions: ["all"]` |
| Docker `no permission ... WiredTiger` | Build context includes `.protegedata` | Use temp-context build (step 2 / script) |
| Port not listening | Compose not started | `docker compose up -d` |

## Out of scope

- Remote / SSH / 服务器部署（本 skill 仅本机）
- Creating admin accounts (`docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account`) unless the user asks
- Pushing images to a registry
- Production / K8s / CI/CD deploy
