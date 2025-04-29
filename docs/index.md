- Table of contents
{:toc}

-   link to the [Javadoc](https://kazurayam.github.io/MonkDirectoryScanner/api)

-   link to the [repository](https://www.github.com/kazurayam/MonkDirectoryScanner)

-   link to the [Releases](https://www.github.com/kazurayam/MonkDirectoryScanner/releases) page

-   link to the [Maven Central](https://mvnrepository.com/artifact/com.kazurayam/monk-directory-scanner)

# Monk Directory Scanner

The [DirectoryScanner](https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html) is the core of the Ant build tool. Its "pattern" language is super useful. A concise pattern like “**/main/**/\*.class” can express a set of files/folders under a base directory flexibly. Its expressiveness is amazing.

See [Ant doc](https://ant.apache.org/manual/dirtasks.html#patterns) for the examples of pattern language.

Since 1980s as a Java programmer, I often desired to use the `DirectoryScanner` in my tiny Java/Groovy projects. I didn’t need the full features of Ant. I didn’t need the `build.xml`. I just wanted to use the `DirectoryScanner` class to read/write local files while including/excluding files by the pattern language like “**\*/**.java”. But I couldn’t. Why? The `DirectoryScanner` class is bundled in the [jar of Ant Core](https://mvnrepository.com/artifact/org.apache.ant/ant), which is huge in size (2.2 Mega Bytes) and has quite a lot of external dependencies. If I make my project dependent on the Ant Core jar, it turns my project needlessly heavy, difficult to manage.

One day in April 2025, I looked into the [Ant’s source code](https://github.com/apache/ant/blob/master/src/main/org/apache/tools/ant/DirectoryScanner.java) and realized that I can rewrite it super-fit. Let it be ascetic. I would cut-off the unnecessary "nice-to-haves". I would be able to make the `DirectoryScanner` stand alone.

I made a copy of Ant’s `DirectoryScanner` source and a few related java classes. I assigned a new package name `com.kazurayam.ant` in order to distinguish it from the original. I partially rewrote the code to isolate the `DirectoryScanner` class from the vast code universe of Ant.

So I named this code set as "Monk Directory Scanner". This library will be provided in a small jar file. The jar depends only on the Java Core library. It has no external dependencies.

# Example

    package com.kazurayam.ant;

    import org.testng.annotations.Test;

    import static org.testng.Assert.assertEquals;

    public class DirectoryScannerTest {

        @Test
        public void test_monk() {
            DirectoryScanner ds = new DirectoryScanner();
            ds.setBasedir("build/classes");
            ds.setIncludes(new String[]{"**/*.class"});
            //ds.setExcludes(new String[]{"**/test/**"});
            ds.scan();
            assertEquals(ds.getIncludedFilesCount(), 15);
        }
    }

# Javadoc 

- [Javadoc](https://kazurayam.github.io/MonkDirectoryScanner/api/)

# Docs

- [docs](https://kazurayam.github.io/MonkDirectoryScanner/)