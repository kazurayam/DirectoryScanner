package com.kazurayam.ant;

import com.kazurayam.unittest.TestOutputOrganizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DirectoryScannerTest {

    private static final Logger log = LoggerFactory.getLogger(MonkDirectoryScannerTest.class);
    private static final TestOutputOrganizer too =
            new TestOutputOrganizer.Builder(MonkDirectoryScannerTest.class)
                    .outputDirectoryRelativeToProject("build/tmp/testOutput")
                    .subOutputDirectory(MonkDirectoryScannerTest.class)
                    .build();

    @BeforeTest
    public void setUp() {}

    @BeforeMethod
    public void beforeMethod() {}

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
