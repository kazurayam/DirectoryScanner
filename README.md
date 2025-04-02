# Monk DirectoryScanner

The [DirectoryScanner](https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html) is the core part of Ant build tool. I wanted to use the DirectoryScanner in my Java/Groovy projects but abandoned the idea because the DirectoryScanner is bundled in the [jar of Ant Core](https://mvnrepository.com/artifact/org.apache.ant/ant). The jar is very big in size (2.2 Mega Bytes), has quite a lot of external dependencies. I could not use the Ant Core jar as a component of my own tiny projects.

However, I looked into the [source code](https://github.com/apache/ant/blob/master/src/main/org/apache/tools/ant/DirectoryScanner.java) and found it is possible to make it a monk. The class formally known as "Ant's DirectoryScanner" now stands on its own; it requires no external dependencies indirectly linked to. It is distributable in a small jar file, provides the Java API but requires not build.xml.


