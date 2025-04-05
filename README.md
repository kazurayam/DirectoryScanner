# Monk DirectoryScanner

The [DirectoryScanner](https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html) is the core class of the Ant build tool. Amongst others, the "pattern" language (e.g. "`**/main/*.class`") that match files/folders under a base directory is super useful.

For many years of my development career since 1980s, I wanted to use the `DirectoryScanner` in my tiny Java/Groovy projects. But I couldn't. Why? The `DirectoryScanner` class is bundled in the [jar of Ant Core](https://mvnrepository.com/artifact/org.apache.ant/ant). The jar is huge in size (2.2 Mega Bytes), and has quite a lot of external dependencies. If I make my project dependent on the Ant Core jar, it makes my project too heavy and difficult to manage. I don't need the full features of Ant, I don't need the `build.xml`. I just want to use the `DirectoryScanner` class to read/write local files while including/excluding files by the pattern language `**/*.java`.


One day in April 2025, I looked into the [Ant's source code](https://github.com/apache/ant/blob/master/src/main/org/apache/tools/ant/DirectoryScanner.java) and found it is possible to make super-fit. Let it be ascetic, cut-off unnecessary "nice-to-haves", let it stand alone. Here I named it "Monk Directory Scanner". This library will be provide a super small jar file, that depends on Java Core lib. It has no external dependencies.



