# Monk DirectoryScanner

The [DirectoryScanner](https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html) is the core of the Ant build tool. Its "pattern" language is super useful. A concise pattern like "`**/main/**/*.class`" can express a set of files/folders under a base directory flexibly. Its expressiveness is amazing.

Since 1980s as a Java programmer, I often desired to use the `DirectoryScanner` in my tiny Java/Groovy projects. I didn't need the full features of Ant. I didn't need the `build.xml`. I just wanted to use the `DirectoryScanner` class to read/write local files while including/excluding files by the pattern language like "`**/*.java`". But I couldn't. Why? The `DirectoryScanner` class is bundled in the [jar of Ant Core](https://mvnrepository.com/artifact/org.apache.ant/ant), which is huge in size (2.2 Mega Bytes) and has quite a lot of external dependencies. If I make my project dependent on the Ant Core jar, it turns my project needlessly heavy, difficult to manage.

One day in April 2025, I looked into the [Ant's source code](https://github.com/apache/ant/blob/master/src/main/org/apache/tools/ant/DirectoryScanner.java) and realized that I can rewrite it super-fit. Let it be ascetic. I would cut-off the unnecessary "nice-to-haves". I would be able to make the `DirectoryScanner` stand alone. 

I made a copy of Ant's `DirectoryScanner` source and a few related java classes. I assigned a new package name `com.kazurayam.ant` in order to distinguish it from the original. I partially rewrote the code to isolate the `DirectoryScanner` class from the vast code universe of Ant.

So I named this code set as "Monk Directory Scanner". This library will be provided in a small jar file. The jar depends only on the Java Core library. It has no external dependencies.



