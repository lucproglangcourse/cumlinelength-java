#!/usr/bin/env bash
# Tests interactive/streaming behavior: the app must respond to each input line
# immediately, without buffering. Requires bash 4+ for named coprocesses.
set -euo pipefail

if [[ "${BASH_VERSINFO[0]}" -lt 4 ]]; then
    echo "This script requires bash 4 or later (found: $BASH_VERSION)" >&2
    exit 1
fi

JAVA="${JAVA_HOME}/bin/java"
TIMEOUT=5  # seconds to wait for each response (generous to allow JVM startup)
PASS=0
FAIL=0

# Launch the app as a coprocess with connected stdin/stdout pipes
coproc PROC { "$JAVA" CumLineLength; }

check() {
    local input="$1"
    local expected="$2"

    printf '%s\n' "$input" >&"${PROC[1]}"

    local actual
    if IFS= read -t "$TIMEOUT" -u "${PROC[0]}" actual; then
        if [[ "$actual" == "$expected" ]]; then
            echo "PASS: '$input' -> $actual"
            ((++PASS))
        else
            echo "FAIL: '$input': expected $expected, got $actual"
            ((++FAIL))
        fi
    else
        echo "FAIL: '$input': no response within ${TIMEOUT}s (output may be buffered)"
        ((++FAIL))
    fi
}

check "hello" "5"
check "world" "10"
check "what"  "14"
check "up"    "16"

# Close the write end to signal EOF to the app
exec {PROC[1]}>&-

wait "$PROC_PID"

echo ""
echo "Results: $PASS passed, $FAIL failed"
[[ $FAIL -eq 0 ]]
