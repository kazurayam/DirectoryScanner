package com.kazurayam.file;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DirectoryScannerTest {

    @Test
    public void test_match_no_star() {
        assertTrue(DirectoryScanner.match("main", "main"), "pattern='main', str='main'");
    }

    @Test
    public void test_match_a_star_only() {
        assertTrue(DirectoryScanner.match("*", "Foo.class"), "pattern='*', str='Foo.class'");
    }

    @Test
    public void test_match_leading_star() {
        assertTrue(DirectoryScanner.match("*.class", "Foo.class"), "pattern='*.class', str='Foo.class'");
    }

    @Test
    public void test_match_question() {
        assertTrue(DirectoryScanner.match("ma??", "main"),"pattern='ma??', str='main");
    }

    @Test
    public void test_allStars_falsy() {
        assertFalse(DirectoryScanner.allStars("foo".toCharArray(), 0, "foo".length() - 1));
    }

    @Test
    public void test_allStars_truthy() {
        assertTrue(DirectoryScanner.allStars("*".toCharArray(), 0, "*".length() - 1));
        assertTrue(DirectoryScanner.allStars("**".toCharArray(), 0, "**".length() - 1));
        assertTrue(DirectoryScanner.allStars("***".toCharArray(), 0, "***".length() - 1));
    }

    @Test
    public void test_different_truthy() {
        assertTrue(DirectoryScanner.different(true, 'a', 'b'));
        assertTrue(DirectoryScanner.different(false, 'a', 'b'));
    }

    @Test
    public void test_different_falsy() {
        assertFalse(DirectoryScanner.different(true, 'a', 'a'));
        assertFalse(DirectoryScanner.different(false, 'a', 'A'));
    }

}
