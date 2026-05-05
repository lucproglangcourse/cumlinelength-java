# cumlinelength-java

Minimal example of a pipe-friendly Java command-line app.

The app reads lines from stdin one at a time and, after each line, prints the total number of characters read so far. It stops when it reaches EOF (end of input).


## Environment

For convenience, we choose the oldest Java SE LTS that is available through SDKMAN.

```bash
❯ $JAVA_HOME/bin/java -version
openjdk version "1.8.0_492"
OpenJDK Runtime Environment (Zulu 8.94.0.17-CA-macos-aarch64) (build 1.8.0_492-b09)
OpenJDK 64-Bit Server VM (Zulu 8.94.0.17-CA-macos-aarch64) (build 25.492-b09, mixed mode)
```

## Building

```bash
❯ $JAVA_HOME/bin/javac CumLineLength.java
```

## Interactive use

In interactive use with finite input, our app behaves as required.
We test with inputs of length zero, one, and several lines.

```bash
❯ $JAVA_HOME/bin/java CumLineLength
^D
```

```bash
❯ $JAVA_HOME/bin/java CumLineLength
asdf
4
```

```bash
❯ $JAVA_HOME/bin/java CumLineLength
hello
5
world
10
what
14
up
16
^D
```


## Initial problem: hanging when downstream truncates output

Our app works as part of a UNIX pipeline for finite input:

```bash
❯ yes | head | $JAVA_HOME/bin/java CumLineLength
1
2
3
4
5
6
7
8
9
10
❯ 
```

If the input is infinite, the output of our app is also infinite. 
In a typical UNIX pipeline, we select a finite prefix downstream of the infinite output.

```bash
❯ yes | $JAVA_HOME/bin/java CumLineLength | head
1
2
3
4
5
6
7
8
9
10
```

The initial solution, however, hangs: the prompt does not return, and the JVM process continues running with high CPU utilization.
The reason is Java's default behavior of ignoring the I/O error that occurs when the downstream process exits, closing its end of the pipe, so our app can no longer write to it.


## What not to do: trap `SIGPIPE`

In C-style UNIX programs, a common approach is to rely on or trap `SIGPIPE`.
For this Java program, that is the wrong fix: JVM-level signal handling is non-portable and brittle, and it bypasses normal Java I/O error handling.
Instead, detect the failed write directly via Java output APIs.


## Robust handling of downstream truncation

To fix this behavior, we check whether `System.out` has encountered any write error — for example, a broken pipe caused by the downstream process exiting.

```java
if (System.out.checkError()) break;
```

We use `break` rather than `System.exit(1)` because a downstream process consuming only part of our output (e.g. `head`) is normal, not an error.
Exiting with a non-zero status would signal failure to the shell and break scripts that check exit codes.

Now the application behaves as required.

```bash
❯ yes | $JAVA_HOME/bin/java CumLineLength | head
1
2
3
4
5
6
7
8
9
10
❯
```
