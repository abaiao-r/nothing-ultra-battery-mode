#!/usr/bin/env python3
"""PreToolUse guard for Agent GREEN: deny any write to a test/ATP source.

The inverse of the Agent RED guard, so Agent GREEN can never edit, create,
or delete a test/feature file to make its own life easier.

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
SHELL_MUTATE_RE = re.compile(r"(>>?|\btee\b|\bcp\b|\bmv\b|\brm\b|sed\s+-i|\bdd\b)")
SHELL_TEST_RE = re.compile(r"(src/(test|androidTest|testShared|testFixtures)|(Test|IT)\.kt|\.feature)")


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
            emit("deny",
                 "Agent GREEN must not modify tests or ATP scenarios. They are frozen input. "
                 f"Refused write to test/ATP source: {path}. "
                 "If the test is wrong, hand it back to Agent RED.")
        emit("allow", f"Production write permitted: {path or '<unknown>'}")

    if tool == "run_in_terminal":
        command = params.get("command") or params.get("cmd") or ""
        if SHELL_MUTATE_RE.search(command) and SHELL_TEST_RE.search(command):
            emit("deny", "Agent GREEN must not edit or delete tests/ATP scenarios from the shell.")
        emit("allow", "Terminal command permitted (no test-source mutation detected).")

    emit("allow", "Non-write tool permitted.")


if __name__ == "__main__":
    main()
