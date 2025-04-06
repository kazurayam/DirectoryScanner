# Monk DirectoryScanner

The [DirectoryScanner](https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html) is the core of the Ant build tool. Its "pattern" language is super useful. A concise pattern like "`**/main/*.class`" can express a set of files/folders under a base directory flexibly. Its expressiveness is amazing.

Since 1980s as a Java programmer, I often desired to use the `DirectoryScanner` in my tiny Java/Groovy projects. I don't need the full features of Ant. I don't need the `build.xml`. I just want to use the `DirectoryScanner` class to read/write local files while including/excluding files by the pattern language like "`**/*.java`". But I couldn't. Why? The `DirectoryScanner` class is bundled in the [jar of Ant Core](https://mvnrepository.com/artifact/org.apache.ant/ant), which is huge in size (2.2 Mega Bytes) and has quite a lot of external dependencies. If I make my project dependent on the Ant Core jar, it turns my project heavy and difficult to manage.

One day in April 2025, I looked into the [Ant's source code](https://github.com/apache/ant/blob/master/src/main/org/apache/tools/ant/DirectoryScanner.java) and found I can rewrite it to make super-fit. Let it be ascetic, cut-off unnecessary "nice-to-haves", let it stand alone. I made a copy of Ant's `DirectoryScanner` and a few related java classes; give a package name `com.kazurayam.ant`; and partially rewrote to make it disconnected from the heavy Ant code universe.

So I named this code set "Monk Directory Scanner". This library will be provided in a small jar file. The jar depends only on the Java Core library. It has no other external dependencies.



