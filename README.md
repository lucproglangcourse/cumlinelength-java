
```bash
❯ $JAVA_HOME/bin/java -version
openjdk version "1.8.0_492"
OpenJDK Runtime Environment (Zulu 8.94.0.17-CA-macos-aarch64) (build 1.8.0_492-b09)
OpenJDK 64-Bit Server VM (Zulu 8.94.0.17-CA-macos-aarch64) (build 25.492-b09, mixed mode)
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
```





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

