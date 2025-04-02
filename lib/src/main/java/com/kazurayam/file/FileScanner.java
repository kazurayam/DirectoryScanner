package com.kazurayam.file;

import java.nio.file.Path;
import java.util.List;

/**
 * Homage to the marvelous FileScanner of Ant
 * https://ant.apache.org/manual/api/org/apache/tools/ant/FileScanner.html
 *
 * @author kazurayam
 *
 */
public interface FileScanner {

    /**
     * Adds default exclusion to the current exclusions set
     */
    void addDefaultExcludes();

    /**
     * Return the base directory to be scanned
     * @return the Path of the base directory, absolute, normalized
     */
    Path getBasedir();

    /**
     * Returns the relative paths of the directories which matched at least one of the invcluded patterns
     * and at least one of the exclude patterns.
     * The returned Path is relative to the base directory.
     */
    List<Path> getExcludedDirectories();

    /**
     * Returns the relative paths of the files which matched at least one of the included patterns
     * and at least one of the exclude patterns.
     * The returned Path is relative to the base directory.
     */
    List<Path> getExcludedFiles();

    /**
     * Returns the relative paths of the directories which matched at least one of the included patterns
     * and none of the exclude patterns.
     * The returned Path is relative to the base directory.
     */
    List<Path> getIncludedDirectories();

    /**
     * Returns the relative paths of the files which matched at least one of the included patterns
     * and none of the exclude patterns.
     * The returned Path is relative to the base directory.
     */
    List<Path> getIncludedFiles();

    /**
     * Returns the relative paths of the directories which matched none of the include patterns
     */
    List<Path> getNotIncludedDirectories();

    /**
     * Returns the relative paths of the files which matched none of the include patterns
     */
    List<Path> getNotIncludedFiles();

    /**
     * Scans the base directory for files which match at least one include pattern
     * and don't match any exclude patterns.
     */
    void scan();

    /**
     * Sets the base directory to be scanned
     * @param basedir
     */
    void setBasedir(Path basedir);

    /**
     * Sets the list of exclude patterns to use
     */
    void setExcludes(List<String> excludes);

    /**
     * Sets the list of include patterns to use
     */
    void setIncludes(List<String> includes);

    /**
     * Set whether or not include and exlude patterns are matched in a case sensitive way
     */
    void setCaseSensitive(Boolean isCaseSensitive);
}
