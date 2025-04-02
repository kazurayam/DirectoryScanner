package com.kazurayam.ant.types.selectors;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SelectorUtilsTest {

    @Test
    public void test_match_no_star() {
        assertTrue(SelectorUtils.match("main", "main"), "pattern='main', str='main'");
    }

    @Test
    public void test_match_a_star_only() {
        assertTrue(SelectorUtils.match("*", "Foo.class"), "pattern='*', str='Foo.class'");
    }

    @Test
    public void test_match_leading_star() {
        assertTrue(SelectorUtils.match("*.class", "Foo.class"), "pattern='*.class', str='Foo.class'");
    }

    @Test
    public void test_match_question() {
        assertTrue(SelectorUtils.match("ma??", "main"),"pattern='ma??', str='main");
    }

    /* allStars method is declared private
    @Test
    public void test_allStars_falsy() {
        assertFalse(SelectorUtils.allStars("foo".toCharArray(), 0, "foo".length() - 1));
    }

    @Test
    public void test_allStars_truthy() {
        assertTrue(SelectorUtils.allStars("*".toCharArray(), 0, "*".length() - 1));
        assertTrue(SelectorUtils.allStars("**".toCharArray(), 0, "**".length() - 1));
        assertTrue(SelectorUtils.allStars("***".toCharArray(), 0, "***".length() - 1));
    }
     */

    /* different method is declared private
    @Test
    public void test_different_truthy() {
        assertTrue(SelectorUtils.different(true, 'a', 'b'));
        assertTrue(SelectorUtils.different(false, 'a', 'b'));
    }

    @Test
    public void test_different_falsy() {
        assertFalse(SelectorUtils.different(true, 'a', 'a'));
        assertFalse(SelectorUtils.different(false, 'a', 'A'));
    }
    */
}
