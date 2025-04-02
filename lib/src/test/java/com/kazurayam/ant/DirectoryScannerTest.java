package com.kazurayam.ant;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.kazurayam.unittest.TestOutputOrganizer;

public class DirectoryScannerTest {

    private static final Logger log = LoggerFactory.getLogger(DirectoryScannerTest.class);
    private static final TestOutputOrganizer too =
            new TestOutputOrganizer.Builder(DirectoryScannerTest.class)
                    .outputDirectoryRelativeToProject("build/tmp/testOutput")
                    .subOutputDirectory(DirectoryScannerTest.class)
                    .build();

    @BeforeTest
    public void setUp() {}

    @BeforeMethod
    public void beforeMethod() {}

    @Test
    public void test_smoke() {
        DirectoryScanner ds = new DirectoryScanner();
        ds.setBasedir("src/main/java");
        ds.setIncludes(new String[]{"**/*.java"});
        ds.scan();
        assertEquals(ds.getIncludedFilesCount(), 10);
    }


}
