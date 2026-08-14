#!/usr/bin/env bash
# Generate a WebProtégé API key for user admin (interactive CLI via PTY).
set -euo pipefail

CONTAINER="${WEBPROTEGE_CONTAINER:-webprotege}"
USER_NAME="${WEBPROTEGE_APIKEY_USER:-admin}"
PURPOSE="${1:-${WEBPROTEGE_APIKEY_PURPOSE:-integration}}"

if ! command -v docker >/dev/null 2>&1; then
  echo "error: docker not found" >&2
  exit 1
fi

if ! docker inspect -f '{{.State.Running}}' "$CONTAINER" 2>/dev/null | grep -qx true; then
  echo "error: container '$CONTAINER' is not running" >&2
  echo "hint: from repo root run: docker compose up -d" >&2
  exit 1
fi

export CONTAINER USER_NAME PURPOSE

# Java CLI uses System.console(); needs a real TTY. Drive prompts via Python PTY.
python3 - <<'PY'
import os
import pty
import re
import select
import sys
import time

container = os.environ["CONTAINER"]
username = os.environ["USER_NAME"]
purpose = os.environ["PURPOSE"]
cmd = [
    "docker", "exec", "-i", container,
    "java", "-jar", "/webprotege-cli.jar", "generate-api-key",
]

master, slave = pty.openpty()
pid = os.fork()
if pid == 0:
    os.close(master)
    os.setsid()
    os.dup2(slave, 0)
    os.dup2(slave, 1)
    os.dup2(slave, 2)
    if slave > 2:
        os.close(slave)
    os.execvp(cmd[0], cmd)

os.close(slave)
buf = b""
sent_user = False
sent_purpose = False
deadline = time.time() + 90
exit_status = None

try:
    while time.time() < deadline:
        ready, _, _ = select.select([master], [], [], 0.5)
        if master in ready:
            try:
                chunk = os.read(master, 4096)
            except OSError:
                break
            if not chunk:
                break
            buf += chunk
            sys.stdout.write(chunk.decode(errors="replace"))
            sys.stdout.flush()
            lower = buf.decode(errors="replace").lower()
            if (not sent_user) and "user name" in lower:
                os.write(master, (username + "\n").encode())
                sent_user = True
            elif sent_user and (not sent_purpose) and "purpose" in lower:
                os.write(master, (purpose + "\n").encode())
                sent_purpose = True

        wpid, status = os.waitpid(pid, os.WNOHANG)
        if wpid != 0:
            exit_status = status
            drain_deadline = time.time() + 2
            while time.time() < drain_deadline:
                ready, _, _ = select.select([master], [], [], 0.2)
                if master not in ready:
                    break
                try:
                    chunk = os.read(master, 4096)
                except OSError:
                    break
                if not chunk:
                    break
                buf += chunk
                sys.stdout.write(chunk.decode(errors="replace"))
                sys.stdout.flush()
            break
finally:
    try:
        os.close(master)
    except OSError:
        pass

text = buf.decode(errors="replace")
match = re.search(
    r"Generated API key:\s*\n\s*\n\s+([A-Za-z0-9_-]+)",
    text,
)
print()
if match:
    key = match.group(1)
    print("---")
    print(f"user: {username}")
    print(f"purpose: {purpose}")
    print(f"api_key: {key}")
    print("---")
    print("Keep this key safe. It cannot be recovered.")
    print("Header: Authorization: ApiKey " + key)
else:
    print("error: failed to parse generated API key from CLI output", file=sys.stderr)
    sys.exit(1)

if exit_status is None:
    os.kill(pid, 9)
    os.waitpid(pid, 0)
    sys.exit(1)

if os.WIFEXITED(exit_status) and os.WEXITSTATUS(exit_status) != 0:
    sys.exit(os.WEXITSTATUS(exit_status))
if os.WIFSIGNALED(exit_status):
    sys.exit(1)
PY
