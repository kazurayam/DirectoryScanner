package com.kazurayam.file;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Study how to use java.nio.file.Path class which was introduced in Java 1.8
 *
 * Ant was developed priort to Java 1.8 so that the DirectoryScanner of Ant does not
 * use the Path API. Too old.
 */
public class PathTest {

    @Test
    public void test_parsing_pattern_string_with_wildcard_into_a_Path() {
        String pattern = "test\\**\\*.class";
        Path p = Paths.get(normalizeSeparatorChar(pattern));
        assertFalse(p.isAbsolute(), "expected p is not absolute but was");
    }

    @Test
    public void test_parsing_pattern_string_with_wildcard_into_a_Path_and_iterate_over_it() {
        String pattern = "test\\**\\*.class";
        Path p = Paths.get(normalizeSeparatorChar(pattern));
        /*
        Iterator<Path> iter = p.iterator();
        while (iter.hasNext()) {
            Path pathElement = iter.next();
            System.out.println(pathElement.toString());
        }
        */
        p.iterator().forEachRemaining(it -> System.out.println(it));
    }

    @Test
    public void test_parsing_pattern_string_with_wildcard_into_a_List_of_String() {
        String pattern = "test\\**\\*.class";
        Path p = Paths.get(normalizeSeparatorChar(pattern));
        List<String> pathElements = new ArrayList<>();
        /*
        Iterator<Path> iter = p.iterator();
        while (iter.hasNext()) {
            Path element = iter.next();
            elements.add(element.toString());
        }
        */
        p.iterator().forEachRemaining(it -> pathElements.add(it.toString()));
        assertEquals(pathElements.size(), 3);
        assertEquals(pathElements.get(0), "test");
        assertEquals(pathElements.get(1), "**");
        assertEquals(pathElements.get(2), "*.class");
    }


    /**
     * convert '/' and '\' character into File.separatorChar
     * to make the code portable to both of UNIX and Windows
     *
     * @param pathString
     * @return
     */
    String normalizeSeparatorChar(String pathString) {
        return pathString.replace('/', File.separatorChar)
                .replace('\\', File.separatorChar);
    }
}
