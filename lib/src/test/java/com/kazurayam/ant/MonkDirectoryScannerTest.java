package com.kazurayam.ant;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * The simplest demonstration how to use the Monk DirectoryScanner
 * through Java API without build.xml.
 */
public class MonkDirectoryScannerTest {

    @BeforeTest
    public void setUp() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

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
