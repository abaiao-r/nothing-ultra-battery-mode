#!/usr/bin/env python3
"""PreToolUse guard for Agent RED: only allow writes to test sources.

Denies any file write or terminal command whose target is production code
(outside src/test, src/androidTest, or *.feature/testdata support paths).

Contract:
    stdin:  PreToolUse JSON payload (tool name + input params)
    stdout: PreToolUse JSON with hookSpecificOutput.permissionDecision
    exit 0: always; the decision travels in the JSON, not the exit code
"""
from __future__ import annotations

import json
import re
import sys

TEST_PATH = re.compile(
    r"(/src/(test|androidTest|testShared|testFixtures)[/A-Za-z0-9_.-]*"
    r"|/testdata/|/testData/|\.feature$)"
)
TEST_FILE = re.compile(r"(Test|IT)\.kt$")
WRITE_TOOLS = {"create_file", "insert_edit_into_file", "replace_string_in_file"}
SHELL_WRITE_RE = re.compile(r"(>>?|\btee\b|\bcp\b|\bmv\b|sed\s+-i|\bdd\b)")


def emit(decision: str, reason: str) -> None:
    print(json.dumps({
        "hookSpecificOutput": {
            "hookEventName": "PreToolUse",
            "permissionDecision": decision,
            "permissionDecisionReason": reason,
        }
    }))
    sys.exit(0)


def is_test_path(path: str) -> bool:
    if not path:
        return False
    return bool(TEST_PATH.search(path) or TEST_FILE.search(path))


def main() -> None:
    try:
        payload = json.loads(sys.stdin.read() or "{}")
    except Exception as exc:
        emit("allow", f"Payload parse error (allowing by default): {exc}")
        return

    tool = payload.get("tool_name") or payload.get("toolName") or payload.get("name") or ""
    params = payload.get("tool_input") or payload.get("toolInput") or payload.get("input") or {}

    if tool in WRITE_TOOLS:
        path = params.get("filePath") or params.get("file_path") or params.get("path") or ""
        if is_test_path(path):
            emit("allow", f"Test/ATP source write permitted: {path}")
        emit("deny",
             "Agent RED may only write test or ATP sources "
             "(src/test, src/androidTest, *.feature, testdata). "
             f"Refused write to production path: {path or '<unknown>'}")

    if tool == "run_in_terminal":
        command = params.get("command") or params.get("cmd") or ""
        if SHELL_WRITE_RE.search(command) and "/src/main/" in command:
            emit("deny", "Agent RED must not write production code from the shell.")
        emit("allow", "Terminal command permitted (no production write detected).")

    emit("allow", "Non-write tool permitted.")


if __name__ == "__main__":
    main()
