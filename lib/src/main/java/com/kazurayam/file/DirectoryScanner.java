package com.kazurayam.file;

// copied from https://github.com/apache/ant/blob/master/src/main/org/apache/tools/ant/types/selectors/SelectorUtils.java
/**
 * An homage to the org.apache.tools.ant.DirectoryScanner.
 * See https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
 *
 * Class for scanning a directory for files/directories which match certain criteria.
 *
 * These criteria consists of selectors and patterns which have been specified.
 * With the selectors you can select which files you want to have included.
 * Files which are not selected are excluded.
 * With patterns you can include or exclude files based on their filename.
 *
 * The idea is simple. A given directory is recursively scanned for all files and directories.
 * Each file/directory is matched against a set of selectors,
 * including special support for matching against filenames with include and exclude patterns.
 * Only files/directories which match at least one pattern of the include pattern list
 * or other file selector, and don't match any pattern of the exclude pattern list
 * or fail to match against a required selector
 * will be placed in the list of files/directories found.
 *
 * When no list of include pattern is supplied, "**" will be used, which means that
 * everything will be matched. When no list of exclude patterns is supplied,
 * an empty list is used, such that nothing will be excluded.
 * When no selectors are supplied, none are applied.
 *
 * The filename pattern matching is done as follows: The name to be matched
 * is split up in path segments. A path segment is the name of a directory
 * or file, which is bounded by File.separator('/' under UNIX, '¥' under Windows).
 * For example, "abc/def/ghi/xyz.java" is split up in the segments "abc", "def",
 * "ghi" and "xyz.java". The same is done for the pattern against which should be matched.
 *
 * The segments of the name and the pattern are then matched against each other.
 * When '**' is used for a path segment in the pattern, it matches zero or more
 * path segments of the name
 *
 * There is a special case regarding the use of File.separators at the beginning
 * of the pattern and the string to match.
 *
 * - When a pattern starts with a File.separator, the string to match must also
 *   start with a File.separator
 * - When a pattern does not start with a File.separator, the string to match
 *   may not start with a File.separator
 * - When one of the above rules is not obeyed, the string will not match.
 *
 * When a name path segment is matched against a pattern path segment,
 * the following special characters are used:
 *
 * '*' matches zero or more characters
 * '?' matches one character.
 *
 * Examples:
 *
 * "**¥*.class" matches all .class files/dires in a directory tree.
 *
 * "test¥a??.java" matches all files/dires which start with an 'a', then two more
 * characters and then ".java", in a directory called test.
 *
 * "**" matches everything in a directory tree.
 *
 * "**¥test¥**¥XYZ*" matches all files/dirs which start with "XYZ" and where
 * there is a parent directory called test (e.g. "abc¥test¥def¥ghi¥XYZ123")
 *
 * Case sensitivity may be turned off if necessary. By default, it is turned on.
 *
 * Example of usage:
 * <code>
 *     Path baseDir = Paths.get(".").resolve("build/classes/java")
 *     List&lt;String&gt; includes = Arrays.toList("**¥¥*.class");
 *     List&lt;String&gt; excludes = Arrays.toList("test¥¥**¥¥*")
 *     DirectoryScanner ds = new DirectoryScanner()
 *     ds.setIncludes(includes);
 *     ds.setExcludes(excludes);
 *     ds.setBasedir(baseDir);
 *     ds.scan();
 *
 *     System.out.println("FILES:")
 *     List&lt;Path&gt; files 0 ds.getIncludedFiles();
 *     for (Path p : files) {
 *         System.out.println(p)
 *     }
 * </code>
 *
 * This will scan a directory "build/classes/java" under the current directory
 * for the .class files, but excludes all files in all proper subdirectories
 * of directory called "test".
 */
public class DirectoryScanner {

    public static final String DEEP_TREE_MATCH = "**";

    static boolean matchPatternsStart(String[] patDirs, String[] strDirs,
                                      boolean isCaseSensitive) {
        int patIdxStart = 0;
        int patIdxEnd = patDirs.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;

        // up to first '**'
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxStart];
            if (patDir.startsWith(DEEP_TREE_MATCH)) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }

        // Fail if string is not exhausted or pattern is exhausted
        // Otherwise the pattern now holds ** while string is not exhausted
        // this will generate false positivebut we can live with that.
        return strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd;
    }

    /**
     *
     */
    public static boolean match(String pattern, String str) {
        return match(pattern, str, true);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The patten may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern The pattern to match against. Must not be null
     * @param str The string which will be matched against the pattern. Must not be null
     * @param caseSensitive Whether or not matching should be performed case sensitively.
     * @return <code>true</code> if the string matches against the pattern,
     * or <code>false</> otherwise.
     */
    public static boolean match(String pattern, String str,
                                boolean caseSensitive) {
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;

        boolean containsStar = false;
        for (char ch : patArr) {
            if (ch == '*') {
                containsStar = true;
                break;
            }
        }

        if (!containsStar) {
            // No '*'s, so we make a shortcut
            if (patIdxEnd != strIdxEnd) {
                return false; // Patterns and string do not have the same size
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                char ch = patArr[i];
                if (ch != '?' && different(caseSensitive, ch, strArr[i])) {
                    return false; // Character mismatch
                }
            }
            return true; // String matches against pattern
        }

        if (patIdxEnd == 0) {
            return true;  // Pattern contains only '*', which matches anything
        }

        // Process characters before first star
        while (true) {
            char ch = patArr[patIdxStart];
            if (ch == '*' || strIdxStart > strIdxEnd) {
                break;
            }
            if (ch != '?'
                    && different(caseSensitive, ch, strArr[strIdxStart])) {
                return false; // Character mismatch
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string ar used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otheriwse failure.
            return allStars(patArr, patIdxStart, patIdxEnd);
        }

        // Process characters after last star
        while (true) {
            char ch = patArr[patIdxEnd];
            if (ch == '*' || strIdxStart > strIdxEnd) {
                break;
            }
            if (ch != '?' && different(caseSensitive, ch, strArr[strIdxEnd])) {
                return false; // Character mismatch
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            return allStars(patArr, patIdxStart, patIdxEnd);
        }

        // process pattern between stars. padIdxStart and patIdxEnd point
        // always to a '*'.
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                // Two stars next to each other, skip the first one.
                patIdxStart++;
                continue;
            }
            // Find the pattern between patIdxStart & patIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    char ch = patArr[patIdxStart + j + 1];
                    if (ch != '?' && different(caseSensitive, ch,
                            strArr[strIdxStart + i + j])) {
                        continue strLoop;
                    }
                    foundIdx = strIdxStart + i;
                    break;
                }
            }

            if (foundIdx == -1) {
                return false;
            }
            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        // All characters in the string are used. Check if only '*'s are left
        // in the pattern. If so, we succeeded. Otherwise failure.
        return allStars(patArr, patIdxStart, patIdxEnd);
    }

    protected static boolean allStars(char[] chars, int start, int end) {
        for (int i = start; i <= end; ++i) {
            if (chars[i] != '*') {
                return false;
            }
        }
        return true;
    }

    protected static boolean different(
        boolean caseSensitive, char ch, char other) {
        return caseSensitive
                ? ch != other
                : Character.toUpperCase(ch) != Character.toUpperCase(other);
    }
}


















