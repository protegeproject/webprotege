---
name: generate-apikey
description: >-
  Generate a WebProtégé API key for user admin via docker CLI
  (generate-api-key). Use when the user asks to create/generate an API key,
  ApiKey, Authorization ApiKey, or 生成 API Key for local WebProtégé
  (container webprotege, port 5000).
---

# Generate WebProtégé API Key

为本地 Docker 部署的 WebProtégé 生成 API Key。默认用户名固定为 **admin**。

App: `http://192.168.3.230:5000/`（或 `http://localhost:5000`）。  
Container: `webprotege`。CLI: `/webprotege-cli.jar`。

## Prerequisites

- 容器 `webprotege` 与 `webprotege-mongodb` 已运行
- 用户 `admin` 已存在（若无：`docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account`）

## Workflow

```
Progress:
- [ ] 1. Confirm container is up
- [ ] 2. Run generate script (user=admin)
- [ ] 3. Show key + usage example to user
```

Prefer the script (handles interactive `System.console()` via PTY):

```bash
.cursor/skills/generate-apikey/scripts/generate-apikey.sh
```

Optional purpose (default `integration`):

```bash
.cursor/skills/generate-apikey/scripts/generate-apikey.sh "integration-runtime-data"
```

Env overrides:

| Variable | Default | Meaning |
|----------|---------|---------|
| `WEBPROTEGE_CONTAINER` | `webprotege` | Docker container name |
| `WEBPROTEGE_APIKEY_USER` | `admin` | Username（本 skill 固定默认 admin） |
| `WEBPROTEGE_APIKEY_PURPOSE` | `integration` | Key purpose metadata |

Shell needs Docker permissions (`all` if sandbox blocks the daemon).

## Manual fallback

```bash
docker exec -it webprotege java -jar /webprotege-cli.jar generate-api-key
```

Prompt answers:

1. user name → `admin`
2. purpose → e.g. `integration`

## After generation

1. Print the plaintext key once; tell the user it cannot be recovered.
2. Do **not** commit the key to git or write it into tracked files unless the user explicitly asks.
3. Usage header:

```http
Authorization: ApiKey <generated-key>
```

Example against local integration API:

```bash
curl -H "Authorization: ApiKey <generated-key>" \
  "http://192.168.3.230:5000/data/integration/projects/<projectId>/individuals/runtime-data"
```

## Troubleshooting

| Symptom | Action |
|---------|--------|
| container not found / not running | `docker compose up -d` from repo root |
| Mongo / connection errors | Ensure `webprotege-mongodb` is healthy; CLI must run **inside** `webprotege` |
| NullPointerException on console | Use the script (PTY), not bare `docker exec -i` without TTY |
| 401 on REST calls | Wrong key, guest user, or key for a different account |
