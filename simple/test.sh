#!/usr/bin/env bash
set -euo pipefail

JAVA="${JAVA_HOME}/bin/java"
PASS=0
FAIL=0

run_test() {
    local description="$1"
    local input="$2"
    local expected="$3"

    local actual
    actual=$(printf '%s' "$input" | "$JAVA" CumLineLength)

    if [[ "$actual" == "$expected" ]]; then
        echo "PASS: $description"
        ((++PASS))
    else
        echo "FAIL: $description"
        echo "  expected: $(printf '%q' "$expected")"
        echo "  actual:   $(printf '%q' "$actual")"
        ((++FAIL))
    fi
}

# Test 1: empty input — no output
run_test "empty input" "" ""

# Test 2: single line
run_test "single line" "asdf
" "4"

# Test 3: multiple lines
run_test "multiple lines" "hello
world
what
up
" "5
10
14
16"

echo ""
echo "Results: $PASS passed, $FAIL failed"
[[ $FAIL -eq 0 ]]
