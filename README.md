# cumlinelength-java

Minimal example of a pipe-friendly Java command-line app.

Our app reads line by line from stdin until EOF. For each successive line it reads, it immediately responds with an updated cumulative char count.


## Environment

For convenience, we choose the oldest Java SE LTS that is available through SDKMAN.

```bash
❯ $JAVA_HOME/bin/java -version
openjdk version "1.8.0_492"
OpenJDK Runtime Environment (Zulu 8.94.0.17-CA-macos-aarch64) (build 1.8.0_492-b09)
OpenJDK 64-Bit Server VM (Zulu 8.94.0.17-CA-macos-aarch64) (build 25.492-b09, mixed mode)
```

## Interactive use

In interactive use with finite input, our app behaves as required.
We test with inputs of length zero, one, and several lines.

```bash
❯ $JAVA_HOME/bin/java CumLineLength
EOF
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
EOF
```


## Initial problem when used in a UNIX pipeline

Our app works as part of a UNIX pipeline for finite input:

```bash
❯ $JAVA_HOME/bin/javac CumLineLength.java
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
[hangs]
```

The initial solution, however, hangs in this scenario, and its JVM process continues running with high CPU utilization.
The reason is Java's default behavior of ignoring the IO error that occurs when the downstream process closes its input stream and our app can no longer write to it.


## What not to do: trap `SIGPIPE`

In C-style UNIX programs, a common approach is to rely on or trap `SIGPIPE`.
For this Java program, that is the wrong fix: JVM-level signal handling is non-portable and brittle, and it bypasses normal Java I/O error handling.
Instead, detect the failed write directly via Java output APIs.


## Robust handling of downstream truncation

To fix this behavior, we need to check programmatically whether the most recent attempt to write to the input of the downstream process was successful.

```java
if (System.out.checkError()) break;
```

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
