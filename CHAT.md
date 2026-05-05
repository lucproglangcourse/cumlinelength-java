User: Use Java 8 API. Read line by line from stdin until EOF. For each successive line read, immediately respond with updated cumulative char count.

GitHub Copilot: Read [](file:///Users/laufer/Work/teaching/cs371/cumlinelength-java/CumLineLength.java)

Replacing 3 lines with 14 lines in [](file:///Users/laufer/Work/teaching/cs371/cumlinelength-java/CumLineLength.java)


```

```
Done. Each line read from stdin adds its character count (excluding the newline) to the running total, which is printed immediately.

Made changes.

User: Good start! But not pipe-friendly:

yes | $JAVA_HOME/bin/java CumLineLength | head

hangs, i.e., doesn't terminate orderly and return to the shell prompt.

GitHub Copilot: Read [](file:///Users/laufer/Work/teaching/cs371/cumlinelength-java/CumLineLength.java)

The problem is that `PrintStream` (`System.out`) silently swallows broken-pipe errors instead of throwing. When `head` closes the pipe, `System.out.println` fails silently and the loop keeps running. The fix is to check `System.out.checkError()` after each write. 

Replacing 5 lines with 6 lines in [](file:///Users/laufer/Work/teaching/cs371/cumlinelength-java/CumLineLength.java)


```

```
`PrintStream.checkError()` flushes and returns `true` if any write has failed (including a broken pipe), so the loop exits cleanly when the downstream consumer (`head`) closes its end of the pipe.

Made changes.